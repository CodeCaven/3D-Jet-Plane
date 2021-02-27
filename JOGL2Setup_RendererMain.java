import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import java.io.IOException;

 
/**
 * Based on :http://www3.ntu.edu.sg/home/ehchua/programming/opengl/JOGL2.0.html with minor modifications
 * JOGL 2.0 Program Template (GLCanvas)
 * This is the top-level "Container", which allocates and add GLCanvas ("Component")
 * and animator.
 */
@SuppressWarnings("serial")
public class JOGL2Setup_RendererMain extends JFrame{
   
   private static String TITLE = "FIT3088 - Assignment2 - 11966939"; 
   private static final int CANVAS_WIDTH = 640;  
   private static final int CANVAS_HEIGHT = 480; 
   private static final int FPS = 60;
 
   /** Constructor to setup the top-level container and animator */
   public JOGL2Setup_RendererMain() {

      // Create the OpenGL rendering canvas
	  
      GLCanvas canvas = new JOGL2Setup_GLCanvas();
      canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
      
      // Create a animator that drives canvas' display() at the specified FPS.
      final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);
      
      // Create the top-level container frame
      
      this.getContentPane().add(canvas);
      
      this.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            // Use a dedicate thread to run the stop() to ensure that the
            // animator stops before program exits.
            new Thread() {
               @Override
               public void run() {
                  if (animator.isStarted()) animator.stop();
                  System.exit(0);
               }
            }.start();
         }
      });
      
      this.setTitle(TITLE);
      this.pack();
      this.setVisible(true);
      animator.start(); // start the animation loop
   }
 
   /** The entry main() method */
   public static void main(String[] args) throws IOException{
	   
      // Run the GUI codes in the event-dispatching thread for thread safety
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
        	new JOGL2Setup_RendererMain();  // run the constructor
         }
      });
      
   }


}