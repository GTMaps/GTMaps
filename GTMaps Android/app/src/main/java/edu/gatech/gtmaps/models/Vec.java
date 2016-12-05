package edu.gatech.gtmaps.models;

/**
 * Created by yingqiongshi on 9/27/16.
 */

public class Vec {
    float x, y;

    //constructor
    Vec(float px, float py) {
        x = px;
        y = py;
    };

    //constructor
    public Vec(Point P, Point Q) {
        x = Q.x-P.x;
        y = Q.y-P.y;
    }

  
    /**
     * left this be the current forward direction. If this method returns true, the turn is a left turn.Else it is a right turn
     * @param prevForwardDirection previous forard direction
     * @return Boolean if the turn is a left turn.
     */  
    public boolean isLeftTurn(Vec prevForwardDirection) {
        return det(prevForwardDirection,this) > 0;
    }
    /**
     * rotate in clockwise
     * @param a radian
     * @return Vec vector after rotation
     */  
    Vec rotateBy(float a) {
        float xx=x, yy=y;
        x= (float) (xx*Math.cos(a)-yy*Math.sin(a));
        y= (float) (xx*Math.sin(a)+yy*Math.cos(a));
        return this;
    }

     /**
     * dot(this,V): U*V (dot product U*V)
     * @param V R vectors to be calcuated
     * @return float dot product result
     */    
    float dot(Vec U, Vec V) {
        return U.x*V.x+U.y*V.y;
    } 

     /**
     * dot product for no normal of U and V    
     * @param V R vectors to be calcuated
     * @return float det product result
     */                                              
    float det(Vec U, Vec V) {
        return dot(R(U), V);
    }

    /**
     * V turned 90 degrees (clockwise)
     * @param V vector to be rotated
     * @return Vec vector after rotation
     */
    Vec R(Vec V) {
        return new Vec(-V.y, V.x);
    }                                                            


}
