package leitner;

import java.util.ArrayList;
import java.util.random.RandomGenerator;

public class Bucket {
    ArrayList<Pair> pairs;

    public Bucket() {
        pairs = new ArrayList<>();
    }

    int getSize() {
        return pairs.size();
    }

    Pair getRandom(RandomGenerator gen){
        int index = gen.nextInt(0, getSize());
        return pairs.get(index);
    }

    void erasePair(Pair pair) {
        pairs.remove(pair);
    }

    void addPair(Pair pair) {
        pairs.add(pair);
    }
}
