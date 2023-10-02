/*
 * Copyright © 2021-present Lenovo. All rights reserved.
 * Confidential and Restricted
 *
 */

package br.ipt.thl.common.func;

public interface CheckedPredicate<T> {
    boolean test(T t) throws Exception;
}
