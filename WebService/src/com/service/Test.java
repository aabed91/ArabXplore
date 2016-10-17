package com.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import jung.PlainPageRank;
import utils.Constants;
import utils.Utils;
import wikipedia.WikiPage;

public class Test {


    public static String getWikipediaPageUrl(int pageId){
		String pageUrl = "";
		
		try {
			
			JSONObject jsonObject = readJsonFromUrl("https://ar.wikipedia.org/w/api.php?action=query&prop=info&pageids="+pageId+"&inprop=url&format=json");
			JSONObject jsonObject2 = jsonObject.getJSONObject("query");
			JSONObject jsObject = jsonObject2.getJSONObject("pages");
			JSONObject jsonObject3 = jsObject.getJSONObject(String.valueOf(pageId));
			pageUrl = jsonObject3.getString("fullurl");
			
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pageUrl;
	}
    private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	    
  }
  
  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONObject json = new JSONObject(jsonText);
	      return json;
	    } finally {
	      is.close();
	    }
	    
  }
    public static void main(String[] args) {
        try {
        	File file = new File(Constants.TIME_TEST_PATH+"2.txt");
        	if(!file.exists()){
        		file.createNewFile();
        	}
			Files.write(Paths.get(Constants.TIME_TEST_PATH+"2.txt"), "tttt \n".getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}