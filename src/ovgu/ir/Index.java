package ovgu.ir;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.Term;

public class Index {
	
	public void index(String INDEX_DIR, String DOCS_DIR, OpenMode OPEN_MODE, long lastModified) throws IOException {
		IndexWriter writer = createWriter(INDEX_DIR, OPEN_MODE);
		Path path = Paths.get(DOCS_DIR);
		
        Date start = new Date();
        IndexDocs(writer, path,lastModified);
        
        writer.close();
        
        Date end = new Date();
        
        System.out.println("Indexing time in ms: " + (end.getTime() - start.getTime()));
	}
	public void IndexDocs(IndexWriter writer, Path file, long lastModified) throws IOException{
		try (InputStream stream = Files.newInputStream(file)) {
			Document doc = new Document();
			Field pathField = new StringField("path", file.toString(), Field.Store.YES);
			doc.add(pathField);
			doc.add(new LongPoint("modified", lastModified));
			doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));
//			  BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
//
//		            StringBuilder sb = new StringBuilder();
//
//		            String line;
//		            while ((line = br.readLine()) != null) {
//
//		                sb.append(line);
//		                sb.append(System.lineSeparator());
//		            }
//
//		           System.out.println(sb);
			if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
				System.out.println("adding " + file);
				writer.addDocument(doc);
			} else {
				System.out.println("updating " + file);
				writer.updateDocument(new Term("path", file.toString()), doc);
			}
		}
	}
	private IndexWriter createWriter(String INDEX_DIR, OpenMode OPEN_MODE) throws IOException {
		FSDirectory dir = FSDirectory.open(Paths.get(INDEX_DIR));
	    IndexWriterConfig config = new IndexWriterConfig(new EnglishAnalyzer());
	    config.setOpenMode(OPEN_MODE);
	    IndexWriter writer = new IndexWriter(dir, config);
	    return writer;
	}	

}
