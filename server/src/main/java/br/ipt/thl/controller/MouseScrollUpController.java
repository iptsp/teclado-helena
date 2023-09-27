package br.ipt.thl.controller;

import br.ipt.thl.event.EventDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MouseScrollUpController {

    private final EventDispatcher eventDispatcher;

    @Autowired
    public MouseScrollUpController(final EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @PostMapping("/api/v1/systems/mouses/scrolls/events/up")
    MouseScrollUpControllerResponse handleKeyboardInput(@RequestBody MouseScrollUpControllerRequest
                                                            mouseScrollUpControllerRequest) {
        int units = mouseScrollUpControllerRequest.units();
        eventDispatcher.mouseScrollUp(units);
        return new MouseScrollUpControllerResponse();
    }

    record MouseScrollUpControllerRequest(int units) {
    }

    record MouseScrollUpControllerResponse() {
    }

}
