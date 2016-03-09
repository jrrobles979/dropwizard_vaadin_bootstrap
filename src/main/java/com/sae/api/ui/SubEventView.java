package com.sae.api.ui;

import com.sae.api.SAEConstants;
import com.sae.api.core.Event;
import com.sae.api.core.Subevent;
import com.sae.api.db.ManagedServiceDAO;
import com.sae.api.db.QueryParameters;
import com.sae.api.db.ServiceDAO;
import com.sae.api.services.SAEUtil;
import com.sebworks.vaadstrap.Col;
import com.sebworks.vaadstrap.ColMod;
import com.sebworks.vaadstrap.Container;
import com.sebworks.vaadstrap.Row;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.*;
import org.slf4j.Logger;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by ralmeida on 3/3/16.
 */
public class SubEventView extends VerticalLayout implements View {
    private final Logger logger = getLogger(this.getClass());
    private CustomLayout layout = null;
    private com.sae.api.core.Event event;

    public SubEventView(){
        logger.info("Constructor");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        logger.info("Enter");

        try {
            if (layout == null) {
                layout = new CustomLayout(SAEUtil.getResourceFile("subevent.html"));
                addComponent(layout);
            }
            else {
                layout.removeAllComponents();
            }
        }
        catch (Exception ex){
            logger.error("SubEventView.enter: " + ex.toString());
            return;
        }

        loadSubevents();
    }

    private void loadSubevents(){
        HomeUI homeUI = (HomeUI) UI.getCurrent();
        ManagedServiceDAO serviceDAO = homeUI.getServiceDAO();

        serviceDAO.openSession();

        try {
            List<com.sae.api.core.Subevent> subeventList= serviceDAO.findAll(Subevent.class);
            if (subeventList == null || subeventList.isEmpty()) {
                Label label = new Label("Please enter settings/setup to configure an Event with sub events");
                layout.addComponent(label, "content");
            }
            else {
                displaySubevents(subeventList);
            }
        }
        catch (Exception ex){
            logger.error("loadEvent: " + ex.toString());
        }
        finally {
            serviceDAO.closeSession();
        }
    }

    private void displaySubevents(List<Subevent> subeventList){
        try{

            Label labelTitle = new Label(subeventList.get(0).getEvent().getName());
            layout.addComponent(labelTitle,"title");

            Container container = new Container();
            container.setFluid(true);
            layout.addComponent(container, "content");

            int count = 0;
            Row row = container.addRow();
            row.setWidth("100%");
            for (Subevent subevent : subeventList){
                if (count == 4) {
                    row = container.addRow();
                    row.setWidth("100%");
                }

                Col col = row.addCol(ColMod.SM_4, ColMod.MD_4);
                CustomLayout subeventLayout = new CustomLayout(SAEUtil.getResourceFile("subevent-item.html"));
                col.addComponent(subeventLayout);

                subeventLayout.addComponent(new Label(subevent.getName()), "title");
                subeventLayout.addComponent(new Label(subevent.getDescription()), "description");
                subeventLayout.addComponent(new Label(String.valueOf(subevent.getCapacity())), "capacity");
                subeventLayout.addComponent(new Label(SAEUtil.formatSimple(subevent.getStart_date())), "startson");
                subeventLayout.addComponent(new Label(SAEUtil.formatSimple(subevent.getEnd_date())), "endson");
                subeventLayout.addComponent(new Label(SAEUtil.getTypeDescription(subevent.getType())), "type");

                if (subevent.getLocation_lat() != null && subevent.getLocation_long() != null) {
                    GoogleMap googleMap = new GoogleMap(SAEConstants.GOOGLE_MAP_KEY, null, "english");
                    googleMap.setWidth("100%");
                    googleMap.setHeight("200px");
                    googleMap.setMinZoom(4);
                    googleMap.setMaxZoom(100);
                    googleMap.setZoom(15);
                    googleMap.setCenter(new LatLon(subevent.getLocation_lat(), subevent.getLocation_long()));
                    googleMap.setReadOnly(true);
                    subeventLayout.addComponent(googleMap, "map");
                }

                subeventLayout.addComponent(new Link("Dashboard",new ExternalResource("#!dashboard/" + String.valueOf(subevent.getId()))), "link");

                count++;
            }
        }
        catch (Exception ex){
            logger.error("DisplaySubEvents: " + ex.toString());
        }

    }


}
