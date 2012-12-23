package net.miz_hi.warotter.viewmodel;

import twitter4j.StatusUpdate;
import android.view.View;
import gueei.binding.Command;
import gueei.binding.DependentObservable;
import gueei.binding.observables.StringObservable;
import net.miz_hi.warotter.core.ToastMessage;
import net.miz_hi.warotter.core.ViewModel;
import net.miz_hi.warotter.util.AsyncTweetTask;

public class TweetViewModel extends ViewModel
{
	public StringObservable text = new StringObservable("");
	public DependentObservable<String> textCount = new DependentObservable<String>(String.class, text)
	{

		@Override
		public String calculateValue(Object... arg0) throws Exception
		{
			return String.valueOf(140 - text.get().length());
		}
	};

	public Command commandSubmit = new Command()
	{

		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			String result;
			if (text.isNull() || text.get().length() == 0)
			{
				eventAggregator.publish("toast", new ToastMessage("“ü—Í‚µ‚Ä‚­‚¾‚³‚¢"), null);
			}
			else
			{
				new AsyncTweetTask(eventAggregator).execute(new StatusUpdate(text.get()));
				text.set("");
				eventAggregator.publish("toggle", null, null);
			}
		}
	};

	public Command commandReplace = new Command()
	{

		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			// TODO Auto-generated method stub

		}
	};

	public Command commandTemplate = new Command()
	{

		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			// TODO Auto-generated method stub

		}
	};

	public Command commandMorse = new Command()
	{

		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			// TODO Auto-generated method stub

		}
	};

	public Command commandHashtag = new Command()
	{

		@Override
		public void Invoke(View arg0, Object... arg1)
		{
			// TODO Auto-generated method stub

		}
	};
}
