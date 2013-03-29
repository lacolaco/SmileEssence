package net.miz_hi.smileessence.command;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.data.StatusStore;
import net.miz_hi.smileessence.status.StatusListAdapter;
import net.miz_hi.smileessence.system.MainSystem;
import net.miz_hi.smileessence.util.TwitterManager;
import net.miz_hi.smileessence.view.MainActivity;
import net.miz_hi.smileessence.view.RelationListPageFragment;
import twitter4j.Status;

public class StatusCommandChaseRelation extends StatusCommand implements IHideable
{

	public StatusCommandChaseRelation(StatusModel status)
	{
		super(status);
	}

	@Override
	public String getName()
	{
		return "‰ï˜b‚ð‚½‚Ç‚é";
	}

	@Override
	public void workOnUiThread()
	{
		RelationListPageFragment.setChasingId(status.statusId);
		final StatusListAdapter adapter = MainSystem.getInstance().relationListAdapter;
		adapter.clear();
		adapter.forceNotifyAdapter();
		adapter.addFirst(status);
		adapter.notifyAdapter();
		
		MyExecutor.execute(new Runnable()
		{
			
			@Override
			public void run()
			{
				long id = status.statusId;

				for(StatusModel statusModel0: StatusStore.getList())
				{
					List<StatusModel> list = new ArrayList<StatusModel>();
					for(StatusModel statusModel1: StatusStore.getList())
					{
						if(statusModel1.inReplyToStatusId == id)
						{
							list.add(statusModel1);
						}
					}
					StatusModel statusModel;
					if(!list.isEmpty())
					{
						statusModel = list.get(0);
						for(StatusModel statusModel2 : list)
						{
							if(statusModel.compareTo(statusModel2) > 0)
							{
								statusModel = statusModel2;
							}
						}
						adapter.addFirst(statusModel);
						id = statusModel.statusId;
						RelationListPageFragment.setChasingId(id);
						continue;
					}
				}
				
				id = status.inReplyToStatusId;
				while(id > -1)
				{
					StatusModel statusModel = chaseRelation(id);
					if(statusModel != null)
					{
						adapter.addLast(statusModel);
						adapter.forceNotifyAdapter();
					}
					else
					{
						break;
					}
					id = statusModel.inReplyToStatusId;
				}				
			}
		});
		
		MainActivity.getInstance().getViewPager().setCurrentItem(3, true);
		
	}
	
	private StatusModel chaseRelation(long id)
	{
		StatusModel statusModel = StatusStore.get(id);
		if(statusModel == null)
		{
			Status status = TwitterManager.getStatus(Client.getMainAccount(), id);
			if(status == null)
			{
				return null;
			}
			statusModel = StatusStore.put(status);
		}
		return statusModel;
	}

	@Override
	public boolean getDefaultVisibility()
	{
		return status.inReplyToStatusId > -1;
	}

	
}
