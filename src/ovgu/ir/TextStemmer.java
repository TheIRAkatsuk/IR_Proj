package ovgu.ir;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.tartarus.snowball.ext.PorterStemmer;


public class TextStemmer {
	
	public List<String> getStemmedValue(List<String> termList){
		Tokenizer tk = new Tokenizer();
		Analyzer az = new StandardAnalyzer();
		List<String> resultList = new ArrayList<>();
		resultList=tk.tokenizeList(termList, az);
		 PorterStemmer stemmer = new PorterStemmer();
		 List<String> ls = new ArrayList<String>();
		 for (String term : resultList) {
			 stemmer.setCurrent(term);
			stemmer.stem();
			String result = stemmer.getCurrent();
			ls.add(result);
		}
		 return ls;
	}

}
