package net.orekyuu.workbench.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

@Entity
@Table(name = "user_setting")
public class UserSetting {

    @Id
    @Column(name = "id")
    public String id;

    @Column(name = "gravatar")
    public boolean useGravatar;

}
