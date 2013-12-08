import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Game {
	
	public static String getID(String asin) throws IOException {
		
		String id="0";
		BufferedReader bf = new BufferedReader(new FileReader("/home/sonja/workspace/VideogamesRS/Resources/gamesID.txt"));
		String line;
		int firstCommaIndex;
		int secondCommaIndex;
	
	while (( line = bf.readLine()) != null)
	{
		int indexfound = line.indexOf(asin);	//is -1 if the asin is not found in the line
		if (indexfound > -1) {
			System.out.println("Trovatoooo!!!game\n");
			System.out.println(line);
			firstCommaIndex=line.indexOf(",");
			secondCommaIndex=line.indexOf(",", firstCommaIndex+1);
			id=line.substring(secondCommaIndex+1);
			System.out.println("VIrgola: " + firstCommaIndex + ", " + secondCommaIndex);
		}
	}
	bf.close();
	return id;
}
}
