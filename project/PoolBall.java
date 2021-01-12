public class PoolBall extends Ball
{
  /**
   * PoolBall constructor builds a PoolBall using the constructor from the super class ball,
   * as is shown below in the super statement.
   * @param x x coordinate of the ball
   * @param y y coordinate of the ball
   * @param diameter diameter of the ball
   * @param col colour of the ball
   */
  public PoolBall(double x, double y, double diameter, String col)
  {
    super(x, y, diameter, col); 
    /*
    You cannot inherit a constructor, hence you call the super constructor in the first line of the 
    object which inherits the Ball class, allowing the constructor to create a PoolBall which will then
    have its own methods.
    */
  }

  /**
   * Used for launching the white ball based on the aim
   * @param aim line used to aim
   * @param power power the user wishes to launch the ball with
   */
  public void launchBall(Line aim, double power)
  {    
    double totalX = aim.getXEnd() - this.getXPosition();
    double totalY = aim.getYEnd() - this.getYPosition();
    double theta = Math.atan2(totalY,totalX);
    
    move(power*Math.cos(theta), power*Math.sin(theta));
  }

  /**
   * Simple movement method, moves dx in the x axis and dy in the y axis.
   * However this movement method accounts for friction.
   */
  public void move(double dx, double dy)
	{
    
    this.setXSpeed(dx * 0.95);
    this.setYSpeed(dy * 0.95);
    this.setXPosition(this.getXPosition() + this.getXSpeed());
    this.setYPosition(this.getYPosition() + this.getYSpeed());
	} 
}