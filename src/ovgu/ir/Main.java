package ovgu.ir;

import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		if (args!=null && (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0])))) {
			System.out.println("Usage:\tjava -jar IR P.jar [path to document folder] ");
			System.exit(0);
		}

		
		String docsPath= Constants.FILE_PATH;//(args[0]!=null && !args[0].isEmpty())?args[0]:Constants.FILE_PATH; //args[0];
		String IndexPath =Constants.INDEX_PATH;
		String query ="";
		List<Model> docList= new ArrayList<Model>();
		ParseDocs ps = new ParseDocs();
		docList=ps.parseFiles(docsPath);
		
	   
		SearchDocs searchDocs= new SearchDocs();
		 Index index = new Index();
	       
	      
	    try {
	    	
	    	index.index(IndexPath,Constants.STEM_PATH, Constants.OPEN_MODE,docList);
			searchDocs.search(IndexPath, query, true, "contents");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
