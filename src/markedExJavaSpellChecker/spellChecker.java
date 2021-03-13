package markedExJavaSpellChecker;
import java.util.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class spellChecker {
	//for StringTokenizer
	public static String delim = " ,:?!/[].;\t\n";
	//path - will have to be changed by tester
	public static String path = "/Users/dragos/Desktop/spellChecker/";
	//public static TreeSet<String> editedDictionary = new TreeSet<String>();
	//empty String variable
	public static String document = "";
	//treeset to automatically order words
	public static TreeSet<String> lines = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

	//method to read dictionary - parameter for user input
	public static ArrayList<String> readDictionary(String name){
		//initialise an array list to hold the dictionary
		ArrayList<String> dictionary = new ArrayList<String>();
		//iterate through dictionary and add them to the arraylist
		try {
			Scanner read = new Scanner(new File(path + name));
			while(read.hasNext()) {
				dictionary.add(read.next());
			}
		//throw exception if file not found
		}catch (Exception e){
			System.out.println("Couldn't read file or cannot be found!");
		}
		return dictionary;
	}
	
	//method to read the other external file - parameter for user input
	public static HashSet<String> readText(String input){
		//initialise a hashset as order doesn't matter
		HashSet<String> text = new HashSet<String>();
		
		//iterate through the file and add to the hashset - StringTokenizer from lecture slides
		try {
			Scanner readText = new Scanner(new File(path + input));
			
			while(readText.hasNextLine()) {
				String line = readText.nextLine();
				StringTokenizer st = new StringTokenizer(line, delim);
				while(st.hasMoreTokens()) {
					text.add(st.nextToken().toLowerCase());
				}
			}
			//throw exception if file not found
		}catch (Exception e) {
			System.out.println("Couldn't read other file");
			
		}
		return text;
	}
	
		//method to add the word to the dictionary
	public static void addToDictionary(String word) {
		try {
			//creating a bufferedwriter for the dictionary
			String write_name = path + "dictionary.txt";
			FileWriter writeFile = new FileWriter(write_name,true);
			BufferedWriter writeThis = new BufferedWriter(writeFile);
			//writes the word to the dictionary
			writeThis.write(word);
			writeThis.newLine();
			writeThis.close();
			//make a bufferedreader to read the dictionary with new words
			BufferedReader reader = new BufferedReader(new FileReader(path + "dictionary.txt"));
		
            //Read lines of input file one by one and adding them into lines
            
            String currentLine = reader.readLine();
            //add items to treeset
            while (currentLine != null) 
            {
                lines.add(currentLine);
                
                currentLine = reader.readLine();
            }
            
         
            
            //Creating BufferedWriter object to write into separate output file
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(path + "printHere.txt"));
            
            // iterating through treeset and adding to the new file
            
            for (String line : lines)
            {
                writer.write(line);
                
                writer.newLine();
            }
		}catch (Exception e) {
			System.out.println("Could not write to file as it doesn't exist");
		}
	}
	//method to compare the text file and the dictionary 
	public static void compare(HashSet<String> text, ArrayList<String> dictionary) {
		//initialise an array list to store output
		ArrayList<String> output = new ArrayList<String>(text);
		
		int temp = 0;
		try {
			//iterate through the arraylist and if the dictionary does not contain that word, it'll call the request function
			while(temp < output.size()) {
				if(!dictionary.contains(output.get(temp))) {
					request(output.get(temp));
					//addToDictionary(output.get(temp));
				}
				temp++;
			}
		}catch (Exception e) {
			System.out.println("Could not compare");
		}
	}
	//method to ask for user input; 1 to add to dictionary or 2 to replace the word
	private static void request(String word) throws IOException{
		System.out.println("The following word is not in the dictionary: " + word);
		System.out.println("If you want to add to dictionary press 1 ");
		System.out.println("If you want to replace it manually press 2");
		Scanner scan = new Scanner(System.in);
		String req = scan.next();
		//if user input is 1, then the word will be added to the dictionary 
		if(req.equals("1")) {
			addToDictionary(word);
			//System.out.println("Do this");
		}
		else if(req.equals("2")) {
			//if user input is 2, then the word in the text file(paragraph) will be changed to the user's input
			Scanner scann = new Scanner(System.in);
			String works = scann.next();
//			document = document.replaceAll("(?i)\\b"+word+"\\b", works);
//			setting a path to the paragraph file
			Path path1 = Paths.get(path + "mydictionary.txt");
			//sets the character format to UTF_8
			Charset charset = StandardCharsets.UTF_8;
			
			//reading the file at path1
			String document = new String(Files.readAllBytes(path1), charset);
			//replace the word that is not in the dictionary with user input 
			document = document.replaceAll(word, works);
			//writes it to the document
			Files.write(path1, document.getBytes(charset));
			
			//System.out.println("Do that");
		}
		else {
			System.out.println("Goodbye!");
			
		}
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Comparison file first: ");
		String input = scan.next();
		
		Scanner scan1 = new Scanner(System.in);
		System.out.println("Enter dictionary: ");
		String out = scan1.next();
		
		compare(readText(input),readDictionary(out));
		
	}

}
