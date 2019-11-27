package ovgu.ir;

import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		if (args!=null && (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0])))) {
			System.out.println("Usage:\tjava -jar IR P.jar [path to document folder] [path to index folder] [VS/OK] [query]");
			System.exit(0);
		}

		
		String docsPath=Constants.FILE_PATH;//(args[0]!=null && !args[0].isEmpty())?args[0]:Constants.FILE_PATH;//Constants.FILE_PATH; //args[0];
		String IndexPath =Constants.INDEX_PATH;//(args[0]!=null && !args[0].isEmpty())? args[1]:Constants.INDEX_PATH;//Constants.INDEX_PATH; //args[1];
		boolean useBM25Okapi = true;//(args[2]!=null && !args[2].isEmpty() && args[2].equals("OK")) ? true : false;//true;//
		String query ="";//(args[3]!=null && !args[3].isEmpty())?args[3]:""; //"";
		List<Model> docList= new ArrayList<Model>();
		ParseDocs ps = new ParseDocs();
		docList=ps.parseFiles(docsPath);
		
	   
		SearchDocs searchDocs= new SearchDocs();
		 Index index = new Index();
	       
	      
	    try {
	    	
	    	index.index(IndexPath,Constants.STEM_PATH, Constants.OPEN_MODE,docList);
			searchDocs.search(IndexPath, query, useBM25Okapi, "contents");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
