package com.prepostseo.plagchecker.plans.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.prepostseo.plagchecker.R;
import com.prepostseo.plagchecker.api.ApiClient;
import com.prepostseo.plagchecker.plans.response.UpgradeUserResponse;

import com.prepostseo.plagchecker.plans.restInterface.UpgradeService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlansFragment extends Fragment implements BillingProcessor.IBillingHandler{


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static Button planButton, monthlyStandard,monthlyCompany,yearlyBasic,yearlyStandard,yearlyCompany;
    private View contentView;
    private static BillingProcessor bp;
    private ProgressDialog pd;
    public PlansFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return contentView = inflater.inflate(R.layout.fragment_plans, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }


    public void initialize(){

        pd = new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter( ((AppCompatActivity)getActivity()).getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) contentView.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) contentView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        bp = BillingProcessor.newBillingProcessor(getActivity(), "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAypUeSOCiDqtaLU0qMS0CdzZCZdZFkoHK0kaIDukvhik2I/TLNcQoKjFRQHh9xJ6gewKf1RBIYVM9uq3rY2Q+n+mzGEUbCA4KohlCR3oM+nj2Cq8YoXJo93owEkiatp+F2IEOzpQwqcMR6NQPzN+j4W28ACBHaAfpKTyNMOM10TY8t91ysxG2teMY0i6Pe2uebL82XIhag5fKXpibt4vNfI4PmwiiP7vqH6W3xRN3xkfw1KjwHWzHCoCQ2E7dvPCINnMYl1fKtRmN2osR5jb+Ct92n+CTgHvEBGEtQRBb7kmQ355JDNtObYRMCfm2mH+4wEHvtE7hD4O0ALzck+f5FQIDAQAB",
                this);
        bp.initialize();
        bp.loadOwnedPurchasesFromGoogle();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends android.support.v4.app.Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

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
            View rootView=null;
            if( getArguments().getInt(ARG_SECTION_NUMBER) == 1)
            {
                rootView = inflater.inflate(R.layout.fragment_monthly, container, false);
                initializeMonthlyButtons(rootView);
                setMonthlyClickListener(getActivity());
            }else if( getArguments().getInt(ARG_SECTION_NUMBER) == 2)
            {
                rootView = inflater.inflate(R.layout.fragment_yearly, container, false);
                initializeYearlyButtons(rootView);
                setYearlyClickListeners(getActivity());
            }

            return rootView;
        }
    }
    static void initializeMonthlyButtons(View contentView)
    {
        planButton = (Button)contentView.findViewById(R.id.monthlyBasic);
        monthlyStandard = (Button)contentView.findViewById(R.id.monthlyStandard);
        monthlyCompany = (Button)contentView.findViewById(R.id.monthlyCompany);
    }
    static void initializeYearlyButtons(View contentView)
    {
        yearlyBasic = (Button)contentView.findViewById(R.id.yearlyBasic);
        yearlyStandard = (Button)contentView.findViewById(R.id.yearlyStandard);
        yearlyCompany = (Button)contentView.findViewById(R.id.yearlyCompany);
    }
    static void setMonthlyClickListener(final Activity activity)
    {
        planButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                boolean isAvailable = BillingProcessor.isIabServiceAvailable(activity);
                if(isAvailable) {

                    boolean isSubsUpdateSupported = bp.isSubscriptionUpdateSupported();
                    if(isSubsUpdateSupported) {
                        if(getSubscriptionId(activity).equalsIgnoreCase(""))
                            bp.subscribe(activity, "com.prepostseo.plagiarismchecker.monthly.basic");
                        else
                            bp.updateSubscription(activity,getSubscriptionId(activity), "com.prepostseo.plagiarismchecker.monthly.basic");
                    }else
                        Toast.makeText(activity,"Something went wrong. Please check you have added google account on play store.",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(activity,"Play store not found",Toast.LENGTH_SHORT).show();
            }
        });

        monthlyStandard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                boolean isAvailable = BillingProcessor.isIabServiceAvailable(activity);
                if(isAvailable) {

                    boolean isSubsUpdateSupported = bp.isSubscriptionUpdateSupported();
                    if(isSubsUpdateSupported) {
                        if(getSubscriptionId(activity).equalsIgnoreCase(""))
                            bp.subscribe(activity, "com.prepostseo.plagiarismchecker.monthly.standard");
                        else
                            bp.updateSubscription(activity,getSubscriptionId(activity), "com.prepostseo.plagiarismchecker.monthly.standard");
                    }else
                        Toast.makeText(activity,"Something went wrong. Please check you have added google account on play store.",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(activity,"Play store not found",Toast.LENGTH_SHORT).show();
            }
        });

        monthlyCompany.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                boolean isAvailable = BillingProcessor.isIabServiceAvailable(activity);
                if(isAvailable) {

                    boolean isSubsUpdateSupported = bp.isSubscriptionUpdateSupported();
                    if(isSubsUpdateSupported) {
                        if(getSubscriptionId(activity).equalsIgnoreCase(""))
                            bp.subscribe(activity, "com.prepostseo.plagiarismchecker.monthly.company");
                        else
                            bp.updateSubscription(activity, getSubscriptionId(activity),"com.prepostseo.plagiarismchecker.monthly.company");
                    }else
                        Toast.makeText(activity,"Something went wrong. Please check you have added google account on play store.",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(activity,"Play store not found",Toast.LENGTH_SHORT).show();
            }
        });

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
        public android.support.v4.app.Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Monthly";
                case 1:
                    return "Yearly";
            }
            return null;
        }
    }

    static void setYearlyClickListeners(final Activity activity){

        yearlyBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isAvailable = BillingProcessor.isIabServiceAvailable(activity);
                if (isAvailable) {

                    boolean isSubsUpdateSupported = bp.isSubscriptionUpdateSupported();
                    if (isSubsUpdateSupported) {
                        if(getSubscriptionId(activity).equalsIgnoreCase(""))
                            bp.subscribe(activity, "com.prepostseo.plagiarismchecker.yearly.basic");
                        else
                            bp.updateSubscription(activity, getSubscriptionId(activity),"com.prepostseo.plagiarismchecker.yearly.basic");
                    } else
                        Toast.makeText(activity, "Something went wrong. Please check you have added google account on play store.", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(activity, "Play store not found", Toast.LENGTH_SHORT).show();
            }
        });

        yearlyStandard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                boolean isAvailable = BillingProcessor.isIabServiceAvailable(activity);
                if(isAvailable) {

                    boolean isSubsUpdateSupported = bp.isSubscriptionUpdateSupported();
                    if(isSubsUpdateSupported) {
                        if(getSubscriptionId(activity).equalsIgnoreCase(""))
                            bp.subscribe(activity, "com.prepostseo.plagiarismchecker.yearly.standard");
                        else
                            bp.updateSubscription(activity,getSubscriptionId(activity) ,"com.prepostseo.plagiarismchecker.yearly.standard");
                    }else
                        Toast.makeText(activity,"Something went wrong. Please check you have added google account on play store.",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(activity,"Play store not found",Toast.LENGTH_SHORT).show();
            }
        });

        yearlyCompany.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                boolean isAvailable = BillingProcessor.isIabServiceAvailable(activity);
                if(isAvailable) {

                    boolean isSubsUpdateSupported = bp.isSubscriptionUpdateSupported();
                    if(isSubsUpdateSupported) {
                        if(getSubscriptionId(activity).equalsIgnoreCase(""))
                            bp.subscribe(activity, "com.prepostseo.plagiarismchecker.yearly.company");
                        else
                            bp.updateSubscription(activity, getSubscriptionId(activity),"com.prepostseo.plagiarismchecker.yearly.company");
                    }else
                        Toast.makeText(activity,"Something went wrong. Please check you have added google account on play store.",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(activity,"Play store not found",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }

    public String getKey(){
        SharedPreferences shared = getActivity().getSharedPreferences( "com.prepostseo.plagiarismchecker", MODE_PRIVATE);
        return shared.getString("api_key", "");
    }


    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Toast.makeText(getActivity(), details.toString(), Toast.LENGTH_LONG).show();
        bp.loadOwnedPurchasesFromGoogle();
        callUpgradeService(getKey(),details.purchaseInfo.purchaseData.packageName,productId,details.purchaseInfo.purchaseData.purchaseToken);
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    public void saveSubscriptionId(String id){

            SharedPreferences prefs = getActivity().getSharedPreferences(
                    "com.prepostseo.plagiarismchecker", Context.MODE_PRIVATE);

            prefs.edit().putString("subscription_id", id).apply();

    }

    static public String getSubscriptionId(Activity activity){

            SharedPreferences shared = activity.getSharedPreferences( "com.prepostseo.plagiarismchecker", MODE_PRIVATE);
            return shared.getString("subscription_id", "");


    }
    //this should be call when request is successful
    void callUpgradeService(String apiKey,String packageName,String subscriptionId, String token)
    {
        UpgradeService upgradeUserService = ApiClient.getClient().create(UpgradeService.class);
        Call<UpgradeUserResponse> call;


        call = upgradeUserService.upgradeUser(apiKey, packageName, subscriptionId, token);
        pd.show();
        call.enqueue(new Callback<UpgradeUserResponse>() {
            @Override
            public void onResponse(Call<UpgradeUserResponse> call, Response<UpgradeUserResponse> response) {
                pd.hide();
                if (response != null) {
                    onUpgradeResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<UpgradeUserResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.hide();
            }
        });
    }
    void onUpgradeResponse(UpgradeUserResponse response)
    {
        if(response!=null)
        {
            if(response.getResponse() == 1)
            {
                //successfull
                upgradeMembership();
                Toast.makeText(getActivity(),"Your account upgraded",Toast.LENGTH_SHORT).show();
            }
            else
            {
                //unsuccessfull
                Toast.makeText(getActivity(),"Could not upgrade your account. Please try again later.",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getActivity(),"Could not upgrade your account. Please try again later.",Toast.LENGTH_SHORT).show();
            //unsuccessfull
        }

    }
    void upgradeMembership()
    {

        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.prepostseo.plagiarismchecker", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("membership", true ).apply();
    }

}
