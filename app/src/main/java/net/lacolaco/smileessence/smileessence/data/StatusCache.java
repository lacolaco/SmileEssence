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
import java.util.concurrent.ConcurrentLinkedQueue;

public class StatusCache
{

    // ------------------------------ FIELDS ------------------------------

    private static StatusCache instance = new StatusCache();

    private ConcurrentHashMap<Long, Status> cache = new ConcurrentHashMap<>();

    private ConcurrentLinkedQueue<Long> ignoreIDs = new ConcurrentLinkedQueue<>();

    // -------------------------- STATIC METHODS --------------------------

    private StatusCache()
    {
    }

    // --------------------------- CONSTRUCTORS ---------------------------

    public static StatusCache getInstance()
    {
        return instance;
    }

    // -------------------------- OTHER METHODS --------------------------

    public void addToIgnoreStatus(long id)
    {
        ignoreIDs.add(id);
    }

    /**
     * Get status by id
     *
     * @param id status id
     * @return cached value
     */
    public Status get(long id)
    {
        return cache.get(id);
    }

    public boolean isIgnored(long id)
    {
        return ignoreIDs.contains(id);
    }

    /**
     * Put status into cache
     *
     * @param status
     * @return the previous value associated with key, or null if there was no mapping for key
     */
    public Status put(Status status)
    {
        if(status.isRetweet())
        {
            put(status.getRetweetedStatus());
        }
        if(cache.containsKey(status.getId()))
        {
            cache.remove(status.getId());
        }
        return cache.put(status.getId(), status);
    }

    /**
     * Remove status by id
     *
     * @param id status id
     * @return removed status
     */
    public Status remove(long id)
    {
        return cache.remove(id);
    }
}
