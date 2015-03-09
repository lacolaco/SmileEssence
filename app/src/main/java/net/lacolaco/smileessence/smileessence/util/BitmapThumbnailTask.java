/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2012-2014 lacolaco.net
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.lacolaco.smileessence.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.widget.ImageView;

public class BitmapThumbnailTask extends AsyncTask<Void, Void, Bitmap>
{

    // ------------------------------ FIELDS ------------------------------

    private final Activity activity;
    private final String filePath;
    private final ImageView imageView;

    // --------------------------- CONSTRUCTORS ---------------------------

    public BitmapThumbnailTask(Activity activity, String filePath, ImageView imageView)
    {
        this.activity = activity;
        this.filePath = filePath;
        this.imageView = imageView;
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
        if(bitmap != null && imageView != null)
        {
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected Bitmap doInBackground(Void... params)
    {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPurgeable = true; // GC可能にする
        opt.inSampleSize = 2;
        //        ContentResolver resolver = activity.getContentResolver();
        //        Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.ImageColumns.DATA + " = ?", new String[]{filePath}, null);
        //        if(cursor.moveToFirst())
        //        {
        //            // サムネイルの取得
        //            long id = cursor.getLong(cursor.getColumnIndex("_id"));
        //            return MediaStore.Images.Thumbnails.getThumbnail(resolver, id, MediaStore.Images.Thumbnails.MICRO_KIND, opt);
        //        }
        //        return null;
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(filePath), 100, 100, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
    }
}
