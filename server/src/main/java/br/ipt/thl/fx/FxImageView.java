/*
 * Copyright © 2021-present Lenovo. All rights reserved.
 * Confidential and Restricted
 * Partnership with Instituto de Pesquisas Tecnológicas de São Paulo - IPT
 *
 */

package br.ipt.thl.fx;

import br.ipt.thl.common.Files;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.file.Path;

public class FxImageView extends ImageView {

    public FxImageView(final Path path) {
        this(path, -1, -1);
    }

    public FxImageView(final Path path,
                       final double width,
                       final double height) {
        this(Files.uriAbsoluteAsString(path), width, height);
    }

    public FxImageView(final String url) {
        this(url, -1, -1);
    }

    public FxImageView(final String url,
                       final double width,
                       final double height) {
        super();
        doBehaviour(url);
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

    private void doBehaviour(final String url) {
        setFocusTraversable(false);
        setImage(new Image(url));
        setCache(false);
    }

    public void hide() {
        setVisible(false);
    }

    public void show() {
        setVisible(true);
    }

}
