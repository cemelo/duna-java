import io.duna.core.eventbus.EventBus;
import io.duna.core.internal.DunaImpl;
import io.duna.core.internal.eventbus.MultithreadLocalEventBus;

import java.util.concurrent.Executors;

public class Main {
    public static void main(String... args) {
        EventBus eventBus = new MultithreadLocalEventBus(new DunaImpl(),
            Executors.newFixedThreadPool(2),
            Executors.newSingleThreadExecutor());

        eventBus.<String>inbound("test")
            .addListener(System.out::println);

        eventBus.<String>inbound("test2")
            .addListener(System.out::println);

        eventBus.queue("random", Math::random);

        new Thread(() -> {
            System.out.println("Mandando eventos 1");

            eventBus.<String>outbound("test")
                .setBody("Chamando test")
                .emit();
        }).start();

        new Thread(() -> {
            System.out.println("Mandando eventos 2");

            eventBus.<String>outbound("test2")
                .setBody("Chamando test2")
                .emit();
        }).start();

        new Thread(() -> {
            System.out.println("Chamando vários randoms");

            eventBus.inbound()
                .poll("random")
                .onComplete(System.out::println);

            eventBus.inbound()
                .poll("random")
                .onComplete(System.out::println);

            eventBus.inbound()
                .poll("random")
                .onComplete(System.out::println);
        }).start();
    }
}
