package br.ipt.thl.controller;

import br.ipt.thl.event.EventDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MouseMoveController {

    private final EventDispatcher eventDispatcher;

    @Autowired
    public MouseMoveController(final EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @PostMapping("/api/v1/systems/mouses/events/move")
    MouseMoveControllerResponse handleKeyboardInput(@RequestBody MouseMoveControllerRequest
                                                            mouseMoveControllerRequest) {

        var x = mouseMoveControllerRequest.x();
        var y = mouseMoveControllerRequest.y();
        eventDispatcher.mouseMove(x, y);
        return new MouseMoveControllerResponse();
    }

    record MouseMoveControllerRequest(int x, int y) {
    }

    record MouseMoveControllerResponse() {
    }

}
