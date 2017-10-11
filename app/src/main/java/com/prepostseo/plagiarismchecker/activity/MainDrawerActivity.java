package com.prepostseo.plagiarismchecker.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.TextViewCompat;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.prepostseo.plagiarismchecker.R;
import com.prepostseo.plagiarismchecker.accountDetails.fragment.AccountInfoFragment;
import com.prepostseo.plagiarismchecker.checker.fragment.PlagiarismCheckerFragment;
import com.prepostseo.plagiarismchecker.plans.fragment.PlansFragment;

public class MainDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PlagiarismCheckerFragment.OnFragmentInteractionListener {

    private boolean fromPlagFragment = false;
    private static String TAG_PLAG = "plagFrag";
    private static String TAG_ACCOUNT_INFO = "infoFrag";
    public static String TAG_PLANS = "plansFragment";
    public NavigationView navigationView;

    private TextView userNameTextView,emailTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View  headerView=(View)navigationView.getHeaderView(0);
        userNameTextView=(TextView)headerView.findViewById(R.id.header_username);
        emailTextView=(TextView)headerView.findViewById(R.id.header_email);
        getSavedHeaderData();
        replaceFragment(new PlagiarismCheckerFragment(),TAG_PLAG);
    }

    private void getSavedHeaderData()
    {
        SharedPreferences shared = getSharedPreferences( "com.prepostseo.plagiarismchecker", Context.MODE_PRIVATE);
        userNameTextView.setText(shared.getString("username", ""));
        emailTextView.setText(shared.getString("email",""));
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_drawer, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            Fragment fragment = new PlagiarismCheckerFragment();
            replaceFragment(fragment,TAG_PLAG);
            // Handle the camera action
        } else if (id == R.id.nav_account) {
            Fragment fragment = new AccountInfoFragment();
            replaceFragment(fragment,TAG_ACCOUNT_INFO);
        }else if (id == R.id.nav_plans) {
            Fragment fragment = new PlansFragment();
            replaceFragment(fragment, TAG_PLANS);
        } else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    void logout()
    {
        deleteApiKey();
        Intent intent=new Intent(MainDrawerActivity.this, PublicActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void deleteApiKey() {
        SharedPreferences prefs = this.getSharedPreferences(
                "com.prepostseo.plagiarismchecker", Context.MODE_PRIVATE);

        prefs.edit().putString("api_key", "").apply();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

        fromPlagFragment = true;
    }
    public void replaceFragment(Fragment fragment,String TAG) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment,TAG)
                .commit();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment;
        if(requestCode == 234) {
            fragment = getFragmentManager().findFragmentByTag("plagFrag");
        }else{
            fragment = getFragmentManager().findFragmentByTag("plansFragment");
        }
        fragment.onActivityResult(requestCode, resultCode, data);

    }
}
