package com.panafold.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.panafold.R;
import com.panafold.main.datamodel.ReviewWord;
import com.panafold.main.datamodel.Word;

public class CurrentWord {
	public static Word theCurrentWord;
	public static List<Word> beginningReviewWords, allWords;
	public static List<ReviewWord> previouslySavedWords;
	public static List<String> previouslySavedStrings, shouldBeBold;
	public static String weatherString;
	public static HashMap<String, Integer> getImage;
	public static String currentColor;

	public static String translate(String text) throws IOException {
		// fetch
		URL url = new URL(
				"http://translate.google.com.tw/translate_a/t?client=t&hl=en&sl=en&tl=ja&ie=UTF-8&oe=UTF-8&multires=1&oc=1&otf=2&ssel=0&tsel=0&sc=1&q="
						+ URLEncoder.encode(text, "UTF-8"));
		URLConnection urlConnection = url.openConnection();
		urlConnection.setRequestProperty("User-Agent", "Something Else");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				urlConnection.getInputStream()));
		String result = br.readLine();
		br.close();
		// parse
		// System.out.println(result);
		result = result.substring(2, result.indexOf("]]") + 1);
		StringBuilder sb = new StringBuilder();
		String[] splits = result.split("(?<!\\\\)\"");
		for (int i = 1; i < splits.length; i += 8)
			sb.append(splits[i]);
		return sb.toString().replace("\\n", "\n").replaceAll("\\\\(.)", "$1");
	}

	public static void initStrings() {
		String[] colors = new String[5];
		colors[0] = "#E87505";
		colors[1] = "#958B02";
		colors[2] = "#A5160F";
		colors[3] = "#443138";
		colors[4] = "#18A893";

		Random rd = new Random();
		int randomNum = rd.nextInt(5);
		currentColor = colors[randomNum];

	}

	public static void initHashMap() {

		getImage = new HashMap<String, Integer>();

		getImage.put("first and second semester", R.drawable.weather);

		getImage.put("telephone", R.drawable.cellphone);

		getImage.put("bathroom", R.drawable.toilet);

		getImage.put("What are you like?", R.drawable.cool);

		getImage.put("really?", R.drawable.understand);

		getImage.put("impatient", R.drawable.healthyfine);

		getImage.put("police", R.drawable.police);

		getImage.put("virtual reality", R.drawable.virtualreality);

		getImage.put("bathroom", R.drawable.toilet);

		getImage.put("television", R.drawable.television);

		getImage.put("taxi", R.drawable.taxi);

		getImage.put("coffee", R.drawable.coffee);

		getImage.put("spider", R.drawable.spider);

		getImage.put("kilometer", R.drawable.kilometer);

		getImage.put("internet", R.drawable.internet);

		getImage.put("urban", R.drawable.urban);

		getImage.put("apartment", R.drawable.apartment);

		getImage.put("bad thing", R.drawable.badthing);

		getImage.put("give critical assistance",
				R.drawable.providecriticalassistance);

		getImage.put("understand", R.drawable.understand);

		getImage.put("practice", R.drawable.practice);

		getImage.put("apple", R.drawable.apple);

		getImage.put("camel", R.drawable.camel);

		getImage.put("four, five, six", R.drawable.fourfivesix);

		getImage.put("firstandsecondsemester", R.drawable.slowly);

		getImage.put("quit", R.drawable.quit);

		getImage.put("problem", R.drawable.problem);

		getImage.put("hello hello (phone)", R.drawable.hellohello);

		getImage.put("once again", R.drawable.onceagain);

		getImage.put("eyeglasses", R.drawable.eyeglasses);

		getImage.put("business card", R.drawable.businesscard);

		getImage.put("green", R.drawable.green);

		getImage.put("short", R.drawable.short_);

		getImage.put("again", R.drawable.again);

		getImage.put("chubby", R.drawable.chubby);

		getImage.put("incompetent", R.drawable.incompetent);

		getImage.put("winter", R.drawable.winter);

		getImage.put("hospital", R.drawable.hospital);

		getImage.put("beginning", R.drawable.beginning);

		getImage.put("cat", R.drawable.cat);

		getImage.put("Japanese", R.drawable.japanese);

		getImage.put("first, given name", R.drawable.name);

		getImage.put("what", R.drawable.what);

		getImage.put("summer", R.drawable.summer);

		getImage.put("long", R.drawable.long_);

		getImage.put("where", R.drawable.where);

		getImage.put("please (offering)", R.drawable.pleaseoffering);

		getImage.put("bird", R.drawable.bird);

		getImage.put("friend", R.drawable.friend);

		getImage.put("next to", R.drawable.nextto);

		getImage.put("year", R.drawable.year);

		getImage.put("Tokyo", R.drawable.tokyo);

		getImage.put("assist", R.drawable.assist);

		getImage.put("hand", R.drawable.hand);

		getImage.put("tired", R.drawable.tired);

		getImage.put("a little", R.drawable.alittle);

		getImage.put("like very much", R.drawable.likeverymuch);

		getImage.put("food", R.drawable.food);

		getImage.put("fun", R.drawable.fun);

		getImage.put("IÕm back", R.drawable.imback);

		getImage.put("provide critical assistance",
				R.drawable.providecriticalassistance);

		getImage.put("teach", R.drawable.teacher);

		getImage.put("life", R.drawable.life);

		getImage.put("adult", R.drawable.adult);

		getImage.put("sorry", R.drawable.sorry);

		getImage.put("bold, daring", R.drawable.airport);
		getImage.put("skillful", R.drawable.skillful);

		getImage.put("time", R.drawable.time);

		getImage.put("white", R.drawable.white);

		getImage.put("book", R.drawable.book);

		getImage.put("hometown", R.drawable.hometown);

		getImage.put("question", R.drawable.question);

		getImage.put("cold", R.drawable.cold);

		getImage.put("good evening", R.drawable.goodevening);

		getImage.put("good afternoon", R.drawable.goodafternoon);

		getImage.put("spring", R.drawable.spring);

		getImage.put("this", R.drawable.this_);

		getImage.put("thing", R.drawable.thing);

		getImage.put("this one, this way", R.drawable.thisonethisway);

		getImage.put("highway", R.drawable.highway);

		getImage.put("English", R.drawable.english);

		getImage.put("fine, healthy", R.drawable.healthyfine);

		getImage.put("police", R.drawable.police);

		getImage.put("black", R.drawable.black);

		getImage.put("cloud", R.drawable.cloud);

		getImage.put("socks", R.drawable.socks);

		getImage.put("shoes, boots", R.drawable.shoesboots);

		getImage.put("please (a favor)", R.drawable.pleasefavor);

		getImage.put("airport", R.drawable.airport);

		getImage.put("today", R.drawable.today);

		getImage.put("dangerous", R.drawable.dangerous);

		getImage.put("yellow", R.drawable.yellow);

		getImage.put("feeling", R.drawable.feeling);

		getImage.put("tree", R.drawable.tree);

		getImage.put("student", R.drawable.student);

		getImage.put("cool, stylish", R.drawable.cool);

		getImage.put("write", R.drawable.write);

		getImage.put("office worker", R.drawable.officeworker);

		getImage.put("idea", R.drawable.idea);

		getImage.put("hungry", R.drawable.hunfry);

		getImage.put("tea", R.drawable.tea);

		getImage.put("welcome (home)", R.drawable.welcomehome);

		getImage.put("yen", R.drawable.yen);

		getImage.put("railway station", R.drawable.railwaystation);

		getImage.put("movies", R.drawable.movies);

		getImage.put("horse", R.drawable.horse);

		getImage.put("clothes", R.drawable.clothes);

		getImage.put("dog", R.drawable.dog);

		getImage.put("someday", R.drawable.someday);

		getImage.put("when", R.drawable.when);

		getImage.put("one, two, three", R.drawable.onetwothree);

		getImage.put("bon appetit", R.drawable.bonappetit);

		getImage.put("do", R.drawable.do_);

		getImage.put("thanks", R.drawable.thanks);

		getImage.put("hot", R.drawable.hot);

		getImage.put("tomorrow", R.drawable.tomorrow);

		getImage.put("autumn", R.drawable.autumn);

		getImage.put("red", R.drawable.red);

		getImage.put("blue", R.drawable.blue);

		getImage.put("seven, eight, nine, ten",
				R.drawable.seveneightnineten);

		getImage.put("hat", R.drawable.hat);

		getImage.put("congratulations", R.drawable.congratulations);

	}
}
