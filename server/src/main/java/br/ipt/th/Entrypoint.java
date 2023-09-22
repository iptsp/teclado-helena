package br.ipt.th;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Entrypoint {
    public static void main(String[] args) {
        Application.launch(FxApplication.class, args);
    }
}
