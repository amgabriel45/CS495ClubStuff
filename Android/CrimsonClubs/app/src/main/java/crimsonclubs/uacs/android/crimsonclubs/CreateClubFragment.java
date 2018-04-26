package crimsonclubs.uacs.android.crimsonclubs;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class CreateClubFragment extends BaseFragment implements AdapterView.OnItemSelectedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Spinner spinner;
    public ArrayList<GroupDto> groupList = new ArrayList<>();
    //public List<GroupDto> groupList = new ArrayList<GroupDto>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArrayList<GroupDto> objs = new ArrayList<>();
    public ClubAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public CreateClubFragment() {
        // Required empty public constructor
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        //parent.getItemAtPosition(pos);
    }
    public void onNothingSelected(AdapterView<?> parent) {
        //Do stuff
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_club, container, false);



        return view;
    }

    public int getGroupId(String groupName) {

        for (int i = 0; i < groupList.size(); i++) {
            if (groupList.get(i).name.compareTo(groupName) == 0)
                return groupList.get(i).id;
        }

        return -1;
    }

    public void addItemsSpinner() {
        spinner = (Spinner) main.findViewById(R.id.group_spinner);
        //spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        List<String> list = new ArrayList<String>();

        String token;
        token = main.getIntent().getStringExtra("bearerToken");
        Log.e("token=", token);

        final Gson gson = new GsonBuilder().serializeNulls().create();
        //String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/events?token=" + token;
        String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/groups";

        Log.e("url", url);

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
                        main.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(main,
                                                                    "Authentication failed.",
                                                                    Toast.LENGTH_LONG).show();

                                                        }
                                                    }

                        );
                    } else {
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
                } else {

                    //ArrayList<GroupDto> temp = new ArrayList<GroupDto>();

                    String body = response.body().string();


                    try {
                        groupList = new ArrayList<GroupDto>(Arrays.asList(gson.fromJson(body, GroupDto[].class)));
                    } catch (JsonSyntaxException e) {
                        groupList.add(gson.fromJson(body, GroupDto.class));
                    }

                    objs.clear();
                    objs.addAll(groupList);


                    // Run view-related code back on the main thread
                    main.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Context context = main;
                            spinner = (Spinner) main.findViewById(R.id.group_spinner);

                            ArrayList<String> groupNameList = new ArrayList<>();

                            for (int i = 0; i < groupList.size(); i++) {
                                groupNameList.add(groupList.get(i).name);
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                                    android.R.layout.simple_spinner_item, groupNameList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(dataAdapter);

                        }
                    });
                }
            }
        });
    }


    public void sendClub(final AddClubDto newClub) {
        String token;
        token = main.getIntent().getStringExtra("bearerToken");
        Log.e("token=",token);

        final Gson gson = new GsonBuilder().serializeNulls().create();
        //String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/events?token=" + token;
        String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/clubs";

        String gname = String.valueOf(newClub.groupId);
        String isRequest = String.valueOf(newClub.isRequestToJoin);

        Log.e("url",url);

        RequestBody requestBody = new FormBody.Builder()
                .add("name", newClub.name)
                .add("description", newClub.description)
                .add("isRequestToJoin", isRequest)
                .add("groupId", gname)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + MainActivity.bearerToken)
                .post(requestBody)
                .build();

        MainActivity.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               e.printStackTrace();

               main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(main, "Fail", Toast.LENGTH_LONG).show();
                    }
               });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    ResponseBody responseBody = response.body();

                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    main.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(main, "Success", Toast.LENGTH_LONG).show();
                        }
                    });

                    //change to view detailed club for club just created when finished
                    main.refreshUserClubs();
                    BrowseClubsFragment nextFrag = new BrowseClubsFragment();
                    nextFrag.currId = newClub.groupId;
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

        //Add items to spinner dropdown
        addItemsSpinner();

        FancyButton btnInput = (FancyButton) main.findViewById(R.id.btn_select);

        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputName = (EditText) getView().findViewById(R.id.inputName);
                final EditText inputDesc = (EditText) getView().findViewById(R.id.inputDesc);
                spinner = (Spinner) main.findViewById(R.id.group_spinner);
                CheckBox check1 = (CheckBox) main.findViewById(R.id.checkPrivate);

                String clubName = inputName.getText().toString();
                String clubDesc = inputDesc.getText().toString();
                String groupName = String.valueOf(spinner.getSelectedItem());
                //System.out.println(groupName);

                //This could be -1 do errors
                int groupId = getGroupId(groupName);

                AddClubDto newClub = new AddClubDto();
                newClub.name = clubName;
                newClub.description = clubDesc;

                if(check1.isChecked())
                    newClub.isRequestToJoin = true;
                else
                    newClub.isRequestToJoin = false;

                newClub.groupId = groupId;

                View view2 = main.getCurrentFocus();
                if (view2 != null){
                    InputMethodManager imm = (InputMethodManager) main.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                }

                sendClub(newClub);
            }
        });

        //updateList();
    }

    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    */

    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    */

    /*
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    */

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
