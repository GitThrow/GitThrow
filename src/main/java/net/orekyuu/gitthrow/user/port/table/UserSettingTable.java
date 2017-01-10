package net.orekyuu.gitthrow.user.port.table;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import java.util.Objects;

@Entity(immutable = true)
@Table(name = "user_setting")
public class UserSettingTable {

    @Id
    @Column(name = "id")
    private final String id;

    @Column(name = "gravatar")
    private final boolean useGravatar;

    public UserSettingTable(String id, boolean useGravatar) {
        this.id = id;
        this.useGravatar = useGravatar;
    }

    public String getId() {
        return id;
    }

    public boolean isUseGravatar() {
        return useGravatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSettingTable that = (UserSettingTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
