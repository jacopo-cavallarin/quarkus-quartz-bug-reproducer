# quarkus-quartz-bug-reproducer

This project is a reproducer for a bug in the `quarkus-quartz` extension.

## Description of the bug

The bug happens when scheduling asynchronous jobs _programmatically_ while using the `jdbc-cmt`
store for Quartz jobs.

The job details are stored in the `qrtz_job_details` when the job is first scheduled.
After that, retrieving the stored job programmatically results in an exception like this:

```shell
...
Caused by: java.lang.ClassCastException: class io.test.ProgrammaticAsyncJob_ClientProxy cannot be cast to class java.util.function.Consumer (io.test.ProgrammaticAsyncJob_ClientProxy is in unnamed module of loader io.quarkus.bootstrap.classloading.QuarkusClassLoader @463c632c; java.util.function.Consumer is in module java.base of loader 'bootstrap')
        at io.quarkus.quartz.runtime.QuartzSchedulerImpl$SerializedExecutionMetadata.task(QuartzSchedulerImpl.java:1305)
        at io.quarkus.quartz.runtime.QuartzSchedulerImpl.createJobDefinitionQuartzTrigger(QuartzSchedulerImpl.java:928)
        at io.quarkus.quartz.runtime.QuartzSchedulerImpl.<init>(QuartzSchedulerImpl.java:312)
        ... 29 more

```

The problem seems to be that quarkus is attempting to read the async job as a synchronous one
(as it's attempting to cast it to a `Consumer<ScheduledExecution>` instead
of `Function<ScheduledExecution, Uni<Void>>`).

## Structure

This application schedules two asynchronous quartz jobs to be run every second.
One job ([DeclarativeAsyncJob](src/main/java/io/test/DeclarativeAsyncJob.java)) is scheduled
declaratively with `@Scheduled`,
the other ([ProgrammaticAsyncJob](src/main/java/io/test/ProgrammaticAsyncJob.java)) programmatically
with `QuartzScheduler` at
startup ([ProgrammaticJobInitializer](src/main/java/io/test/ProgrammaticJobInitializer.java)).

Both jobs just log a message on each execution.

## Steps to reproduce the bug

1. Launch the application with
    ```shell
    ./mvnw quarkus:dev
    ```
2. Notice that both jobs are printing a log message every second.
3. Restart the application by pressing `s`.
4. The application fails to start due to the exception mentioned above.
