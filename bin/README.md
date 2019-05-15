# Code

Place the code you developped for the term project in this folder. Add any instructions/documentation required to run the code, and what results can be expected in this *README* file.

---

**Quick Intro**

The TIT FOR TAT Strategy is the best strategy for Prisoners' Dilemma to be theoretically known.  
Therefore, the *goal of this project* is to create a generic algorithm that produces a strategy possessing the similar characteristics as the TIT FOR TAT Strategy.

***Brief explanation of the TIT FOR TAT Strategy***:  
Its first move is always cooperate. It's never the first to defect.  
If the opponent cooperates, TFT keeps cooperating.  
If the opponent defects, then TFT is going to defect in the next game.  
However, if the opponent begins cooperating, TIT FOR TAT will come back to cooperation in the next game.  
Note: TFT only looks at opponent's previous move to decide next move.

***Characteristics of TIT FOR TAT Strategy***  
1. It punishes its un-cooperative opponent by defecting in the next game. It protects itself by not letting the opponent exploit it.  
2. It is also forgiving because once the opponent cooperates, it comes back to cooperation as well. Its ultimate goal is to promote cooperation.

---

**A) In the boolean section at the beginning:**

1. Can decide to keep elites or not.  
2. Can choose to add the strategies *"TIT FOR TAT"*, *"all cooperate"*, and *"all defect"* in the initial strategies (in the initial population)  
It doesn't seem to change much, but may be interesting.

---

**B) In the main method:**

When running the main method, two choices (excluding the exit choice) appear.  
1. Start the genetic algorithm  
2. Start with the *TIT FOR TAT zone*

It is recommended to start with the *TIT FOR TAT zone*, because *TIT FOR TAT* is the control strategy. It is the one of the theoretically known best strategies.  
Beginning by playing with the *TIT FOR TAT* strategy can give you an idea about what a good strategy looks like, and about whether the strategy developped by the GA is a good one or not.  
Upon entering the *TIT FOR TAT* zone, four choices appear.  
1. Evaluate winning rate (fitness) of *TIT FOR TAT* strategy. (By making it play against 1000 random strategies)  
2. Simulate round of *TIT FOR TAT* against *All COOPERATE*  
3. Simulate *TIT FOR TAT* against *All DEFECT*  
4. Simulate *TIT FOR TAT* against *RANDOM STRATEGY*  
It is recommended to try all of them.

1. If you simulate *TIT FOR TAT* against *All COOPERATE*:  
They will tie.  
It is logical: All the moves by both players are cooperative, therefore both of them gain 3 points at every game.

2. If you simulate *TIT FOR TAT* against *All DEFECT*:  
*TIT FOR TAT* will obtain 5 pts less than *All DEFECT*.  
Because in the first game TFT cooperates and AllD defects, so +0 for TFT and +5 for AllD.  
However *TIT FOR TAT* doesn't allow *All DEFECT* to further exploit him and will defect in every following game as well.  
Therefore for rest of the games, both of them gain 1 point at every game.

3. If you simulate *TIT FOR TAT* against *RANDOM STRATEGY*:   
They will most likely have the approximately same score. Because TIT FOR TAT is nice, and at the same time doesn't allow the opponent to have a big advantage.   
The random strategies are generated in a way such that their genes have 50% chance to be C and 50% chance to be D. Therefore most of them would cooperate to some extent.  
Thus the sum of *TIT FOR TAT*' score and *RANDOM STRATEGY*'s score is most of the time over 400 points.

Then, exit the *TIT FOR TAT zone*.

Return to the GA. The GA will start. Wait for the best solution to come out.  
Then, you can re-evaluate the fitness of the best solution as many times as you wish (with newly generated random strategies at each time).   
Afterward, continue with simulation.  
Now, you still have the choice to simulate *TIT FOR TAT* against various opponents, but in addition, you will be able to simulate the games of the *GA best solution* against opponents.  
Try all simulations. For the expected results of simulations of *GA best solution* against various opponents, see section G.

---

**C) In the *simulation* method:**

History arrays:  
The 6 integers in the array "history" represent (in order):   
player's first move, opponent's first move  
player's second move, opponent's second move   
player's third move, opponent's third move

The third move is the move in the last game with respect to the game that is about to be played.  
For example, if game 45 is about to be played, the opponent's third move is his move in game 44, whereas the opponent's first move is his move in game 42.

---

**D) In the *situation* method:**

The number of situations (of different histories) was supposed to be 2^6 = 64, because there are 6 integers in the history, which are all either 0 or 1 (either defect or cooperate).  
However, for the three first games of a round, there is no complete history of the three past games.  
So 7 new situations were added to account for this problem.
 
(First game of the round has not been played yet)  
Situation 1: no history.

