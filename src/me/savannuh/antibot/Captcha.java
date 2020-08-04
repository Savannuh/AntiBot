package me.savannuh.antibot;

public class Captcha {
	
	private String word;
	private int attempts;
	
	public Captcha(String word) {
		this.word = word;
		this.attempts = 3;
	}
	
	public String getWord() {
		return this.word;
	}
	
	public int getAttempts() {
		return this.attempts;
	}
	
	public void minusAttempt() {
		this.attempts--;
	}

}
