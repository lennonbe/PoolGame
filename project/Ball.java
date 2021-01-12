/**
 * Models a simple solid sphere. 
 * This class represents a Ball object. When combined with the GameArena class,
 * instances of the Ball class can be displayed on the screen.
 */
public class Ball 
{
	// The following instance variables define the
	// information needed to represent a Ball
	// Feel free to more instance variables if you think it will 
	// support your work... 
	
	private double xPosition;			// The X coordinate of this Ball
	private double yPosition;			// The Y coordinate of this Ball
	private double size;				// The diameter of this Ball
	private double radius = size/2;
	private int layer;					// The layer of this ball is on.
	private String colour;				// The colour of this Ball
	private double xSpeed = 0;
	private double ySpeed = 0;
	private boolean put = false;


										// Permissable colours are:
										// BLACK, BLUE, CYAN, DARKGREY, GREY,
										// GREEN, LIGHTGREY, MAGENTA, ORANGE,
										// PINK, RED, WHITE, YELLOW or #RRGGBB 

	/**
	 * Constructor. Creates a Ball with the given parameters.
	 * @param x The x co-ordinate of centre of the Ball (in pixels)
	 * @param y The y co-ordinate of centre of the Ball (in pixels)
	 * @param diameter The diameter of the Ball (in pixels)
	 * @param col The colour of the Ball (Permissable colours are: BLACK, BLUE, CYAN, DARKGREY, GREY, GREEN, LIGHTGREY, MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW or ##RRGGBB)
	 */
	public Ball(double x, double y, double diameter, String col)
	{
		this.xPosition = x;
		this.yPosition = y;
		this.size = diameter;
		this.colour = col;
		this.layer = 0;
	}	

	/**
	 * Constructor. Creates a Ball with the given parameters.
	 * @param x The x co-ordinate of centre of the Ball (in pixels)
	 * @param y The y co-ordinate of centre of the Ball (in pixels)
	 * @param diameter The diameter of the Ball (in pixels)
	 * @param col The colour of the Ball (Permissable colours are: BLACK, BLUE, CYAN, DARKGREY, GREY, GREEN, LIGHTGREY, MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW or ##RRGGBB)
	 * @param layer The layer this ball is to be drawn on. Objects with a higher layer number are always drawn on top of those with lower layer numbers.
	 */
	public Ball(double x, double y, double diameter, String col, int layer)
	{
		this.xPosition = x;
		this.yPosition = y;
		this.size = diameter;
		this.colour = col;
		this.layer = layer;
	}	

	/**
	 * Obtains the current position of this Ball.
	 * @return the X coordinate of this Ball within the GameArena.
	 */
	public double getXPosition()
	{
		return xPosition;
	}

	/**
	 * Obtains the current position of this Ball.
	 * @return the Y coordinate of this Ball within the GameArena.
	 */
	public double getYPosition()
	{
		return yPosition;
	}

	/**
	 * Moves the current position of this Ball to the given co-ordinates
	 * @param x the new x co-ordinate of this Ball
	 */
	public void setXPosition(double x)
	{
		this.xPosition = x;
	}

	/**
	 * Moves the current position of this Ball to the given co-ordinates
	 * @param y the new y co-ordinate of this Ball
	 */
	public void setYPosition(double y)
	{
		this.yPosition = y;
	}

	/**
	 * Sets the x movement direction for the ball
	 * @param x the new direction x
	 */
	public void setXSpeed(double x)
	{
		this.xSpeed = x;
	}

	/**
	 * Sets the y movement direction for the ball
	 * @param y the new direction y 
	 */
	public void setYSpeed(double y)
	{
		this.ySpeed = y;
	}

	/**
	 * Fetches the x speed of the ball
	 * @return xSpeed of ball
	 */
	public double getXSpeed()
	{
		return xSpeed;
	}

	/**
	 * Fetches the x speed of the ball
	 * @return ySpeed of ball
	 */
	public double getYSpeed()
	{
		return ySpeed;
	}

	/**
	 * Obtains the size of this Ball.
	 * @return the diameter of this Ball,in pixels.
	 */
	public double getSize()
	{
		return size;
	}
	
	/**
	 * Sets the diameter of this Ball to the given size.
	 * @param s the new diameter of this Ball, in pixels.
	 */
	public void setSize(double s)
	{
		size = s;
	}

	/**
	 * Obtains the layer of this Ball.
	 * @return the layer of this Ball.
	 */
	public int getLayer()
	{
		return layer;
	}

	/**
	 * Obtains the colour of this Ball.
	 * @return a textual description of the colour of this Ball.
	 */
	public String getColour()
	{
		return colour;
	}

	/**
	 * Sets the colour of this Ball.
	 * @param c the new colour of this Ball, as a String value. Permissable colours are: BLACK, BLUE, CYAN, DARKGREY, GREY, GREEN, LIGHTGREY, MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW or #RRGGBB.
	 */
	public void setColour(String c)
	{
		colour = c;
	}

