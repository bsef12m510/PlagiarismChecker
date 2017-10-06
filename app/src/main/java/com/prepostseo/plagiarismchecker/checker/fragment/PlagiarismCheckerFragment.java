package com.prepostseo.plagiarismchecker.checker.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
//import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.awt.geom.CubicCurve2D;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.prepostseo.plagiarismchecker.R;
import com.prepostseo.plagiarismchecker.api.ApiClient;
import com.prepostseo.plagiarismchecker.checker.response.PlagiarismDetail;
import com.prepostseo.plagiarismchecker.checker.response.PlagiarismResponse;
import com.prepostseo.plagiarismchecker.checker.restInterface.PlagiarismService;

import org.apache.commons.io.FilenameUtils;
import org.docx4j.XmlUtils;
import org.docx4j.convert.out.html.HtmlExporterNonXSLT;
import org.docx4j.model.images.ConversionImageHandler;
import org.docx4j.openpackaging.io.LoadFromZipNG;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlagiarismCheckerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PlagiarismCheckerFragment extends Fragment {
    public static final int PERMISSIONS_REQUEST_CODE = 0;
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    private List<String> docPaths;
    private Button uploadButton, checkButton;
    private EditText content;
    private View contentView;
    private OnFragmentInteractionListener mListener;
    private String key = "";
    private ProgressDialog pd;
    private TextView apiProgressTextView,uniquePerTextView,plagPerTextView;
    private DialogPlus customDialogBox;
    private ScrollView dialogueBoxView;
    private TextView wordsLimit,currentwords;


    public PlagiarismCheckerFragment() {
        // Required empty public constructor
    }
    void setTitle()
    {
        ((Activity) getActivity()).setTitle(getResources().getString(R.string.app_name));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_plagiarism_checker, container, false);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle();
        initialize();
        getKey();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(content != null)
            content.setError(null);
    }

    public void getKey(){
        SharedPreferences shared = getActivity().getSharedPreferences( "com.prepostseo.plagiarismchecker", MODE_PRIVATE);
        key = shared.getString("api_key", "");
    }

    public void initialize() {
        uploadButton = (Button) contentView.findViewById(R.id.uploadFile);
        checkButton = (Button) contentView.findViewById(R.id.check);
        content = (EditText) contentView.findViewById(R.id.content);
        apiProgressTextView=(TextView)contentView.findViewById(R.id.api_progress_text_view);
        wordsLimit=(TextView)contentView.findViewById(R.id.limit_number);
        currentwords=(TextView)contentView.findViewById(R.id.current_words);
        wordsLimit.setText( Integer.toString( getWordsLimit()));


        pd = new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionsAndOpenFilePicker();
            }
        });
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(content.getText().toString().trim().equalsIgnoreCase(""))
                    content.setError("Cannot be empty.");
                else if(content.getText().toString().length() > 5000)
                    content.setError("Word limit exceeded.");
                else {
                    content.setError(null);
                    callPlagiarismService(content.getText().toString());
                }
            }
        });
        content.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                StringTokenizer st = new StringTokenizer(s.toString());
                currentwords.setText(Integer.toString( st.countTokens()));
            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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


    private void checkPermissionsAndOpenFilePicker() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                showError();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            openFilePicker();
        }
    }

    private void showError() {
       // Toast.makeText(getActivity(), "Allow external storage reading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFilePicker();
                } else {
                    showError();
                }
            }
        }
    }

    private void openFilePicker() {
        FilePickerBuilder.getInstance().setMaxCount(1)
                .setActivityTheme(R.style.AppTheme)
                .pickFile(getActivity());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String ext;
        if (resultCode == Activity.RESULT_OK && data != null) {
            docPaths = new ArrayList<>();
            docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
            if (docPaths.size() > 0) {
               // Toast.makeText(getActivity(), docPaths.get(0), Toast.LENGTH_SHORT).show();
                content.setText("");
                ext = getFileType(docPaths.get(0));
                if ("pdf".equalsIgnoreCase(ext))
                    extractTextFromPdf(docPaths.get(0));
                else if ("txt".equalsIgnoreCase(ext))
                    readFromTextFile(docPaths.get(0));
                else
                    docx4j(docPaths.get(0));
            }
        }
    }

    public String getFileType(String filePath) {
        String ext = "";
        if (filePath != null) {
            ext = FilenameUtils.getExtension(filePath);
        }

        return ext;
    }

    public String extractTextFromPdf(String filePath) {
        String fileContent = "";
        try {
            PdfReader reader = new PdfReader(filePath);
            int n = reader.getNumberOfPages();
            String str = PdfTextExtractor.getTextFromPage(reader, 1); //Extracting the content from a particular page.
            content.setText(str);
            reader.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return fileContent;
    }

    private String readFromTextFile(String filePath) {

        String ret = "";
        try {
            InputStream inputStream = new FileInputStream(new File(filePath));
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
                content.setText(ret);
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public void docx4j(final String fileName) {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);
        pd.show();


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //TODO your background code
                try {

                    InputStream is = new FileInputStream(new File(fileName));

                    final LoadFromZipNG loader = new LoadFromZipNG();
                    WordprocessingMLPackage wordMLPackage = (WordprocessingMLPackage) loader.get(is);

                    String IMAGE_DIR_NAME = "images";

                    String baseURL = getActivity().getDir(IMAGE_DIR_NAME, MODE_PRIVATE).toURL().toString();
                    System.out.println(baseURL);

                    ConversionImageHandler conversionImageHandler = new AndroidFileConversionImageHandler(IMAGE_DIR_NAME,
                            baseURL, false, getActivity());

                    HtmlExporterNonXSLT withoutXSLT = new HtmlExporterNonXSLT(wordMLPackage, conversionImageHandler);

                    final String html = Jsoup.parse(XmlUtils.w3CDomNodeToString(withoutXSLT.export()), "ISO-8859-1").select("body").text();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            content.setText(html);
                            pd.hide();

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            pd.hide();

                        }
                    });
                }
            }
        });
    }
    int getWordsLimit() {
        try
        {
            SharedPreferences shared = getActivity().getSharedPreferences("com.prepostseo.plagiarismchecker", MODE_PRIVATE);
            boolean isPremium = (shared.getBoolean("membership", false));
            if (isPremium)
                return getResources().getInteger(R.integer.premium);
            else
                return getResources().getInteger(R.integer.free);
        }catch (Exception e)
        {
            return 0;
        }
    }

    public void callPlagiarismService(String data){
        PlagiarismService plagService = ApiClient.getClient().create(PlagiarismService.class);
        RequestBody keyParam = RequestBody.create(MediaType.parse("text/plain"), key);
        RequestBody dataParam = RequestBody.create(MediaType.parse("text/plain"), data);


        Call<PlagiarismResponse> call = plagService.checkPlagiarism(keyParam, dataParam);
        pd.show();

        inflateDialogBoxViewLayout();
        call.enqueue(new Callback<PlagiarismResponse>() {
            @Override
            public void onResponse(Call<PlagiarismResponse> call, Response<PlagiarismResponse> response) {
                pd.hide();
                if(response != null){
                    updateResult(response);
                }
            }

            @Override
            public void onFailure(Call<PlagiarismResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.hide();
            }
        });
    }
    private void updateResult( Response<PlagiarismResponse> response)
    {

        apiProgressTextView.setText("100%");
        uniquePerTextView.setText( response.body().getUniquePercent().intValue()+"%");
        plagPerTextView.setText(response.body().getPlagPercent().intValue()+"%");
        addDetailedItems(response.body().getDetails());


        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        double height = size.y* 0.8;
        customDialogBox = DialogPlus.newDialog(getActivity())
        .setContentHolder(new ViewHolder(dialogueBoxView))
        .setHeader(R.layout.result_header)
        .setGravity(Gravity.CENTER)
        .setCancelable(true)
        .setInAnimation(R.anim.abc_fade_in)
        .setOutAnimation(R.anim.abc_fade_out)
        .setMargin(16, 16, 16, 16)
        .setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT)  // or any custom width ie: 300
        .setContentHeight((int)height)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        onDialogBoxClicked(dialog,view);
                    }
                })
        .setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogPlus dialog) {
                apiProgressTextView.setText("0%");
            }
        })
        .create();
        customDialogBox.show();


        //Toast.makeText(getActivity(),"Plagiarism : " + response.body().getPlagPercent().toString() + " Uniqueness : " + response.body().getUniquePercent().toString(),Toast.LENGTH_SHORT).show();
    }
    void onDialogBoxClicked(DialogPlus dialog, View view)
    {
        if(view.getId()==R.id.cross_imgview)
        {
            if(customDialogBox.isShowing()) {
                customDialogBox.dismiss();
            }
        }
    }
    void addDetailedItems(List<PlagiarismDetail> detailItems)
    {
        if(!detailItems.isEmpty())
        {
            for (PlagiarismDetail item:detailItems)
            {
                if(item!=null)
                {
                    if(item.getUnique())
                    {
                        LinearLayout itemLayout = inflateUniqueItemLayout();
                        TextView textView=(TextView) itemLayout.findViewById(R.id.detail_text);
                        textView.setText(item.getQuery());
                        ((LinearLayout)dialogueBoxView.findViewById(R.id.detail_layout)).addView(itemLayout);
                    }else
                    {
                        LinearLayout itemLayout=inflatePlagiarizedItemLayout();
                        TextView textView=(TextView) itemLayout.findViewById(R.id.detail_text);
                        textView.setText(item.getQuery());
                        ((LinearLayout)dialogueBoxView.findViewById(R.id.detail_layout)).addView(itemLayout);
                    }
                }
            }
        }
    }
    void inflateDialogBoxViewLayout()
    {
        dialogueBoxView=(ScrollView) LayoutInflater.from(getActivity()).inflate(R.layout.result_view, null);
        uniquePerTextView=(TextView)dialogueBoxView.findViewById(R.id.unique_perc_text_view);
        plagPerTextView=(TextView)dialogueBoxView.findViewById(R.id.plag_perc_text_view);
    }
    LinearLayout inflatePlagiarizedItemLayout()
    {
       return (LinearLayout)LayoutInflater.from(getActivity()).inflate(R.layout.plagiarise_content_view, null);
    }
    LinearLayout inflateUniqueItemLayout()
    {
        return (LinearLayout)LayoutInflater.from(getActivity()).inflate(R.layout.unique_content_view, null);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
