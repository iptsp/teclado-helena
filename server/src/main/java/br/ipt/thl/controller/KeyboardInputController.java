package br.ipt.thl.controller;

import br.ipt.thl.event.EventDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class KeyboardInputController {

    private final EventDispatcher eventDispatcher;

    @Autowired
    public KeyboardInputController(final EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @GetMapping("/api/v1/systems/keyboards/inputs")
    KeyboardInputControllerResponse handleKeyboardInput(final KeyboardInputControllerRequest
                                                                keyboardInputControllerRequest) {
        var text = keyboardInputControllerRequest.text();
        eventDispatcher.keyboardInputEvent(text);
        return new KeyboardInputControllerResponse(text);
    }

    record KeyboardInputControllerResponse(String text) {
    }

    record KeyboardInputControllerRequest(String text) {
    }
}
