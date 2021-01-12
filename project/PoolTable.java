import java.lang.Math; 
import java.lang.Thread;

public class PoolTable{

    /**
     * Variables and constructor for a pool table.
     * Uses a for loop, 5 rectangles and an array of black balls to build a table.
     * 
     * The whole constructor is variable base and object oriented, allowing you to build the table without having to define many variables.
     * All the table holes are defined to help calculate the appropriate spawn location for the balls when it comes to that.
     * 
     * The table is built by one carpet and 4 pieces of wood, which are added to the game arena after the balls which make the holes, in a specific order that allows you 
     * to make them not overlap and layer them correctly.
     * 
     * The value 0.57 used in tableHeight calculation is the proportion between the table with and height found online
     * 
     * The value 0.0243589748 used in the ball size calculation is based on the proportional size of the ball to a pool table width, knowing the average table size is 2,34 metres 
     * and the average ball diameter is 0.057 metres.
     */

    private GameArena pool = new GameArena(880, 900);

    private int tableStartX = 20;
    private int tableStartY = 20;
    private int tableWidth = 800;
    private int tableHeight = (int)Math.round(tableWidth * 0.57);
    private int woodThickness = 40;

    private double tableFriction = 5;

    private int ballSize = (int)Math.round(0.0243589748 * tableWidth); 
    private int ballRadius = (int)(ballSize / 2);
    private int xDistanceBetweenBalls = (int)Math.sqrt((ballSize * ballSize) - (ballRadius * ballRadius));
    //private double tempSpeed = 0;

    private Rectangle carpet = new Rectangle(60.0, 60.0, tableWidth - woodThickness, tableHeight - woodThickness, "#008000");

    private Rectangle woodLeft = new Rectangle(tableStartX, tableStartY, woodThickness, tableHeight, "#8B4513");
    private Rectangle woodRight = new Rectangle(tableStartX + tableWidth, tableStartY, woodThickness, tableHeight, "#8B4513");
    private Rectangle woodTop = new Rectangle(tableStartX, tableStartY, tableWidth, woodThickness, "#8B4513");
    private Rectangle woodBottom = new Rectangle(tableStartX, tableStartY + tableHeight, tableWidth + woodThickness, woodThickness, "#8B4513");

    private Ball holes[] = new Ball[6];
    private PoolBall poolBalls[] = new PoolBall[16];
    //private int putCount = 0;

    private double topMiddleHoleX = (carpet.getXPosition() + carpet.getWidth()) / 2;
    private double topMiddleHoleY = carpet.getYPosition();
    private double bottomMiddleHoleY = carpet.getYPosition() + carpet.getHeight();
    private double bottomMiddleHoleX = (carpet.getXPosition() + carpet.getWidth()) / 2;
    private double topLeftHoleY = carpet.getYPosition();
    private double topLeftHoleX = carpet.getXPosition();
    private double bottomLeftHoleY = (carpet.getYPosition() + carpet.getHeight());
    private double bottomLeftHoleX = carpet.getXPosition();
    private double topRightHoleY = carpet.getYPosition();
    private double topRightHoleX = carpet.getXPosition() + carpet.getWidth();
    private double bottomRightHoleY = carpet.getYPosition() + carpet.getHeight();
    private double bottomRightHoleX = carpet.getXPosition() + carpet.getWidth();

    private double xWhiteBall = (topMiddleHoleX + topLeftHoleX)/2;
    private double yWhiteBall = (topLeftHoleY + bottomLeftHoleY)/2;

    private double whiteBallX, whiteBallY, width, height;
    private double aimPositionX, aimPositionY;

    private Line aim = new Line(whiteBallX, whiteBallY, aimPositionX, aimPositionY, 2, "WHITE");

    private double power = 0;
    private double powerBarSpawnY = tableStartY + tableHeight + 2 * woodThickness;
    private Rectangle powerIndicator = new Rectangle(tableStartX, powerBarSpawnY, 200 * power, woodThickness, "RED");

