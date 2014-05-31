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

package net.lacolaco.smileessence.entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

@Table(name = "SearchQueries")
public class SearchQuery extends Model
{

    // ------------------------------ FIELDS ------------------------------

    @Column(name = "Query", unique = true)
    public String query;

    @Column(name = "CreatedAt")
    public Date createdAt;

    // -------------------------- STATIC METHODS --------------------------

    public SearchQuery()
    {
        super();
    }

    public SearchQuery(String query, Date createdAt)
    {
        this.query = query;
        this.createdAt = createdAt;
    }

    public static List<SearchQuery> getAll()
    {
        return new Select().from(SearchQuery.class).orderBy("CreatedAt DESC").execute();
    }

    // --------------------------- CONSTRUCTORS ---------------------------

    /**
     * @param query
     * @return
     */
    public static boolean saveIfNotFound(String query)
    {
        if(findByQuery(query) != null)
        {
            return false;
        }
        else
        {
            new SearchQuery(query, new Date()).save();
            return true;
        }
    }

    public static SearchQuery findByQuery(String query)
    {
        return new Select().from(SearchQuery.class).where("Query = ?", query).executeSingle();
    }
}
