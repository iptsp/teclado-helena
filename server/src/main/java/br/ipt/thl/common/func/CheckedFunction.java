/*
 * Copyright © 2021-present Lenovo. All rights reserved.
 * Confidential and Restricted
 *
 */

package br.ipt.thl.common.func;

@FunctionalInterface
public interface CheckedFunction<T, R> {
    R apply(T t) throws Exception;
}