package com.prepostseo.plagiarismchecker.register.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.prepostseo.plagiarismchecker.R;
import com.prepostseo.plagiarismchecker.api.ApiClient;
import com.prepostseo.plagiarismchecker.register.response.RegisterResponse;
import com.prepostseo.plagiarismchecker.register.restInterface.RegisterService;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnRegisterResponseListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    private OnRegisterResponseListener mListener;
    private View contentView;
    private Button registerButton;
    private EditText name,email,pass;
    private TextView accountExist;
    private ProgressDialog pd;
    private SignInButton siginBtn;
    private GoogleApiClient mGoogleApiClient;
    private boolean isGoogleSignin = false;
    private GoogleSignInOptions gso;
    private boolean isSignedIn = false;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage((FragmentActivity) getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */).addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        if(isSignedIn){
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("tag", "onConnectionFailed:" + connectionResult);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_register, container, false);
        return contentView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        setClickListeners();
        pd = new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);
    }

    public void initialize(){
        name = (EditText)contentView.findViewById(R.id.name);
        email = (EditText)contentView.findViewById(R.id.email);
        pass = (EditText)contentView.findViewById(R.id.password);
        registerButton = (Button)contentView.findViewById(R.id.registerButton);
        accountExist = (TextView) contentView.findViewById(R.id.alreadyAccountTextView);
        siginBtn = (SignInButton) contentView.findViewById(R.id.sign_in_button);
        underLineTetView(accountExist);
    }

    public void setClickListeners(){
        accountExist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onLoginClick();
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callRegisterService();
            }
        });

        siginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGoogleSignin = true;
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, 007);
            }

        });
    }
    void underLineTetView(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onSuccessfulRegister(RegisterResponse registerResponse) {
        if (mListener != null) {
            mListener.onRegisterResponse(registerResponse,isGoogleSignin);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnRegisterResponseListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 007) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();
            name.setText(acct.getDisplayName());
            email.setText(acct.getEmail());
            Toast.makeText(getActivity(), "Please create a password to proceed..", Toast.LENGTH_SHORT).show();
            isSignedIn = true;
            mGoogleApiClient.connect();
        }
    }

    public void callRegisterService(){
        if(name.getText().toString().trim().equalsIgnoreCase("")){
            name.setError("Name cannot be empty.");
        }else if(email.getText().toString().trim().equalsIgnoreCase("")){
            email.setError("Email cannot be empty.");
        }else if(pass.getText().toString().trim().equalsIgnoreCase("")){
            pass.setError("Password cannot be empty");
        }else {
            RegisterService registerService = ApiClient.getClient().create(RegisterService.class);
            String googleAuth;
            Call<RegisterResponse> call;
            if (isGoogleSignin) {
                googleAuth = "1";
            } else {
                googleAuth = "0";
            }

            call = registerService.register(name.getText().toString(), email.getText().toString(), pass.getText().toString(), googleAuth);
            pd.show();
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    pd.hide();
                    if (response != null) {
                        onSuccessfulRegister(response.body());
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("failure", "failure");
                    pd.hide();
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mGoogleApiClient.stopAutoManage((FragmentActivity) getActivity());
        mGoogleApiClient.disconnect();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRegisterResponseListener {
        // TODO: Update argument type and name
        public void onRegisterResponse(RegisterResponse registerResponse,boolean isGoogleSignin);
        public void onLoginClick();
    }

}