    private Text powerNum = new Text("Power = " + (int)power, 40, woodLeft.getXPosition(), woodThickness + woodLeft.getYPosition() + woodLeft.getHeight() + 40, "WHITE");
    private Text playerTurn = new Text("It's player 1's turn", 40, woodLeft.getXPosition() + woodTop.getWidth()/2, woodThickness + woodLeft.getYPosition() + woodLeft.getHeight() + 40, "WHITE");

    private boolean moving = false;

    private boolean hitsHole = false;
    private boolean noBallsPut = true;//at the start of the game no balls have been put yet, so this boolean changes when the first ball is put, storing the colour of each players balls
    private boolean whiteBallPut = false;
    private boolean blackBallPut = false;
    private boolean blackBallPermission1 = false;
    private boolean blackBallPermission2 = false;
    private boolean player1Wins = false;
    private boolean player2Wins = false;
    private boolean gameOver = false;
    private boolean playAgain = false;
    private boolean player1Turn = true;
    private boolean play = true;
    private int turn = 1;
    private boolean firstHitFlag = false;
    private boolean ballPutFlag = false;
    private String ballPut = "";
    private String firstHit = "";
    private boolean collisionOccurs = false;
    private boolean foul = false; //includes no balls hit and wrong balls put in socket
    private String player1Colour = "";
    private String player2Colour = "";
    
    private int holeNum;
    private double scoreBallsX = woodLeft.getXPosition() + ballRadius;

    /*public GameArena getPool()
    {
        return pool;
    }*/

