//Teclado Helena is a keyboard designed to better the experience mainly of users with cerebral palsy.
//        Copyright (C) 2024  Instituto de Pesquisas Tecnológicas
//This file is part of Teclado Helena.
//
//        Teclado Helena is free software: you can redistribute it and/or modify
//        it under the terms of the GNU General Public License as published by
//        the Free Software Foundation, either version 3 of the License, or
//        (at your option) any later version.
//
//        Teclado Helena is distributed in the hope that it will be useful,
//        but WITHOUT ANY WARRANTY; without even the implied warranty of
//        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//        GNU General Public License for more details.
//
//        You should have received a copy of the GNU General Public License
//        along with Teclado Helena. If not, see <https://www.gnu.org/licenses/>6.

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
    public static final String OS = TASK_PREFIX + "os";
    public static final String DEFAULT = TASK_PREFIX + "default";

    private static ThreadFactory threadFactory(final String threadNamePrefix) {
        return r -> {
            var thread = new Thread(r);
            thread.setName(threadNamePrefix + "-" + thread.getId());
            return thread;
        };
    }

    @Bean(OS)
    public Executor sendKeysExecutor() {
        return Executors.newSingleThreadExecutor(
                threadFactory(OS)
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
