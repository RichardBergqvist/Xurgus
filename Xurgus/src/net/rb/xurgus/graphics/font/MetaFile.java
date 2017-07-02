package net.rb.xurgus.graphics.font;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.Display;

/**
 * 
 * @author Richard Bergqvist
 *
 */
public class MetaFile {

	private static final int PAD_TOP = 0;
	private static final int PAD_LEFT = 1;
	private static final int PAD_BOTTOM = 2;
	private static final int PAD_RIGHT = 3;
	
	private static final int DESIRED_PADDING = 8;
	
	private static final String SPLITTER = " ";
	private static final String NUMBER_SEPARATOR = ",";
	
	private double aspectRatio;
	
	private double verticalPixelSize;
	private double horizontalPixelSize;
	private double spaceWidth;
	private int[] padding;
	private int paddingWidth;
	private int paddingHeight;
	
	private Map<Integer, Character> metadata = new HashMap<Integer, Character>();
	
	private BufferedReader reader;
	private Map<String, String> values = new HashMap<String, String>();
	
	public MetaFile(String fileName) {
		this.aspectRatio = (double) Display.getWidth() / (double) Display.getHeight();
		openFile(fileName);
		loadPaddingData();
		loadLineSizes();
		int imageWidth = getValueOfVariable("scaleW");
		loadCharacterData(imageWidth);
		close();
	}
	
	private void openFile(String fileName) {
		File file = new File("res/fonts/" + fileName + ".fnt");
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Could not find font file: " + file + ".fnt");
			System.exit(-1);
		}
	}
	
	private void loadPaddingData() {
		processNextLine();
		this.padding = getValuesOfVariable("padding");
		this.paddingWidth = padding[PAD_LEFT] + padding[PAD_RIGHT];
		this.paddingHeight = padding[PAD_BOTTOM] + padding[PAD_TOP];
	}
	
	private void loadLineSizes() {
		processNextLine();
		int lineHeightInPixels = getValueOfVariable("lineHeight") - paddingHeight;
		verticalPixelSize = TextModelCreator.LINE_HEIGHT / (double) lineHeightInPixels;
		horizontalPixelSize = verticalPixelSize / aspectRatio;
	}
	
	private void loadCharacterData(int imageWidth) {
		processNextLine();
		processNextLine();
		while (processNextLine()) {
			Character c = loadCharacter(imageWidth);
			if (c != null)
				metadata.put(c.getId(), c);
		}
	}
	
	private boolean processNextLine() {
		values.clear();
		String line = null;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		if (line == null)
			return false;
		
		for (String part : line.split(SPLITTER)) {
			String[] valuePairs = part.split("=");
			if (valuePairs.length == 2)
				values.put(valuePairs[0], valuePairs[1]);
		}
		
		return true;
	}
	
	private Character loadCharacter(int imageSize) {
		int id = getValueOfVariable("id");
		if (id == TextModelCreator.SPACE_ASCII) {
			this.spaceWidth = (getValueOfVariable("xadvance") - paddingWidth) * horizontalPixelSize;
			return null;
		}
		
		double xTexture = ((double) getValueOfVariable("x") + (padding[PAD_LEFT] - DESIRED_PADDING)) / imageSize;
		double yTexture = ((double) getValueOfVariable("y") + (padding[PAD_TOP] - DESIRED_PADDING)) / imageSize;
		int width = getValueOfVariable("width") - (paddingWidth - (2 * DESIRED_PADDING));
		int height = getValueOfVariable("height") - ((paddingHeight) - (2 * DESIRED_PADDING));
		double quadWidth = width * horizontalPixelSize;
		double quadHeight = height * verticalPixelSize;
		double xTextureSize = (double) width / imageSize;
		double yTextureSize = (double) height / imageSize;
		double xOffset = (getValueOfVariable("xoffset") + padding[PAD_LEFT] - DESIRED_PADDING) * horizontalPixelSize;
		double yOffset = (getValueOfVariable("yoffset") + (padding[PAD_TOP] - DESIRED_PADDING)) * verticalPixelSize;
		double xAdvance = (getValueOfVariable("xadvance") - paddingWidth) * horizontalPixelSize;
		return new Character(id, xTexture, yTexture, xTextureSize, yTextureSize, xOffset, yOffset, quadWidth, quadHeight, xAdvance);
	}
	
	private void close() {
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private int getValueOfVariable(String variable) {
		return Integer.parseInt(values.get(variable));
	}
	
	private int[] getValuesOfVariable(String variable) {
		String[] numbers = values.get(variable).split(NUMBER_SEPARATOR);
		int[] actualValues = new int[numbers.length];
		for (int i = 0; i < actualValues.length; i++)
			actualValues[i] = Integer.parseInt(numbers[i]);
		return actualValues;
	}
	
	public double getSpaceWidth() {
		return spaceWidth;
	}
	
	public Character getCharacter(int index) {
		return metadata.get(index);
	}
}