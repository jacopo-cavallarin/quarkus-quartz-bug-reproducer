package io.test;

import io.quarkus.arc.Unremovable;
import io.quarkus.scheduler.ScheduledExecution;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.context.ManagedExecutor;

import java.util.function.Function;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

@Unremovable
@ApplicationScoped
public class ProgrammaticAsyncJob implements Function<ScheduledExecution, Uni<Void>> {
    public static final String IDENTITY = ProgrammaticAsyncJob.class.getSimpleName();

    private final Logger log = getLogger(getClass().getName());

    private final ManagedExecutor managedExecutor;

    @Inject
    public ProgrammaticAsyncJob(ManagedExecutor managedExecutor) {
        this.managedExecutor = managedExecutor;
    }

    @Override
    public Uni<Void> apply(ScheduledExecution scheduledExecution) {
        return Uni.createFrom()
                .completionStage(managedExecutor.runAsync(() -> log.info(() -> "QuartzAsyncJob has been executed")));
    }
}
