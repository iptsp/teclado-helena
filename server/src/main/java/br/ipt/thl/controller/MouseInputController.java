package br.ipt.thl.controller;

import br.ipt.thl.event.EventDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MouseInputController {

    private final EventDispatcher eventDispatcher;

    @Autowired
    public MouseInputController(final EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @GetMapping("/api/v1/systems/keyboards/mouses")
    MouseInputControllerResponse handleKeyboardInput() {
        var x = 10;
        var y = 10;
        eventDispatcher.mouseMoveEvent(x, y);
        return new MouseInputControllerResponse(x, y);
    }

    record MouseInputControllerResponse(int x,
                                        int y) {
    }

    record MouseInputControllerRequest(int x,
                                       int y) {
    }
}
