package ovgu.ir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ParseHTMLDocs {
	public Document htmlDoc = null;
	public Long lastModified = 0L;
	public String fileTitle = null;
	public String fileBody = null;

	public void readContent(Path filePath) throws IOException {
		List<String> fileList = new ArrayList<>();
		//System.out.println("read file " + filePath);
		String[] arr = filePath.toString().split("\\\\");
		parseHTML(filePath);
		fileList.add(readBodyContent());
//		Date d = new Date(lastModified);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//System.out.println("Last Modified date is : " + sdf.format(d));
		TextStemmer ts = new TextStemmer();
		List<String> result = ts.getStemmedValue(fileList);
		//System.out.println("stemmed list is : " + result);
		File newDirectory = new File(System.getProperty("user.dir") + "/stemmed");
		// Create directory for non existed path.
		boolean isCreated = newDirectory.mkdirs();
		File newFile = new File(System.getProperty("user.dir") + "/stemmed" + File.separator + arr[arr.length - 1]);
		isCreated = newFile.createNewFile();
		if (isCreated) {
			FileWriter fileWriter = new FileWriter(
					System.getProperty("user.dir") + "/stemmed" + File.separator + arr[arr.length - 1]);
			for (String string : result) {
				fileWriter.write(string + " ");
			}

			fileWriter.close();
			//System.out.printf("\n2. Successfully created new file, path:%s", newFile.getCanonicalPath());
		} else {
			System.out.printf("\n2. Unable to create new file");
		}

	}

	public void parseHTML(Path filePath) {
		try {
			File file = new File(filePath.toString());
			lastModified = file.lastModified();
			htmlDoc = Jsoup.parse(file, "UTF-8");
			// System.out.println("Title is : "+htmlDoc.title());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String readBodyContent() {
		fileBody = htmlDoc.body().text();
		//System.out.println("Body is : " + fileBody);
		return fileBody;
	}

	public String readTitle() {
		fileTitle = htmlDoc.title();
		return fileTitle;
	}
}
