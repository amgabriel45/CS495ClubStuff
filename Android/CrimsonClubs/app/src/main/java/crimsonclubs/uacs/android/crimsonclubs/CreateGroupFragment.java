package crimsonclubs.uacs.android.crimsonclubs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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


public class CreateGroupFragment extends BaseFragment implements AdapterView.OnItemSelectedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Spinner spinner;
    public ArrayList<GroupDto> groupList = new ArrayList<GroupDto>();
    //public List<GroupDto> groupList = new ArrayList<GroupDto>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArrayList<GroupDto> objs = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public CreateGroupFragment() {
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
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);


        FancyButton btnInput = (FancyButton) view.findViewById(R.id.btn_select);

        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputName = (EditText) getView().findViewById(R.id.inputName);
                final EditText inputDesc = (EditText) getView().findViewById(R.id.inputDesc);
                
                String groupName = inputName.getText().toString();
                String groupDesc = inputDesc.getText().toString();



                AddGroupDto newGroup = new AddGroupDto();
                newGroup.name = groupName;
                newGroup.description = groupDesc;

                View view2 = main.getCurrentFocus();
                if (view2 != null){
                    InputMethodManager imm = (InputMethodManager) main.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                }

                sendGroup(newGroup);
            }
        });

        return view;
    }




    public void sendGroup(final AddGroupDto newGroup) {
        String token;
        token = main.getIntent().getStringExtra("bearerToken");
        Log.e("token=",token);

        final Gson gson = new GsonBuilder().serializeNulls().create();
        //String url = "http://cgroups.us-east-2.elasticbeanstalk.com/api/events?token=" + token;
        String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/groups";


        Log.e("url",url);

        RequestBody requestBody = new FormBody.Builder()
                .add("name", newGroup.name)
                .add("description", newGroup.description)
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


                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    main.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(main, "Success", Toast.LENGTH_LONG).show();
                        }
                    });


                    BrowseGroupsFragment nextFrag = new BrowseGroupsFragment();
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
