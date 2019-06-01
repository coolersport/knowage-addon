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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author Franco vuoto (franco.vuoto@eng.it)
 * @author Alessandro Pegoraro (alessandro.pegoraro@eng.it)
 *
 */
@SuppressWarnings("all")
public class Password
{
    // XXX Genix custom code
    private static final Pattern BCRYPTED = Pattern.compile("^\\$2[abxy]\\$[0-9]+\\$.{53}$"); //$NON-NLS-1$

    private String value = "";
    private String encValue = "";

    static private Logger logger = Logger.getLogger(Password.class);

    public Password()
    {
        value = "";
        encValue = "";
    }

    public Password(final String value)
    {
        this.value = value;
        encValue = "";
    }

    public boolean hasAltenateCase()
    {
        return true;
    }

    public boolean hasDigits()
    {

        return true;
    }

    public boolean isEnoughLong()
    {
        return (value.length() >= 8);
    }

    /**
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public String getEncValue() throws InvalidKeyException, NoSuchAlgorithmException
    {

        if (encValue != null)
        {
            // XXX Genix custom code
            encValue = BCrypt.hashpw(value, BCrypt.gensalt());
        }
        return encValue;
    }

    /**
     * @return
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @param string
     */
    public void setValue(final String string)
    {
        value = string;
    }

    /**
     * public method used to store passwords on DB.
     *
     * @param password
     *            password to encrypt
     * @return encrypted password
     * @throws Exception
     *             wrapping InvalidKeyException and NoSuchAlgorithmException
     */
    public static String encriptPassword(String password) throws Exception
    {
        // XXX Genix custom code
        if (password != null && !BCRYPTED.matcher(password).matches())
        {
            Password hashPass = new Password(password);
            password = hashPass.getEncValue();
        }
        return password;
    }
}
