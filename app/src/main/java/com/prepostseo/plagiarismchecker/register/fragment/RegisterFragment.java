package com.prepostseo.plagiarismchecker.register.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
public class RegisterFragment extends Fragment {

    private OnRegisterResponseListener mListener;
    private View contentView;
    private Button registerButton;
    private EditText name,email,pass;
    private TextView accountExist;
    private ProgressDialog pd;

    public RegisterFragment() {
        // Required empty public constructor
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

    }

    public void initialize(){
        name = (EditText)contentView.findViewById(R.id.name);
        email = (EditText)contentView.findViewById(R.id.email);
        pass = (EditText)contentView.findViewById(R.id.password);
        registerButton = (Button)contentView.findViewById(R.id.registerButton);
        accountExist = (TextView) contentView.findViewById(R.id.alreadyAccountTextView);
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

    }
    void underLineTetView(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onSuccessfulRegister(RegisterResponse registerResponse) {
        if (mListener != null) {
            mListener.onRegisterResponse(registerResponse);
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

    public void callRegisterService(){
        RegisterService registerService = ApiClient.getClient().create(RegisterService.class);
        RequestBody nameParam = RequestBody.create(MediaType.parse("text/plain"), name.getText().toString());
        RequestBody emailParam = RequestBody.create(MediaType.parse("text/plain"), email.getText().toString());
        RequestBody passParam = RequestBody.create(MediaType.parse("text/plain"), pass.getText().toString());

        Call<RegisterResponse> call = registerService.register(nameParam,emailParam,passParam);
        pd.show();
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                pd.hide();
                if(response != null){
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
        public void onRegisterResponse(RegisterResponse registerResponse);
        public void onLoginClick();
    }

}
