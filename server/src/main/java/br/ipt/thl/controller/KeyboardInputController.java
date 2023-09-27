package br.ipt.thl.controller;

import br.ipt.thl.event.EventDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class KeyboardInputController {

    private final EventDispatcher eventDispatcher;

    @Autowired
    public KeyboardInputController(final EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @PostMapping("/api/v1/systems/keyboards/inputs")
    KeyboardInputControllerResponse handleKeyboardInput(@RequestBody final KeyboardInputControllerRequest
                                                                keyboardInputControllerRequest) {
        var text = keyboardInputControllerRequest.text();
        eventDispatcher.keyboardInput(text);
        return new KeyboardInputControllerResponse();
    }

    record KeyboardInputControllerResponse() {
    }

    record KeyboardInputControllerRequest(String text) {
    }
}
