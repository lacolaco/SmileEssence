package net.miz_hi.smileessence.system;

import java.util.Collection;
import java.util.HashMap;

import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.status.StatusListAdapter;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.status.StatusStore;
import net.miz_hi.smileessence.status.StatusUtils;
import net.miz_hi.smileessence.view.MainActivity;
import net.miz_hi.smileessence.view.RelationFragment;

public class RelationSystem
{
	
	private static final RelationSystem singleton = new RelationSystem();
	
	private HashMap<RelationFragment, StatusListAdapter> relFragments = new HashMap<RelationFragment, StatusListAdapter>();
	
	public static RelationSystem singleton()
	{
		return singleton;
	}
	
	public static StatusListAdapter getAdapter(RelationFragment relFragment)
	{
		return singleton.relFragments.get(relFragment);
	}
	
	public static Collection<StatusListAdapter> getAdapters()
	{
		return singleton.relFragments.values();
	}
	
	public static StatusListAdapter getAdapter(long id)
	{
		return getAdapter(getRelationByChasingId(id));
	}
	
	public static boolean isChasing()
	{
		return !singleton.relFragments.isEmpty();
	}
	
	public static void startRelation(final RelationFragment fragment)
	{
		if(getAdapter(fragment) == null)
		{
			StatusListAdapter adapter = new StatusListAdapter(MainActivity.getInstance());
			singleton.relFragments.put(fragment, adapter);
		}

		MyExecutor.execute(new Runnable()
		{
			
			@Override
			public void run()
			{
				StatusListAdapter adapter = RelationSystem.getAdapter(fragment);
				adapter.clear();
				StatusModel status = StatusUtils.getOrCreateStatusModel(fragment.getChasingId());	
				adapter.addFirst(status);
				long id = status.statusId;
				//–¢—ˆ
				for(StatusModel status1: StatusStore.getList())
				{
					if(status1.inReplyToStatusId == id)
					{
						adapter.addFirst(status1);
						id = status1.statusId;
						continue;
					}
				}
				fragment.setChasingId(id);
				//‰ß‹Ž
				id = status.inReplyToStatusId;
				while(id > -1)
				{
					StatusModel statusModel1 = StatusUtils.getOrCreateStatusModel(id);
					if(statusModel1 != null)
					{
						adapter.addLast(statusModel1);
					}
					else
					{
						break;
					}
					id = statusModel1.inReplyToStatusId;
				}		
				adapter.forceNotifyAdapter();
			}
		});
	}
	
	public static void stopRelation(RelationFragment fragment)
	{		
		singleton.relFragments.remove(fragment);
	}
	
	public static RelationFragment getRelationByChasingId(long id)
	{
		for(RelationFragment rel : singleton.relFragments.keySet())
		{
			if(rel.getChasingId() == id)
			{
				return rel;
			}
		}
		return null;
	}

}
