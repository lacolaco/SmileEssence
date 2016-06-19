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

import android.app.Activity;

import net.lacolaco.smileessence.view.adapter.StatusListAdapter;

public class UserListListAdapter extends StatusListAdapter {

    // ------------------------------ FIELDS ------------------------------

    private String listFullName;

    // --------------------------- CONSTRUCTORS ---------------------------

    public UserListListAdapter(Activity activity) {
        super(activity);
    }

    // --------------------- GETTER / SETTER METHODS ---------------------

    public String getListFullName() {
        return listFullName;
    }

    public void setListFullName(String listFullName) {
        this.listFullName = listFullName;
    }

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    public void addToBottom(StatusViewModel... items) {
        super.addToBottom(items);
    }

    @Override
    public void addToTop(StatusViewModel... items) {
        super.addToTop(items);
    }
}