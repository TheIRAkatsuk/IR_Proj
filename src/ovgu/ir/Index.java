package ovgu.ir;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.List;

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
	
	public void index(String INDEX_DIR, String DOCS_DIR, OpenMode OPEN_MODE,List<Model> list) throws IOException {
		IndexWriter writer = createWriter(INDEX_DIR, OPEN_MODE);
		Path path = Paths.get(System.getProperty("user.dir"),DOCS_DIR);
        Date start = new Date();
        IndexDocs(writer, path,list);
        
        writer.close();
        
        Date end = new Date();
        
        System.out.println("Indexing time in ms: " + (end.getTime() - start.getTime()));
	}
	public void IndexDocs(IndexWriter writer, Path file,List<Model> list) throws IOException{
		if (Files.isDirectory(file)) {
        	Files.walkFileTree(file, new SimpleFileVisitor<Path>() {
        		@Override
        		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    			try (InputStream stream = Files.newInputStream(file)) {
    				FilterDocs filter = new FilterDocs();
    				if(filter.checkDocType(file).equals("text") || filter.checkDocType(file).equals("html")){
    					String title= "";
    					String actPath= "";
    					Long modified= 0L;
    					for (Model model : list) {
    						String[] arr = model.getActualPath().toString().split("\\\\");
    						String[] toCheck = file.toString().split("\\\\");
							if(arr[arr.length-1].equals(toCheck[toCheck.length-1])){
								title=model.getTitle();
								actPath= model.getActualPath();
								modified= model.getModified();
								break;
							}
						}
    				Document doc = new Document();
    				Field pathField = new StringField("path", file.toString(), Field.Store.YES);
    				doc.add(pathField);
    				doc.add(new StringField("title", title , Field.Store.YES));
    				doc.add(new StringField("ActualPath", actPath , Field.Store.YES));
    				doc.add(new StringField("modified",modified.toString() ,Field.Store.YES));
    				doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));
    				if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
    					System.out.println("adding " + file);
    					writer.addDocument(doc);
    				} else {
    					System.out.println("updating " + file);
    					writer.updateDocument(new Term("path", file.toString()), doc);
    				}
    			}
    			}
    			return FileVisitResult.CONTINUE;
        		}
        	});
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
