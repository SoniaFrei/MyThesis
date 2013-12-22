import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//prendi il file con i giochi già ordinati ed eliminati i doppioni per ASIN e ci attacchi un id da 1 in poi
//es: "B004Xblabla, Tomb Raider, 1
public class Appender {
	
	public static void main (String[] args) throws IOException{
		
		File games = new File("/home/sonja/UNI/THESIS/sortedGames.txt");
		int counter=1;
		String newline;
		
		FileWriter gamesWriter = new FileWriter("/home/sonja/UNI/THESIS/gamesID.txt", true);
		  
		BufferedReader gamesBr = new BufferedReader(new FileReader(games));
		String line = gamesBr.readLine();
		
		
		   while(line != null)
		    {
		    newline = line + counter + "\n";
		    gamesWriter.write(newline);
			counter++;
			line=gamesBr.readLine();
		    }
		   gamesWriter.close();
		   gamesBr.close();

	}

}
