package net.miz_hi.smileessence.view;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.data.TemplateListAdapter;
import net.miz_hi.smileessence.data.Templates;
import net.miz_hi.smileessence.listener.TemplateOnClickListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class TemplateActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.templateactivity_layout);
		
		ListView listView = (ListView)findViewById(R.id.listView_template);
		TemplateListAdapter adapter = new TemplateListAdapter(this);
		listView.addFooterView(getFooterView(adapter));
		listView.setAdapter(adapter);
		adapter.addAll(Templates.getTemplates());
		adapter.forceNotifyAdapter();
	}
	
	private View getFooterView(TemplateListAdapter adapter)
	{
		View view = getLayoutInflater().inflate(R.layout.menuitem_layout, null);
		TextView viewText = (TextView)view.findViewById(R.id.textView_menuItem);
		viewText.setText("êVÇµÇ¢íËå^ï∂Çí«â¡");
		viewText.setTextColor(Client.getColor(R.color.Black));
		view.setOnClickListener(new TemplateOnClickListener(adapter, this, null));
		return view;		
	}

}
