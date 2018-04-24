package crimsonclubs.uacs.android.crimsonclubs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

public class EventAdapter extends BaseAdapter {

    private ArrayList<EventDto> mData = new ArrayList<>();

    public MainActivity main;

    public ArrayList<EventDto> orig;

    public EventAdapter(ArrayList<EventDto> data, MainActivity m) {
        super();
        mData = data;
        main = m;
        orig = (ArrayList<EventDto>) mData.clone();
    }

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

        final EventDto targ = (EventDto) getItem(pos);

        View row = null;

        if(convertView == null) {
            row = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        }
        else {
            row = convertView;
        }

        TextView tv;
        tv = (TextView) row.findViewById(R.id.name);
        tv.setText(targ.name);
        tv = (TextView) row.findViewById(R.id.description);
        tv.setText(targ.description);

        FancyButton selectBtn = (FancyButton) row.findViewById(R.id.btn_select);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewEventFragment f = new ViewEventFragment();
                f.currId = targ.id;
                main.goToFragment(f);
            }
        });


        return row;
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
