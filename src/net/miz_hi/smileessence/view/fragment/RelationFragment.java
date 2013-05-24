package net.miz_hi.smileessence.view.fragment;

import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.listener.TimelineScrollListener;
import net.miz_hi.smileessence.system.RelationSystem;
import net.miz_hi.smileessence.util.NamedFragment;
import net.miz_hi.smileessence.view.IRemainable;
import net.miz_hi.smileessence.view.IRemovable;
import net.miz_hi.smileessence.view.activity.MainActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;

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
		ProgressBar progress = new ProgressBar(getActivity());
		progress.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		progress.setVisibility(View.GONE);
		((ViewGroup)listView.getParent()).addView(progress);
		listView.setEmptyView(progress);
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
