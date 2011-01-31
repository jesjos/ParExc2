import java.awt.Color;
import java.util.concurrent.Semaphore;

import javax.swing.*;

public class Balls {

    public static void nap(int ms) {
	try {
	    Thread.sleep(ms);
	}
	catch(InterruptedException e) {
	    //
	    //  Print out the name of the tread that caused this.
	    //
	    System.err.println("Thread "+Thread.currentThread().getName()+
			       " throwed exception "+e.getMessage());
	}
    }

    public static void main(String[] a) {
	
        final BallWorld world = new BallWorld();
	final JFrame win = new JFrame();
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.getContentPane().add(world);
		win.pack();
		win.setVisible(true);
	    }});
	
	Thread.currentThread().setName("MyMainThread");
	
	Semaphore[] flags = new Semaphore[4];
	Semaphore continueFlag = new Semaphore (0,true);
	
	for (int i = 0; i<4;i++) {
		flags[i] = new Semaphore(0,true);
	}

	nap((int)(5000*Math.random()));
	new Ball(world, 50, 80, 5, 10, Color.red, flags[0], continueFlag).start();
	nap((int)(5000*Math.random()));
	new Ball(world, 70, 100, 8, 6, Color.blue, flags[1], continueFlag).start();
	nap((int)(5000*Math.random()));
	new Ball(world, 150, 100, 9, 7, Color.green, flags[2], continueFlag).start();
	nap((int)(5000*Math.random()));
	new Ball(world, 200, 130, 3, 8, Color.black, flags[3], continueFlag).start();
	nap((int)(5000*Math.random()));
    
    while(true) {
    	try {
			continueFlag.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int i = 0;
		for (Semaphore s : flags) {
			if (s.hasQueuedThreads()) {
				i++;
			}
		}
    	if (i == 4) {
    		nap(2000);
    		for (Semaphore s : flags) {
    			s.release();
    		}
    	}
    }
  }
}
