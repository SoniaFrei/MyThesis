import java.io.*;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.AbstractDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;


public abstract class VideogamesRecommender extends AbstractDataModel{
     
      public static void main(String args[]) throws IOException
      {

  String name;
  
  Scanner scanner= new Scanner(System.in);
  System.out.println("Hello, what's your name?");
  name=scanner.nextLine();
  
  System.out.printf("How many games do you want me to find? ");
  int noOfRecommendations=scanner.nextInt();
  
  int myRating;
  PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("/home/sonja/workspace/VideogamesRS/Resources/ratings.txt",true)));
  File gamesFile = new File("/home/sonja/workspace/VideogamesRS/Resources/gamesID.txt");
  BufferedReader gamesBr = new BufferedReader(new FileReader(gamesFile));
  String gamesLine = gamesBr.readLine();
  String gameName;
  int gameFirstComma;
  int gameSecondComma;
  int myUserID=2; //poi devo cambiarlo!!!
  String asin, gameID;
  
  //numero a caso per non iniziare sempre dai primi giochi che sono solo scacchi!
  for(int n=0;n<1062;n++)
	  gamesLine = gamesBr.readLine();
  
  for(int k=0;k<10;k++)	//quanti giochi chiedo di votare prima di iniziare
	  {
			  gameFirstComma=gamesLine.indexOf(",");
			  gameSecondComma=gamesLine.indexOf(",", gameFirstComma+1);
			  gameName=gamesLine.substring(gameFirstComma+1, gameSecondComma);
			  System.out.println("Give me your rating for the game \""+ gameName +"\", please: (1-5)");
			  myRating=scanner.nextInt();
			  asin=gamesLine.substring(0,gameFirstComma);
			  System.out.println("ASIN: "+asin);
			  gameID=Game.getID(asin);
			  System.out.println("\nGameID: " +gameID);
			  
			  //save the rating only if the value is valid
			  if(myRating<6 && myRating>0)
			  {
				  pw.println(myUserID + ","+ gameID + ","+ myRating);	
			  }
		      gamesLine = gamesBr.readLine();
	  }
  pw.close();
  gamesBr.close();


 
          
  /*******let's start the recommendation******/
      
          BasicConfigurator.configure();

           // int noOfRecommendations=10;
            long recGames[]=new long[noOfRecommendations];
           
            try
            {
                  // Data model with the user ratings
                  FileDataModel dataModel = new FileDataModel(new File("/home/sonja/workspace/VideogamesRS/Resources/ratings.txt"));

                  ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(dataModel);
                  
                  //System.out.println("Preferenze: " + dataModel.getPreferencesFromUser(4105));
                  
                  // Create a recommender based on item similarity
                  Recommender recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);
                            
                  //devo fare in modo che lo faccia solo finchè ci sono abbastanza recommendations da dare,
                  //altrimenti mi dà errore Array out of bound exception --> provo con un try catch?
                 for(int l=0;l<noOfRecommendations;l++)
                 {
                	 //save the IDs of the recommended games into the array recGames[]
                	 recGames[l]=recommender.recommend(myUserID, noOfRecommendations).get(l).getItemID();
                	 //System.out.println("REC: " + recGames[l]);
                 }
                 
                 System.out.printf("\n\nDear %s, I recommend you the following games: \n\n", name);

                 //reading again the games file to find the titles of the recommended games
                 BufferedReader titleBr = new BufferedReader(new FileReader(gamesFile));
                 String TitleLine = titleBr.readLine();
                 
                 while (TitleLine != null)
                 {
               	  for(int l=0;l<noOfRecommendations;l++)
               	  {
               		  if(TitleLine.endsWith(","+recGames[l]))
               		  	{
               			  gameFirstComma=TitleLine.indexOf(","); 
               			  gameSecondComma=TitleLine.indexOf(",", gameFirstComma+1);
               			  gameName=TitleLine.substring(gameFirstComma+1, gameSecondComma);
               			  System.out.println(gameName); 
               			  //System.out.println(recGames[l]);
               		  	}
               	  }
               	  TitleLine=titleBr.readLine();
                 }
                 
                 titleBr.close();
                   
            } catch (TasteException e) {
                  e.printStackTrace();
            }
      }
                 
}
