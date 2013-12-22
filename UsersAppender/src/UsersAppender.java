import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//add a number from 1 onwards to the file usersID
public class UsersAppender {
	
	public static void main (String[] args) throws IOException{
		
		File users = new File("/home/sonja/UNI/THESIS/sortedUsers2.txt");
		int counter=1;
		String newline;
		
		FileWriter usersWriter = new FileWriter("/home/sonja/UNI/THESIS/usersID2.txt", true);
		  
		BufferedReader usersBr = new BufferedReader(new FileReader(users));
		String line = usersBr.readLine();
		
		
		   while(line != null)
		    {
		    newline = line + counter + "\n";
		    usersWriter.write(newline);
			counter++;
			line=usersBr.readLine();
		    }
		   usersWriter.close();
		   usersBr.close();

	}

}
