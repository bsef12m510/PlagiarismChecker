package com.prepostseo.plagiarismchecker.register.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chaos.view.PinView;
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
 * {@link com.prepostseo.plagiarismchecker.register.fragment.VerifyFragment.OnVerifyResponseListener} interface
 * to handle interaction events.
 */
public class VerifyFragment extends Fragment {

    private OnVerifyResponseListener mListener;
    private Button verifyButton;
    private PinView verification_code;
    private String user_id;
    private View contentView;
    private ProgressDialog pd;
    private boolean isButtonEnabled =false;

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
        pd = new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);
    }

    public void initialize(){
        verification_code = (PinView)contentView.findViewById(R.id.code);
        verification_code.setAnimationEnable(false);
        verification_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()== 4) {
                    isButtonEnabled=true;
                    verifyButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }else {
                    isButtonEnabled = false;
                    verifyButton.setBackgroundColor(getResources().getColor(R.color.buttonGray));
                }
            }
        });
        verifyButton = (Button)contentView.findViewById(R.id.verifyButton);
    }

    public void setClickListeners(){

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isButtonEnabled)
                    callVerifyService();
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
        RequestBody codeParam = RequestBody.create(MediaType.parse("text/plain"), verification_code.getText().toString());
        RequestBody userIdParam = RequestBody.create(MediaType.parse("text/plain"),user_id);

        Call<RegisterResponse> call = registerService.verify(codeParam, userIdParam);
        pd.show();
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                pd.hide();
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
    public interface OnVerifyResponseListener {
        // TODO: Update argument type and name
        public void onVerifyResponse(boolean isVerified);
    }

}
