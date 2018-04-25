package crimsonclubs.uacs.android.crimsonclubs;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

public class GroupAdapter extends BaseAdapter implements Filterable {

    private ArrayList<GroupDto> mData; //original data

    public MainActivity main;

    public ArrayList<GroupDto> orig;

    public GroupAdapter(ArrayList<GroupDto> data, MainActivity m){
        super();
        mData  = data;
        main = m;
        orig = (ArrayList<GroupDto>) mData.clone();
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

        View row = null;

        if(convertView == null) {
            row = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_list_item, parent, false);
        }
        else{
            row = convertView;
        }

        final GroupDto targ = (GroupDto) getItem(pos);

        TextView groupName = (TextView) row.findViewById(R.id.groupName);
        TextView groupDesc = (TextView) row.findViewById(R.id.groupDesc);
        TextView numClubs = (TextView) row.findViewById(R.id.numClubs);

        FancyButton selectBtn = (FancyButton) row.findViewById(R.id.btn_select);

        groupName.setText(targ.name);

        groupDesc.setText(targ.description);
        numClubs.setText(targ.clubCount + " clubs");

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BrowseClubsFragment f = new BrowseClubsFragment();
                f.currId = targ.id;
                main.goToFragment(f);
            }
        });



        return row;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<GroupDto> results = new ArrayList<GroupDto>();
                if (orig == null)
                    orig = mData;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final GroupDto g : orig) {
                            if (g.name.toLowerCase()
                                    .contains(constraint.toString().toLowerCase())) {
                                results.add(g);

                            } else {
                                if (g.description.toLowerCase()
                                        .contains(constraint.toString().toLowerCase())) {
                                    results.add(g);

                                }
                            }
                        }
                        oReturn.values = results;
                    }
                }


                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {


                mData = (ArrayList<GroupDto>) results.values;
                notifyDataSetChanged();
            }
        };
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
