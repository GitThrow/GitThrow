package net.orekyuu.workbench.entity;

import net.orekyuu.workbench.util.BotUserUtil;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    public String id;
    @Column(name = "name")
    public String name;
    @Column(name = "password")
    public String password;
    @Column(name = "email")
    public String email;
    @Column(name = "admin")
    public boolean admin;

    public User() {
    }

    public User(String id, String name, String password, boolean admin) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email="";
        this.admin = admin;
    }

    public boolean isBotUser() {
        return BotUserUtil.isBotUserId(id);
    }

    @Override
    public String toString() {
        return "User{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", password='" + password + '\'' +
            ", admin=" + admin +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
