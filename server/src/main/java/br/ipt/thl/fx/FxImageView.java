package br.ipt.thl.fx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

public class FxImageView extends ImageView {


    public FxImageView(final byte[] image,
                       final double width,
                       final double height) {
        super();
        doBehaviour(new Image(new ByteArrayInputStream(image)));
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
