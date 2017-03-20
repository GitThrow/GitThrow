package net.orekyuu.gitthrow.project.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import net.orekyuu.gitthrow.project.domain.policy.ProjectMemberPolicy;
import net.orekyuu.gitthrow.project.domain.policy.ProjectNamePolicy;
import net.orekyuu.gitthrow.user.domain.model.User;
import net.orekyuu.gitthrow.util.PolicyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Project {

    private final String id;
    private String name;
    private User owner;

    private final List<User> member;

    private static final Logger logger = LoggerFactory.getLogger(Project.class);

    private ProjectMemberPolicy projectMemberPolicy = new ProjectMemberPolicy(this);
    private ProjectNamePolicy projectNamePolicy = new ProjectNamePolicy();

    @JsonCreator
    public Project(String id, String name, User owner, List<User> member) {
        if (!projectNamePolicy.check(name)) {
            throw new PolicyException();
        }
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.member = new ArrayList<>(member);
    }

    /**
     * プロジェクトオーナーを変更する
     * @param user 新しいオーナー
     */
    public void changeProjectOwner(User user) {
        Objects.requireNonNull(user, "user is null.");
        if (!projectMemberPolicy.check(user)) {
            throw new PolicyException();
        }
        this.owner = user;
    }

    /**
     * プロジェクト名を変更する
     * @param name プロジェクト名
     */
    public void renameProject(String name) {
        if (!projectNamePolicy.check(name)) {
            throw new PolicyException();
        }
        this.name = name;
    }

    /***
     * @return プロジェクトメンバー
     */
    public List<User> getMember() {
        //botは弾く
        return member.stream().filter(user -> !user.isBotUser()).collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getOwner() {
        return owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Project{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", owner=" + owner +
            ", member=" + member +
            '}';
    }
}
