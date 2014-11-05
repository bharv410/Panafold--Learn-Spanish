package com.panafold.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.panafold.R;

public class PhraseFragment extends Fragment {
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {       
        super.onActivityCreated(savedInstanceState);
        TextView engPhr = (TextView)getActivity().findViewById(R.id.englishPhrase);
        engPhr.setText(CurrentWord.theCurrentWord.getEnglPhrase());
        TextView japPhr =(TextView)getActivity().findViewById(R.id.spanishPhrase);
        japPhr.setText(CurrentWord.theCurrentWord.getLang2Phrase());
        
//        RelativeLayout rl = (RelativeLayout)getActivity().findViewById(R.id.phraseFrag);
//        rl.setBackgroundColor(Color.parseColor(CurrentWord.currentColor));
    }

    @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_phrase, container, false);        
        
		return rootView;

    }

}
