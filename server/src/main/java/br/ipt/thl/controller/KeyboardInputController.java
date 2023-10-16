package br.ipt.thl.controller;

import br.ipt.thl.event.EventDispatcher;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Validated
public class KeyboardInputController {

    private final EventDispatcher eventDispatcher;

    @Autowired
    public KeyboardInputController(final EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @PostMapping("/api/v1/systems/keyboards/inputs")
    KeyboardInputControllerResponse handleKeyboardInput(@RequestBody @Valid final KeyboardInputControllerRequest
                                                                keyboardInputControllerRequest) {
        var text = keyboardInputControllerRequest.text();
        var event = keyboardInputControllerRequest.event();

        eventDispatcher.keyboardInput(text, event);

        return new KeyboardInputControllerResponse();
    }

    record KeyboardInputControllerResponse() {
    }

    record KeyboardInputControllerRequest(@NotBlank String text,
                                          @NotBlank String event) {
    }
}