    /**
     * Constructor for a pool table, incorporates all the balls of such table, all of the
     * tables components, amongst others.
     */
    public PoolTable()
    {

        /* 
        The following loop automates the process of adding the holes into the pool table, using a for loop and nested if conditions to 
        define what the xCoordinates will be for the ball to be placed at.
        */

        for(int i = 0; i < 6; i++)
        {
            double xCoordinate = 0;
            double yCoordinate = 0;

            if(i == 0 || i == 1 || i == 2)
            {
                yCoordinate = carpet.getYPosition();

                if(i == 0)
                {
                    xCoordinate = carpet.getXPosition();
                }
                else if(i == 1)
                {
                    xCoordinate = (carpet.getXPosition() + carpet.getWidth()) / 2;
                }
                else if(i == 2)
                {
                    xCoordinate = carpet.getXPosition() + carpet.getWidth();
                }
            }
            else if(i == 3 || i == 4 || i == 5)
            {
                yCoordinate = carpet.getYPosition() + carpet.getHeight();

                if(i == 3)
                {
                    xCoordinate = carpet.getXPosition();
                }
                else if(i == 4)
                {
                    xCoordinate = (carpet.getXPosition() + carpet.getWidth()) / 2;
                }
                else if(i == 5)
                {
                    xCoordinate = carpet.getXPosition() + carpet.getWidth();
                }
            }

            holes[i] = new Ball(xCoordinate, yCoordinate, ballSize * 2, "BLACK");
        }
        
        /*
        The order in which the elemnts (rectangles, balls) are added to the GameArena is essential,
        as this is what defines what part of the ball is visualized in the program.

        Therefore you must add the carpet, which consists of the green part of the pool table,
        after this the balls, using the for loop presented, and after this the 4 rectangles which make the edge of the 
        table, and this edge must be implemented like this due to overlapping issues which make the balls visible in the wrong
        way.
        */ 
        
        pool.addRectangle(carpet);

        pool.addRectangle(woodLeft);
        pool.addRectangle(woodRight);
        pool.addRectangle(woodTop);
        pool.addRectangle(woodBottom);

        for(int i = 0; i < 6; i++)
        {
            pool.addBall(holes[i]);
        }

        double ball0X = (topRightHoleX + topMiddleHoleX) / 2;
        double ball0Y = (topMiddleHoleY + bottomMiddleHoleY) / 2;

        for(int i = 0; i < 16; i++)
        {
            if(i == 0)
            {
                poolBalls[i] = new PoolBall(ball0X, ball0Y, ballSize, "YELLOW");
                pool.addBall(poolBalls[i]);
            }
            else if(i == 1)
            {
                poolBalls[i] = new PoolBall(ball0X + xDistanceBetweenBalls, ball0Y + ballRadius, ballSize, "RED");
                pool.addBall(poolBalls[i]);
            }
            else if(i == 2)
            {
                poolBalls[i] = new PoolBall(ball0X + xDistanceBetweenBalls, ball0Y - ballRadius, ballSize, "YELLOW");
                pool.addBall(poolBalls[i]);
            }
            else if(i == 3)
            {
                poolBalls[i] = new PoolBall(ball0X + 2 * xDistanceBetweenBalls, ball0Y - 2 * ballRadius, ballSize, "RED");
                pool.addBall(poolBalls[i]);
            }
            else if(i == 4)
            {
                poolBalls[i] = new PoolBall(ball0X + 2 * xDistanceBetweenBalls, ball0Y, ballSize, "BLACK");
                pool.addBall(poolBalls[i]);
            }
            else if(i == 5)
            {
                poolBalls[i] = new PoolBall(ball0X + 2 * xDistanceBetweenBalls, ball0Y + 2 * ballRadius, ballSize, "RED");
                pool.addBall(poolBalls[i]);
            }
            else if(i == 6)
            {
                poolBalls[i] = new PoolBall(ball0X + 3 * xDistanceBetweenBalls, ball0Y + 3 * ballRadius, ballSize, "YELLOW");
                pool.addBall(poolBalls[i]);
            }
            else if(i == 7)
            {
                poolBalls[i] = new PoolBall(ball0X + 3 * xDistanceBetweenBalls, ball0Y + ballRadius, ballSize, "RED");
                pool.addBall(poolBalls[i]);
            }
            else if(i == 8)
            {
                poolBalls[i] = new PoolBall(ball0X + 3 * xDistanceBetweenBalls, ball0Y - ballRadius, ballSize, "YELLOW");
                pool.addBall(poolBalls[i]);
            }
            else if(i == 9)
            {
                poolBalls[i] = new PoolBall(ball0X + 3 * xDistanceBetweenBalls, ball0Y - 3 * ballRadius, ballSize, "RED");
                pool.addBall(poolBalls[i]);
            }
            else if(i == 10)
            {
                poolBalls[i] = new PoolBall(ball0X + 4 * xDistanceBetweenBalls, ball0Y - 4 * ballRadius, ballSize, "YELLOW");
                pool.addBall(poolBalls[i]);
            }
            else if(i == 11)
            {
                poolBalls[i] = new PoolBall(ball0X + 4 * xDistanceBetweenBalls, ball0Y - 2 * ballRadius, ballSize, "RED");
                pool.addBall(poolBalls[i]);
            }
            else if(i == 12)
            {
                poolBalls[i] = new PoolBall(ball0X + 4 * xDistanceBetweenBalls, ball0Y, ballSize, "YELLOW");
                pool.addBall(poolBalls[i]);
            }
            else if(i == 13)
            {
                poolBalls[i] = new PoolBall(ball0X + 4 * xDistanceBetweenBalls, ball0Y + 2 * ballRadius, ballSize, "RED");
                pool.addBall(poolBalls[i]);
            }
            else if(i == 14)
            {
                poolBalls[i] = new PoolBall(ball0X + 4 * xDistanceBetweenBalls, ball0Y + 4 * ballRadius, ballSize, "YELLOW");
                pool.addBall(poolBalls[i]);
            }
            else if(i == 15)
            {
                poolBalls[i] = new PoolBall(xWhiteBall, yWhiteBall, ballSize, "WHITE");
                pool.addBall(poolBalls[i]);
            }
        }

        pool.addText(powerNum);
        pool.addText(playerTurn);

        whiteBallX = this.poolBalls[15].getXPosition();
        whiteBallY = this.poolBalls[15].getYPosition();

        aimPositionX = this.carpet.getXPosition();
        aimPositionY = this.carpet.getYPosition();

        width = this.carpet.getWidth();
        height = this.carpet.getHeight();

        pool.addLine(aim);        

    }

