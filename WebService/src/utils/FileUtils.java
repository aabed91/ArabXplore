package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import com.service.GoogleResult;

import wikipedia.WikiPage;

public class FileUtils {
	
	public String myReadFileUtf8(String fileName){
        String str = null;
	try {
		File fileDir = new File(fileName);
 
		BufferedReader in = new BufferedReader(
		   new InputStreamReader(
                      new FileInputStream(fileDir), "UTF8"));
 
		
 
		while ((str = in.readLine()) != null) {
		    System.out.println(str);
		}
 
                in.close();
	    } 
	    catch (UnsupportedEncodingException e) 
	    {
			System.out.println(e.getMessage());
	    } 
	    catch (IOException e) 
	    {
			System.out.println(e.getMessage());
	    }
	    catch (Exception e)
	    {
			System.out.println(e.getMessage());
	    }
        
        return str;
	}
    
    
    public String readEntilerFile(String fn) throws FileNotFoundException{
        String content = new Scanner(new File(fn), "UTF-8").useDelimiter("\\A").next();
        return content;
    }
    
    
    public void writeFileUTF(String sring, String fn) throws UnsupportedEncodingException, FileNotFoundException, IOException{
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fn), "UTF-8"));
        try {
            out.write(sring);
        } finally {
            out.close();
        }
    }
    
    public static void writeResultsToFile(String searchWork,ArrayList<GoogleResult> googleResult,Map<WikiPage, Double> result,int fileIndex) throws Exception{
    	Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/aabed91/Desktop/Ahmed/MasterThesis/RankResults/AugmentedPageRank/"+fileIndex+".txt"), "UTF-8"));
    	out.append(searchWork+"\n");
    	for(Entry<WikiPage, Double> entry : result.entrySet()){
    		WikiPage wiki = entry.getKey();
    		Double rank = entry.getValue();
    		int rate = Utils.getRankScale(rank);
    		
    		String line = "R:"+wiki.getUrl()+","+rank+","+rate+",\n";
    		
    		out.append(line);
    	}
    	out.append("\n\n\n");
    	for(GoogleResult google : googleResult){
    		out.append("S:"+google.getContent()+"\n");
    	}
    	
    	out.close();
    }
    
    public static void writeBiaseResultsToFile(String searchWork,ArrayList<GoogleResult> googleResult,Map<WikiPage, Double> result,int fileIndex) throws Exception{
    	Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/aabed91/Desktop/Ahmed/MasterThesis/RankResults/PageRank/"+fileIndex+".txt"), "UTF-8"));
    	out.append(searchWork+"\n");
    	for(Entry<WikiPage, Double> entry : result.entrySet()){
    		WikiPage wiki = entry.getKey();
    		Double rank = entry.getValue();
    		int rate = Utils.getRankScale(rank);
    		
    		String line = "R:"+wiki.getUrl()+","+rank+","+rate+",\n";
    		
    		out.append(line);
    	}
    	out.append("\n\n\n");
    	for(GoogleResult google : googleResult){
    		out.append("S:"+google.getContent()+"\n");
    	}
    	
    	out.close();
    }
    
    public static void writeTimeToFile(int fileIndex,String task,long time) throws IOException{
    	File file = new File(Constants.TIME_TEST_PATH+fileIndex+".txt");
    	String text = task+": "+String.valueOf(time)+"\n";
    	if(!file.exists()){
    		file.createNewFile();
    	}
		Files.write(Paths.get(Constants.TIME_TEST_PATH+fileIndex+".txt"), text.getBytes(), StandardOpenOption.APPEND);

    }
}
