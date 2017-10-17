package com.prepostseo.plagiarismchecker.aboutUs;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prepostseo.plagiarismchecker.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AboutUs.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AboutUs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutUs extends Fragment {

    public AboutUs() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutUs.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutUs newInstance(String param1, String param2) {
        AboutUs fragment = new AboutUs();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }
}
