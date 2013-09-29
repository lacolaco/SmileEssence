package net.miz_hi.smileessence.status;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.cache.IconCache;
import net.miz_hi.smileessence.cache.TweetCache;
import net.miz_hi.smileessence.model.status.IStatusModel;
import net.miz_hi.smileessence.model.status.event.EventModel;
import net.miz_hi.smileessence.model.status.event.StatusEvent;
import net.miz_hi.smileessence.model.status.tweet.EnumTweetType;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.model.status.user.UserModel;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.util.Morse;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class StatusViewFactory
{
	
	LayoutInflater inflater;
	View baseView;
	ImageView icon;
	TextView textTop;
	TextView textContent;
	TextView textBottom;
	ImageView favorited;
	int colorTop;
	int colorContent;
	int colorBottom;
	
	private StatusViewFactory(){}
	
	public static StatusViewFactory newInstance(LayoutInflater inflater, View baseView)
	{
		StatusViewFactory factory = new StatusViewFactory();
		factory.inflater = inflater;
		if(baseView == null)
		{
			factory.baseView = factory.inflater.inflate(R.layout.status_layout, null);
		}
		else
		{
			factory.baseView = baseView;
		}
		factory.icon = (ImageView) factory.baseView.findViewById(R.id.imageView_icon);
		factory.textTop = (TextView) factory.baseView.findViewById(R.id.textView_header);
		factory.textContent = (TextView) factory.baseView.findViewById(R.id.textView_text);
		factory.textBottom = (TextView) factory.baseView.findViewById(R.id.textView_footer);
		factory.favorited = (ImageView) factory.baseView.findViewById(R.id.imageView_favorited);
		return factory;
	}
	
	public View getStatusView(IStatusModel model)
	{
		// initialize
		favorited.setVisibility(View.GONE);
		int textSize = Client.getTextSize();
		textTop.setTextSize(textSize);
		textContent.setTextSize(textSize);
		textBottom.setTextSize(textSize - 2);
		colorTop = Client.getColor(R.color.ThickGreen);
		colorContent = Client.getColor(R.color.Gray);
		colorBottom = Client.getColor(R.color.Gray2);
		//adjust to model
		if(model instanceof TweetModel)
		{
			adjustToTweetView((TweetModel) model);
		}
		else if(model instanceof EventModel)
		{
			adjustToEventView((EventModel) model);
		}
		else if(model instanceof UserModel)
		{
			adjustToUserModel((UserModel) model);
		}
		//coloring
		textTop.setTextColor(colorTop);
		textContent.setTextColor(colorContent);
		textBottom.setTextColor(colorBottom);		
		//set value
		icon.setTag(model.getUser().userId);
		IconCache.setIconBitmapToView(model.getUser(), icon);				
		textTop.setText(model.getTextTop());		
		String text;
		if(Morse.isMorse(model.getTextContent()) && Client.<Boolean>getPreferenceValue(EnumPreferenceKey.READ_MORSE))
		{
			text = model.getTextContent() + "\n(" + Morse.mcToJa(model.getTextContent()) + ")";
		}
		else
		{
			text = model.getTextContent();
		}
		textContent.setText(text);		
		textBottom.setText(model.getTextBottom());	
		
		return baseView;
	}

	private void adjustToTweetView(TweetModel model)
	{
		if (model.type == EnumTweetType.RETWEET)
		{
			baseView.setBackgroundColor(Client.getColor(R.color.LightBlue));
		}
		else if (model.type == EnumTweetType.REPLY)
		{
			baseView.setBackgroundColor(Client.getColor(R.color.LightRed));
		}
		
		if (model.user.isMe())
		{
			colorTop = Client.getColor(R.color.DarkBlue);
		}		
		favorited.setVisibility(TweetCache.isFavorited(model.statusId) ? View.VISIBLE : View.GONE);
	}
	
	private void adjustToEventView(EventModel model)
	{
		if(model instanceof StatusEvent)
		{
			colorTop = Client.getColor(R.color.DarkBlue);
		}
	}	
	
	private void adjustToUserModel(UserModel model)
	{

	}

}
