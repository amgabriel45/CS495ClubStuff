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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class EditClubFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EditClubFragment() {
        // Required empty public constructor
    }

    /*
    public static CreateClubFragment newInstance(String param1, String param2) {
        CreateClubFragment fragment = new CreateClubFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_club, container, false);
        FancyButton btnInput = (FancyButton) view.findViewById(R.id.btn_select);

        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputName = (EditText) getView().findViewById(R.id.inputName);
                final EditText inputDesc = (EditText) getView().findViewById(R.id.inputDesc);

                String clubName = inputName.getText().toString();
                String clubDesc = inputDesc.getText().toString();

                EditClubDto newClub = new EditClubDto();
                newClub.name = clubName;
                newClub.description = clubDesc;
                newClub.isRequestToJoin = true;
                newClub.groupId = 1;

                View view2 = getActivity().getCurrentFocus();
                if (view2 != null){
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                }

                sendClub(newClub);
            }
        });

        return view;
    }


    public void sendClub(EditClubDto newClub) {
        String token;
        token = getActivity().getIntent().getStringExtra("bearerToken");
        Log.e("token=",token);

        final Gson gson = new GsonBuilder().serializeNulls().create();
        //String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/events?token=" + token;
        String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/clubs";

        Log.e("url",url);

        RequestBody requestBody = new FormBody.Builder()
                .add("name", newClub.name)
                .add("description", newClub.description)
                .add("isRequestToJoin", "true")
                .add("groupId", "1")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + MainActivity.bearerToken)
                .put(requestBody)
                .build();

        MainActivity.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Fail", Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    ResponseBody responseBody = response.body();

                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                        }
                    });

                    //change to view detailed club for club just created when finished
                    BrowseClubsFragment nextFrag = new BrowseClubsFragment();
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
    }

    @Override
    public void onResume() {
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
