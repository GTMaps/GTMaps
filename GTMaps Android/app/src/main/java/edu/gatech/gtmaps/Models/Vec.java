package edu.gatech.gtmaps.Models;

/**
 * Created by yingqiongshi on 9/27/16.
 */

public class Vec {
    float x, y;
    Vec(float px, float py) {
        x = px;
        y = py;
    };

    // MODIFY
    Vec setTo(float px, float py) {
        x = px;
        y = py;
        return this;
    };
    Vec setTo(Vec V) {
        x = V.x;
        y = V.y;
        return this;
    };
    Vec zero() {
        x=0;
        y=0;
        return this;
    }
    Vec scaleBy(float u, float v) {
        x*=u;
        y*=v;
        return this;
    };
    Vec scaleBy(float f) {
        x*=f;
        y*=f;
        return this;
    };
    Vec reverse() {
        x=-x;
        y=-y;
        return this;
    };
    Vec divideBy(float f) {
        x/=f;
        y/=f;
        return this;
    };
    Vec normalize() {
        double n=Math.sqrt(x*x+y*y);
        if (n>0.000001) {
            x/=n;
            y/=n;
        };
        return this;
    };
    Vec add(float u, float v) {
        x += u;
        y += v;
        return this;
    };
    Vec add(Vec V) {
        x += V.x;
        y += V.y;
        return this;
    };
    Vec add(float s, Vec V) {
        x += s*V.x;
        y += s*V.y;
        return this;
    };
//    Vec rotateBy(float a) {
//        float xx=x, yy=y;
//        x=xx*cos(a)-yy*sin(a);
//        y=xx*sin(a)+yy*cos(a);
//        return this;
//    };
    Vec left() {
        float m=x;
        x=-y;
        y=m;
        return this;
    };

//    // OUTPUT VEC
//    Vec clone() {
//        return(new Vec(x, y));
//    };

    // OUTPUT TEST MEASURE
    double norm() {
        return(Math.sqrt(x*x+y*y));
    }
    boolean isNull() {
        return((Math.abs(x)+Math.abs(y)<0.000001));
    }
    double angle() {
        return(Math.atan2(y, x));
    }

    // DRAW, PRINT
    void write() {
        System.out.println("<"+x+","+y+">");
    };
//    void showAt (pt P) {
//        line(P.x, P.y, P.x+x, P.y+y);
//    };
//    void showArrowAt (pt P)
//    {
//        line(P.x, P.y, P.x+x, P.y+y);
//        float n=min(this.norm()/10., height/50.);
//        pt Q=P(P, this);
//        Vec U = S(-n, U(this));
//        Vec W = S(.3, R(U));
//        beginShape();
//        Q.add(U).add(W).v();
//        Q.v();
//        Q.add(U).add(M(W)).v();
//        endShape(CLOSE);
//    }
//
//    void label(String s, pt P) {
//        P(P).add(0.5, this).add(3, R(U(this))).label(s);
//    }
}
