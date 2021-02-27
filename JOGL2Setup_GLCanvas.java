
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import static com.jogamp.opengl.GL.*;  // GL constants
import static com.jogamp.opengl.GL2.*; // GL2 constants

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * Based on :http://www3.ntu.edu.sg/home/ehchua/programming/opengl/JOGL2.0.html with minor modifications
 * JOGL 2.0 Program Template (GLCanvas)
 * This is a "Component" which can be added into a top-level "Container".
 * It also handles the OpenGL events to render graphics.
 */

@SuppressWarnings("serial")
public class JOGL2Setup_GLCanvas extends GLCanvas implements MouseListener,KeyListener,GLEventListener, MouseMotionListener, MouseWheelListener {
   private GLU glu;  // for the GL Utility
   Model jet = new Model();
   ColourMap map = new ColourMap();
   Boolean fill = true;
   int c_width;
   int c_height;
   
   /** Constructor to setup the GUI for this Component */
   public JOGL2Setup_GLCanvas() {
      this.addGLEventListener(this);
      this.addMouseMotionListener(this);
      this.addMouseListener(this);
      this.addMouseWheelListener(this);
      this.addKeyListener(this);
   }
 
   // ------ Implement methods declared in GLEventListener ------
 
   /**
    * Called back immediately after the OpenGL context is initialized. Can be used
    * to perform one-time initialization. Run only once.
    */
   @Override
   public void init(GLAutoDrawable drawable) {
      GL2 gl = drawable.getGL().getGL2();      // get the OpenGL graphics context
      glu = new GLU();                         // get GL Utilities
      gl.glClearColor(jet.background[0], jet.background[1], jet.background[2], jet.background[3]); // set background (clear) color
      gl.glClearDepth(1.0f);      // set clear depth value to farthest
      gl.glEnable(GL_DEPTH_TEST); // enables depth testing
      gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
      gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
      gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
      gl.glEnable(GL2.GL_NORMALIZE);
     
      // ----- Your OpenGL initialization code here -----
      
      // Light0 (L0) position: {x, y, z, w}
      //float light0_pos[] = {1.0f, 2.0f, 3.0f, 1.0f};
      
      //float ambient0[] = {1.0f, 0.0f, 0.0f, 0.0f};
      //float diffuse0[] = {1.0f, 0.0f, 0.0f, 0.0f};
      //float specular0[] = {1.0f, 1.0f, 1.0f, 1.0f};
     
      float lmodel_ambient[]= {0.2f, 0.2f, 0.2f, 1.0f};
      
      //gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light0_pos,0);// L0 position
      //gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient0,0); // L0 ambient color
      //gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse0,0); // L0 diffuse color
      //gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specular0,0); // L0 specular color
      
      gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, lmodel_ambient,0);
      
      
      gl.glEnable(GL2.GL_LIGHTING);
      //gl.glEnable(GL2.GL_LIGHT0); // set L0’s properties from arrays above
      
   }
 
   /**
    * Call-back handler for window re-size event. Also called when the drawable is
    * first set to visible.
    */
   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
      if (height == 0) height = 1;   // prevent divide by zero
      float aspect = (float)width / height;
      
      // used to manipulate mouse
      c_width = width;
      c_height = height;
      
      gl.glViewport(0, 0, width, height);
 
      // Setup perspective projection, with aspect ratio matches viewport
      gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
      gl.glLoadIdentity();             // reset projection matrix
      
      // logic for Orth/Proj
      if(!jet.orth){
    	  glu.gluPerspective(90.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar
      }
      else{
    	  gl.glOrtho(-20.0, 20.0, -20.0 , 20.0 , -20.0, 20.0); 
      }
      
      // Enable the model-view transform
      gl.glMatrixMode(GL_MODELVIEW);
      gl.glLoadIdentity(); // reset
   }
 
   /**
    * Called back by the animator to perform rendering.
    */
   @Override
   public void display(GLAutoDrawable drawable) {
	   if(fill){
		   drawFill(drawable);
	   } 
	   else{
		   drawStrip(drawable);
	   }
   }
   
   public void drawFill(GLAutoDrawable drawable){
	   // called if fill variable set to true
	   GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
	      gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
	      gl.glLoadIdentity();  // reset the model-view matrix
	      
	      gl.glTranslatef(jet.xPos, jet.yPos, jet.zPos);
	      gl.glRotatef(jet.rotateX, 1.0f, 0.0f, 0.0f);
	      gl.glRotatef(jet.rotateY, 0.0f, 1.0f, 0.0f);
	      gl.glRotatef(jet.rotateZ, 0.0f, 0.0f, 1.0f);
	      
	      for(int k = 0; k < jet.model.length; k ++){
		      for(int i = 0; i < jet.model[k].polygons.length; i++){ 
		    	  gl.glBegin(GL_QUADS);  
		    	  for(int j = 0; j < jet.model[k].polygons[i].length; j++){
		    		  	int index = map.colourIndex(jet.model[k].vertices[jet.model[k].polygons[i][j]-1][jet.s_index], jet.min_s, jet.max_s);
		    		  	gl.glColor3f(map.colour_map[index][1], map.colour_map[index][2], map.colour_map[index][3]);
		    		  	float [] emission = {map.colour_map[index][1], map.colour_map[index][2], map.colour_map[index][3],1.0f};
		    		  	gl.glMaterialfv(GL2.GL_FRONT,GL2.GL_EMISSION, emission,0);
		  	 	 		gl.glVertex3f(jet.model[k].vertices[jet.model[k].polygons[i][j]-1][0], jet.model[k].vertices[jet.model[k].polygons[i][j]-1][1], jet.model[k].vertices[jet.model[k].polygons[i][j]-1][2]);	      	 
		  	
		    	  }
		    	  gl.glEnd();
		      }
	      }
	      
	 	 
   }
   
   public void drawStrip(GLAutoDrawable drawable){
	   // called if fill variable set to false
	   GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
	      gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
	      gl.glLoadIdentity();  // reset the model-view matrix
	      
	      gl.glTranslatef(jet.xPos, jet.yPos, jet.zPos);
	      gl.glRotatef(jet.rotateX, 1.0f, 0.0f, 0.0f);
	      gl.glRotatef(jet.rotateY, 0.0f, 1.0f, 0.0f);
	      gl.glRotatef(jet.rotateZ, 0.0f, 0.0f, 1.0f);
	      
	      for(int k = 0; k < jet.model.length; k ++){
		      for(int i = 0; i < jet.model[k].polygons.length; i++){ 
		    	  gl.glBegin(GL_LINE_STRIP);  
		    	  for(int j = 0; j < jet.model[k].polygons[i].length; j++){
		    		  	int index = map.colourIndex(jet.model[k].vertices[jet.model[k].polygons[i][j]-1][jet.s_index], jet.min_s, jet.max_s);
		    		  	gl.glColor3f(map.colour_map[index][1], map.colour_map[index][2], map.colour_map[index][3]);
		    		  	float [] emission = {map.colour_map[index][1], map.colour_map[index][2], map.colour_map[index][3],1.0f};
		    		  	gl.glMaterialfv(GL2.GL_FRONT,GL2.GL_EMISSION, emission,0);
		  	 	 		gl.glVertex3f(jet.model[k].vertices[jet.model[k].polygons[i][j]-1][0], jet.model[k].vertices[jet.model[k].polygons[i][j]-1][1], jet.model[k].vertices[jet.model[k].polygons[i][j]-1][2]);	      	 
		  	
		    	  }
		    	  gl.glEnd();
		      }
	      }
	      
   }
   
   /**
    * Called back before the OpenGL context is destroyed. Release resource such as buffers.
    */
   @Override
   public void dispose(GLAutoDrawable drawable) { }


	@Override
	public void mouseDragged(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)){
			if(e.getY() < c_height/2){
				jet.yPos = jet.yPos + 0.2f;
			}
			else if( e.getY() > c_height/2){
				jet.yPos = jet.yPos - 0.2f;
			}
		}
		
		else{
			if(e.getX() > c_width/2){
				jet.xPos = jet.xPos + 0.2f;
			}
			else if( e.getX() < c_width/2){
				jet.xPos = jet.xPos - 0.2f;
			}
		
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		float tempY = (float)e.getY();
		
		if(tempY > c_height/2){
			jet.rotateX = jet.rotateX + 0.2f;
		}
		
		else{
			jet.rotateX = jet.rotateX - 0.2f;
		}
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		 
		if(e.getPreciseWheelRotation() > 0){
			jet.zPos = jet.zPos + 0.2f;
		}
		else{
			jet.zPos = jet.zPos - 0.2f;
		}
		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyChar() == 'w'){
			jet.rotateZ = jet.rotateZ + 0.5f;
		}
		else if (e.getKeyChar() == 's'){
			jet.rotateZ = jet.rotateZ - 0.5f;
		}
		else if (e.getKeyChar() == 'a'){
			jet.rotateY = jet.rotateY - 0.5f;
		}
		else if (e.getKeyChar() == 'd'){
			jet.rotateY = jet.rotateY + 0.5f;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(fill){
			fill = false;
		}
		else{
			fill = true;
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	
}