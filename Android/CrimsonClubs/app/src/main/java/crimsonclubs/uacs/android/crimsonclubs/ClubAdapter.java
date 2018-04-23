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

public class ClubAdapter extends BaseAdapter implements Filterable {

    private ArrayList<ClubDto> mData; //original data

    public ArrayList<ClubDto> orig;

    public ClubAdapter(ArrayList<ClubDto> data){
        super();
        mData  = data;
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

        ClubListItemHolder holder;

        ClubDto targ = (ClubDto) getItem(pos);

        if(convertView == null) {

                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.club_list_item, parent, false);
                holder = new ClubListItemHolder();

                holder.groupName = (TextView) convertView.findViewById(R.id.groupName);
                holder.clubDesc = (TextView) convertView.findViewById(R.id.clubDesc);
                holder.numMembers = (TextView) convertView.findViewById(R.id.numMembers);
                holder.locked = (ImageView) convertView.findViewById(R.id.isLocked);
                holder.selectBtn = (FancyButton) convertView.findViewById(R.id.btn_select);

            holder.groupName.setText(targ.groupName);

            holder.clubDesc.setText(targ.description);
            holder.numMembers.setText(targ.memberCount + " members");

            if(targ.isRequestToJoin){
                holder.locked.setVisibility(View.VISIBLE);
            }

        }
        else{
            holder = (ClubListItemHolder) convertView.getTag();
        }





        return convertView;
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
                            if (g.groupName.toLowerCase()
                                    .contains(constraint.toString().toLowerCase())) {
                                results.add(g);
                                Log.e("name", g.groupName);
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
                    Log.e("arr",a.groupName);
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
