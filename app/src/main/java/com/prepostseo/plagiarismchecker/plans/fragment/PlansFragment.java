package com.prepostseo.plagiarismchecker.plans.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.prepostseo.plagiarismchecker.Login.response.LoginResponse;
import com.prepostseo.plagiarismchecker.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlansFragment extends Fragment implements BillingProcessor.IBillingHandler{
    private Button planButton, monthlyStandard,monthlyCompany,yearlyBasic,yearlyStandard,yearlyCompany;
    private View contentView;
    BillingProcessor bp;
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
        setClickListeners();
    }

    public void initialize(){

        bp = BillingProcessor.newBillingProcessor(getActivity(), "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyWP97K6YWTYsa294pi/O/p/UirdcPsrybS/dUX1mrRQ202kY+l/pLiNH89uLyUBb8XK1X26zf1pC9MLaJNJMC/psG8906QvCQRbhiT/6em8NAyeAtQhn5ZAbueay45eB/vpdURsvd4Io1apxkZ6CiInzX7n/vMN184MCAfkWIphRwURLV9mikiD4qt2fiVogSSt2kDPEKSMVXr2z0jEBfmTCMUYIO5B3rkUoLTJfY0b0wT63N53QSmXgYwtD4Jzr8fKrztCBpI+jxJygeo/+jAN/4/w8YGewWu6cGPgYF3Ww6OhTt9cYSmyPNlkmrV54E3eezDZMFRiikExFyjzJjQIDAQAB",
                this);
        bp.initialize();
        bp.loadOwnedPurchasesFromGoogle();
        planButton = (Button)contentView.findViewById(R.id.basicPlan);
        monthlyStandard = (Button)contentView.findViewById(R.id.monthlyStandard);
        monthlyCompany = (Button)contentView.findViewById(R.id.monthlyCompany);
        yearlyBasic = (Button)contentView.findViewById(R.id.yearlyBasic);
        yearlyStandard = (Button)contentView.findViewById(R.id.yearlyStandard);
        yearlyCompany = (Button)contentView.findViewById(R.id.yearlyCompany);
    }



    public void setClickListeners(){
        planButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                boolean isAvailable = BillingProcessor.isIabServiceAvailable(getActivity());
                if(isAvailable) {

                    boolean isSubsUpdateSupported = bp.isSubscriptionUpdateSupported();
                    if(isSubsUpdateSupported) {
                        if(getSubscriptionId().equalsIgnoreCase(""))
                            bp.subscribe(getActivity(), "com.prepostseo.plagiarismchecker.monthly.basic");
                        else
                            bp.updateSubscription(getActivity(),getSubscriptionId(), "com.prepostseo.plagiarismchecker.monthly.basic");
                    }else
                        Toast.makeText(getActivity(),"Something went wrong. Please check you have added google account on play store.",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getActivity(),"Play store not found",Toast.LENGTH_SHORT).show();
            }
        });

        monthlyStandard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                boolean isAvailable = BillingProcessor.isIabServiceAvailable(getActivity());
                if(isAvailable) {

                    boolean isSubsUpdateSupported = bp.isSubscriptionUpdateSupported();
                    if(isSubsUpdateSupported) {
                        if(getSubscriptionId().equalsIgnoreCase(""))
                            bp.subscribe(getActivity(), "com.prepostseo.plagiarismchecker.monthly.standard");
                        else
                            bp.updateSubscription(getActivity(),getSubscriptionId(), "com.prepostseo.plagiarismchecker.monthly.standard");
                    }else
                        Toast.makeText(getActivity(),"Something went wrong. Please check you have added google account on play store.",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getActivity(),"Play store not found",Toast.LENGTH_SHORT).show();
            }
        });

        monthlyCompany.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                boolean isAvailable = BillingProcessor.isIabServiceAvailable(getActivity());
                if(isAvailable) {

                    boolean isSubsUpdateSupported = bp.isSubscriptionUpdateSupported();
                    if(isSubsUpdateSupported) {
                        if(getSubscriptionId().equalsIgnoreCase(""))
                            bp.subscribe(getActivity(), "com.prepostseo.plagiarismchecker.monthly.company");
                        else
                            bp.updateSubscription(getActivity(), getSubscriptionId(),"com.prepostseo.plagiarismchecker.monthly.company");
                    }else
                        Toast.makeText(getActivity(),"Something went wrong. Please check you have added google account on play store.",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getActivity(),"Play store not found",Toast.LENGTH_SHORT).show();
            }
        });

        yearlyBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isAvailable = BillingProcessor.isIabServiceAvailable(getActivity());
                if (isAvailable) {

                    boolean isSubsUpdateSupported = bp.isSubscriptionUpdateSupported();
                    if (isSubsUpdateSupported) {
                        if(getSubscriptionId().equalsIgnoreCase(""))
                            bp.subscribe(getActivity(), "com.prepostseo.plagiarismchecker.yearly.basic");
                        else
                            bp.updateSubscription(getActivity(), getSubscriptionId(),"com.prepostseo.plagiarismchecker.yearly.basic");
                    } else
                        Toast.makeText(getActivity(), "Something went wrong. Please check you have added google account on play store.", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), "Play store not found", Toast.LENGTH_SHORT).show();
            }
        });

        yearlyStandard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                boolean isAvailable = BillingProcessor.isIabServiceAvailable(getActivity());
                if(isAvailable) {

                    boolean isSubsUpdateSupported = bp.isSubscriptionUpdateSupported();
                    if(isSubsUpdateSupported) {
                        if(getSubscriptionId().equalsIgnoreCase(""))
                            bp.subscribe(getActivity(), "com.prepostseo.plagiarismchecker.yearly.standard");
                        else
                            bp.updateSubscription(getActivity(),getSubscriptionId() ,"com.prepostseo.plagiarismchecker.yearly.standard");
                    }else
                        Toast.makeText(getActivity(),"Something went wrong. Please check you have added google account on play store.",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getActivity(),"Play store not found",Toast.LENGTH_SHORT).show();
            }
        });

        yearlyCompany.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                boolean isAvailable = BillingProcessor.isIabServiceAvailable(getActivity());
                if(isAvailable) {

                    boolean isSubsUpdateSupported = bp.isSubscriptionUpdateSupported();
                    if(isSubsUpdateSupported) {
                        if(getSubscriptionId().equalsIgnoreCase(""))
                            bp.subscribe(getActivity(), "com.prepostseo.plagiarismchecker.yearly.company");
                        else
                            bp.updateSubscription(getActivity(), getSubscriptionId(),"com.prepostseo.plagiarismchecker.yearly.company");
                    }else
                        Toast.makeText(getActivity(),"Something went wrong. Please check you have added google account on play store.",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getActivity(),"Play store not found",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Toast.makeText(getActivity(), details.toString(), Toast.LENGTH_LONG).show();
        bp.loadOwnedPurchasesFromGoogle();
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

    public String getSubscriptionId(){

            SharedPreferences shared = getActivity().getSharedPreferences( "com.prepostseo.plagiarismchecker", MODE_PRIVATE);
            return shared.getString("subscription_id", "");


    }
}
