// Import packages
import java.io.*;
import java.text.*;
import java.util.*;

public class PrisonersDilemma
{
	public static int situation(int[] history) //gives the gene number
    {
		/*
			Reminder: 
			history[0] == player's first move, history[1] == opponent's second move
			history[2] == player's second move, history[3] == opponent's second move
			history[4] == player's third move, history[5] == opponent's third move
		*/
		int situation = 0;
		
		/*in determining the first three moves of a round, 
		   we don't care about the previous moves of the player in question himself, 
		   we only care about the previous moves of the player's opponent (which are history[3] and history[5])
        */
		if (history[5] == -1) //first move of the round hasn't been made yet
			situation = 0;
		else if (history[3] == -1 && history[5] == 1) //first move of opponent was cooperate
			situation = 1;
		else if (history[3] == -1 && history[5] == 0) //first move of opponent was defect
			situation = 2;
		else if (history[1] == -1 && history[3] == 1 && history[5] == 1) //opponent's 1st move && 2nd move == cooperate
			situation = 3;
		else if (history[1] == -1 && history[3] == 1 && history[5] == 0) //opponent's 1st move == cooperate, 2nd move == defect
			situation = 4;
		else if (history[1] == -1 && history[3] == 0 && history[5] == 1) //opponent's 1st move == defect, 2nd move == cooperate
			situation = 5;
		else if (history[1] == -1 && history[3] == 0 && history[5] == 0) //opponent's 1st move && 2nd move == defect
			situation = 6;
		
        /*in determining the fourth move up until the last, 
		  we care about the previous moves of both the player himself and his opponent
        */
        else 
		{
			situation = 7;
			
			if (history[0] == 1)
				situation += 0;
			else if (history[0] == 0)
				situation += Math.pow(2,5);

			if (history[1] == 1)
				situation += 0;
			else if(history[1] == 0)
				situation += Math.pow(2,4);

			if (history[2] == 1)
				situation += 0;
			else if(history[2] == 0)
				situation += Math.pow(2,3);

			if (history[3] == 1)
				situation += 0;
			else if(history[3] == 0)
				situation += Math.pow(2,2);

			if (history[4] == 1)
				situation += 0;
			else if(history[4] == 0)
				situation += Math.pow(2,1);
			
			if (history[5] == 1)
				situation += 0;
			else if (history[5] == 0)
				situation += Math.pow(2,0);
		}

        return situation;
    }
}
	