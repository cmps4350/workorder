package edu.csub.cs.WorkOrderApp.activity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.csub.cs.WorkOrderApp.R;


public class WOFeedListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<WorkOrderHolder> feedItems;

    public WOFeedListAdapter(Activity activity, List<WorkOrderHolder> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.order_list, null);

        TextView wo_no = (TextView) convertView.findViewById(R.id.order_no);
        TextView created_date = (TextView) convertView.findViewById(R.id.created_date);

        WorkOrderHolder item = feedItems.get(position);

        wo_no.setText("Work Order #00" + item.getId());
        created_date.setText("Created On: " +item.getCreateDate());
        return convertView;
    }
}
