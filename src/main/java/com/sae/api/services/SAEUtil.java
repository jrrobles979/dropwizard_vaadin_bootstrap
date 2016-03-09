package com.sae.api.services;

import com.sae.api.SAEConstants;
import com.sae.api.core.Event;
import com.sae.api.db.ManagedServiceDAO;
import com.vaadin.ui.Label;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by ralmeida on 2/18/16.
 */
public class SAEUtil {
    private static final Logger logger = getLogger(SAEUtil.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("Y/MMM/d h:mm a");

    public static String createSalt(){
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return Hex.encodeHexString(bytes);
    }

    public static java.sql.Timestamp getNow() {
        return new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public static InputStream getResourceFile(String filename){
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream(filename);
        return inputStream;
    }

    public static InputStream getResourceFile(String filename, Properties properties){
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream(filename);

        String content = "";
        StringWriter writer = new StringWriter();

        try {
            IOUtils.copy(inputStream, writer, "UTF-8");
            String dummy = writer.toString();

            String token = "";
            String value = "";
            for (Object key : properties.keySet()){
                token = (String)key;
                value = properties.getProperty(token);
                dummy = dummy.replace("[" + token.toUpperCase() + "]", value);
            }
            content = dummy;

        } catch (Exception e) {
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }

        return IOUtils.toInputStream(content);
    }

    public static Event getEvent(ManagedServiceDAO serviceDAO){

        serviceDAO.openSession();

        Event selectedEvent = null;
        try {
            List<Event> eventList = serviceDAO.findAll(com.sae.api.core.Event.class);
            if (eventList == null || eventList.isEmpty()) {
                return null;
            }
            else {
                for (com.sae.api.core.Event event : eventList){
                    selectedEvent = event;
                    break;
                }
            }
        }
        catch (Exception ex){
            logger.error("getEvent: " + ex.toString());
        }
        finally {
            serviceDAO.closeSession();
        }

        return selectedEvent;
    }

    public static String getTypeDescription(int type){
        switch (type) {
            case SAEConstants.EVENT_TYPE_OPEN:
                return "Open";

            case SAEConstants.EVENT_TYPE_CLOSED:
                return "Closed";

            case SAEConstants.EVENT_TYPE_INVITATION:
                return "Invitation";

            default:
                return "Other";
        }
    }

    public static String formatSimple(Timestamp timestamp){
        String dummy = "";

        try {
            if (timestamp != null) {
                LocalDateTime ldt = timestamp.toLocalDateTime();
                dummy = ldt.format(dateTimeFormatter);
            }
        }
        catch (Exception ex){
            logger.error("formatSimple: " + ex.toString());
        }

        return dummy;
    }

}
