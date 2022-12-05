import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    static BlockingQueue<String> maxCountA = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> maxCountB = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> maxCountC = new ArrayBlockingQueue<>(100);
    static int amountLines = 10_000;
    static int textLength = 100_000;

    public static void main(String[] args) {
        new Thread(() -> {
            for (int i = 0; i < amountLines; i++) {
                String line = generateText("abc", textLength);
                try {
                    maxCountA.put(line);
                    maxCountB.put(line);
                    maxCountC.put(line);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            try {
                int maxCountChar = maxCountLetter('a', maxCountA);
                System.out.println("Max amount \"a\" in line: " + maxCountChar);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                int maxCountChar = maxCountLetter('b', maxCountB);
                System.out.println("Max amount \"b\" in line: " + maxCountChar);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                int maxCountChar = maxCountLetter('c', maxCountC);
                System.out.println("Max amount \"c\" in line: " + maxCountChar);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int maxCountLetter(char letter, BlockingQueue<String> blockingQueue) throws InterruptedException {
        int maxCharCount = 0;
        for (int i = 0; i < amountLines; i++) {
            String line = blockingQueue.take();
            int temp = line.length() - line.replaceAll(String.valueOf(letter), "").length();
            if (temp > maxCharCount) {
                maxCharCount = temp;
            }
        }
        return maxCharCount;
    }
}
