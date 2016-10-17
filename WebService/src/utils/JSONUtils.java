package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.service.GoogleResult;

public class JSONUtils {

	public static ArrayList<GoogleResult> getGoogleSnippetResult(String keyWord){
		
		ArrayList<GoogleResult> results = new ArrayList<GoogleResult>();
		GoogleResult googleResult = new GoogleResult();
		
		try {
			keyWord = URLEncoder.encode(keyWord, "UTF-8");
			for (int k = 1 ; k <=11; k+=10){
				JSONObject json = readJsonFromUrl("https://www.googleapis.com/customsearch/v1?key="+Constants.CUSTOM_SEARCH_API_KEY+"&cx=013036536707430787589:_pqjad5hr1a&q="+keyWord+"&alt=json&hl=ar&start="+k);
				//JSONObject responseDate = json.getJSONObject("responseData");
				JSONArray result = json.getJSONArray("items");
				for (int i=0; i < result.length(); i++){
					googleResult = new GoogleResult();
					JSONObject res = result.getJSONObject(i);
					//System.out.println("URL "+i+":"+res.getString("url"));
					//System.out.println("title "+i+":"+res.getString("title"));
					//System.out.println("content"+i+":"+res.getString("content"));
					
					googleResult.setUrl(res.getString("formattedUrl"));
					googleResult.setTitle(res.getString("title"));
					googleResult.setContent(res.getString("snippet"));
					
					results.add(googleResult);
				}
				//System.out.println(json.toString());
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return results;
	}
	
	
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
}
