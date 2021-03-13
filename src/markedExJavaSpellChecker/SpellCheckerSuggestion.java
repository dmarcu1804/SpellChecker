package markedExJavaSpellChecker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class SpellCheckerSuggestion {
	//for StringTokenizer
		public static String delim = " ,:?!/[].;\t\n";
		//path - will have to be changed by tester
		public static String path = "/Users/dragos/Desktop/spellChecker/";
		//public static TreeSet<String> editedDictionary = new TreeSet<String>();
		//empty String variable
		public static String document = "";
		//treeset to automatically order words
		public static TreeSet<String> lines = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		//public arraylist to store the dictionary
		public static ArrayList<String> dictionary;
		//public arraylist which 
		public static ArrayList<String> suggestions;

		
		
		//method to read dictionary - parameter for user input
		public static ArrayList<String> readDictionary(String name){
			//initialise an array list to hold the dictionary
			dictionary = new ArrayList<String>();
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
				//if text has next line
				while(readText.hasNextLine()) {
					String line = readText.nextLine();
					//separating the words
					StringTokenizer st = new StringTokenizer(line, delim);
					//adding it to the list
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
	            
	            // iterating through treeset and adding to the new file; sorted dictionary is printHere
	            
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
		
		//levenshtein algorithm
		
		public static int distance(String a, String b) {
			//changing the parameters to lower case
	        a = a.toLowerCase();
	        b = b.toLowerCase();
	        //initialising array to be the size of the string + 1
	        int[] cost = new int [b.length() + 1];
//	        for (int j = 0; j < costs.length; j++)
//	            costs[j] = j;
	        int temp = 0;
	        //iterating through the length of the array
	        while(temp < cost.length) {
	        	cost[temp] = temp;
	        	temp++;
	        }
	        //comparing the 2 strings and returning the amount of differences between them
	        //
	        for (int i = 1; i <= a.length(); i++) {
	            cost[0] = i;
	            int newNo = i - 1;
	            for (int j = 1; j <= b.length(); j++) {
	                int cj = Math.min(1 + Math.min(cost[j], cost[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? newNo : newNo + 1);
	                newNo = cost[j];
	                cost[j] = cj;
	            }
	        }
	        //returning the distance
	        return cost[b.length()];
	    }
		
		//getting distance using the levenshtein algorithm
		private static ArrayList<String> getDistance(String word){
			//initialise arraylist
			suggestions = new ArrayList<String>();
			//Scanner read = new Scanner(new File(path + "dictionary.txt"));
			//iterating through the dictionary and if the distance between the word and the words in the dictionary is one or less
			//add it to the suggestions array list and return it
			for(String dictWord : dictionary) {
				if(distance(word,dictWord)<=1) {
					suggestions.add(dictWord);
				}
			}
			return suggestions;
		}
		
		
		//method to ask for user input; 1 to add to dictionary or 2 to replace the word
		private static void request(String word) throws IOException{
			System.out.println("The following word is not in the dictionary: " + word);
			System.out.println("If you want to add to dictionary press 1 ");
			System.out.println("If you want to replace it manually press 2");
			System.out.println("If you want a suggestion press 3");
			Scanner scan = new Scanner(System.in);
			String req = scan.next();
			//if user input is 1, then the word will be added to the dictionary 
			if(req.equals("1")) {
				addToDictionary(word);
				//System.out.println("Do this");
			}
			else if(req.equals("2")) {
				//if user input is 2, then the word in the text file(paragraph) will be changed to the user's input
				System.out.println("Type the replacement word");
				Scanner scann = new Scanner(System.in);
				String works = scann.next();
//				document = document.replaceAll("(?i)\\b"+word+"\\b", works);
//				setting a path to the paragraph file
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
			else if(req.equals("3")) {
//				Scanner scannn = new Scanner(System.in);
//				String works1 = scannn.next();
				ArrayList<String> suggestions = getDistance(word);
				//will print the arraylist with suggestions
				System.out.println(suggestions);
				//ask for user inout
				System.out.println("Did you mean one of these words? y/n ");
				Scanner inpinp = new Scanner(System.in);
				//if user entered y then
				if(inpinp.next().toLowerCase().equals("y")) {
					int i = 0;
					//iterate through the suggestions and print then and a number
					for(i = 0; i < suggestions.size(); i++)
						System.out.println(i + ", " + suggestions.get(i));
					System.out.println("Enter the replacement number of the word");
					//the user will enter the name with the correct replacement 
					Scanner userInp = new Scanner(System.in);
					int number = userInp.nextInt();
					
					//replace word
					//replacement word is new_word
					String new_word = suggestions.get(number);
					Path path2 = Paths.get(path + "mydictionary.txt");
					//sets the character format to UTF_8
					Charset charset = StandardCharsets.UTF_8;
					
					//reading the file at path2
					String document = new String(Files.readAllBytes(path2), charset);
					//replace the word that is not in the dictionary with user input 
					document = document.replaceAll(word, new_word);
					//writes it to the document
					Files.write(path2, document.getBytes(charset));
				}
				else {
					System.out.println("Skipped to next word");
				}
				
			}
			else {
				System.out.println("Goodbye!");
				
			}
		}
		
		public static void main(String[] args) throws FileNotFoundException {
			//user input for readDictionary
			System.out.println("Enter name of dictionary");
			Scanner scan0 = new Scanner(System.in);
			String home = scan0.next();
			
			readDictionary(home);
			Scanner scan = new Scanner(System.in);
			Scanner scan1 = new Scanner(System.in);
			System.out.println("Enter the comparsion file");
			String input = scan.next();
			

			compare(readText(input),readDictionary(home));
			
						
		}	
}
