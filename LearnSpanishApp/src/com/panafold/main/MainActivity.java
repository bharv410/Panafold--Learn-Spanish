package com.panafold.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import zh.wang.android.apis.yweathergetter4a.WeatherInfo;
import zh.wang.android.apis.yweathergetter4a.YahooWeather;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherInfoListener;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import at.theengine.android.bestlocation.BestLocationListener;
import at.theengine.android.bestlocation.BestLocationProvider;
import at.theengine.android.bestlocation.BestLocationProvider.LocationType;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.panafold.AboutPageActivity;
import com.panafold.R;
import com.panafold.RateThisApp;
import com.panafold.adapter.TabsPagerAdapter;
import com.panafold.helpers.AlarmReciever;
import com.panafold.main.datamodel.LocalDBHelper;
import com.panafold.main.datamodel.ReviewWord;
import com.panafold.main.datamodel.SqlLiteDbHelper;
import com.panafold.main.datamodel.Word;
import com.viewpagerindicator.LinePageIndicator;

public class MainActivity extends FragmentActivity implements
		TextToSpeech.OnInitListener, YahooWeatherInfoListener,
		BillingProcessor.IBillingHandler {
	private TextToSpeech tts;
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private YahooWeather mYahooWeather = YahooWeather.getInstance(5000, 5000,
			true);
	private ImageView mIvWeather0;
	private BestLocationProvider mBestLocationProvider;
	private BestLocationListener mBestLocationListener;
	private LocalDBHelper dynamicdb;
	private ProgressBar weatherPB;
	private Boolean currentWordIsSet, supportsTextToSpeech;
	//private BillingProcessor bp;
	private PendingIntent pendingIntent;
	private AlarmManager alarmManager;
	
	public static int shown;
	public static Typeface gothamFont, neutrafaceFont, japaneseFont;
	public static Boolean isTablet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		supportsTextToSpeech = true;
		checkIfTablet();
		// String base64EncodedPublicKey =
		// "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqiLU0GwvBQu7VQTN821qMfmjaec2DKksSfXU8klufTp8H0nPoVnufdb87W5PVIttNWfOQK+3SO+ZTfPNCPYZWf5RBDR9U6Km/jMPxhQ526NdYf9Q4PyBBJDlo96ycDxBdjgi7yoCSfdVsCKgBuThAjsdcUmHrdRMAQIBN9b8IGFH2lhtgQHHbvHXz9k4Vyx/xjMw3YJHaOmh9RtZTKB944u9i1AFVa+YCisvVabeIafV+vcG2D2LdyucWcuG+3LROn8EZhyC3ByJNuexebTKg/7KqWD826bh6o5Wg0AnOa2AdnsyXl18S19oZ44QkKfM7IOpSlB+W4JqXbc7gDaxkwIDAQAB";
		// bp = new BillingProcessor(this, base64EncodedPublicKey, this);

		viewPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		viewPager.setCurrentItem(1);
		viewPager.setOffscreenPageLimit(4);
		CurrentWord.initHashMap();
		CurrentWord.initStrings();
		currentWordIsSet = false;

		final LinePageIndicator titleIndicator = (LinePageIndicator) findViewById(R.id.indicator);
		titleIndicator.setViewPager(viewPager);
		titleIndicator.setCurrentItem(1);

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int i, float v, int i2) {
			}

			@Override
			public void onPageSelected(int i) {
				if (i <= 1) {
					titleIndicator.setBackgroundColor(Color
							.parseColor("#F8EFCB"));
				} else {
					titleIndicator.setBackgroundColor(Color
							.parseColor("#555F5F"));
				}
				titleIndicator.setCurrentItem(i);
				HashMap<Integer, String> getClassName = new HashMap<Integer, String>();
				getClassName.put(0, "com.panafold.main.ChangeWordFragment");
				getClassName.put(1, "com.panafold.main.CurrentWordFragment");
				getClassName.put(2, "com.panafold.main.PhraseFragment");
				getClassName.put(3, "com.panafold.main.WebsiteFragment");
				getClassName.put(4, "com.panafold.main.ChangeWordFragment");
			}

			@Override
			public void onPageScrollStateChanged(int i) {
			}
		});

		// setup text to speech engine
		tts = new TextToSpeech(this, this);
		setupFonts();
		setupDatabases();
		// setupNotifications();
		setWordsThatShouldBeReviewed();
		addWordsToMenu();
		if(CurrentWord.theCurrentWord==null)
		showCorrectWord();
		initLocation();

		//promptForPurchase();
		// get location for waether info
		mBestLocationProvider
				.startLocationUpdatesWithListener(mBestLocationListener);

		setAlarm();
	}

	private void checkIfTablet() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;

		if (width > 1023 || height > 1023) {
			isTablet = true;
		} else {
			isTablet = false;
		}
	}

	public void setAlarm() {
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent alarmIntent = new Intent(MainActivity.this, AlarmReciever.class);
		pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
				alarmIntent, 0);
		Calendar alarmStartTime = Calendar.getInstance();
		// alarmStartTime.set(Calendar.HOUR_OF_DAY, 9);
		// alarmStartTime.set(Calendar.MINUTE, 00);
		// alarmStartTime.set(Calendar.SECOND, 0);
		alarmStartTime.add(Calendar.DAY_OF_YEAR, 1);
		alarmManager.setRepeating(AlarmManager.RTC,
				alarmStartTime.getTimeInMillis(), getInterval(), pendingIntent);
	}

	private void setupFonts() {
		// add custom fonts
		gothamFont = Typeface.createFromAsset(getAssets(),
				"fonts/Gotham-Book.ttf");
		neutrafaceFont = Typeface.createFromAsset(getAssets(),
				"fonts/NeutraText-Demi.otf");
		japaneseFont = Typeface.createFromAsset(getAssets(),
				"fonts/AozoraMinchoMedium.ttf");
	}

