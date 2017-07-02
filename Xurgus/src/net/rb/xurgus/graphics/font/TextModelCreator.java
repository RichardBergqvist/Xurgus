package net.rb.xurgus.graphics.font;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class TextModelCreator {

	protected static final double LINE_HEIGHT = 0.03F;
	protected static final int SPACE_ASCII = 32;
	
	private MetaFile metadata;
	
	public TextModelCreator(String fileName) {
		metadata = new MetaFile(fileName);
	}
	
	public TextModelData createTextModel(GuiText text) {
		List<Line> lines = createStructure(text);
		TextModelData data = createQuadVertices(text, lines);
		return data;
	}
	
	private List<Line> createStructure(GuiText text) {
		char[] chars = text.getText().toCharArray();
		List<Line> lines = new ArrayList<Line>();
		Line currentLine = new Line(metadata.getSpaceWidth(), text.getFontSize(), text.getMaxLineLength());
		Word currentWord = new Word(text.getFontSize());
		
		for (char c : chars) {
			int ascii = (int) c;
			if (ascii == SPACE_ASCII) {
				boolean added = currentLine.attemptToAddWord(currentWord);
				if (!added) {
					lines.add(currentLine);
					currentLine = new Line(metadata.getSpaceWidth(), text.getFontSize(), text.getMaxLineLength());
					currentLine.attemptToAddWord(currentWord);
				}
				
				currentWord = new Word(text.getFontSize());
				continue;
			}
			
			Character character = metadata.getCharacter(ascii);
			currentWord.addCharacter(character);
		}
		
		completeStructure(lines, currentLine, currentWord, text);
		return lines;
	}
	
	private void completeStructure(List<Line> lines, Line currentLine, Word currentWord, GuiText text) {
		boolean added = currentLine.attemptToAddWord(currentWord);
		if (!added) {
			lines.add(currentLine);
			currentLine = new Line(metadata.getSpaceWidth(), text.getFontSize(), text.getMaxLineLength());
			currentLine.attemptToAddWord(currentWord);
		}
		
		lines.add(currentLine);
	}
	
	private TextModelData createQuadVertices(GuiText text, List<Line> lines) {
		text.setNumberOfLines(lines.size());
		double cursorX = 0;
		double cursorY = 0;
		List<Float> vertices = new ArrayList<Float>();
		List<Float> textureCoordinates = new ArrayList<Float>();
		for (Line line : lines) {
			if (text.isCentered())
				cursorX = (line.getMaxLength() - line.getLineLength()) / 2;
			
			for (Word word : line.getWords()) {
				for (Character letter : word.getCharacters()) {
					addVerticesForCharacter(cursorX, cursorY, letter, text.getFontSize(), vertices);
					addTextureCoordinates(textureCoordinates, letter.getXTextureCoordinate(), letter.getYTextureCoordinate(), letter.getMaxXTextureCoordinate(), letter.getMaxYTextureCoordinate());
					cursorX += letter.getXAdvance() * text.getFontSize();
				}
				
				cursorX += metadata.getSpaceWidth() * text.getFontSize();
			}
			
			cursorX = 0;
			cursorY = LINE_HEIGHT * text.getFontSize();
		}
		
		return new TextModelData(listToArray(vertices), listToArray(textureCoordinates));
	}
	
	private void addVerticesForCharacter(double curserX, double curserY, Character character, double fontSize,
            List<Float> vertices) {
        double x = curserX + (character.getXOffset() * fontSize);
        double y = curserY + (character.getYOffset() * fontSize);
        double maxX = x + (character.getXSize() * fontSize);
        double maxY = y + (character.getYSize() * fontSize);
        double properX = (2 * x) - 1;
        double properY = (-2 * y) + 1;
        double properMaxX = (2 * maxX) - 1;
        double properMaxY = (-2 * maxY) + 1;
        addVertices(vertices, properX, properY, properMaxX, properMaxY);
    }
	
	private static void addVertices(List<Float> vertices, double x, double y, double maxX, double maxY) {
        vertices.add((float) x);
        vertices.add((float) y);
        vertices.add((float) x);
        vertices.add((float) maxY);
        vertices.add((float) maxX);
        vertices.add((float) maxY);
        vertices.add((float) maxX);
        vertices.add((float) maxY);
        vertices.add((float) maxX);
        vertices.add((float) y);
        vertices.add((float) x);
        vertices.add((float) y);
    }
 
    private static void addTextureCoordinates(List<Float> textureCoordinates, double x, double y, double maxX, double maxY) {
    	textureCoordinates.add((float) x);
    	textureCoordinates.add((float) y);
    	textureCoordinates.add((float) x);
    	textureCoordinates.add((float) maxY);
    	textureCoordinates.add((float) maxX);
    	textureCoordinates.add((float) maxY);
    	textureCoordinates.add((float) maxX);
    	textureCoordinates.add((float) maxY);
    	textureCoordinates.add((float) maxX);
    	textureCoordinates.add((float) y);
    	textureCoordinates.add((float) x);
    	textureCoordinates.add((float) y);
    }
 
     
    private static float[] listToArray(List<Float> listOfFloats) {
        float[] array = new float[listOfFloats.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = listOfFloats.get(i);
        }
        return array;
    }
}
