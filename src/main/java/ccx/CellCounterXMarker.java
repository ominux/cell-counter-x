package ccx;






/**
 *
 * @author Kurt De Vos
 */
public class CellCounterXMarker {
    private int x;
    private int y;
    private int z;
    
    /** Creates a new instance of Marker */
    public CellCounterXMarker() {
    }
    
    public CellCounterXMarker(int x, int y, int z) {
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

}
