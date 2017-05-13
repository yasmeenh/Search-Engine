import java.util.Vector;

public class Word {
	String word;
	String type;
	Integer url;
	String position;
	String steamword;
	int repeated;
	boolean updated;
	
	public Word(String word, String type, Integer url,String position,String steam,int rep,boolean update) {
		super();
		this.word = word;
		this.type = type;
		this.url = url;
		this.position = position;
		this.repeated = rep;
		this.steamword=steam;
		this.updated=update;
		
	}
	public Word(){
		
	}

}
