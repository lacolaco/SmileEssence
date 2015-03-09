/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2012-2014 lacolaco.net
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.lacolaco.smileessence.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Morse
{

    // ------------------------------ FIELDS ------------------------------

    private static HashMap<String, String> jaMc;
    private static HashMap<String, String> mcJa;

    // -------------------------- STATIC METHODS --------------------------

    /**
     * 与えられた文字列がモールス文を含むかどうかを返す
     *
     * @param mc 判定する文字列
     * @return モールスを含むならtrue, そうでなければfalse
     */
    public static boolean isMorse(String mc)
    {
        Pattern pattern = Pattern.compile("[－・]+");
        Matcher matcher = pattern.matcher(mc);
        ArrayList<String> list = new ArrayList<String>();
        while(matcher.find())
        {
            list.add(matcher.group());
        }
        if(list.size() <= 2)
        {
            return false;
        }
        else
        {
            for(String s : list)
            {
                if(!s.equals("・・・") && !s.equals("・・") && !s.equals("・"))
                {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 和文モールスをカタカナ・数字に復元する	 *
     *
     * @param str 復元したい文字列
     * @return 復元部分が置換された文字列
     */
    public static String morseToJa(String str)
    {
        String[] strArr = toRightMorse(str).split(" ");
        StringBuilder sb = new StringBuilder();
        for(String tok : strArr)
        {
            sb.append(mcJa.containsKey(tok) ? mcJa.get(tok) : tok);
        }
        return sb.toString();
    }

    /**
     * ひらがな・カタカナ・数字をモールス文に変換する
     *
     * @param str 変換したい文字列
     * @return 変換部分が置換された文字列
     */
    public static String jaToMorse(String str)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < str.length(); i++)
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
        return sb.toString();
    }

    private static String toRightMorse(String str)
    {
        str = str.replace("‐", "－").replace("　", " ").trim();
        Pattern pattern = Pattern.compile("[^・－ ][・－]");
        StringBuilder sb = new StringBuilder(str);
        Matcher matcher = pattern.matcher(str);
        while(matcher.find())
        {
            int i = matcher.start();
            sb.insert(i + 1, " ");
            matcher.reset(sb);
        }
        pattern = Pattern.compile("[・－][^・－ ]");
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

        String[][] ja2 = {{"・－", "イ"}, {"・－・－", "ロ"}, {"－・・・", "ハ"}, {"－・－・", "ニ"}, {"－・・", "ホ"}, {"・", "ヘ"}, {"・・－・・", "ト"}, {"・・－・", "チ"}, {"－－・", "リ"}, {"・・・・", "ヌ"}, {"－・－－・", "ル"}, {"・－－－", "ヲ"}, {"－・－", "ワ"}, {"・－・・", "カ"}, {"－－", "ヨ"}, {"－・", "タ"}, {"－－－", "レ"}, {"－－－・", "ソ"}, {"・－－・", "ツ"}, {"－－・－", "ネ"}, {"・－・", "ナ"}, {"・・・", "ラ"}, {"－", "ム"}, {"・・－", "ウ"}, {"・－・・－", "ヰ"}, {"・・－－", "ノ"}, {"・－・・・", "オ"}, {"・・・－", "ク"}, {"・－－", "ヤ"}, {"－・・－", "マ"}, {"－・－－", "ケ"}, {"－－・・", "フ"}, {"－－－－", "コ"}, {"－・－－－", "エ"}, {"・－・－－", "テ"}, {"－－・－－", "ア"}, {"－・－・－", "サ"}, {"－・－・・", "キ"}, {"－・・－－", "ユ"}, {"－・・・－", "メ"}, {"・・－・－", "ミ"}, {"－－・－・", "シ"}, {"・－－・・", "ヱ"}, {"－－・・－", "ヒ"}, {"－・・－・", "モ"}, {"・－－－・", "セ"}, {"－－－・－", "ス"}, {"・－・－・", "ン"}, {"・・", "゛"}, {"・・－－・", "゜"}, {"・－－・－", "ー"}, {"・－・－・－", "、"}, {"－・－－・－", "（"}, {"・－・・－・", "）"}, {"・－－－－", "1"}, {"・・－－－", "2"}, {"・・・－－", "3"}, {"・・・・－", "4"}, {"・・・・・", "5"}, {"－・・・・", "6"}, {"－－・・・", "7"}, {"－－－・・", "8"}, {"－－－－・", "9"}, {"－－－－－", "0"}, {"", ""}};
        for(String[] pr : ja2)
        {
            mcJa.put(pr[0], pr[1]);
            jaMc.put(pr[1], pr[0]);
        }

        String[][] ja1 = {{"い", "・－"}, {"ィ", "・－"}, {"ぃ", "・－"}, {"ろ", "・－・－"}, {"は", "－・・・"}, {"に", "－・－・"}, {"ほ", "－・・"}, {"へ", "・"}, {"と", "・・－・・"}, {"ち", "・・－・"}, {"り", "－－・"}, {"ぬ", "・・・・"}, {"る", "－・－－・"}, {"を", "・－－－"}, {"わ", "－・－"}, {"ヮ", "－・－"}, {"ゎ", "－・－"}, {"か", "・－・・"}, {"ヵ", "・－・・"}, {"よ", "－－"}, {"ョ", "－－"}, {"ょ", "－－"}, {"た", "－・"}, {"れ", "－－－"}, {"そ", "－－－・"}, {"つ", "・－－・"}, {"ッ", "・－－・"}, {"っ", "・－－・"}, {"ね", "－－・－"}, {"な", "・－・"}, {"ら", "・・・"}, {"む", "－"}, {"う", "・・－"}, {"ゥ", "・・－"}, {"ぅ", "・・－"}, {"ゐ", "・－・・－"}, {"の", "・・－－"}, {"お", "・－・・・"}, {"ォ", "・－・・・"}, {"ぉ", "・－・・・"}, {"く", "・・・－"}, {"や", "・－－"}, {"ャ", "・－－"}, {"ゃ", "・－－"}, {"ま", "－・・－"}, {"け", "－・－－"}, {"ヶ", "－・－－"}, {"ふ", "－－・・"}, {"こ", "－－－－"}, {"え", "－・－－－"}, {"ェ", "－・－－－"}, {"ぇ", "－・－－－"}, {"て", "・－・－－"}, {"あ", "－－・－－"}, {"ァ", "－－・－－"}, {"ぁ", "－－・－－"}, {"さ", "－・－・－"}, {"き", "－・－・・"}, {"ゆ", "－・・－－"}, {"ュ", "－・・－－"}, {"ゅ", "－・・－－"}, {"め", "－・・・－"}, {"み", "・・－・－"}, {"し", "－－・－・"}, {"ゑ", "・－－・・"}, {"ひ", "－－・・－"}, {"も", "－・・－・"}, {"せ", "・－－－・"}, {"す", "－－－・－"}, {"ん", "・－・－・"}, {"ガ", "・－・・ ・・"}, {"が", "・－・・ ・・"}, {"ギ", "－・－・・ ・・"}, {"ぎ", "－・－・・ ・・"}, {"グ", "・・・－ ・・"}, {"ぐ", "・・・－ ・・"}, {"ゲ", "－・－－ ・・"}, {"げ", "－・－－ ・・"}, {"ゴ", "－－－－ ・・"}, {"ご", "－－－－ ・・"}, {"ザ", "－・－・－ ・・"}, {"ざ", "－・－・－ ・・"}, {"ジ", "－－・－・ ・・"}, {"じ", "－－・－・ ・・"}, {"ズ", "－－－・－ ・・"}, {"ず", "－－－・－ ・・"}, {"ゼ", "・－－－・ ・・"}, {"ぜ", "・－－－・ ・・"}, {"ゾ", "－－－・ ・・"}, {"ぞ", "－－－・ ・・"}, {"ダ", "－・ ・・"}, {"だ", "－・ ・・"}, {"ヂ", "・・－・ ・・"}, {"ぢ", "・・－・ ・・"}, {"ヅ", "・－－・ ・・"}, {"づ", "・－－・ ・・"}, {"デ", "・－・－－ ・・"}, {"で", "・－・－－ ・・"}, {"ド", "・・－・・ ・・"}, {"ど", "・・－・・ ・・"}, {"バ", "－・・・ ・・"}, {"ば", "－・・・ ・・"}, {"ビ", "－－・・－ ・・"}, {"び", "－－・・－ ・・"}, {"ブ", "－－・・ ・・"}, {"ぶ", "－－・・ ・・"}, {"ベ", "・ ・・"}, {"べ", "・ ・・"}, {"ボ", "－・・ ・・"}, {"ぼ", "－・・ ・・"}, {"パ", "－・・・ ・・－－・"}, {"ぱ", "－・・・ ・・－－・"}, {"ピ", "－－・・－ ・・－－・"}, {"ぴ", "－－・・－ ・・－－・"}, {"プ", "－－・・ ・・－－・"}, {"ぷ", "－－・・ ・・－－・"}, {"ペ", "・ ・・－－・"}, {"ぺ", "・ ・・－－・"}, {"ポ", "－・・ ・・－－・"}, {"ぽ", "－・・ ・・－－・"}, {"ヴ", "・・－ ・・"}};
        for(String[] to : ja1)
            jaMc.put(to[0], to[1]);
    }

}
