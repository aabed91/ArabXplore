package jung;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import wikipedia.WikiPage;

public class BiasePageRank {
	private Graph graph = new Graph() ;
	private HashMap<WikiPage, Double> pagerank_current = new HashMap<WikiPage, Double>() ; 
	private Map<WikiPage, Double> pagerank_new = new HashMap<WikiPage, Double>() ; 
	private Map<WikiPage,List<WikiPage>> pageMap;
	
	private double dumping_factor ;
	private int iterations ;
	
	public BiasePageRank(Map<WikiPage,List<WikiPage>> pageMap,double dumping_factor, int iterations){
		
		this.pageMap = pageMap;
		this.dumping_factor = dumping_factor ;
		this.iterations = iterations ;
		
		
		load_data ( pageMap ) ;
		initialize_pagerank() ;
			
		
	}
	
	private void load_data(Map<WikiPage,List<WikiPage>> pageMap){
		Iterator<WikiPage> iter = pageMap.keySet().iterator();
		
		while(iter.hasNext()){
			WikiPage source = iter.next();
			graph.addNode(source);
			
			HashSet<WikiPage> seen = new HashSet<WikiPage>() ;
			Iterator<WikiPage> iter2 = pageMap.get(source).iterator();
			while(iter2.hasNext()){
				WikiPage destination = iter2.next();
				if (destination != source){
					graph.addNode(destination);
					graph.addLink(source, destination);
					System.out.println("Source: "+source.getUrl()+" | Destiniation: "+destination.getUrl());

				}
				seen.add(destination);
			}
		}
	}
	
	private void initialize_pagerank() {
		Double initial_pagerank = ( 1.0d / graph.countNodes() ) ; 
		for ( WikiPage node : graph.getNodes() ) {
			pagerank_current.put ( node, initial_pagerank ) ;
		}
	}
	
	public HashMap<WikiPage, Double> compute() {
		
		for ( WikiPage node : graph.getNodes() ) {
//			System.out.println("NODE: "+node.getUrl()+" - HitScore: "+node.getHitScore());
			double otherSumHitScores = 0;
			for ( WikiPage source : graph.getIncomingLinks(node) ) {
				otherSumHitScores+=source.getHitScore();
//				System.out.println("INLINK: "+source.getUrl());
			}
			double jump =1d;
			if(otherSumHitScores != 0){
				jump = node.getHitScore()/otherSumHitScores;
				node.setJump(jump);

			}
//			System.out.println("NODE: "+node.getUrl()+" - HitScore: "+node.getHitScore() +" - OtherHS: "+otherSumHitScores+" - Jump: "+node.getJump());

		}
		
		
		
		double teleport = ( 1.0d - dumping_factor ) / graph.countNodes() ;
		
		//The main loop
		for (int i = 0; i < iterations; i++) {
			System.out.println("iteration " + i);
			double dangling_nodes = 0.0d ;
			for ( WikiPage node : graph.getNodes() ) {
				if ( graph.countOutgoingLinks(node) == 0 ) {
					dangling_nodes += pagerank_current.get ( node ) ;
				}
			}
			dangling_nodes = ( dumping_factor * dangling_nodes ) / graph.countNodes() ;
			
			//Summation
			for ( WikiPage node : graph.getNodes() ) {
				double r = 0.0d ; 
				//props(e`,e)
				for ( WikiPage source : graph.getIncomingLinks(node) ) {
					//props(e`,e) / o(e`)
					r += pagerank_current.get ( source ) / graph.countOutgoingLinks ( source ) ; 
					
				}
			
				
				//final equation
//				r = ((1-dumping_factor)*node.getJump()) + dumping_factor * r + dangling_nodes + teleport ;
				r = dumping_factor * r + dangling_nodes + teleport ;	
//				r = ((1-dumping_factor)*node.getHitScore()) + dumping_factor * r + dangling_nodes + teleport ;
				
				
				
//				r = node.getHitScore() * (dumping_factor * r + dangling_nodes + teleport) ;
				pagerank_new.put ( node, r ) ;
			}
			for ( WikiPage node : graph.getNodes() ) {
				pagerank_current.put ( node, pagerank_new.get ( node ) ) ;
			}
		}

		return pagerank_current ;
	}
}
