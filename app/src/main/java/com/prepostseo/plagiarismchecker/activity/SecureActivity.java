package com.prepostseo.plagiarismchecker.activity;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.prepostseo.plagiarismchecker.Login.fragment.LoginFragment;
import com.prepostseo.plagiarismchecker.R;
import com.prepostseo.plagiarismchecker.checker.fragment.PlagiarismCheckerFragment;
import com.prepostseo.plagiarismchecker.register.fragment.RegisterFragment;
import com.prepostseo.plagiarismchecker.register.fragment.VerifyFragment;

import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;




public class SecureActivity extends AppCompatActivity implements PlagiarismCheckerFragment.OnFragmentInteractionListener {
    private FrameLayout fragmentContainer;
    private FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure);
        initialize();
        displayFragment(1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_secure, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void initialize() {
        fragmentContainer = (FrameLayout) findViewById(R.id.fragmentContainer);
    }


    public void displayFragment(int key) {
        // get fragment manager
        fm = getFragmentManager();

        // replace
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left,
                R.anim.slide_out_right,R.anim.slide_in_left,
                R.anim.slide_out_right);
        if (key == 1)
            ft.replace(R.id.fragmentContainer, new PlagiarismCheckerFragment(),"plagFrag");
        else if (key == 2)
            ft.replace(R.id.fragmentContainer, new RegisterFragment());
        else if (key == 3) {

        }

        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getFragmentManager().findFragmentByTag("plagFrag");
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
