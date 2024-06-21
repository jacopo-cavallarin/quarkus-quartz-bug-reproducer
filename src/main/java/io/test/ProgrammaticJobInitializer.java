package io.test;

import io.quarkus.quartz.QuartzScheduler;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;

public class ProgrammaticJobInitializer {
    public void scheduleOnStartup(@Observes Startup startup, QuartzScheduler quartzScheduler) {
        if (quartzScheduler.getScheduledJob(ProgrammaticAsyncJob.IDENTITY) == null) {
            quartzScheduler
                    .newJob(ProgrammaticAsyncJob.IDENTITY)
                    .setAsyncTask(ProgrammaticAsyncJob.class)
                    .setInterval("1s")
                    .schedule();
        }
        quartzScheduler.resume(ProgrammaticAsyncJob.IDENTITY);
    }
}
