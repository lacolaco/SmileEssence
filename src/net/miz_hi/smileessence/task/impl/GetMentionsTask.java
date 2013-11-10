package net.miz_hi.smileessence.task.impl;

import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.model.status.ResponseConverter;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.notification.Notificator;
import net.miz_hi.smileessence.task.Task;
import net.miz_hi.smileessence.twitter.API;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.LinkedList;
import java.util.List;

public class GetMentionsTask extends Task<List<TweetModel>>
{

    private Account account;
    private Paging page;

    public GetMentionsTask(Account account, Paging page)
    {
        this.account = account;
        this.page = page;
    }

    @Override
    public List<TweetModel> call()
    {
        LinkedList<Status> resp = new LinkedList<Status>();
        try
        {
            resp.addAll(API.getMentions(account, page));
        }
        catch (TwitterException e)
        {
            e.printStackTrace();
        }

        LinkedList<TweetModel> list = new LinkedList<TweetModel>();
        while (!resp.isEmpty())
        {
            TweetModel model = ResponseConverter.convert(resp.pollLast());
            list.offerFirst(model);
        }
        return list;
    }

    @Override
    public void onPreExecute()
    {
    }

    @Override
    public void onPostExecute(List<TweetModel> result)
    {
        if (result.size() < 1)
        {
            Notificator.alert("メンションの取得に失敗しました");
        }
    }

}
