package crimsonclubs.uacs.android.crimsonclubs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EventAdapter extends BaseAdapter {

    private ArrayList<EventDto> mData = new ArrayList<>();

    public EventAdapter(ArrayList<EventDto> data) { mData = data; }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        EventDto targ = (EventDto) getItem(pos);

        if(convertView == null) {
            TextView tv;
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
            tv = (TextView) convertView.findViewById(R.id.name);
            tv.setText(targ.name);
            tv = (TextView) convertView.findViewById(R.id.description);
            tv.setText(targ.description);

        }


        return convertView;
    }



    @Override
    public int getViewTypeCount() {

        return 1;
    }

    @Override
    public int getItemViewType(int position) {

        return 0;

    }

}
