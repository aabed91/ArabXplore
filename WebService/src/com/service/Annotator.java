package com.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.lang.model.element.Element;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.main.NGramsGenerator;

import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;
import jersey.repackaged.com.google.common.collect.Iterators;
import jersey.repackaged.com.google.common.collect.Lists;
import jung.PlainPageRank;
import links.PageRank;
import links.WebGraph;
import utils.Constants;
import utils.FileUtils;
import utils.JSONUtils;
import utils.PageRankUtils;
import utils.PreProcessing;
import utils.Utils;
import wikipedia.WikiPage;
import wikipedia.WikipediaInit;

@Path("/")
public class Annotator {

	

	private PreProcessing preProcessing;

	 

	

	@Path("annotate")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String annotate(/* @QueryParam("url") String url */) throws Exception {
		String keyWord = "القرآن الكريم";

		preProcessing = PreProcessing.getInstance();

			
			// save google result on this array list
			ArrayList<GoogleResult> resultArrayList = new ArrayList<>();

			// save wikipedia page url

			// save urls extracted from wikipedia and their tfidf values
			ArrayList<HashMap<String, Term>> terms = new ArrayList<>();

			ArrayList<URLRank> finalUrls = new ArrayList<URLRank>();
			ArrayList<URLRank> importantPages = new ArrayList<URLRank>();
			ArrayList<String> pageIds = new ArrayList<String>();
			List<String> allGrams = new ArrayList<String>();
			String snippet = "";
			int position = 1;
			long startTime,endTime,totalTime;
			String id;

			WebGraph graph = new WebGraph();
			
			String line = null;
			int index=930;
			FileReader fileReader = new FileReader(Constants.TEST_DATASET_PATH);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null){
				keyWord = line;
				startTime = System.currentTimeMillis();
				resultArrayList = JSONUtils.getGoogleSnippetResult(keyWord);
				endTime = System.currentTimeMillis();
				
				totalTime = endTime - startTime;
				FileUtils.writeTimeToFile(index, "Get Snippet", totalTime);
				
				
				for (int y = 0; y < resultArrayList.size(); y++) {
					startTime = System.currentTimeMillis();
					position = y + 1;
					snippet = resultArrayList.get(y).getContent();
					if (snippet.contains("<b>"))
						snippet = snippet.substring(snippet.indexOf("<b>"));
					snippet = PreProcessing.removeHTMLTag(snippet);
					snippet = PreProcessing.removeNumbers(snippet);
					// third do segmentation in this snippet
					String snippetAfterSegmentation = preProcessing.segmentString(snippet);

					// forth do stop word removal
					String snippetAfterStopwordRemoval = preProcessing.stopWordRemoval(snippetAfterSegmentation);
					endTime = System.currentTimeMillis();
					
					totalTime = endTime - startTime;
					FileUtils.writeTimeToFile(index, "PreProcessing", totalTime);
//					System.out.println("Snippet " + y + ": " + snippetAfterStopwordRemoval);

//					// String[] words = snippetAfterStopwordRemoval.split(" ");
					
					for (int i = Constants.GRAM_NUMBER; i >= 0; i--)
						allGrams.addAll(NGramsGenerator.ngrams(i, snippetAfterStopwordRemoval));
						
					for (int z = 0; z < allGrams.size(); z++) {
						startTime = System.currentTimeMillis();
						try {

							id = allGrams.get(z);
							boolean isCovered = false;
							for (URLRank r : finalUrls) {
								String s = r.getWord();
								if (s.equals(allGrams.get(z))) {
									r.addRank(position);
									isCovered = true;
								} else if (s.contains(allGrams.get(z))) {
									isCovered = true;
									// if(!allGrams.get(z).equals(""))
									// System.out.println("NOT ADDED
									// ID:"+allGrams.get(z));

								}
							}
							
							
							if (!isCovered && Utils.isProbablyArabic(allGrams.get(z))) {
								try {
									Page page = WikipediaInit.getInstance().getWikipediaPage(id);

									String url = id;
									if (!url.equals("")) {
										
										URLRank urlRank = new URLRank();
										
										urlRank.setUrl(url);
										urlRank.addRank(position);
										urlRank.setWord(allGrams.get(z));
										urlRank.setPage(page);
										urlRank.setId(page.getPageId());
										urlRank.setOutlinks(page.getOutlinkIDs());
										finalUrls.add(urlRank);
										// System.out.println("ADDED
										// ID:"+allGrams.get(z));
										pageIds.add(id);
									}
								} catch (Exception e) {
									// e.printStackTrace();
								}
							}
							
							
							

						} catch (Exception e) {
							// e.printStackTrace();
						}
						endTime = System.currentTimeMillis();
						
						totalTime = endTime - startTime;
						FileUtils.writeTimeToFile(index, "MapPages", totalTime);
					}

				}
				
				FileUtils.writeTimeToFile(index, "SIZE"+finalUrls.size(), 1);
				
	//
//				for (URLRank urlRank : finalUrls) {
//					int hitScore = 0;
//					int times = 0;
//					for (int rank : urlRank.getRanks()) {
//						hitScore += (Constants.SNIPPETS_NUMBER + 1) + 1 - rank;
//						times++;
//					}
//					urlRank.setHiScore(hitScore * times);
//				}
//
//				startTime = System.currentTimeMillis();
				for (URLRank urlRank : finalUrls) {

					try {
						terms.add(Utils.getLinksFromWikipediaPage(urlRank));
					} catch (Exception e) {

					}

				}
//				endTime = System.currentTimeMillis();
//				
//				totalTime = endTime - startTime;
//				FileUtils.writeTimeToFile(index, "ExtractLinks", totalTime);
//				
//				startTime = System.currentTimeMillis();
//				Utils.calculateTFIDF(terms);
//				
//				
//	//
				Utils.addURLsToFinalArrayList(terms, finalUrls);
				FileUtils.writeTimeToFile(index, "SIZE"+finalUrls.size(), 2);

//				endTime = System.currentTimeMillis();
//				
//				totalTime = endTime - startTime;
//				FileUtils.writeTimeToFile(index, "TFIDF", totalTime);
//				
//	//
//				startTime = System.currentTimeMillis();
//				for (URLRank url : finalUrls) {
//					try {
//						if (Utils.isImportantWord(url.getWord())) {
//							importantPages.add(url);
//						}
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						// e.printStackTrace();
//					}
//				}
//				endTime = System.currentTimeMillis();
//				
//				totalTime = endTime - startTime;
//				FileUtils.writeTimeToFile(index, "Importance", totalTime);
//			
//				startTime = System.currentTimeMillis();
//				HashMap<WikiPage, Double> res = PageRankUtils.rankSnippetPages(importantPages);
//				
//				Map<WikiPage, Double> sorted = Utils.sortHashMapByValues(res);
//				endTime = System.currentTimeMillis();
//				
//				totalTime = endTime - startTime;
//				FileUtils.writeTimeToFile(index, "RANK", totalTime);
//				index++;
//				
//				terms.clear();
//				finalUrls.clear();
//				res.clear();
//				sorted.clear();
//				importantPages.clear();
//				allGrams.clear();
//				pageIds.clear();
//				Iterator<Double> iter3 = sorted.values().iterator();
//				ArrayList<Double> values = Lists.newArrayList(iter3);
//				Double max = Collections.max(values);
//				Double min = Collections.min(values);
//
//				
//				Utils.normalizeRankingResults(sorted, max, min);
//				int rank = 1;
//				for (Entry<WikiPage, Double> entry : sorted.entrySet()){
//					WikiPage wikiPage = entry.getKey();
//					FinalResult finalResult = new FinalResult();
//					finalResult.setId(wikiPage.getId());
//					finalResult.setName(wikiPage.getUrl());
//					finalResult.setRank(String.valueOf(rank));
//					rank++;
//					finalResultArray.add(finalResult);
//					
//				}
				
			}
			// http://localhost:8080/SimpleService/rest/annotate
//			
			System.out.println(resultArrayList.size());
//

			
	//#################################################################
			
			
//			for(FinalResult f : finalResultArray){
//				System.out.println(f.getName() + " | "+f.getId()+ " | "+f.getRank());
//			}
//			try {
//				FileUtils.writeResultsToFile(keyWord, resultArrayList, sorted, fileIndex);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			HashMap<WikiPage, Double> biaseRes = PageRankUtils.rankSnippetPagesUsingBiasePageRank(importantPages);
//			Map<WikiPage, Double> biaseSorted = Utils.sortHashMapByValues(biaseRes);
//
//			Iterator<Double> biaseIter3 = biaseSorted.values().iterator();
//			ArrayList<Double> biaseValues = Lists.newArrayList(biaseIter3);
//			Double biaseMax = Collections.max(biaseValues);
//			Double biaseMin = Collections.min(biaseValues);
//
//			Utils.normalizeRankingResults(biaseSorted, biaseMax, biaseMin);
//
//			try {
//				FileUtils.writeBiaseResultsToFile(keyWord, resultArrayList, biaseSorted, fileIndex);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}


		return "Search word is " + keyWord;

	}

	@Path("network")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String buildSemanticNetwork(@QueryParam("url") String url, @QueryParam("term") String term) {
		String jsonNetwork = "{'id':'1','name':'Semantic Web', 'children':[{'id':'1_1', 'name':'Ontology', 'children':[{'id':'2_1', 'name':'OWL', 'children':[{	'id':'3_1', 'name':'XML','children':[]}]},{}]},{'id':'1_2', 'name': 'Predicate Logic', 'children':[]}],	'data':{'relation': 'uses ontology, represented by Predicate Logic'}}";
		return jsonNetwork;
	}

}