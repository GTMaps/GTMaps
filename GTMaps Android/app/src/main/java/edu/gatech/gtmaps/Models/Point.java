package edu.gatech.gtmaps.models;

/**
 * Created by yingqiongshi on 9/27/16.
 */

//Each room will be present a Point to indicate their position
public class Point {
    float x, y;

    Point(float px, float py) {
        x = px;
        y = py;
    };
    Point setTo(float px, float py) {
        x = px;
        y = py;
        return this;
    };
    Point setTo(Point P) {
        x = P.x;
        y = P.y;
        return this;
    };
//    Point setToMouse() {
//        x = mouseX;
//        y = mouseY;
//        return this;
//    };
    Point add(float u, float v) {
        x += u;
        y += v;
        return this;
    }                       // P.add(u,v): P+=<u,v>
    Point add(Point P) {
        x += P.x;
        y += P.y;
        return this;
    };                              // incorrect notation, but useful for computing weighted averages
    Point add(float s, Point P) {
        x += s*P.x;
        y += s*P.y;
        return this;
    };               // adds s*P
    Point add(Vec V) {
        x += V.x;
        y += V.y;
        return this;
    }                              // P.add(V): P+=V
    Point add(float s, Vec V) {
        x += s*V.x;
        y += s*V.y;
        return this;
    }                 // P.add(s,V): P+=sV
    Point translateTowards(float s, Point P) {
        x+=s*(P.x-x);
        y+=s*(P.y-y);
        return this;
    };  // transalte by ratio s towards P
    Point scale(float u, float v) {
        x*=u;
        y*=v;
        return this;
    };
    Point scale(float s) {
        x*=s;
        y*=s;
        return this;
    }                                  // P.scale(s): P*=s
    Point scale(float s, Point C) {
        x*=C.x+s*(x-C.x);
        y*=C.y+s*(y-C.y);
        return this;
    }    // P.scale(s,C): scales wrt C: P=L(C,P,s);
//    Point rotate(float a) {
//        float dx=x, dy=y, c=cos(a), s=sin(a);
//        x=c*dx+s*dy;
//        y=-s*dx+c*dy;
//        return this;
//    };     // P.rotate(a): rotate P around origin by angle a in radians
//    Point rotate(float a, Point G) {
//        float dx=x-G.x, dy=y-G.y, c=cos(a), s=sin(a);
//        x=G.x+c*dx+s*dy;
//        y=G.y-s*dx+c*dy;
//        return this;
//    };   // P.rotate(a,G): rotate P around G by angle a in radians
//    Point rotate(float s, float t, Point G) {
//        float dx=x-G.x, dy=y-G.y;
//        dx-=dy*t;
//        dy+=dx*s;
//        dx-=dy*t;
//        x=G.x+dx;
//        y=G.y+dy;
//        return this;
//    };   // fast rotate s=sin(a); t=tan(a/2);
//    pt moveWithMouse() {
//        x += mouseX-pmouseX;
//        y += mouseY-pmouseY;
//        return this;
//    };
    // DRAW , WRITE
//    pt write() {
//        print("("+x+","+y+")");
//        return this;
//    };  // writes Point coordinates in text window
//    pt v() {
//        vertex(x, y);
//        return this;
//    };  // used for drawing polygons between beginShape(); and endShape();
//    pt show(float r) {
//        ellipse(x, y, 2*r, 2*r);
//        return this;
//    }; // shows Point as disk of radius r
//    pt show() {
//        show(3);
//        return this;
//    }; // shows Point as small dot
//    pt label(String s, float u, float v) {
//        fill(black);
//        text(s, x+u, y+v);
//        noFill();
//        return this;
//    };
//    pt label(String s, Vec V) {
//        fill(black);
//        text(s, x+V.x, y+V.y);
//        noFill();
//        return this;
//    };

}
