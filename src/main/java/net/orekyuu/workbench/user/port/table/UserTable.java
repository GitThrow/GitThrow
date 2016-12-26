package net.orekyuu.workbench.user.port.table;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import java.util.Objects;

@Entity(immutable = true)
@Table(name = "users")
public class UserTable {

    @Id
    @Column(name = "id")
    private final String id;
    @Column(name = "name")
    private final String name;
    @Column(name = "password")
    private final String password;
    @Column(name = "email")
    private final String email;
    @Column(name = "admin")
    private final boolean admin;

    public UserTable(String id, String name, String password, String email, boolean admin) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.admin = admin;
    }

    public UserTable(String id, String name, String password, boolean admin) {
        this(id, name, password, "", admin);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdmin() {
        return admin;
    }

    @Override
    public String toString() {
        return "UserTable{" +
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
        UserTable user = (UserTable) o;
        return Objects.equals(id, user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
