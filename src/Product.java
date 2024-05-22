import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Product implements Comparable<Product> {
    String name;
    String category;
    double cost;

    public Product(String name, String category, double cost) {
        this.name = name;
        this.category = category;
        this.cost = cost;
    }

    @Override
    public int compareTo(Product other) {
        return Double.compare(this.cost, other.cost);
    }

    @Override
    public String toString() {
        return name + ", " + category + ", " + cost;
    }
}

class ProductFileReader implements Runnable {
    private final String filepath;
    private final PriorityQueue<Product> productQueue;

    public ProductFileReader(String filepath, PriorityQueue<Product> productQueue) {
        this.filepath = filepath;
        this.productQueue = productQueue;
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(new File(filepath))) {
            while (scanner.hasNext()) {
                String name = scanner.next();
                String category = scanner.next();
                double cost = scanner.nextDouble();
                synchronized (productQueue) {
                    productQueue.add(new Product(name, category, cost));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
