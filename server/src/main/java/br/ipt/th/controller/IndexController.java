package br.ipt.th.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    IndexControllerResponse index() {
        return new IndexControllerResponse("Teclado Helena its up and running");
    }

    record IndexControllerResponse(String message) {
    }
}
