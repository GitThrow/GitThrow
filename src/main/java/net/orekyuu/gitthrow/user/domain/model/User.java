package net.orekyuu.gitthrow.user.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.orekyuu.gitthrow.user.util.BotUserUtil;

import java.util.Objects;

public final class User {

    private final String id;
    private String name;
    private String email;
    private boolean admin;
    private UserSettings userSettings;

    @JsonCreator
    public User(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("email") String email,
        @JsonProperty("admin") boolean admin,
        @JsonProperty("settings") UserSettings settings) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.admin = admin;
        this.userSettings = settings;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public boolean isBotUser() {
        return BotUserUtil.isBotUserId(id);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(UserSettings userSettings) {
        this.userSettings = userSettings;
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

    @Override
    public String toString() {
        return "User{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", admin=" + admin +
            ", userSettings=" + userSettings +
            '}';
    }
}
