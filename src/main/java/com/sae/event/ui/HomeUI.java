package com.sae.event.ui;

import com.sae.event.SAEConstants;
import com.sae.event.db.UnmanagedHibernateServiceDAO;
import com.sae.event.db.UnmanagedServiceDAO;
import com.sae.event.services.SAEUtil;
import com.sebworks.vaadstrap.Container;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Viewport;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by ralmeida on 2/29/16.
 */
@Title("SAE UI")
@Theme("valo")
@Widgetset("com.sae.event.ui.AppWidgetSet")
@Viewport("width=device-width, initial-scale=1")
public class HomeUI extends UI{
    private final Logger logger = getLogger(this.getClass());
    private CustomLayout layout;
    private VerticalLayout contentLayout;
    private SessionFactory sessionFactory;
    private Navigator navigator;

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        try {

            Container container = new Container();
            container.setFluid(true);

            layout = new CustomLayout(SAEUtil.getResourceFile("home.html"));
            container.addComponent(layout);
            setContent(container);

            contentLayout = new VerticalLayout();
            layout.addComponent(contentLayout, "content");

            sessionFactory = (SessionFactory) getSession().getAttribute(SAEConstants.SESSION_FACTORY);
            if (sessionFactory == null){
                logger.error("Session factory == null");
            }
            else {
                logger.error("Session factory is OK");
            }
        }
        catch (Exception ex){
            logger.error("HOME error: " + ex.toString());
        }

//        String fragment = getPage().getUriFragment();

        navigator = new Navigator(this, contentLayout);
        navigator.addView("", new DashboardView());
        navigator.addView("event", new EventView());
        navigator.addView("subevents", new SubEventView());
        navigator.addView("dashboard", new DashboardView());
        navigator.setErrorView(new ErrorView());
        this.setNavigator(navigator);

    }

    public UnmanagedServiceDAO getServiceDAO(){
        UnmanagedServiceDAO serviceDAO = new UnmanagedHibernateServiceDAO(sessionFactory);
        return serviceDAO;
    }
}
