package ovgu.ir;

public class Main {
	public static void main(String[] args) {
		
		String docsPath= Constants.FILE_PATH;
		String IndexPath = Constants.INDEX_PATH;

		ParseDocs ps = new ParseDocs();
		ps.parseFiles(docsPath);
		SearchDocs searchDocs= new SearchDocs();
	    try {
			searchDocs.search(Constants.INDEX_PATH, "", true, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
