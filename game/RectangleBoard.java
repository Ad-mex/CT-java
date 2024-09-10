package game;

public class RectangleBoard extends AbstractInRectangleBoard{

    public RectangleBoard(int m, int n, int k) throws Exception{
        super(m, n, k);
    }

    public boolean isInBoard(int r, int c) {
        return 0 <= r && r < getN() && 0 <= c && c < getM();
    }
    
}