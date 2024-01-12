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

package br.ipt.thl.fx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

public class FxImageView extends ImageView {


    public FxImageView(final String url,
                       final double width,
                       final double height) {
        super();
        doBehaviour(new Image(url));
        doStyle(width, height);
    }

    public FxImageView(final byte[] image,
                       final double width,
                       final double height) {
        super();
        doBehaviour(new Image(new ByteArrayInputStream(image)));
        doStyle(width, height);
    }

    public FxImageView(Image image, int width, int height) {
        doBehaviour(image);
        doStyle(width, height);
    }

    private void doStyle(final double width,
                         final double height) {
        if (width != -1) {
            setFitWidth(width);
        }
        if (height != -1) {
            setFitHeight(height);
        }
    }

    private void doBehaviour(final Image image) {
        setFocusTraversable(false);
        setImage(image);
        setCache(false);
    }

}
