package net.lacolaco.smileessence.entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Commands")
public class CommandSetting extends Model
{

    @Column(name = "CommandID")
    public int commandID;
    @Column(name = "Visibility")
    public boolean visibility;

    public CommandSetting()
    {
        super();
    }

    public CommandSetting(int commandID, boolean visibility)
    {
        super();
        this.commandID = commandID;
        this.visibility = visibility;
    }
}
