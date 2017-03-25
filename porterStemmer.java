
import org.tartarus.snowball.ext.PorterStemmer;

public class porterStemmer {
	
	porterStemmer()
	{
		
	}
	
	static String stemTerm (String term) {
		PorterStemmer stemmer = new PorterStemmer();
        stemmer.setCurrent(term);
        stemmer.stem();
        return stemmer.getCurrent();
	}
}