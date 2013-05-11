package net.miz_hi.smileessence.data.extra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExtraWords
{
		
	private static List<ExtraWord> extraWords = new ArrayList<ExtraWord>();
	
	static
	{
		read();
	}
	
	private static void read()
	{
		extraWords.clear();
		if(ExtraWordDB.instance().findAll() != null)
		{
			for(ExtraWord ExtraWord : ExtraWordDB.instance().findAll())
			{
				extraWords.add(ExtraWord);
			}
		}
	}
	
	public static void update()
	{
		ExtraWordDB.instance().deleteAll();
		for(ExtraWord ExtraWord : extraWords)
		{
			ExtraWordDB.instance().save(ExtraWord);
		}
		read();
	}

	public static void addExtraWord(String ExtraWord)
	{
		extraWords.add(new ExtraWord(ExtraWord));
		update();
	}
	
	public static void addExtraWord(ExtraWord ExtraWord)
	{
		extraWords.add(ExtraWord);
		update();
	}


	public static void setExtraWord(String ExtraWord, int index)
	{
		extraWords.set(index, new ExtraWord(ExtraWord));
		update();
	}
	
	public static void setExtraWord(ExtraWord ExtraWord, int index)
	{
		extraWords.set(index, ExtraWord);
		update();
	}
	
	public static void deleteExtraWord(ExtraWord ExtraWord)
	{
		extraWords.remove(ExtraWord);
		update();
	}
	
	public static List<ExtraWord> getExtraWords()
	{
		return Collections.unmodifiableList(extraWords);
	}
}