	/**
	 * Determines if this Ball is overlapping the given ball.
	 * 
	 * @param b the ball to test for collision
	 * @return true of this ball is overlapping the ball b, false otherwise.
	 */
	public boolean collides(Ball b)
	{
		double dx = b.xPosition - xPosition;
		double dy = b.yPosition - yPosition;
		double distance = Math.sqrt(dx*dx+dy*dy);

		return distance < size/2 + b.size/2;
	}

	/**
	* Converts a vector into a unit vector.
	* Used by the deflect() method to calculate the resultnt direction after a collision.
	*/
	private double[] normalizeVector(double[] vec)
	{
	double mag = 0.0;
	int dimensions = vec.length;
	double[] result = new double[dimensions];
	for (int i=0; i < dimensions; i++)
	mag += vec[i] * vec[i];
	mag = Math.sqrt(mag);
	if (mag == 0.0)
	{
	result[0] = 1.0;
	for (int i=1; i < dimensions; i++)
	result[i] = 0.0;
	}
	else
	{
	for (int i=0; i < dimensions; i++)
	result[i] = vec[i] / mag;
	}
	return result;
	}

	public double distTwoBalls(Ball b)
	{
		double dx = b.xPosition - xPosition;
		double dy = b.yPosition - yPosition;
		double distance = Math.sqrt(dx*dx+dy*dy);

		return distance;
	}

	public void setAsPut()
	{
		this.put = true;
	}

	public boolean putStatus()
	{
		return this.put;
	}

	public void deflect(Ball ball)
	{
	// The position and speed of each of the two balls in the x and y axis before collision.
	// YOU NEED TO FILL THESE VALUES IN AS APPROPRIATE...

	//System.out.println("deflects is here");
	double xPosition1, xPosition2, yPosition1, yPosition2;
	double xSpeed1, xSpeed2, ySpeed1, ySpeed2;

	xPosition1 = this.xPosition;
	yPosition1 = this.yPosition;
	
	xPosition2 = ball.xPosition;
	yPosition2 = ball.yPosition;

	xSpeed1 = this.xSpeed;
	ySpeed1 = this.ySpeed;

	xSpeed2 = ball.xSpeed;
	ySpeed2 = ball.ySpeed;

	if(ySpeed1 == 0 && xSpeed1 == 0 && ySpeed2 == 0 && xSpeed2 == 0)
	{
		this.xSpeed = 0;
		this.ySpeed = 0;
		ball.xSpeed = 0;
		ball.ySpeed = 0;
		//System.out.println("speed is 00");
	}
	else
	{
		// Calculate initial momentum of the balls... We assume unit mass here.
		double p1InitialMomentum = Math.sqrt(xSpeed1 * xSpeed1 + ySpeed1 * ySpeed1);
		double p2InitialMomentum = Math.sqrt(xSpeed2 * xSpeed2 + ySpeed2 * ySpeed2);
		// calculate motion vectors
		double[] p1Trajectory = {xSpeed1, ySpeed1};
		double[] p2Trajectory = {xSpeed2, ySpeed2};
		// Calculate Impact Vector
		double[] impactVector = {xPosition2 - xPosition1, yPosition2 - yPosition1};
		double[] impactVectorNorm = normalizeVector(impactVector);
		// Calculate scalar product of each trajectory and impact vector
		double p1dotImpact = Math.abs(p1Trajectory[0] * impactVectorNorm[0] + p1Trajectory[1] * impactVectorNorm[1]);
		double p2dotImpact = Math.abs(p2Trajectory[0] * impactVectorNorm[0] + p2Trajectory[1] * impactVectorNorm[1]);
		// Calculate the deflection vectors - the amount of energy transferred from one ball to the other in each axis
		double[] p1Deflect = { -impactVectorNorm[0] * p2dotImpact, -impactVectorNorm[1] * p2dotImpact };
		double[] p2Deflect = { impactVectorNorm[0] * p1dotImpact, impactVectorNorm[1] * p1dotImpact };
		// Calculate the final trajectories
		double[] p1FinalTrajectory = {p1Trajectory[0] + p1Deflect[0] - p2Deflect[0], p1Trajectory[1] + p1Deflect[1] - p2Deflect[1]};
		double[] p2FinalTrajectory = {p2Trajectory[0] + p2Deflect[0] - p1Deflect[0], p2Trajectory[1] + p2Deflect[1] - p1Deflect[1]};
		// Calculate the final energy in the system.
		double p1FinalMomentum = Math.sqrt(p1FinalTrajectory[0] * p1FinalTrajectory[0] + p1FinalTrajectory[1] * p1FinalTrajectory[1]);
		double p2FinalMomentum = Math.sqrt(p2FinalTrajectory[0] * p2FinalTrajectory[0] + p2FinalTrajectory[1] * p2FinalTrajectory[1]);
		// Scale the resultant trajectories if we've accidentally broken the laws of physics.
		double mag = (p1InitialMomentum + p2InitialMomentum) / (p1FinalMomentum + p2FinalMomentum);
		// Calculate the final x and y speed settings for the two balls after collision.
		this.xSpeed = p1FinalTrajectory[0] * mag;
		this.ySpeed = p1FinalTrajectory[1] * mag;
		ball.xSpeed = p2FinalTrajectory[0] * mag;
		ball.ySpeed = p2FinalTrajectory[1] * mag;
	}
	
	}

}
