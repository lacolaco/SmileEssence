package net.lacolaco.smileessence.entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Hashtag")
public class SavedHashtag extends Model
{

    @Column(name = "Text", notNull = true)
    public String text;

    public SavedHashtag()
    {
        super();
    }

    public SavedHashtag(String text)
    {
        super();
        this.text = text;
    }
}
