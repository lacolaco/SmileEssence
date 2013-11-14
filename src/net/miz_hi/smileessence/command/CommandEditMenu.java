package net.miz_hi.smileessence.command;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.dialog.CheckBoxListDialog;
import net.miz_hi.smileessence.dialog.CheckBoxListDialog.CheckBoxItem;
import net.miz_hi.smileessence.menu.TweetMenu;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.preference.EnumPreferenceKey.EnumValueType;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandEditMenu extends MenuCommand
{

    private Activity activity;

    public CommandEditMenu(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public String getName()
    {
        return "メニュー表示設定";
    }

    @Override
    public void workOnUiThread()
    {
        final CheckBoxListDialog listDialog = new CheckBoxListDialog(activity);
        listDialog.setTitle("表示する項目を設定してください");

        ArrayList<CheckBoxItem> list = new ArrayList<CheckBoxItem>();
        final HashMap<CheckBoxItem, ICommand> map = new HashMap<CheckBoxItem, ICommand>();
        TweetModel nullModel = TweetModel.getSampleModel();
        TweetMenu adapter = new TweetMenu(activity, nullModel);
        for (ICommand item : adapter.getStatusMenu())
        {
            if (item instanceof IHideable && item.getDefaultVisibility())
            {
                boolean startValue = Client.getPreferenceHelper().getPreferenceValue(item.getClass().getSimpleName(), EnumValueType.BOOLEAN, false);
                CheckBoxItem checkBoxItem = new CheckBoxItem(item.getName(), startValue);
                list.add(checkBoxItem);
                map.put(checkBoxItem, item);
            }
        }

        listDialog.setItems((list.toArray(new CheckBoxItem[0])));

        listDialog.setOnClicked(new OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                    {
                        CheckBoxItem[] items = listDialog.getItems();
                        for (CheckBoxItem item : items)
                        {
                            boolean value = item.value;
                            ICommand command = map.get(item);
                            Client.getPreferenceHelper().putPreferenceValue(command.getClass().getSimpleName(), EnumValueType.BOOLEAN, item.value);
                        }
                        dialog.dismiss();
                        break;
                    }
                    default:
                    {
                        dialog.dismiss();
                    }
                }
            }
        });

        listDialog.createDialog().show();
    }

}
