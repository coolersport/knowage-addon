/*
 * FILENAME
 *     LoginModuleEx.java
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

package com.genix.protrack.saas.knowage.core;

import javax.servlet.http.Cookie;

import com.genix.protrack.saas.knowage.utils.addon.Utils;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.commons.services.LoginModule;
import it.eng.spagobi.security.InternalSecurityServiceSupplierImpl;
import it.eng.spagobi.security.InternalSecurityServiceSupplierImpl.AuthenticationCallback;

//
// IMPORTS
// NOTE: Import specific classes without using wildcards.
//

public class LoginModuleEx extends LoginModule implements AuthenticationCallback
{
    @Override
    public void service(final SourceBean request, final SourceBean response) throws Exception
    {
        InternalSecurityServiceSupplierImpl.CALLBACK.set(this);
        try
        {
            super.service(request, response);
        }
        finally
        {
            InternalSecurityServiceSupplierImpl.CALLBACK.set(null);
        }
    }

    @Override
    public void authenticationSucceeded(final String userId)
    {
        this.getHttpResponse().addCookie(ssoCookie(Utils.encrypt(userId)));
    }

    static Cookie ssoCookie(final String userId)
    {
        Cookie cookie = new Cookie("sess", userId);
        cookie.setPath("/");
        cookie.setMaxAge(userId == null ? -1 : 8 * 60 * 60);
        return cookie;
    }
}
