package net.miz_hi.smileessence.system;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.auth.AuthenticationDB;
import net.miz_hi.smileessence.auth.AuthorizeHelper;
import net.miz_hi.smileessence.auth.Consumers;
import net.miz_hi.smileessence.cache.IconCache;
import net.miz_hi.smileessence.cache.TweetCache;
import net.miz_hi.smileessence.cache.UserCache;
import net.miz_hi.smileessence.core.EnumRequestCode;
import net.miz_hi.smileessence.core.MyExecutor;
import net.miz_hi.smileessence.data.list.ListManager;
import net.miz_hi.smileessence.dialog.SingleButtonDialog;
import net.miz_hi.smileessence.extraction.ExtractManager;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.model.statuslist.timeline.Timeline;
import net.miz_hi.smileessence.model.statuslist.timeline.impl.ListTimeline;
import net.miz_hi.smileessence.notification.Notificator;
import net.miz_hi.smileessence.preference.EnumPreferenceKey;
import net.miz_hi.smileessence.statuslist.StatusListAdapter;
import net.miz_hi.smileessence.statuslist.StatusListManager;
import net.miz_hi.smileessence.task.impl.GetHomeTimelineTask;
import net.miz_hi.smileessence.task.impl.GetMentionsTask;
import net.miz_hi.smileessence.twitter.TwitterManager;
import net.miz_hi.smileessence.view.fragment.impl.ListFragment;
import twitter4j.Paging;

import java.util.List;

public class MainActivitySystem
{

    public AuthorizeHelper authHelper;
    public Uri tempFilePath;

    public void onDestroyed()
    {
        TwitterManager.closeTwitterStream();
        PostSystem.clear(false);
        IconCache.clearCache();
        TweetCache.clearCache();
        UserCache.clearCache();
        MyExecutor.shutdown();
    }

    public boolean checkAccount(Activity activity)
    {
        if (Client.hasAuthorizedAccount())
        {
            accountSetup();
            return true;
        }
        else
        {
            authHelper = new AuthorizeHelper(activity, Consumers.getDedault());
            //NOT AUTHORIZED
            SingleButtonDialog.show(activity, "認証してください", "認証ページヘ", new Runnable()
            {

                @Override
                public void run()
                {
                    authHelper.oauthSend();
                }
            });
            return false;
        }
    }

    public void authorize(Activity activity, Uri data)
    {
        Account account = authHelper.oauthReceive(data);
        Client.setMainAccount(account);
        if (checkAccount(activity))
        {
            startTwitter(activity);
            loadListPage(activity);
        }
    }

    public void accountSetup()
    {
        long lastUsedId = Client.getPreferenceValue(EnumPreferenceKey.LAST_USED_USER_ID);

        for (Account account : AuthenticationDB.instance().findAll())
        {
            if (account.getUserId() == lastUsedId)
            {
                Client.setMainAccount(account);
                break;
            }
        }
        if (Client.getMainAccount() == null)
        {
            Client.setMainAccount(AuthenticationDB.instance().findAll().get(0));
        }
    }

    public void loadListPage(Activity activity)
    {
        for (net.miz_hi.smileessence.data.list.List list : ListManager.getLists())
        {
            Timeline timeline = new ListTimeline();
            StatusListManager.registerListTimeline(list.getListId(), timeline, new StatusListAdapter(activity, timeline));
            ListFragment fragment = ListFragment.newInstance(list.getListId(), list.getName());
            PageController.getInstance().addPage(fragment);
        }
    }

    public void startTwitter(Activity activity)
    {

        boolean connected = TwitterManager.openTwitterStream(activity);

        if (connected)
        {
            final GetHomeTimelineTask getHome = new GetHomeTimelineTask(Client.getMainAccount(), new Paging(1, 100));
            final GetMentionsTask getMentions = new GetMentionsTask(Client.getMainAccount(), new Paging(1, 200));


            MyExecutor.execute(new Runnable()
            {

                @Override
                public void run()
                {
                    try
                    {

                        List<TweetModel> home = getHome.call();
                        List<TweetModel> mentions = getMentions.call();
                        for (TweetModel tweetModel : home)
                        {
                            StatusListManager.getHomeTimeline().addToBottom(tweetModel);
                            ExtractManager.check(tweetModel);
                        }
                        for (TweetModel tweetModel : mentions)
                        {
                            StatusListManager.getMentionsTimeline().addToBottom(tweetModel);
                        }
                        StatusListManager.getAdapter(StatusListManager.getHomeTimeline()).forceNotifyAdapter();
                        StatusListManager.getAdapter(StatusListManager.getMentionsTimeline()).forceNotifyAdapter();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        else
        {
            Notificator.alert("接続出来ません");
        }
    }

    public void receivePicture(Activity activity, Intent data, int reqCode)
    {
        try
        {
            Uri uri;
            if (reqCode == EnumRequestCode.PICTURE.ordinal())
            {
                uri = data.getData();
            }
            else
            {
                uri = tempFilePath;
            }
            Cursor c = activity.getContentResolver().query(uri, null, null, null, null);
            c.moveToFirst();
            String path = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
            PostSystem.getState().setPicturePath(path);
            PostSystem.openPostPage();
            Notificator.info("画像をセットしました");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Notificator.alert("失敗しました");
        }
    }
}