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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    /*
    btnInput.setOnClickListener(new View.OnClickListener() {
        @Override
                public void onClick(View view) {
                    String eventName = inputName.getText().toString();
                    String eventDesc = inputDesc.getText().toString();
                    String eventStart = inputStart.getText().toString();
                    String eventFinish = inputFinish.getText().toString();

                    //Pass to Database
        }
    });
    */

   /*
    public static CreateEventFragment newInstance(String param1, String param2) {
        CreateEventFragment fragment = new CreateEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    */

    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
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

                ArrayList<Integer> clubs = new ArrayList<>();
                clubs.add(1);
                clubs.add(2);

                AddEventDto newEvent = new AddEventDto();
                newEvent.name = eventName;
                newEvent.description = eventDesc;
                newEvent.start = eventStart;
                newEvent.finish = eventFinish;
                newEvent.clubId = 1;
                newEvent.isGroupEvent = true;
                newEvent.clubIds = clubs;

                View view2 = getActivity().getCurrentFocus();
                if (view2 != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                }

                //Pass to Database
                sendEvent(newEvent);
            }
        });

        return view;
    }

    public void sendEvent(AddEventDto newEvent){
        String token;
        token = getActivity().getIntent().getStringExtra("bearerToken");
        Log.e("token=",token);

        final Gson gson = new GsonBuilder().serializeNulls().create();
        //String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/events?token=" + token;
        String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/events";

        Log.e("url",url);

        RequestBody requestBody = new FormBody.Builder()
                .add("name", newEvent.name)
                .add("description", newEvent.description)
                .add("start", newEvent.start)
                .add("finish", newEvent.finish)
                .add("isGroupEvent", "true")
                .add("clubId", "1")
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

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Failure", Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try  {
                    ResponseBody responseBody = response.body();
                    System.out.println("Response: " + response.toString());
                    System.out.println("ResponseBody: " + responseBody.string());
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    //Headers responseHeaders = response.headers();
                    /*
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }
                    */

                    //System.out.println(responseBody.string());

                    Context context = getActivity();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                        }
                    });

                    //Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();

                    //((MainActivity)getActivity()).goToLastFragment();

                    //change to view detailed event for event just created when finished
                    BrowseEventsFragment nextFrag = new BrowseEventsFragment();
                    FragmentTransaction fragTrans = getActivity().getFragmentManager().beginTransaction();
                    fragTrans
                            .setCustomAnimations(
                                    R.animator.card_flip_right_in,
                                    R.animator.card_flip_right_out,
                                    R.animator.card_flip_left_in,
                                    R.animator.card_flip_left_out)
                            .replace(R.id.container, nextFrag)
                            .addToBackStack(null)
                            .commit();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        /*
        try {
            Response response = MainActivity.client.newCall(request).enqueue(new Callback());

            Log.e("response", response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        /*
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,
                                    "Remote server could not be reached. ",
                                    Toast.LENGTH_LONG).show();
                        }
                });
            }
        });
        */
    }
    /*
    public void showSuccessDialog() {
        DialogFragment newFragment = new SuccessEventDialogFragment();
        newFragment.show();
    }
    */
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
