package net.miz_hi.smileessence.data.extra;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.listener.ExtraWordOnClickListener;
import net.miz_hi.smileessence.util.CustomListAdapter;

public class ExtraWordListAdapter extends CustomListAdapter<ExtraWord>
{

	public ExtraWordListAdapter(Activity activity)
	{
		super(activity, Integer.MAX_VALUE);
	}
	
	@Override
	public View getView(int position, View convertedView, ViewGroup parent)
	{
		if(convertedView == null)
		{
			convertedView = getInflater().inflate(R.layout.menuitem_white, null);
		}		
		
		ExtraWord ExtraWord = (ExtraWord)getItem(position);
		
		TextView viewText = (TextView)convertedView.findViewById(R.id.textView_menuItem);		
		viewText.setText(ExtraWord.getText());
		
		ExtraWordOnClickListener listener = new ExtraWordOnClickListener(this, getActivity(), ExtraWord);
		
		convertedView.setOnClickListener(listener);
		convertedView.setOnLongClickListener(listener);
		
		return convertedView;
	}
}