//	private void promptForPurchase() {
//		// if 3 words are saved than tell them to purchase
//		if (CurrentWord.previouslySavedStrings.size() == 4
//				|| CurrentWord.previouslySavedStrings.size() == 12
//				|| CurrentWord.previouslySavedStrings.size() == 20) {
//			showDialog();
//		}
//	}

	private int getInterval() {
		int days = 1;
		int hours = 24;
		int minutes = 60;
		int seconds = 60;
		int milliseconds = 1000;
		int repeatMS = days * hours * minutes * seconds * milliseconds;
		return repeatMS;
	}

	@Override
	protected void onStart() {
		super.onStart();
		// If the criteria is satisfied, "Rate this app" dialog will be shown
		RateThisApp.showRateDialogIfNeeded(this);
	}

	@Override
	protected void onPause() {
		initLocation();
		mBestLocationProvider.stopLocationUpdates();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public void onInit(int status) {
		Button soundIcon = (Button) findViewById(R.id.soundButton);
		Button soundIcon2 = (Button) findViewById(R.id.soundButtonPhrase);
		try {
			soundIcon.setVisibility(View.VISIBLE);
			soundIcon2.setVisibility(View.VISIBLE);
		} catch (NullPointerException n) {

		}
		if (status == TextToSpeech.SUCCESS) {
			Locale loc = new Locale("spa", "ESP");
			int result = tts.setLanguage(loc);
			tts.setSpeechRate((float) 0.2);
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				supportsTextToSpeech = false;
			} else {
				supportsTextToSpeech = true;
			}
		}
	}

	private void setupDatabases() {
		try {
			// init databases
			dynamicdb = new LocalDBHelper(MainActivity.this);
			SqlLiteDbHelper dbhelper = new SqlLiteDbHelper(MainActivity.this);

			// open db
			dbhelper.CopyDataBaseFromAsset();
			dbhelper.openDataBase();

			// init static objects
			CurrentWord.allWords = dbhelper.getAllWords();
			CurrentWord.previouslySavedWords = new ArrayList<ReviewWord>();
			CurrentWord.previouslySavedStrings = new ArrayList<String>();

			for (ReviewWord r : dynamicdb.getReviewWords()) {
				CurrentWord.previouslySavedStrings.add(r.getEnglish());
				CurrentWord.previouslySavedWords.add(r);
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	// private void setupNotifications(){
	// Intent myIntent = new Intent(MainActivity.this, MyReciever.class);
	// pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
	// myIntent,0);
	// AlarmManager alarmManager =
	// (AlarmManager)getSystemService(ALARM_SERVICE);
	// alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(),
	// 3000, pendingIntent);
	// }

	private void setWordsThatShouldBeReviewed() {
		CurrentWord.shouldBeBold = new ArrayList<String>();

		// check all words and if haven't been viewed in 9 days. add to
		// shouldBeBold list for review page
		for (ReviewWord r : CurrentWord.previouslySavedWords) {
			try {
				String timestampOfWord = r.getTimeStamp();
				Date wordTimeStamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
						.parse(timestampOfWord);

				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, -3);
				Date nineDaysPrior = calendar.getTime();

				if (wordTimeStamp.before(nineDaysPrior)) {
					System.out.println(r.getEnglish()
							+ "hasn't been clicked in 9days");
					CurrentWord.shouldBeBold.add(r.getEnglish());
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
	}

	private void showCorrectWord() {
		// if they bought all the words then show a random one
		int savedWordsSize = CurrentWord.previouslySavedWords.size();
		int allWordsSize = CurrentWord.allWords.size();
		if (savedWordsSize >= allWordsSize
				&& CurrentWord.theCurrentWord == null) {
			Random r = new Random();
			CurrentWord.theCurrentWord = CurrentWord.allWords.get(r
					.nextInt(allWordsSize - 1));
			currentWordIsSet = true;
			System.out.println("THey bought the words");
			System.out.println("THey bought the words");
			System.out.println("THey bought the words");
		}
		
		
		//if "showword" counter(0 default) is less than 1 (first time opening the app)
		//then show first word.
		if (getSharedPreferences("PrefsFile", 0).getInt("showword", 0)<1) {
			CurrentWord.theCurrentWord=CurrentWord.allWords.get(0);
			//save 6 so it show the 7th word next time
			getSharedPreferences("PrefsFile", 0).edit().putInt("showword", 6).commit(); 
			return;
		}
		 
		int showwordcount=getSharedPreferences("PrefsFile", 0).getInt("showword", 0);
		CurrentWord.theCurrentWord=CurrentWord.allWords.get(showwordcount);
	}

	public void speakOut(View v) {
		// speak the japanese text

		if (!supportsTextToSpeech) {
			Toast.makeText(getApplicationContext(),
					"Your device does not support Japanese text-to-speech",
					Toast.LENGTH_LONG).show();
			supportsTextToSpeech = true;
		}
		TextView textview = (TextView) findViewById(R.id.spanishTextView);
		speakOut(textview.getText().toString());
	}

	private void speakOut(String text) {
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}

	public void speakOutPhrase(View v) {
		TextView textview = (TextView) findViewById(R.id.japanesePhrase);
		speakOut(textview.getText().toString());
	}

	private void initLocation() {
		if (mBestLocationListener == null) {
			mBestLocationListener = new BestLocationListener() {

				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) {
				}

				@Override
				public void onProviderEnabled(String provider) {
				}

				@Override
				public void onProviderDisabled(String provider) {
				}

				@Override
				public void onLocationUpdateTimeoutExceeded(LocationType type) {
				}

				@Override
				public void onLocationUpdate(Location location,
						LocationType type, boolean isFresh) {
					String lat = String.valueOf(location.getLatitude());
					String lng = String.valueOf(location.getLongitude());
					mYahooWeather.queryYahooWeatherByLatLon(
							getApplicationContext(), lat, lng,
							MainActivity.this);

				}
			};

			if (mBestLocationProvider == null) {
				mBestLocationProvider = new BestLocationProvider(this, true,
						true, 10000, 1000, 2, 0);
			}
		}
	}

	@Override
	public void gotWeatherInfo(WeatherInfo weatherInfo) {
		if (weatherInfo != null) {
			// turn off location updates because they are no longer needed.
			mBestLocationProvider.stopLocationUpdates();

			setWeatherIcon(weatherInfo.getCurrentCode());

			// exception thrown when user exits app but weather info is updated
			try {
				// start weather progressbar to indicate loading
				weatherPB = (ProgressBar) findViewById(R.id.weatherProgressBar);
				weatherPB.setVisibility(ProgressBar.GONE);

				TextView weatherTextView = (TextView) findViewById(R.id.weatherTextView);
				weatherTextView.setTypeface(gothamFont);
				CurrentWord.weatherString = weatherInfo.getCurrentTempF()
						+ " °";
				weatherTextView.setText(CurrentWord.weatherString);
			} catch (Exception e) {
				System.out.println("Exited app");
			}
		}
	}

	private void setWeatherIcon(int code) {
		mIvWeather0 = (ImageView) findViewById(R.id.imageView2);
		if (mIvWeather0 != null) {
			switch (code) {
			case 0:
				mIvWeather0.setImageResource(R.drawable.tornado);
				break;
			case 1:
				mIvWeather0.setImageResource(R.drawable.storm);
				break;
			case 2:
				mIvWeather0.setImageResource(R.drawable.tornado);
				break;
			case 3:
				mIvWeather0.setImageResource(R.drawable.storm);
				break;
			case 4:
				mIvWeather0.setImageResource(R.drawable.storm);
				break;
			case 5:
				mIvWeather0.setImageResource(R.drawable.rainsnow);
				break;
			case 6:
				mIvWeather0.setImageResource(R.drawable.rainhail);
				break;
			case 7:
				mIvWeather0.setImageResource(R.drawable.rainsnow);
				break;
			case 8:
				mIvWeather0.setImageResource(R.drawable.rain);
				break;
			case 9:
				mIvWeather0.setImageResource(R.drawable.rain);
				break;
			case 10:
				mIvWeather0.setImageResource(R.drawable.rain);
				break;
			case 11:
				mIvWeather0.setImageResource(R.drawable.rain);
				break;
			case 12:
				mIvWeather0.setImageResource(R.drawable.rain);
				break;
			case 13:
				mIvWeather0.setImageResource(R.drawable.snow);
				break;
			case 14:
				mIvWeather0.setImageResource(R.drawable.snow);
				break;
			case 15:
				mIvWeather0.setImageResource(R.drawable.snow);
				break;
			case 16:
				mIvWeather0.setImageResource(R.drawable.snow);
				break;
			case 17:
				mIvWeather0.setImageResource(R.drawable.rainhail);
				break;
			case 18:
				mIvWeather0.setImageResource(R.drawable.rainhail);
				break;
			case 19:
				mIvWeather0.setImageResource(R.drawable.cloudy);
				break;
			case 20:
				mIvWeather0.setImageResource(R.drawable.foggy);
				break;
			case 21:
				mIvWeather0.setImageResource(R.drawable.foggy);
				break;
			case 22:
				mIvWeather0.setImageResource(R.drawable.foggy);
				break;
			case 23:
				mIvWeather0.setImageResource(R.drawable.foggy);
				break;
			case 24:
				mIvWeather0.setImageResource(R.drawable.windy);
				break;
			case 25:
				mIvWeather0.setImageResource(R.drawable.cloudy);
				break;
			case 26:
				mIvWeather0.setImageResource(R.drawable.cloudy);
				break;
			case 27:
				mIvWeather0.setImageResource(R.drawable.cloudy);
				break;
			case 28:
				mIvWeather0.setImageResource(R.drawable.cloudy);
				break;
			case 29:
				mIvWeather0.setImageResource(R.drawable.cloudy);
				break;
			case 30:
				mIvWeather0.setImageResource(R.drawable.cloudy);
				break;
			case 31:
				mIvWeather0.setImageResource(R.drawable.cloudy);
				break;
			case 32:
				mIvWeather0.setImageResource(R.drawable.sunny);
				break;
			case 33:
				mIvWeather0.setImageResource(R.drawable.cloudy);
				break;
			case 34:
				mIvWeather0.setImageResource(R.drawable.cloudy);
				break;
			case 35:
				mIvWeather0.setImageResource(R.drawable.rainsnow);
				break;
			case 36:
				mIvWeather0.setImageResource(R.drawable.sunny);
				break;
			case 37:
				mIvWeather0.setImageResource(R.drawable.thunderstorm);
				break;
			case 38:
				mIvWeather0.setImageResource(R.drawable.thunderstorm);
				break;
			case 39:
				mIvWeather0.setImageResource(R.drawable.thunderstorm);
				break;
			case 40:
				mIvWeather0.setImageResource(R.drawable.rain);
				break;
			case 41:
				mIvWeather0.setImageResource(R.drawable.snow);
				break;
			case 42:
				mIvWeather0.setImageResource(R.drawable.snow);
				break;
			case 43:
				mIvWeather0.setImageResource(R.drawable.snow);
				break;
			case 44:
				mIvWeather0.setImageResource(R.drawable.storm);
				break;
			case 45:
				mIvWeather0.setImageResource(R.drawable.cloudy);
				break;
			case 46:
				mIvWeather0.setImageResource(R.drawable.rainsnow);
				break;
			case 47:
				mIvWeather0.setImageResource(R.drawable.thunderstorm);
				break;
			default:
				mIvWeather0.setImageResource(R.drawable.sunny);
				break;
			}
		}
	}

	private void addWordsToMenu() {
		CurrentWord.beginningReviewWords = new ArrayList<Word>();
		for (int i = 1; i < getSharedPreferences("PrefsFile", 0).getInt("showword", 7); i++) {
			CurrentWord.beginningReviewWords.add(CurrentWord.allWords.get(i));

		}

	}

	// private void saveDateAndNumberOfWords() {
	//
	// //if date is the same as the saved date then keep the same word
	// //String outputString = "Hello world!";
	// Calendar now = Calendar.getInstance();
	// SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd",
	// Locale.ENGLISH);
	//
	// if(lastDate()==null){
	// //first time using app
	// saveDate(dff.format(now.getTime()));
	// }else{
	//
	// //check if the last date saved is the same as todays
	// if(dff.format(now.getTime()).contains(lastDate())){
	// //load the same word
	// }else{
	// //load different word.
	// //save the new date
	// saveDate(dff.format(now.getTime()));
	// }
	// }
	//
	// }

	private Boolean itsANewDay() {
		Calendar now = Calendar.getInstance();
		SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd",
				Locale.ENGLISH);
		String theTime = dff.format(now.getTime());
		if (lastDate() == null) {
			// first time opening app
			saveDate(theTime);
			return true;
		} else {

			// check if the last date saved is the same as todays
			if (theTime.contains(lastDate())) {
				return false;
			} else {
				// load different word.
				// save the new date
				saveDate(dff.format(now.getTime()));
				return true;
			}
		}
	}

	private void saveDate(String date) {
		try {
			FileOutputStream outputStream = openFileOutput("date.txt",
					Context.MODE_PRIVATE);
			outputStream.write(date.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String lastDate() {
		try {
			FileInputStream inputStream = openFileInput("date.txt");
			BufferedReader r = new BufferedReader(new InputStreamReader(
					inputStream));
			StringBuilder total = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				total.append(line);
			}
			r.close();
			inputStream.close();
			Log.d("File", "File contents: " + total);
			return total.toString();
		} catch (Exception e) {
			Calendar now = Calendar.getInstance();
			SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd",
					Locale.ENGLISH);
			String theTime = dff.format(now.getTime());
			saveDate(theTime);
			e.printStackTrace();
			return null;
		}
	}

//	private void showDialog() {
//		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//				MainActivity.this);
//
//		// set title
//		alertDialogBuilder.setTitle("Purchase More Words?");
//
//		// set dialog message
//		alertDialogBuilder
//				.setMessage(
//						"Do you want to purchase all words and phrases rather than recieving 1 per day?")
//				.setCancelable(false)
//				.setPositiveButton("Yes",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog, int id) {
//								dialog.cancel();
//								bp.purchase("com.panafold.allwords");
//								System.out.println("clicked");
//							}
//						})
//				.setNegativeButton("No", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int id) {
//						dialog.cancel();
//					}
//				});
//
//		// create alert dialog
//		AlertDialog alertDialog = alertDialogBuilder.create();
//
//		// show it
//		alertDialog.show();
//	}

	public void goToTutorialPage(View v) {
		startActivity(new Intent(MainActivity.this, AboutPageActivity.class));
	}

	public void aboutPage(View v) {
		startActivity(new Intent(MainActivity.this, AboutPageActivity.class));
	}

//	public void getMoreWords(View v) {
//		showDialog();
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (!bp.handleActivityResult(requestCode, resultCode, data))
//			super.onActivityResult(requestCode, resultCode, data);
//	}

	@Override
	public void onBillingInitialized() {
		System.out.println("onBillingInitialized");
	}

	@Override
	public void onProductPurchased(String productId,
			TransactionDetails transactionId) {
		/*
		 * Called then requested PRODUCT ID was successfully purchased
		 */
		System.out.println("onProductPurchased");
		LocalDBHelper dynamicdb = new LocalDBHelper(MainActivity.this);
		dynamicdb.getWritableDatabase();

		for (Word w : CurrentWord.allWords) {
			if (CurrentWord.previouslySavedStrings.contains(w.getEnglish())) {
				// if word is already saved do nothing
			} else {
				dynamicdb.addWord(w);
			}

		}
		startActivity(new Intent(MainActivity.this, MainActivity.class));

	}

	@Override
	public void onBillingError(int errorCode, Throwable error) {
		/*
		 * Called then some error occured. See Constants class for more details
		 */
		System.out.println("onBillingError");
	}

	@Override
	public void onPurchaseHistoryRestored() {
		/*
		 * Called then purchase history was restored and the list of all owned
		 * PRODUCT ID's was loaded from Google Play
		 */
		System.out.println("onPurchaseHistoryRestored");
	}
}