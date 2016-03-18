package com.sae.event;

import com.codahale.metrics.MetricRegistry;
import com.sae.event.auth.SAEAuthenticator;
import com.sae.event.auth.SAEAuthorizer;
import com.sae.event.core.Event;
import com.sae.event.core.Session;
import com.sae.event.core.Subevent;
import com.sae.event.core.User;
import com.sae.event.db.*;
import com.sae.event.health.TemplateHealthCheck;
import com.sae.event.resources.SessionsResource;
import com.sae.event.resources.StatusResource;
import com.sae.event.ui.HomeUI;
import com.sae.event.ui.VaadinBundle;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.CachingAuthenticator;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.hibernate.SessionFactory;
import org.joda.time.DateTimeZone;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletException;
import java.util.EnumSet;

/**
 * Dropwizard + vaadin + hibernate + liquibase + bootstrap + swagger
 */
public class SAEApp extends Application<SAEConfiguration> {

    final MetricRegistry metricsRegistry = new MetricRegistry();

    private static final HibernateBundle<SAEConfiguration> hibernateBundle = new HibernateBundle<SAEConfiguration>(
            Session.class,
            User.class,
            Event.class,
            Subevent.class
    ) {
        public DataSourceFactory getDataSourceFactory(SAEConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public static void main(String[] args) throws Exception {

        System.out.println("\n");
        System.out.println("=========================================================================================");
        System.out.println(SAEConstants.APP_NAME + " v" + SAEConstants.APP_VERSION + "." + SAEConstants.APP_BUILD + "  (" + SAEConstants.BUILD_DATE + ")");
        System.out.println("=========================================================================================");

        SAEApp saeApp = new SAEApp();
        saeApp.run(args);
    }

    @Override
    public String getName() {
        return SAEConstants.APP_NAME + " v" + SAEConstants.APP_VERSION + "." + SAEConstants.APP_BUILD + "  (" + SAEConstants.BUILD_DATE + ")";
    }

    @Override
    public void initialize(Bootstrap<SAEConfiguration> bootstrap) {

        //TimeZone
        DateTimeZone.setDefault(DateTimeZone.UTC);

        //Hibernate
        bootstrap.addBundle(hibernateBundle);

        //Swagger
        bootstrap.addBundle(new SwaggerBundle<SAEConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(SAEConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });

        //LiquiBase
        bootstrap.addBundle(new MigrationsBundle<SAEConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(SAEConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });

        //Vaadin Servlet as Bundle
        bootstrap.addBundle(new VaadinBundle(ServletApp.class, "/app/*"));

        //Assets
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html", "index"));
    }

    @Override
    public void run(SAEConfiguration configuration, Environment environment) throws Exception {

        //---
        //Hibernate Service
        //---
        final ServiceDAO serviceDAO = new HibernateServiceDAO(hibernateBundle.getSessionFactory());

        //---
        //Configuration
        //---
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORSFilter", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, environment.getApplicationContext().getContextPath() + "*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,OPTIONS,DELETE,HEAD");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Origin, Content-Type, Accept, Authorization, Content-Disposition");
        filter.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");


        //---
        //Health
        //---
        environment.healthChecks().register("template", new TemplateHealthCheck());

        //---
        //Authorization/Authenticator & Cache
        //---
//        final SessionDAO sessionDAO = new SessionDAO(hibernateBundle.getSessionFactory());
        final SAEAuthenticator oAuthAuthenticator= new SAEAuthenticator(new UnmanagedHibernateServiceDAO(hibernateBundle.getSessionFactory()));
        CachingAuthenticator<String, User> cachingAuthenticator = new CachingAuthenticator<>(
                metricsRegistry, oAuthAuthenticator,
                configuration.getAuthenticationCachePolicy());

        environment.jersey().register(new AuthDynamicFeature(
                new OAuthCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(cachingAuthenticator)
                        .setAuthorizer(new SAEAuthorizer())
                        .setPrefix("Bearer")
                        .buildAuthFilter()));

        environment.jersey().register(RolesAllowedDynamicFeature.class);

        //---
        //Resources
        //---
        environment.jersey().register(new SessionsResource(serviceDAO));
        environment.jersey().register(new StatusResource(serviceDAO));
    }

    @VaadinServletConfiguration(ui = HomeUI.class, productionMode = false)
    public static class ServletApp extends VaadinServlet implements SessionInitListener, SessionDestroyListener {

        //Required to build sessions
        private SessionFactory sessionFactory;

        @Override
        protected void servletInitialized() throws ServletException {
            super.servletInitialized();
            getService().addSessionInitListener(this);
            getService().addSessionDestroyListener(this);
            sessionFactory = hibernateBundle.getSessionFactory();
        }

        @Override
        public void sessionDestroy(SessionDestroyEvent sessionDestroyEvent) {
        }

        @Override
        public void sessionInit(SessionInitEvent sessionInitEvent) throws ServiceException {

            //Storage on session. Any vaadin UI/View can load this object
            sessionInitEvent.getSession().setAttribute(SAEConstants.SESSION_FACTORY, sessionFactory);
        }
    }

}
