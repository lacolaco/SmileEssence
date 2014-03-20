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

package net.lacolaco.smileessence.entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Commands")
public class CommandSetting extends Model
{

    @Column(name = "CommandID")
    public int commandID;
    @Column(name = "Visibility")
    public boolean visibility;

    public CommandSetting()
    {
        super();
    }

    public CommandSetting(int commandID, boolean visibility)
    {
        super();
        this.commandID = commandID;
        this.visibility = visibility;
    }
}
