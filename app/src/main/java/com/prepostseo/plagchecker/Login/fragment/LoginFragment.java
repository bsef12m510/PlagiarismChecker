package com.prepostseo.plagchecker.Login.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.prepostseo.plagchecker.Login.response.LoginResponse;
import com.prepostseo.plagchecker.Login.restInterface.LoginService;
import com.prepostseo.plagchecker.R;
import com.prepostseo.plagchecker.activity.PublicActivity;
import com.prepostseo.plagchecker.api.ApiClient;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnLoginResponseListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment {

    private EditText email,password;
    private LoginResponse loginResponseData;
    private OnLoginResponseListener mListener;
    private ProgressDialog pd;
    private Button  loginButton;
    private TextView registerButton;
    private View contentView;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_login, container, false);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize(contentView);
        setClickListeners();
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Logging you in");
        pd.setCanceledOnTouchOutside(false);
 }

    public void initialize(View contentView){

        registerButton = (TextView) contentView.findViewById(R.id.registerTextView);
        underLineTetView(registerButton);
        loginButton = (Button)contentView.findViewById(R.id.loginButton);
        email = (EditText)contentView.findViewById(R.id.email);
        password = (EditText)contentView.findViewById(R.id.password);
//        email.setText("913kc@gmail.com");
//        password.setText("123");
    }
    void underLineTetView(TextView textView)
    {
        textView.setPaintFlags(textView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
    }

    public void setClickListeners(){
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onRegisterClick();
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if(((PublicActivity)getActivity()).checkConnection()) {
                    callLoginService();
                }
            }
        });
    }

    public void onSuccessfulLogin(LoginResponse loginResponseData) {
        if (mListener != null) {
            mListener.onLoginResponse(loginResponseData);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLoginResponseListener) activity;
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

    public void callLoginService() {
        if(email.getText().toString().trim().equalsIgnoreCase("")){
            email.setError("Username cannot be empty.");
        }else if(password.getText().toString().trim().equalsIgnoreCase("")){
            password.setError("Password cannot be empty");
        }else {
            LoginService loginService =
                    ApiClient.getClient().create(LoginService.class);
            RequestBody emailParam = RequestBody.create(MediaType.parse("text/plain"), email.getText().toString());
            RequestBody passParam = RequestBody.create(MediaType.parse("text/plain"), password.getText().toString());
            Call<LoginResponse> call = loginService.login(emailParam, passParam);
            pd.show();
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    pd.hide();
                    if (response != null) {
                        loginResponseData = (LoginResponse) response.body();
                        onSuccessfulLogin(loginResponseData);
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
    public interface OnLoginResponseListener {
        // TODO: Update argument type and name
        public void onLoginResponse(LoginResponse loginResponseData);
        public void onRegisterClick();
    }

}
