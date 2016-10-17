package utils;

import java.util.regex.Pattern;

public class Constants {
	public static final String DATA_PATH = "/Users/aabed91/Downloads/Compressed/stanford-segmenter-2015-12-09/data";
	public static final String SAMPLE_DATA_PATH = "/Users/aabed91/Downloads/Compressed/stanford-segmenter-2015-12-09/test.simp.utf8";
	
	public static final String CUSTOM_SEARCH_API_KEY = "AIzaSyD-bjYbOOfHT9uiGKQcfrfzY2HQINk0KgE";
	
	public static final String INDEXING_FILE_PATH = "/Users/aabed91/Desktop/Ahmed/MasterThesis/LuceneDirectory";
	
	public static final String STOP_WORD_PATH = "/Users/aabed91/Documents/workspace-jee/SimpleService/data/sw1.txt";
	
	public static final String DATASET_PATH ="/Users/aabed91/Desktop/Ahmed/MasterThesis/RankResults/dataset.txt";
	
	public static final double TFIDF_THRESHOLD = 13;
	public static final double IMPORTANCE_THRESHOLD = 0.04;
	
	public static final Pattern TAG_REGEX = Pattern.compile("[[(.+?)]]");
	
	public static final int GRAM_NUMBER = 3;
	public static final int SNIPPETS_NUMBER = 20;
	
	public static final String TEST_DATASET_PATH = "/Users/aabed91/Desktop/Ahmed/MasterThesis/TimeTest/dataset.txt";
	public static final String TIME_TEST_PATH = "/Users/aabed91/Desktop/Ahmed/MasterThesis/TimeTest/";
}
