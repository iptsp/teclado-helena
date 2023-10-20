package br.ipt.thl.common.func;

public interface CheckedPredicate<T> {
    boolean test(T t) throws Exception;
}
