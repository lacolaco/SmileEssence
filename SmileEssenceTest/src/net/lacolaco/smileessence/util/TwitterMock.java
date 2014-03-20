/*
 * SmileEssence Lite
 * Copyright 2014 laco0416
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License
 */

package net.lacolaco.smileessence.util;

import android.content.Context;
import android.content.res.AssetManager;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.json.DataObjectFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TwitterMock
{

    AssetManager assets;

    public TwitterMock(Context context)
    {
        assets = context.getAssets();
    }

    private String getJson(String fileName) throws IOException
    {
        try(InputStream is = assets.open(fileName);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr))
        {
            String buffer = "";
            String str;
            while((str = reader.readLine()) != null)
            {
                buffer += str;
            }
            return buffer;
        }
    }

    public Status getStatusMock() throws IOException, TwitterException
    {
        return DataObjectFactory.createStatus(getJson("status.json"));
    }

    public Status getRetweetMock() throws IOException, TwitterException
    {
        return DataObjectFactory.createStatus(getJson("retweet.json"));
    }

    private User getUserMock() throws IOException, TwitterException
    {
        return DataObjectFactory.createUser(getJson("user.json"));
    }

}
