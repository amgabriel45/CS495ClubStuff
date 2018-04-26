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

        return view;
    }


    public void sendClub(final EditClubDto newClub) {

        int id = this.getArguments().getInt("id");
        String token;
        token = main.getIntent().getStringExtra("bearerToken");
        Log.e("token=",token);

        final Gson gson = new GsonBuilder().serializeNulls().create();
        //String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/events?token=" + token;
        String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/clubs";

        Log.e("url",url);



        RequestBody requestBody = new FormBody.Builder()
                .add("name", (newClub.name.length() == 0 ? this.getArguments().getString("name") : newClub.name))
                .add("description", (newClub.description.length() == 0 ? this.getArguments().getString("desc") : newClub.description))
                .add("isRequestToJoin", Boolean.toString(this.getArguments().getBoolean("isRequestToJoin")))
                .add("id", Integer.toString(id))
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
    public void onResume() {
        super.onResume();

        FancyButton btnInput = (FancyButton) main.findViewById(R.id.btn_select);

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

                View view2 = main.getCurrentFocus();
                if (view2 != null){
                    InputMethodManager imm = (InputMethodManager) main.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                }

                sendClub(newClub);
            }
        });

        FancyButton btnInput2 = (FancyButton) main.findViewById(R.id.btn_approve_member);

        btnInput2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcceptUsersFragment acceptUsers = new AcceptUsersFragment();
                main.goToFragment(acceptUsers);
            }
        });

        FancyButton btnInput3 = (FancyButton) main.findViewById(R.id.btn_delete);

        final int clubId = getArguments().getInt("id");

        btnInput3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteClub(clubId);
            }
        });

    }

    public void deleteClub(int clubId){
        final Gson gson = new GsonBuilder().serializeNulls().create();

        String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/clubs/" + clubId ;

        RequestBody body = RequestBody.create(null, new byte[]{}); // empty POST request

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + MainActivity.bearerToken)
                .delete()
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

                                               Toast.makeText(main, "Successfully deleted club", Toast.LENGTH_SHORT).show();
                                               main.refreshUserClubs();

                                               BrowseClubsFragment f = new BrowseClubsFragment();
                                               f.currId = getArguments().getInt("groupId");

                                               main.goToFragment(f);

                                           }
                                       }
                    );
                }
            }

        });
    }



}
