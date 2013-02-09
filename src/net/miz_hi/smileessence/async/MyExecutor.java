package net.miz_hi.smileessence.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class MyExecutor
{
	private static int count = 1;

	private static ExecutorService executor;

	public static ExecutorService getExecutor()
	{
		if (executor == null)
		{
			executor = Executors.newFixedThreadPool(5, new ThreadFactory()
			{

				@Override
				public Thread newThread(Runnable r)
				{
					return new Thread(r, "AsyncTaskThread " + count++);
				}
			});
		}
		return executor;
	}

	public static void shutdown()
	{
		executor.shutdown();
	}
}
