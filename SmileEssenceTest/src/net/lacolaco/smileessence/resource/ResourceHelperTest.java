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

package net.lacolaco.smileessence.resource;

import android.test.AndroidTestCase;
import net.lacolaco.smileessence.R;

public class ResourceHelperTest extends AndroidTestCase
{

    public void testReadResource() throws Exception
    {
        ResourceHelper pref = new ResourceHelper(getContext());
        assertNotNull(pref.getString(R.string.app_name));
    }

    public void testNotFound() throws Exception
    {
        ResourceHelper pref = new ResourceHelper(getContext());
        assertNull(pref.getString(-1));
    }
}
