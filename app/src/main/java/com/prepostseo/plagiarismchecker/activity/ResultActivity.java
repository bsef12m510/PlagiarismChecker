package com.prepostseo.plagiarismchecker.activity;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.prepostseo.plagiarismchecker.R;
import com.prepostseo.plagiarismchecker.checker.response.PlagiarismDetail;
import com.prepostseo.plagiarismchecker.checker.response.PlagiarismResponse;

import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static TextView uniquePerTextView,plagPerTextView;
    private static PlagiarismResponse response;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //parsing response
        parseResponse();

    }
    void parseResponse()
    {
        // To retrieve object in second Activity
        response = (PlagiarismResponse)getIntent().getSerializableExtra("response");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private final static String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            int section_number = getArguments().getInt(ARG_SECTION_NUMBER);
            if(section_number == 1) {
                rootView = inflater.inflate(R.layout.fragment_result, container, false);
                inflateSentenceWiseCompletionLayout(rootView,inflater,rootView);
            }
            return rootView;
        }
    }
    static void inflateSentenceWiseCompletionLayout(View view , LayoutInflater inflater,View rootView)
    {
        inflateDialogBoxViewLayout(view);
        uniquePerTextView.setText(response.getUniquePercent().intValue() + "%");
        plagPerTextView.setText(response.getPlagPercent().intValue() + "%");
        addDetailedItems(response.getDetails(),inflater,rootView);
    }
    static void inflateDialogBoxViewLayout(View rootView)
    {
        uniquePerTextView=(TextView)rootView.findViewById(R.id.unique_perc_text_view);
        plagPerTextView=(TextView)rootView.findViewById(R.id.plag_perc_text_view);
    }
    static void addDetailedItems(List<PlagiarismDetail> detailItems, LayoutInflater inflater,View rootView)
    {
        if(!detailItems.isEmpty())
        {
            for (PlagiarismDetail item:detailItems)
            {
                if(item!=null)
                {
                    if(item.getUnique())
                    {
                        LinearLayout itemLayout = inflateUniqueItemLayout(inflater);
                        TextView textView=(TextView) itemLayout.findViewById(R.id.detail_text);
                        textView.setText(item.getQuery());
                        ((LinearLayout)rootView.findViewById(R.id.detail_layout)).addView(itemLayout);
                    }else
                    {
                        LinearLayout itemLayout=inflatePlagiarizedItemLayout(inflater);
                        TextView textView=(TextView) itemLayout.findViewById(R.id.detail_text);
                        textView.setText(item.getQuery());
                        ((LinearLayout)rootView.findViewById(R.id.detail_layout)).addView(itemLayout);
                    }
                }
            }
        }
    }
    static LinearLayout inflatePlagiarizedItemLayout(LayoutInflater inflater)
    {
        return (LinearLayout)inflater.inflate(R.layout.plagiarise_content_view, null);
    }
    static LinearLayout inflateUniqueItemLayout(LayoutInflater inflater)
    {
        return (LinearLayout)inflater.inflate(R.layout.unique_content_view, null);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Sentence Wise Results";
                case 1:
                    return "Matched Sources";
            }
            return null;
        }
    }
}
