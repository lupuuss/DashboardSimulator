package p.lodz.dashboardsimulator.modules.dashboard.console;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Provides user input from System.in as a stream of commands represented by {@link Observable} of {@link String}.
 * It starts the job on first subscription and ends on the last disposal. It must not be recycled and read cannot be run twice.
 */
public class ConsoleCommandsReader {

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private boolean running = false;

    /**
     * Initializes {@link Observable} that provides a stream of commands from System.in and returns it.
     * Cannot be called twice.
     * @return {@link Observable} that provides a stream of commands
     * @throws IllegalStateException When called more than once.
     */
    public Observable<String> read() {

        if (running) {
            throw new IllegalStateException("ConsoleCommandsReader#read() cannot be called twice!");
        } else {
            running = true;
        }

        return Observable
                .create((ObservableOnSubscribe<String>) emitter -> {

                    Scanner scanner = new Scanner(System.in);

                    do {

                        emitter.onNext(scanner.nextLine());

                    } while (!emitter.isDisposed());

                    executor.shutdown();
                })
                .subscribeOn(Schedulers.from(executor));
    }

    /**
     * It allows to wait for the current read operation to end.
     */
    public void await() {
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
