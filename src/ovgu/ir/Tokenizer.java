package ovgu.ir;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class Tokenizer {
	public List<String> tokenizeList(List<String> fullList,Analyzer analyzer) {
		CharArraySet stopWords = EnglishAnalyzer.getDefaultStopSet();
        List<String> result = new ArrayList<String>();
        for (String string : fullList) {
        	 TokenStream stream  = analyzer.tokenStream(null, string);
             stream = new StopFilter(stream, stopWords);
             try {
             	stream.reset();
                 while(stream.incrementToken()) {
                     result.add(stream.getAttribute(CharTermAttribute.class).toString());
                 }
                 stream.close();
             }
             catch(IOException e) {
                 // not thrown b/c we're using a string reader...
             }
             

		}
       
        return result;
    }  
}
