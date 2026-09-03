package engine.utilities;

public class DVector2 {

    private double x;
    private double y;

    public DVector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public DVector2(DVector2 newVect) {
        x = newVect.x();
        y = newVect.y();
    }

    public double x() {
        return this.x;
    }

    public int xToInt() {
        return (int) this.x;
    }

    public double y() {
        return this.y;
    }

    public int yToInt() {
        return (int) this.x;
    }

    public void setVect(double dx, double dy) {
        x = dx;
        y = dy;
    }

    public void setVect(DVector2 newVect) {
        x = newVect.x();
        y = newVect.y();
    }

    public DVector2 add(DVector2 sndVect) {
        return new DVector2(x + sndVect.x(), y + sndVect.y());
    }

    public static DVector2 add(DVector2 fstVect, DVector2 sndVect) {
        return new DVector2(fstVect.x() + sndVect.x(), fstVect.y() + sndVect.y());
    }

    public DVector2 substract(DVector2 sndVect) {
        return new DVector2(x - sndVect.x(), y - sndVect.y());
    }

    public static DVector2 substract(DVector2 fstVect, DVector2 sndVect) {
        return new DVector2(fstVect.x() - sndVect.x(), fstVect.y() - sndVect.y());
    }

    public DVector2 multiply(double k) {
        return new DVector2(x * k, y * k);
    }

    public double squaredDist(DVector2 sndVect) {
        double dx = sndVect.x - x;
        double dy = sndVect.y - y;

        return dx * dx + dy * dy;
    }

    public DVector2 normalize() {
        double length = Math.sqrt(x * x + y * y);

        if (length == 0) {
            return new DVector2(0, 0);
        }

        return new DVector2(x / length, y / length);
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
