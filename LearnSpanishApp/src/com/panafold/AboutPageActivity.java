package com.panafold;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class AboutPageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_page);
	}
	public void back(View v){
		finish();
	}
}
