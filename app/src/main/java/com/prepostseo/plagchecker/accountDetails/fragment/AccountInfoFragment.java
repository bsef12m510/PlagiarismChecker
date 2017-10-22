package com.prepostseo.plagchecker.accountDetails.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.prepostseo.plagchecker.Login.response.LoginResponse;
import com.prepostseo.plagchecker.R;
import com.prepostseo.plagchecker.accountDetails.restInterface.AccountInfoService;
import com.prepostseo.plagchecker.activity.MainDrawerActivity;
import com.prepostseo.plagchecker.api.ApiClient;
import com.prepostseo.plagchecker.plans.fragment.PlansFragment;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountInfoFragment extends Fragment {
    private String key = "";
    private ProgressDialog pd;
    private View contentView;
    private Button addMoreQueries;
    private TextView usernameTextView, apikeyTopTextView, quriesLimitTopTextView, quriesUsedTopTextView, emailTextView,apikeyBottomTextView, quriesLimitBottomTextView, quriesUsedBottomTextView,membershipTextView,expiresTextView;
    public AccountInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_account, container, false);
        return contentView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle();
        initialize();
        addClickListeners();
        getKey();
        callAccountInfoService();
    }

    void setTitle()
    {
        ((Activity) getActivity()).setTitle(getResources().getString(R.string.app_name));
    }

    public void getKey(){
        SharedPreferences shared = getActivity().getSharedPreferences( "com.prepostseo.plagiarismchecker", Context.MODE_PRIVATE);
        key = shared.getString("api_key", "");
    }


    public void initialize(){
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Fetching account information");
        pd.setCanceledOnTouchOutside(false);
        usernameTextView = (TextView) contentView.findViewById(R.id.username);
        apikeyTopTextView = (TextView) contentView.findViewById(R.id.api_key);
        quriesLimitTopTextView = (TextView) contentView.findViewById(R.id.query_limit);
        quriesUsedTopTextView = (TextView) contentView.findViewById(R.id.query_used);
        emailTextView = (TextView) contentView.findViewById(R.id.email);
        apikeyBottomTextView = (TextView) contentView.findViewById(R.id.api_key_double);
        quriesLimitBottomTextView = (TextView) contentView.findViewById(R.id.query_limit_double);
        quriesUsedBottomTextView = (TextView) contentView.findViewById(R.id.query_used_double);
        membershipTextView = (TextView) contentView.findViewById(R.id.membership);
        addMoreQueries=(Button)contentView.findViewById(R.id.add_more_queries);
        expiresTextView=(TextView)contentView.findViewById(R.id.membership_expire);
    }
    void addClickListeners()
    {
        addMoreQueries.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                onClickAddMoreQueries(v);
            }
        });
    }

    public void callAccountInfoService(){
        AccountInfoService infoService = ApiClient.getClient().create(AccountInfoService.class);
        RequestBody keyParam = RequestBody.create(MediaType.parse("text/plain"), key);

        Call<LoginResponse> call = infoService.getAccountDetails(keyParam);
        pd.show();

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                pd.hide();
                if(response != null){
                  //set UI
                    assignValues(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.hide();
            }
        });
    }
    public void onClickAddMoreQueries(View view)
    {
        ((MainDrawerActivity)getActivity()).replaceFragment(new PlansFragment(),MainDrawerActivity.TAG_PLANS);
        ((MainDrawerActivity)getActivity()).navigationView.setCheckedItem(R.id.nav_plans);
    }
    private void assignValues(LoginResponse response)
    {
        if(response!=null) {
            usernameTextView.setText(response.getUser_name());
            apikeyTopTextView.setText(response.getApi_key());
            apikeyBottomTextView.setText(response.getApi_key());
            quriesLimitTopTextView.setText(getFormatedAmount(response.getQueries_limit()));
            quriesLimitBottomTextView.setText(getFormatedAmount(response.getQueries_limit()));
            quriesUsedTopTextView.setText(getFormatedAmount(response.getQueries_used()));
            int percentageComplete=(int)((Double.parseDouble(response.getQueries_used())/Double.parseDouble(response.getQueries_limit()))*100.00);
            quriesUsedBottomTextView.setText(Integer.parseInt(response.getQueries_used()) + " (" + Integer.toString (percentageComplete) +  "% used )" );
            emailTextView.setText(response.getUser_email());


            String membership= "Free";
            if(response.getPremium().equals("1")) {
                membership="Premium";
                int numberOfseconds =Integer.parseInt( response.getExpireDate());
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                Date date=new Date();
                date.setTime((long)numberOfseconds*1000);
                calendar.setTime(date);
                SimpleDateFormat formatDate = new SimpleDateFormat("dd MMM,  yyyy");
                String formattedDate=formatDate.format(calendar.getTime());
                expiresTextView.setText("(Expire on: " + formattedDate+")");
            }
            membershipTextView.setText(membership);

        }
    }
    private String getFormatedAmount(String amount){
        return NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(amount));
    }

}
