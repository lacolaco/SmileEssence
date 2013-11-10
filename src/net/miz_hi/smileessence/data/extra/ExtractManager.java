package net.miz_hi.smileessence.data.extra;

import java.util.List;

public class ExtractManager
{

    public static void addExtractWord(String extractWord)
    {
        addExtractWord(new ExtraWord(extractWord));
    }

    public static void addExtractWord(ExtraWord extractWord)
    {
        ExtractModel.instance().save(extractWord);
    }

    public static void deleteExtraWord(ExtraWord extractWord)
    {
        ExtractModel.instance().delete(extractWord);
    }

    public static List<ExtraWord> getExtraWords()
    {
        return ExtractModel.instance().findAll();
    }
}
