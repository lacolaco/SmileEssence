package net.miz_hi.smileessence.async;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.AsyncTask;

public abstract class ConcurrentAsyncTask<T> extends AsyncTask<Object, Object, T>
{
	
	protected static ConcurrentLinkedQueue<ConcurrentAsyncTask> taskQueue = new ConcurrentLinkedQueue<ConcurrentAsyncTask>();
	protected static ExecutorService executor = Executors.newFixedThreadPool(3);
	
	protected static Runnable runnable = new Runnable()
	{
		
		@Override
		public void run()
		{
			if(!taskQueue.isEmpty())
			{
				taskQueue.poll().execute(new Object());
			}			
		}
	};
	
	public static void addTask(ConcurrentAsyncTask task)
	{
		taskQueue.offer(task);
		executor.execute(runnable);
	}
	
	@Override
	protected abstract T doInBackground(Object... params);
	
	@Override
	protected abstract void onPostExecute(T result);

}
