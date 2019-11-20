package ovgu.ir;

import org.apache.lucene.index.IndexWriterConfig.OpenMode;

public class Constants {
	   public static final String FILE_PATH = "docs";
	   public static final String STEM_PATH = "stemmed";
	   public static final String INDEX_PATH = "index";
	   public static final String FIELD_PATH = "path";
		public static final String FIELD_CONTENTS = "contents";
		public final static OpenMode OPEN_MODE = OpenMode.CREATE; 
}
