package crimsonclubs.uacs.android.crimsonclubs;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity

{

    public Toolbar toolbar;
    public static GoogleSignInClient mGoogleSignInClient;

    private BaseFragment currFragment;

    public static String bearerToken;
    public static UserDto currUser;
    public ArrayList<ClubDto> userClubs = new ArrayList<>();

    public static OkHttpClient client;

    public Drawer navDrawer = null;

    private Stack<BaseFragment> backStack = new Stack<>();



    //private static SQLiteDatabaseHandler db;

    //Lifecycle/General Android Functions

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        client = getUnsafeOkHttpClient(); //sets up client that accepts any SSL certificate

        initDrawers(toolbar); //build nav drawer and bookmark drawer


    }

    public void refreshUserClubs(){
        final Gson gson = new GsonBuilder().serializeNulls().create();

        String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/clubs";


        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + MainActivity.bearerToken)
                .build();


        MainActivity.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                MainActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(MainActivity.this,
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
                        // do nothing
                    }
                    else{
                        MainActivity.this.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(MainActivity.this,
                                                                    "An unspecified networking error has occurred\n" +
                                                                            "Error Code: " + response.code(),
                                                                    Toast.LENGTH_LONG).show();

                                                        }
                                                    }

                        );
                    }
                }
                else {


                    ArrayList<ClubDto> temp = new ArrayList<ClubDto>();

                    String body = response.body().string();


                    try {
                        temp = new ArrayList<ClubDto>(Arrays.asList(gson.fromJson(body, ClubDto[].class)));
                    } catch (JsonSyntaxException e) {
                        temp.add(gson.fromJson(body, ClubDto.class));
                    }

                    userClubs = temp;

                    // Run view-related code back on the main thread
                    MainActivity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {


                                                        refreshNavDrawer();

                                                    }
                                                }
                    );
                }
            }

        });
    }


    private  Drawer.OnDrawerItemClickListener listener(final BaseFragment fragmentType){ //handles what happens when poi draweritem is clicked

        Drawer.OnDrawerItemClickListener result = new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                if(drawerItem != null) {


                    if(currFragment != null){
                        backStack.push(currFragment);
                    }

                    currFragment = fragmentType;

                    getFragmentManager()
                            .beginTransaction()

                            .setCustomAnimations(
                                    R.animator.card_flip_right_in,
                                    R.animator.card_flip_right_out,
                                    R.animator.card_flip_left_in,
                                    R.animator.card_flip_left_out)

                            .replace(R.id.container, currFragment)
                            .commit();


                    navDrawer.closeDrawer();
                }

                return false;
            }
        };

        return result;
    }

    public void goToLastFragment(){

        try {
            BaseFragment lastFragment = backStack.pop();

            if (lastFragment != null) {
                Log.e("nm", lastFragment.getClass().toString());
                getFragmentManager()
                        .beginTransaction()

                        .setCustomAnimations(
                                R.animator.card_flip_right_in,
                                R.animator.card_flip_right_out,
                                R.animator.card_flip_left_in,
                                R.animator.card_flip_left_out)

                        .replace(R.id.container, lastFragment)

                        .commit();

                currFragment = lastFragment;

                navDrawer.closeDrawer();
            }
        }
        catch(java.util.EmptyStackException e){
            e.printStackTrace();
        }

    }

    public void goToFragment(BaseFragment fragment){

        backStack.push(currFragment);

        currFragment = fragment;

        getFragmentManager()
                .beginTransaction()

                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)

                .replace(R.id.container, currFragment, "curr")

                .commit();


    }


    public void refreshNavDrawer(){

        initDrawers(toolbar);

        SecondaryDrawerItem item;


        navDrawer.addItem( new SectionDrawerItem().withName("My Groups").withDivider(false));

        for(ClubDto c: userClubs){

            int color = c.isAdmin ? R.color.crimson : R.color.black; //sets color to red if user is admin of group

            ViewClubFragment f = new ViewClubFragment();
            f.currId = c.id;

            item = new SecondaryDrawerItem()
                    .withName(c.name)
                    .withDescription(c.memberCount + " members")
                    .withIcon(R.drawable.poi_mark)
                    .withTextColorRes(color)
                    .withIdentifier(1)
                    .withOnDrawerItemClickListener(listener(f))
                    .withSelectedBackgroundAnimated(false);

            navDrawer.addItem(item);
        }

        navDrawer.addItem( new SectionDrawerItem().withName("Organizations").withDivider(true));



        item = new SecondaryDrawerItem()
                .withName("Browse Groups")
                .withIcon(R.drawable.ic_info_black_24dp)
                .withIdentifier(1)
                .withOnDrawerItemClickListener(listener(new BrowseGroupsFragment()))
                .withSelectedBackgroundAnimated(false);

        navDrawer.addItem(item);

        item = new SecondaryDrawerItem()
                .withName("Create a Group")
                .withIcon(android.R.drawable.ic_menu_add)
                .withIdentifier(1)
                .withOnDrawerItemClickListener(listener(new CreateGroupFragment()))
                .withSelectedBackgroundAnimated(false);

        navDrawer.addItem(item);

        item = new SecondaryDrawerItem()
                .withName("Create a Club")
                .withIcon(android.R.drawable.ic_menu_add)
                .withIdentifier(1)
                .withOnDrawerItemClickListener(listener(new CreateClubFragment()))
                .withSelectedBackgroundAnimated(false);

        navDrawer.addItem(item);

        navDrawer.addItem( new SectionDrawerItem().withName("Events").withDivider(true));

        BrowseEventsFragment e = new BrowseEventsFragment();
        e.clubId = -1; //set to get all club events

        item = new SecondaryDrawerItem()
                .withName("My Upcoming Events")
                .withIcon(R.drawable.calendar)
                .withIdentifier(1)
                .withOnDrawerItemClickListener(listener(e))
                .withSelectedBackgroundAnimated(false);

        navDrawer.addItem(item);


        item = new SecondaryDrawerItem()
                .withName("Create an Event")
                .withIcon(android.R.drawable.ic_menu_add)
                .withIdentifier(1)
                .withOnDrawerItemClickListener(listener(new CreateEventFragment()))
                .withSelectedBackgroundAnimated(false);

        navDrawer.addItem(item);


    }

    @Override
    public void onResume(){
        super.onResume();




        getAuthToken();

        refreshUserClubs();

    }

    @Override
    public void onStart(){
        super.onStart();

        BaseFragment f = new BaseFragment();
        goToFragment(f);
    }

    public BaseFragment getCurrentFragment(){
        return currFragment;
    }

    public void getAuthToken(){

        String token = getIntent().getStringExtra("bearerToken");
        Log.e("debug","token: " + token);



        final Gson gson = new GsonBuilder().serializeNulls().create();

        String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/auth?token=" + token;
        //String url = "http://cclubs.us-east-2.elasticbeanstalk.com/api/auth/test/1";

        Log.e("url",url);

        Request request = new Request.Builder()
                .url(url)
                .build();


        MainActivity.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                MainActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(MainActivity.this,
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
                        MainActivity.this.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(MainActivity.this,
                                                                    "Authentication failed.",
                                                                    Toast.LENGTH_LONG).show();

                                                        }
                                                    }

                        );
                    }
                    else{
                        MainActivity.this.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(MainActivity.this,
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

                    ArrayList<ApiAuthDto> temp = new ArrayList<ApiAuthDto>();

                    String body = response.body().string();


                    try {
                        temp = new ArrayList<ApiAuthDto>(Arrays.asList(gson.fromJson(body, ApiAuthDto[].class)));
                    } catch (JsonSyntaxException e) {
                        temp.add(gson.fromJson(body, ApiAuthDto.class));
                    }

                    if (temp.get(0) == null) { //read failed
                        isNull = true;
                    }

                    final ApiAuthDto resp = temp.get(0);

                    // Run view-related code back on the main thread
                    MainActivity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Log.e("bearer token:",resp.token);
                                                        bearerToken = resp.token;
                                                        currUser = resp.user;
                                                        refreshUserClubs();
                                                    }
                                                }
                    );
                }
            }

        });
    }




    @Override
    public void onBackPressed() {


        if(navDrawer.isDrawerOpen()) {

                navDrawer.closeDrawer();

        }
        else{
            goToLastFragment();
        }

    }


    private void initDrawers(Toolbar toolbar){ //sets up the drawers

            navDrawer = null;

            AccountHeader headerResult = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withHeaderBackground(R.drawable.side_nav_bar)
                    .addProfiles(
                            new ProfileDrawerItem()
                                    .withName(getIntent().getStringExtra("name"))
                                    .withEmail(getIntent().getStringExtra("email"))
                    )
                    .build();

            navDrawer = new DrawerBuilder()
                    .withActivity(this)
                    .withToolbar(toolbar)
                    .withActionBarDrawerToggle(true)
                    .withActionBarDrawerToggleAnimated(true)
                    .withHeader(R.layout.nav_header_main)
                    .withDisplayBelowStatusBar(true)
                    .withAccountHeader(headerResult)
                    .withCloseOnClick(false)
                    .withDrawerGravity(GravityCompat.START)
                    .build();



    }



    private static OkHttpClient getUnsafeOkHttpClient() { //sets up client that trusts all certificates
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}


