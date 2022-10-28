package parallel;

import model.Model;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ParallelComputer {

    public static ExecutorService executorService = Executors.newFixedThreadPool(Model.nThreads);

    public static void compute(List<Runnable> tasks) {

        CompletableFuture<?>[] futures = tasks.stream()
                .map(task -> CompletableFuture.runAsync(task, executorService))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();

    }

}
