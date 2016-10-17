package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.lucene.analysis.ar.ArabicNormalizer;

import edu.stanford.nlp.international.arabic.process.ArabicSegmenter;
import khoja.Stemx;

public class PreProcessing {
	private static PreProcessing instance;
	private List<String> stopwordsList;
	ArabicSegmenter arabicSegmenter;
	
	public static PreProcessing getInstance(){
		if(instance == null){
			instance = new PreProcessing();
			System.out.println("FAILLLLLLLLLLL *******");
		}
		return instance;
	}
	
	private PreProcessing(){
		Properties properties = new Properties();
		properties.setProperty("sighanCorporaDict", Constants.DATA_PATH);
		properties.setProperty("serDictionary", Constants.DATA_PATH + "dict-chris6.ser.gz");
		properties.setProperty("testFile", Constants.SAMPLE_DATA_PATH);
		properties.setProperty("inputEncoding", "UTF-8");
		properties.setProperty("sighanPostProcessing", "true");
		
		arabicSegmenter = new ArabicSegmenter(properties);
		arabicSegmenter.loadSegmenter(Constants.DATA_PATH+"/arabic-segmenter-atb+bn+arztrain.ser.gz");
	}
	public static String applyArabicNormalization(String text){
		
		ArabicNormalizer arabicNorm = new ArabicNormalizer();
        char[] c = text.toCharArray();
        int len = c.length;
        len = arabicNorm.normalize(c, len);
        char[] normalizedWord = new char[len];
        System.arraycopy(c, 0, normalizedWord, 0, len);
        String input_text =new String(normalizedWord);
        
        return input_text;
	}
	
	
	public static String applyKhojaStemmer(String text){
		
		Stemx s = new Stemx();///intialize stem class
		
        String khoja_stem_result = s.stem(text);
        
        return khoja_stem_result;
        
	}
	
	public static String applyLighterStemmer(String text){
		
		Stemx s = new Stemx();///intialize stem class
        
		LightStemmer ss = new LightStemmer();
        String light_stem_result = ss.stem(text);
        
        return light_stem_result;
        
	}
	
	public void load_stop_words() {
        try {
            FileUtils fr = new FileUtils();
            String stopwords = fr.readEntilerFile("/Users/aabed91/Documents/workspace-jee/SimpleService/data/sw1.txt");
            List<String> stopwordsList1 = Arrays.asList(stopwords.split("\n"));
            stopwords = fr.readEntilerFile("/Users/aabed91/Documents/workspace-jee/SimpleService/data/sw2.txt");
            List<String> stopwordsList2 = Arrays.asList(stopwords.split("\n"));
            //List<String> stpwrd = new ArrayList<String>(stopwordsList1);
            stopwordsList = new ArrayList<String>();
            for (String s: stopwordsList1){
            	stopwordsList.add(s.replace("\n", "").replace("\r", ""));
            }
            
            for (String s: stopwordsList2){
            	stopwordsList.add(s.replace("\n", "").replace("\r", ""));
            }
        } catch (Exception e) {
        }
    }

    public String stopWordRemoval(String line) {
    	load_stop_words();
        System.out.println(stopwordsList.size());
        StringBuilder sbuf = new StringBuilder();
        String[] tokens = line.split("\\s");
        for (int j = 0; j < tokens.length; j++) {
            String t = tokens[j];
            try{
            if (!stopwordsList.contains(t)) {	// ignore stopwords 
                sbuf.append(t).append(" ");
            }
            }catch(Exception ex){
                //System.out.println(ex.toString());
            }
        }
        return sbuf.toString();
    }
    
    public String segmentString(String line){
    	
    	
		
		return arabicSegmenter.segmentString(line);
    }
    
    public static String removeHTMLTag(String sneppets){
    	return sneppets.replace("<b>","").replace("</b>", "").replace(",", "").replace(".", "").replace(")", "").replace("(", "");
    }
    
    public static String removeNumbers(String sneppets){
    	return sneppets.replace("0", "").replace("1", "").replace("2", "").replace("3", "").replace("4", "").replace("5", "").replace("6", "").replace("7", "").replace("8", "").replace("9", "");
    }
    
}
