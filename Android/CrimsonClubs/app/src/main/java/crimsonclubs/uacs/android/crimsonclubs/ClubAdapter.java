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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        final FancyButton selectBtn = (FancyButton) row.findViewById(R.id.btn_select);

        groupName.setText(targ.name);

        clubDesc.setText(targ.description);
        numMembers.setText(targ.memberCount + " members");


        if(targ.isRequestToJoin){
            locked.setVisibility(View.VISIBLE);

            if(!targ.isAccepted){
                if(!targ.hasRequested) {
                    selectBtn.setText("Request to Join");
                }
                else{
                    selectBtn.setText("Undo Request");
                }
            }
        }


        selectBtn.setOnClickListener(btnListener(targ,selectBtn));



        return row;
    }

    private View.OnClickListener btnListener(final ClubDto targ, final FancyButton btn){

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(targ.hasRequested && !targ.isAccepted){
                    requestLeave(targ,btn);
                }

                else if(targ.isRequestToJoin && !targ.isAccepted){
                    requestJoin(targ,btn);
                }
                else {
                    ViewClubFragment f = new ViewClubFragment();
                    f.currId = targ.id;
                    main.goToFragment(f);
                }
            }
        };
    }

    public void requestJoin(final ClubDto targ, final FancyButton btn){
        final Gson gson = new GsonBuilder().serializeNulls().create();

        String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/clubs/" + targ.id + "/join";
        Log.e("jurl",url);
        RequestBody body = RequestBody.create(null, new byte[]{}); // empty POST request

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + MainActivity.bearerToken)
                .post(body)
                .build();


        MainActivity.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                main.runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           Toast.makeText(main,
                                                   "Remote server could not be reached. "
                                                   ,Toast.LENGTH_LONG).show();
                                       }
                                   }

                );
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (!response.isSuccessful()) {
                    if (response.code() == 401) {
                        main.runOnUiThread(new Runnable() {
                                               @Override
                                               public void run() {
                                                   Toast.makeText(main,
                                                           "Authentication failed.",
                                                           Toast.LENGTH_LONG).show();

                                               }
                                           }

                        );
                    }
                    else{
                        main.runOnUiThread(new Runnable() {
                                               @Override
                                               public void run() {
                                                   Toast.makeText(main,
                                                           "An unspecified networking error has occurred\n" +
                                                                   "Error Code: " + response.code(),
                                                           Toast.LENGTH_LONG).show();

                                               }
                                           }

                        );
                    }
                }
                else {

                    // Run view-related code back on the main thread
                    main.runOnUiThread(new Runnable() {
                                           @Override
                                           public void run() {

                                               Toast.makeText(main, "Request successful", Toast.LENGTH_SHORT).show();
                                               BrowseClubsFragment f = (BrowseClubsFragment) main.getCurrentFragment();
                                               f.updateList();
                                           }
                                       }
                    );
                }
            }

        });
    }

    public void requestLeave(final ClubDto targ, final FancyButton btn){
        final Gson gson = new GsonBuilder().serializeNulls().create();

        String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/clubs/" + targ.id + "/leave";
        Log.e("lurl",url);
        RequestBody body = RequestBody.create(null, new byte[]{}); // empty POST request

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + MainActivity.bearerToken)
                .post(body)
                .build();


        MainActivity.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                main.runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           Toast.makeText(main,
                                                   "Remote server could not be reached. "
                                                   ,Toast.LENGTH_LONG).show();
                                       }
                                   }

                );
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (!response.isSuccessful()) {
                    if (response.code() == 401) {
                        main.runOnUiThread(new Runnable() {
                                               @Override
                                               public void run() {
                                                   Toast.makeText(main,
                                                           "Authentication failed.",
                                                           Toast.LENGTH_LONG).show();

                                               }
                                           }

                        );
                    }
                    else{
                        main.runOnUiThread(new Runnable() {
                                               @Override
                                               public void run() {
                                                   Toast.makeText(main,
                                                           "An unspecified networking error has occurred\n" +
                                                                   "Error Code: " + response.code(),
                                                           Toast.LENGTH_LONG).show();

                                               }
                                           }

                        );
                    }
                }
                else {

                    // Run view-related code back on the main thread
                    main.runOnUiThread(new Runnable() {
                                           @Override
                                           public void run() {

                                               Toast.makeText(main, "Request successfully undone", Toast.LENGTH_SHORT).show();
                                               BrowseClubsFragment f = (BrowseClubsFragment) main.getCurrentFragment();
                                               f.updateList();
                                           }
                                       }
                    );
                }
            }

        });
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
