package com.prepostseo.plagiarismchecker.checker.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.prepostseo.plagiarismchecker.R;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;


import org.docx4j.XmlUtils;
import org.docx4j.convert.out.html.HtmlExporterNonXSLT;
import org.docx4j.model.images.ConversionImageHandler;
import org.docx4j.openpackaging.io.LoadFromZipNG;
import org.docx4j.openpackaging.packages.OpcPackage;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

import java.io.BufferedInputStream;
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
    private Button uploadButton;
    private EditText content;
    private View contentView;
    private OnFragmentInteractionListener mListener;

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
//        setClickListeners();
//        pd = new ProgressDialog(getActivity());

    }

    public void initialize() {
        uploadButton = (Button) contentView.findViewById(R.id.uploadFile);
        content = (EditText) contentView.findViewById(R.id.content);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionsAndOpenFilePicker();
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

        if (resultCode == Activity.RESULT_OK && data != null) {
            docPaths = new ArrayList<>();
            docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
            if (docPaths.size() > 0) {
                Toast.makeText(getActivity(), docPaths.get(0), Toast.LENGTH_SHORT).show();
//                extractTextFromPdf(docPaths.get(0));
//                readFromTextFile(docPaths.get(0));
//                readDocxFile(docPaths.get(0));
                doc4jx(docPaths.get(0));
            }
        }
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



   /* public static void readDocFile(String fileName) {

        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());

            HWPFDocument doc = new HWPFDocument(fis);

            WordExtractor we = new WordExtractor(doc);

            String[] paragraphs = we.getParagraphText();

            System.out.println("Total no of paragraph "+paragraphs.length);
            for (String para : paragraphs) {
                System.out.println(para.toString());
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

   /* public static void readDocxFile(String fileName) {

        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());



           *//* Tika tika = new Tika();
            String extractedText = tika.parseToString(file);*//*

            XWPFDocument document = new XWPFDocument(fis);

            List<XWPFParagraph> paragraphs = document.getParagraphs();

            System.out.println("Total no of paragraph "+paragraphs.size());
            for (XWPFParagraph para : paragraphs) {
                System.out.println(para.getText());
            }
            fis.close();
        } catch (Exception e) {
            e.getCause();

        }
    }*/

    public void doc4jx(final String fileName) {
        final ProgressDialog pd =new ProgressDialog(getActivity());

        pd.show();


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //TODO your background code
                try {

                    InputStream is = new FileInputStream(new File(fileName));
//			File file = new File(this.getFilesDir(), "samples.docx");
//			OpcPackage opcPackage = OpcPackage.load(is);
//			WordprocessingMLPackage wordMLPackage = (WordprocessingMLPackage)opcPackage;
//			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(is);
//			MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();


                    final LoadFromZipNG loader = new LoadFromZipNG();
                    WordprocessingMLPackage wordMLPackage = (WordprocessingMLPackage)loader.get(is);

                    String IMAGE_DIR_NAME = "images";

                    String baseURL = getActivity().getDir(IMAGE_DIR_NAME, Context.MODE_WORLD_READABLE).toURL().toString();
                    System.out.println(baseURL); // file:/data/data/com.example.HelloAndroid/app_images/

                    // Uncomment this to write image files to file system
                    ConversionImageHandler conversionImageHandler = new AndroidFileConversionImageHandler( IMAGE_DIR_NAME, // <-- don't use a path separator here
                            baseURL, false, getActivity());

                    // Uncomment to use a base 64 encoded data URI for each image
//			ConversionImageHandler conversionImageHandler = new AndroidDataUriImageHandler();

                    HtmlExporterNonXSLT withoutXSLT = new HtmlExporterNonXSLT(wordMLPackage, conversionImageHandler);

                    final String html = XmlUtils.w3CDomNodeToString(withoutXSLT.export());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

//stuff that updates ui
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
