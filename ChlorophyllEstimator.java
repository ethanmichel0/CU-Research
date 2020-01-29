// Ethan Michel
// 1/15/2019
// ChlorophyllEstimator.Java

// The purpose of this class is to estimate the chlorophyll content of a sample of algae given a photo to determine the right time to harvest

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.IllegalFormatException;



@SuppressWarnings("unused")
public class ChlorophyllEstimator {
	
	private String fileName;
	private File imageFile;
	// allow us to open the image
	
	private int averageRed = 0;
	private int averageGreen = 0;
	private int averageBlue = 0;
	
	// rgb values calculated as averages across entire photo as with constant lighting, the values should be constant
	
	private double redRatio = 0;
	private double greenRatio = 0;
	private double blueRatio = 0;
	
	private int sumAverageColors = 0;
	
	float hue = 0f;
	
	// these four variables used to calculate what % of the image is r, g, and b
	
	private BufferedImage image;
	
	public ChlorophyllEstimator (String input) throws IOException{ // constructor should take file name as a path
		// the IOException is caught in the main method if you look there
		
		this.fileName = input;
		
		// rgb values calculated as averages across entire photo
		this.imageFile = new File(input); // create a new file based on input text
		this.image = ImageIO.read(imageFile); // Return a new image from the javaIO class
		
		
	}
	
	public boolean calculateRGBValues() { // function to loop through image and calculate average Red Green and Blue values
		// This function should be called before calling getRed() getGreen() or getBlue()
		
		try {
			int totalRed = 0;
			int totalGreen = 0;
			int totalBlue = 0;
			int totalNumPixels = this.image.getHeight() * this.image.getWidth();		
			
			// loop through each pixel and add that pixels RGB values to calculate average values eventually
			
			
			for (int x = 0; x < this.image.getWidth(); x++) {
				for (int y = 0; y < this.image.getHeight(); y++) {
					
					int clr = image.getRGB(x, y); 
					
					// taken from  https://stackoverflow.com/questions/7427141/how-to-get-rgb-value-from-hexadecimal-color-code-in-java
					totalRed += (clr & 0x00ff0000) >> 16;
		          	totalGreen += (clr & 0x0000ff00) >> 8;
		          	totalBlue +=  clr & 0x000000ff;
				}
				
			} // loop through each pixel and add that pixels RGB values to calculate average values eventually
			this.averageRed = totalRed / totalNumPixels;
			this.averageGreen = totalGreen / totalNumPixels;
			this.averageBlue = totalBlue / totalNumPixels;
			
			this.sumAverageColors = this.averageRed + this.averageGreen + this.averageBlue;
			
			this.redRatio = (double) this.averageRed / this.sumAverageColors;
			this.greenRatio = (double) this.averageGreen / this.sumAverageColors;
			this.blueRatio = (double) this.averageBlue / this.sumAverageColors;
			
			return true;
		}
		
		catch (Exception e) {
			e.printStackTrace();
			return false; // exit early
		}
	}
	
	public int getRed () {
		return this.averageRed;
	}
	public int getGreen () {
		return this.averageGreen;
	}
	public int getBlue () {
		return this.averageBlue;
	}
	public double getRedRatio() {
		return this.redRatio;
	}
	public double getGreenRatio() {
		return this.greenRatio;
	}
	public double getBlueRatio() {
		return this.blueRatio;
	}
	
	public int getHue() { // code from https://stackoverflow.com/questions/23090019/fastest-formula-to-get-hue-from-rgb
		
	    float min = Math.min(Math.min(averageRed, averageGreen), averageBlue);
	    float max = Math.max(Math.max(averageRed, averageGreen), averageBlue);

	    if (min == max) {
	        return 0;
	    }
	    
	    if (max == averageRed) {
	        this.hue = (averageGreen - averageBlue) / (max - min);

	    } else if (max == averageGreen) {
	        this.hue = 2f + (averageBlue - averageRed) / (max - min);

	    } else {
	        this.hue = 4f + (averageRed - averageGreen) / (max - min);
	    }

	    this.hue = this.hue * 60;
	    if (hue < 0) this.hue = this.hue + 360;

	    return Math.round(hue);
	}
	
	public String getFileName () {
		return this.fileName;
	}

	
	public static void main (String[] args) throws IOException {
		if(args.length < 1) {
			System.out.println("usage: ChlorophyllEstimator.java filename filename2 ...");
			System.out.println("Please only at least one argument where each argument is the filename of the picture to be analyzed");
			return; // exit early if only one argument provided
		}
		
		for (int arg = 0; arg < args.length; arg++) { 
			
			// for each file, a new object is created to find a new estimate. The purpose of the try/catch block is to 
			// catch errors mainly with invalid files
			
			try {
				ChlorophyllEstimator newEstimate = new ChlorophyllEstimator(args[arg]);
				
				newEstimate.calculateRGBValues();
				String fileName = newEstimate.getFileName();
				
				int averageRed = newEstimate.getRed();
				int averageGreen = newEstimate.getGreen();
				int averageBlue = newEstimate.getBlue();
				
				int hue = newEstimate.getHue();
				
				double redRatio = newEstimate.getRedRatio();
				double greenRatio = newEstimate.getGreenRatio();
				double blueRatio = newEstimate.getBlueRatio();
				
				
				// idea taken from https://howtodoinjava.com/java/io/java-append-to-file/
				// https://www.baeldung.com/java-write-to-file
				
				
				FileWriter fileWriter = new FileWriter("output.txt", true); // change this line ("output.txt") to change the file to be written to 
				PrintWriter printWriter = new PrintWriter(fileWriter);
				printWriter.print("Filename: " + fileName + "\n");
				printWriter.printf("%15s %3d %15s %3d %15s %3d %15s %4f %15s %4f %15s %4f %s %d \n \n \n", "red value", averageRed, "green value", averageGreen,
						"blue value", averageBlue, "red ratio", redRatio, "green ratio", greenRatio, "blue ratio", blueRatio, "hue:", hue);
				
				
				// this line will (hopefully) write all of the important data to a file as a formatted string
				
				//printWriter.printf("%3d %3d %3d %4f %4f %4f %d \n \n \n",averageRed,averageGreen,averageBlue,redRatio,greenRatio,blueRatio,hue);
				
				// this line only adds data, not text. Make sure to comment out one of the printWriter.printf() lines to avoid redundancy
				
				
				printWriter.close();
				fileWriter.close();
				
				
				
				
				// all of these lines commented out are used for printing, but if we are appending data to a file they are not needed
				
				//System.out.print("r value " + averageRed + " ");
				//System.out.print("g value " + averageGreen + " ");
				//System.out.print("b value " + averageBlue + " ");
				
				//System.out.print("r ratio " + redRatio + " ");
				//System.out.print("g ratio " + greenRatio + " ");
				//System.out.print("b ratio " + blueRatio + " ");
				
				
			} 
			catch (IOException ioe) { // invalid file
				FileWriter fileWriter = new FileWriter("output.txt", true); // change this line ("output.txt") to change the file to be written to 
				PrintWriter printWriter = new PrintWriter(fileWriter);
				printWriter.print("File \""+ args[arg] + "\" is invalid. \n \n \n");
				
				// ioe.printStackTrace(); // uncomment this line for more debugging
				
				printWriter.close();
				fileWriter.close();
			}
		}
	}
	
}
