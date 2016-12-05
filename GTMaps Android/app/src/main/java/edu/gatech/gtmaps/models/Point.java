package edu.gatech.gtmaps.models;

/**
 * Created by yingqiongshi on 9/27/16.
 */

//Each room will be present a Point to indicate their position
public class Point {
    float x, y;

    //constructor
    public Point(float px, float py) {
        x = px;
        y = py;
    }
    Point setTo(float px, float py) {
        x = px;
        y = py;
        return this;
    }
    Point setTo(Point P) {
        x = P.x;
        y = P.y;
        return this;
    }
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    /**
     * measure distance between point P and Q
     * @param P Q points
     * @return double distance between P,Q
     */
    public double d(Point P, Point Q) {
        return Math.sqrt(d2(P, Q));
    };                                                  



}
