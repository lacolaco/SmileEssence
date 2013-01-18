package net.miz_hi.warotter.auth;

import java.util.HashMap;

import net.miz_hi.warotter.model.Warotter;

public class Consumers
{
	public static HashMap<String, Consumer> consumersMap = new HashMap<String, Consumers.Consumer>();

	static
	{
		consumersMap.put("default", new Consumer(Warotter.CONSUMER_KEY, Warotter.CONSUMER_SECRET));
		consumersMap.put("miz_hi", new Consumer("3VntTn1gM1cLkrzwdMfg", "V6YA6Qsx7lt6CTFFzvlDXIAqdCKGj5tr6C0q7F5TY"));
		consumersMap.put("pro", new Consumer("gpa02tIvQ6X0PPekHag", "5oaocYg2CcyNPL9HP2qJ09RZG6xToGbTVEwUfnKjTs"));
	}

	public static Consumer getDedault()
	{
		return consumersMap.get("default");
	}

	public static class Consumer
	{

		public String key;
		public String secret;

		public Consumer(String key, String secret)
		{
			this.key = key;
			this.secret = secret;
		}
	}
}
