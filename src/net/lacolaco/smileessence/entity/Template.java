package net.lacolaco.smileessence.entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Templates")
public class Template extends Model
{

    @Column(name = "Text", notNull = true)
    public String text;
    @Column(name = "Position")
    public int position;

    public Template()
    {
        super();
    }

    public Template(String text, int position)
    {
        super();
        this.text = text;
        this.position = position;
    }
}
