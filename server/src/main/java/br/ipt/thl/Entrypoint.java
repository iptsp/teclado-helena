package br.ipt.thl;

import br.ipt.thl.common.OsProcesses;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Entrypoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(Entrypoint.class);

    public static void main(String[] args) {
        if (!OsProcesses.isRunning("THL.exe")) {
            Application.launch(FxApplication.class, args);
        } else {
            LOGGER.error("THL is already running..");
        }
    }
}
