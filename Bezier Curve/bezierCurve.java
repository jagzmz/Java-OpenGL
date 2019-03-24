import com.sun.opengl.util.Animator;
import com.sun.opengl.util.GLUT;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.time;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.lang.Math.pow;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

class BezierCurve implements GLEventListener {

 private GLU glu;
 int c = 0;
 int xx;
 boolean state = false;
 ArrayList < Pair < Integer, Integer >> plottedPts = new ArrayList < Pair < Integer, Integer >> ();
 public void setxx(int n) {
  xx = n;
 }
 public void init(GLAutoDrawable gld) {
  GL gl = gld.getGL();
  glu = new GLU();

  gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
  gl.glViewport(0, 0, 640, 480);
  gl.glMatrixMode(GL.GL_PROJECTION);
  gl.glLoadIdentity();
  glu.gluOrtho2D(0, 640, 0, 480);


 }

 public void display(GLAutoDrawable drawable) {
  GL gl = drawable.getGL();

  // Clear the drawing area
  gl.glClear(GL.GL_COLOR_BUFFER_BIT); // GL.GL_DEPTH_BUFFER_BIT);


  gl.glBegin(GL.GL_LINES);

  gl.glVertex2i(10, 0);
  gl.glVertex2i(10, 480);

  gl.glVertex2i(10, 240);
  gl.glVertex2i(640, 240);

  gl.glEnd();



  showPoints(state, gl);



  bezireCurve(plottedPts, gl);
  //        System.out.println(plottedPts.size());





  //        /*drawThickLine(390,230,470,310,gl);*/
  //        xu = pow(1-u,3)*x[0]+3*u*pow(1-u,2)*x[1]+3*pow(u,2)*(1-u)*x[2]+pow(u,3)*x[3]; 
  //                yu = pow(1-u,3)*y[0]+3*u*pow(1-u,2)*y[1]+3*pow(u,2)*(1-u)*y[2]+pow(u,3)*y[3]; 
 }


 private void bezireCurve(ArrayList < Pair < Integer, Integer >> plottedPts, GL gl) {
  try {

   gl.glColor3f(1.0f, 1.0f, 1.0f);

   gl.glPointSize(1f);
   gl.glBegin(GL.GL_POINTS);







   double xu = 0.0, yu = 0.0, u = 0.0, cc = 0.0;
   int i = 0;



   for (u = 0.0; u <= 1.0; u += 0.0001) {
    xu = 0.0;
    yu = 0.0;

    for (i = 0; i < plottedPts.size(); i++) {
     cc = ((double) factorial(plottedPts.size() - 1) / (factorial((plottedPts.size() - 1) - i) * factorial(i))) * pow(u, i) * pow(1 - u, (plottedPts.size() - 1) - i);
     xu += cc * plottedPts.get(i).getKey();
     yu += cc * plottedPts.get(i).getValue();
     //                    if(plottedPts.size()>13)
     //                    System.out.println(xu+" "+yu);
    }

    gl.glVertex2d(xu, yu);

   }


   gl.glEnd();


  } catch (Exception e) {}
 }




 public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}

 public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {

 }

 private double factorial(int x) {

  int i, f = 1;
  for (i = 1; i <= x; i++)
   f *= i;
  return f;


 }

 private void showPoints(boolean state, GL gl) {

  if (state) {
   gl.glPointSize(6f);
   gl.glBegin(GL.GL_POINTS);
   gl.glColor3f(1.0f, 0.0f, 0.0f);

   for (int i = 0; i < plottedPts.size(); i++) {
    gl.glVertex2i(plottedPts.get(i).getKey(), plottedPts.get(i).getValue());

   }
   gl.glEnd();


   gl.glPointSize(1f);
   gl.glBegin(GL.GL_LINE_STRIP);
   gl.glColor3f(1.1f, 0.0f, 0.0f);

   for (int i = 0; i < plottedPts.size(); i++) {
    gl.glVertex2i(plottedPts.get(i).getKey(), plottedPts.get(i).getValue());

   }
   gl.glEnd();
  }
 }




}


public class bezierCurve {
 public static void main(String[] args) {
  GLCapabilities capabilities = new GLCapabilities();
  final GLCanvas glcanvas = new GLCanvas(capabilities);
  final BezierCurve b = new BezierCurve();

  glcanvas.addGLEventListener(b);
  glcanvas.setSize(400, 400);
  final JFrame frame = new JFrame("Basic frame");
  frame.add(glcanvas);
  frame.setSize(640, 480);
  //    frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
  frame.setVisible(true);
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  final Animator animator = new Animator(glcanvas);
  frame.addWindowListener(new WindowAdapter() {

   @Override
   public void windowClosing(WindowEvent e) {
    // Run this on another thread than the AWT event queue to
    // make sure the call to Animator.stop() completes before
    // exiting
    new Thread(new Runnable() {

     public void run() {
      animator.stop();
      System.exit(0);
     }
    }).start();
   }
  });


  glcanvas.addKeyListener(new KeyListener() {

   public void keyTyped(KeyEvent e) {



   }

   public void keyPressed(KeyEvent e) {



    try {

     switch (e.getKeyCode()) {
      case KeyEvent.VK_BACK_SPACE:
       b.plottedPts.remove(b.plottedPts.size() - 1);
       break;
      case KeyEvent.VK_ENTER:

       b.state = b.state == false ? true : false;
       break;
      case KeyEvent.VK_DELETE:
       b.plottedPts.clear();

     }

    } catch (ArrayIndexOutOfBoundsException ex) {
     System.out.println("No points defined");
    }
   }

   public void keyReleased(KeyEvent e) {
    //                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }
  });

  glcanvas.addMouseListener(new MouseListener() {

   public void mouseClicked(MouseEvent e) {

    if (e.getButton() == MouseEvent.BUTTON1) {


     int x = e.getX();
     int y = 460 - e.getY();


     System.out.println(x + " " + y);

     b.plottedPts.add(new Pair < Integer, Integer > (x, y));

    }

   }

   public void mousePressed(MouseEvent e) {
    //                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void mouseReleased(MouseEvent e) {
    //                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void mouseEntered(MouseEvent e) {
    //                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void mouseExited(MouseEvent e) {
    //                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }
  });
  
  animator.start();




 }
}
