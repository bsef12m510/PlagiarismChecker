package com.prepostseo.plagiarismchecker.checker.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.prepostseo.plagiarismchecker.R;
import com.prepostseo.plagiarismchecker.api.ApiClient;
import com.prepostseo.plagiarismchecker.checker.response.PlagiarismResponse;
import com.prepostseo.plagiarismchecker.checker.restInterface.PlagiarismService;
import com.prepostseo.plagiarismchecker.register.response.RegisterResponse;
import com.prepostseo.plagiarismchecker.register.restInterface.RegisterService;

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

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

    public PlagiarismCheckerFragment() {
        // Required empty public constructor
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
        initialize();
        getKey();
//        setClickListeners();
//        pd = new ProgressDialog(getActivity());

    }

    public void getKey(){
        SharedPreferences shared = getActivity().getSharedPreferences( "com.prepostseo.plagiarismchecker", Context.MODE_PRIVATE);
        key = shared.getString("api_key", "");
    }

    public void initialize() {
        uploadButton = (Button) contentView.findViewById(R.id.uploadFile);
        checkButton = (Button) contentView.findViewById(R.id.check);
        content = (EditText) contentView.findViewById(R.id.content);
        pd = new ProgressDialog(getActivity());
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionsAndOpenFilePicker();
            }
        });
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPlagiarismService(content.getText().toString());
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
//            showFileChooser();
            openFilePicker();
        }
    }

    private void showError() {
        Toast.makeText(getActivity(), "Allow external storage reading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    showFileChooser();
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
                Toast.makeText(getActivity(), docPaths.get(0), Toast.LENGTH_SHORT).show();
                ext = getFileType(docPaths.get(0));
                if ("pdf".equalsIgnoreCase(ext))
                    extractTextFromPdf(docPaths.get(0));
                else if ("txt".equalsIgnoreCase(ext))
                    readFromTextFile(docPaths.get(0));
                else
                    doc4jx(docPaths.get(0));
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
//            System.out.println(str);
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
//            FileInputStream fis = new FileInputStream(new File(filePath));
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
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public void doc4jx(final String fileName) {
        final ProgressDialog pd = new ProgressDialog(getActivity());

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

                    String baseURL = getActivity().getDir(IMAGE_DIR_NAME, Context.MODE_WORLD_READABLE).toURL().toString();
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

    public void callPlagiarismService(String data){
        PlagiarismService plagService = ApiClient.getClient().create(PlagiarismService.class);
        RequestBody keyParam = RequestBody.create(MediaType.parse("text/plain"), key);
        RequestBody dataParam = RequestBody.create(MediaType.parse("text/plain"), data);


        Call<PlagiarismResponse> call = plagService.checkPlagiarism(keyParam, dataParam);
        pd.show();
        call.enqueue(new Callback<PlagiarismResponse>() {
            @Override
            public void onResponse(Call<PlagiarismResponse> call, Response<PlagiarismResponse> response) {
                pd.hide();
                if(response != null){
                    Toast.makeText(getActivity(),"Plagiarism : " + response.body().getPlagPercent().toString() + " Uniqueness : " + response.body().getUniquePercent().toString(),Toast.LENGTH_SHORT).show();
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
