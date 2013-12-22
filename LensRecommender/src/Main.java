import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.ItemScorer;
import org.grouplens.lenskit.Recommender;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.baseline.BaselineScorer;
import org.grouplens.lenskit.baseline.ItemMeanRatingItemScorer;
import org.grouplens.lenskit.baseline.UserMeanBaseline;
import org.grouplens.lenskit.baseline.UserMeanItemScorer;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.SimpleFileRatingDAO;
import org.grouplens.lenskit.knn.item.ItemItemScorer;
import org.grouplens.lenskit.scored.ScoredId;
import org.grouplens.lenskit.transform.normalize.BaselineSubtractingUserVectorNormalizer;
import org.grouplens.lenskit.transform.normalize.UserVectorNormalizer;



public abstract class Main{
     
      public static void main(String args[]) throws IOException, RecommenderBuildException
      {

  String name;
  
  Scanner scanner= new Scanner(System.in);
  System.out.println("Hello, what's your name?");
  name=scanner.nextLine();
  
  System.out.printf("How many games do you want me to find? ");
  int noOfRecommendations=scanner.nextInt();
  
  int myRating;
  PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("/home/sonja/workspace/LensRecommender/Resources/ratings.txt",true)));
  File gamesFile = new File("/home/sonja/workspace/LensRecommender/Resources/gamesID.txt");
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
  
  for(int k=0;k<10;k++)
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
  
  EventDAO dataSource = new SimpleFileRatingDAO(new File("/home/sonja/workspace/LensRecommender/Resources/ratings.txt"), ",");
  LenskitConfiguration config = new LenskitConfiguration();
  config.bind(EventDAO.class)
        .to(dataSource);
  
  ///////////////////////////////////////////////////////////////////////7
  
//Here, we want an item-item recommender and rating predictor.
  config.bind(ItemScorer.class)
        .to(ItemItemScorer.class);

  // let's use personalized mean rating as the baseline predictor. 2-step process:
  // First, use the user mean rating as the baseline scorer
  config.bind(BaselineScorer.class, ItemScorer.class)
         .to(UserMeanItemScorer.class);
  // Second, use the item mean rating as the base for user means
  config.bind(UserMeanBaseline.class, ItemScorer.class)
        .to(ItemMeanRatingItemScorer.class);
  // and normalize ratings by baseline prior to computing similarities
  // This one has 3 parameters because it uses a role - UserVectorNormalizer -
  // to restrict what kind of vector normalizer we're talking about.

  // There are more parameters, roles, and components that can be set. See the
  // JavaDoc for each recommender algorithm for more information.

  // Now that we have a factory, build a recommender from the configuration
  // and data source. This will compute the similarity matrix and return a recommender
  // that uses it.
  Recommender rec = LenskitRecommender.build(config);

  // we want to recommend items
  ItemRecommender irec = rec.getItemRecommender();
  System.out.println("Ecco: " + irec);
  System.out.println("GLOBAL: " + rec.getGlobalItemRecommender());//null
  System.out.println("GLOBAL SCORER: " + rec.getGlobalItemScorer());//null
  assert irec != null; // not null because we configured one

      // get 10 recommendation for the user
      List<ScoredId> recs = irec.recommend(myUserID, 10);
      System.out.format("Recommendations for %d:\n", myUserID);
      for (ScoredId item: recs) {
          System.out.format("\t%d\n", item.getId());
      }
  //////////////////////////////////////////////////////////////////////

      System.out.printf("\n\nDear %s, I recommend you the following games: \n\n", name);

      //reading again the games file to find the titles of the recommended games
      BufferedReader titleBr = new BufferedReader(new FileReader(gamesFile));
      String TitleLine = titleBr.readLine();
      int l = 0;
      
      while (TitleLine != null)
      {
    	  for (ScoredId item: recs)
    	  {
    		  if(l<noOfRecommendations && TitleLine.endsWith(","+item.getId()))
    		  	{
    			  gameFirstComma=TitleLine.indexOf(","); 
    			  gameSecondComma=TitleLine.indexOf(",", gameFirstComma+1);
    			  gameName=TitleLine.substring(gameFirstComma+1, gameSecondComma);
    			  System.out.println(gameName); 
    			  l++;
    		  	}
    	  }
    	  TitleLine=titleBr.readLine();
    }
      
      titleBr.close();
  

  /* configure a normalizer and baseline predictor */
/*  config.bind(UserVectorNormalizer.class)
        .to((Class<? extends UserVectorNormalizer>) BaselineSubtractingUserVectorNormalizer.class);
  config.bind(BaselineScorer.class, ItemScorer.class)
        .to(ItemMeanRatingItemScorer.class);//QUI!!!
*/
      }
                 
}