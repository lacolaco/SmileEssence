package net.miz_hi.smileessence.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.listener.TimelineScrollListener;
import net.miz_hi.smileessence.status.StatusListAdapter;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusStore;
import net.miz_hi.smileessence.status.StatusUtils;
import net.miz_hi.smileessence.system.MainSystem;
import net.miz_hi.smileessence.system.RelationSystem;
import net.miz_hi.smileessence.util.LogHelper;
import net.miz_hi.smileessence.util.NamedFragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class RelationFragment extends NamedFragment implements IRemovable, IRemainable
{
	
	long chasingId = -1;
	ListView listView;
	
	private RelationFragment(){};
	
	public static RelationFragment newInstance(long chansingId)
	{
		RelationFragment relFragment = new RelationFragment();
		relFragment.chasingId = chansingId;
		return relFragment;
	}
	
	public long getChasingId()
	{
		return chasingId;
	}

	public void setChasingId(long id)
	{
		chasingId = id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View page = inflater.inflate(R.layout.listpage_layout, container, false);
		listView = (ListView)page.findViewById(R.id.listpage_listview);
		listView.setFastScrollEnabled(true);
		listView.setAdapter(RelationSystem.getAdapter(this));
		listView.setOnScrollListener(new TimelineScrollListener(RelationSystem.getAdapter(this)));
		return page;
	}	

	@Override
	public String getTitle()
	{
		return "Talk";
	}

	@Override
	public void onRemoved()
	{
		RelationSystem.stopRelation(this);
	}

	@Override
	public String save()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(chasingId);
		return builder.toString();
	}

	@Override
	public void load(String data)
	{
		String[] ar = data.split(" ");		
		chasingId = Long.parseLong(ar[0]);
		RelationSystem.startRelation(this);
		MainActivity.addPage(this);
	}
}
