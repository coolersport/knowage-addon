/*
 * FILENAME
 *     NoopSsoService.java
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

package com.genix.protrack.saas.knowage.addon;

import java.io.IOException;

import javax.portlet.PortletSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import it.eng.spagobi.services.common.SsoServiceInterface;
import it.eng.spagobi.services.security.exceptions.SecurityException;

//
// IMPORTS
// NOTE: Import specific classes without using wildcards.
//

public class NoopSsoService implements SsoServiceInterface
{
    @Override
    public String readTicket(final HttpSession session) throws IOException
    {
        return null;
    }

    @Override
    public String readUserIdentifier(final HttpServletRequest request)
    {
        return null;
    }

    @Override
    public String readUserIdentifier(final PortletSession session)
    {
        return null;
    }

    @Override
    public void validateTicket(final String ticket, final String userId) throws SecurityException
    {
    }
}
