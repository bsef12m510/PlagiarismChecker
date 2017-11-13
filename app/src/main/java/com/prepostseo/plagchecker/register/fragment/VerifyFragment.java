package com.prepostseo.plagchecker.register.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.dpizarro.pinview.library.PinView;
import com.dpizarro.pinview.library.PinViewSettings;
import com.prepostseo.plagchecker.R;
import com.prepostseo.plagchecker.activity.PublicActivity;
import com.prepostseo.plagchecker.api.ApiClient;
import com.prepostseo.plagchecker.register.response.RegisterResponse;
import com.prepostseo.plagchecker.register.response.ResendActivationResponse;
import com.prepostseo.plagchecker.register.restInterface.RegisterService;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.prepostseo.plagchecker.register.fragment.VerifyFragment.OnVerifyResponseListener} interface
 * to handle interaction events.
 */
public class VerifyFragment extends Fragment {

    private OnVerifyResponseListener mListener;
    private Button verifyButton, resendButton;
    private PinView verification_code;
    private String user_id;
    private View contentView;
    private ProgressDialog pdVerifyEmail;
    private ProgressDialog pdResendEmail;
    private boolean isButtonEnabled = false;
    private String [] myTitles={"#","#","#","#"};

    public VerifyFragment() {
        // Required empty public constructor
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_verify, container, false);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        setClickListeners();
        pdVerifyEmail = new ProgressDialog(getActivity());
        pdVerifyEmail.setMessage("Verifying your email");
        pdVerifyEmail.setCanceledOnTouchOutside(false);
        pdResendEmail = new ProgressDialog(getActivity());
        pdResendEmail.setMessage("Resending email");
        pdResendEmail.setCanceledOnTouchOutside(false);
    }

    public void initialize(){
        verification_code = (PinView)contentView.findViewById(R.id.pinView);
        PinViewSettings pinViewSettings = new PinViewSettings.Builder()
                .withPinTitles(myTitles)
                .withMaskPassword(false)
                .withDeleteOnClick(true)
                .withKeyboardMandatory(false)
                .withNumberPinBoxes(4)
                .withNativePinBox(false)
                .withColorTextTitles(getResources().getColor(R.color.buttonGray))
                .withColorTextPinBox(getResources().getColor(R.color.colorAccent))
                .build();
        verification_code.setSettings(pinViewSettings);
        verifyButton = (Button)contentView.findViewById(R.id.verifyButton);
        resendButton = (Button)contentView.findViewById(R.id.resendEmail);
    }

    public void setClickListeners(){

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verification_code.getPinResults().replaceAll(" ","").length() == 4 &&   ((PublicActivity)getActivity()).checkConnection())
                    callVerifyService();
            }
        });

        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              callResendEmailService();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onVerifyServiceResponse(boolean isVerified) {
        if (mListener != null) {
            mListener.onVerifyResponse(isVerified);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnVerifyResponseListener) activity;
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

    public void callVerifyService(){
        RegisterService registerService = ApiClient.getClient().create(RegisterService.class);
        RequestBody codeParam = RequestBody.create(MediaType.parse("text/plain"), verification_code.getPinResults().toString());
        RequestBody userIdParam = RequestBody.create(MediaType.parse("text/plain"),user_id);

        Call<RegisterResponse> call = registerService.verify(codeParam, userIdParam);
        pdVerifyEmail.show();
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                pdVerifyEmail.hide();
                if(response != null){
                    if(response.body().getResponse() == 1)
                        onVerifyServiceResponse(true);
                    else
                        Toast.makeText(getActivity(),"Wrong code",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pdVerifyEmail.hide();
            }
        });
    }
    public void callResendEmailService(){
        RegisterService registerService = ApiClient.getClient().create(RegisterService.class);
        RequestBody resendParam = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody userIdParam = RequestBody.create(MediaType.parse("text/plain"),user_id);

        Call<ResendActivationResponse> call = registerService.resendActivation(userIdParam,resendParam);
        pdResendEmail.show();
        call.enqueue(new Callback<ResendActivationResponse>() {
            @Override
            public void onResponse(Call<ResendActivationResponse> call, Response<ResendActivationResponse> response) {
                pdResendEmail.hide();
                if(response != null){
                    if(response.body().getResponse() == 1)
                        Toast.makeText(getActivity(),"Please check your email account now, also check your junk/trash folder.",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getActivity(),response.body().getError(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResendActivationResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pdResendEmail.hide();
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
    public interface OnVerifyResponseListener {
        // TODO: Update argument type and name
        public void onVerifyResponse(boolean isVerified);
    }

}
