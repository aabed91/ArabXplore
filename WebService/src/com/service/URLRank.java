package com.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import de.tudarmstadt.ukp.wikipedia.api.Page;

public class URLRank {
	private String word;
	private String url;
	private Set<Integer> ranks;
	private Page page;
	private int hiScore = 0;
	private int id;
	private Set<Integer> outlinks;
	
	
	
	public Set<Integer> getOutlinks() {
		return outlinks;
	}

	public void setOutlinks(Set<Integer> outlinks) {
		this.outlinks = outlinks;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHiScore() {
		return hiScore;
	}

	public void setHiScore(int hiScore) {
		this.hiScore = hiScore;
	}

	public URLRank(){
		ranks = new TreeSet<Integer>();
	}
	
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Set<Integer> getRanks() {
		return ranks;
	}
	public void addRank(int rank) {
		ranks.add(rank);
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	
	
	
	
}
