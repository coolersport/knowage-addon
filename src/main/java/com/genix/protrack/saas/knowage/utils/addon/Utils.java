/*
 * FILENAME
 *     Utils.java
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

package com.genix.protrack.saas.knowage.utils.addon;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.encryptor4j.Encryptor;

//
// IMPORTS
// NOTE: Import specific classes without using wildcards.
//

public class Utils
{
    private static final String CIPHER_NAME = "AES/GCM/NoPadding"; //$NON-NLS-1$
    private static final String ALGORITHM_NAME = "AES"; //$NON-NLS-1$
    private static final Encryptor ENCRYPTOR;

    static
    {
        byte[] seeds;
        int size = 16;
        try
        {
            String source = "/" + //$NON-NLS-1$
                Utils.class.getName().replace('.', '/') + ".class"; //$NON-NLS-1$
            seeds = IOUtils.toByteArray(Utils.class.getResourceAsStream(source));
        }
        catch (IOException e)
        {
            throw new RuntimeException("Couldn't generate default secret", e); //$NON-NLS-1$
        }

        int start = seeds.length / 2 - 5;
        byte[] secret = new byte[size];
        for (int i = 0; i < size; i++)
            secret[i] = seeds[start + i];

        SecretKey key = new SecretKeySpec(secret, ALGORITHM_NAME);
        ENCRYPTOR = new Encryptor(key, CIPHER_NAME, 16, 128);
    }

    public static String encrypt(final String userId)
    {
        try
        {
            return Base64.encodeBase64URLSafeString(ENCRYPTOR.encrypt(userId.getBytes(StandardCharsets.UTF_8)));
        }
        catch (GeneralSecurityException e)
        {
            throw new RuntimeException(e);
        }
    }

    static String decrypt(final String base64)
    {
        try
        {
            byte[] bytes = ENCRYPTOR.decrypt(Base64.decodeBase64(base64));
            return new String(bytes, StandardCharsets.UTF_8);
        }
        catch (GeneralSecurityException e)
        {
            return null; // null to indicate error
        }
    }
}
