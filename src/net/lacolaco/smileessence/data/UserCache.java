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

import twitter4j.User;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UserCache
{

    // ------------------------------ FIELDS ------------------------------

    private static UserCache instance = new UserCache();

    private ConcurrentHashMap<Long, User> cache = new ConcurrentHashMap<>();

    private ConcurrentLinkedQueue<Long> invisibleIDs = new ConcurrentLinkedQueue<>();

    // -------------------------- STATIC METHODS --------------------------

    private UserCache()
    {
    }

    // --------------------------- CONSTRUCTORS ---------------------------

    public static UserCache getInstance()
    {
        return instance;
    }

    // -------------------------- OTHER METHODS --------------------------

    /**
     * Get user by id
     *
     * @param id user id
     * @return cached value
     */
    public User get(long id)
    {
        return cache.get(id);
    }

    public boolean isInvisibleUserID(long id)
    {
        return invisibleIDs.contains(Long.valueOf(id));
    }

    /**
     * Put user into cache
     *
     * @param user
     * @return the previous value associated with key, or null if there was no mapping for key
     */
    public User put(User user)
    {
        return cache.put(user.getId(), user);
    }

    public void putInvisibleUser(long id)
    {
        invisibleIDs.add(id);
    }

    /**
     * Remove user by id
     *
     * @param id user id
     * @return removed user
     */
    public User remove(long id)
    {
        return cache.remove(id);
    }

    public void removeInvisibleUser(long id)
    {
        invisibleIDs.remove(Long.valueOf(id));
    }
}
