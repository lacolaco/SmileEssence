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

package net.lacolaco.smileessence.data;

import android.graphics.Bitmap;
import android.util.LruCache;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.*;

public class ImageCache implements ImageLoader.ImageCache
{

    // ------------------------------ FIELDS ------------------------------

    private static ImageCache instance = new ImageCache();
    private LruCache<String, Bitmap> cache;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    // -------------------------- STATIC METHODS --------------------------

    private ImageCache()
    {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        cache = new LruCache<>(cacheSize);
        requestQueue = new RequestQueue(new NoCache(), new BasicNetwork(new HurlStack()));
        imageLoader = new ImageLoader(requestQueue, this);
        requestQueue.start();
    }

    // --------------------------- CONSTRUCTORS ---------------------------

    public static ImageCache getInstance()
    {
        return instance;
    }

    // ------------------------ INTERFACE METHODS ------------------------


    // --------------------- Interface ImageCache ---------------------

    @Override
    public Bitmap getBitmap(String url)
    {
        return cache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap)
    {
        cache.put(url, bitmap);
    }

    // -------------------------- OTHER METHODS --------------------------

    public ImageLoader.ImageContainer requestBitmap(String imageURL)
    {
        return imageLoader.get(imageURL, new ImageLoader.ImageListener()
        {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
            {
            }

            @Override
            public void onErrorResponse(VolleyError error)
            {
            }
        });
    }

    public void setImageToView(String imageURL, NetworkImageView view)
    {
        view.setImageUrl(imageURL, imageLoader);
    }
}
