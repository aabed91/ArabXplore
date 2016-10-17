package utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.service.GoogleResult;
import com.service.LuceneExamples2;
import com.service.Term;
import com.service.URLRank;

import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;
import wikipedia.WikiPage;
import wikipedia.WikipediaInit;

public class Utils {

	//get all snippet from returned google results
	//Input: array list of google result objects
	//Output: string of snippet
	public static String getSnippetFromGoogleResults(ArrayList<GoogleResult> resultArrayList){
		String sneppts = "";
		for (int i=0;i<resultArrayList.size();i++){
			 sneppts = sneppts + " " + resultArrayList.get(i).getContent();
		 }
		
		return sneppts;
	}
	
	//get Links from Wikipedia page and calculate tf
	//Input: Wikipedia page URL
	//Output: Hash map with extracted links as key and number of occurance as value (TF)
	public static HashMap<String, Term> getLinksFromWikipediaPage(URLRank urlRank) throws IOException, WikiInitializationException, WikiApiException{
		
		HashMap<String, Term> hashMap = new HashMap<>();
		String page = urlRank.getPage().getText();
		//WikipediaInit.getInstance().getWikipediaPage(url).getText();
		List<String> res = Utils.getTagValues(page);
		
        for (String in_link : res) {
        	//System.out.println("IN LINK:"+in_link);        	
        	Term term = new Term();
        	if (hashMap.containsKey(in_link)){
        		term = hashMap.get(in_link);
        		term.setTf(term.getTf()+1);
        		hashMap.replace(in_link, term);
        	}else{
        		term.setTf(1);
        		hashMap.put(in_link, term);
        	}
            //print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }
        /*
		//System.out.println("###########################");
        for (Map.Entry<String, Term> entry : hashMap.entrySet()){
        	String key = entry.getKey().toString();
        	int value = entry.getValue().getTf();
        	System.out.println("key, " + key + " value " + value );
        }
        */
		return hashMap;
	}
	
	//calculate idf = log(number of documents / number of documents contains the url)
	//Input: link and arraylist
	//Output: idf for input link
	public static double calculateIdf(String url,ArrayList<HashMap<String, Term>> terms){
		int count = 0;
		double idf;
		for (int i =0;i<terms.size();i++){
			if (terms.get(i).containsKey(url)){
				count++;
			}
		}
		idf = Math.log(terms.size() / count);
		
		return idf;
	}
	
	//calculate tfidf
	//Input: array list
	//Output: edited array list with tfidf values
	public static ArrayList<HashMap<String, Term>> calculateTFIDF(ArrayList<HashMap<String, Term>> terms){
		for (int i = 0 ; i < terms.size();i++){
        	Term t = new Term();
        	for (Map.Entry<String, Term> entry : terms.get(i).entrySet()){
        		String key = entry.getKey();
        		double idf = Utils.calculateIdf(key, terms);
        		t = terms.get(i).get(key);
        		t.setIdf(idf);
        		t.setTfidf(t.getTf() * t.getIdf());
        		terms.get(i).replace(key, t);
        		
        		
        	}
        }
		
		return terms;
	}
	
	
    
	public static void printArrayList(ArrayList<HashMap<String, Term>> terms){
		for (int i=0;i<terms.size();i++){
        	for (Map.Entry<String, Term> entry : terms.get(i).entrySet()){
        		String key = entry.getKey();
            	int tf = entry.getValue().getTf();
            	double idf = entry.getValue().getIdf();
            	double tfidf = entry.getValue().getTfidf();
            	
            	System.out.println("URL:" + key);
            	System.out.println("TF:" +tf );
            	//System.out.println("IDF:" + idf);
            	//System.out.println("TFIDF:" + tfidf);
            	System.out.println("#######################################");
            }
        }
	}
	
	public static void addURLsToFinalArrayList(ArrayList<HashMap<String, Term>> terms,ArrayList<URLRank> finalUrls){
		for (int i=0;i<terms.size();i++){
        	for (Map.Entry<String, Term> entry : terms.get(i).entrySet()){
        		try{
        			
        		
        		String key = entry.getKey();
            	double tfidf = entry.getValue().getTfidf();
            	Page page = WikipediaInit.getInstance().getWikipediaPage(key.toString());
            	if (tfidf >= Constants.TFIDF_THRESHOLD){
            		URLRank url = new URLRank();
            		url.setUrl(key.toString());
            		url.setPage(page);
            		url.setOutlinks(page.getOutlinkIDs());
            		url.setHiScore(1);
            		url.addRank(0);
            		//url.setRank(-1);
            		finalUrls.add(url);
            	}
        		}catch(Exception e){
        			//e.printStackTrace();
        		}
            }
        }
	}
	
	public static List<String> getTagValues(final String str) {
		final List<String> tagValues = new ArrayList<String>();
	    Matcher matcher = Pattern.compile("\\[\\[(.*?)\\]\\]").matcher(str);
	    while (matcher.find()) {
	    	if (matcher.group(1).contains(":")){
	    		matcher.group(1).replace(":", "").replace("تصنيف", "");
	    	}
	    	//System.out.println("WIKI WORD: "+matcher.group(1));
	        tagValues.add(new String(matcher.group(1)));
	        
	    }
	    
	    return tagValues;
	}
	    

	public static boolean isProbablyArabic(String s) {
	    for (int i = 0; i < s.length();) {
	        int c = s.codePointAt(i);
	        if (c >= 0x0600 && c <= 0x06E0)
	            return true;
	        i += Character.charCount(c);            
	    }
	    return false;
	  }
	
	
	public static LinkedHashMap<WikiPage, Double> sortHashMapByValues(HashMap<WikiPage, Double> passedMap) {
	    List<WikiPage> mapKeys = new ArrayList<>(passedMap.keySet());
	    List<Double> mapValues = new ArrayList<>(passedMap.values());
	    Collections.sort(mapValues);
	    Collections.sort(mapKeys);
	    Collections.reverse(mapValues);
	    Collections.reverse(mapKeys);
	    LinkedHashMap<WikiPage, Double> sortedMap =
	        new LinkedHashMap<>();

	    Iterator<Double> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	        double val = valueIt.next();
	        Iterator<WikiPage> keyIt = mapKeys.iterator();

	        while (keyIt.hasNext()) {
	            WikiPage key = keyIt.next();
	            double comp1 = passedMap.get(key);
	            double comp2 = val;

	            if (comp1 == comp2) {
	                keyIt.remove();
	                sortedMap.put(key, val);
	                break;
	            }
	        }
	    }
	    return sortedMap;
	}
    
