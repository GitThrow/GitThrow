package net.orekyuu.workbench.controller.rest.model;

import net.orekyuu.workbench.entity.User;

public class UserModel {

    private String id;
    private String name;
    private String mail;
    private boolean admin;

    public UserModel(User user) {
        id = user.id;
        name = user.name;
        mail = user.email;
        admin = user.admin;
    }

    public UserModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "UserModel{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", mail='" + mail + '\'' +
            ", admin=" + admin +
            '}';
    }
}
