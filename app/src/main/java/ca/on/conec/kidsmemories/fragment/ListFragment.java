package ca.on.conec.kidsmemories.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import ca.on.conec.kidsmemories.R;

/**
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    // Constructor
    public ListFragment() {
        // Required empty public constructor
    }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        WebView webView;

        webView = (WebView) v.findViewById(R.id.webView);

        //Load the vaccination schedule site into the Web View
        webView.loadUrl("https://www.canada.ca/en/public-health/services/provincial-territorial-immunization-information/provincial-territorial-routine-vaccination-programs-infants-children.html");

        return v;
    }
}