    /**
     * aimLine method allows the user to aim down the line whilst playing, allowing less cluttered 
     * code in the playPool method
     */
    public void aimLine()
    {

        this.aim.setLinePosition(whiteBallX, whiteBallY, aimPositionX, aimPositionY);
        
        whiteBallX = this.poolBalls[15].getXPosition();
        whiteBallY = this.poolBalls[15].getYPosition();
        
        if(pool.rightPressed() == true)
        {
            if(aimPositionY == carpet.getYPosition() && aimPositionX <= carpet.getXPosition() + carpet.getWidth() && aimPositionX >= carpet.getXPosition())
            {
                aimPositionX = aimPositionX + 10;
            }
            else if(aimPositionY == carpet.getYPosition() && aimPositionX >= carpet.getXPosition() + carpet.getWidth())
            {
                aimPositionY = aimPositionY + 10;
                aimPositionX = carpet.getWidth() + carpet.getXPosition();
            }
            else if(aimPositionX == carpet.getXPosition() + carpet.getWidth() && aimPositionY <= carpet.getHeight() + carpet.getYPosition() && aimPositionY > carpet.getYPosition())
            {
                aimPositionY = aimPositionY + 10;
            }
            else if(aimPositionX == carpet.getXPosition() + carpet.getWidth() && aimPositionY >= carpet.getHeight() + carpet.getYPosition())
            {
                aimPositionY = carpet.getHeight() + carpet.getYPosition();
                aimPositionX = aimPositionX - 10;
            }
            else if(aimPositionY == carpet.getHeight() + carpet.getYPosition() && aimPositionX <= carpet.getXPosition() + carpet.getWidth() && aimPositionX > carpet.getXPosition())
            {
                aimPositionX = aimPositionX - 10;
            }
            else if(aimPositionX <= carpet.getXPosition() && aimPositionY == carpet.getHeight() + carpet.getYPosition())
            {
                aimPositionX = carpet.getXPosition();
                aimPositionY = aimPositionY - 10;
            }
            else if(aimPositionX == carpet.getXPosition() && aimPositionY <= carpet.getHeight() + carpet.getYPosition() && aimPositionY > carpet.getYPosition())
            {
                aimPositionY = aimPositionY - 10; 
            }
            else if(aimPositionX == carpet.getXPosition() && aimPositionY <= carpet.getYPosition())
            {
                aimPositionY = carpet.getYPosition();
                aimPositionX = aimPositionX + 10;
            }
        }
        else if(pool.leftPressed() == true)
        {
            if(aimPositionX == carpet.getXPosition() && aimPositionY >= carpet.getYPosition() && aimPositionY < carpet.getYPosition() + carpet.getHeight())
            {
                aimPositionY = aimPositionY + 10;
            }
            else if(aimPositionX == carpet.getXPosition() && aimPositionY >= carpet.getYPosition() && aimPositionY < carpet.getYPosition() + carpet.getHeight())
            {
                aimPositionY = aimPositionY + 10;
            }
            else if(aimPositionX == carpet.getXPosition() && aimPositionY >= carpet.getYPosition() + carpet.getHeight())
            {
                aimPositionY = carpet.getYPosition() + carpet.getHeight();
                aimPositionX = aimPositionX + 10;
            }
            else if(aimPositionY == carpet.getYPosition() + carpet.getHeight() && aimPositionX >= carpet.getXPosition() && aimPositionX < carpet.getXPosition() + carpet.getWidth())
            {
                aimPositionX = aimPositionX + 10;
            }
            else if(aimPositionY == carpet.getYPosition() + carpet.getHeight() && aimPositionX >= carpet.getXPosition() + carpet.getWidth())
            {
                aimPositionX = carpet.getXPosition() + carpet.getWidth();
                aimPositionY = aimPositionY - 10;
            }
            else if(aimPositionX == carpet.getXPosition() + carpet.getWidth() && aimPositionY <= carpet.getYPosition() + carpet.getHeight() && aimPositionY > carpet.getYPosition())
            {
                aimPositionY = aimPositionY - 10;
            }
            else if(aimPositionY <= carpet.getYPosition() && aimPositionX == carpet.getXPosition() + carpet.getWidth())
            {
                aimPositionX = aimPositionX - 10;
                aimPositionY = carpet.getYPosition();
            }
            else if(aimPositionY <= carpet.getYPosition() && aimPositionX <= carpet.getXPosition() + carpet.getWidth())
            {
                aimPositionX = aimPositionX - 10;
                
            }
        }
    }

