package net.miz_hi.smileessence.status;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.data.IconCaches;
import net.miz_hi.smileessence.data.StatusModel;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class StatusViewFactory
{

	public static View getView(LayoutInflater layoutInflater, StatusModel model)
	{
		View viewStatus = layoutInflater.inflate(R.layout.status_layout, null);
		ImageView viewIcon = (ImageView) viewStatus.findViewById(R.id.imageView_icon);
		TextView viewHeader = (TextView) viewStatus.findViewById(R.id.textView_header);
		TextView viewText = (TextView) viewStatus.findViewById(R.id.textView_text);
		TextView viewFooter = (TextView) viewStatus.findViewById(R.id.textView_footer);
		if (!model.isRetweet && !model.isReply())
		{
			model.backgroundColor = Client.getResource().getColor(R.color.White);
		}
		viewStatus.setBackgroundColor(model.backgroundColor);
		viewIcon.setImageBitmap(IconCaches.getEmptyIcon());
		if (IconCaches.getIcon(model.user.userId) != null)
		{
			viewIcon.setImageBitmap(IconCaches.getIcon(model.user.userId).use());
		}
		else
		{
			IconCaches.setIconBitmapToView(model.user, viewIcon);
		}
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
		return viewStatus;
	}
}
