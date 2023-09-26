package br.ipt.thl.os;

import java.util.concurrent.CompletableFuture;

public interface OsDispatcher {

    void keyPress(final int keyCode);

    void keyRelease(final int keyCode);
}
