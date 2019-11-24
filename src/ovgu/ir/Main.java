package ovgu.ir;

import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
			System.out.println("Usage:\tjava -jar IR P.jar [path to document folder] [path to index folder] [VS/OK] [query]");
			System.exit(0);
		}

		
		String docsPath=args[0];// Constants.FILE_PATH;
		String IndexPath =args[1];// Constants.INDEX_PATH;
		boolean useBM25Okapi = args[2].equals("OK") ? true : false;
		String query = args[3];
		List<Model> docList= new ArrayList<Model>();
		ParseDocs ps = new ParseDocs();
		docList=ps.parseFiles(docsPath);
		
	   
		SearchDocs searchDocs= new SearchDocs();
		 Index index = new Index();
	       
	      
	    try {
	    	
	    	index.index(Constants.INDEX_PATH,Constants.STEM_PATH, Constants.OPEN_MODE,docList);
			searchDocs.search(Constants.INDEX_PATH, query, useBM25Okapi, "contents");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
