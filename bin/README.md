# Code

Place the code you developped for the term project in this folder. Add any instructions/documentation required to run the code, and what results can be expected in this *README* file.


A) In the boolean section at the beginning:

1. Can decide to keep elites or not.
Suggestion: do not keep elites, as after not many generations the genes of the elites will dominate the population, 
and all the population would end up by having the nearly exact same genes

2. Can choose to add the strategies "TIT FOR TAT", "all cooperate", and "all defect" in the initial strategies (in the initial population)
It doesn't seem to change much, but may be interesting.


B) In the history section:
The 6 integers in the array "history" represent (in order):
player's first move, opponent's second move
player's second move, opponent's second move
player's third move, opponent's third move


C) In the situation section:
The number of situations (of different histories) was supposed to be 2^6 = 64, because there are 6 integers in history,
which are all either 0 or 1 (either defect or cooperate). 
However, for the three first games of a round, there is no history of the three past games.
So 7 new situations were added to account for this problem. The integer -1 is used to represent no history (neither defect nor cooperate).
Situation 1: no history.  
Situation 2: opponent's last move was Cooperate
Situation 3: opponent's last move was Defect
Situation 4: opponent's last two moves were C and C
Situation 5: opponent's last two moves were C and D
Situation 6: opponent's last two moves were D and C
Situation 7: opponent's last two moves were D and D


D) The game rules:
If both cooperate, +3 for each.
If both defect, +1 for each.
If one cooperates and one defects, +0 for cooperating one and +5 for defecting one.


E) Winning rate of the best strategy
While running the program, you can choose to evaluate the winning rate of the best strategy against 1000 random strategies.
This lets you have an vague idea of whether the strategy is good or not. 

****HOWEVER**** 
A real good strategy does not necessarily win. So A HIGH WINNING RATE OR FITNESS DOES NOT NECESSARILY INDICATE THAT IT IS A GOOD STRATEGY.
For example, the TIT FOR TAT does not always win, but rather tries to maximize points for both players.
A truly good strategy should not exploit its opponent, but should rather try to enforce cooperation by being nice and by punishing the un-cooperation of the opponent.
(see section below on Characteristices of TIT FOR TAT Strategy)



*********The most important part: expected results**********
F) In the simulation section:
The TIT FOR TAT Strategy is the best strategy to be known. 
Therefore, the goal of the project is to use a generic algorithm that produces a strategy that possesses the same characteristics as the TIT FOR TAT Strategy.

Brief explanation of the TIT FOR TAT Strategy (the best strategy to be known): 
Its first move is always cooperate. It's never the first to defect.
If the opponent cooperates, TFT keeps cooperating.
If the opponent defects, then TFT is going to defect in the next game. 
However, if the opponent begins cooperating, TIT FOR TAT will come back to cooperation in the next game.

Characteristics of TIT FOR TAT Strategy 
Therefore, it punishes its un-cooperative opponent by defecting in the next game, 
but is also forgiving because once the opponent cooperates it comes back to cooperation as well. 

1. If you simulate TIT FOR TAT vs All Cooperate,
They will tie. It is logical: All the moves by both players are cooperative, there for both of them gain 3 points at every game.

2. If you simulate TIT FOR TAT vs All defect,
TIT FOR TAT will obtain 5 pts less than All Defect. Because in the first game TFT cooperates and AllD defects, so +0 for TFT and +5 for AllD.
For all the rest of the round, both of them gain 1 point at every game.

3. If you simulate TIT FOR TAT vs Random Strategy,
They will most likely have the approximately same score. Because TIT FOR TAT is nice, and at the same time doesn't allow the opponent to have a big advantage.


Therefore, for the best strategy from our GA to good,
It should resemble the TIT FOR TAT strategy in some ways. It should either beat or tie with TIT FOR TAT.
It should possess some TFT's characteristics, and should have similar results as TIT FOR TAT when competing against AllC, AllD, and random strategies. 
