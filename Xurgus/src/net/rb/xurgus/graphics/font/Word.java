package net.rb.xurgus.graphics.font;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class Word {

	private List<Character> characters = new ArrayList<Character>();
	private double width = 0;
	private double fontSize;
	
	public Word(double fontSize) {
		this.fontSize = fontSize;
	}
	
	public void addCharacter(Character character) {
		characters.add(character);
		width += character.getXAdvance() * fontSize;
	}
	
	public List<Character> getCharacters() {
		return characters;
	}
	
	public double getWidth() {
		return width;
	}
}
