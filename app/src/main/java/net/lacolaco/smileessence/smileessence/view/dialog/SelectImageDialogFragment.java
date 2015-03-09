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

package net.lacolaco.smileessence.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListView;
import net.lacolaco.smileessence.R;
import net.lacolaco.smileessence.activity.MainActivity;
import net.lacolaco.smileessence.command.Command;
import net.lacolaco.smileessence.entity.Account;
import net.lacolaco.smileessence.util.IntentUtils;
import net.lacolaco.smileessence.view.adapter.CustomListAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectImageDialogFragment extends MenuDialogFragment
{

    // ------------------------ OVERRIDE METHODS ------------------------

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        MainActivity activity = (MainActivity) getActivity();
        Account account = activity.getCurrentAccount();
        List<Command> commands = getCommands(activity);
        View body = activity.getLayoutInflater().inflate(R.layout.dialog_menu_list, null);
        ListView listView = (ListView) body.findViewById(R.id.listview_dialog_menu_list);
        CustomListAdapter<Command> adapter = new CustomListAdapter<>(activity, Command.class);
        listView.setAdapter(adapter);
        for(Command command : commands)
        {
            adapter.addToBottom(command);
        }
        adapter.update();
        listView.setOnItemClickListener(onItemClickListener);

        return new AlertDialog.Builder(activity).setView(body).setTitle(R.string.dialog_title_select_image).setCancelable(true).create();
    }

    // -------------------------- OTHER METHODS --------------------------

    public List<Command> getCommands(final MainActivity activity)
    {
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(new Command(-1, activity)
        {
            @Override
            public boolean execute()
            {
                startGallery(activity);
                return true;
            }

            @Override
            public String getText()
            {
                return activity.getString(R.string.command_select_image_from_gallery);
            }

            @Override
            public boolean isEnabled()
            {
                return true;
            }
        });
        commands.add(new Command(-1, activity)
        {
            @Override
            public boolean execute()
            {
                startCamera(activity);
                return true;
            }

            @Override
            public String getText()
            {
                return activity.getString(R.string.command_select_image_from_camera);
            }

            @Override
            public boolean isEnabled()
            {
                return true;
            }
        });
        return commands;
    }

    private void startCamera(MainActivity activity)
    {
        ContentValues values = new ContentValues();
        String filename = System.currentTimeMillis() + ".jpg";
        values.put(MediaStore.MediaColumns.TITLE, filename);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

        // Uriを取得して覚えておく、Intentにも保存先として渡す
        Uri tempFilePath = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        activity.setCameraTempFilePath(tempFilePath);
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempFilePath);
        IntentUtils.startActivityForResultIfFound(activity, intent, MainActivity.REQUEST_GET_PICTURE_FROM_CAMERA);
    }

    private void startGallery(MainActivity activity)
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        IntentUtils.startActivityForResultIfFound(activity, intent, MainActivity.REQUEST_GET_PICTURE_FROM_GALLERY);
    }
}
