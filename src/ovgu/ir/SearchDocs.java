package ovgu.ir;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class SearchDocs {

	/** Simple command-line based search demo. */
	public void search(String INDEX_DIR, String queryString, boolean value, String field) throws Exception {

		// has been Paths.get(index) - commented out in line 35
		IndexReader reader = DirectoryReader
				.open(FSDirectory.open(Paths.get(System.getProperty("user.dir"), INDEX_DIR)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();

		// QueryParser parser = new QueryParser(field, analyzer);
		MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[] { "contents", "title", "modified" },
				analyzer);
		Scanner scan = new Scanner(System.in);

		while (true) {
			if (queryString.isEmpty()) { // prompt the user
				System.out.println("Enter query: ");

				queryString = scan.nextLine().trim();

				if (queryString.isEmpty()) {
					break;
				}
			}
			// String special = "contents:" + queryString + " OR title:" +
			// queryString+ " OR modified:" + queryString;
			// Query query = parser.parse(queryString);
			System.out.println("Searching for: " + queryString);
			TextStemmer tx = new TextStemmer();
			List<String> ls = new ArrayList<String>();
			ls.add(queryString);
			List<String> stemList = new ArrayList<String>();
			stemList = tx.getStemmedValue(ls);
			StringBuilder sb = new StringBuilder();
			for (String object : stemList) {
				sb.append(object + " ");
			}
			Query stemQuery = parser.parse(sb.toString());
			doPagingSearch(scan, searcher, stemQuery, 10, true);

			queryString = "";
		}
		reader.close();
	}

	private void doPagingSearch(Scanner scan, IndexSearcher searcher, Query query, int hitsPerPage, boolean interactive)
			throws IOException {

		// Collect enough docs to show 5 pages
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		// TopDocsCollector<ScoreDoc> collector = searcher.search(query, 5 *
		// hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;
		// System.out.println(hits[0]);
		int numTotalHits = Math.toIntExact(results.totalHits.value);
		System.out.println(numTotalHits + " total matching documents");

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage);
		int ind = 0;
		while (true) {
			// System.out.println(hits[ind]);
			if (end > hits.length) {
				System.out.println("Only results 1 - " + hits.length + " of " + numTotalHits
						+ " total matching documents collected.");
				System.out.println("Collect more (y/n) ?");
				String line = scan.nextLine();
				if (line.length() == 0 || line.charAt(0) == 'n') {
					break;
				}

				hits = searcher.search(query, numTotalHits).scoreDocs;
			}

			end = Math.min(hits.length, start + hitsPerPage);

			for (int i = start; i < end; i++) {
				/*
				 * if (raw) { // output raw format System.out.println("doc=" +
				 * hits[i].doc + " score=" + hits[i].score); continue; }
				 */

				Document doc = searcher.doc(hits[i].doc);
				String path = doc.get("ActualPath");
				if (path != null) {
					System.out.println("Rank: " + (i + 1) + "--Path: " + path);
					String title = doc.get("title");
					if (title != null && !title.isEmpty()) {
						System.out.println("   Title: " + doc.get("Acttitle"));
					}
					String modified = doc.get("modified");
					if (modified != null) {

						
						System.out.println("   Last Modified: " + modified);
						
					}
					System.out.println("   Document score: "+hits[ind].score);
					ind++;
				} else {
					System.out.println((i + 1) + ". " + "No path for this document");
				}

			}

			if (!interactive || end == 0) {
				break;
			}

			if (numTotalHits >= end) {
				boolean quit = false;
				while (true) {

					System.out.print("Press ");
					if (start - hitsPerPage >= 0) {
						System.out.print("(p)revious page, ");
					}
					if (start + hitsPerPage < numTotalHits) {
						System.out.print("(n)ext page, ");
					}
					System.out.println("(q)uit to search different or enter number to jump to a page.");

					String line = scan.nextLine();
					if (line.length() == 0 || line.charAt(0) == 'q') {
						quit = true;
						break;
					}
					if (line.charAt(0) == 'p') {
						start = Math.max(0, start - hitsPerPage);
						break;
					} else if (line.charAt(0) == 'n') {
						if (start + hitsPerPage < numTotalHits) {
							start += hitsPerPage;
						}
						break;
					} else {
						int page = Integer.parseInt(line);
						if ((page - 1) * hitsPerPage < numTotalHits) {
							start = (page - 1) * hitsPerPage;
							break;
						} else {
							System.out.println("No such page");
						}
					}
				}
				if (quit)
					break;
				end = Math.min(numTotalHits, start + hitsPerPage);
			}
		}
	}
}
