package net.miz_hi.smileessence.status;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.data.extra.ExtraWord;
import net.miz_hi.smileessence.data.extra.ExtraWords;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.system.MainSystem;
import net.miz_hi.smileessence.system.RelationSystem;
import net.miz_hi.smileessence.view.fragment.ExtractFragment;
import net.miz_hi.smileessence.view.fragment.RelationFragment;

public class StatusChecker
{
	
	public static void check(StatusModel status)
	{		
		if(!status.isRetweet && !ExtraWords.getExtraWords().isEmpty())
		{
			for(ExtraWord word : ExtraWords.getExtraWords())
			{
				if(status.text.contains(word.getText()))
				{
					if(Client.<Boolean>getPreferenceValue(EnumPreferenceKey.EXTRACT_TO))
					{
						ExtractFragment.singleton().getAdapter().addFirst(status);
						ExtractFragment.singleton().getAdapter().notifyAdapter();
					}
					else
					{
						MainSystem.getInstance().mentionsListAdapter.addFirst(status);
						MainSystem.getInstance().mentionsListAdapter.notifyAdapter();
					}
					break;
				}
			}
		}
		if(RelationSystem.isChasing())
		{
			RelationFragment rel = RelationSystem.getRelationByChasingId(status.inReplyToStatusId);
			if(rel != null)
			{
				StatusListAdapter relAdapter = RelationSystem.getAdapter(rel);
				relAdapter.addFirst(status);
				relAdapter.notifyAdapter();
				rel.setChasingId(status.statusId);
			}
		}
	}
	
}
