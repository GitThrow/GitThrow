package net.orekyuu.gitthrow.git.domain;

import difflib.Chunk;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import net.orekyuu.gitthrow.git.ChangeType;
import net.orekyuu.gitthrow.git.FileDiff;
import net.orekyuu.gitthrow.git.Line;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.merge.ThreeWayMerger;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

/**
 * リモートリポジトリの抽象
 */
public class RemoteRepository {

    private static final String DEFAULT_BRANCH = "master";
    private final String projectId;
    private final Path repositoryDir;

    RemoteRepository(String projectId, Path repositoryDir) {
        this.projectId = projectId;
        this.repositoryDir = repositoryDir;
    }

    public String getProjectId() {
        return projectId;
    }

    public Path getRepositoryDir() {
        return repositoryDir;
    }

    public Repository getRepository() throws IOException {
        Repository repo = new FileRepositoryBuilder()
            .setGitDir(repositoryDir.toFile())
            .setBare()
            .build();
        repo.incrementOpen();
        return repo;
    }

    /**
     * リモートリポジトリを初期化する
     * @throws IOException
     */
    public void init() throws IOException {
        if (Files.exists(repositoryDir)) {
            throw new IOException("すでに存在している: " + projectId);
        }

        Files.createDirectories(repositoryDir);

        Repository repo = new FileRepositoryBuilder()
            .setGitDir(repositoryDir.toFile())
            .setBare()
            .build();
        final boolean isBare = true;
        repo.create(isBare);

        StoredConfig config = repo.getConfig();
        config.setBoolean("http", null, "receivepack", true);
        config.save();
    }

