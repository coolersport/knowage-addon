/*
 * Knowage, Open Source Business Intelligence suite
 * Copyright (C) 2016 Engineering Ingegneria Informatica S.p.A.
 *
 * Knowage is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Knowage is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.eng.spagobi.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IRoleDAO;
import it.eng.spagobi.commons.metadata.SbiExtRoles;
import it.eng.spagobi.profiling.bean.SbiUser;
import it.eng.spagobi.profiling.bean.SbiUserAttributes;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;

@SuppressWarnings("all")
public class InternalSecurityServiceSupplierImpl implements ISecurityServiceSupplier
{

    static private Logger logger = Logger.getLogger(InternalSecurityServiceSupplierImpl.class);

    private SpagoBIUserProfile checkAuthentication(final SbiUser user, final String userId, final String psw)
    {
        logger.debug("IN - userId: " + userId);

        if (userId == null)
        {
            return null;
        }

        // get user from database

        try
        {

            String password = user.getPassword();
            if (password == null || password.length() == 0)
            {
                logger.error("UserName/pws not defined into database");
                return null;
            }
            else if (!BCrypt.checkpw(psw, password))
            {
                logger.error("UserName/pws not found into database");
                return null;
            }

            logger.debug("Logged in with SHA pass");
            SpagoBIUserProfile obj = new SpagoBIUserProfile();
            obj.setUniqueIdentifier(user.getUserId());
            obj.setUserId(user.getUserId());
            obj.setUserName(user.getFullName());
            obj.setOrganization(user.getCommonInfo().getOrganization());
            obj.setIsSuperadmin(user.getIsSuperadmin());

            logger.debug("OUT");
            return obj;
        }
        catch (Exception e)
        {
            logger.error("PASS decrypt error:" + e.getMessage(), e);
        }
        return null;

    }

    @Override
    public SpagoBIUserProfile checkAuthentication(final String userId, final String psw)
    {
        logger.debug("IN - userId: " + userId);

        if (userId != null)
        {
            SbiUser user;
            try
            {
                user = DAOFactory.getSbiUserDAO().loadSbiUserByUserId(userId);
                if (user == null)
                {
                    logger.error("UserName not found into database");
                    return null;
                }
            }
            catch (EMFUserError e)
            {
                logger.error(e.getMessage(), e);
                return null;
            }
            return checkAuthentication(user, userId, psw);
        }
        return null;
    }

    @Override
    public SpagoBIUserProfile checkAuthenticationWithToken(final String userId, final String token)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean checkAuthorization(final String userId, final String function)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public SpagoBIUserProfile createUserProfile(final String userId)
    {
        logger.debug("IN - userId: " + userId);
        SpagoBIUserProfile profile = null;
        try
        {
            SbiUser user = DAOFactory.getSbiUserDAO().loadSbiUserByUserId(userId);

            if (user == null)
            {
                logger.error("UserName [" + userId + "] not found!!");
                return null;
            }

            profile = new SpagoBIUserProfile();
            profile.setUniqueIdentifier(user.getUserId());
            profile.setUserId(user.getUserId());
            profile.setUserName(user.getFullName());
            profile.setOrganization(user.getCommonInfo().getOrganization());
            profile.setIsSuperadmin(user.getIsSuperadmin());

            // get user name
            String userName = userId;
            // get roles of the user

            ArrayList<SbiExtRoles> rolesSB = DAOFactory.getSbiUserDAO().loadSbiUserRolesById(user.getId());
            List roles = new ArrayList();
            Iterator iterRolesSB = rolesSB.iterator();

            IRoleDAO roleDAO = DAOFactory.getRoleDAO();
            while (iterRolesSB.hasNext())
            {
                SbiExtRoles roleSB = (SbiExtRoles) iterRolesSB.next();

                roles.add(roleSB.getName());
            }
            HashMap attributes = new HashMap();
            ArrayList<SbiUserAttributes> attribs = DAOFactory.getSbiUserDAO().loadSbiUserAttributesById(user.getId());
            if (attribs != null)
            {
                Iterator iterAttrs = attribs.iterator();
                while (iterAttrs.hasNext())
                {
                    // Attribute to lookup
                    SbiUserAttributes attribute = (SbiUserAttributes) iterAttrs.next();

                    String attributeName = attribute.getSbiAttribute().getAttributeName();

                    String attributeValue = attribute.getAttributeValue();
                    if (attributeValue != null)
                    {
                        logger
                            .debug("Add attribute. " + attributeName + "=" + attributeName + " to the user" + userName);
                        attributes.put(attributeName, attributeValue);
                    }
                }
            }

            logger.debug("Attributes load into SpagoBI profile: " + attributes);

            // end load profile attributes

            String[] roleStr = new String[roles.size()];
            for (int i = 0; i < roles.size(); i++)
            {
                roleStr[i] = (String) roles.get(i);
            }

            profile.setRoles(roleStr);
            profile.setAttributes(attributes);
        }
        catch (EMFUserError e)
        {
            logger.error(e.getMessage(), e);
            return null;
        }
        logger.debug("OUT");
        return profile;

    }

    @Override
    public SpagoBIUserProfile checkAuthenticationToken(final String token)
    {
        return null;
    }

}
