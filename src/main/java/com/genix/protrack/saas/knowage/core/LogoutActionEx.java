/*
 * FILENAME
 *     LogoutActionEx.java
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

import static com.genix.protrack.saas.knowage.core.LoginModuleEx.ssoCookie;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.commons.services.LogoutAction;

//
// IMPORTS
// NOTE: Import specific classes without using wildcards.
//

public class LogoutActionEx extends LogoutAction
{
    @Override
    public void service(final SourceBean request, final SourceBean response) throws Exception
    {
        super.service(request, response);
        getHttpResponse().addCookie(ssoCookie(null));
    }
}
