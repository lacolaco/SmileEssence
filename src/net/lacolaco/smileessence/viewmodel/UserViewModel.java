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

package net.lacolaco.smileessence.viewmodel;

import twitter4j.User;

public class UserViewModel
{

    // ------------------------------ FIELDS ------------------------------

    private long id;
    private String screenName;
    private String name;
    private String description;
    private String location;
    private String url;
    private String iconURL;
    private String bannerURL;
    private int statusesCount;
    private int friendsCount;
    private int followersCount;
    private int favoritesCount;
    private boolean isProtected;
    private boolean isVerified;

    // --------------------------- CONSTRUCTORS ---------------------------

    public UserViewModel(User user)
    {
        id = user.getId();
        screenName = user.getScreenName();
        name = user.getName();
        description = user.getDescription();
        location = user.getLocation();
        url = user.getURL();
        iconURL = user.getBiggerProfileImageURL();
        bannerURL = user.getProfileBannerURL();
        statusesCount = user.getStatusesCount();
        friendsCount = user.getFriendsCount();
        followersCount = user.getFollowersCount();
        favoritesCount = user.getFavouritesCount();
        isProtected = user.isProtected();
        isVerified = user.isVerified();
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    public String getBannerURL()
    {
        return bannerURL;
    }

    public String getDescription()
    {
        return description;
    }

    public int getFavoritesCount()
    {
        return favoritesCount;
    }

    public int getFollowersCount()
    {
        return followersCount;
    }

    public int getFriendsCount()
    {
        return friendsCount;
    }

    public String getIconURL()
    {
        return iconURL;
    }

    public long getID()
    {
        return id;
    }

    public String getLocation()
    {
        return location;
    }

    public String getName()
    {
        return name;
    }

    public String getScreenName()
    {
        return screenName;
    }

    public int getStatusesCount()
    {
        return statusesCount;
    }

    public String getURL()
    {
        return url;
    }

    public boolean isProtected()
    {
        return isProtected;
    }

    public boolean isVerified()
    {
        return isVerified;
    }
}
