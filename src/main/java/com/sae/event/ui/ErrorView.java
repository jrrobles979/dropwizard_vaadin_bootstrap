package com.sae.event.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by ralmeida on 3/3/16.
 */
public class ErrorView extends VerticalLayout implements View {
    private final Logger logger = getLogger(this.getClass());

    public ErrorView(){
        logger.info("Constructor");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        logger.info("Enter");
    }


}
