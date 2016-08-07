package net.orekyuu.workbench.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

@Entity
@Table(name = "user_avatar")
public class UserAvatar {

    @Id
    @Column(name = "id")
    public String id;
    @Column(name = "avatar")
    public byte[] avatar;
}
