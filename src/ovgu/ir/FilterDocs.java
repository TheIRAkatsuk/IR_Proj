package ovgu.ir;

import java.nio.file.Path;

public class FilterDocs {

	public String checkDocType(Path file){
		String type = null;
		if(file.getFileName().toString().endsWith(".txt")){
			type="text";
		}
		else if(file.getFileName().toString().endsWith(".htm") || file.getFileName().toString().endsWith(".html")){
			type="html";
		}
		else{
			type="none";
		}
		return type;
	}
}
