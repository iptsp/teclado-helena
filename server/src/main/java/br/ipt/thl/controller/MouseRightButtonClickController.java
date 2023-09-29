package br.ipt.thl.controller;

import br.ipt.thl.event.EventDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MouseRightButtonClickController {

    private final EventDispatcher eventDispatcher;

    @Autowired
    public MouseRightButtonClickController(final EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @PostMapping("/api/v1/systems/mouses/buttons/right/events/click")
    MouseRightButtonClickControllerResponse handleKeyboardInput() {
        eventDispatcher.mouseRightButtonClick();
        return new MouseRightButtonClickControllerResponse();
    }

    record MouseRightButtonClickControllerResponse() {
    }
}
