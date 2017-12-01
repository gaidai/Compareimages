/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compareimages;


public class Rect {
    int x1;
    int y1;
    int x2;
    int y2;

    public Rect(int x1, int y1) {
        this.x1 = x1;
        this.y1 = y1;
    }

    public Rect(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Rect() {
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.x1;
        hash = 53 * hash + this.y1;
        hash = 53 * hash + this.x2;
        hash = 53 * hash + this.y2;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rect other = (Rect) obj;
        if (this.x1 != other.x1) {
            return false;
        }
        if (this.y1 != other.y1) {
            return false;
        }
        if (this.x2 != other.x2) {
            return false;
        }
        if (this.y2 != other.y2) {
            return false;
        }
        return true;
    }

    
    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }
    
  
    
}
