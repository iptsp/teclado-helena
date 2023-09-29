/*
 * Copyright © 2021-present Lenovo. All rights reserved.
 * Confidential and Restricted
 *
 */

package br.ipt.thl.common.func;

@FunctionalInterface
public interface CheckedSupplier<T> {
    T get() throws Exception;
}