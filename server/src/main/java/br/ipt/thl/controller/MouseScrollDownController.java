package br.ipt.thl.controller;

import br.ipt.thl.event.EventDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MouseScrollDownController {

    private final EventDispatcher eventDispatcher;

    @Autowired
    public MouseScrollDownController(final EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @PostMapping("/api/v1/systems/mouses/scrolls/events/down")
    MouseScrollDownControllerResponse handleKeyboardInput(@RequestBody MouseScrollDownControllerRequest
                                                            mouseScrollDownControllerRequest) {
        int units = mouseScrollDownControllerRequest.units();
        eventDispatcher.mouseScrollDown(units);
        return new MouseScrollDownControllerResponse();
    }

    record MouseScrollDownControllerRequest(int units) {
    }

    record MouseScrollDownControllerResponse() {
    }

}
