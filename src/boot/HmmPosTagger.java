package boot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HmmPosTagger {

	public static void main(String[] args) {
	if(args.length==2){	
		System.out.println(args[1]);
		List<Character> mysclass = new ArrayList<Character>();
		List<Integer> mysclass2 = new ArrayList<Integer>();
		List<String> mysclass3 = new ArrayList<String>();
		List<Integer> mysclass4 = new ArrayList<Integer>();
		List<String> mysclass5 = new ArrayList<String>();
		List<Integer> mysclass6 = new ArrayList<Integer>();
		List<Character> mysclass7 = new ArrayList<Character>();
		List<Character> mysclass8 = new ArrayList<Character>();
		List<Integer> mysclass9 = new ArrayList<Integer>();
		List<Character> taggerChar = new ArrayList<Character>();
		List<Double> taggerPro = new ArrayList<Double>();
		HashMap<Character,Double> taggerPi= new HashMap<Character,Double>() ;
		HashMap<String,Double> matrixA= new HashMap<String,Double>() ;
		HashMap<String,Double> matrixB= new HashMap<String,Double>() ;
		
		BufferedReader br =null;
		BufferedReader brTest=null;
		int num;
		int sum=0;
		int counter=0;
		char A;
		String word="";
		int index=0;
		boolean flag=false;
		
		try {

			String sCurrentLine;
			
			br = new BufferedReader(new FileReader("oct27.train"));
//			new BufferedWriter(new FileWriter("model2.txt")); 
			{
			while ((sCurrentLine = br.readLine()) != null) {
				
				if (sCurrentLine.isEmpty()){
					flag=true;
					sum=0;
				}
				for (int i = 0; i <  sCurrentLine.length(); i++)
				{
					if (sCurrentLine.charAt(i)=='\t')
					{
						
						A= sCurrentLine.charAt(i+1);
						if (flag)
						{
							if (!mysclass8.contains(A)){
								mysclass8.add(A);
								mysclass9.add(1);
								counter++;
							}
							else{
								num= mysclass9.get((mysclass8.indexOf(A)));
								num++;							
								mysclass9.set(mysclass8.indexOf(A),num);
								counter++;
							}
							flag=false;
						}
					
						if (!mysclass5.contains(sCurrentLine)){
							mysclass5.add(sCurrentLine);
							mysclass6.add(1);
							mysclass7.add(A);
							
						}
						else
						{
							num= mysclass6.get((mysclass5.indexOf(sCurrentLine)));
							num++;							
							mysclass6.set(mysclass5.indexOf(sCurrentLine),num);
						}
						sum++;						
						if(sum>=2)
						{
							if (!mysclass3.contains(mysclass.get(index)+" "+A))
							{
								
								mysclass3.add(mysclass.get(index)+" "+A);
								mysclass4.add(1);
								
							}
							else
							{
								num= mysclass4.get(mysclass3.indexOf(mysclass.get(index)+" "+A));
								num++;
								mysclass4.set(mysclass3.indexOf(mysclass.get(index)+" "+A),num);
								
							}						
						}
						if (!mysclass.contains(A)){
							mysclass.add(A);
							mysclass2.add(1);
						}
						else
						{
							num= mysclass2.get((mysclass.indexOf(A)));
							num++;
							
							mysclass2.set(mysclass.indexOf(A),num);
						}
						index=mysclass.indexOf(A);
					}
					
					
				
				}

			}
			
			double pro;
			
			for (int i=0; i<mysclass3.size(); i++)
			{
				A=mysclass3.get(i).charAt(0);
				index=mysclass.indexOf(A);
				pro=(double)mysclass4.get(i)/mysclass2.get(index);
				matrixA.put(mysclass3.get(i),pro);
				
			}
			
			for (int i=0; i<mysclass5.size(); i++)
			{
				index=mysclass.indexOf(mysclass7.get(i));
				pro=(double)mysclass6.get(i)/mysclass2.get(index);
				matrixB.put(mysclass5.get(i), pro);
			
			}
			
			for (int i=0; i<mysclass8.size(); i++)
			{
				pro=(double)mysclass9.get(i)/counter;
				taggerPi.put(mysclass8.get(i),pro);
			
			}
			
			 }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		try {
			
			boolean start=true;
			String sCurrentLine;
			double maxstart=0;
			double max=0;
			double sumStart;
			double sumTag;
			boolean foundStart=false;
			boolean found=false;
			
			brTest = new BufferedReader(new FileReader(args[1]));

			while ((sCurrentLine = brTest.readLine()) != null) {
				
				if(sCurrentLine.isEmpty()){
					start=true;
				}
				
				for (int i = 0; i <  sCurrentLine.length(); i++)
					
					if (!(sCurrentLine.charAt(i)=='\t')){				
						word=word+sCurrentLine.charAt(i);
					}
					else
						if (start)
						{
							for(int j=0;j<mysclass8.size();j++)
							{
 								if (mysclass5.contains(word+"\t"+mysclass8.get(j)))
								{
									if (taggerPi.containsKey(mysclass8.get(j)))
									{
										sumStart=taggerPi.get(mysclass8.get(j))*matrixB.get(word+"\t"+mysclass8.get(j));
										if (sumStart>maxstart)
										{
											maxstart=sumStart;
											index=j;
											foundStart=true;
										}
										
									}
								}
							}
							if (foundStart){
								taggerChar.add(mysclass8.get(index));
								taggerPro.add(maxstart);
								maxstart=0;
								start=false;
								word="";
								i++;
								foundStart=false;
							}
							else
							{
								taggerChar.add('@');
								taggerPro.add(0.0014*0.3);
								maxstart=0;
								start=false;
								word="";
								i++;
							}
						}
						else
						{
							for(int j=0;j<mysclass.size();j++)
							{
								if (mysclass5.contains(word+"\t"+mysclass.get(j)))
								{
									if (matrixA.containsKey(taggerChar.get(taggerChar.size()-1)+" "+mysclass.get(j)))
									{
										sumTag=matrixA.get(taggerChar.get(taggerChar.size()-1)+" "+mysclass.get(j))*
												matrixB.get(word+"\t"+mysclass.get(j))*
												taggerPro.get(taggerPro.size()-1);
										
										
										
										if (sumTag>max)
										{
											max=sumTag;
											index=j;
											found=true;
										}
									}
								}
									
							}
							if (found)
							{
								taggerChar.add(mysclass.get(index));
								taggerPro.add(max);
								max=0;
								word="";
								i++;
								found=false;
							}
							else
							{
								taggerChar.add('N');
								taggerPro.add(0.3*0.001*taggerPro.get(taggerPro.size()-1));		
								max=0;
								word="";
								i++;
							}
						}
								
					{
			}
				
					
					
		}
			for (int i=0; i<taggerChar.size();i++)
			{
				if ((i+1)<taggerChar.size())
					if(taggerPro.get(i)>taggerPro.get(i+1))
						System.out.println(taggerChar.get(i)+" "+taggerPro.get(i)); 
					else
					{	
						System.out.println(taggerChar.get(i)+" "+taggerPro.get(i)); 
						System.out.println();
					}
				else
					System.out.println(taggerChar.get(i)+" "+taggerPro.get(i)); 
			}		
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (brTest != null)brTest.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		

	}else {System.err.println("no valid input . please type according to This structure - testFile <test.txt>(example)");}
}
		
}
	


