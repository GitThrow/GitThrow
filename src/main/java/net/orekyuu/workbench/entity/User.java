package net.orekyuu.workbench.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;

@Entity
public class User {

    @Id
    public String id;
    public String name;
    public String password;
}
