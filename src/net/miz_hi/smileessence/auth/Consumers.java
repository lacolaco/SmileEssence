package net.miz_hi.smileessence.auth;

import java.util.HashMap;

public class Consumers
{
	public static HashMap<String, Consumer> consumersMap = new HashMap<String, Consumers.Consumer>();

	public static Consumer smileEssence = new Consumer("uWzFVW7gaRIxWybOsAEWzg", "CMkSxT4YfgE5Yrf77qyh8msy13UYqRTECjrQCePM11Q");
	public static Consumer smileEssenceLite = new Consumer("SIt6h4O6qmBB2URSKsF1Q", "Uil1dyrqiodLLqXIB6B0rVwVxFfFCxTf8ggAcszWc");

	static
	{
		consumersMap.put("SmileEssence", smileEssence);
		consumersMap.put("SmileEssenceLite", smileEssenceLite);
	}

	public static Consumer getDedault()
	{
		return smileEssenceLite;
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
