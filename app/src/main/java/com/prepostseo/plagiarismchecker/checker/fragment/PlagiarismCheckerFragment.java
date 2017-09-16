package com.prepostseo.plagiarismchecker.checker.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.prepostseo.plagiarismchecker.R;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
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
                extractTextFromPdf(docPaths.get(0));
            }
        }
    }

    public String extractTextFromPdf(String filePath){
        String fileContent="";
        try {
            PdfReader reader = new PdfReader(filePath);
            int n = reader.getNumberOfPages();
            String str=PdfTextExtractor.getTextFromPage(reader,1); //Extracting the content from a particular page.
//            System.out.println(str);
            content.setText(str);
            reader.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return fileContent;
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
