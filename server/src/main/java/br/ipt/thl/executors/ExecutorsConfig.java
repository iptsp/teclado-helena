package br.ipt.thl.executors;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;

@Configuration
public class ExecutorsConfig {

    private static final String TASK_PREFIX = "thl-task-";
    public static final String SEND_KEYS = TASK_PREFIX + "send-keys";
    public static final String DEFAULT = TASK_PREFIX + "default";

    private static ThreadFactory threadFactory(final String threadNamePrefix) {
        return r -> {
            var thread = new Thread(r);
            thread.setName(threadNamePrefix + "-" + thread.getId());
            return thread;
        };
    }

    @Bean(SEND_KEYS)
    public Executor sendKeysExecutor() {
        return Executors.newSingleThreadExecutor(
                threadFactory(SEND_KEYS)
        );
    }

    @Bean
    public Executor taskExecutor() {
        var threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(4);
        threadPoolTaskExecutor.setMaxPoolSize(4);
        threadPoolTaskExecutor.setQueueCapacity(20);
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        threadPoolTaskExecutor.setThreadNamePrefix(DEFAULT);
        threadPoolTaskExecutor.setRejectedExecutionHandler((runnable, threadPoolExecutor) -> {
            throw new RejectedExecutionException("Task " + runnable + " was rejected from " + threadPoolExecutor);
        });
        return threadPoolTaskExecutor;
    }
}
