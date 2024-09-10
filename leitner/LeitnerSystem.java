package leitner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.random.RandomGenerator;

public class LeitnerSystem {
    private static int bucketsCount = 10;

    private static ArrayList<Double> prob;

    private static void setProbabilities() {
        prob = new ArrayList<>();
        for (int i = 1; i <= bucketsCount; i++) {
            prob.add(Math.pow(1.5, -i));
        }
    }

    private static int getRandomBucket(HashSet<Integer> busyBuckets, RandomGenerator gen) {
        ArrayList<Double> partialSums = new ArrayList<>();
        ArrayList<Integer> orderedIndexes = new ArrayList<>();
        for (Integer index : busyBuckets) {
            partialSums.add((partialSums.isEmpty() ? 0 : partialSums.getLast()) + prob.get(index));
            orderedIndexes.add(index);
        }
        double rand = gen.nextDouble() * partialSums.getLast();
        int resultIndex = 0;
        for (int i = 0; i < orderedIndexes.size(); i++) {
            if (rand <= partialSums.get(i)) {
                resultIndex = orderedIndexes.get(i);
                break;
            }
        }
        return resultIndex;
    }

    private static void moveBetweenBuckets(int fromBucket, int toBucket, Pair pair, ArrayList<Bucket> buckets,
            HashSet<Integer> busyBuckets) {
        buckets.get(fromBucket).erasePair(pair);
        buckets.get(toBucket).addPair(pair);
        if (buckets.get(fromBucket).getSize() == 0) {
            busyBuckets.remove(fromBucket);
        }
        if (!busyBuckets.contains(toBucket)) {
            busyBuckets.add(toBucket);
        }
    }

    private static void getFromCache(Scanner cachedScanner, HashSet<Integer> busyBuckets, ArrayList<Bucket> buckets) {
        while (cachedScanner.hasNextInt()) {
            int index = cachedScanner.nextInt();
            int count = cachedScanner.nextInt();
            busyBuckets.add(index);
            for (int i = 0; i < count; i++) {
                try {
                    buckets.get(index).addPair(Pair.getPair(cachedScanner));
                } catch (Exception e) {
                }
            }
        }
    }

    private static void getFromFile(Scanner scanner, HashSet<Integer> busyBuckets, ArrayList<Bucket> buckets) {
        boolean inputFlag = true;
        while (inputFlag) {
            try {
                buckets.get(0).addPair(Pair.getPair(scanner));
            } catch (Exception e) {
                inputFlag = false;
            }
        }
        busyBuckets.add(0);
    }

    private static void loadToCache(HashSet<Integer> busyBuckets, ArrayList<Bucket> buckets, File cachedFile) {
        try (FileWriter writer = new FileWriter(cachedFile)) {
            for (int index : busyBuckets) {
                writer.write("" + index);
                writer.write(' ');
                writer.write("" + buckets.get(index).getSize());
                writer.write('\n');
                for (Pair pair : buckets.get(index).pairs) {
                    writer.write(pair.original);
                    writer.write(' ');
                    writer.write(pair.translate);
                    writer.write('\n');
                }
            }
        } catch (IOException e) {
            System.out.println("Cache failed");
        }
    }

    public static void main(String[] args) {
        RandomGenerator gen = new Random();
        if (args.length != 1) {
            System.out.println("Wrong format");
            return;
        }
        String name = args[0];
        String cachedName = ".cached " + name;
        File cachedFile = new File(cachedName);

        try (Scanner scanner = new Scanner(new File(name));
                Scanner inputScanner = new Scanner(System.in);
                Scanner cachedScanner = (cachedFile.exists() ? new Scanner(cachedFile) : null)) {

            setProbabilities();

            ArrayList<Bucket> buckets = new ArrayList<>(10);
            for (int i = 0; i < bucketsCount; i++) {
                buckets.add(new Bucket());
            }
            HashSet<Integer> busyBuckets = new HashSet<>();

            if (cachedScanner != null) {
                getFromCache(cachedScanner, busyBuckets, buckets);
            } else {
                getFromFile(scanner, busyBuckets, buckets);
            }

            while (true) {
                int bucketIndex = getRandomBucket(busyBuckets, gen);
                System.err.println(bucketIndex);
                System.err.println(buckets.get(bucketIndex).getSize());

                Pair current = buckets.get(bucketIndex).getRandom(gen);
                System.out.println("Word: " + current.original);
                String answer = inputScanner.nextLine();
                if (answer.isEmpty()) {
                    break;
                }
                if (!answer.equals(current.translate)) {
                    System.out.println("Wrong :(");
                    moveBetweenBuckets(bucketIndex, 0, current, buckets, busyBuckets);
                } else {
                    System.out.println("You're right!");
                    moveBetweenBuckets(bucketIndex, Math.min(bucketsCount, bucketIndex + 1), current, buckets,
                            busyBuckets);
                }
            }

            loadToCache(busyBuckets, buckets, cachedFile);

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        }
    }
}