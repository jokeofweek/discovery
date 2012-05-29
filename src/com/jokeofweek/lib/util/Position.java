package com.jokeofweek.lib.util;

/**
 * Utility class to make using coordinates easier.
 * @author Santiago Zapata
 */
/**
 * Utility class to make using coordinates easier.
 * @author Santiago Zapata
 */
public class Position {
    public int x,y;
	
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }


    public static int distance(int x1, int y1, int x2, int y2){
        return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow (y1 - y2, 2));
    }

    public static int distance(Position a, Position b){
        return (int) Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow (a.y - b.y, 2));
    }

    public boolean inSquareRange(Position a, int range)
    {
        if (Math.abs(a.x-this.x) > range)
            return false;

        if (Math.abs(a.y-this.y) > range)
            return false;

        return true;
    }

}
