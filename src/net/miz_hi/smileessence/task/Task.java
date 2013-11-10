package net.miz_hi.smileessence.task;

import android.os.Handler;
import net.miz_hi.smileessence.core.MyExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public abstract class Task<T> implements Callable<T>
{

    protected Runnable callback;

    public void setCallBack(Runnable callback)
    {
        this.callback = callback;
    }

    public Future<T> callAsync()
    {
        final Handler handler = new Handler();
        final Future<T> future = MyExecutor.submit(this);
        MyExecutor.execute(new Runnable()
        {

            @Override
            public void run()
            {

                try
                {
                    final T result = future.get();
                    handler.post(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            onPostExecute(result);
                            if (callback != null)
                            {
                                callback.run();
                            }
                        }
                    });
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                catch (ExecutionException e)
                {
                    e.printStackTrace();
                }
            }
        });
        return future;
    }

    public abstract void onPreExecute();

    public abstract void onPostExecute(T result);

}