(First game was played, second game is about to be played)  
Situation 2: opponent's last move was Cooperate  
Situation 3: opponent's last move was Defect

(First and second game were played, third game is about to be played)  
Situation 4: opponent's last two moves were C and C  
Situation 5: opponent's last two moves were C and D  
Situation 6: opponent's last two moves were D and C  
Situation 7: opponent's last two moves were D and D

In the code, the integer -1 is used to represent no history (neither defect nor cooperate).

---

**E) The game rules:**

If both cooperate, +3 for each.  
If both defect, +1 for each.  
If one cooperates and one defects, +0 for cooperating one and +5 for defecting one.

---

**F) Definition of *winning* PART 1**

Winning does not necessarily mean having a higher score than the opponent. A HIGHER SCORE DOES NOT NECESSARILY INDICATE THAT IT IS A GOOD STRATEGY.  
For example, the *TIT FOR TAT* does not always win, but rather tries to maximize points for both players.  
A truly good strategy should not exploit its opponent, but should rather try to enforce cooperation by being nice and by punishing the un-cooperation of the opponent.  
(see section below on Characteristics of TIT FOR TAT Strategy)

If a strategy has a score that is much higher than his opponent, it means that he is exploiting his opponent, which is undesirable.  
i.e. He always defects when his opponent cooperates, and his opponent keeps cooperating.  
The same strategy can fail drastically if he encounters a different opponent that does not tend to cooperate.

***Therefore, players do not win individually, but actually win with their opponent as a team of two. ***

Since *TIT FOR TAT* is the best strategy, *win* should be defined in a way such that *TIT FOR TAT* always wins or wins most of the time (and such that it has a very high fitness).

Definition of *winning* (PART 1):  
In order to win, two conditions has to be fulfilled.  
1-(score of player1 + score of player2) >= 400  
(Sum of the player's score and his opponent's score is high. Indicates that there is enough cooperation)  
**AND**  
2-Math.abs(score of player1 - score of player2) <=5  
(the difference between the two players' score is small. Meaning that the high score of over 400 really results from cooperation and does not result from the exploitation of one by the other.)

With this definition, *TIT FOR TAT* effectively has a high fitness (around 80%). But note that this is only definition PART 1. PART 2 of the definition is in section H) below.

---

**G) *Important*: expected results**  
For the *GA best strategy* to truly good, it should possess some of *TIT FOR TAT*'s characteristics (when we analyze its moves and its scores in the simulation).  
Which means that:

1. If you simulate *GA best strategy* against *TIT FOR TAT*:  
They should tie and both should have a high score.   
It would indicate that *GA best strategy* tends to be cooperative.

2. If you simulate *GA best strategy* against *All COOPERATIVE*:  
They should tie and both should have a high score.   
Again, this would indicate that *GA best strategy* tends to be cooperative.

3. If you simulate *GA best strategy* against *RANDOM STRATEGIES*:  
Most of the time sum of the two players' scores should be larger than 400 and the difference between the two scores should be equal or less than five.  
This would indicate that *GA best strategy* tends to enforce cooperation, and does not exploit the opponent or let the opponent exploit him.

**HOWEVER**  
4. If you simulate *GA best strategy* against *All DEFECT*:  
Contrarily to *TIT FOR TAT* who is able to avoid being exploited by *All DEFECT*, ***GA best strategy* may get HEAVILY EXPLOITED by *All DEFECT*!!!**

This is because the *GA best strategy* has a problem of **specificity**.  
Just like it was mentioned in section B), the random strategies used to "train" the *GA best strategy* are generated in a way such that their genes have 50% chance of being C and 50% chance of being D.  
Therefore the random strategies most often tend to cooperate with the *GA best strategy* to a certain extent.

However, when encountering a strategy with 0% cooperation, which is the *All DEFECT* strategy, the *GA best strategy* becomes inefficient.  
It is just like in Robby the Robot where Robby was trained with rooms containing 50% of garbage, but becomes inefficient when put in a room with 100% garbage.

---

**H) Definition of *winning* PART 2**

To solve the problem of specificity which is mentioned in the previous section, we have to put some *All DEFECT* amidst the set of random strategies against which the *GA best strategy* is trained.   
Then, a new part should be added to the already existing definition of *winning*.

In addition to Part 1, a round of games is also won if:  
1. The genes of the opponent are over 70% Defect.  
**AND**  
2. Math.abs(score of player1 - score of player2) <=5.

Notice that in this part of definition, there is no restriction on the sum of both players' scores.  
It is lenient, because we understand that if the opponent refuses to cooperate, then it is impossible for the sum to be high.  
However, our goal is for the *GA best strategy* to be able to defend himself against this kind of player, so the restriction of a small difference between the two players' scores is still present.

The final definition of *winning* would be if (Definition part 1 = true **OR* Definition part 2 = true), then player wins.




