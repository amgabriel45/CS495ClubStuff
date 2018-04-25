package crimsonclubs.uacs.android.crimsonclubs;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class JoinRequestAdapter extends BaseAdapter implements Filterable {

    private ArrayList<JoinRequestDto> mData; //original data

    public MainActivity main;

    public ArrayList<JoinRequestDto> orig;

    public int clubId;

    public JoinRequestAdapter(ArrayList<JoinRequestDto> data, MainActivity m, int id){
        super();
        mData  = data;
        main = m;
        clubId = id;
        orig = (ArrayList<JoinRequestDto>) mData.clone();
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
            row = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_item, parent, false);
        }
        else{
            row = convertView;
        }

        final JoinRequestDto targ = (JoinRequestDto) getItem(pos);

        TextView userName = (TextView) row.findViewById(R.id.userName);
        FancyButton selectBtn = (FancyButton) row.findViewById(R.id.btn_accept);

        userName.setText(targ.first + " " + targ.last);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryAcceptUser(targ.userId);
            }
        });



        return row;
    }

    public void tryAcceptUser(int userId){

        final Gson gson = new GsonBuilder().serializeNulls().create();

        //String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/clubs/all";
        String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/clubs/" + clubId + "/requests?userId=" + userId + "&accept=true";

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + MainActivity.bearerToken)
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

                                               Toast.makeText(main, "Successfully accepted user", Toast.LENGTH_SHORT).show();
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
                final ArrayList<JoinRequestDto> results = new ArrayList<JoinRequestDto>();
                if (orig == null)
                    orig = mData;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final JoinRequestDto g : orig) {
                            if (g.first.toLowerCase()
                                    .contains(constraint.toString().toLowerCase())) {
                                results.add(g);

                            } else {
                                if (g.last.toLowerCase()
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

                mData = (ArrayList<JoinRequestDto>) results.values;
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
