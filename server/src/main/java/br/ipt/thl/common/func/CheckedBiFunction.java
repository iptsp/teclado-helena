/*
 * Copyright © 2021-present Lenovo. All rights reserved.
 * Confidential and Restricted
 *
 */

package br.ipt.thl.common.func;

@FunctionalInterface
public interface CheckedBiFunction<T1, T2, R> {
    R apply(T1 t1, T2 t2) throws Exception;
}