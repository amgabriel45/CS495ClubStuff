package crimsonclubs.uacs.android.crimsonclubs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.Filter;
import android.text.TextUtils;

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


public class BrowseEventsFragment extends BaseFragment implements SearchView.OnQueryTextListener{

    public ArrayList<EventDto> objs = new ArrayList<>();
    public EventAdapter adapter;

    public SearchView mSearchView;
    public ListView mListView;

    public int clubId = -1; //-1 if browsing all events, else returns list of events relevant to specified club

    public BrowseEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browse_events, container, false);
    }


    @Override
    public void onResume(){
        super.onResume();

        if(clubId == -1) {

            updateList();
        }
        else{

            getEventsForClubId();
        }

    }

    private void setupSearchView()
    {
        mSearchView = (SearchView)main.findViewById(R.id.searchBar);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search for events...");

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

        //Ignore this just something I found that could be useful for dates if what
        //I have now doesn't work
        /*
        import java.lang.reflect.Type;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.TimeZone;
        import com.google.gson.JsonDeserializationContext;
        import com.google.gson.JsonDeserializer;
        import com.google.gson.JsonElement;
        import com.google.gson.JsonParseException;

        public class DateDeserializer implements JsonDeserializer<Date> {

          @Override
          public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
              String date = element.getAsString();

              SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
              format.setTimeZone(TimeZone.getTimeZone("GMT"));

              try {
                  return format.parse(date);
              } catch (ParseException exp) {
                  System.err.println("Failed to parse Date:", exp);
                  return null;
              }
           }
        }
         */

        String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/events";


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

                    ArrayList<EventDto> temp = new ArrayList<EventDto>();

                    String body = response.body().string();


                    try {
                        temp = new ArrayList<EventDto>(Arrays.asList(gson.fromJson(body, EventDto[].class)));
                    } catch (JsonSyntaxException e) {
                        temp.add(gson.fromJson(body, EventDto.class));
                    }


                    objs.clear();
                    objs.addAll(temp);


                    // Run view-related code back on the main thread
                    main.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        adapter = new EventAdapter(objs, (MainActivity) main);

                                                        mListView = (ListView) main.findViewById(R.id.eventsList);


                                                        mListView.setAdapter(adapter);

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

        EventAdapter adapter = (EventAdapter) mListView.getAdapter();
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

    public void getEventsForClubId(){
        final Gson gson = new GsonBuilder().serializeNulls().create();

        String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/clubs/" + Integer.toString(clubId) + "/calendar";


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

                    ArrayList<EventDto> temp = new ArrayList<EventDto>();

                    String body = response.body().string();


                    try {
                        temp = new ArrayList<EventDto>(Arrays.asList(gson.fromJson(body, EventDto[].class)));
                    } catch (JsonSyntaxException e) {
                        temp.add(gson.fromJson(body, EventDto.class));
                    }

                    objs.clear();
                    objs.addAll(temp);


                    // Run view-related code back on the main thread
                    main.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        adapter = new EventAdapter(objs, (MainActivity) main);
                                                        mListView = (ListView) main.findViewById(R.id.eventsList);
                                                        //final TextView tv = main.findViewById(R.id.eventsList);

                                                        //lv.setOnItemClickListener( infoItemClickListener());

                                                        mListView.setAdapter(adapter);
                                                        //tv.setAdapter(adapter);

                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }
                    );
                }
            }

        });
    }
}
