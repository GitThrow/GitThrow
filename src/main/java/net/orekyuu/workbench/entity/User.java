package net.orekyuu.workbench.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    public String id;
    public String name;
    public String password;
    public boolean admin;

    public User() {
    }

    public User(String id, String name, String password, boolean admin) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.admin = admin;
    }
}