    /**
     * Play pool function incorporates all the aspets of the pool game, such as
     * changing te score, balls being put in sockets, re-spawning the white ball
     * after it goes in a socket, amongst many more.
     */
    public void playPool()
    {
        foul = false;
        collisionOccurs = false;

        while(/*true*/play) 
        {
            this.aimLine();
            pool.pause();
            if(pool.upPressed() == true || pool.downPressed() == true) 
            {

                if(pool.upPressed() == true && power<= 50)
                {
                    power = power + 0.3;
                }
                else if(pool.downPressed() == true && power > 0)
                {
                    power = power - 0.3;
                }

                powerNum.setText("Power = " + (int)power);
                
            }
                
            if(pool.spacePressed() == true)
            {
                pool.removeLine(aim);
                poolBalls[15].launchBall(aim, power);
                moving = true;
            }

            while(moving) 
            {   
                pool.pause();

                for(int i = 0; i < 16; i++) 
                {
                    moving = false;
                    if(poolBalls[i].getXSpeed() > 0.01 || poolBalls[i].getXSpeed() < -0.01 || poolBalls[i].getYSpeed() > 0.01 || poolBalls[i].getYSpeed() < -0.01) 
                    {
                        moving = true;
                    }

                }

                if(firstHitFlag == false)
                {
                    for(int i = 0; i < 15; i++) 
                    {
                        if(poolBalls[15].collides(poolBalls[i]) == true)
                        {
                            firstHit = poolBalls[i].getColour();
                            firstHitFlag = true;
                            collisionOccurs = true;
                        }
                    }
                }
                
                for(int i = 0; i < 16; i++) 
                {

                    for(int j = i + 1; j < 16; j++) 
                    {
                        if(poolBalls[i].collides(poolBalls[j]) == true) 
                        {
                            
                            poolBalls[i].deflect(poolBalls[j]);
                            
                        }
                    }
                }

                for(int i = 0; i < 16; i++) 
                {
                    poolBalls[i].move(poolBalls[i].getXSpeed(), poolBalls[i].getYSpeed());
                    inBounds(poolBalls[i]);
                    putBalls(poolBalls[i]);
                }
                
                if(moving == false)
                {
                    pool.addLine(aim);

                    if(player1Colour != "" && player2Colour != "")
                    {
                        allOfColourPut(player1Colour);

                        if(blackBallPermission1 == true)
                        {
                            player1Colour = "BLACK";
                        }
                            
                        allOfColourPut(player2Colour);

                        if(blackBallPermission2 == true)
                        {
                            player2Colour = "BLACK";
                        }
                    }

                    detectFouls();
                    firstHitFlag = false;
                    collisionOccurs = false;

                    if(gameOver == true)
                    {
                        foul = false;
                        playAgain = false;

                        if(blackBallPermission1 == true)
                        {
                            playerTurn.setText("Player 1 wins!");
                            player1Wins = false;
                        }
                        else if(blackBallPermission2 == true)
                        {
                            playerTurn.setText("Player 2 wins!");
                            player2Wins = true;
                        }
                        else if(blackBallPermission1 == false && blackBallPermission2 == false)
                        {
                            if(player1Turn == true)
                            {
                                player1Wins = false;
                                player2Wins = true;
                                playerTurn.setText("Player 2 wins!");
                            }
                            else if(player1Turn != true)
                            {
                                player1Wins = true;
                                player2Wins = false;
                                playerTurn.setText("Player 1 wins!");
                            }
                        }
                        pool.removeLine(aim);
                        power = 0;
                        play = false;
                    }
                    else if(foul == true)
                    {
                        if(turn == 1)
                        {
                            turn = 2;
                        }
                        else
                        {
                            turn = 1;
                        }
                        playerTurn.setText("It's player " + turn +"'s turn");
                        ballInHand();
                        player1Turn = !player1Turn;
                    }
                    else if(foul == false && playAgain == false)
                    {
                        if(turn == 1)
                        {
                            turn = 2;
                        }
                        else
                        {
                            turn = 1;
                        }
                        playerTurn.setText("It's player " + turn +"'s turn");
                        player1Turn = !player1Turn;
                    }
                    else if(foul == false && playAgain == true)
                    {
                        //do nothing as player turn does not change
                        playerTurn.setText("It's player " + turn +"'s turn");
                        playAgain = false;
                    }                  
                }
            }
        }
    }

