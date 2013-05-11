package net.miz_hi.smileessence.event;

import twitter4j.Status;
import twitter4j.User;

public class RetweetEvent extends StatusEventModel implements IAttackEvent
{
	
//	private int retweetedCount;

	public RetweetEvent(User source, Status targetStatus)
	{
		super(source, targetStatus);
//		String jsonStr = DataObjectFactory.getRawJSON(targetStatus);
//		try
//		{
//			JSONObject json = new JSONObject(jsonStr);
//			retweetedCount = json.getInt("retweet_count");
//		}
//		catch (JSONException e)
//		{
//			retweetedCount = 0;
//		}
	}

	@Override
	public String getText()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(source.getScreenName());
		sb.append("にリツイートされた");
//		if(retweetedCount > 1)
//		{
//			sb.append(" (");
//			sb.append(retweetedCount);
//			sb.append(")");
//		}
		return sb.toString();
	}
}
