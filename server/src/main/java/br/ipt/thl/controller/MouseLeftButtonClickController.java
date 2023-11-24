package br.ipt.thl.controller;

import br.ipt.thl.event.EventDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MouseLeftButtonClickController {

    private final EventDispatcher eventDispatcher;

    @Autowired
    public MouseLeftButtonClickController(final EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @PostMapping("/api/v1/systems/mouses/buttons/left/events/click")
    MouseLeftButtonClickControllerResponse handleKeyboardInputClick() {
        eventDispatcher.mouseLeftButtonClick();
        return new MouseLeftButtonClickControllerResponse();
    }

    @PostMapping("/api/v1/systems/mouses/buttons/left/events/press")
    MouseLeftButtonPressControllerResponse handleKeyboardInputPress() {
        eventDispatcher.mouseLeftButtonPress();
        return new MouseLeftButtonPressControllerResponse();
    }

    @PostMapping("/api/v1/systems/mouses/buttons/left/events/release")
    MouseLeftButtonReleaseControllerResponse handleKeyboardInputRelease() {
        eventDispatcher.mouseLeftButtonRelease();
        return new MouseLeftButtonReleaseControllerResponse();
    }

    record MouseLeftButtonClickControllerResponse() {
    }

    record MouseLeftButtonPressControllerResponse() {
    }

    record MouseLeftButtonReleaseControllerResponse() {
    }
}
