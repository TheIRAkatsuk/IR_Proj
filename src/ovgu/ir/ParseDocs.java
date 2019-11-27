package ovgu.ir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class ParseDocs {

	public List<Model> parseFiles(String docsPath) {
		// TODO Auto-generated method stub
		ParseDocs listFiles = new ParseDocs();
		System.out.println("Reading Parsing all the files under and adding to stemmed folder : "+docsPath);
		List<Model> list=listFiles.listAllFiles(docsPath);
return list;
	}

	// Uses Files.walk method
	public List<Model> listAllFiles(String path) {
		List<Model> list = new ArrayList<Model>();
		//System.out.println("In listAllfiles(String path) method");
		File filepos = new File(System.getProperty("user.dir") + "/stemmed");
		if (filepos.listFiles() != null) {
			for (File file : filepos.listFiles()) {
				if (!file.isDirectory()) {
					file.delete();
				}
			}
		}
		try (Stream<Path> paths = Files.walk(Paths.get(path))) {
			paths.forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					try {
						
						Long lastModified = 0L;
						FilterDocs filter = new FilterDocs();
						String title = "";
						
						if(filter.checkDocType(filePath).equals("text")){
							ParseTextDocs textdoc = new ParseTextDocs();
							textdoc.readContent(filePath);
							File file = new File(filePath.toString());
							lastModified= file.lastModified();
						}
						else if(filter.checkDocType(filePath).equals("html")){
							ParseHTMLDocs htmldoc = new ParseHTMLDocs();
							htmldoc.readContent(filePath);
							title = htmldoc.readTitle();
							lastModified= htmldoc.lastModified;
						}
						else{
							System.out.println("Invalid Document Types. Please include only HTML or Text Documents in docs folder");
						}
						Model model = new Model();
						model.setActualPath(System.getProperty("user.dir")+"\\"+filePath);
						Date dt = new Date(lastModified);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						model.setModified(sdf.format(dt).toString());
						TextStemmer ts = new TextStemmer();
						List<String> ls = new ArrayList<>();
						ls.add(title);
						List<String> result = ts.getStemmedValue(ls);
						model.setTitle(result.isEmpty()?"":result.get(0));
						model.setActTitle(title);
						list.add(model);
//						Index index = new Index();
//						index.index(Constants.INDEX_PATH,filePath.getFileName().toString() , Constants.OPEN_MODE,lastModified,title,System.getProperty("user.dir")+"/docs"+filePath.getFileName());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	

}
