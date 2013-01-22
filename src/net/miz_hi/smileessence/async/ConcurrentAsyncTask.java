package net.miz_hi.smileessence.async;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.AsyncTask;

public abstract class ConcurrentAsyncTask<T> extends AsyncTask<Object, Object, T>
{
		
	@Override
	protected abstract T doInBackground(Object... params);
	
	@Override
	protected abstract void onPostExecute(T result);

}
