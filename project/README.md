This is the term 3 project for SCC110.

This project consists of a game of 8 ball pool created using game arena.
Firstly we create the PoolBall class with a launch and move method. The constructor in this class uses the super constructor of the ball class which allows us to create instances of the PoolBall using the same parameters of Ball.

We then created a PoolTable class which creates the table based on data from the internet, making it always proportional in size based of the width of the table. This includes making the balls proportional as well.
This class includes an aim() method, which allows us to aim the cue ball, a playPool() method which allows us to play the game, a detectFouls() method for detecting fouls, a putBalls() for putting the balls in the holes and a allBallsPut(String colour) which checks if all balls of said colour have been put.

The game is played by using the space button to launch the white ball and the left and right buttons to rotate left and right, respectively.
If a foul occurs you must place the ball using left, right, up and down, and when you are pleased with the ball position you can press enter and set it.

IN ORDER TO COMPILE AND RUN THE PROGRAM YOU MUST DO THE FOLLOWING:

-download the repository
-javac *.java
-java Driver


