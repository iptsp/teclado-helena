/*
 * Copyright © 2021-present Lenovo. All rights reserved.
 * Confidential and Restricted
 *
 */

package br.ipt.thl.common.func;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Try<T> {

    static <T> Try<T> of(final T t) {
        return Try.of(() -> t);
    }

    static <T> Try<T> of(final CheckedSupplier<T> checkedSupplier) {
        try {
            return Try.success(checkedSupplier.get());
        } catch (Exception t) {
            return Try.failure(t);
        }
    }

    static <T, R> Function<T, Try<R>> of(final CheckedFunction<T, R> checkedFunction) {
        return t -> {
            try {
                return Try.success(checkedFunction.apply(t));
            } catch (Exception e) {
                return Try.failure(e);
            }
        };
    }

    static <T> Try<T> success(final T value) {
        return new Success<>(value);
    }

    static <T> Try<T> failure(final Exception exception) {
        return new Failure<>(exception);
    }

    static <T1 extends AutoCloseable> WithResources<T1> withResources(final CheckedSupplier<T1> checkedSupplier) {
        return new WithResources<>(checkedSupplier);
    }

    static <T1 extends AutoCloseable, T2 extends AutoCloseable> WithResources2<T1, T2> withResources(final CheckedSupplier<T1> checkedSupplier,
                                                                                                     final CheckedFunction<T1, T2> checkedFunction) {
        return new WithResources2<>(checkedSupplier, checkedFunction);
    }

    T get();

    Exception exception();

    boolean isSuccess();

    boolean isFailure();

    boolean isEmpty();

    default <R> Try<R> flatMap(final CheckedFunction<T, Try<R>> checkedFunction) {
        if (isFailure()) {
            return Try.failure(exception());
        }

        try {
            return checkedFunction.apply(get());
        } catch (Exception t) {
            return Try.failure(t);
        }
    }

    default Try<T> exceptionally(final Consumer<Exception> action) {
        if (isFailure()) {
            action.accept(exception());
        }
        return this;
    }

    default Try<T> filter(final CheckedPredicate<T> checkedPredicate) {

        if (isFailure()) {
            return this;
        }

        try {
            if (checkedPredicate.test(get())) {
                return this;
            }
            return Try.failure(new NoSuchElementException("Predicate does not match for " + get()));
        } catch (Exception t) {
            return Try.failure(t);
        }
    }

    default <R> Try<R> map(final CheckedFunction<T, R> checkedFunction) {
        if (isFailure()) {
            return Try.failure(exception());
        }

        try {
            return Try.success(checkedFunction.apply(get()));
        } catch (Exception t) {
            return Try.failure(t);
        }
    }

    default T orElse(T s) {
        if (isSuccess()) {
            return get();
        }
        return s;
    }

    default T orElseGet(final Supplier<T> supplier) {
        if (isSuccess()) {
            return get();
        }
        return supplier.get();
    }

    default T orElseThrow(final Function<Throwable, ? extends RuntimeException> function) {
        if (isSuccess()) {
            return get();
        }
        throw function.apply(exception());
    }

    default T orElseThrow() {
        if (isSuccess()) {
            return get();
        }
        var e = exception();
        if (e instanceof RuntimeException runtimeException) {
            throw runtimeException;
        }
        throw new RuntimeException(exception());
    }

    default Try<T> or(final CheckedSupplier<Try<T>> checkedSupplier) {

        if (isSuccess()) {
            return this;
        }

        try {
            return checkedSupplier.get();
        } catch (Exception e) {
            return Try.failure(e);
        }
    }

    default void match(final Consumer<T> success, Consumer<Exception> failure) {
        if (isSuccess()) {
            success.accept(get());
        } else {
            failure.accept(exception());
        }
    }


    record Success<T>(T value) implements Try<T> {

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public Exception exception() {
            throw new UnsupportedOperationException("Could not get cause of success");
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public boolean isFailure() {
            return false;
        }

    }

    record Failure<T>(Exception exception) implements Try<T> {

        @Override
        public T get() {
            throw new UnsupportedOperationException("Try getting value from Failure");
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

    }

    record WithResources<T extends AutoCloseable>(CheckedSupplier<T> checkedSupplier) {

        @SuppressWarnings("try")
        public <R> Try<R> of(final CheckedFunction<T, R> checkedFunction) {
            return Try.of(() -> {
                T t = checkedSupplier.get();
                try (t) {
                    return checkedFunction.apply(t);
                }
            });
        }

        public <R> Try<R> flatMap(final CheckedFunction<T, Try<R>> checkedFunction) {
            return of(t -> checkedFunction.apply(t).orElseThrow());
        }
    }

    record WithResources2<T1 extends AutoCloseable, T2 extends AutoCloseable>(
            CheckedSupplier<T1> checkedSupplier,
            CheckedFunction<T1, T2> checkedFunction) {

        @SuppressWarnings("try")
        public <R> Try<R> of(final CheckedBiFunction<T1, T2, R> checkedBiFunction) {
            return Try.of(() -> {
                T1 t1 = checkedSupplier.get();
                T2 t2 = checkedFunction.apply(t1);
                try (t1; t2) {
                    return checkedBiFunction.apply(t1, t2);
                }
            });
        }
    }

}
