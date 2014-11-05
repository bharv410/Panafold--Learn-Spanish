package com.panafold.main.datamodel;

public class ReviewWord {
String timestamp,engWord;
 
Integer review;

public ReviewWord(String eng, Integer rev, String ts){
	this.engWord=eng;
	this.review=rev;
	this.timestamp=ts;
}

public String getEnglish() {
	return this.engWord;
}

public Integer getReview() {
	return this.review;
}

public String getTimeStamp() {
	return this.timestamp;
}
}
