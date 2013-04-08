package net.miz_hi.smileessence.event;

import twitter4j.Status;
import twitter4j.User;

public class FavoriteEvent extends StatusEvent implements IAttackEvent
{

//	private int favedCount;
	
	public FavoriteEvent(User source, Status targetStatus)
	{
		super(source, targetStatus);
//		String jsonStr = DataObjectFactory.getRawJSON(targetStatus);
//		try
//		{
//			JSONObject json = new JSONObject(jsonStr);
//			
//			favedCount = json.getInt("favorite_count");
//
//		}
//		catch (JSONException e)
//		{
//			e.printStackTrace();
//			favedCount = 0;
//		}
	}

	@Override
	public String getText()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(source.getScreenName());
		sb.append("‚É‚Ó‚Ÿ‚Ú‚ç‚ê‚½");
//		if(favedCount > 1)
//		{
//			sb.append(" (");
//			sb.append(favedCount);
//			sb.append("‚Ó‚Ÿ‚Ú)");
//		}
		return sb.toString();
	}
}
