package com.service;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import de.tudarmstadt.ukp.wikipedia.api.Page;
import utils.Constants;
import wikipedia.WikipediaInit;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class LuceneExamples2 {
	
	public static void main(String[] args) {
		//indexDirectory();
		System.out.println("Done");
		search("طريق");
		//printInlinks("1");
	}	
		
	public static void indexDirectory() {		
		 //Apache Lucene Indexing Directory .txt files     
	     try {	
		 //indexing directory
	  

	     Path path = Paths.get(Constants.INDEXING_FILE_PATH);
		 Directory directory = FSDirectory.open(path);
		 
	     CharArraySet stopSet = CharArraySet.copy(StandardAnalyzer.STOP_WORDS_SET);
	     stopSet.clear();
	     Scanner scanner = new Scanner(new File(Constants.STOP_WORD_PATH));
	     while(scanner.hasNextLine()){	
	     	stopSet.add(scanner.nextLine().trim());
	     }
	     scanner.close();
	     StandardAnalyzer analyzer = new StandardAnalyzer(stopSet);
	     IndexWriterConfig config = new IndexWriterConfig(analyzer);		
		 IndexWriter indexWriter = new IndexWriter(directory, config);
		 indexWriter.deleteAll();
		 Iterable<Page> pages = WikipediaInit.getInstance().getAllWikipediaPages();
		 
		 
		 
		 int i=0;
		     for (Page content : pages) {
		    	 
					
					System.out.println(i);
		    	 	Document doc = new Document();
		    	 	try{
					doc.add(new TextField("path", String.valueOf(content.getPageId()), Store.YES));
					
					doc.add(new TextField("contents", content.getPlainText(), Store.YES));
		    	 	}catch(Exception e){
		    	 		e.printStackTrace();
		    	 	}
					//doc.add(new TextField("inlinks", "1 2 3 4 5 66", Store.YES));
					//doc.add(new TextField("outlinks", "1 2 3 4 5 66", Store.YES));
					//doc.add(new TextField("numInlinks", "234", Store.YES));
					//doc.add(new TextField("numOutlinks", "567", Store.YES));

					indexWriter.addDocument(doc);
					
					i++;
					
		     }	 		  
		     indexWriter.close();		    
		     directory.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}					
	}
	
	public static void printInlinks(String pageId){
		try {	
			Path path = Paths.get(Constants.INDEXING_FILE_PATH);
			Directory directory = FSDirectory.open(path);		
			IndexReader indexReader =  DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			
		    TermQuery query = new TermQuery(new Term("path", pageId));
			TopDocs topDocs = indexSearcher.search(query,indexReader.numDocs());
	        System.out.println("totalHits " + topDocs.totalHits);
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {			
			    Document document = indexSearcher.doc(scoreDoc.doc);
			    System.out.println("path " + document.get("path"));
			    System.out.println("inlinks " + document.get("inlinks"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static int search(String text) {	
		//Apache Lucene searching text inside .txt files
		try {	
			Path path = Paths.get(Constants.INDEXING_FILE_PATH);
			Directory directory = FSDirectory.open(path);		
			IndexReader indexReader =  DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			QueryParser queryParser = new QueryParser("contents",  new StandardAnalyzer());  
			queryParser.setDefaultOperator(QueryParser.Operator.AND);
			queryParser.setPhraseSlop(0);
			Query query = queryParser.parse(text);
			TopDocs topDocs = indexSearcher.search(query, 1000000000);
	        return topDocs.totalHits;
//			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {			
//			    Document document = indexSearcher.doc(scoreDoc.doc);
//			    System.out.println("path " + document.get("path"));
//			    System.out.println("content " + document.get("contents"));
//			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}				
	}
  }
