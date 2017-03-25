import java.util.Vector;

public class Word {
	String word;
	String type;
	Integer url;
	String position;
	String steamword;
	int repeated;
	
	public Word(String word, String type, Integer url,String position,String steam,int rep) {
		super();
		this.word = word;
		this.type = type;
		this.url = url;
		this.position = position;
		this.repeated = rep;
		this.steamword=steam;
		
	}
	public Word(){
		
	}

}
