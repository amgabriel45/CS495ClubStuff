package crimsonclubs.uacs.android.crimsonclubs;

import android.app.DialogFragment;
import android.app.DownloadManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class CreateEventFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Spinner spinner;
    public ArrayList<ClubDto> clubList = new ArrayList<ClubDto>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArrayList<ClubDto> objs = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    /*
    EditText inputName = (EditText) getView().findViewById(R.id.inputName);
    EditText inputDesc = (EditText) getView().findViewById(R.id.inputDesc);
    EditText inputStart = (EditText) getView().findViewById(R.id.inputStartDate);
    EditText inputFinish = (EditText) getView().findViewById(R.id.inputFinishDate);
    Button btnInput = (Button) getView().findViewById(R.id.btn_select);
    */

    public CreateEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_event, container, false);

        addClubsSpinner();

        FancyButton btnInput = (FancyButton) view.findViewById(R.id.btn_select);

        // Inflate the layout for this fragment
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputName = (EditText) getView().findViewById(R.id.inputName);
                final EditText inputDesc = (EditText) getView().findViewById(R.id.inputDesc);
                final EditText inputStart = (EditText) getView().findViewById(R.id.inputStartDate);
                final EditText inputFinish = (EditText) getView().findViewById(R.id.inputFinishDate);

                String eventName = inputName.getText().toString();
                String eventDesc = inputDesc.getText().toString();
                String eventStart = inputStart.getText().toString();
                String eventFinish = inputFinish.getText().toString();
                String eventClub = String.valueOf(spinner.getSelectedItem());

                ArrayList<Integer> clubs = new ArrayList<>();
                clubs.add(1);
                clubs.add(2);

                int clubId = getClubId(eventClub);

                AddEventDto newEvent = new AddEventDto();
                newEvent.name = eventName;
                newEvent.description = eventDesc;
                newEvent.start = eventStart;
                newEvent.finish = eventFinish;
                newEvent.clubId = clubId;
                newEvent.isGroupEvent = true;
                newEvent.clubIds = clubs;

                View view2 = main.getCurrentFocus();
                if (view2 != null) {
                    InputMethodManager imm = (InputMethodManager) main.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                }

                //Pass to Database
                sendEvent(newEvent);
            }
        });

        return view;
    }

    public void addClubsSpinner() {
        spinner = (Spinner) getActivity().findViewById(R.id.club_spinner);

        String token;
        token = getActivity().getIntent().getStringExtra("bearerToken");
        Log.e("token=", token);

        final Gson gson = new GsonBuilder().serializeNulls().create();
        //String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/events?token=" + token;
        String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/clubs/all";

        Log.e("url", url);

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
                                                            , Toast.LENGTH_LONG).show();
                                                }
                                            }

                );
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (!response.isSuccessful()) {
                    if (response.code() == 401) {
                        System.out.println("Response: " + response.toString());
                        getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getActivity(),
                                                                    "Authentication failed.",
                                                                    Toast.LENGTH_LONG).show();

                                                        }
                                                    }

                        );
                    } else {
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
                } else {
                    String body = response.body().string();
                    boolean isNull = false;

                    try {
                        clubList = new ArrayList<ClubDto>(Arrays.asList(gson.fromJson(body, ClubDto[].class)));
                    } catch (JsonSyntaxException e) {
                        clubList.add(gson.fromJson(body, ClubDto.class));
                    }

                    if (clubList.get(0) == null) { //read failed
                        isNull = true;
                    }

                    objs.clear();
                    objs.addAll(clubList);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Context context = getActivity();
                            spinner = (Spinner) getActivity().findViewById(R.id.club_spinner);

                            ArrayList<String> clubNameList = new ArrayList<>();

                            for (int i = 0; i < clubList.size(); i++) {
                                clubNameList.add(clubList.get(i).name);
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                                    android.R.layout.simple_spinner_item, clubNameList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(dataAdapter);

                        }
                    });
                }
            }
        });
    }

    public int getClubId(String clubName) {
        for (int i = 0; i < clubList.size(); i++) {
            if (clubList.get(i).name.compareTo(clubName) == 0)
                return clubList.get(i).id;
        }

        return -1;
    }

    public void sendEvent(final AddEventDto newEvent){
        String token;
        token = main.getIntent().getStringExtra("bearerToken");
        Log.e("token=",token);

        final Gson gson = new GsonBuilder().serializeNulls().create();
        //String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/events?token=" + token;
        String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/events";

        Log.e("url",url);

        String cname = String.valueOf(newEvent.clubId);

        RequestBody requestBody = new FormBody.Builder()
                .add("name", newEvent.name)
                .add("description", newEvent.description)
                .add("start", newEvent.start)
                .add("finish", newEvent.finish)
                .add("isGroupEvent", "true")
                .add("clubId", cname)
                .add("clubIds", "2")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + MainActivity.bearerToken)
                .post(requestBody)
                .build();

        System.out.println(request.toString());

        MainActivity.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(main, "Failure", Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try  {
                    ResponseBody responseBody = response.body();
                    System.out.println("Response: " + response.toString());
                    System.out.println("ResponseBody: " + responseBody.string());
                    if (!response.isSuccessful()) {
                        //do more stuff here
                        throw new IOException("Unexpected code " + response);
                    }

                    //Headers responseHeaders = response.headers();
                    /*
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }
                    */

                    //System.out.println(responseBody.string());

                    Context context = main;

                    main.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(main, "Success", Toast.LENGTH_LONG).show();
                        }
                    });

                    //Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();

                    //((MainActivity)main).goToLastFragment();

                    //change to view detailed event for event just created when finished
                    BrowseEventsFragment nextFrag = new BrowseEventsFragment();

                    nextFrag.clubId = newEvent.clubId;
                    main.goToFragment(nextFrag);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();

        //updateList();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

