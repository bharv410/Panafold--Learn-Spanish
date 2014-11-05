package com.panafold.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.panafold.R;

public class WebsiteFragment extends Fragment {
	private String currentURL;
	WebView wv;
	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {       
	        super.onActivityCreated(savedInstanceState);
	    }
	
	    @Override
	   public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
	        currentURL="https://www.youtube.com/results?search_query="+CurrentWord.theCurrentWord.getLang2();
	        if (currentURL != null) {
	            wv = (WebView) rootView.findViewById(R.id.webView1);
	            wv.getSettings().setJavaScriptEnabled(true);
	            wv.setWebViewClient(new SwAWebClient());
	            wv.loadUrl(currentURL);
	        }
			return rootView;
	    }
	    private class SwAWebClient extends WebViewClient {
	        @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            return false;
	        }
	    }
	    
	    @Override
	    public void onPause(){
	    	super.onPause();
	    	wv.loadUrl("http://www.google.com");
	    	wv.destroy();
	    	getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
	    }
	}
