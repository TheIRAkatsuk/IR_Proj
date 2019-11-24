package ovgu.ir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParseTextDocs {
	public void readContent(Path filePath) throws IOException {
		List<String> fileList = new ArrayList<>();
		//System.out.println("read file " + filePath);
		String[] arr = filePath.toString().split("\\\\");
		fileList = Files.readAllLines(filePath);
		//System.out.println("File List is : " + fileList);
		TextStemmer ts = new TextStemmer();
		List<String> result = ts.getStemmedValue(fileList);
		//System.out.println("stemmed list is : " + result);
		File newDirectory = new File(System.getProperty("user.dir") + "/stemmed");
		// Create directory for non existed path.
		boolean isCreated = newDirectory.mkdirs();
		File newFile = new File(System.getProperty("user.dir") + "/stemmed" + File.separator + arr[arr.length - 1]);
		// Create new file under specified directory
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
}
