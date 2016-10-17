package utils;

/*
	
# Arabic light stemmer

A command line version of the Arabic light stemmer, which is implemented in Apache lucene https://lucene.apache.org 

Light stemming for Arabic words is to remove common affix (prefix and suffix) from words, but it does not convert words into their root form.  

Version 1.0

Author: Motaz Saad (motaz dot saad at gmail dot com)

This software is a modification of the Arabic light stemmer. The original implementation is available at https://lucene.apache.org/


Arabic light stemming algorithm is described in: 

Larkey, Leah S., Lisa Ballesteros, and Margaret E. Connell. "Light stemming for Arabic information retrieval." Arabic computational morphology. Springer Netherlands, 2007. 221-243.‏
	
*/

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.lucene.analysis.ar.ArabicNormalizer;
import org.apache.lucene.analysis.ar.ArabicStemmer;


public class LightStemmer {

   /**
    * @param args the command line arguments
    */
   public String stem(String in_text) {
       StringBuilder sbuf = new StringBuilder();
       String[] tokens = in_text.split("\\s");
       String tt = "";
       for (int j = 0; j < tokens.length; j++) {
           String t = tokens[j];
           String resut = lightStem(t);	// Light stemming algorithm 
           sbuf.append(resut).append(" ");
           tt += resut + " ";
       }
       return sbuf.toString();
   }

   private  String lightStem(String string) {
       ArabicNormalizer arabicNorm = new ArabicNormalizer();
       char[] c = string.toCharArray();
       int len = c.length;
       len = arabicNorm.normalize(c, len);
       char[] normalizedWord = new char[len];
       System.arraycopy(c, 0, normalizedWord, 0, len);

       ArabicStemmer araLightStemmer = new ArabicStemmer();
       len = araLightStemmer.stem(normalizedWord, len);
       char[] lightWord = new char[len];
       System.arraycopy(normalizedWord, 0, lightWord, 0, len);

       StringBuilder sbuf = new StringBuilder();
       sbuf.append(lightWord);

       String result = sbuf.toString();
       return result;
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

}

