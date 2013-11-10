package net.miz_hi.smileessence.task.impl;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.cache.IconCache;
import net.miz_hi.smileessence.cache.IconCache.Icon;
import net.miz_hi.smileessence.model.status.user.UserModel;
import net.miz_hi.smileessence.task.Task;
import net.miz_hi.smileessence.util.LogHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetIconTask extends Task<Bitmap>
{

    private UserModel user;

    public GetIconTask(UserModel user)
    {
        this.user = user;
    }

    @Override
    public Bitmap call()
    {
        try
        {
            URL url = new URL(user.iconUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Options opt = new Options();
            opt.inPurgeable = true; // GC
            Bitmap bm = BitmapFactory.decodeStream(input, null, opt);
            File file = Client.getApplicationFile(IconCache.genIconName(user));
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(CompressFormat.PNG, 90, fos);
            fos.close();
            return bm;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return IconCache.getEmptyIcon();
        }
    }

    @Override
    public void onPreExecute()
    {
    }

    @Override
    public void onPostExecute(Bitmap result)
    {
        Icon icon = new Icon(result, IconCache.genIconName(user));
        IconCache.putIconToMap(user.userId, icon);
        icon.use();
        LogHelper.d("icon get from web");
    }
}
