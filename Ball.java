import java.awt.*;
import java.util.concurrent.Semaphore;

public class Ball extends Thread {

    BallWorld world;
    
    private int xpos, ypos, xinc, yinc;

    private final Color col;

    private final static int ballw = 10;
    private final static int ballh = 10;
    
    private Semaphore myFlag;
    private Semaphore continueFlag;
    
    public Ball(BallWorld world, int xpos, int ypos, 
		int xinc, int yinc, Color col, Semaphore flag, Semaphore continueFlag) {
	//
	// Assign a name to this thread for easier debugging
	//
	super("Ball :"+col);
	
	this.world = world;
      	this.xpos = xpos; this.ypos = ypos;
	this.xinc = xinc; this.yinc = yinc;
        this.col = col;

        world.addBall(this);
    this.myFlag = flag;
    this.continueFlag = continueFlag;
    }
    
    public void run() {
    	while (true) {
	    	move();
    		if (xpos-ypos  == 0) {
    			continueFlag.release();
    			try {
    				myFlag.acquire();
    			} catch (InterruptedException e) {
    				System.exit(1);
    			}
    		}
    	}
    }
   
    public void move() {
	if (xpos >= world.getWidth() - ballw || xpos <= 0 ) xinc = -xinc;
	
	if (ypos >= world.getHeight() - ballh || ypos <= 0 ) yinc = -yinc;

	Balls.nap(30);
	doMove();
	world.repaint();
    }
     
    //
    // SYNC: This modifies xpos and ypos.
    //
    public synchronized void doMove() {
	xpos += xinc;
	ypos += yinc;
    }

    //
    // SYNC: This is accessed from the GUI thread, and
    //       it reads xpos and ypos.
    //
    public synchronized void draw(Graphics g) {
	g.setColor(col);
	g.fillOval(xpos,ypos,ballw,ballh);
    }
}
