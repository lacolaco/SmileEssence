package net.lacolaco.smileessence.entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Extraction")
public class ExtractionWord extends Model
{

    @Column(name = "Text", notNull = true)
    public String text;

    public ExtractionWord()
    {
        super();
    }

    public ExtractionWord(String text)
    {
        super();
        this.text = text;
    }
}
