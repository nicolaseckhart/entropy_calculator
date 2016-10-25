import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class EntropyCalculator1 {

	// Key is of type Integer (we will use character codes as key).
	// Content of hash-map is of type CharProp.
	// Name of hash-map is "chars" (characters).
	static HashMap <Integer, CharProp> chars = new HashMap<>();
	
	static double fileCharactersCount = 0;
	static double fileEntropy = 0;
	
	public static void main(String[] args) {
		
		System.out.println("======================================================");
		System.out.println("Starting ComputeMain...");
		
		// Check if a valid filename has been supplied on command line:
		if (args.length <= 0) {
			System.out.println("ERROR: You have to supply a filename on the command line!");
			System.out.println(" ");
			System.exit(0);
		}
		
		String s = args[0];
		File file = new File(s);
		
		if (!file.exists()) {
			// Quit program with an error message:
			System.out.println("ERROR: Data file: " + s + " does not exist!");
			System.out.println(" ");
			System.exit(0);
		}
		
		System.out.println( "Data file " + s + " exists.");
		
		ReadInputTextFileCharacters(s);
		ComputeProbabilities();
		ComputeInformation();
		fileEntropy = ComputeEntropy();
		PrintOutCharProps();
		
		System.out.println("Done.");
		System.out.println("======================================================");
	}
	
	
	// Character property class:
	static class CharProp {
		int    occurence = 0;
		double probability = 0;
		double information = 0;
	}
	
	
	// Base 2 logarithm:
	static double log2(double d) {
		return Math.log(d)/Math.log(2.0);
	}
	

	// Read character from file and count them:
	static void ReadInputTextFileCharacters(String relativeFilePath) {

    	System.out.println("Reading file...");
    	
		try (BufferedReader in = new BufferedReader(new FileReader(relativeFilePath))) {
			int c;
			while ((c = in.read()) != -1) {
				fileCharactersCount++;
				if(chars.containsKey(c)) {
					chars.get(c).occurence++;
				} else {
					CharProp charProp = new CharProp();
					charProp.occurence = 1;
					chars.put(c, charProp);
				}	
			}
		} 
		catch(IOException ioe) {}
	}
	
	
	// Compute probability of each character in hash-map:
	static void ComputeProbabilities() {
		
		System.out.println( "Computing probabilities...");
		
		for ( int c : chars.keySet() ) {
			CharProp charProp = chars.get(c);
			charProp.probability = charProp.occurence / fileCharactersCount;
		}
		
	}

	
	// Compute information for each character in hash-map:
	static void ComputeInformation() {
		
		System.out.println( "Computing information...");
		
		for ( int c : chars.keySet() ) {
			CharProp charProp = chars.get(c);
			charProp.information = log2(1.0/(charProp.probability));
		}
	}

	
	// Compute entropy of all characters in hash-map:
	static double ComputeEntropy() {
		
		System.out.println( "Computing entropy...");
		double sum = 0;
		
		for ( int c : chars.keySet() ) {
			CharProp charProp = chars.get(c);
			sum += charProp.probability * charProp.information; 
		}
		
		return sum;
	}
	
	// Print result table with occurence, probability and information:
	static void PrintOutCharProps() {
	
		// Print general statistics
		System.out.println("Character types in file: " + chars.size());
		System.out.println("Number of character in file: " + fileCharactersCount);
		System.out.println("Entropy of file: " + fileEntropy);
		System.out.println(" ");
		
		// Print character statistics:
		String chr = "";
		for ( int c : chars.keySet() ) {
			
			if ( Character.isWhitespace(c) ) {
				chr = "(" + c + ")";
			} else {
				chr = "" + (char) c;
			}
			System.out.format("  %5s : o=%8d  p=%1.10f  i=%-2.10f%n", 
				chr, chars.get(c).occurence, chars.get(c).probability, chars.get(c).information);
		}
	}
}
