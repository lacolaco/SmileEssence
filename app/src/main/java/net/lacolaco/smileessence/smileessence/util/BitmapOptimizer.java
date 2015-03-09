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
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import net.lacolaco.smileessence.logging.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitmapOptimizer
{

    // -------------------------- STATIC METHODS --------------------------

    public static String rotateImageByExif(Activity activity, String filePath)
    {
        filePath = filePath.replace("file://", "");
        int degree = getRotateDegreeFromExif(filePath);
        if(degree > 0)
        {
            OutputStream out = null;
            Bitmap bitmap = null;
            Bitmap rotatedImage = null;
            try
            {
                Matrix mat = new Matrix();
                mat.postRotate(degree);
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath, opt);
                int width = 480;
                int scale = 1;
                if(opt.outWidth > width)
                {
                    scale = opt.outWidth / width + 2;
                }
                opt.inJustDecodeBounds = false;
                opt.inSampleSize = scale;
                bitmap = BitmapFactory.decodeFile(filePath, opt);
                rotatedImage = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
                File file = new File(filePath);
                String outPath = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + file.getName();
                Logger.debug(outPath);
                out = new FileOutputStream(outPath);
                rotatedImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                return outPath;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                Logger.error(e);
            }
            finally
            {
                if(out != null)
                {
                    try
                    {
                        out.close();
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                        Logger.error(e);
                    }
                }
                if(bitmap != null)
                {
                    bitmap.recycle();
                }
                if(rotatedImage != null)
                {
                    rotatedImage.recycle();
                }
            }
        }
        return filePath;
    }

    private static int getRotateDegreeFromExif(String filePath)
    {
        int degree = 0;
        try
        {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            if(orientation == ExifInterface.ORIENTATION_ROTATE_90)
            {
                degree = 90;
            }
            else if(orientation == ExifInterface.ORIENTATION_ROTATE_180)
            {
                degree = 180;
            }
            else if(orientation == ExifInterface.ORIENTATION_ROTATE_270)
            {
                degree = 270;
            }
            if(degree != 0)
            {
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "0");
                exifInterface.saveAttributes();
            }
        }
        catch(IOException e)
        {
            degree = -1;
            e.printStackTrace();
            Logger.error(e);
        }
        Logger.debug("Exif degree = " + degree);
        return degree;
    }
}
