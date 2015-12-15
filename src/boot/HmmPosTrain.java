package boot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class HmmPosTrain {
	LinkedHashMap<String, Integer> tagToTag;
	LinkedHashMap<String, Integer> tagToTWord;
	LinkedHashMap<String, Integer> tagAndCount;
	LinkedHashMap<String, Integer> wordAndCount;
	HashMap<String, Integer> tagInFirstSent;
	HashMap<Tuple<String, String>,Integer > s1 = null;
	HashMap<Tuple<String, String>,Integer > s2 = null;
	float numOfSentences=0;

	public static void main(String[] args) {
      new HmmPosTrain("oct27.train");
	}
	
    public HmmPosTrain(String inputCorpusFile){
		try {
			Scanner input=new Scanner(new BufferedReader(new FileReader(inputCorpusFile)));
//			input.useDelimiter("[\r\n]");
			tagToTag=new LinkedHashMap<String, Integer>();
			tagToTWord=new LinkedHashMap<String, Integer>();
			tagAndCount=new LinkedHashMap<String, Integer>();
			wordAndCount=new LinkedHashMap<String, Integer>();
            tagInFirstSent=new HashMap<String, Integer>();
//			s1=new HashMap<Tuple<String,String>, Integer>();
//			s2=new HashMap<Tuple<String,String>, Integer>();
			String line1=null,line2=null;
			while(input.hasNextLine()){
				line1=input.nextLine();
			addLineToMap(line1.toLowerCase());
			addToMap(tagInFirstSent, line1.split("\t")[1].toLowerCase());
            	while((input.hasNextLine())&&(!((line1=input.nextLine()).intern()=="".intern()))){
				addLineToMap(line1.toLowerCase());
				if((input.hasNextLine())&&!((line2=input.nextLine()).intern()=="".intern())){
					addLineToMap(line2.toLowerCase());
					addToMap(tagToTag,line1.split("\t")[1].toLowerCase()+"\t"+line2.split("\t")[1].toLowerCase());
					
				}
				System.out.println("line1:"+line1);
				System.out.println("line2:"+line2);
			}
				System.out.println();
				numOfSentences++;
			}
            input.close();
    	
            printModelToFile("out.txt");
         
		
		
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}

	}
    private void printModelToFile(String fileName){
    	   PrintWriter writer;
   		try {
   			writer = new PrintWriter(new FileWriter(fileName));
   			writer.println("SECTION1");
   			printProbabilitiesToFile(tagToTag,tagAndCount,writer);
   			writer.println("SECTION2");
   			printProbabilitiesToFile( tagToTWord, tagAndCount, writer);
   			writer.println("SECTION3");
   			for(Entry<String, Integer> en : tagInFirstSent.entrySet()){
   				writer.println(String.format("%s\t%.14f", en.getKey(),(float)(en.getValue()/numOfSentences)));
   			}
   			writer.flush();
   			writer.close();
   		} catch (IOException e) {
   			e.printStackTrace();
   		}
    }
    private void addLineToMap(String line){
    	String[] wordAndTag=null;
    	wordAndTag=line.split("\t");
		addToMap(tagToTWord, wordAndTag[1]+"\t"+wordAndTag[0]);
		addToMap(wordAndCount, wordAndTag[0]);
		addToMap(tagAndCount, wordAndTag[1]);
		
    }
    private void printProbabilitiesToFile(
    LinkedHashMap<String,Integer> map1,LinkedHashMap<String, Integer> map2,PrintWriter pw){
    	PrintWriter	writer = pw;
	for(Entry<String, Integer> en : map1.entrySet()){
		String key=en.getKey();
		double d =(double)(map2.get(key.substring(0,key.lastIndexOf("\t"))));
		writer.println(key+"\t"+String.format("%.14f",en.getValue()/d));
	}
	writer.flush();
    
    }
//    private void addTupleToMap(HashMap<Tuple<String, String>,Integer> map,Tuple<String,String> tuple){
//    	if(map.containsKey(tuple))
//    		map.put(tuple, map.get(tuple)+1);
//    	else {
//    		map.put(tuple, 1);
//    	}
//    }
    private void addToMap(Map<String, Integer> map,String word){
    	if(map.containsKey(word))
    		map.put(word, map.get(word)+1);
    	else {
    		map.put(word, 1);
    	}
//        System.out.println(word);

    }
}
