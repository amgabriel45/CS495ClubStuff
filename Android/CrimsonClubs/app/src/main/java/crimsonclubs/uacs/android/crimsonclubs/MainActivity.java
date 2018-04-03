package crimsonclubs.uacs.android.crimsonclubs;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Keep;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.util.Base64;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.json.JSONArray;

import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@Keep
@RuntimePermissions
public class MainActivity extends AppCompatActivity

{


    public static OkHttpClient client;
    public static boolean hasConnected = false; //set true when app first connects to a server, set back to false when host is changed

    public Drawer navDrawer = null;
    public Drawer bookmarkDrawer = null;

    public Stack<Integer> navStack = new Stack<>();

    //private static SQLiteDatabaseHandler db;

    //Lifecycle/General Android Functions

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initDrawers(toolbar); //build nav drawer and bookmark drawer


        setUpDemoDrawer();



        client = getUnsafeOkHttpClient(); //sets up client that accepts any SSL certificate

        //db = new SQLiteDatabaseHandler(this);

       // db.getReadableDatabase();

      //  db.close();
    }

    private  Drawer.OnDrawerItemClickListener listener(){ //handles what happens when poi draweritem is clicked

        Drawer.OnDrawerItemClickListener result = new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                if(drawerItem != null) {


                    Fragment temp = new BaseFragment();

                    getFragmentManager()
                            .beginTransaction()

                            .setCustomAnimations(
                                    R.animator.card_flip_right_in,
                                    R.animator.card_flip_right_out,
                                    R.animator.card_flip_left_in,
                                    R.animator.card_flip_left_out)

                            .replace(R.id.container, temp, "curr")

                            .commit();

                    navDrawer.closeDrawer();
                }

                return false;
            }
        };

        return result;
    }

    public void setUpDemoDrawer(){

        navDrawer.addItem( new SectionDrawerItem().withName("My Groups").withDivider(false));

        SecondaryDrawerItem item = new SecondaryDrawerItem()
                .withName("Men's Tennis Singles")
                .withDescription("2 new events")
                .withIcon(R.drawable.poi_mark)
                .withIdentifier(1)
                .withOnDrawerItemClickListener(listener())
                .withSelectedBackgroundAnimated(false);

        navDrawer.addItem(item);

        item = new SecondaryDrawerItem()
                .withName("Chess Club")
                .withDescription("0 new events")
                .withIcon(R.drawable.poi_mark)
                .withIdentifier(1)
                .withOnDrawerItemClickListener(listener())
                .withSelectedBackgroundAnimated(false);

        navDrawer.addItem(item);

        navDrawer.addItem( new SectionDrawerItem().withName("Calendar and Events").withDivider(true));

        item = new SecondaryDrawerItem()
                .withName("My Calendar")
                .withIcon(R.drawable.calendar)
                .withIdentifier(1)
                .withOnDrawerItemClickListener(listener())
                .withSelectedBackgroundAnimated(false);

        navDrawer.addItem(item);

        item = new SecondaryDrawerItem()
                .withName("Browse Events")
                .withIcon(R.drawable.ic_info_black_24dp)
                .withIdentifier(1)
                .withOnDrawerItemClickListener(listener())
                .withSelectedBackgroundAnimated(false);

        navDrawer.addItem(item);

        item = new SecondaryDrawerItem()
                .withName("Create an Event")
                .withIcon(android.R.drawable.ic_menu_add)
                .withIdentifier(1)
                .withOnDrawerItemClickListener(listener())
                .withSelectedBackgroundAnimated(false);

        navDrawer.addItem(item);

        navDrawer.addItem( new SectionDrawerItem().withName("Clubs").withDivider(true));



        item = new SecondaryDrawerItem()
                .withName("Browse Clubs")
                .withIcon(R.drawable.ic_info_black_24dp)
                .withIdentifier(1)
                .withOnDrawerItemClickListener(listener())
                .withSelectedBackgroundAnimated(false);

        navDrawer.addItem(item);

        item = new SecondaryDrawerItem()
                .withName("Create a Club")
                .withIcon(android.R.drawable.ic_menu_add)
                .withIdentifier(1)
                .withOnDrawerItemClickListener(listener())
                .withSelectedBackgroundAnimated(false);

        navDrawer.addItem(item);



    }

    @Override
    public void onResume(){
        super.onResume();

       // SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        /*
        isLive = prefs.getBoolean("livemode_switch",false);
        isTLS = prefs.getBoolean("network_use_ssl",false);
        host = prefs.getString("host_name","127.0.0.1");
        port = Integer.parseInt(prefs.getString("port_name","8080"));

        prefs = getSharedPreferences("com.crimsonclubs.uacs.android.crimsonclubs.prefs", Context.MODE_PRIVATE);

        String temp = prefs.getString("iqagent_uuid","NOTFOUND");

        if(temp.compareTo("NOTFOUND") != 0) {
            token = temp;
        }

        if(prefs.getBoolean("mode_changed",false)){ //if user has switched between live/internal modes
            hasConnected = false;
            clearPathText();
            updateBookmarks = true;
            SharedPreferences pref = getSharedPreferences("com.crimsonclubs.uacs.android.crimsonclubs.prefs",Context.MODE_PRIVATE);
            pref.edit().putBoolean("mode_changed", false).apply();
        }

        if(prefs.getBoolean("host_changed",false)){ //if user has changed host
            hasConnected = false;
            clearPathText();
            updateBookmarks = true;
            SharedPreferences pref = getSharedPreferences("com.crimsonclubs.uacs.android.crimsonclubs.prefs",Context.MODE_PRIVATE);
            pref.edit().putBoolean("host_changed", false).apply();
        }
        */

        //db = new SQLiteDatabaseHandler(this);

       // db.getReadableDatabase();

       // db.close();

        if(!navDrawer.isDrawerOpen()) {
            updateNavDrawer(); //populates drawer w/ top level navObjects info
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {

        if(navDrawer.isDrawerOpen()) {

                navDrawer.closeDrawer();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

           // Intent i = new Intent(this, IQAgentSettingsActivity.class);
           // startActivity(i);
          //  return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //UI and Setup functions

    private void updateNavDrawer(){ //adds navObjects and POIs to drawer according to current groupID

       // navDrawer.removeAllItems();


            InternalModeSQLiteHelper db = new InternalModeSQLiteHelper(this);

            //JSONArray arr = db.getJson(db.getItemsAtLevel(currGroupId)); //same thing as live mode but get data from DB

           // IQAgentNavObject[] temp;
/*
            try {
                temp = gson.fromJson(arr.toString(), IQAgentNavObject[].class);
            } catch (JsonSyntaxException e) {
                temp = new IQAgentNavObject[1];
                temp[0] = gson.fromJson(arr.toString(), IQAgentNavObject.class);
            }

            updateMenusFromArr(temp); //update menus

            db.close();
            */

        }


  //  private void updateMenusFromArr(final IQAgentNavObject[] navArr){ //updates nav drawer with given nav obj info

        /*

        ArrayList<IDrawerItem> pois = new ArrayList<>();
        ArrayList<IDrawerItem> groups = new ArrayList<>();

        for (int i = 0; i < navArr.length; i++) {
            if (navArr[i].type.compareTo("poi") == 0) {
                SecondaryDrawerItem item = new SecondaryDrawerItem()
                        .withName(navArr[i].name)
                        .withDescription(navArr[i].description)
                        .withIcon(R.drawable.poi_mark)
                        .withIdentifier(navArr[i].id)
                        .withSelectedBackgroundAnimated(false)
                        .withOnDrawerItemClickListener(poiListener());
                pois.add(item);
            } else {
                SecondaryDrawerItem item = new SecondaryDrawerItem()
                        .withName(navArr[i].name)
                        .withDescription(navArr[i].description)
                        .withIcon(R.drawable.caret)
                        .withIdentifier(navArr[i].id)
                        .withSelectedBackgroundAnimated(false)
                        .withOnDrawerItemClickListener(groupListener());
                groups.add(item);
            }
        }

        if(!pois.isEmpty()){
            navDrawer.addItem( new SectionDrawerItem().withName("POIs").withDivider(false));
            for(IDrawerItem i : pois){
                navDrawer.addItem(i);
            }
        }

        if(!groups.isEmpty()){
            navDrawer.addItem( new SectionDrawerItem().withName("Groups").withDivider(!pois.isEmpty()));
            for(IDrawerItem i : groups){
                navDrawer.addItem(i);
            }
        }

*/

//}

    private void initDrawers(Toolbar toolbar){ //sets up the drawers

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.side_nav_bar)
                .addProfiles(
                        new ProfileDrawerItem().withName("Taylor Meads").withEmail("tfmeads@crimson.ua.edu").withIcon(getResources().getDrawable(R.drawable.pro))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
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



    //Utility Functions




    //Everything Else



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


