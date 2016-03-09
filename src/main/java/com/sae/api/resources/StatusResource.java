package com.sae.api.resources;

import com.sae.api.SAEConstants;
import com.sae.api.core.bean.StatusInfo;
import com.sae.api.db.ServiceDAO;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by ralmeida on 10/15/15.
 */

@Path("/status")
@Api(
        value = "/status",
        description = "Get API Status"
)
public class StatusResource {
    private static final Log log = LogFactory.getLog(StatusResource.class);
    private final ServiceDAO dao;

    public StatusResource(ServiceDAO dao){
        this.dao = dao;
    }

    @GET
    @ApiOperation(
            value = "Get API Status",
            response = StatusInfo.class
    )
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public StatusInfo getStatus() {

        StatusInfo statusInfo = new StatusInfo();
        try {
//            List<YeiiProgram> programs = dao.findAll(YeiiProgram.class);
            statusInfo.setStatus(SAEConstants.APP_NAME + " is online");
        }
        catch (Exception ex){
            statusInfo.setStatus(SAEConstants.APP_NAME + " is offline");
        }

        statusInfo.setVersion(SAEConstants.APP_VERSION + "." + SAEConstants.APP_BUILD);
        statusInfo.setReleaseDate(SAEConstants.BUILD_DATE);

        return statusInfo;
    }


}

