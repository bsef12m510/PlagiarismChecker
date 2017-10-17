package com.prepostseo.plagiarismchecker.contactUs;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.prepostseo.plagiarismchecker.R;
import com.prepostseo.plagiarismchecker.api.ApiClient;
import com.prepostseo.plagiarismchecker.contactUs.response.MessageResponse;
import com.prepostseo.plagiarismchecker.contactUs.restInterface.SubmitMsgService;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUs extends Fragment {

    private String key = "";
    private EditText nameEditText, emailEditText, messageEditText;
    private Button submitButton;
    private View contentView;
    private ProgressDialog pd;
    // Regular Expression
    // you can change the expression based on your need
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    // Error Messages
    private static final String REQUIRED_MSG = "required";
    private static final String EMAIL_MSG = "invalid email";
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
        nameEditText =(EditText)contentView.findViewById(R.id.name);
        emailEditText =(EditText)contentView.findViewById(R.id.email);
        messageEditText =(EditText)contentView.findViewById(R.id.message);
        submitButton=(Button) contentView.findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

    }
    void validateForm()
    {
        if(hasText(nameEditText)&&hasText(emailEditText)&&hasText(messageEditText))
        {
            if(isEmailAddress(emailEditText,true))
            {
                callSubmitMsgService(nameEditText.getText().toString(),emailEditText.getText().toString(),messageEditText.getText().toString());
            }
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
                        nameEditText.setText("");
                        emailEditText.setText("");
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

    // call this method when you need to check emailEditText validation
    public boolean isEmailAddress(EditText editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }
    // return true if the input field is valid, based on the parameter passed
    public  boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        boolean returnValue=false;

        // text required and editText is blank, so return false
        if ( required && !hasText(editText) )
            returnValue= false;


        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            returnValue= false;
        }else
        {
            returnValue=true;
        }

        return returnValue;
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
