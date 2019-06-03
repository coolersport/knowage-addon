/*
 * FILENAME
 *     ExtendedUserResource.java
 *
 * FILE LOCATION
 *     $Source$
 *
 * VERSION
 *     $Id$
 *         @version       $Revision$
 *         Check-Out Tag: $Name$
 *         Locked By:     $Lockers$
 *
 * FORMATTING NOTES
 *     * Lines should be limited to 78 characters.
 *     * Files should contain no tabs.
 *     * Indent code using four-character increments.
 *
 * COPYRIGHT
 *     Copyright (C) 2007 Genix Ventures Pty. Ltd. All rights reserved.
 *     This software is the confidential and proprietary information of
 *     Genix Ventures ("Confidential Information"). You shall not
 *     disclose such Confidential Information and shall use it only in
 *     accordance with the terms of the license agreement you entered into
 *     with Genix Ventures.
 */

package com.genix.protrack.saas.knowage.core.addon.api.v2;

import static java.lang.String.format;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import it.eng.spagobi.api.AbstractSpagoBIResource;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.profiling.bean.SbiUser;
import it.eng.spagobi.profiling.bo.UserBO;
import it.eng.spagobi.profiling.dao.ISbiUserDAO;
import it.eng.spagobi.profiling.dao.SbiUserDAOHibImpl;
import it.eng.spagobi.services.rest.annotations.ManageAuthorization;
import it.eng.spagobi.services.rest.annotations.UserConstraint;
import it.eng.spagobi.utilities.exceptions.SpagoBIRestServiceException;

//
// IMPORTS
// NOTE: Import specific classes without using wildcards.
//

@SuppressWarnings("all")
@Path("/2.0/addon/users")
@ManageAuthorization
public class ExtendedUserResource extends AbstractSpagoBIResource
{
    @GET
    @UserConstraint(functionalities = {
        SpagoBIConstants.PROFILE_MANAGEMENT, SpagoBIConstants.FINAL_USERS_MANAGEMENT
    })
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") final String id)
    {
        try
        {
            ISbiUserDAO usersDao = DAOFactory.getSbiUserDAO();
            SbiUser sbiUser = usersDao.loadSbiUserByUserId(id);
            if (sbiUser == null)
                return Response.status(Status.BAD_REQUEST).entity(format("User %s not found", id)).build();

            usersDao.setUserProfile(getUserProfile());
            // reload user by id as loadSbiUserByUserId doesn't load user associated data, e.g. roles
            sbiUser = usersDao.loadSbiUserById(sbiUser.getId());
            UserBO user = new SbiUserDAOHibImpl().toUserBO(sbiUser);
            return Response.ok(user).build();
        }
        catch (Exception e)
        {
            String msg = format("Error finding user with selected id: %s", id);
            logger.info(msg, e);
            throw new SpagoBIRestServiceException(msg, buildLocaleFromSession(), e);
        }
    }
}
