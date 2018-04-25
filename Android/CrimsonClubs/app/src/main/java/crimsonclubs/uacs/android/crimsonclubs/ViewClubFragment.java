package crimsonclubs.uacs.android.crimsonclubs;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewClubFragment extends BaseFragment {


    public int currId;

    public ClubDto currClub;

    public ArrayList<MemberDto> members = new ArrayList<>();
    public ArrayList<EventDto> events = new ArrayList<>();

    public ViewClubFragment() {

    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // Set listener for edit button and launch edit club fragment if clicked
        View view = inflater.inflate(R.layout.fragment_view_club, container, false);

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();


        updateUI();

    }


    public void updateUI(){


        final Gson gson = new GsonBuilder().serializeNulls().create();

            String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/clubs/" + Integer.toString(currId);
            Log.e("url",url);

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

                        ArrayList<ClubDto> temp = new ArrayList<ClubDto>();

                        String body = response.body().string();


                        try {
                            temp = new ArrayList<ClubDto>(Arrays.asList(gson.fromJson(body, ClubDto[].class)));
                        } catch (JsonSyntaxException e) {
                            temp.add(gson.fromJson(body, ClubDto.class));
                        }

                        if (temp.get(0) == null) { //read failed
                            isNull = true;
                        }
                        else{
                            currClub = temp.get(0);
                        }


                        // Run view-related code back on the main thread
                        getActivity().runOnUiThread(new Runnable() {
                                                               @Override
                                                               public void run() {


                                                                   Log.e("name",currClub.name);
                                                                   Log.e("num",Integer.toString(currClub.members.size()));

                                                                   TextView name = (TextView) getActivity().findViewById(R.id.name);
                                                                   name.setText(currClub.name);
                                                                   TextView desc = (TextView) getActivity().findViewById(R.id.description);
                                                                   desc.setText(currClub.description);
                                                                   TextView numMembers = (TextView) getActivity().findViewById(R.id.numMembers);

                                                                   numMembers.setText(currClub.memberCount + " members");

                                                                   BrowseEventsFragment f = new BrowseEventsFragment();
                                                                   f.clubId = currClub.id;

                                                                   getFragmentManager()
                                                                           .beginTransaction()



                                                                           .replace(R.id.eventsContainer, f, "curr")

                                                                           .commit();

                                                                   FancyButton btnInput = (FancyButton) getActivity().findViewById(R.id.btn_edit);

                                                                   if (currClub.isAdmin){
                                                                       btnInput.setVisibility(View.VISIBLE);
                                                                   }

                                                                   btnInput.setOnClickListener(new View.OnClickListener() {
                                                                       @Override
                                                                       public void onClick(View view) {
                                                                           Bundle bundle = new Bundle();
                                                                           bundle.putInt("id",currClub.id);
                                                                           EditClubFragment editClub = new EditClubFragment();
                                                                           editClub.setArguments(bundle);

                                                                           main.goToFragment(editClub);
                                                                       }
                                                                   });

                                                                   btnInput = (FancyButton) getActivity().findViewById(R.id.btn_join);

                                                                   btnInput.setText(currClub.isAccepted ? "Leave" : "Join");
                                                                   btnInput.setOnClickListener(new View.OnClickListener() {
                                                                       @Override
                                                                       public void onClick(View view) {
                                                                           if(currClub.isAccepted){
                                                                               tryLeave(currClub.id);
                                                                           }
                                                                           else{
                                                                               tryJoin(currClub.id);
                                                                           }
                                                                       }
                                                                   });

                                                               }
                                                           }
                        );
                    }
                }

            });
        }

        public void tryJoin(int clubId){
            final Gson gson = new GsonBuilder().serializeNulls().create();

            String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/clubs/" + clubId + "/join";


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

                                                                Toast.makeText(main, "Successfully joined club", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                        );
                    }
                }

            });
        }

    public void tryLeave(int clubId){
        final Gson gson = new GsonBuilder().serializeNulls().create();

        String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/clubs/" + clubId + "/leave";


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

                                               Toast.makeText(main, "Successfully left club", Toast.LENGTH_SHORT).show();

                                           }
                                       }
                    );
                }
            }

        });
    }


}


