package com.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PageTerm {
	List<String> alternatives = new ArrayList<String>();
	
	public PageTerm(String reg){
		if(reg.contains("|")){
			Collections.addAll(alternatives, reg.split("\\|"));
		}else{
			alternatives.add(reg.trim());
		}
	}
	
	public List<String> getAlternatives(){
		return this.alternatives;
	}
	
	@Override
	public boolean equals(Object obj) {
		PageTerm term2 = (PageTerm) obj;
		for(String s1: this.getAlternatives()){
			System.out.println(s1);

		}
		for(String s1: term2.getAlternatives()){
			System.out.println(s1);

		}
		return !(Collections.disjoint(this.getAlternatives(), term2.getAlternatives()));
	}
	
	
	public static void main(String args[]){
		PageTerm t1 = new PageTerm("البحر الأبيض المتوسط|البحر المتوسط");
		PageTerm t2 = new PageTerm("البحر المتوسط");
		PageTerm t3 = new PageTerm("البحر الأبيض المتوسط");
		
		System.out.println(t1.equals(t2));
		System.out.println(t1.equals(t3));


	}
}
