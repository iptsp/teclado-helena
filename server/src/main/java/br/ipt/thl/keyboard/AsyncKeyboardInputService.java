package br.ipt.thl.keyboard;

import br.ipt.thl.executors.ExecutorsConfig;
import br.ipt.thl.os.OsDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.awt.event.KeyEvent;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Component
public class AsyncKeyboardInputService {

    private final OsDispatcher osDispatcher;
    private static final Map<String, List<KeyMap>> maps = new HashMap<>();
    private String accent = "";

    static {

        maps.put("left-arrow", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_LEFT),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_LEFT)
        ));

        maps.put("right-arrow", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_RIGHT),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_RIGHT)
        ));

        maps.put("up-arrow", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_UP),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_UP)
        ));

        maps.put("down-arrow", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_DOWN),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_DOWN)
        ));

        maps.put("space", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_SPACE),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_SPACE)
        ));

        maps.put("enter", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ENTER),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ENTER)
        ));

        maps.put("backspace", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_BACK_SPACE),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_BACK_SPACE)
        ));

        maps.put("esc", List.of(
                new KeyMap(KeyMapEvent.PRESS, KeyEvent.VK_ESCAPE),
                new KeyMap(KeyMapEvent.RELEASE, KeyEvent.VK_ESCAPE)
        ));
    }

    @Autowired
    public AsyncKeyboardInputService(final OsDispatcher osDispatcher) {
        this.osDispatcher = osDispatcher;
    }

    @Async(ExecutorsConfig.OS)
    public CompletableFuture<Void> sendText(final String text) {

        if (text.length() > 1) {
            var keyMaps = Stream.of(text.toCharArray())
                    .map(String::valueOf)
                    .map(key -> maps.getOrDefault(key, maps.get("esc")))
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

    public void showCodePoints(String str, Normalizer.Form forma) {
        String s = Normalizer.normalize(str, forma);
        System.out.printf("Code points da string '%s' em %s\n", s, forma);
        s.codePoints().forEach(cp -> {
            System.out.printf(" - U+%04X %s\n", cp, Character.getName(cp));
            System.out.printf(" - U+%04X %s\n", cp, Character.getName(cp));
        });
    }

    public int calcCodePoints(String str) {
        String s = Normalizer.normalize(str, Normalizer.Form.NFD);
        var result = 0;
        for(Integer cp : s.codePoints().toArray()) {
            result += cp;
        }
        return result;
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

    enum KeyMapEvent {
        PRESS,
        RELEASE
    }

    record KeyMap(KeyMapEvent keyMapEvent, int keyCode) {

    }

}
