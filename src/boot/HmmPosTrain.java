package boot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class HmmPosTrain {
	LinkedHashMap<String, Integer> tagToTag;
	LinkedHashMap<String, Integer> wordToTag;
	LinkedHashMap<String, Integer> tagAndCount;
	LinkedHashMap<String, Integer> wordAndCount;

	public static void main(String[] args) {
      HmmPosTrain h=new HmmPosTrain("oct27.train");
	}
	
    public HmmPosTrain(String inputCorpusFile){
		try {
			Scanner input=new Scanner(new BufferedReader(new FileReader(inputCorpusFile)));
//			input.useDelimiter("[\r\n]");
			tagToTag=new LinkedHashMap<String, Integer>();
			String line=null;
            String []wordAndTag=null;
			while(input.hasNextLine()){
			
				
				while(!((line=input.nextLine()).intern()=="".intern())){
				wordAndTag=line.split("\t");
				addToMap(wordToTag, line.toLowerCase());
				addToMap(wordAndCount, wordAndTag[0]);
				addToMap(tagAndCount, wordAndTag[1]);
				System.out.println(line);
			};
		}
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}

	}
    
    private void addToMap(LinkedHashMap<String, Integer> map,String word){
    	if(map.containsKey(word))
    		map.put(word, map.get(word)+1);
    	else {
    		map.put(word, 1);
    	}
//        System.out.println(word);

    }
}
