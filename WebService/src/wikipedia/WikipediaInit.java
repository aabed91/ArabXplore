package wikipedia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.service.LuceneExamples2;

import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;
import javassist.compiler.ast.Keyword;
import jersey.repackaged.com.google.common.collect.Iterators;

public class WikipediaInit {

	private static Wikipedia wikipedia;
	private static WikipediaInit wikipediaInit;
	
	private WikipediaInit() throws WikiInitializationException{
		DatabaseConfiguration dbConfig = new DatabaseConfiguration();
        dbConfig.setHost("localhost");
        dbConfig.setDatabase("arwiki");
        dbConfig.setUser("root");
        dbConfig.setPassword("root");
        dbConfig.setLanguage(Language.arabic);
        
        wikipedia = new Wikipedia(dbConfig);        
        
	}
	
	public Iterable getAllWikipediaPages(){
		return wikipedia.getPages();
	}
	
	public static WikipediaInit getInstance() throws WikiInitializationException{
		if (wikipediaInit == null)
			return new WikipediaInit();
		return wikipediaInit;
	}
	
	public Page getWikipediaPage(String keyWord) throws WikiApiException{
		return wikipedia.getPage(keyWord);
	}
	
	public int getWikipediaPageId(String keyWord) throws WikiApiException{
		return wikipedia.getPage(keyWord).getPageId();
	}
	
	
	public Page getPageById(int id) throws WikiApiException{
		return wikipedia.getPage(id);
	}
	
	public Set<Integer> getInLinks(int id) throws WikiApiException{
		return wikipedia.getPage(id).getInlinkIDs();
	}
	
	public Set<Integer> getOutLinks(int id) throws WikiApiException{
		
		return wikipedia.getPage(id).getOutlinkIDs();
	}
	
	public static void main(String[] args) throws WikiInitializationException, WikiApiException, IOException{
		
		DatabaseConfiguration dbConfig = new DatabaseConfiguration();
        dbConfig.setHost("localhost");
        dbConfig.setDatabase("arwiki");
        dbConfig.setUser("root");
        dbConfig.setPassword("root");
        dbConfig.setLanguage(Language.arabic);
        
        Wikipedia wiki = new Wikipedia(dbConfig);
        System.out.println(wiki.getPage("ياسر عرفات").getPlainText());
//	        Page page = wiki.getPage("Berlin");
//	        String pageText = page.getText();
//	        MediaWikiParserFactory pf = new MediaWikiParserFactory();
//	        MediaWikiParser parser = pf.createParser();
//	        ParsedPage pp = parser.parse(page.getText());
//	        for(Template t : pp.getTemplates()){
//	                if (t.getName().toLowerCase().startsWith("infobox"))
//	                        for(String tp : t.getParameters()){
//	                                System.out.println(tp);
//	                }
//	        }
	
	        
		
//		File f = new File("/Users/aabed91/Downloads/result3.txt");
//		DatabaseConfiguration dbConfig = new DatabaseConfiguration();
//        dbConfig.setHost("localhost");
//        dbConfig.setDatabase("arwiki");
//        dbConfig.setUser("root");
//        dbConfig.setPassword("root");
//        dbConfig.setLanguage(Language.arabic);
//        
//        wikipedia = new Wikipedia(dbConfig); 
//        
//        System.out.println(wikipedia.getPage("برشلونة").getText());
//		FileWriter fr = new FileWriter(f);
//		Iterator<Page> iter = wikipedia.getPages().iterator();
//		PrintWriter pr = new PrintWriter(fr);
//		while(iter.hasNext()){
//			Page p = iter.next();
//			String s = p.getTitle()+","+p.getPageId();
//			pr.println(wikipedia.getPage("برشلونة").getText());
//			pr.flush();
////		}
//		pr.close();
//		fr.close();
		
//		ArrayList<WikipediaPage> wiki = new ArrayList<>();
//		try{
//		File file = new File("/Users/aabed91/Downloads/names-1.txt");
//		FileInputStream fis = new FileInputStream(file);
//		 
//		//Construct BufferedReader from InputStreamReader
//		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
//	 
//		String line = null;
//		while ((line = br.readLine()) != null) {
//			
//			try{
//			Page page = WikipediaInit.getInstance().getWikipediaPage(line);
//			WikipediaPage wik = new WikipediaPage();
//			wik.name = line;
//			wik.page = page.getPlainText();
//			wik.id = page.getPageId();
//			wiki.add(wik);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//		
//		Gson gson = new Gson();
//		PrintWriter writer = new PrintWriter("/Users/aabed91/Downloads/result.txt", "UTF-8");
//		writer.println(gson.toJson(wiki));
//		writer.close();
//		//System.out.println(gson.toJson(wiki));
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		System.out.println("Done");
		
	}
	
}

class WikipediaPage{
	@Expose
	public String name;
	@Expose
	public String page;
	@Expose
	public int id;
	
}
