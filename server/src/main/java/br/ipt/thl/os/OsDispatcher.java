package br.ipt.thl.os;

public interface OsDispatcher {

    void keyPress(final int keyCode);

    void keyRelease(final int keyCode);

    void mouseMove(final int x,
                   final int y);

    void mousePress(final int buttons);

    void mouseRelease(final int buttons);

    void mouseWheel(final int wheelAmt);
}
