package net.miz_hi.smileessence.core;

import java.util.concurrent.*;

public class MyExecutor
{

    private static ExecutorService executor;

    public static void init()
    {
        if (executor == null)
        {
            executor = Executors.newFixedThreadPool(5);
        }
    }

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
        try
        {
            executor.awaitTermination(1000, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
