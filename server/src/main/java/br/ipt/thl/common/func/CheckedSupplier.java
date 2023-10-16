package br.ipt.thl.common.func;

@FunctionalInterface
public interface CheckedSupplier<T> {
    T get() throws Exception;
}