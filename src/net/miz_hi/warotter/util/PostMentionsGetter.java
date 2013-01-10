package net.miz_hi.warotter.util;

import java.util.ArrayList;

import net.miz_hi.warotter.model.StatusStore;
import net.miz_hi.warotter.model.Warotter;
import net.miz_hi.warotter.viewmodel.MainActivityViewModel;
import net.miz_hi.warotter.viewmodel.StatusViewModel;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import android.os.AsyncTask;

public class PostMentionsGetter extends AsyncTask<Paging, Integer, ResponseList<twitter4j.Status>>
{
	
	private MainActivityViewModel viewModel;
	
	public PostMentionsGetter(MainActivityViewModel mainViewModel)
	{
		this.viewModel = mainViewModel;
	}

	@Override
	protected ResponseList<twitter4j.Status> doInBackground(Paging... arg0)
	{
		try
		{
			return Warotter.getTwitter(Warotter.getMainAccount()).getMentionsTimeline(arg0[0]);			
		}
		catch (TwitterException e)
		{
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(ResponseList<twitter4j.Status> result)
	{
		ArrayList<StatusViewModel> list = new ArrayList<StatusViewModel>();
		for(twitter4j.Status st : result)
		{
			StatusStore.put(st);
			list.add(StatusViewModel.createInstance(st.getId()));
		}
		viewModel.mentionsTimeline.addAll(list);	
	}

}
