package net.miz_hi.smileessence.data.extra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExtraWords
{
		
	private static List<ExtraWord> ExtraWords = new ArrayList<ExtraWord>();
	
	static
	{
		read();
	}
	
	private static void read()
	{
		ExtraWords.clear();
		if(ExtraWordDB.instance().findAll() != null)
		{
			for(ExtraWord ExtraWord : ExtraWordDB.instance().findAll())
			{
				ExtraWords.add(ExtraWord);
			}
		}
	}
	
	public static void update()
	{
		ExtraWordDB.instance().deleteAll();
		for(ExtraWord ExtraWord : ExtraWords)
		{
			ExtraWordDB.instance().save(ExtraWord);
		}
		read();
	}

	public static void addExtraWord(String ExtraWord)
	{
		ExtraWords.add(new ExtraWord(ExtraWord));
		update();
	}
	
	public static void addExtraWord(ExtraWord ExtraWord)
	{
		ExtraWords.add(ExtraWord);
		update();
	}


	public static void setExtraWord(String ExtraWord, int index)
	{
		ExtraWords.set(index, new ExtraWord(ExtraWord));
		update();
	}
	
	public static void setExtraWord(ExtraWord ExtraWord, int index)
	{
		ExtraWords.set(index, ExtraWord);
		update();
	}
	
	public static void deleteExtraWord(ExtraWord ExtraWord)
	{
		ExtraWords.remove(ExtraWord);
		update();
	}
	
	public static List<ExtraWord> getExtraWords()
	{
		return Collections.unmodifiableList(ExtraWords);
	}
}
