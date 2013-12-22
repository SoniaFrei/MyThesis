import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//from the csv file with all the reviews, create a file usersToSort.txt with a list of user asins (not sorted)
public class Users {

	public static void main(String[] args) throws IOException {

		File extractedReviews = new File("/home/sonja/UNI/THESIS/reviews.txt");
		File userFile = new File("/home/sonja/UNI/THESIS/usersToSort2.txt");
		int index;
		int userIndex;
		int startIndex;
		int userStartIndex;
		int userFinishIndex;
		String userID;
		int indexProva;
		int index2;
		int index3;
		int index4;
		int index5;
		int index6;
		
		FileWriter userWriter = new FileWriter(userFile, true);
		  
		BufferedReader revBr = new BufferedReader(new FileReader(extractedReviews));
		String line = revBr.readLine();
		      while (line != null) {	    	  
		    		  index=line.indexOf("\"XX\",\"");
		    		  startIndex=index+6;
		    		  userIndex=line.indexOf("\",\"", startIndex+17);
		    		  userStartIndex=userIndex+3;
		    		  userFinishIndex=line.indexOf("\"", userStartIndex);
		    		  
		    		  indexProva=line.indexOf("\",\"");
		    		  index2=line.indexOf("\",\"", indexProva+3);
		    		  index3=line.indexOf("\",\"", index2+3);
		    		  index4=line.indexOf("\",\"", index3+3);
		    		  index5=line.indexOf("\",\"", index4+3);
		    		  index6=line.indexOf("\",\"", index5+3);
		    		  userID=line.substring(index5+3,index6);
		    		  
		    		  //userID=line.substring(userStartIndex,userFinishIndex);
		    	      userWriter.write(userID + "," + "\n");
		    		  line = revBr.readLine();
		    		  userWriter.flush();
		      }
		      revBr.close();
    	      userWriter.close();

}
}
