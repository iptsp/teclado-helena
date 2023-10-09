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
    private String accent = "";

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
    }

    @Autowired
    public AsyncKeyboardInputService(final OsDispatcher osDispatcher) {
        this.osDispatcher = osDispatcher;
    }

    @Async(ExecutorsConfig.OS)
    public CompletableFuture<Void> sendText(final String text) {

        if (keyMap.containsKey(text)) {
            var keyCode = keyMap.getOrDefault(text, keyMap.get("esc"));
            osDispatcher.keyPress(keyCode);
            osDispatcher.keyRelease(keyCode);
            return CompletableFuture.completedFuture(null);
        }

        if (isAccent(text)) {
            accent = text;
            return CompletableFuture.completedFuture(null);
        }

        var compositeChar = text + accent;
        var textToSend = Normalizer.normalize(compositeChar, Normalizer.Form.NFC)
                .codePointAt(0);

        this.sendUnicode(textToSend);

        accent = "";

        return CompletableFuture.completedFuture(null);
    }

    private void sendUnicode(int keyCode) {

        osDispatcher.keyPress(KeyEvent.VK_ALT);

        for (int i = 3; i >= 0; --i) {
            var numpadSeq = keyCode / (int) (Math.pow(10, i)) % 10 + KeyEvent.VK_NUMPAD0;

            osDispatcher.keyPress(numpadSeq);
            osDispatcher.keyRelease(numpadSeq);
        }

        osDispatcher.keyRelease(KeyEvent.VK_ALT);
    }

    public static String convertToUnicode(String input) {
        StringBuilder sb = new StringBuilder();

        for (char c : input.toCharArray()) {
            sb.append("\\u").append(Integer.toHexString((int) c));
        }

        return sb.toString();
    }

    private boolean isAccent(final String text) {
        var unicode = convertToUnicode(text);
        return unicode.equals("\\u303");
    }

}
