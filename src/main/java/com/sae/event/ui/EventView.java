package com.sae.event.ui;

import com.sae.event.SAEConstants;
import com.sae.event.db.UnmanagedServiceDAO;
import com.sae.event.services.SAEUtil;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.*;
import org.slf4j.Logger;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by ralmeida on 3/3/16.
 */
public class EventView extends VerticalLayout implements View {
    private final Logger logger = getLogger(this.getClass());
    private CustomLayout layout = null;

    public EventView(){
        logger.info("Constructor");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        logger.info("Enter");

        try{
            if (layout == null) {
                layout = new CustomLayout(SAEUtil.getResourceFile("event.html"));
                addComponent(layout);
            }
            else {
                layout.removeAllComponents();
            }
        }
        catch (Exception ex){
            logger.error("Constructor: " + ex.toString());
        }

        loadEvent();
    }

    private void loadEvent(){
        HomeUI homeUI = (HomeUI)UI.getCurrent();
        UnmanagedServiceDAO serviceDAO = homeUI.getServiceDAO();
        serviceDAO.openSession();

        try {
            List<com.sae.event.core.Event> eventList = serviceDAO.findAll(com.sae.event.core.Event.class);
            if (eventList == null || eventList.isEmpty()) {
                Label label = new Label("Please enter settings/setup to configure an Event");
                layout.addComponent(label, "content");
            }
            else {
                for (com.sae.event.core.Event event : eventList){
                    displayEvent(event);
                    break;
                }
            }
        }
        catch (Exception ex){
            logger.error("loadEvent: " + ex.toString());
        }
        finally {
            serviceDAO.closeSession();
        }

    }

    private void displayEvent(com.sae.event.core.Event event){
        Label labelTitle = new Label(event.getName());
        layout.addComponent(labelTitle, "title");

        CustomLayout eventLayout;
        try {
            eventLayout = new CustomLayout(SAEUtil.getResourceFile("event-item.html"));
            layout.addComponent(eventLayout, "content");
        }
        catch (Exception ex){
            logger.error("displayEvent: " + ex.toString());
            return;
        }

        eventLayout.addComponent(new Label(event.getName()), "name");
        eventLayout.addComponent(new Label(event.getDescription()), "description");
        eventLayout.addComponent(new Label(SAEUtil.formatSimple(event.getStart_date())), "startson");
        eventLayout.addComponent(new Label(SAEUtil.formatSimple(event.getEnd_date())), "endson");
        eventLayout.addComponent(new Label(SAEUtil.getTypeDescription(event.getType())), "type");
        eventLayout.addComponent(new Label(String.valueOf(event.getCapacity())), "capacity");


        GoogleMap googleMap = new GoogleMap(SAEConstants.GOOGLE_MAP_KEY, null, "english");
        googleMap.setWidth("100%");
        googleMap.setHeight("300px");
        googleMap.setMinZoom(4);
        googleMap.setMaxZoom(100);
        googleMap.setZoom(15);
        googleMap.setCenter(new LatLon(event.getLocation_lat(),event.getLocation_long()));
        googleMap.setReadOnly(true);
        eventLayout.addComponent(googleMap, "map");
    }

}
