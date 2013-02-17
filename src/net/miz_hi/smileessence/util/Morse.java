package net.miz_hi.smileessence.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;

/**
 * òaï∂ÉÇÅ[ÉãÉXïœä∑ÉâÉCÉuÉâÉä
 * @author flour
 * @version 2.2
 * @arrange laco0416
 */
public class Morse
{

	private static HashMap<String, String> jaMc;
	private static HashMap<String, String> mcJa;

	/**
	 * ó^Ç¶ÇÁÇÍÇΩï∂éöóÒÇ™ÉÇÅ[ÉãÉXï∂Çä‹ÇﬁÇ©Ç«Ç§Ç©Çï‘Ç∑
	 * @param mc îªíËÇ∑ÇÈï∂éöóÒ
	 * @return ÉÇÅ[ÉãÉXÇä‹ÇﬁÇ»ÇÁtrue,ÇªÇ§Ç≈Ç»ÇØÇÍÇŒfalse
	 */
	public static boolean isMorse(String mc)
	{
		Pattern pattern = Pattern.compile("[Å|ÅE]+");
		Matcher matcher = pattern.matcher(mc);
		ArrayList<String> list = new ArrayList<String>();
		while(matcher.find())
		{
			list.add(matcher.group());
		}
		if(list.size() <= 1)
		{
			return false;
		}
		else
		{
			for(String s: list)
			{
				if(!s.equals("ÅEÅEÅE") && !s.equals("ÅEÅE") && !s.equals("ÅE"))
				{
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * òaï∂ÉÇÅ[ÉãÉXÇÉJÉ^ÉJÉiÅEêîéöÇ…ïúå≥Ç∑ÇÈ	 *
	 * @param str ïúå≥ÇµÇΩÇ¢ï∂éöóÒ
	 * @return ïúå≥ïîï™Ç™íuä∑Ç≥ÇÍÇΩï∂éöóÒ
	 */
	public static String mcToJa(String str)
	{
		String[] strArr = toRightMorse(str).split(" ");
		StringBuilder sb = new StringBuilder();
		for (String tok : strArr)
		{
			sb.append(mcJa.containsKey(tok) ? mcJa.get(tok) : tok);
		}
		return sb.toString();
	}

	/**
	 * Ç–ÇÁÇ™Ç»ÅEÉJÉ^ÉJÉiÅEêîéöÇÉÇÅ[ÉãÉXï∂Ç…ïœä∑Ç∑ÇÈ
	 * @param str ïœä∑ÇµÇΩÇ¢ï∂éöóÒ
	 * @return ïœä∑ïîï™Ç™íuä∑Ç≥ÇÍÇΩï∂éöóÒ
	 */
	public static String jaToMc(String str)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++)
		{
			String tok = String.valueOf(str.charAt(i));
			if(jaMc.containsKey(tok))
			{
				sb.append(jaMc.get(tok));
				sb.append(" ");
			}
			else
			{
				sb.append(tok);
			}
		}
		return sb.toString().trim();
	}


	private static String toRightMorse(String str)
	{
		str = str.replace("Å]", "Å|").replace("Å@", " ").trim();
		Pattern pattern = Pattern.compile("[^ÅEÅ| ][ÅEÅ|]");
		StringBuilder sb = new StringBuilder(str);
		Matcher matcher = pattern.matcher(str);
		while(matcher.find())
		{
			int i = matcher.start();
			sb.insert(i + 1, " ");
			matcher.reset(sb);
		}
		pattern = Pattern.compile("[ÅEÅ|][^ÅEÅ| ]");
		matcher = pattern.matcher(sb);
		while(matcher.find())
		{
			int i = matcher.start();
			sb.insert(i + 1, " ");
			matcher.reset(sb);
		}
		return sb.toString();
	}

	static
	{
		jaMc = new HashMap<String, String>();
		mcJa = new HashMap<String, String>();

		String[][] ja2 = {{"ÅEÅ|", "ÉC"}, {"ÅEÅ|ÅEÅ|", "Éç"}, {"Å|ÅEÅEÅE", "Én"}, {"Å|ÅEÅ|ÅE", "Éj"}, {"Å|ÅEÅE", "Éz"},
				{"ÅE", "Éw"}, {"ÅEÅEÅ|ÅEÅE", "Ég"}, {"ÅEÅEÅ|ÅE", "É`"}, {"Å|Å|ÅE", "Éä"}, {"ÅEÅEÅEÅE", "Ék"},
				{"Å|ÅEÅ|Å|ÅE", "Éã"}, {"ÅEÅ|Å|Å|", "Éí"}, {"Å|ÅEÅ|", "Éè"}, {"ÅEÅ|ÅEÅE", "ÉJ"}, {"Å|Å|", "Éà"},
				{"Å|ÅE", "É^"}, {"Å|Å|Å|", "Éå"}, {"Å|Å|Å|ÅE", "É\"}, {"ÅEÅ|Å|ÅE", "Éc"}, {"Å|Å|ÅEÅ|", "Él"},
				{"ÅEÅ|ÅE", "Éi"}, {"ÅEÅEÅE", "Éâ"}, {"Å|", "ÉÄ"}, {"ÅEÅEÅ|", "ÉE"}, {"ÅEÅ|ÅEÅEÅ|", "Éê"},
				{"ÅEÅEÅ|Å|", "Ém"}, {"ÅEÅ|ÅEÅEÅE", "ÉI"}, {"ÅEÅEÅEÅ|", "ÉN"}, {"ÅEÅ|Å|", "ÉÑ"}, {"Å|ÅEÅEÅ|", "É}"},
				{"Å|ÅEÅ|Å|", "ÉP"}, {"Å|Å|ÅEÅE", "Ét"}, {"Å|Å|Å|Å|", "ÉR"}, {"Å|ÅEÅ|Å|Å|", "ÉG"}, {"ÅEÅ|ÅEÅ|Å|", "Ée"},
				{"Å|Å|ÅEÅ|Å|", "ÉA"}, {"Å|ÅEÅ|ÅEÅ|", "ÉT"}, {"Å|ÅEÅ|ÅEÅE", "ÉL"}, {"Å|ÅEÅEÅ|Å|", "ÉÜ"}, {"Å|ÅEÅEÅEÅ|", "ÉÅ"},
				{"ÅEÅEÅ|ÅEÅ|", "É~"}, {"Å|Å|ÅEÅ|ÅE", "ÉV"}, {"ÅEÅ|Å|ÅEÅE", "Éë"}, {"Å|Å|ÅEÅEÅ|", "Éq"}, {"Å|ÅEÅEÅ|ÅE", "ÉÇ"},
				{"ÅEÅ|Å|Å|ÅE", "ÉZ"}, {"Å|Å|Å|ÅEÅ|", "ÉX"}, {"ÅEÅ|ÅEÅ|ÅE", "Éì"}, {"ÅEÅE", "ÅJ"}, {"ÅEÅEÅ|Å|ÅE", "ÅK"},
				{"ÅEÅ|Å|ÅEÅ|", "Å["}, {"ÅEÅ|ÅEÅ|ÅEÅ|", "ÅA"}, {"Å|ÅEÅ|Å|ÅEÅ|", "Åi"}, {"ÅEÅ|ÅEÅEÅ|ÅE", "Åj"}, {"ÅEÅ|Å|Å|Å|", "1"},
				{"ÅEÅEÅ|Å|Å|", "2"}, {"ÅEÅEÅEÅ|Å|", "3"}, {"ÅEÅEÅEÅEÅ|", "4"}, {"ÅEÅEÅEÅEÅE", "5"}, {"Å|ÅEÅEÅEÅE", "6"},
				{"Å|Å|ÅEÅEÅE", "7"}, {"Å|Å|Å|ÅEÅE", "8"}, {"Å|Å|Å|Å|ÅE", "9"}, {"Å|Å|Å|Å|Å|", "0"}, {"", ""}};
		for(String[] pr : ja2)
		{
			mcJa.put(pr[0], pr[1]);
			jaMc.put(pr[1], pr[0]);
		}

		String[][] ja1 = {{"Ç¢", "ÅEÅ|"}, {"ÉB", "ÅEÅ|"}, {"Ç°", "ÅEÅ|"}, {"ÇÎ", "ÅEÅ|ÅEÅ|"}, {"ÇÕ", "Å|ÅEÅEÅE"},
				{"Ç…", "Å|ÅEÅ|ÅE"}, {"ÇŸ", "Å|ÅEÅE"}, {"Ç÷", "ÅE"}, {"Ç∆", "ÅEÅEÅ|ÅEÅE"}, {"Çø", "ÅEÅEÅ|ÅE"},
				{"ÇË", "Å|Å|ÅE"}, {"Ç ", "ÅEÅEÅEÅE"}, {"ÇÈ", "Å|ÅEÅ|Å|ÅE"}, {"Ç", "ÅEÅ|Å|Å|"}, {"ÇÌ", "Å|ÅEÅ|"},
				{"Éé", "Å|ÅEÅ|"}, {"ÇÏ", "Å|ÅEÅ|"}, {"Ç©", "ÅEÅ|ÅEÅE"}, {"Éï", "ÅEÅ|ÅEÅE"}, {"ÇÊ", "Å|Å|"},
				{"Éá", "Å|Å|"}, {"ÇÂ", "Å|Å|"}, {"ÇΩ", "Å|ÅE"}, {"ÇÍ", "Å|Å|Å|"}, {"Çª", "Å|Å|Å|ÅE"},
				{"Ç¬", "ÅEÅ|Å|ÅE"}, {"Éb", "ÅEÅ|Å|ÅE"}, {"Ç¡", "ÅEÅ|Å|ÅE"}, {"ÇÀ", "Å|Å|ÅEÅ|"}, {"Ç»", "ÅEÅ|ÅE"},
				{"ÇÁ", "ÅEÅEÅE"}, {"Çﬁ", "Å|"}, {"Ç§", "ÅEÅEÅ|"}, {"ÉD", "ÅEÅEÅ|"}, {"Ç£", "ÅEÅEÅ|"},
				{"ÇÓ", "ÅEÅ|ÅEÅEÅ|"}, {"ÇÃ", "ÅEÅEÅ|Å|"}, {"Ç®", "ÅEÅ|ÅEÅEÅE"}, {"ÉH", "ÅEÅ|ÅEÅEÅE"}, {"Çß", "ÅEÅ|ÅEÅEÅE"},
				{"Ç≠", "ÅEÅEÅEÅ|"}, {"Ç‚", "ÅEÅ|Å|"}, {"ÉÉ", "ÅEÅ|Å|"}, {"Ç·", "ÅEÅ|Å|"}, {"Ç‹", "Å|ÅEÅEÅ|"},
				{"ÇØ", "Å|ÅEÅ|Å|"}, {"Éñ", "Å|ÅEÅ|Å|"}, {"Ç”", "Å|Å|ÅEÅE"}, {"Ç±", "Å|Å|Å|Å|"}, {"Ç¶", "Å|ÅEÅ|Å|Å|"},
				{"ÉF", "Å|ÅEÅ|Å|Å|"}, {"Ç•", "Å|ÅEÅ|Å|Å|"}, {"Çƒ", "ÅEÅ|ÅEÅ|Å|"}, {"Ç†", "Å|Å|ÅEÅ|Å|"}, {"É@", "Å|Å|ÅEÅ|Å|"},
				{"Çü", "Å|Å|ÅEÅ|Å|"}, {"Ç≥", "Å|ÅEÅ|ÅEÅ|"}, {"Ç´", "Å|ÅEÅ|ÅEÅE"}, {"Ç‰", "Å|ÅEÅEÅ|Å|"}, {"ÉÖ", "Å|ÅEÅEÅ|Å|"},
				{"Ç„", "Å|ÅEÅEÅ|Å|"}, {"Çﬂ", "Å|ÅEÅEÅEÅ|"}, {"Ç›", "ÅEÅEÅ|ÅEÅ|"}, {"Çµ", "Å|Å|ÅEÅ|ÅE"}, {"ÇÔ", "ÅEÅ|Å|ÅEÅE"},
				{"Ç–", "Å|Å|ÅEÅEÅ|"}, {"Ç‡", "Å|ÅEÅEÅ|ÅE"}, {"Çπ", "ÅEÅ|Å|Å|ÅE"}, {"Ç∑", "Å|Å|Å|ÅEÅ|"}, {"ÇÒ", "ÅEÅ|ÅEÅ|ÅE"},
				{"ÉK", "ÅEÅ|ÅEÅE ÅEÅE"}, {"Ç™", "ÅEÅ|ÅEÅE ÅEÅE"}, {"ÉM", "Å|ÅEÅ|ÅEÅE ÅEÅE"}, {"Ç¨", "Å|ÅEÅ|ÅEÅE ÅEÅE"}, {"ÉO", "ÅEÅEÅEÅ| ÅEÅE"},
				{"ÇÆ", "ÅEÅEÅEÅ| ÅEÅE"}, {"ÉQ", "Å|ÅEÅ|Å| ÅEÅE"}, {"Ç∞", "Å|ÅEÅ|Å| ÅEÅE"}, {"ÉS", "Å|Å|Å|Å| ÅEÅE"}, {"Ç≤", "Å|Å|Å|Å| ÅEÅE"},
				{"ÉU", "Å|ÅEÅ|ÅEÅ| ÅEÅE"}, {"Ç¥", "Å|ÅEÅ|ÅEÅ| ÅEÅE"}, {"ÉW", "Å|Å|ÅEÅ|ÅE ÅEÅE"}, {"Ç∂", "Å|Å|ÅEÅ|ÅE ÅEÅE"}, {"ÉY", "Å|Å|Å|ÅEÅ| ÅEÅE"},
				{"Ç∏", "Å|Å|Å|ÅEÅ| ÅEÅE"}, {"É[", "ÅEÅ|Å|Å|ÅE ÅEÅE"}, {"Ç∫", "ÅEÅ|Å|Å|ÅE ÅEÅE"}, {"É]", "Å|Å|Å|ÅE ÅEÅE"}, {"Çº", "Å|Å|Å|ÅE ÅEÅE"},
				{"É_", "Å|ÅE ÅEÅE"}, {"Çæ", "Å|ÅE ÅEÅE"}, {"Éa", "ÅEÅEÅ|ÅE ÅEÅE"}, {"Ç¿", "ÅEÅEÅ|ÅE ÅEÅE"}, {"Éd", "ÅEÅ|Å|ÅE ÅEÅE"},
				{"Ç√", "ÅEÅ|Å|ÅE ÅEÅE"}, {"Éf", "ÅEÅ|ÅEÅ|Å| ÅEÅE"}, {"Ç≈", "ÅEÅ|ÅEÅ|Å| ÅEÅE"}, {"Éh", "ÅEÅEÅ|ÅEÅE ÅEÅE"}, {"Ç«", "ÅEÅEÅ|ÅEÅE ÅEÅE"},
				{"Éo", "Å|ÅEÅEÅE ÅEÅE"}, {"ÇŒ", "Å|ÅEÅEÅE ÅEÅE"}, {"Ér", "Å|Å|ÅEÅEÅ| ÅEÅE"}, {"Ç—", "Å|Å|ÅEÅEÅ| ÅEÅE"}, {"Éu", "Å|Å|ÅEÅE ÅEÅE"},
				{"Ç‘", "Å|Å|ÅEÅE ÅEÅE"}, {"Éx", "ÅE ÅEÅE"}, {"Ç◊", "ÅE ÅEÅE"}, {"É{", "Å|ÅEÅE ÅEÅE"}, {"Ç⁄", "Å|ÅEÅE ÅEÅE"},
				{"Ép", "Å|ÅEÅEÅE ÅEÅEÅ|Å|ÅE"}, {"Çœ", "Å|ÅEÅEÅE ÅEÅEÅ|Å|ÅE"}, {"És", "Å|Å|ÅEÅEÅ| ÅEÅEÅ|Å|ÅE"}, {"Ç“", "Å|Å|ÅEÅEÅ| ÅEÅEÅ|Å|ÅE"}, {"Év", "Å|Å|ÅEÅE ÅEÅEÅ|Å|ÅE"},
				{"Ç’", "Å|Å|ÅEÅE ÅEÅEÅ|Å|ÅE"}, {"Éy", "ÅE ÅEÅEÅ|Å|ÅE"}, {"Çÿ", "ÅE ÅEÅEÅ|Å|ÅE"}, {"É|", "Å|ÅEÅE ÅEÅEÅ|Å|ÅE"}, {"Ç€", "Å|ÅEÅE ÅEÅEÅ|Å|ÅE"},
				{"Éî", "ÅEÅEÅ| ÅEÅE"}};
		for(String[] to : ja1) jaMc.put(to[0], to[1]);
	}

}
