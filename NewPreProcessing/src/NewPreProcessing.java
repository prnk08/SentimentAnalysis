
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.InputStreamReader;
import java.util.ArrayList;
//import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


public class NewPreProcessing {

	static String path2 = "/home/priyanka/Desktop/IRE/MajorProject/Data";
	//static String path2 = "/home/al/Desktop/MajorProject/Data";
	static Set<String> stopWord = new HashSet<String>();
	static ArrayList<String> buf = new ArrayList<String>();
	static ArrayList<String> buf1 = new ArrayList<String>();
	static TreeSet<String> uniq = new TreeSet<String>();
	

	public static void main(String[] args) 
	{
		
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader("stopwords.txt"));
			String data=null;
			
			while((data=br.readLine())!=null)
			{
				stopWord.add(data);
			}
			
			br.close();
			
			String input = "/home/priyanka/Desktop/IRE/MajorProject/final_out";
			//String input = "/home/al/Desktop/MajorProject/final_out";
			
			BufferedReader br_file = new BufferedReader(new FileReader(input));
			
			File f1 = new File(input);
			String str = f1.getName();
			
			File f2 = new File(path2+"/processed_"+str);
			
			// if file doesn't exist, then create it
			if (!f2.exists())
			{
				f2.createNewFile();
			}
			
			FileWriter fw = new FileWriter(f2.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			File f4 = new File(path2+"/tagged_processed_"+str);
			
			// if file doesn't exist, then create it
			if (!f4.exists())
			{
				f4.createNewFile();
			}
			
			FileWriter fw2 = new FileWriter(f4.getAbsoluteFile());
			BufferedWriter bw2 = new BufferedWriter(fw2);
			
			
			while((data=br_file.readLine())!=null)
			{
				String[] values = data.split("\\s\\[");
				
				if(values[0].trim().equals("positive"))
					{
						buf.add("+1");
						buf1.add("+1");
					}
				else if(values[0].trim().equals("negative"))
					{
						buf.add("-1");
						buf1.add("-1");
					}
				else if(values[0].trim().equals("neutral"))
					{
						buf.add("0");
						buf1.add("0");
					}
				
				//System.out.println(data);
				//System.out.println(values[1]);
				
				String temp1 = values[1].substring(0,values[1].length()-1);
				String temp2 = values[2].substring(0,values[2].length()-1);
				
				//System.out.println(temp1);
				
				//String[] values1 = temp1.split("[,\\s]+");
				String[] values1 = temp1.split("\\s");
				String[] values2 = temp2.split("\\s");
				
				int len = values1.length;
				//System.out.println("len:"+len);
				//System.out.println(values1);
				if(len==2 && values1[0].substring(1,values1[0].length()-2).equals("not") && values1[1].substring(1,values1[1].length()-1).equals("available"))
				{
					//System.out.println(values1);
					buf.clear();
					buf1.clear();
					continue;
				}
				
				for(int j=0;j<len;j++)
				{
					//System.out.println("j="+j);
					//System.out.println(values1[j]);
					String temp = null;
					String tag = null;
					//System.out.println("length-1:"+(values1[j].length()-1));
					
					if(j==len-1)
					{
						temp = values1[j].substring(1,values1[j].length()-1);
						tag = values2[j].substring(1,values2[j].length()-1);
					}
						
					else
					{
						temp = values1[j].substring(1,values1[j].length()-2);
						tag = values2[j].substring(1,values2[j].length()-2);
					}
					
					if(temp.equals(""))
					{
						continue;
					}
					
					if(temp.length()>1 && temp.charAt(0)=='#')
						temp = temp.substring(1,temp.length());
					
					tag=tag.toLowerCase();
					
					if(!stopWord.contains(temp))
					{
						buf.add(temp);
						buf1.add(tag);
					}
				}
				
				int size = buf.size();
				
				if(size==1)
				{
					//System.out.println(len);
					for(int j=0;j<len;j++)
					{
						
						String temp = null;
						String tag=null;
						
						if(j==len-1)
						{
							temp = values1[j].substring(1,values1[j].length()-1);
							tag = values2[j].substring(1,values2[j].length()-1);
						}
							
						else
						{
							temp = values1[j].substring(1,values1[j].length()-2);
							tag = values2[j].substring(1,values2[j].length()-2);
						}
							
						//System.out.println("----"+temp);
						temp=temp.toLowerCase();
						tag=tag.toLowerCase();
						
						buf.add(temp);
						buf1.add(tag);
						
					}
				}
				
				size = buf.size();
				int size1 = buf1.size();
				
				if(size1 != size)
					System.out.println("size:"+size+" size1:"+size1);
				
				if(size > 1){
					for(int x=0;x<size;x++)
					{
						if(x==0)
						{
							bw.write(buf.get(x)+" ");
							bw2.write(buf1.get(x)+" ");	
						}
							
						else if(x>0)
						{							
							uniq.add(buf.get(x));
							
							if(x==size-1)
							{
								bw.write(buf.get(x)+"\n");
								bw2.write(buf.get(x)+"#"+buf1.get(x)+"\n");
							}
								
							else
							{
								bw.write(buf.get(x)+" ");
								bw2.write(buf.get(x)+"#"+buf1.get(x)+" ");
							}
						
						}
					}
					
					for(int x=1;x<size-1;x++)
					{
							uniq.add(buf.get(x)+" "+buf.get(x+1));	
					}
				}
					
					buf.clear();
					buf1.clear();
				
			}
			
			br_file.close();
			bw.close();
			bw2.close();
			
			File f3 = new File(path2+"/uniq_uni&bigrams_processed_"+str);
			
			// if file doesn't exist, then create it
			if (!f3.exists())
			{
				f3.createNewFile();
			}
			
			FileWriter fw1 = new FileWriter(f3.getAbsoluteFile());
			BufferedWriter bw1 = new BufferedWriter(fw1);
			
			Iterator itr = uniq.iterator();
			
			while(itr.hasNext())
			{
				bw1.write(itr.next()+"\n");
			}
			
			bw1.close();
			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}

}
