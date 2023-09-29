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
import java.util.stream.Stream;

@Component
public class AsyncKeyboardInputService {

    private final OsDispatcher osDispatcher;
    private static Map<String, List<KeyMap>> maps = new HashMap<>();

    static {


        maps.put("q", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_Q),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_Q)
        ));

        maps.put("Q", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SHIFT),
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_Q),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_Q),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SHIFT)
        ));

        maps.put("w", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_W),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_W)
        ));

        maps.put("W", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SHIFT),
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_W),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_W),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SHIFT)
        ));

        maps.put("e", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_E),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_E)
        ));

        maps.put("E", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SHIFT),
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_E),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_E),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SHIFT)
        ));

        maps.put("r", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_R),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_R)
        ));

        maps.put("R", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SHIFT),
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_R),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_R),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SHIFT)
        ));

        maps.put("t", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_T),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_T)
        ));

        maps.put("T", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SHIFT),
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_T),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_T),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SHIFT)
        ));

        maps.put("a", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_A),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_A)
        ));

        maps.put("A", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SHIFT),
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_A),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_A),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SHIFT)
        ));

        maps.put("s", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_S),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_S)
        ));

        maps.put("S", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SHIFT),
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_S),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_S),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SHIFT)
        ));

        maps.put("d", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_D),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_D)
        ));

        maps.put("D", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SHIFT),
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_D),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_D),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SHIFT)
        ));

        maps.put("f", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_F),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_F)
        ));

        maps.put("F", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SHIFT),
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_F),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_F),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SHIFT)
        ));

        maps.put("z", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_Z),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_Z)
        ));

        maps.put("Z", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SHIFT),
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_Z),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_Z),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SHIFT)
        ));

        maps.put("x", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_X),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_X)
        ));

        maps.put("X", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SHIFT),
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_X),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_X),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SHIFT)
        ));

        maps.put("c", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_C),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_C)
        ));

        maps.put("C", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SHIFT),
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_C),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_C),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SHIFT)
        ));

        maps.put("v", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_V),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_V)
        ));

        maps.put("V", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SHIFT),
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_V),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_V),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SHIFT)
        ));

        // Alt + 0225
        maps.put("á", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ALT),
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD0),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD0),
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD2),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD2),
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD2),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD2),
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_NUMPAD5),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_NUMPAD5),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ALT)
        ));
    }

    @Autowired
    public AsyncKeyboardInputService(final OsDispatcher osDispatcher) {
        this.osDispatcher = osDispatcher;
    }

    @Async(ExecutorsConfig.OS)
    public CompletableFuture<Void> sendText(final String text) {
        var keyMaps = Stream.of(text.toCharArray())
                .map(String::valueOf)
                .map(key -> maps.getOrDefault(key, maps.get("x")))
                .flatMap(List::stream)
                .toList();

        for (var keyMap : keyMaps) {
            var keyCode = keyMap.keyCode();
            switch (keyMap.keyMapEvent()) {
                case PRESS -> osDispatcher.keyPress(keyCode);
                case RELEASE -> osDispatcher.keyRelease(keyCode);
            }
        }
        return CompletableFuture.completedFuture(null);
    }


    enum KeyMapEvent {
        PRESS,
        RELEASE
    }

    record KeyMap(KeyMapEvent keyMapEvent, int keyCode) {

    }

}
