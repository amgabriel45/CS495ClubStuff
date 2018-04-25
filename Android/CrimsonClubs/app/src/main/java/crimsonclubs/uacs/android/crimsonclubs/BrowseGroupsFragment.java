package crimsonclubs.uacs.android.crimsonclubs;


import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseGroupsFragment extends BaseFragment implements SearchView.OnQueryTextListener  {

    public ArrayList<GroupDto> objs = new ArrayList<>();
    public GroupAdapter adapter;

    public SearchView mSearchView;
    public ListView mListView;

    public BrowseGroupsFragment() {

    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_browse_groups, container, false);
    }

    @Override
    public void onResume(){
        super.onResume();



        updateList();

    }

    private void setupSearchView()
    {
        mSearchView = (SearchView) main.findViewById(R.id.searchBar);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search for groups...");

        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if(!queryTextFocused) {
                    mSearchView.setQuery("", false);
                }
            }
        });
    }

    public void updateList(){


        final Gson gson = new GsonBuilder().serializeNulls().create();

            //String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/clubs/all";
        String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/groups";

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
                        boolean isNull = false;

                        ArrayList<GroupDto> temp = new ArrayList<GroupDto>();

                        String body = response.body().string();
                        Log.e("groups",body);

                        try {
                            temp = new ArrayList<GroupDto>(Arrays.asList(gson.fromJson(body, GroupDto[].class)));
                        } catch (JsonSyntaxException e) {
                            temp.add(gson.fromJson(body, GroupDto.class));
                        }

                        if (temp.get(0) == null) { //read failed
                            isNull = true;
                        }

                        objs.clear();
                        objs.addAll(temp);


                        // Run view-related code back on the main thread
                        main.runOnUiThread(new Runnable() {
                                                               @Override
                                                               public void run() {

                                                                   adapter = new GroupAdapter(objs, (MainActivity) main);
                                                                   mListView = (ListView) main.findViewById(R.id.groupsList);

                                                                   //lv.setOnItemClickListener( infoItemClickListener());

                                                                   mListView.setAdapter(adapter);
                                                                   //mListView.setTextFilterEnabled(true);
                                                                   setupSearchView();

                                                                   adapter.notifyDataSetChanged();
                                                               }
                                                           }
                        );
                    }
                }

            });
        }

    @Override
    public boolean onQueryTextChange(String newText)
    {


        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {

        GroupAdapter adapter = (GroupAdapter) mListView.getAdapter();
        Filter filter = adapter.getFilter();

        if(TextUtils.isEmpty(query)){

            mListView.clearTextFilter();
        }
        else{
            mListView.setFilterText(query);
            filter.filter(query);
        }


        return false;
    }

        }


