package br.ipt.thl.keyboard;

import br.ipt.thl.executors.ExecutorsConfig;
import br.ipt.thl.os.OsDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component

public class AsyncKeyboardInputService {
    private final OsDispatcher osDispatcher;
    private static final Map<String, Map<KeyShiftState, List<KeyMap>>> keyMap = new HashMap<>();
    private boolean isShiftPressed = false;

    static {

        keyMap.put("esc", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ESCAPE),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ESCAPE)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ESCAPE),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ESCAPE)
                )
        ));

        keyMap.put("backspace", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_BACK_SPACE),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_BACK_SPACE)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_BACK_SPACE),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_BACK_SPACE)
                )
        ));

        keyMap.put("space", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SPACE),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SPACE)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SPACE),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SPACE)
                )
        ));

        keyMap.put("enter", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ENTER),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ENTER)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ENTER),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ENTER)
                )
        ));

        keyMap.put("tab", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_TAB),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_TAB)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_TAB),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_TAB)
                )
        ));

        keyMap.put("caps-lock", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_CAPS_LOCK),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_CAPS_LOCK)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_CAPS_LOCK),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_CAPS_LOCK)
                )
        ));

        keyMap.put("left-arrow", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_LEFT),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_LEFT)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_LEFT),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_LEFT)
                )
        ));

        keyMap.put("right-arrow", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_RIGHT),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_RIGHT)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_RIGHT),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_RIGHT)
                )
        ));

        keyMap.put("up-arrow", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_UP),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_UP)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_UP),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_UP)
                )
        ));

        keyMap.put("down-arrow", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_DOWN),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_DOWN)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_DOWN),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_DOWN)
                )
        ));

        keyMap.put("shift", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SHIFT),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SHIFT)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SHIFT),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SHIFT)
                )
        ));

        keyMap.put("ctrl", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_CONTROL),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_CONTROL)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_CONTROL),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_CONTROL)
                )
        ));

        keyMap.put("alt", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ALT),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ALT)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ALT),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ALT)
                )
        ));

        keyMap.put("delete", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_DELETE),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_DELETE)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_DELETE),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_DELETE)
                )
        ));

        keyMap.put("home", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_HOME),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_HOME)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_HOME),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_HOME)
                )
        ));

        keyMap.put("end", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_END),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_END)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_END),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_END)
                )
        ));

        keyMap.put("page-up", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_PAGE_UP),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_PAGE_UP)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_PAGE_UP),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_PAGE_UP)
                )
        ));

        keyMap.put("page-down", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_PAGE_DOWN),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_PAGE_DOWN)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_PAGE_DOWN),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_PAGE_DOWN)
                )
        ));

        keyMap.put("insert", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_INSERT),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_INSERT)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_INSERT),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_INSERT)
                )
        ));

        keyMap.put("period", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_PERIOD),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_PERIOD)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_PERIOD),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_PERIOD)
                )
        ));

        keyMap.put("grave", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_DEAD_GRAVE),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_DEAD_GRAVE)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_DEAD_GRAVE),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_DEAD_GRAVE)
                )
        ));

        keyMap.put("quote", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_QUOTE),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_QUOTE)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_QUOTE),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_QUOTE)
                )
        ));

        keyMap.put("semicolon", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SEMICOLON),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SEMICOLON)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SEMICOLON),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SEMICOLON)
                )
        ));

        keyMap.put("tilde", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_DEAD_TILDE),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_DEAD_TILDE)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_DEAD_TILDE),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_DEAD_TILDE)
                )
        ));

        keyMap.put("acute", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_DEAD_ACUTE),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_DEAD_ACUTE)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_DEAD_ACUTE),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_DEAD_ACUTE)
                )
        ));

        keyMap.put("backslash", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_BACK_SLASH),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_BACK_SLASH)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_BACK_SLASH),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_BACK_SLASH)
                )
        ));

        keyMap.put("slash", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ALT),
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD4),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD4),
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD7),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD7),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ALT)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ALT),
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD6),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD6),
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD3),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD3),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ALT)
                )
        ));

        keyMap.put("colon", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ALT),
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD4),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD4),
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD4),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD4),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ALT)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ALT),
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD6),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD6),
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD0),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD0),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ALT)
                )
        ));

        keyMap.put("cedilla", Map.of(
                KeyShiftState.UNSHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ALT),
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD1),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD1),
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD3),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD3),
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD5),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD5),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ALT)
                ),
                KeyShiftState.SHIFTED, List.of(
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ALT),
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD1),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD1),
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD2),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD2),
                        new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD8),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD8),
                        new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ALT)
                )
        ));
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
        var keyEvent = event == KeyboardEventType.PRESSED ? KeyMapEvent.PRESS : KeyMapEvent.RELEASE;
        var keyCodes = keyMap.getOrDefault(text, toKeyMap(text, keyState, keyEvent));

        keyCodes.get(keyState).forEach(keyMap -> {
            switch (keyMap.keyMapEvent) {
                case PRESS -> osDispatcher.keyPress(keyMap.keyCode);
                case RELEASE -> osDispatcher.keyRelease(keyMap.keyCode);
            }
        });

        return CompletableFuture.completedFuture(null);
    }

    private Map<KeyShiftState, List<KeyMap>> toKeyMap(final String text,
                                                       final KeyShiftState keyShiftState,
                                                       final KeyMapEvent keyMapEvent) {
        var keyCode = KeyEvent.getExtendedKeyCodeForChar(text.charAt(0));
        return Map.of(keyShiftState, List.of(
                new KeyMap(keyMapEvent, keyCode)
        ));
    }

    record KeyState(KeyShiftState keyShiftState, KeyMapEvent keyMapEvent) {
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