package io.test;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.context.ManagedExecutor;

import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.logging.Logger.getLogger;

@ApplicationScoped
public class DeclarativeAsyncJob {
    private final Logger log = getLogger(getClass().getName());

    private final ManagedExecutor managedExecutor;

    @Inject
    public DeclarativeAsyncJob(ManagedExecutor managedExecutor) {
        this.managedExecutor = managedExecutor;
    }

    @Scheduled(every = "1s")
    public CompletionStage<Void> run() {
        return runAsync(() -> log.info(() -> "DeclarativeAsyncJob has been executed"), managedExecutor);
    }
}