    /**
     * リモートリポジトリを削除する
     */
    public void delete() {
        if (Files.notExists(repositoryDir)) {
            return;
        }
        try {

            Files.walkFileTree(repositoryDir, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//                    Files.setAttribute(file, "dos:readonly",false);
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }

            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * デフォルトブランチのREADME.mdを取得する
     * @return README.mdの内容
     * @throws GitAPIException
     */
    public Optional<String> getReadmeFile() throws GitAPIException {

        try (Repository repository = getRepository(); Git git = new Git(repository)) {
            // ブランチの一覧を取得
            List<Ref> branchResult = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();

            String targetBranch = "refs/heads/" + DEFAULT_BRANCH;

            // デフォルトブランチがなければ何もしない(まっさらなリポジトリとかの状況でありえる)
            if (branchResult.stream().noneMatch(ref -> ref.getName().equals(targetBranch))) {
                return Optional.empty();
            }
            return getRepositoryFile(targetBranch, "README.md").map((bytes) -> bytes.toString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * リポジトリ内にあるファイルのByteArrayOutputStreamを返します
     * @param hash 対象のハッシュ
     * @param relativePath プロジェクトから見た相対パス
     * @return 見つけたコンテンツのByteArrayOutputStream
     * @throws GitAPIException
     */
    public Optional<ByteArrayOutputStream> getRepositoryFile(String hash, String relativePath) throws GitAPIException {
        try (Repository repository = getRepository()) {
            // ハッシュかその他で分岐
            // タグとかブランチも食える模様
            ObjectId commitId = ObjectId.isId(hash)?ObjectId.fromString(hash):repository.resolve(hash);
            if(commitId==null){
                return Optional.empty();
            }
            try (RevWalk revWalk = new RevWalk(repository)) {
                RevCommit commit = revWalk.parseCommit(commitId);
                RevTree tree = commit.getTree();

                try (TreeWalk treeWalk = new TreeWalk(repository)) {
                    treeWalk.addTree(tree);
                    treeWalk.setRecursive(true);
                    treeWalk.setFilter(PathFilter.create(relativePath));

                    boolean found=false;
                    while (treeWalk.next()) {
                        if(Objects.equals(treeWalk.getPathString(),relativePath)){
                            found=true;
                            break;
                        }
                    }
                    if(!found)return Optional.empty();

                    ObjectId objectId = treeWalk.getObjectId(0);
                    ObjectLoader objectLoader = repository.open(objectId);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    objectLoader.copyTo(outputStream);
                    revWalk.dispose();
                    return Optional.of(outputStream);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public List<DiffEntry> diff(String baseBranch, String targetBranch) throws GitAPIException {
        try (Repository repository = getRepository()) {
            Git git = new Git(repository);
            AbstractTreeIterator baseTree = createTreeParser(repository, baseBranch);
            AbstractTreeIterator targetTree = createTreeParser(repository, targetBranch);

            return git.diff()
                .setOldTree(baseTree)
                .setNewTree(targetTree)
                .call();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public FileDiff calcFileDiff(DiffEntry entry) throws GitAPIException {
        try (Repository repository = getRepository()) {

            //ファイルの内容を持ってくる
            byte[] newContentBytes = entry.getNewPath().equals("/dev/null") ? null :readContentFromId(repository, entry.getNewId().toObjectId());
            byte[] oldContentBytes = entry.getOldPath().equals("/dev/null") ? null : readContentFromId(repository, entry.getOldId().toObjectId());

            //文字列にする。文字列出なかった場合はnull
            String newContent = (newContentBytes != null && isText(newContentBytes)) ? new String(newContentBytes) : null;
            String oldContent = (oldContentBytes != null && isText(oldContentBytes)) ? new String(oldContentBytes) : null;

            //行で分割したListに変換。文字列でない場合は空のリスト
            List<String> newContentList = newContent == null ? new ArrayList<>() : Arrays.stream(newContent.split("\n")).collect(Collectors.toList());
            List<String> oldContentList = oldContent == null ? new ArrayList<>() : Arrays.stream(oldContent.split("\n")).collect(Collectors.toList());

            //Diffを出す
            Patch<String> patch = DiffUtils.diff(newContentList, oldContentList);
            List<Delta<String>> deltas = patch.getDeltas();

            //差分をパースしたやつ
            List<Line> lines = new LinkedList<>();

            int oldLineNum = 0; //変更前の行番号
            int newLineNum = 0; //変更後の行番号

            //変更があった要素のループ
            for (Delta<String> delta : deltas) {
                Chunk<String> oldChunk = delta.getRevised();
                Chunk<String> newChunk = delta.getOriginal();

                //変更がある行までは変更なしの行として追加
                while (oldLineNum < oldChunk.getPosition() && newLineNum < newChunk.getPosition()) {
                    lines.add(new Line(oldLineNum + 1, newLineNum + 1, oldContentList.get(oldLineNum), ChangeType.EQUAL));
                    //次の行へ
                    oldLineNum++;
                    newLineNum++;
                }

                //古い方にチャンクがある場合は削除
                for (String oldLine : oldChunk.getLines()) {
                    lines.add(new Line(oldLineNum + 1, Line.NULL_LINE_NUMBER, oldLine, ChangeType.DELETE));
                    oldLineNum++;
                }

                //新しい方にチャンクがある場合は追加
                for (String newLine : newChunk.getLines()) {
                    lines.add(new Line(Line.NULL_LINE_NUMBER, newLineNum + 1, newLine, ChangeType.ADD));
                    newLineNum++;
                }
            }

            //リストの要素数になるまでループ
            while(oldLineNum < oldContentList.size() && newLineNum < newContentList.size()) {
                lines.add(new Line(oldLineNum + 1, newLineNum + 1, oldContentList.get(oldLineNum), ChangeType.EQUAL));
                oldLineNum++;
                newLineNum++;
            }

            String fileName = entry.getNewPath().equals("/dev/null") ? entry.getOldPath() : entry.getNewPath();
            return new FileDiff(fileName, lines);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean checkConflict(String baseBranch, String targetBranch) {
        try (Repository repository = getRepository()) {
            ObjectId base = repository.resolve(baseBranch);
            ObjectId target = repository.resolve(targetBranch);

            ThreeWayMerger merger = MergeStrategy.RECURSIVE.newMerger(repository, true);
            return merger.merge(base, target);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private boolean isText(byte[] bytes) {
        try {
            //TODO つらい！
            //文字列に変換できるか
            new String(bytes);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private byte[] readContentFromId(Repository repository, ObjectId objectId) throws IOException {
        ObjectDatabase objectDatabase = null;
        try {
            objectDatabase = repository.getObjectDatabase();
            ObjectLoader objectLoader = objectDatabase.open(objectId);
            return objectLoader.getBytes();
        } finally {
            if (objectDatabase != null) {
                objectDatabase.close();
            }
        }
    }

    private AbstractTreeIterator createTreeParser(Repository repo, String ref) throws IOException {
        ObjectId objectId = repo.resolve(ref).toObjectId();
        try (RevWalk walk = new RevWalk(repo)) {
            RevCommit commit = walk.parseCommit(objectId);
            RevTree tree = commit.getTree();

            CanonicalTreeParser parser = new CanonicalTreeParser();
            try(ObjectReader reader = repo.newObjectReader()) {
                parser.reset(reader, tree.getId());
            }
            return parser;
        }
    }

    public List<String> findBranch() {
        try (Repository repository = getRepository()) {
            Git git = new Git(repository);
            return git.branchList().call().stream().map(Ref::getName).collect(Collectors.toList());
        } catch (IOException | GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoteRepository that = (RemoteRepository) o;
        return Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId);
    }
}
