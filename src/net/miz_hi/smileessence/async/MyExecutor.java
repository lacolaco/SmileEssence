package net.miz_hi.smileessence.async;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public class MyExecutor
{

	private static ExecutorService executor = Executors.newFixedThreadPool(5);

	public static ExecutorService getExecutor()
	{
		return executor;
	}
	
	public static void execute(Runnable runnable)
	{
		executor.execute(runnable);
	}
	
	public static <T> Future<T> submit(Callable<T> callable)
	{
		return executor.submit(callable);
	}

	public static void shutdown()
	{
		executor.shutdown();
	}
}
