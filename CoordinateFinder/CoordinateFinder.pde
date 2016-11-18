// Template for 2D projects
// Author: Jarek ROSSIGNAC
import processing.pdf.*;    // to save screen shots as PDFs, does not always work: accuracy problems, stops drawing or messes up some curves !!!

//**************************** global variables ****************************
pts P = new pts(); // class containing array of points, used to standardize GUI
float t=0, f=0;
boolean animate=true, fill=false, timing=false;
boolean lerp=true, slerp=true, spiral=true; // toggles to display vector interpoations
int ms=0, me=0; // milli seconds start and end for timing
int npts=20000; // number of points
pt A=P(100,100), B=P(300,300);
int counter = 0;
pt curP = new pt();
boolean addP = false;
//**************************** initialization ****************************
void setup()               // executed once at the begining 
  {
  size(800, 800);            // window size
  frameRate(30);             // render 30 frames per second
  smooth();                  // turn on antialiasing
  mapImage = loadImage("data/pic.jpg");  // load image from file pic.jpg in folder data *** replace that file with your pic of your own face
  P.declare(); // declares all points in P. MUST BE DONE BEFORE ADDING POINTS 
  // P.resetOnCircle(4); // sets P to have 4 points and places them in a circle on the canvas
  P.loadPts("data/pts");  // loads points form file saved with this program
  } // end of setup

//**************************** display current frame ****************************
void draw()      // executed at each frame
  {
  if(recordingPDF) startRecordingPDF(); // starts recording graphics to make a PDF
  
    background(white); // clear screen and paints white background
    //pen(black,3); fill(yellow); P.drawCurve(); P.IDs(); // shows polyloop with vertex labels
    //stroke(red); pt G=P.Centroid(); show(G,10); // shows centroid
    //pen(green,5); arrow(A,B);            // defines line style wiht (5) and color (green) and draws starting arrow from A to B

P.savePts("data/pts"); 
  if(recordingPDF) endRecordingPDF();  // end saving a .pdf file with the image of the canvas

  fill(black); displayHeader(); // displays header
  if(scribeText && !filming) displayFooter(); // shows title, menu, and my face & name 

  if(filming && (animating || change)) snapFrameToTIF(); // saves image on canvas as movie frame 
  if(snapTIF) snapPictureToTIF();   
  if(snapJPG) snapPictureToJPG();   
  change=false; // to avoid capturing movie frames when nothing happens
  for (int i=0; i<P.nv; i++) {
    pt p = P.G[i];
       pen(green,3); 
       show(P.G[i]);

       pt a = new pt(542.0,123.0);
       pt b = new pt(115.0,189.0);
       pt c = new pt(115.0,235.0);
       pt d = new pt(93,235);
       //show(a); 
       //show(b);show(c);show(d);
       String s = P.G[i].x%10+"";
       //+","+P.G[i].y;
       //text(s,p.x+5,p.y+5);
       } 
       if(addP) {
       StringBuilder toAppend =new StringBuilder();
       if(counter == 0) {
         toAppend.append(curP.x+","+curP.y);
       } else {
         toAppend.append("\n"+curP.x+","+curP.y);
       }
       counter++;
       if(counter == 5) {
         toAppend.append("\n\n");
         counter = 0;
       }
       //System.out.println("aaaa"+toAppend.toString()+"aaaa");
   appendTextToFile("roomPts", toAppend.toString());
   addP = false;
       }
  }  // end of draw
  