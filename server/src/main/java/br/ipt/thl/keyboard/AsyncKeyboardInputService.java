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
    private static final Map<String, Integer> maps = new HashMap<>();
    private String accent = "";

    static {
        maps.put("esc", KeyEvent.VK_ESCAPE);
        maps.put("backspace", KeyEvent.VK_BACK_SPACE);
        maps.put("space", KeyEvent.VK_SPACE);
        maps.put("enter", KeyEvent.VK_ENTER);
        maps.put("left-arrow", KeyEvent.VK_LEFT);
        maps.put("right-arrow", KeyEvent.VK_RIGHT);
        maps.put("up-arrow", KeyEvent.VK_UP);
        maps.put("down-arrow", KeyEvent.VK_DOWN);
    }

    @Autowired
    public AsyncKeyboardInputService(final OsDispatcher osDispatcher) {
        this.osDispatcher = osDispatcher;
    }

    @Async(ExecutorsConfig.OS)
    public CompletableFuture<Void> sendText(final String text) {

        if (text.length() > 1) {
            var keyCode = maps.getOrDefault(text, maps.get("esc"));
            osDispatcher.keyPress(keyCode);
            osDispatcher.keyRelease(keyCode);
            return CompletableFuture.completedFuture(null);
        }

        var newText = text;

        if (isAccent(text)) {
            accent = text;
            System.out.println("Accent: " + accent);
            return CompletableFuture.completedFuture(null);
        } else {
            String textToSend = text + accent;
            newText = Normalizer.normalize(textToSend, Normalizer.Form.NFC);
            accent = "";
        }

        sendUnicode(newText.codePointAt(0));

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
