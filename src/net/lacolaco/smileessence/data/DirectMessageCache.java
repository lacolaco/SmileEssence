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

import twitter4j.DirectMessage;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class DirectMessageCache
{

    // ------------------------------ FIELDS ------------------------------

    private static DirectMessageCache instance = new DirectMessageCache();

    private ConcurrentHashMap<Long, DirectMessage> cache = new ConcurrentHashMap<>();

    // -------------------------- STATIC METHODS --------------------------

    private DirectMessageCache()
    {
    }

    // --------------------------- CONSTRUCTORS ---------------------------

    public static DirectMessageCache getInstance()
    {
        return instance;
    }

    // -------------------------- OTHER METHODS --------------------------

    /**
     * Get all elements
     *
     * @return cached all message
     */
    public Collection<DirectMessage> all()
    {
        return cache.values();
    }

    /**
     * Get message by id
     *
     * @param id message id
     * @return cached value
     */
    public DirectMessage get(long id)
    {
        return cache.get(id);
    }

    /**
     * Put message into cache
     *
     * @param message
     * @return the previous value associated with key, or null if there was no mapping for key
     */
    public DirectMessage put(DirectMessage message)
    {
        return cache.put(message.getId(), message);
    }

    /**
     * Remove message by id
     *
     * @param id message id
     * @return removed message
     */
    public DirectMessage remove(long id)
    {
        return cache.remove(id);
    }
}
