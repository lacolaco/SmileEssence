package net.miz_hi.smileessence.async;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentAsyncTaskHelper
{
	private static ConcurrentLinkedQueue<ConcurrentAsyncTask> taskQueue = new ConcurrentLinkedQueue<ConcurrentAsyncTask>();
	private static ExecutorService executor = Executors.newFixedThreadPool(5);
		
	private static Runnable runnable = new Runnable()
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
	
	public static void addAsyncTask(ConcurrentAsyncTask task)
	{
		taskQueue.offer(task);
		executor.execute(runnable);
	}

}
