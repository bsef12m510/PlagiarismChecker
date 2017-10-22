package com.prepostseo.plagchecker.contactUs.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.prepostseo.plagchecker.R;
import com.prepostseo.plagchecker.api.ApiClient;
import com.prepostseo.plagchecker.contactUs.response.MessageResponse;
import com.prepostseo.plagchecker.contactUs.restInterface.SubmitMsgService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUs extends Fragment {

    private String key = "";
    private EditText messageEditText;
    private Button submitButton;
    private View contentView;
    private ProgressDialog pd;
    private String saved_username;
    private String saved_email;

    // Error Messages
    private static final String REQUIRED_MSG = "required";
    public ContactUs() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ContactUs newInstance(String param1, String param2) {
        ContactUs fragment = new ContactUs();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    void initialize()
    {
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Submitting your message");
        pd.setCanceledOnTouchOutside(false);
        messageEditText =(EditText)contentView.findViewById(R.id.message);
        submitButton=(Button) contentView.findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });
        getSavedHeaderData();

    }
    private void getSavedHeaderData()
    {
        SharedPreferences shared = getActivity().getSharedPreferences( "com.prepostseo.plagiarismchecker", Context.MODE_PRIVATE);
        saved_username = shared.getString("username", "");
        saved_email = shared.getString("email","");
    }
    void validateForm()
    {
        if(hasText(messageEditText)) {
            callSubmitMsgService(saved_username,saved_email,messageEditText.getText().toString());
        }
    }
    public void callSubmitMsgService(String name,String email,String message){
        SubmitMsgService submitService = ApiClient.getClient().create(SubmitMsgService.class);
        Call<MessageResponse> call = submitService.submitMsg(name,email,message);
        pd.show();

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                pd.hide();
                if(response != null) {
                    //set UI
                    if (response.body().getResponse().equals("1")) {
                        messageEditText.setText("");
                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.hide();
            }
        });
    }
    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView= inflater.inflate(R.layout.fragment_contact_us, container, false);
        return contentView;
    }
}
