package Objects;

import java.util.Objects;


public class Vector2d {

    final public int x;
    final public int y;

    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return "(" + Integer.toString(this.x) + "," + Integer.toString(this.y) + ")";
    }

    public boolean precedes(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }

    public boolean follows(Vector2d other) {
        return this.x >= other.x && this.y >= other.y;
    }

    public Vector2d upperRight(Vector2d other) {
        int i = Math.max(this.x, other.x);
        int j = Math.max(this.y, other.y);
        return new Vector2d(i, j);
    }

    public Vector2d lowerLeft(Vector2d other){
        int i = Math.min(this.x, other.x);
        int j = Math.min(this.y, other.y);
        return new Vector2d(i, j);
    }

    public Vector2d add(Vector2d other){
        int i = this.x + other.x;
        int j = this.y + other.y;
        return new Vector2d(i, j);
    }

    public boolean equals(Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Vector2d)){
            return false;
        }
        Vector2d that = (Vector2d) other;
        return that.x == this.x && that.y == this.y;
    }

    public Vector2d opposite(){
        return new Vector2d(-this.x, -this.y);
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.x, this.y);
    }
}
