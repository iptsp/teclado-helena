//Teclado Helena is a keyboard designed to better the experience mainly of users with cerebral palsy.
//        Copyright (C) 2024  Instituto de Pesquisas Tecnológicas
//This file is part of Teclado Helena.
//
//        Teclado Helena is free software: you can redistribute it and/or modify
//        it under the terms of the GNU General Public License as published by
//        the Free Software Foundation, either version 3 of the License, or
//        (at your option) any later version.
//
//        Teclado Helena is distributed in the hope that it will be useful,
//        but WITHOUT ANY WARRANTY; without even the implied warranty of
//        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//        GNU General Public License for more details.
//
//        You should have received a copy of the GNU General Public License
//        along with Teclado Helena. If not, see <https://www.gnu.org/licenses/>6.

package br.ipt.thl;

import br.ipt.thl.event.StageReadyEvent;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Optional;

public class FxApplication extends Application {


    private ConfigurableApplicationContext configurableApplicationContext;

    public FxApplication() {

    }

    @Override
    public void init() {
        this.configurableApplicationContext = new SpringApplicationBuilder()
                .sources(Entrypoint.class)
                .headless(false)
                .build()
                .run(args());
    }

    private String[] args() {
        return Optional.ofNullable(getParameters())
                .map(javafx.application.Application.Parameters::getRaw)
                .map(args -> args.toArray(String[]::new))
                .orElse(new String[]{});
    }

    @Override
    public void start(final Stage stage) {
        this.configurableApplicationContext
                .publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop() {
        this.configurableApplicationContext.close();
        System.exit(0);
    }
}
