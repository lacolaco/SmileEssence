package net.miz_hi.smileessence.command.status;

import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.system.RelationSystem;
import net.miz_hi.smileessence.view.activity.MainActivity;
import net.miz_hi.smileessence.view.fragment.RelationFragment;
import android.support.v4.app.Fragment;

public class StatusCommandChaseRelation extends StatusCommand
{
	
	public StatusCommandChaseRelation(StatusModel status)
	{
		super(status);
	}

	@Override
	public String getName()
	{
		return "会話をたどる";
	}

	@Override
	public void workOnUiThread()
	{		
		if(RelationSystem.getRelationByChasingId(status.statusId) != null)
		{
			Fragment fragment = RelationSystem.getRelationByChasingId(status.statusId);
			MainActivity.moveViewPage(MainActivity.getInstance().getPagerAdapter().getItemPosition(fragment));
			return;
		}
		final RelationFragment relFragment = RelationFragment.newInstance(status.statusId);
		RelationSystem.startRelation(relFragment);		
		MainActivity.addPage(relFragment);	
		MainActivity.moveViewPage(MainActivity.getPagerCount());
	}

	@Override
	public boolean getDefaultVisibility()
	{
		return status.inReplyToStatusId > -1;
	}

	
}
