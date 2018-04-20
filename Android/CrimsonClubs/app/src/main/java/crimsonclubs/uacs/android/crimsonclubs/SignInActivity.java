package crimsonclubs.uacs.android.crimsonclubs;

import android.content.Intent;
import android.support.annotation.Keep;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

@Keep
public class SignInActivity extends AppCompatActivity { //Just a splash screen that displays logo until MainActivity is loaded

    public static int RC_SIGN_IN = 1861; //result code for google sign-in

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        forceProceedToMainActivity();
        finish();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("77421544828-k57594dl8a1rgmitclu6e0rj8970ved1.apps.googleusercontent.com")
                .requestEmail()
                .requestProfile()
                .build();

        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton btn = (SignInButton) findViewById(R.id.sign_in_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }

    @Override
    protected void onStart(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account == null){
            //null
        }
        else{

            proceedToMainActivity(account);
        }

        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                proceedToMainActivity(account);

            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Toast.makeText(this,"Sign-in failed. Please try again.",Toast.LENGTH_LONG).show();
                Log.e("error", "signInResult:failed code=" + e.getStatusCode());

            }
        }
    }

    private void proceedToMainActivity(GoogleSignInAccount account){

        Bundle b = new Bundle();
        b.putString("email",account.getEmail());
        b.putString("name",account.getGivenName());
        b.putString("photoUrl",account.getPhotoUrl().toString());
        b.putString("bearerToken",account.getIdToken());


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(b);

        startActivity(intent);
        finish();
    }

    private void forceProceedToMainActivity() //delete this in final version
    {

        Bundle b = new Bundle();
        b.putString("email","jgarcia@crimson.ua.edu");
        b.putString("name","Jerry");
        b.putString("photoUrl","www.goatse.cx");
        b.putString("bearerToken","abcdefg");


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(b);

        startActivity(intent);
        finish();

    }
}
