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
    public static final int elite = (int)(0.05 * population); // Percentage of solutions to clone
    public static final int rounds = population-1;      //number of rounds that each player plays in a generation
    public static final int movesTotal = 100;           //each round contains 100 moves (each move == cooperate/defect)

    // Allocate memory to store solutions + associated score and fitness
    public static int[][] solutions = new int[population][chromosomes]; // 2D array storing a chromosome for each player of the "population"
    public static int[] score = new int[population];                 // 1D array storing the score of each player in a generation
    public static double[] fitness = new double[population];            // 1D array storing the "fitness" of each player in the "population"

    //start main method
    public static void main(String[] args)
    {
        // Open output files
        PrintWriter outputFile = null;
        try
        {
            outputFile = new PrintWriter(new FileOutputStream("Prisoners.txt",false));
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File error.  Program aborted.");
            System.exit(0);
        }

        PrintWriter currentBestAsList = null;
        try
        {
            currentBestAsList = new PrintWriter(new FileOutputStream("CurrentBestAsList.txt",false));
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File error.  Program aborted.");
            System.exit(0);
        }

        PrintWriter currentBestAsArray = null;
        try
        {
            currentBestAsArray = new PrintWriter(new FileOutputStream("CurrentBestAsArray.txt",false));
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File error.  Program aborted.");
            System.exit(0);
        }

        // Initialize population randomly
        for (int i = 0; i < population; i++)
        {
            for (int j = 0; j < chromosomes; j++)
            {
                double bit = Math.random();
                if (bit < 0.5)
                    solutions[i][j] = 0;
                else
                    solutions[i][j] = 1;
            }
            score[i] = 0;
            fitness[i] = 0.;
        }

        // Output
        System.out.println("Generation 0");
        for (int i = 0; i < population; i++)
        {
            printChromosome(solutions, i);
        }

        // Evaluate initial fitness
        fitness();

        // Keep track of best solutions in a given generation
        int[] best = new int[elite];
        for (int i = 0; i < elite; i++)
        {
            best[i] = 0;
        }

        // Start generation loop
        for (int i = 1; i < generations; i++)
        {
            // Worst fitness in current generation
            double minFitness = 1.;
            for (int k = 0; k < population; k++)
            {
                if (fitness[k] < minFitness)
                    minFitness = fitness[k];
            }
            //System.out.println("Min fitness = " + df.format(minFitness));

            // Temporary memory allocation for next generation
            int[][] tmp = new int[population][chromosomes];

            // Create new population
            int count = 0;
            while (count < population)
            {
                // Selection by roulette wheel
                int[] parents = rouletteWheel(Math.min(minFitness, -1.));
                //printChromosome(solutions,parents[0]);
                //printChromosome(solutions,parents[1]);

                // Two-point crossover
                if (Math.random() < pc)
                {
                    for (int j = 0 ; j<chromosomes; j++)
                    {
                        tmp[count][j] = solutions[parents[0]][j];
                        tmp[count+1][j] = solutions[parents[1]][j];
                    }

                    int point1 = (int)(Math.random()*chromosomes);
                    int point2 = (int)(Math.random()*chromosomes);

                    if (point2 < point1)
                    {
                        int temp = point1;
                        point1 = point2;
                        point2 = temp;
                    }

                    if (point1 != point2)
                    {
                        for (int w=point1; w<=point2; w++)
                        {
                            tmp[count+1][w] = solutions[parents[0]][w];
                            tmp[count][w] = solutions[parents[1]][w];
                        }
                    }

                    else
                    {
                        tmp[count+1][point1] = solutions[parents[0]][point1];
                        tmp[count][point1] = solutions[parents[1]][point1];
                    }

                }

                else
                {
                    for (int j = 0 ; j<chromosomes; j++)
                    {
                        tmp[count][j] = solutions[parents[0]][j];
                        tmp[count+1][j] = solutions[parents[1]][j];
                    }
                }


                // Mutation
                for (int j = 0; j<chromosomes; j++)
                {
                    if (Math.random() <= pm)
                    {
                        tmp[count][j] = (int) (Math.random()*2);
                    }

                    if (Math.random() <= pm)
                    {
                        tmp[count+1][j] = (int) (Math.random()*2);
                    }
                }

                // Advance count by 2 as we have added two children i.e. two new
                // rows in tmp[][].
                count = count + 2;
            }


            // Copy tmp to solutions
            for (int j = 0; j < population; j++)
            {
                if (fitness[j] < fitness[best[elite-1]]) // Keep elites
                {
                    System.arraycopy(tmp[j], 0, solutions[j], 0, chromosomes);
                }
            }


            // Update objective function
            fitness();


            // Calculate average fitness of population and output
            double sumScore = 0.0;
            double maxScoreGen = population * rounds * movesTotal * 3.0; //maximum collective score in a generation

            //***note*** (max collective score) is not equal to (max individual score)*population
            //because everyone cannot get 5pts at each move. If a player gets 5, his opponent has 0.
            //the max collective score would result in each person obtaining 3 at each move (cooperation+cooperation)

            for (int j = 0; j < population; j++){
                sumScore += score[j];
            }

            double avgFitness = sumScore / maxScoreGen;

            // Find elite solutions and output best
            double maxFitness = -1e3;
            for (int j = 0; j < population; j++)
            {
                if (fitness[j] > maxFitness)
                {
                    maxFitness = fitness[j];
                    best[0] = j;
                }
            }
            for (int getBest = 1; getBest < elite; getBest++)
            {
                maxFitness = -1e3;
                for (int j = 0; j < population; j++)
                {
                    if (fitness[j] > maxFitness && fitness[j] < fitness[best[getBest-1]])
                    {
                        maxFitness = fitness[j];
                        best[getBest] = j;
                    }
                }
            }


            // Output
            if (i%10==0)
            {
                System.out.println();
                System.out.println("Generation " + i);
                System.out.println("Avg fitness = " + df.format(avgFitness));
                System.out.println("Max fitness = " + df.format(fitness[best[0]]));
                System.out.println("Elites:");
                for (int print = 0; print < elite; print ++)
                {
                    System.out.println(best[print] + "\t" + df.format(fitness[best[print]]));
                }

                outputFile.printf("%d\t%1.6e\t%1.6e\r\n",i,avgFitness,fitness[best[0]]);
                currentBestAsList.printf("{");
                for(int p = 0; p < chromosomes; p++)
                    currentBestAsList.printf("%d,",solutions[best[0]][p]);
                currentBestAsList.printf("}\n");

                currentBestAsArray.printf("\n");
                for(int p = 0; p < chromosomes; p++)
                {
                    currentBestAsArray.printf("%d",solutions[best[0]][p]);
                }
                currentBestAsArray.printf("\n");

                outputFile.flush();
                currentBestAsList.flush();
                currentBestAsArray.flush();
            }
        }

        for (int i = 0; i < population; i++)
        {
            printChromosome(solutions, i);
        }

        System.out.println("Best strategy after " + generations + " generations:");
        printChromosome(solutions,best[0]);

        outputFile.close();
        currentBestAsList.close();
        currentBestAsArray.close();
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
                    for (int i=0; i<4; i++)
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
        //set minFitness of the population to the origin (to zero) of the reference frame
        //add up the fitness of all the population
        double sum = 0.0;
        for (int i = 0; i < population; i++){
            sum += fitness[i] - minFitness;
        }

        int[] indices = new int[2];
        double luckyNumber, findParent;
        int search;

        // Spin number 1 to get first parent
        luckyNumber = Math.random();
        findParent = 0.0;
        search = 0;
        while (findParent <= luckyNumber && search < fitness.length)
        {
            findParent += (fitness[search] - minFitness) / sum;
            //the bigger the fitness of a player with the index "search",
            //the more chance it has of covering the luckyNumber and thus of being selected as parent
            search ++;
        }

        indices[0] = search - 1;
        //System.out.println("Parent 1 = " + indices[0]);

        // Spin number 2 to get second parent
        luckyNumber = Math.random();
        findParent = 0.0;
        search = 0;
        while (findParent <= luckyNumber && search < fitness.length)
        {
            findParent += (fitness[search] - minFitness) / sum;
            search ++;
        }

        indices[1] = search - 1;
        //System.out.println("Parent 2 = " + indices[1]);

        return indices;
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

	
	
	