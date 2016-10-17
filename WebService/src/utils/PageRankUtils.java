package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.service.URLRank;

import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import jung.BiasePageRank;
import jung.PlainPageRank;
import links.WebGraph;
import wikipedia.WikiPage;
import wikipedia.WikipediaInit;

public class PageRankUtils {

	public static HashMap<WikiPage,Double> rankSnippetPages(List<URLRank> urlRank){
		HashMap<WikiPage, List<WikiPage>> pagesMap = new HashMap<WikiPage, List<WikiPage>>(); 
		WikiPage[] wikiPageArray = new WikiPage[urlRank.size()];
		
		for(int i=0;i<urlRank.size();i++){
			WikiPage wikiPage = new WikiPage();
			wikiPage.setId(String.valueOf(urlRank.get(i).getId()));
			wikiPage.setUrl(urlRank.get(i).getUrl());
			wikiPage.setHitScore(urlRank.get(i).getHiScore());
			wikiPageArray[i] = wikiPage;
		}
		
		for(int i=0;i<urlRank.size();i++){

			
			List<WikiPage> wikiPageList = new ArrayList<WikiPage>();
			
			for(int pageId:urlRank.get(i).getOutlinks()){
				
				for(int j=0;j<urlRank.size();j++){
					if(pageId == urlRank.get(j).getId()){

						wikiPageList.add(wikiPageArray[j]);
					}
				}
			}
			
			pagesMap.put(wikiPageArray[i], wikiPageList);
			
		}
		
		PlainPageRank plainPageRank = new PlainPageRank(pagesMap, 0.85d, 20);
		return plainPageRank.compute();
	}
	
	public static HashMap<WikiPage,Double> rankSnippetPagesUsingBiasePageRank(List<URLRank> urlRank){
		HashMap<WikiPage, List<WikiPage>> pagesMap = new HashMap<WikiPage, List<WikiPage>>(); 
		WikiPage[] wikiPageArray = new WikiPage[urlRank.size()];
		
		for(int i=0;i<urlRank.size();i++){
			WikiPage wikiPage = new WikiPage();
			wikiPage.setId(String.valueOf(urlRank.get(i).getId()));
			wikiPage.setUrl(urlRank.get(i).getUrl());
			wikiPage.setHitScore(urlRank.get(i).getHiScore());
			wikiPageArray[i] = wikiPage;
		}
		
		for(int i=0;i<urlRank.size();i++){

			
			List<WikiPage> wikiPageList = new ArrayList<WikiPage>();
			
			for(int pageId:urlRank.get(i).getOutlinks()){
				
				for(int j=0;j<urlRank.size();j++){
					if(pageId == urlRank.get(j).getId()){

						wikiPageList.add(wikiPageArray[j]);
					}
				}
			}
			
			pagesMap.put(wikiPageArray[i], wikiPageList);
			
		}
		
		BiasePageRank plainPageRank = new BiasePageRank(pagesMap, 0.85d, 20);
		return plainPageRank.compute();
	}
}
