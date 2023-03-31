import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        String[] texts = new String[25]; //25 строк
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000); //каждая строка
        }

        List<Future<Integer>> futureList = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        long startTs = System.currentTimeMillis(); // start time
        for (String text : texts) {
            futureList.add(executorService.submit(
                    new MyCallable(text)
            ));
        }

        int maxSize = 0;
        for(Future<Integer> future: futureList) {
            maxSize = Math.max(future.get(), maxSize);
        }

        long endTs = System.currentTimeMillis(); // end time

        System.out.println("Time: " + (endTs - startTs) + "ms");

        executorService.shutdown();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}