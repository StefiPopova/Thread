import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final String[] FILE_PATHS = {"guess.txt", "calvinklein.txt", "trussardi.txt"};
    private static final PriorityQueue<Product> products = new PriorityQueue<>(Comparator.comparingDouble(p -> p.cost));

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (String filepath : FILE_PATHS) {
            executorService.execute(new ProductFileReader(filepath, products));
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);

        try (PrintWriter printWriter = new PrintWriter(new FileWriter("src/out.txt"))) {
            int counter = 0;
            while (!products.isEmpty() && counter < 10) {
                Product product = products.poll();
                if (product != null) {
                    printWriter.println(product.name);
                    counter++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}