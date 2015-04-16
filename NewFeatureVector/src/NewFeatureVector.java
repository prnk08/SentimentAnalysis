
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class NewFeatureVector 
{
	static TreeMap<String,Integer> unibigrams = new TreeMap<String,Integer>();
	static TreeSet<Integer> ts = new TreeSet<Integer>();
	static Integer count = 0;

	public static void main(String[] args) 
	{
		String path = "UNIQ/uniq_uni&bigrams_processed_TRAINDATA";
		//String path1 = "UNIQ/uniq_uni&bigrams_processed_TESTDATA";
		String path2 = "/home/priyanka/Desktop/IRE/MajorProject/Data";
		//String path2 = "/home/al/Desktop/MajorProject/Data";
		String path3 = "emoticons/";
		Set<String> pos = new HashSet<String>();
		Set<String> neg = new HashSet<String>();
		Set<String> neutral = new HashSet<String>();
		
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(path));
			
			String data=null;
			
			while((data=br.readLine())!=null)
			{
				count++;
				unibigrams.put(data, count);
			}
			
			br.close();
			
			/*BufferedReader br_test = new BufferedReader(new FileReader(path1));
			
			data=null;
			
			while((data=br_test.readLine())!=null)
			{
				if(!unibigrams.containsKey(data))
				{
					count++;
					unibigrams.put(data, count);
				}
			}
			
			br_test.close();*/
			
			/*final count denotes number of dimensions obtained with unigrams and bigrams
			 * now dimension count+1 --> feature f1 (no. of +ve tokens)
			 * 				 count+2 --> feature f2 (no. of -ve tokens)
			 * 				 count+3 --> feature f3 (maxm +ve score)
			 * 				 count+4 --> feature f4 (maxm -ve score)
			 * 				 count+5 --> feature f5 (total score of sentence)
			 * where f1, f2, f3, f4, f5 denote features added other than uni/bi-grams */
			
			//System.out.println("Total unique ngrams:"+count);
			
			//----------------------------------
			
			BufferedReader br_emo = new BufferedReader(new FileReader(path3+"pos"));
			
			data = null;
			
			while((data=br_emo.readLine())!=null)
			{
				pos.add(data);
			}
			
			br_emo.close();
			
			br_emo = new BufferedReader(new FileReader(path3+"neutral"));
			
			while((data=br_emo.readLine())!=null)
			{
				neutral.add(data);
			}
			
			br_emo.close();
			
			br_emo = new BufferedReader(new FileReader(path3+"neg"));
			
			while((data=br_emo.readLine())!=null)
			{
				neutral.add(data);
			}
			
			br_emo.close();
			
			int dim_f1 = count+1;
			int dim_f2 = count+2;
			int dim_f3 = count+3;
			int dim_f4 = count+4;
			int dim_f5 = count+5;
			
			//----------------------------------
			String input = "/home/priyanka/Desktop/IRE/MajorProject/Data/processed_final_out";
			//String input = "/home/al/Desktop/MajorProject/Data/processed_final_out";
			File f1 = new File(input);
			BufferedReader br1 = new BufferedReader(new FileReader(f1));
			String str = f1.getName();
			
			//------------------------------
			BufferedReader br2 = new BufferedReader(new FileReader(path2+"/tagged_"+str));
			//------------------------------
			
			File f2 = new File(path2+"/newfeatures_"+str);
			
			// if file doesn't exist, then create it
			if (!f2.exists())
			{
				f2.createNewFile();
			}
			
			FileWriter fw = new FileWriter(f2.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			String pathToSWN = "SentiWordNet_3.0.0_20130122.txt";
		    SentiWordNet swn = new SentiWordNet(pathToSWN);
			
			while((data=br1.readLine())!=null)
			{
				String[] values = data.split(" ");
			
				//---------------------------------
				String data2 = br2.readLine();
				String[] values2 = data2.split(" ");
				//---------------------------------
				
				bw.write(values[0]+" ");
				
				int len = values.length;
				
				double score_sum = 0.0, max_pos=0.0,max_neg=0.0;
				double token_count = 0,pos_count=0,neg_count=0;
				
				
				for(int i=1;i<len;i++)
				{
					if(unibigrams.containsKey(values[i]))
					{
						ts.add(unibigrams.get(values[i]));
					}
					
					//---------------------
					
					//System.out.println(values2[i]);
					
					String[] temp = values2[i].split("#",2);
					
					double score = 0.0;
					
					if(temp[1].equals("n")||temp[1].equals("a")||temp[1].equals("v")||temp[1].equals("r"))
					{
						score = swn.extract(temp[0],temp[1]);
						
						if(score > 0)
						{
							pos_count++;
							
							if(score > max_pos)
								max_pos = score;
						}
							
						else if(score < 0)
						{
							neg_count++;
							
							if(score < max_neg)
								max_neg = score;
						}
							
						
						score_sum+=score;
						token_count ++;
					}
						
					else if(temp[1].equals("e"))
					{
						if(pos.contains(temp[0]))
							{
								score = 0.5; 
								pos_count++;
								if(score > max_pos)
									max_pos = score;
							}
						else if(neg.contains(temp[0]))
							{
								score = -0.5;
								neg_count++;
								if(score < max_neg)
									max_neg = score;
							}
						else if(neutral.contains(temp[0]))
							score = 0.01;
						else
							score = 0.0;
						
						//or for neutral 0.01 and 0.0 in else
						
						score_sum+=score;
						token_count++;
					}
						
					//---------------------	
				}
				
				for(int i=1;i<len-1;i++)
				{
					if(unibigrams.containsKey(values[i]+" "+values[i+1]))
					{
						ts.add(unibigrams.get(values[i]+" "+values[i+1]));
					}
				}
				
				Iterator itr = ts.iterator();
				
				while(itr.hasNext())
				{
					Integer val = (Integer) itr.next();
					
					/*if(val==ts.last())
						bw.write(val+":1"+"\n");
					else
						bw.write(val+":1 ");*/
					bw.write(val+":1 ");
				}
				
				ts.clear();
				
				if(token_count>0)
					{
						double p = Math.round((pos_count/token_count)*1000);
						p=p/1000;
						double n = Math.round((neg_count/token_count)*1000);
						n=n/1000;
						bw.write(dim_f1+":"+p+" ");
						bw.write(dim_f2+":"+n+" ");
					}
				else
				{
					bw.write(dim_f1+":0.0"+" ");
					bw.write(dim_f2+":0.0"+" ");
				}
				
				max_pos = Math.round(max_pos*1000);
				max_pos = max_pos/1000;
				max_neg = Math.round(max_neg*1000);
				max_neg = max_neg/1000;
				score_sum = Math.round(score_sum*1000);
				score_sum = score_sum/1000;
				
				bw.write(dim_f3+":"+(max_pos)+" ");
				bw.write(dim_f4+":"+(max_neg)+" ");
				bw.write(dim_f5+":"+score_sum+"\n");
				
			}
			
			bw.close();

		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
