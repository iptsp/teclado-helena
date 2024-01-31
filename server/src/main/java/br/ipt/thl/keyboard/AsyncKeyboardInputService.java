//Teclado Helena is a keyboard designed to better the experience mainly of users with cerebral palsy.
//        Copyright (C) 2024  Instituto de Pesquisas Tecnológicas
//This file is part of Teclado Helena.
//
//        Teclado Helena is free software: you can redistribute it and/or modify
//        it under the terms of the GNU General Public License as published by
//        the Free Software Foundation, either version 3 of the License, or
//        (at your option) any later version.
//
//        Teclado Helena is distributed in the hope that it will be useful,
//        but WITHOUT ANY WARRANTY; without even the implied warranty of
//        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//        GNU General Public License for more details.
//
//        You should have received a copy of the GNU General Public License
//        along with Teclado Helena. If not, see <https://www.gnu.org/licenses/>6.

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

/**
 * Componente de preparo e envio dos comandos de tecla ao sistema operacional.
 */
@Component
public class AsyncKeyboardInputService {
    /** Define a variável do envio de comandos para o sistema operacional. */
    private final OsDispatcher osDispatcher;
    /** Variável para o armazenamento das teclas do teclado. */
    private static final Map<String, Map<KeyShiftState, Map<KeyboardEventType, List<KeyMap>>>> keyMap = new HashMap<>();
    /** Inicializar com o SHIFT desligado. */
    private boolean isShiftPressed = false;

    /*
      Atribuição a variável de armazenamento das teclas (keyMap)
      das definições de cada tecla, nome e sua chave de código.
      Chave de código - Corresponde ao código correspondente
      ao comando de tecla para o sistema operacional.
     */
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

        keyMap.put("comma", Map.of(
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

    /**
     * Atribui o comando SHIFT para a tecla indicada.
     * @param keyCode Tecla a ser atribuida o comando SHIFT.
     */
    private static Map<KeyShiftState, Map<KeyboardEventType, List<KeyMap>>> createDefaultKeyMap(int keyCode) {
        return Map.of(
                KeyShiftState.UNSHIFTED, createDefaultEventMap(keyCode),
                KeyShiftState.SHIFTED, createDefaultEventMap(keyCode)
        );
    }

    /**
     * Atribui os eventos de ACIONADO e LIBERADO a tecla indicada.
     * @param keyCode Tecla a ser atribuido os eventos.
     */
    private static Map<KeyboardEventType, List<KeyMap>> createDefaultEventMap(int keyCode) {
        return Map.of(
                KeyboardEventType.PRESSED, List.of(new KeyMap(KeyMapEvent.PRESS, keyCode)),
                KeyboardEventType.RELEASED, List.of(new KeyMap(KeyMapEvent.RELEASE, keyCode))
        );
    }

    /** Inicializador do Componente */
    @Autowired
    public AsyncKeyboardInputService(final OsDispatcher osDispatcher) {
        this.osDispatcher = osDispatcher;
    }

    /**
     * Envia o comando ao sistema operacional.
     * @param text      Em formato de texto, tecla acionada no Teclado Helena.
     * @param event     Tipo de evento realizado, ACIONADO ou LIBERADO.
     */
    @Async(ExecutorsConfig.OS)
    public CompletableFuture<Void> sendText(final String text, final KeyboardEventType event) {

        /* Verifica se a tecla acionada é a tecla SHIFT, caso seja, define a variável como acionada. */
        if ("shift".equals(text)) {
            isShiftPressed = event == KeyboardEventType.PRESSED;
        }

        /* Define o estado da tecla, se está com o SHIFT acionado ou não. */
        var keyState = isShiftPressed ? KeyShiftState.SHIFTED : KeyShiftState.UNSHIFTED;
        /* Resgata a tecla a ser acionada. */
        var keyCodes = keyMap.getOrDefault(text, toKeyMap(text));

        /* Envia a tecla para ser acionada no sistema operacional. */
        keyCodes.get(keyState).get(event).forEach(keyMap -> {
            switch (keyMap.keyMapEvent) {
                case PRESS -> osDispatcher.keyPress(keyMap.keyCode);
                case RELEASE -> osDispatcher.keyRelease(keyMap.keyCode);
            }
        });

        return CompletableFuture.completedFuture(null);
    }

    /**
     * Atribui a tecla a uma nova variável utilizada pelo keyMap.
     * @param text Tecla a ser atribuída.
     */
    private Map<KeyShiftState, Map<KeyboardEventType, List<KeyMap>>> toKeyMap(final String text) {
        var keyCode = KeyEvent.getExtendedKeyCodeForChar(text.charAt(0));
        return createDefaultKeyMap(keyCode);
    }

    /** Objeto KeyMap */
    record KeyMap(KeyMapEvent keyMapEvent, int keyCode) {
    }

    /**
     * Enum dos eventos ACIONADO (PRESS) e LIBERADO (RELEASE).
     */
    enum KeyMapEvent {
        PRESS,
        RELEASE
    }

    /**
     * Enum dos estados do SHIFT, SHIFT ACIONADO (SHIFTED) e SHIFT LIBERADO (UNSHIFTED).
     */
    enum KeyShiftState {
        SHIFTED,
        UNSHIFTED
    }

}