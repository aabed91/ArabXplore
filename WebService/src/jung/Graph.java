package jung;

import java.util.HashSet;
import java.util.Hashtable;

import wikipedia.WikiPage;

public class Graph {

	private HashSet<WikiPage> nodes = new HashSet<WikiPage>() ;
	private Hashtable<WikiPage, HashSet<WikiPage>> outgoing_links = new Hashtable<WikiPage, HashSet<WikiPage>>();
	private Hashtable<WikiPage, HashSet<WikiPage>> incoming_links = new Hashtable<WikiPage, HashSet<WikiPage>>();
	private int count_links = 0 ;
	
	public boolean addNode ( WikiPage node ) {
		if ( nodes.contains(node) ) return false ;
		
		nodes.add(node) ;
		if ( !outgoing_links.containsKey ( node ) ) {
			outgoing_links.put ( node, new HashSet<WikiPage>() ) ;
		}
		if ( !incoming_links.containsKey ( node ) ) {
			incoming_links.put ( node, new HashSet<WikiPage>() ) ;
		}
		return true ;
	}
	
	public boolean addLink ( WikiPage source, WikiPage destination ) {
		if ( source.equals( destination ) ) return false ;

		addNode ( source ) ;
		addNode ( destination ) ;
		
		if ( outgoing_links.get ( source ).contains( destination ) ) {
			return false ;
		}
		
		outgoing_links.get ( source ).add( destination ) ;
		incoming_links.get ( destination ).add( source ) ;
		count_links++ ;

		return true ;
	}
	
	public int countNodes() {
		return nodes.size() ;
	}
	
	public int countLinks() {
		return count_links ;
	}
	
	public int countOutgoingLinks(WikiPage node) {
		return outgoing_links.get(node).size() ;
	}

	public HashSet<WikiPage> getIncomingLinks(WikiPage node) {
		return incoming_links.get(node) ;
	}
	
	public HashSet<WikiPage> getNodes() {
		return nodes ;
	}
}
