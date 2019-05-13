// Import packages
import java.io.*;
import java.text.*;
import java.util.*;

public class PrisonersDilemma
{
	// Formatter
    public static final DecimalFormat df = new DecimalFormat("+#.##%;-#.##%");


    // Declare parameters and constants
    public static final double pc = 0.9;                // Probability of crossover
    public static double pm = 0.005;                    // Probability of mutation 
    public static final int population = 20;           // Population size (represents number of players) (must be even)
    public static final int chromosomes = 71;          // Chromosome length (number of possible game histories)
    public static final int generations = 10000;         // Number of generations
    public static final int elite = (int)(0.5 * population); // Percentage of solutions to clone
	public static final int rounds = population-1;      //number of rounds that each player plays in a generation
	public static final int movesTotal = 100;           //each round contains 100 moves (each move == cooperate/defect)

    // Allocate memory to store solutions + associated score and fitness
    public static int[][] solutions = new int[population][chromosomes]; // 2D array storing a chromosome for each player of the "population"
	public static double[] score = new int[population];                 // 1D array storing the score of each player in a generation
    public static double[] fitness = new double[population];            // 1D array storing the "fitness" of each player in the "population"
	
	//start main method
	public static void main(String[] args)
    {
	}
	
	public static void fitness()
    {
		//Evaluate score
		score();
		
		double maxScorePlayer = rounds * movesTotal * 5.0; //maximum score that each player can get in a generation (max 5 pts at every move)
		
		for (int i = 0; i < population; i++)
        {
			fitness[i] = score[i]/maxScorePlayer;
        }
		
	}
	
	public static void score()
	{
		//re-initialize score of all population to 0
		
		for (int i = 0; i < population; i++)
        {
			score[i] = 0;
        }
		
		//each player plays a round with every other player in the population
		// -1 stands for no history, 0 for defect, 1 for cooperate
		for (int player1 = 0; player1<population; player1++)
		{		
			for (int player2 = player1+1; player2<population; player2++) 
			{
				int[] history1 = new int[6]; //history for player1
				int[] history2 = new int[6]; //history for player2
				/*
				The 6 integers in the array "history" represent (in order):
				player's first move, opponent's second move
				player's second move, opponent's second move
				player's third move, opponent's third move
				*/
				
				//at the beginning of a round, initialize all history to no history(-1)
				for (int i=0; i<6; i++)
				{
					history1[i] = -1; //history for player1
					history2[i] = -1; //history for player2
				} 
				
				for (int moves = 0; moves < movesTotal; moves++) 
				{
					int genePlayer1 = situation(history1);
					int genePlayer2 = situation(history2);
					int movePlayer1 = solutions[player1][genePlayer1];
					int movePlayer2 = solutions[player2][genePlayer2];
					
					//Update history after player1 and player2 both make moves
					for (i=0; i<4; i++)
					{
						history1[i] = history1[i+2];
						history2[i] = history2[i+2];
					}
					history1[4] = movePlayer1;
					history1[5] = movePlayer2;
					history2[4] = movePlayer2;
					history2[5] = movePlayer1;
					
					
					/*
						Update the scores
						If both cooperate, +3 for both. If both defect, +1 for both.
						If one cooperates and one defects, +0 for the one who cooperates and +5 for the one who defects.
					*/
					
					if (movePlayer1==1 && movePlayer2==1) //if both cooperate
					{
						score[player1] += 3;
						score[player2] += 3;	
					}
					else if (movePlayer1==0 && movePlayer2==0) //if both defect
					{
						score[player1] += 1;
						score[player2] += 1;	
					}
					else if (movePlayer1==1 && movePlayer2==0) //if player1 cooperates and player2 defects
					{
						score[player1] += 0;
						score[player2] += 5;	
					}
					else if (movePlayer1==0 && movePlayer2==1) //if player1 defects and player2 cooperates
					{
						score[player1] += 5;
						score[player2] += 0;	
					}
				}
			}
		}
	}
	
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
	
	//The rouletteWheel() method selects next parents based on fitness.
	public static int[] rouletteWheel(double minFitness)
    {
	}
	
	public static void printChromosome(int[][] array, int index)
    {
        System.out.print("\t");

        for (int j = 0; j < chromosomes; j++){
            System.out.print(array[index][j]);
        }

        System.out.println();
    }
}

	
	
	