	public static boolean isImportantWord(String word) throws Exception{
		double occurance = LuceneExamples2.search(word);
		double links = WikipediaInit.getInstance().getWikipediaPage(word).getNumberOfInlinks();
		double importance = links/occurance;
		System.out.println(importance);
		if (importance >= Constants.IMPORTANCE_THRESHOLD){
			return true;
		}else{
			return false;
		}
//		System.out.println("Word: "+word+" | Importance: "+importance);
	}
    
	public static void normalizeRankingResults(Map<WikiPage,Double> rankedPages,double max,double min){
		double scale = max - min;
		Double newValue = 1.0;
		
		for (Entry<WikiPage, Double> entry : rankedPages.entrySet()){
    		
			Double oldValue = entry.getValue();
			if(min != max){
				newValue = (oldValue - min) / scale;
				rankedPages.replace(entry.getKey(), newValue);
			}else{
				newValue = 1.0;
				rankedPages.replace(entry.getKey(), newValue);
			}
    		
    		
    	}
		
	}
	
	public static void printHashMap(Map<WikiPage,Double> map){
		Iterator<WikiPage> iter22 = map.keySet().iterator();
	       
		  Iterator<Double> iter11 = map.values().iterator();
		   
	      while(iter22.hasNext()){
	        	WikiPage wiki = iter22.next();
	        	System.out.println("ID: "+wiki.getUrl());
	        	System.out.println("RANK: "+iter11.next());
	       }
	}
	
	public static int getRankScale(Double rank){
		if (rank <= 0.2){
			return 1;
		}else if (rank > 0.2 && rank <= 0.4){
			return 2;
		}else if (rank > 0.4 && rank <= 0.6){
			return 3;
		}else if (rank > 0.6 && rank <= 0.8){
			return 4;
		}else if (rank > 0.8 && rank <= 1){
			return 5;
		}else{
			return 0;
		}
	}
	
	
}
