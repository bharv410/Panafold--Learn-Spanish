	package com.panafold.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panafold.R;

public class CurrentWordFragment extends Fragment {
	ProgressBar weatherPB;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_imageandword, container, false);
		return rootView;
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {       
        super.onActivityCreated(savedInstanceState);
        
      //set big pic image to the corresponding resource in the static hashmap
      		ImageView bigPic=(ImageView)getActivity().findViewById(R.id.bigPicImageView);
      		
      		if(MainActivity.isTablet){
      			android.view.ViewGroup.LayoutParams layoutParams = bigPic.getLayoutParams();
      			layoutParams.width = 550;
      			layoutParams.height = 471;
      			bigPic.setLayoutParams(layoutParams);
      			bigPic.requestLayout();
      			layoutParams = bigPic.getLayoutParams();      			
      		}
      		
      		System.out.println(CurrentWord.theCurrentWord.getEnglish());
      		bigPic.setImageDrawable(getResources().getDrawable(CurrentWord.getImage.get(CurrentWord.theCurrentWord.getEnglish())));
      		
        
        
        //set english text
        TextView text = (TextView) getActivity().findViewById(R.id.englishTextView);
        text.setTypeface(MainActivity.gothamFont);
		text.setText(CurrentWord.theCurrentWord.getEnglish());
		//set thejapenese text

		
		//set image credits
		TextView text3 = (TextView) getActivity().findViewById(R.id.imageCreditTextView);
		text3.setText(CurrentWord.theCurrentWord.getCred());
		
        
        //initialize progress bar
//        weatherPB=(ProgressBar)getActivity().findViewById(R.id.weatherProgressBar);
//        weatherPB.setVisibility(ProgressBar.VISIBLE);
        
        //set current date and time 
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("dd MMMM",Locale.US);
        SimpleDateFormat day_date = new SimpleDateFormat("EEE",Locale.US);
        String month_name = month_date.format(cal.getTime());
        String day_name = day_date.format(cal.getTime());
        String daytext = day_name.toUpperCase(Locale.US);
        TextView dateview = (TextView) getActivity().findViewById(R.id.dateTextView);
        dateview.setTypeface(MainActivity.gothamFont);
		dateview.setText(month_name + "\n " +daytext);
		MainActivity.shown=0; 
		
		//if you click the bottom part of screen it will toggle between date or japanese translation
		RelativeLayout relativeclic1 =(RelativeLayout)getActivity().findViewById(R.id.relativeLayout1);
		
		//set background to the current random color
		relativeclic1.setBackgroundColor(Color.parseColor(CurrentWord.currentColor));
		
		//set on click listener to change textviews
        relativeclic1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            	ProgressBar weatherPB = (ProgressBar) getActivity().findViewById(R.id.weatherProgressBar);
    			weatherPB.setVisibility(ProgressBar.GONE);
		    	
            	if(MainActivity.shown%2==0){
    	    		TextView japanTextView = (TextView)getActivity().findViewById(R.id.spanishTextView);
    	    		
    	    		japanTextView.setTypeface(MainActivity.japaneseFont);
    		    	japanTextView.setVisibility(View.VISIBLE);
    		    	japanTextView.setText(CurrentWord.theCurrentWord.getLang2());
    		    	//show japanese and hide weather and date and line
    		    	TextView dateView = (TextView)getActivity().findViewById(R.id.dateTextView);
    		    	dateView.setVisibility(View.INVISIBLE);
    		    	TextView weatherTextView =(TextView)getActivity().findViewById(R.id.weatherTextView);
    		    	weatherTextView.setVisibility(View.INVISIBLE);
    		    	RelativeLayout beigeBlock =(RelativeLayout)getActivity().findViewById(R.id.beigeBlock);
    		    	beigeBlock.setBackgroundColor(Color.parseColor("#F8EFCB"));
    		    	ImageView cloudIconImageView =(ImageView)getActivity().findViewById(R.id.imageView2);
    		    	cloudIconImageView.setVisibility(View.INVISIBLE);
    	    		MainActivity.shown++;
//    	    	}else if(MainActivity.shown%3==1){
//    	    		String romText2 = CurrentWord.theCurrentWord.getHirigana().replace("\r\n", " ").replace("\n", " ");
//    		    	romTextView.setText(romText2);
//    		    	System.out.println(romText2);
//    		    	System.out.println(romText2);
//    		    	romTextView.setTypeface(MainActivity.japaneseFont);
//    		    	MainActivity.shown++;MainActivity.shown++;
//    	    	}else{
            	}else{
    	    		TextView japanTextView = (TextView)getActivity().findViewById(R.id.spanishTextView);
    		    	japanTextView.setVisibility(View.INVISIBLE);
    		    	//hide japanese and show weather and date and line
    		    	TextView dateView = (TextView)getActivity().findViewById(R.id.dateTextView);
    		    	dateView.setVisibility(View.VISIBLE);
    		    	TextView weatherTextView =(TextView)getActivity().findViewById(R.id.weatherTextView);
    		    	weatherTextView.setVisibility(View.VISIBLE);
    		    	RelativeLayout beigeBlock =(RelativeLayout)getActivity().findViewById(R.id.beigeBlock);
    		    	beigeBlock.setBackgroundColor(Color.TRANSPARENT);
    		    	ImageView cloudIconImageView =(ImageView)getActivity().findViewById(R.id.imageView2);
    		    	cloudIconImageView.setVisibility(View.VISIBLE);
    	    		MainActivity.shown++;
    	    	}
            }
        });
      //start weather progressbar to indicate loading
        if(CurrentWord.weatherString==null){
      		weatherPB = (ProgressBar) getActivity().findViewById(R.id.weatherProgressBar);
      		weatherPB.setVisibility(ProgressBar.VISIBLE);
        }else{
        	TextView weatherTextView = (TextView)getActivity().findViewById(R.id.weatherTextView);
        	weatherTextView.setTypeface(MainActivity.gothamFont);
			weatherTextView.setText(CurrentWord.weatherString);
        }
    }
}
