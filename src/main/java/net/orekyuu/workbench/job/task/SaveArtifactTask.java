package net.orekyuu.workbench.job.task;

import net.orekyuu.workbench.job.JobMessenger;
import net.orekyuu.workbench.job.JobWorkspaceService;
import net.orekyuu.workbench.job.message.LogMessage;
import net.orekyuu.workbench.service.ArtifactService;
import net.orekyuu.workbench.service.WorkbenchConfig;
import net.orekyuu.workbench.service.WorkbenchConfigService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@Scope("prototype")
public class SaveArtifactTask implements Task {

    private static final Logger logger = LoggerFactory.getLogger(SaveArtifactTask.class);

    @Autowired
    private ArtifactService artifactService;
    @Autowired
    private WorkbenchConfigService workbenchConfigService;
    @Autowired
    private JobWorkspaceService jobWorkspaceService;

    @Override
    public boolean process(JobMessenger messenger, TaskArguments args) throws Exception {

        String projectId = args.getProjectId();
        Optional<WorkbenchConfig> configOpt = workbenchConfigService.find(projectId, "HEAD");
        if (!configOpt.isPresent()) {
            return true;
        }
        WorkbenchConfig config = configOpt.get();
        List<String> artifactPathList = config.getBuildSettings().getArtifactPath();
        //空なら保存対象無し
        if (artifactPathList == null || artifactPathList.isEmpty()) {
            return true;
        }

        Path workspacePath = jobWorkspaceService.getWorkspacePath(args.getJobId());

        List<Path> files = artifactPathList.stream()
            .flatMap(s -> {
                try {
                    return findFiles(workspacePath, s).stream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return Stream.empty();
            }).collect(Collectors.toList());

        if (files.isEmpty()) {
            return true;
        }

        files.stream()
            .map(path -> path.getFileName().getFileName())
            .forEach(str -> messenger.send(new LogMessage("Save: " + str)));

        Pair<String, byte[]> pair = toByteArray(files);
        if (pair != null) {
            artifactService.save(projectId, pair.getRight(), pair.getLeft());
        }

        return true;
    }

    /**
     * 設定の正規表現にマッチするファイルのPathを抽出する
     *
     * @param dir ワークスペースのPath
     * @param reg 正規表現
     * @return 正規表現に一致するファイルのリスト
     * @throws IOException
     */
    private List<Path> findFiles(Path dir, String reg) throws IOException {
        String separator = File.separator;
        //Winなら正規表現のマッチングに使うためにエスケープ
        if (separator.equals("\\")) {
            separator += File.separator;
        }
        String regStr = Matcher.quoteReplacement(dir.toString()) + separator + reg;

        return Files.walk(dir)
            .filter(child -> Files.isRegularFile(child))
            .filter(child -> child.toString().matches(regStr))
            .collect(Collectors.toList());
    }

    private Pair<String, byte[]> toByteArray(List<Path> files) {
        if (files.isEmpty()) {
            return null;
        }

        if (files.size() == 1) {
            Path path = files.get(0);
            try {
                return new ImmutablePair<>(path.getFileName().toString(), Files.readAllBytes(path));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(outputStream);) {
            for (Path file : files) {
                ZipEntry entry = new ZipEntry("result/" + file.getFileName().toString());
                zos.putNextEntry(entry);
                try (InputStream in = Files.newInputStream(file)) {
                    IOUtils.copy(in, zos);
                }
                zos.closeEntry();
            }
            zos.close();
            return new ImmutablePair<>("result.zip", outputStream.toByteArray());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
