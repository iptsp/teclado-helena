package br.ipt.thl.event;

public interface EventDispatcher {

    void keyboardInput(final String text);

    void mouseRightButtonClick();

    void mouseLeftButtonClick();

    void mouseMove(int x, int y);

    void mouseScrollUp(int units);

    void mouseScrollDown(int units);
}