    /**
     * The detectFouls method allows the game to detect fouls based on the playerturn1 variable,
     * it changes parameters such as foul, which registers a foul, playAgain which indicates the current player should
     * play again due to having scored a point, and gameOver, which indicates the game has ended, i.e the black ball has been put and there is a winner.
     */
    public void detectFouls()
    {
        foul = false;
        playAgain = false;
        whiteBallPut = false;

        if(noBallsPut == false)
        {   
            if(player1Turn == true)
            {
                if(firstHit == player1Colour)
                {   
                    if(ballPut == "YELLOW" || ballPut == "RED")  
                    {
                        if(ballPut == player1Colour)
                        {
                            playAgain = true;
                            ballPut = "";
                        }
                    }

                    if(ballPut == "WHITE")
                    {
                        whiteBallPut = true;
                        foul = true;
                    }

                    if(ballPut == "BLACK")
                    {
                        blackBallPut = true;
                        gameOver = true;
                    }
                }
                else
                {
                    foul = true;

                    if(ballPut == "WHITE")
                    {
                        whiteBallPut = true;
                        foul = true;
                        ballPut = "";
                    }

                    if(ballPut == "BLACK")
                    {
                        blackBallPut = true;
                        gameOver = true;
                    }
                }

                firstHit = "";
            }
            else
            {
                if(firstHit == player2Colour)
                {   
                    //firstHit = "";
                    if(ballPut == "YELLOW" || ballPut == "RED")  
                    {
                        if(ballPut == player2Colour)
                        {
                            playAgain = true;
                            ballPut = "";
                        }
                    }

                    if(ballPut == "WHITE")
                    {
                        whiteBallPut = true;
                        foul = true;
                        ballPut = "";
                    }

                    if(ballPut == "BLACK")
                    {
                        blackBallPut = true;
                        gameOver = true;
                    }
                }
                else
                {
                    foul = true;

                    if(ballPut == "WHITE")
                    {
                        whiteBallPut = true;
                        foul = true;
                        ballPut = "";
                    }

                    if(ballPut == "BLACK")
                    {
                        blackBallPut = true;
                        gameOver = true;
                    }
                }

                firstHit = "";
            }
            
              
        }
        else //noBallsPut == true
        {
            if(collisionOccurs == false)
            {
                foul = true;
            }
            else
            {
            }

            if(blackBallPut == true)
            {
                gameOver = true;
            }
            
        }

    }

