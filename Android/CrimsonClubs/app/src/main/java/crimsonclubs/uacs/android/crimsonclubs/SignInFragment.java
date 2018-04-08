package crimsonclubs.uacs.android.crimsonclubs;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.SignInButton;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment  {


    public SignInFragment() {
        // Required empty public constructor
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onResume(){
        super.onResume();



    }

}
