package crimsonclubs.uacs.android.crimsonclubs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
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


public class BrowseEventsFragment extends BaseFragment {

    public ArrayList<EventDto> objs = new ArrayList<>();
    public EventAdapter adapter;

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

        updateList();

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
                getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getActivity(),
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
                        getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getActivity(),
                                                                    "Authentication failed.",
                                                                    Toast.LENGTH_LONG).show();

                                                        }
                                                    }

                        );
                    }
                    else{
                        getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getActivity(),
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

                    ArrayList<EventDto> temp = new ArrayList<EventDto>();

                    String body = response.body().string();


                    try {
                        temp = new ArrayList<EventDto>(Arrays.asList(gson.fromJson(body, EventDto[].class)));
                    } catch (JsonSyntaxException e) {
                        temp.add(gson.fromJson(body, EventDto.class));
                    }

                    if (temp.get(0) == null) { //read failed
                        isNull = true;
                    }

                    objs.clear();
                    objs.addAll(temp);


                    // Run view-related code back on the main thread
                    getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        adapter = new EventAdapter(objs);
                                                        final ListView lv = (ListView) getActivity().findViewById(R.id.eventsList);
                                                        //final TextView tv = getActivity().findViewById(R.id.eventsList);

                                                        //lv.setOnItemClickListener( infoItemClickListener());

                                                        lv.setAdapter(adapter);
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
