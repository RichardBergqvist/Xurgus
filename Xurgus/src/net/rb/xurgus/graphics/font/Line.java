package net.rb.xurgus.graphics.font;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class Line {

	private double spaceSize;
	private double maxLength;
	
	private double currentLineLength = 0;
	private List<Word> words = new ArrayList<Word>();
	
	public Line(double spaceWidth, double fontSize, double maxLength) {
		this.spaceSize = spaceWidth * fontSize;
		this.maxLength = maxLength;
	}
	
	public boolean attemptToAddWord(Word word) {
		double additionalLength = word.getWidth();
		additionalLength += !words.isEmpty() ? spaceSize : 0;
		if (currentLineLength + additionalLength <= maxLength) {
			words.add(word);
			currentLineLength += additionalLength;
			return true;
		} else
			return false;
	}
	
	public double getMaxLength() {
		return maxLength;
	}
	
	public double getLineLength() {
		return currentLineLength;
	}
	
	public List<Word> getWords() {
		return words;
	}
}
