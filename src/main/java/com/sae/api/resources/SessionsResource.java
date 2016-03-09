package com.sae.api.resources;

import com.sae.api.auth.UserCredential;
import com.sae.api.core.Session;
import com.sae.api.core.Token;
import com.sae.api.core.User;
import com.sae.api.core.bean.SessionItem;
import com.sae.api.db.QueryParameters;
import com.sae.api.db.ServiceDAO;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by ralmeida on 10/16/15.
 */
@Path("/sessions")
@Api(
        value = "/sessions",
        description = "Administra sessiones de usuario"
)
@SwaggerDefinition(
        info = @Info(
                description = "SAE API Platform",
                version = "1.0",
                title = "SAE API",
                termsOfService = "http://onewip.com/sae/terms",
                contact = @Contact(
                        name = "Roberto Almeida",
                        email = "robert@onewip.com",
                        url = "http://onewip.com/robert"
                ),
                license = @License(
                        name = "SAE License",
                        url = "http://onewip.com/sae/license"
                )
        ),
        consumes = {"application/json", "application/xml"},
        produces = {"application/json", "application/xml"},
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS},
        tags = {
                @Tag(name = "Private", description = "Tag used to denote operations as private")
        }
)
public class SessionsResource {
    private static final Log log = LogFactory.getLog(SessionsResource.class);
    private ServiceDAO serviceDAO;

    public SessionsResource(ServiceDAO serviceDAO){
        this.serviceDAO = serviceDAO;
    }

    @POST
    @ApiOperation(
            value = "Creates a new session",
            response = SessionItem.class
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 403, message = "Not allowed."),
                    @ApiResponse(code = 404, message = "User or team not found."),
                    @ApiResponse(code = 500, message = "Internal server error")
            }
    )
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SessionItem newSession(
            @ApiParam(value = "User credentials", required = true) UserCredential credential,
            @ApiParam(access = "internal") @Context HttpServletRequest request) {

            User currentUser = serviceDAO.findUniqueWithNamedQuery(User.findByUser, QueryParameters.with("USER", credential.getUsername().toLowerCase()).parameters());
            if (currentUser == null) {
                throw new NotFoundException("User not found");
            }

            StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
            if (!passwordEncryptor.checkPassword(currentUser.getPassword_salt() + credential.getUserpassword(), currentUser.getPassword_digest())) {
                throw new ForbiddenException("Invalid password");
            }

            Session session = serviceDAO.findUniqueWithNamedQuery(Session.findByUser, QueryParameters.with("USER", currentUser).parameters());
            if (session == null) {
                session = new Session();
                session.setUser(currentUser);
                session.setCreated_at(new Timestamp(Calendar.getInstance().getTimeInMillis()));
                session.setUpdated_at(session.getCreated_at());
                session.setApp(credential.getApp());
                session.setDevice(credential.getDevice());
                session.setIp(request.getRemoteAddr());

                Token token = Token.buildNewToken();
                session.setToken(token.getToken());
                serviceDAO.create(session);
                return new SessionItem(session);
            }

            if (session.getToken() == null || session.getToken().isEmpty()) {
                Token token = Token.buildNewToken();
                session.setToken(token.getToken());
                session.setUpdated_at(new Timestamp(Calendar.getInstance().getTimeInMillis()));
                serviceDAO.update(session);
                return new SessionItem(session);
            }

            session.setUpdated_at(new Timestamp(Calendar.getInstance().getTimeInMillis()));
            session.setApp(credential.getApp());
            session.setDevice(credential.getDevice());
            session.setIp(request.getRemoteAddr());
            serviceDAO.update(session);

            return new SessionItem(session);
    }

    @DELETE
    @UnitOfWork
    @ApiOperation(
            value = "Delete session"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 404, message = "Session not found."),
                    @ApiResponse(code = 500, message = "Internal server error")
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Bearer {TOKEN}",
                    required = true,
                    dataType = "string",
                    paramType = "header"
            )
    })
    public Response deleteSession(@Context SecurityContext context){

        User userPrincipal = (User) context.getUserPrincipal();
        Session session = serviceDAO.findUniqueWithNamedQuery(Session.findByUser, QueryParameters.with("USER", userPrincipal).parameters());
        if (session == null) {
            throw new NotFoundException();
        }

        serviceDAO.delete(Session.class, session.getId());
        return Response.noContent().build();
    }


}
