package khoja;

import java.io.BufferedReader;
import java.io.File;
import khoja.ArabicStemmerKhoja;
import utils.FileUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Stemx {

    /**
     * @param args
     */
    private static final String lineSeparator = System.getProperty("line.separator");
    private  List<String> stopwordsList;

    public String[] lineSep(String paragraph) {

        //final Pattern p = Pattern.compile("[\\،\\.\\!\\?]\\s+", Pattern.MULTILINE);
        final Pattern p = Pattern.compile("[\\.\\!\\?]\\s+", Pattern.MULTILINE);
        //final int value = p.split(paragraph).length;
        //System.out.println("Number Of Sentences: " + value);
        return p.split(paragraph);
    }
   

    public String stem(String in_text)/* throws FileNotFoundException, UnsupportedEncodingException, IOException*/ {

        ArabicStemmerKhoja mystemmer = new ArabicStemmerKhoja();
        load_stop_words();
        StringBuilder sbuf = new StringBuilder();
        
        String[] tokens = in_text.split("\\s");
        for (int j = 0; j < tokens.length; j++) {
            String t = tokens[j];
            if (!stopwordsList.contains(t)) {	// ignore stopwords 
                String resut = mystemmer.stem(t);	// Khoja rooting algorithm 
                sbuf.append(resut).append(" ");
            }
        }
        return sbuf.toString();

    }

    public List<String> getAllTerms(String doc_contents) {
        List<String> allTerms = new ArrayList<String>();
        String[] tokenizedTerms = doc_contents.split("\\s");   //to get individual terms
        for (String term : tokenizedTerms) {
            if (!allTerms.contains(term)) {  //avoid duplicate entry
                allTerms.add(term);
                //System.out.println(term);
            }
        }
        //termsDocsArray.add(tokenizedTerms);
        return allTerms;
    }

    

    public String preProccessing(String s) {
        s = s.trim();
        Pattern p = Pattern.compile("[\\s]+");
        Matcher m = p.matcher(" ");
        m.reset(s);
        String result = m.replaceAll(" ");
        result = result.replaceAll("\"", "");
        return result;
    }

    public void load_stop_words() {
        try {
            FileUtils fr = new FileUtils();
            String stopwords = fr.readEntilerFile("/Users/aabed91/Documents/workspace-jee/SimpleService/data/sw1.txt");
            List<String> stopwordsList1 = Arrays.asList(stopwords.split("\n"));
            stopwords = fr.readEntilerFile("/Users/aabed91/Documents/workspace-jee/SimpleService/data/sw2.txt");
            List<String> stopwordsList2 = Arrays.asList(stopwords.split("\n"));
            //List<String> stpwrd = new ArrayList<String>(stopwordsList1);
            stopwordsList = new ArrayList<String>(stopwordsList1);
            stopwordsList.addAll(stopwordsList2);
            System.out.println(stopwordsList.size());
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    public String stopWordRemoval(String line) {
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
}

