package br.ipt.thl.fx;


import javafx.scene.control.Label;

public class FxLabel extends Label {


    public FxLabel(final String text) {
        super(text);
    }

    public void addStyleClass(final String styleClass) {
        if (!containsStyleClass(styleClass)) {
            getStyleClass().add(styleClass);
        }
    }

    public boolean containsStyleClass(final String styleClass) {
        return getStyleClass().contains(styleClass);
    }


}
