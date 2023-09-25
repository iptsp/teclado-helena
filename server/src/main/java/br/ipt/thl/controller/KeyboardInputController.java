package br.ipt.thl.controller;

import br.ipt.thl.event.EventDispatcher;
import javafx.scene.input.KeyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class KeyboardInputController {

    private final EventDispatcher eventDispatcher;

    @Autowired
    public KeyboardInputController(final EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @GetMapping("/api/v1/systems/keyboards/inputs")
    KeyboardInputControllerResponse handleKeyboardInput() {
        var keyCode = "A";
        eventDispatcher.keyboardInputEvent(KeyCode.getKeyCode(keyCode));
        return new KeyboardInputControllerResponse(keyCode);
    }

    record KeyboardInputControllerResponse(String keyCode) {
    }

    record KeyboardInputControllerRequest(String keyCode) {
    }
}
