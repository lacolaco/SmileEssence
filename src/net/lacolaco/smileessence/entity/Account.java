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

@Table(name = "Accounts")
public class Account extends Model
{

    @Column(name = "Token", notNull = true)
    public String accessToken;
    @Column(name = "Secret", notNull = true)
    public String accessSecret;

    public Account()
    {
        super();
    }

    public Account(String token, String tokenSecret)
    {
        super();
        this.accessToken = token;
        this.accessSecret = tokenSecret;
    }
}
