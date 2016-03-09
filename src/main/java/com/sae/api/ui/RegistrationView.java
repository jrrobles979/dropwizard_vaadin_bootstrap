package com.sae.api.ui;

import com.sae.api.core.Event;
import com.sae.api.db.ManagedServiceDAO;
import com.sae.api.services.SAEUtil;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by ralmeida on 3/3/16.
 */
public class RegistrationView extends VerticalLayout implements View {
    private final Logger logger = getLogger(this.getClass());
    private CustomLayout layout = null;

    public RegistrationView(){
        logger.info("Constructor");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        logger.info("Enter");

        try{
            if (layout == null) {
                layout = new CustomLayout(SAEUtil.getResourceFile("registration.html"));
                addComponent(layout);
            }
            else {
                layout.removeAllComponents();
            }

            loadRegistration();
        }
        catch (Exception ex){
            logger.error("Enter: " + ex.toString());
        }
    }

    private void loadRegistration(){

        HomeUI homeUI = (HomeUI) UI.getCurrent();
        ManagedServiceDAO serviceDAO = homeUI.getServiceDAO();

        serviceDAO.openSession();

        com.sae.api.core.Event event = SAEUtil.getEvent(serviceDAO);
        if (event == null){
            serviceDAO.closeSession();
            logger.error("loadRegistration: event not found");
            return;
        }

        layout.addComponent(new Label(event.getName()), "title");

        serviceDAO.closeSession();
    }

    private void displayRegistration(){

    }


}
