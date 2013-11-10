package net.miz_hi.smileessence.statuslist;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.listener.StatusOnClickListener;
import net.miz_hi.smileessence.model.status.IStatusModel;
import net.miz_hi.smileessence.model.statuslist.StatusList;
import net.miz_hi.smileessence.status.StatusViewFactory;
import net.miz_hi.smileessence.util.CustomListAdapter;

import java.util.Collection;

public class StatusListAdapter extends CustomListAdapter<IStatusModel>
{

    private StatusList statusList;
    private IStatusModel[] statusArray;
    private int count;

    public StatusListAdapter(Activity activity, StatusList statusList)
    {
        super(activity, 0);
        this.statusList = statusList;
    }

    @Deprecated
    @Override
    public void addAll(Collection<IStatusModel> collection)
    {
    }

    @Deprecated
    @Override
    public void addFirst(IStatusModel element)
    {
    }

    @Deprecated
    @Override
    public void addLast(IStatusModel element)
    {
    }

    @Deprecated
    @Override
    public void removeElement(IStatusModel element)
    {
    }

    @Deprecated
    @Override
    public void clear()
    {
    }

    @Override
    public View getView(int position, View convertedView, ViewGroup parent)
    {
        if (convertedView == null)
        {
            convertedView = getInflater().inflate(R.layout.status_layout, null);
        }
        IStatusModel model = (IStatusModel) getItem(position);

        int colorBg;
        if (position % 2 == 0)
        {
            colorBg = Client.getColor(R.color.White);
        }
        else
        {
            colorBg = Client.getColor(R.color.LightGray);
        }
        convertedView.setBackgroundColor(colorBg);

        convertedView = StatusViewFactory.newInstance(getInflater(), convertedView).getStatusView(model);

        convertedView.setOnClickListener(new StatusOnClickListener(getActivity(), model));

        return convertedView;
    }

    @Override
    public void notifyDataSetChanged()
    {
        statusArray = statusList.getStatusList();
        count = statusArray.length;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return count;
    }

    @Override
    public Object getItem(int arg0)
    {
        if (statusArray != null && statusArray.length >= arg0)
        {
            return statusArray[arg0];
        }
        else
        {
            return null;
        }
    }

    @Override
    public long getItemId(int arg0)
    {
        return arg0;
    }
}
