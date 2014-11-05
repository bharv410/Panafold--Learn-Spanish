package com.panafold.main.datamodel;

public class Word {
	String english, lang2,spanPhrase, englPhrase,imageCredit;

	public Word(String span,String eng,String attr, String spPhrase,String engPhrase, String img) {
		this.english = eng;
		this.lang2=span;
		this.imageCredit=img;
		this.spanPhrase=spPhrase;
		this.englPhrase = engPhrase;
		
	}

	public String getEnglish() {
		return this.english;
	}
	public String getCred() {
		return this.imageCredit;
	}

	public String getLang2() {
		return this.lang2;
	}

	public String getLang2Phrase() {
		return this.spanPhrase;
	}

	public String getEnglPhrase() {
		return this.englPhrase;
	}
}
