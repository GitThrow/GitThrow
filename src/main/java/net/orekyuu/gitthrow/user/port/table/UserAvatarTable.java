package net.orekyuu.gitthrow.user.port.table;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import java.util.Objects;

@Entity(immutable = true)
@Table(name = "user_avatar")
public class UserAvatarTable {

    @Id
    @Column(name = "id")
    private final String id;
    @Column(name = "avatar")
    private final byte[] avatar;

    public UserAvatarTable(String id, byte[] avatar) {
        this.id = id;
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAvatarTable that = (UserAvatarTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