    /**
     * The ballInHand method allows the user to place the ball anywhere in the first quarter of the tables,
     * starting in the left hand side, which is useful for when a foul occurs and the ball is in hand of the player who plays next
     */
    public void ballInHand()
    {
        pool.removeBall(poolBalls[15]);
        pool.removeLine(aim);
        poolBalls[15].setXPosition(xWhiteBall);
        poolBalls[15].setYPosition(yWhiteBall);
        pool.addBall(poolBalls[15]);
        
        while(pool.enterPressed() != true)
        {
            pool.pause();

            if(pool.leftPressed() == true && poolBalls[15].getXPosition() - poolBalls[15].getSize() / 2 > carpet.getXPosition())
            {
                poolBalls[15].move(-5, 0);
            }
            else if(pool.rightPressed() == true  && poolBalls[15].getXPosition() + poolBalls[15].getSize() / 2 < xWhiteBall + poolBalls[15].getSize() / 2)
            {
                poolBalls[15].move(5, 0);
            }
            else if(pool.downPressed() == true  && poolBalls[15].getYPosition() + poolBalls[15].getSize() / 2 < carpet.getYPosition() + carpet.getHeight())
            {
                poolBalls[15].move(0, 5);
            }
            else if(pool.upPressed() == true  && poolBalls[15].getYPosition() - poolBalls[15].getSize() / 2 > carpet.getYPosition())
            {
                poolBalls[15].move(0, -5);
            }
        }

        pool.addLine(aim);
    }

    /**
     * inBounds is a method which makes sure the ball remains inside the carpet of the pool table by altering the ortogonal x and y variables of a ball
     * @param ball
     */
    public void inBounds(PoolBall ball)
    {

        for(int i = 0; i < 6; i++)
        {
            if(ball.distTwoBalls(holes[i]) < holes[i].getSize()/2 - ball.getSize()/4 && ball.collides(holes[i]) == true)
            {
                holeNum = i;
                hitsHole = true;
            }
        }
        
        if(ball.getXPosition() + (ball.getSize()/2) >= carpet.getXPosition() + carpet.getWidth() && hitsHole != true && ball.getXSpeed() >= 0)
        {
            ball.setXSpeed(0 - ball.getXSpeed());
        }
        else if(ball.getXPosition() - (ball.getSize() / 2) <= carpet.getXPosition() && hitsHole != true && ball.getXSpeed() <= 0)
        {
            ball.setXSpeed(0 - ball.getXSpeed());
        }
        else if(ball.getYPosition() - (ball.getSize() / 2) <= carpet.getYPosition() && hitsHole != true && ball.getYSpeed() <= 0)
        {
            ball.setYSpeed(0 - ball.getYSpeed());
        }
        else if(ball.getYPosition() + (ball.getSize() / 2) >= carpet.getYPosition() + carpet.getHeight() && hitsHole != true && ball.getYSpeed() >= 0)
        {
            ball.setYSpeed(0 - ball.getYSpeed());
        }

    
        hitsHole = false;
    }
 
