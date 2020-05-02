package p.lodz.dashboardsimulator.modules.dashboard.console;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class ConsoleCommandsReader {

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public Observable<String> read() {
        return Observable
                .create((ObservableOnSubscribe<String>) emitter -> {
                    try (Scanner scanner = new Scanner(System.in)) {
                        do {
                            emitter.onNext(scanner.nextLine());

                        } while (!emitter.isDisposed());
                    }
                    executor.shutdown();
                })
                .subscribeOn(Schedulers.from(executor));
    }

    public void await() {
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
