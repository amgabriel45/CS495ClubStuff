package crimsonclubs.uacs.android.crimsonclubs;


import android.app.Activity;
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

public class ClubAdapter extends BaseAdapter implements Filterable {

    private ArrayList<ClubDto> mData; //original data

    public MainActivity main;

    public ArrayList<ClubDto> orig;

    public ClubAdapter(ArrayList<ClubDto> data, MainActivity m){
        super();
        mData  = data;
        main = m;
        orig = (ArrayList<ClubDto>) mData.clone();
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

    public class ClubListItemHolder
    {
        TextView groupName;
        TextView clubDesc;
        TextView numMembers;
        ImageView locked;
        FancyButton selectBtn;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        View row = null;

        if(convertView == null) {
            row = LayoutInflater.from(parent.getContext()).inflate(R.layout.club_list_item, parent, false);
        }
        else{
            row = convertView;
        }

        final ClubDto targ = (ClubDto) getItem(pos);

        TextView groupName = (TextView) row.findViewById(R.id.groupName);
        TextView clubDesc = (TextView) row.findViewById(R.id.clubDesc);
        TextView numMembers = (TextView) row.findViewById(R.id.numMembers);
        ImageView locked = (ImageView) row.findViewById(R.id.isLocked);
        FancyButton selectBtn = (FancyButton) row.findViewById(R.id.btn_select);

        groupName.setText(targ.name);

        clubDesc.setText(targ.description);
        numMembers.setText(targ.memberCount + " members");

        if(targ.isRequestToJoin){
            locked.setVisibility(View.VISIBLE);
        }

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

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<ClubDto> results = new ArrayList<ClubDto>();
                if (orig == null)
                    orig = mData;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final ClubDto g : orig) {
                            if (g.name.toLowerCase()
                                    .contains(constraint.toString().toLowerCase())) {
                                results.add(g);
                                Log.e("name", g.name);
                            } else {
                                if (g.description.toLowerCase()
                                        .contains(constraint.toString().toLowerCase())) {
                                    results.add(g);
                                    Log.e("desc", g.description);
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

                for(ClubDto a : (ArrayList<ClubDto>) results.values){
                    Log.e("arr",a.name);
                }
                mData = (ArrayList<ClubDto>) results.values;
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