    /**
     * Allows the game to check when a ball has been or not put, and checking the colour of the ball put to register which has been put and 
     * altering variables such as whiteIsPut and blackIsPut, which are useful to check for the end of the game or fouls.
     * @param ball
     */
    public void putBalls(PoolBall ball)
    {
        for(int i = 0; i < 6; i++)
        {
            if(ball.distTwoBalls(holes[i]) < holes[i].getSize()/2 /*- ball.getSize()/2*/)
            {

                if(noBallsPut == false)
                {
                    if(ball.getColour() == "WHITE")
                    {
                        whiteBallPut = true;
                        ball.setXSpeed(0);
                        ball.setYSpeed(0);
                        ball.setXPosition(xWhiteBall);
                        ball.setYPosition(yWhiteBall);
                        pool.removeLine(aim);
                    }
                    else if(ball.getColour() == "RED" || ball.getColour() == "YELLOW")
                    {
                        pool.removeBall(ball);
                        ball.setXPosition(scoreBallsX);
                        ball.setYPosition(woodThickness + woodLeft.getYPosition() + woodLeft.getHeight() + 70);
                        ball.setXSpeed(0);
                        ball.setYSpeed(0);
                        ball.setAsPut();
                        pool.addBall(ball);

                        ballPut = ball.getColour();
                        scoreBallsX += poolBalls[0].getSize()/2 + 10;

                    }
                    else if(ball.getColour() == "BLACK")
                    {
                        blackBallPut = true;
                        pool.removeBall(ball);
                        ball.setXPosition(scoreBallsX);
                        ball.setYPosition(woodThickness + woodLeft.getYPosition() + woodLeft.getHeight() + 70);
                        ball.setXSpeed(0);
                        ball.setYSpeed(0);
                        ball.setAsPut();
                        pool.addBall(ball);

                        ballPut = ball.getColour();
                        scoreBallsX += poolBalls[0].getSize()/2 + 10;
                    }
                    
                }
                else if(noBallsPut == true)
                {
                    if(ball.getColour() == "WHITE")
                    {
                        noBallsPut = true;
                        whiteBallPut = true;

                        ball.setXSpeed(0);
                        ball.setYSpeed(0);
                        ball.setXPosition(xWhiteBall);
                        ball.setYPosition(yWhiteBall);
                        pool.removeLine(aim);
                    }
                    else if(ball.getColour() == "RED" || ball.getColour() == "YELLOW")
                    {
                        if(player1Turn == true)
                        {
                            player1Colour = ball.getColour();

                            if(player1Colour == "RED")
                            {
                                player2Colour = "YELLOW";
                            }
                            else if(player1Colour == "YELLOW")
                            {
                                player2Colour = "RED";
                            }
                        }
                        else
                        {
                            player2Colour = ball.getColour();

                            if(player2Colour == "RED")
                            {
                                player1Colour = "YELLOW";
                            }
                            else if(player2Colour == "YELLOW")
                            {
                                player1Colour = "RED";
                            }
                        }
                        
                        noBallsPut = false;

                        pool.removeBall(ball);
                        ball.setXPosition(scoreBallsX);
                        ball.setYPosition(woodThickness + woodLeft.getYPosition() + woodLeft.getHeight() + 70);
                        ball.setXSpeed(0);
                        ball.setYSpeed(0);
                        ball.setAsPut();
                        pool.addBall(ball);

                        ballPut = ball.getColour();
                        scoreBallsX += poolBalls[0].getSize()/2 + 10;
                    }
                    else if(ball.getColour() == "BLACK")
                    {
                        blackBallPut = true;
                        pool.removeBall(ball);
                        ball.setXPosition(scoreBallsX);
                        ball.setYPosition(woodThickness + woodLeft.getYPosition() + woodLeft.getHeight() + 70);
                        ball.setXSpeed(0);
                        ball.setYSpeed(0);
                        ball.setAsPut();
                        pool.addBall(ball);

                        ballPut = ball.getColour();
                        scoreBallsX += poolBalls[0].getSize()/2 + 10;
                    }
                    
                }

            }
        }
        
    }

    /**
     * allOfColour method allows the users to check if all the balls of such a colour are put or not
     * this allows us to check if a certain player is on his last ball or not, changing the permissions
     * such that contacting the black ball as a first hit is allowed and also such that putting said ball
     * is allowed.
     * @param colour
     */
    public void allOfColourPut(String colour)
    {
        if(colour == player1Colour)
        {
            blackBallPermission1 = true;
        }
        else if(colour == player2Colour)
        {
            blackBallPermission2 = true;
        }
        else
        {
            blackBallPermission1 = false;
            blackBallPermission2 = false;
        }

        for(int i = 0; i < 15 ; i++)
        {
            if(poolBalls[i].getColour() == colour && poolBalls[i].putStatus() != true)
            {
                if(colour == player1Colour)
                {
                    blackBallPermission1 = false;
                }
                else if(colour == player2Colour)
                {
                    blackBallPermission2 = false;
                } 
            }
        }
    }
}

