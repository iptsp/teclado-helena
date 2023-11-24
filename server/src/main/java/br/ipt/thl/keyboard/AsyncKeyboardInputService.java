package br.ipt.thl.keyboard;

import br.ipt.thl.executors.ExecutorsConfig;
import br.ipt.thl.os.OsDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component

public class AsyncKeyboardInputService {
    private final OsDispatcher osDispatcher;
    private static final Map<String, Map<KeyShiftState, Map<KeyboardEventType, List<KeyMap>>>> keyMap = new HashMap<>();
    private boolean isShiftPressed = false;

    static {

        keyMap.put("esc", createDefaultKeyMap(KeyEvent.VK_ESCAPE));

        keyMap.put("backspace", createDefaultKeyMap(KeyEvent.VK_BACK_SPACE));

        keyMap.put("space", createDefaultKeyMap(KeyEvent.VK_SPACE));

        keyMap.put("enter", createDefaultKeyMap(KeyEvent.VK_ENTER));

        keyMap.put("tab", createDefaultKeyMap(KeyEvent.VK_TAB));

        keyMap.put("caps-lock", createDefaultKeyMap(KeyEvent.VK_CAPS_LOCK));

        keyMap.put("left-arrow", createDefaultKeyMap(KeyEvent.VK_LEFT));

        keyMap.put("right-arrow", createDefaultKeyMap(KeyEvent.VK_RIGHT));

        keyMap.put("up-arrow", createDefaultKeyMap(KeyEvent.VK_UP));

        keyMap.put("down-arrow", createDefaultKeyMap(KeyEvent.VK_DOWN));

        keyMap.put("shift", createDefaultKeyMap(KeyEvent.VK_SHIFT));

        keyMap.put("ctrl", createDefaultKeyMap(KeyEvent.VK_CONTROL));

        keyMap.put("alt", createDefaultKeyMap(KeyEvent.VK_ALT));

        keyMap.put("delete", createDefaultKeyMap(KeyEvent.VK_DELETE));

        keyMap.put("home", createDefaultKeyMap(KeyEvent.VK_HOME));

        keyMap.put("end", createDefaultKeyMap(KeyEvent.VK_END));

        keyMap.put("page-up", createDefaultKeyMap(KeyEvent.VK_PAGE_UP));

        keyMap.put("page-down", createDefaultKeyMap(KeyEvent.VK_PAGE_DOWN));

        keyMap.put("insert", createDefaultKeyMap(KeyEvent.VK_INSERT));

        keyMap.put("period", createDefaultKeyMap(KeyEvent.VK_PERIOD));

        keyMap.put("grave", createDefaultKeyMap(KeyEvent.VK_DEAD_GRAVE));

        keyMap.put("quote", createDefaultKeyMap(KeyEvent.VK_QUOTE));

        keyMap.put("semicolon", createDefaultKeyMap(KeyEvent.VK_SEMICOLON));

        keyMap.put("tilde", createDefaultKeyMap(KeyEvent.VK_DEAD_TILDE));

        keyMap.put("acute", createDefaultKeyMap(KeyEvent.VK_DEAD_ACUTE));

        keyMap.put("backslash", createDefaultKeyMap(KeyEvent.VK_BACK_SLASH));

        keyMap.put("equals", createDefaultKeyMap(KeyEvent.VK_EQUALS));

        keyMap.put("minus", createDefaultKeyMap(KeyEvent.VK_MINUS));

        keyMap.put("slash", Map.of(
                KeyShiftState.UNSHIFTED, Map.of(
                        KeyboardEventType.PRESSED, List.of(
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ALT),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD4),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD4),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD7),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD7),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ALT)),
                        KeyboardEventType.RELEASED, Collections.emptyList()),
                KeyShiftState.SHIFTED, Map.of(
                        KeyboardEventType.PRESSED, List.of(
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SHIFT),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ALT),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD6),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD6),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD3),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD3),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ALT),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SHIFT)),
                        KeyboardEventType.RELEASED, Collections.emptyList())
        ));

        keyMap.put("colon", Map.of(
                KeyShiftState.UNSHIFTED, Map.of(
                        KeyboardEventType.PRESSED, List.of(
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ALT),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD4),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD4),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD4),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD4),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ALT)),
                        KeyboardEventType.RELEASED, Collections.emptyList()),
                KeyShiftState.SHIFTED, Map.of(
                        KeyboardEventType.PRESSED, List.of(
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SHIFT),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ALT),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD6),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD6),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD0),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD0),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ALT),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SHIFT)),
                        KeyboardEventType.RELEASED, Collections.emptyList())
        ));

        keyMap.put("cedilla", Map.of(
                KeyShiftState.UNSHIFTED, Map.of(
                        KeyboardEventType.PRESSED, List.of(
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ALT),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD1),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD1),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD3),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD3),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD5),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD5),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ALT)),
                        KeyboardEventType.RELEASED, Collections.emptyList()),
                KeyShiftState.SHIFTED, Map.of(
                        KeyboardEventType.PRESSED, List.of(
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SHIFT),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ALT),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD1),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD1),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD2),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD2),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD8),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD8),
                                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ALT),
                                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SHIFT)),
                        KeyboardEventType.RELEASED, Collections.emptyList())
        ));
    }

    private static Map<KeyShiftState, Map<KeyboardEventType, List<KeyMap>>> createDefaultKeyMap(int keyCode) {
        return Map.of(
                KeyShiftState.UNSHIFTED, createDefaultEventMap(keyCode),
                KeyShiftState.SHIFTED, createDefaultEventMap(keyCode)
        );
    }

    private static Map<KeyboardEventType, List<KeyMap>> createDefaultEventMap(int keyCode) {
        return Map.of(
                KeyboardEventType.PRESSED, List.of(new KeyMap(KeyMapEvent.PRESS, keyCode)),
                KeyboardEventType.RELEASED, List.of(new KeyMap(KeyMapEvent.RELEASE, keyCode))
        );
    }

    @Autowired
    public AsyncKeyboardInputService(final OsDispatcher osDispatcher) {
        this.osDispatcher = osDispatcher;
    }

    @Async(ExecutorsConfig.OS)
    public CompletableFuture<Void> sendText(final String text, final KeyboardEventType event) {

        if ("shift".equals(text)) {
            isShiftPressed = event == KeyboardEventType.PRESSED;
        }

        var keyState = isShiftPressed ? KeyShiftState.SHIFTED : KeyShiftState.UNSHIFTED;
        var keyCodes = keyMap.getOrDefault(text, toKeyMap(text));

        keyCodes.get(keyState).get(event).forEach(keyMap -> {
            switch (keyMap.keyMapEvent) {
                case PRESS -> osDispatcher.keyPress(keyMap.keyCode);
                case RELEASE -> osDispatcher.keyRelease(keyMap.keyCode);
            }
        });

        return CompletableFuture.completedFuture(null);
    }

    private Map<KeyShiftState, Map<KeyboardEventType, List<KeyMap>>> toKeyMap(final String text) {
        var keyCode = KeyEvent.getExtendedKeyCodeForChar(text.charAt(0));
        return createDefaultKeyMap(keyCode);
    }

    record KeyMap(KeyMapEvent keyMapEvent, int keyCode) {
    }

    enum KeyMapEvent {
        PRESS,
        RELEASE
    }

    enum KeyShiftState {
        SHIFTED,
        UNSHIFTED
    }

}