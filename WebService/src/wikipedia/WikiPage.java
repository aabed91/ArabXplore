package wikipedia;


public class WikiPage implements Comparable<WikiPage>{

	String id;
	String url;
	double pageRankScore;
	double hitScore;
	double jump;
	
	
	
	
	public double getJump() {
		return jump;
	}

	public void setJump(double jump) {
		this.jump = jump;
	}

	public double getHitScore() {
		return hitScore;
	}

	public void setHitScore(double hitScore) {
		this.hitScore = hitScore;
	}

	public WikiPage() {
	}

	public WikiPage(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return getId();
	}

	public double getPageRankScore() {
		return pageRankScore;
	}

	public void setPageRankScore(double pageRankScore) {
		this.pageRankScore = pageRankScore;
	}

	@Override
	public int compareTo(WikiPage page2) {
		if(this.getPageRankScore()-page2.getPageRankScore()>=0){
			return -1;
		}else{
			return 1;
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
