package edu.gatech.gtmaps.models;

/**
 * Created by yingqiongshi on 9/27/16.
 */

//Useful functions on creating graphs.
public class GraphUtilities {
    GraphUtilities() {

    }

    Vec R(Vec V) {
        return new Vec(-V.y, V.x);
    };                                                             // V turned right 90 degrees (as seen on screen)
    Vec R(Vec V, float a) {
        return W((float)Math.cos(a), V, (float)Math.sin(a), R(V));
    }                                           // V rotated by angle a in radians
    float dot(Vec U, Vec V) {
        return U.x*V.x+U.y*V.y;
    }                                                     // dot(U,V): U*V (dot product U*V)
    float det(Vec U, Vec V) {
        return dot(R(U), V);
    }
    Vec W(float s, Vec V) {
        return V.setTo(s*V.x, s*V.y);
    }                                                      // sV
    Vec W(Vec U, Vec V) {
        return V.setTo(U.x+V.x, U.y+V.y);
    }                                                   // U+V
    Vec W(Vec U, float s, Vec V) {
        return W(U, S(s, V));
    }                                                   // U+sV
    Vec W(float u, Vec U, float v, Vec V) {
        return W(S(u, U), S(v, V));
    }                                   // uU+vV ( Linear combination)
    Vec S(float s, Vec V) {
        return new Vec(s*V.x, s*V.y);
    };

    //if v is on the right side of u(turns rught at u), return true. If v is on the left side of u, return false.
    boolean onTheRight(Vec u, Vec v) {
        return det(u,v) < 0;
    }
    //a is the top-right point, b is the down-left point. If p is on the right side of ab, return true. Else, return false.
    boolean onTheRight(Point p, Point a, Point b) {
        Vec ab = new Vec(b.x-a.x,b.y-a.y);
        Vec ap = new Vec(p.x-a.x,p.y-a.y);
        return onTheRight(ab, ap);
    }

}
