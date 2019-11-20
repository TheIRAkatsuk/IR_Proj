package ovgu.ir;

import java.io.File;
import java.io.FileWriter;
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

	public void parseFiles(String docsPath) {
		// TODO Auto-generated method stub
		 ParseDocs listFiles = new ParseDocs();
	      System.out.println("reading files Java8 - Using Files.walk() method");
	      listFiles.listAllFiles(docsPath);

		}
     // Uses Files.walk method   
     public void listAllFiles(String path){
         System.out.println("In listAllfiles(String path) method");
         try(Stream<Path> paths = Files.walk(Paths.get(path))) {
             paths.forEach(filePath -> {
                 if (Files.isRegularFile(filePath)) {
                     try {
                         readContent(filePath);
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
     }
     

     public void readContent(Path filePath) throws IOException{
    	 List<String> fileList= new ArrayList<>();
         System.out.println("read file " + filePath);
         String[] arr = filePath.toString().split("\\\\");
         File filepos = new File("/stemmed");
         File currentFile = new File(filepos.getPath(),arr[arr.length-1]);
         currentFile.delete();
         ParseHTMLDocs html = new ParseHTMLDocs();
         if(filePath.getFileName().toString().endsWith(".txt")){
        	 fileList= Files.readAllLines(filePath) ;
             System.out.println("File List is : " + fileList);
             

         }
         else{
        	 
        	 html.parseHTML(filePath);
        	 fileList.add(html.readBodyContent());
        	 Date d = new Date(html.lastModified);
        	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	 System.out.println("Last Modified date is : "+sdf.format(d));
         }
         
         TextStemmer ts = new TextStemmer();
         List<String> result = ts.getStemmedValue(fileList);
         System.out.println("stemmed list is : " + result);
         File newDirectory = new File("/stemmed");
         //Create directory for non existed path.
         boolean isCreated = newDirectory.mkdirs();

         
         File newFile = new File("/stemmed" + File.separator + arr[arr.length-1]);
         //Create new file under specified directory
         isCreated = newFile.createNewFile();
         if (isCreated) {
        	 FileWriter fileWriter = new FileWriter("/stemmed" + File.separator + arr[arr.length-1]);
        	 for (String string : result) {
        		 fileWriter.write(string+" ");
			}
        	   
        	    fileWriter.close();
             System.out.printf("\n2. Successfully created new file, path:%s",
                     newFile.getCanonicalPath());
         } else { //File may already exist
             System.out.printf("\n2. Unable to create new file");
         }
       Index index = new Index();
       index.index(Constants.INDEX_PATH,  newFile.getCanonicalPath(), Constants.OPEN_MODE,html.lastModified);
       
     }
     
	}


