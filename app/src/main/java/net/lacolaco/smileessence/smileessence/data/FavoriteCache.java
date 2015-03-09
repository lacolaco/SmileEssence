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

import twitter4j.Status;

import java.util.concurrent.ConcurrentHashMap;

public class FavoriteCache
{

    // ------------------------------ FIELDS ------------------------------

    private static FavoriteCache instance = new FavoriteCache();

    private ConcurrentHashMap<Long, Boolean> cache = new ConcurrentHashMap<>();

    // -------------------------- STATIC METHODS --------------------------

    private FavoriteCache()
    {
    }

    // --------------------------- CONSTRUCTORS ---------------------------

    public static FavoriteCache getInstance()
    {
        return instance;
    }

    // -------------------------- OTHER METHODS --------------------------

    /**
     * Get status by id
     */
    public boolean get(long id)
    {
        Boolean favorited = cache.get(id);
        return favorited != null ? favorited : false;
    }

    /**
     * Put status into cache
     */
    public void put(Status status)
    {
        if(!status.isRetweet())
        {
            cache.put(status.getId(), status.isFavorited());
        }
    }

    public void put(Status status, boolean favorited)
    {
        if(status.isRetweet())
        {
            cache.put(status.getRetweetedStatus().getId(), favorited);
        }
        else
        {
            cache.put(status.getId(), favorited);
        }
    }

    public void put(long id, boolean favorited)
    {
        cache.put(id, favorited);
    }

    /**
     * Remove favorite by id
     */
    public boolean remove(long id)
    {
        return cache.remove(id);
    }
}
