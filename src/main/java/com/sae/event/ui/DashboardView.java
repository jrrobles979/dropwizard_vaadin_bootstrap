package com.sae.event.ui;

import com.sae.event.core.Subevent;
import com.sae.event.db.UnmanagedServiceDAO;
import com.sae.event.services.SAEUtil;
import com.sebworks.vaadstrap.Col;
import com.sebworks.vaadstrap.ColMod;
import com.sebworks.vaadstrap.Container;
import com.sebworks.vaadstrap.Row;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import org.slf4j.Logger;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by ralmeida on 3/3/16.
 */
public class DashboardView extends VerticalLayout implements View {
    private final Logger logger = getLogger(this.getClass());
    private CustomLayout layout = null;

    public DashboardView(){
        logger.info("Constructor");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        logger.info("Enter");

        try{
            if (layout == null) {
                layout = new CustomLayout(SAEUtil.getResourceFile("dashboard.html"));
                addComponent(layout);
            }
            else {
                layout.removeAllComponents();
            }

            loadSubevents();
        }
        catch (Exception ex){
            logger.info("Enter: " + ex.toString());
        }
    }

    private void loadSubevents(){
        HomeUI homeUI = (HomeUI) UI.getCurrent();
        UnmanagedServiceDAO serviceDAO = homeUI.getServiceDAO();

        serviceDAO.openSession();

        try {
            List<Subevent> subeventList= serviceDAO.findAll(Subevent.class);
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
                if (count == 3) {
                    row = container.addRow();
                    row.setWidth("100%");
                }

                Col col = row.addCol(ColMod.SM_4, ColMod.MD_4);
                CustomLayout subeventLayout = new CustomLayout(SAEUtil.getResourceFile("dashboard-item.html"));
                col.addComponent(subeventLayout);

                subeventLayout.addComponent(new Label(subevent.getName()), "title");
                subeventLayout.addComponent(new Link("Dashboard",new ExternalResource("#!dashboard/" + String.valueOf(subevent.getId()))), "link");

                count++;
            }
        }
        catch (Exception ex){
            logger.error("DisplaySubEvents: " + ex.toString());
        }

    }

}
