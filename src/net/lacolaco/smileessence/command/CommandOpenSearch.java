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

package net.lacolaco.smileessence.command;

import android.app.Activity;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.entity.SearchQuery;

public class CommandOpenSearch extends Command
{

    // ------------------------------ FIELDS ------------------------------

    private final SearchQuery query;

    // --------------------------- CONSTRUCTORS ---------------------------

    public CommandOpenSearch(Activity activity, SearchQuery searchQuery)
    {
        super(-1, activity);
        this.query = searchQuery;
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    public SearchQuery getQuery()
    {
        return query;
    }

    @Override
    public String getText()
    {
        return query.query;
    }

    @Override
    public boolean isEnabled()
    {
        MainActivity activity = (MainActivity) getActivity();
        return activity.getPageIndexSearch() != MainActivity.PAGE_GONE;
    }

    // -------------------------- OTHER METHODS --------------------------

    @Override
    public boolean execute()
    {
        ((MainActivity) getActivity()).openSearchPage(query.query);
        return true;
    }
}
