package net.miz_hi.smileessence.core;

import java.util.LinkedList;
import net.miz_hi.smileessence.async.MyExecutor;

import android.os.AsyncTask;

public abstract class SimpleAsyncTask<T> extends AsyncTask<Object, Object, T>
{
	private static LinkedList<SimpleAsyncTask> taskQueue = new LinkedList<SimpleAsyncTask>();

	private static Runnable runnable = new Runnable()
	{

		@Override
		public void run()
		{
			if (!taskQueue.isEmpty())
			{
				taskQueue.poll().execute();
			}
		}
	};

	public void addToQueue()
	{
		taskQueue.offer(this);
		MyExecutor.getExecutor().execute(runnable);
	}

}
