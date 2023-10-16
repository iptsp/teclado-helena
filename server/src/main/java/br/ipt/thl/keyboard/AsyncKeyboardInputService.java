package br.ipt.thl.keyboard;

import br.ipt.thl.executors.ExecutorsConfig;
import br.ipt.thl.os.OsDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.awt.event.KeyEvent;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class AsyncKeyboardInputService {

    private final OsDispatcher osDispatcher;
    private static final Map<String, Integer> keyMap = new HashMap<>();

    static {
        keyMap.put("esc", KeyEvent.VK_ESCAPE);
        keyMap.put("backspace", KeyEvent.VK_BACK_SPACE);
        keyMap.put("space", KeyEvent.VK_SPACE);
        keyMap.put("enter", KeyEvent.VK_ENTER);
        keyMap.put("left-arrow", KeyEvent.VK_LEFT);
        keyMap.put("right-arrow", KeyEvent.VK_RIGHT);
        keyMap.put("up-arrow", KeyEvent.VK_UP);
        keyMap.put("down-arrow", KeyEvent.VK_DOWN);
        keyMap.put("tab", KeyEvent.VK_TAB);
        keyMap.put("caps-lock", KeyEvent.VK_CAPS_LOCK);
        keyMap.put("shift", KeyEvent.VK_SHIFT);
        keyMap.put("ctrl", KeyEvent.VK_CONTROL);
        keyMap.put("alt", KeyEvent.VK_ALT);
        keyMap.put("delete", KeyEvent.VK_DELETE);
        keyMap.put("home", KeyEvent.VK_HOME);
        keyMap.put("end", KeyEvent.VK_END);
        keyMap.put("page-up", KeyEvent.VK_PAGE_UP);
        keyMap.put("page-down", KeyEvent.VK_PAGE_DOWN);
        keyMap.put("insert", KeyEvent.VK_INSERT);
        keyMap.put("tilde", KeyEvent.VK_DEAD_TILDE);
        keyMap.put("grave", KeyEvent.VK_DEAD_GRAVE);
        keyMap.put("acute", KeyEvent.VK_DEAD_ACUTE);
        keyMap.put("circumflex", KeyEvent.VK_DEAD_CIRCUMFLEX);
        keyMap.put("diaeresis", KeyEvent.VK_DEAD_DIAERESIS);
        keyMap.put("cedilla", KeyEvent.VK_DEAD_CEDILLA);
        keyMap.put("plus", KeyEvent.VK_PLUS);
        keyMap.put("minus", KeyEvent.VK_MINUS);
        keyMap.put("multiply", KeyEvent.VK_MULTIPLY);
        keyMap.put("divide", KeyEvent.VK_DIVIDE);
    }

    @Autowired
    public AsyncKeyboardInputService(final OsDispatcher osDispatcher) {
        this.osDispatcher = osDispatcher;
    }

    @Async(ExecutorsConfig.OS)
    public CompletableFuture<Void> sendText(final String text, final String event) {

        System.out.println("text: " + text);
        System.out.println("event: " + event + "\n");

        if (keyMap.containsKey(text)) {

            var keyCode = keyMap.getOrDefault(text, keyMap.get("esc"));

            switch (event) {
                case "pressed":
                    osDispatcher.keyPress(keyCode);
                    break;
                case "released":
                    osDispatcher.keyRelease(keyCode);
                    break;
                default:
                    return CompletableFuture.completedFuture(null);
            }

            return CompletableFuture.completedFuture(null);
        }

        if ("released".equals(event)) {
            var keyCode = KeyEvent.getExtendedKeyCodeForChar(text.codePointAt(0));
            osDispatcher.keyPress(keyCode);
            osDispatcher.keyRelease(keyCode);
        }

        return CompletableFuture.completedFuture(null);
    }

}
