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

import br.ipt.thl.container.QRCodeContainer;
import br.ipt.thl.event.StageReadyEvent;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UiApplication {

    private final QRCodeContainer qrCodeContainer;

    @Autowired
    public UiApplication(final QRCodeContainer qrCodeContainer) {
        this.qrCodeContainer = qrCodeContainer;
    }

    @EventListener
    public void onStageReady(final StageReadyEvent stageReadyEvent) {

        var screenWidth = 350d;
        var screenHeight = 350d;
        var primaryScreenBounds = Screen.getPrimary()
                .getBounds();
        var x = primaryScreenBounds.getWidth() / 2 - screenWidth / 2;
        var y = primaryScreenBounds.getHeight() / 2 - screenHeight / 2;

        var stage = stageReadyEvent.source();
        stage.setHeight(screenHeight);
        stage.setWidth(screenWidth);
        stage.setMinHeight(screenHeight);
        stage.setMinWidth(screenWidth);
        stage.setHeight(screenHeight);
        stage.setWidth(screenWidth);
        stage.setResizable(false);
        stage.setX(x);
        stage.setY(y);
        stage.setOnCloseRequest(event -> {
            Platform.exit();
        });

        var scene = new Scene(
                qrCodeContainer,
                stage.getWidth(),
                stage.getHeight()
        );
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets()
                .add("css/ui.css");
        stage.setScene(scene);
        stage.show();
    }


}