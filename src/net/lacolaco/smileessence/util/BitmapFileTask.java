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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;

public class BitmapFileTask extends AsyncTask<Void, Void, Bitmap>
{

    private final String filePath;
    private final ImageView imageView;

    public BitmapFileTask(String filePath, ImageView imageView)
    {
        this.filePath = filePath;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params)
    {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPurgeable = true; // GC可能にする
        opt.inSampleSize = 2;
        return BitmapFactory.decodeFile(filePath, opt);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
        if(bitmap != null && imageView != null)
        {
            ExifInterface exifInterface = null;
            try
            {
                exifInterface = new ExifInterface(filePath);

                // 向きを取得
                int orientation = Integer.parseInt(exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION));
                imageView.setScaleType(ImageView.ScaleType.MATRIX);
                imageView.setImageBitmap(bitmap);
                // 画像の幅、高さを取得
                int wOrg = bitmap.getWidth();
                int hOrg = bitmap.getHeight();
                imageView.getLayoutParams();
                ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imageView.getLayoutParams();

                float factor;
                float viewWidth = imageView.getWidth();
                Matrix mat = new Matrix();
                mat.reset();
                switch(orientation)
                {
                    case 1://only scaling
                    {
                        factor = (float) viewWidth / (float) wOrg;
                        mat.preScale(factor, factor);
                        lp.width = (int) (wOrg * factor);
                        lp.height = (int) (hOrg * factor);
                        break;
                    }
                    case 2://flip vertical
                    {
                        factor = (float) viewWidth / (float) wOrg;
                        mat.postScale(factor, -factor);
                        mat.postTranslate(0, hOrg * factor);
                        lp.width = (int) (wOrg * factor);
                        lp.height = (int) (hOrg * factor);
                        break;
                    }
                    case 3://rotate 180
                    {
                        mat.postRotate(180, wOrg / 2f, hOrg / 2f);
                        factor = (float) viewWidth / (float) wOrg;
                        mat.postScale(factor, factor);
                        lp.width = (int) (wOrg * factor);
                        lp.height = (int) (hOrg * factor);
                        break;
                    }
                    case 4://flip horizontal
                    {
                        factor = (float) viewWidth / (float) wOrg;
                        mat.postScale(-factor, factor);
                        mat.postTranslate(wOrg * factor, 0);
                        lp.width = (int) (wOrg * factor);
                        lp.height = (int) (hOrg * factor);
                        break;
                    }
                    case 5://flip vertical rotate270
                    {
                        mat.postRotate(270, 0, 0);
                        factor = (float) viewWidth / (float) hOrg;
                        mat.postScale(factor, -factor);
                        lp.width = (int) (hOrg * factor);
                        lp.height = (int) (wOrg * factor);
                        break;
                    }
                    case 6://rotate 90
                    {
                        mat.postRotate(90, 0, 0);
                        factor = (float) viewWidth / (float) hOrg;
                        mat.postScale(factor, factor);
                        mat.postTranslate(hOrg * factor, 0);
                        lp.width = (int) (hOrg * factor);
                        lp.height = (int) (wOrg * factor);
                        break;
                    }
                    case 7://flip vertical, rotate 90
                    {
                        mat.postRotate(90, 0, 0);
                        factor = (float) viewWidth / (float) hOrg;
                        mat.postScale(factor, -factor);
                        mat.postTranslate(hOrg * factor, wOrg * factor);
                        lp.width = (int) (hOrg * factor);
                        lp.height = (int) (wOrg * factor);
                        break;
                    }
                    case 8://rotate 270
                    {
                        mat.postRotate(270, 0, 0);
                        factor = (float) viewWidth / (float) hOrg;
                        mat.postScale(factor, factor);
                        mat.postTranslate(0, wOrg * factor);
                        lp.width = (int) (hOrg * factor);
                        lp.height = (int) (wOrg * factor);
                        break;
                    }
                }
                imageView.setLayoutParams(lp);
                imageView.setImageMatrix(mat);
                imageView.invalidate();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
