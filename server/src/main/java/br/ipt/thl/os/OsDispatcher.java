package br.ipt.thl.os;

import java.util.concurrent.CompletableFuture;

public interface OsDispatcher {

    CompletableFuture<Void> keyPress(final int keyCode);
}
