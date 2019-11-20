package ovgu.ir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ParseHTMLDocs {
	public Document htmlDoc= null;
	public Long lastModified= 0L;
	public String fileTitle=null;
	public String fileBody = null;

	public void parseHTML(Path filePath){
		try {
			File file = new File(filePath.toString());
			lastModified = file.lastModified();
			htmlDoc = Jsoup.parse(file,"UTF-8");
			//System.out.println("Title is : "+htmlDoc.title());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String readBodyContent(){
		fileBody= htmlDoc.body().text();
		System.out.println("Body is : "+fileBody);
		return fileBody;
	}
	public String readTitle(){
		fileTitle = htmlDoc.title();
		return fileTitle;
	}
}
