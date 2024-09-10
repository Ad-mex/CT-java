package game;

public class CircleBoard extends AbstractInRectangleBoard {

    public CircleBoard(int n, int k) throws Exception{
        super(n, n, k);
    }

    public boolean isInBoard(int r, int c) {
        return (2 * r - (getN() - 1)) * (2 * r - (getN() - 1)) + 
        (2 * c - (getN() - 1)) * (2 * c - (getN() - 1)) <= getN() * getN();
    }
    
}
