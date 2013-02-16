package net.miz_hi.smileessence.status;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.data.IconCaches;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.data.StatusStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class StatusViewFactory
{
	
	public static View getView(LayoutInflater layoutInflater, StatusModel model, View baseView)
	{
		if(baseView == null)
		{
			baseView = layoutInflater.inflate(R.layout.status_layout, null);
		}
		ImageView viewIcon = (ImageView) baseView.findViewById(R.id.imageView_icon);
		TextView viewHeader = (TextView) baseView.findViewById(R.id.textView_header);
		TextView viewText = (TextView) baseView.findViewById(R.id.textView_text);
		TextView viewFooter = (TextView) baseView.findViewById(R.id.textView_footer);
		ImageView viewFavorited = (ImageView)baseView.findViewById(R.id.imageView_favorited);
		if (!model.isRetweet && !model.isReply())
		{
			model.backgroundColor = Client.getResource().getColor(R.color.White);
		}
		baseView.setBackgroundColor(model.backgroundColor);
		viewIcon.setImageBitmap(IconCaches.getEmptyIcon());
		if (IconCaches.getIcon(model.user.userId) != null)
		{
			viewIcon.setImageBitmap(IconCaches.getIcon(model.user.userId).use());
		}
		else
		{
			IconCaches.setIconBitmapToView(model.user, viewIcon);
		}
		
		viewFavorited.setVisibility(StatusStore.isFavorited(model.statusId) ? View.VISIBLE : View.GONE);
		
		int textSize = Client.getTextSize();
		viewHeader.setText(model.headerText);
		viewHeader.setTextColor(model.nameColor);
		viewHeader.setTextSize(textSize);
		viewText.setText(model.text);
		viewText.setTextColor(model.textColor);
		viewText.setTextSize(textSize);
		viewFooter.setText(model.footerText);
		viewFooter.setTextColor(model.textColor);
		viewFooter.setTextSize(textSize - 1);
		
		return baseView;
	}

	public static View getView(LayoutInflater layoutInflater, StatusModel model)
	{
		return getView(layoutInflater, model, null);
	}
}
