/*
 * Copyright (c) 2002-2017, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.unittree.modules.notification.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.modules.notification.business.Notification;
import fr.paris.lutece.plugins.unittree.modules.notification.business.NotificationUnitAttribute;
import fr.paris.lutece.plugins.unittree.service.UnitErrorException;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;

/**
 * Tests the validation of the unit email(s) done by NotificationService.populate( Unit, HttpServletRequest )
 */
public class NotificationServiceTest
{
    private static final String MESSAGE_ERROR_EMAIL_MANDATORY = "module.unittree.notification.message.error.email.mandatory";
    private static final String MESSAGE_ERROR_EMAIL_FORMAT = "module.unittree.notification.message.error.email.format";
    private static final String MESSAGE_ERROR_EMAIL_LENGTH = "module.unittree.notification.message.error.email.length";

    private final NotificationService _service = new NotificationService( );

    @ParameterizedTest
    @ValueSource( strings = {
            "jean.dupont@paris.fr", "jean.dupont@paris.fr;marie-curie@ville.paris.fr", "a@b.fr;c@d.com;e_f@g.h.org"
    } )
    void testValidEmails( String strEmail ) throws UnitErrorException
    {
        assertEquals( strEmail, populate( strEmail, "email" ).getEmail( ) );
    }

    @Test
    void testWhitespacesAreRemoved( ) throws UnitErrorException
    {
        assertEquals( "a@b.fr;c@d.com", populate( " a@b.fr ; c@d.com ", "email" ).getEmail( ) );
    }

    @ParameterizedTest
    @ValueSource( strings = {
            "notanemail", "a@b", "a@b.fr;", ";a@b.fr", "a@b.fr;;c@d.com", "a@b.fr,c@d.com", "a@b.fr;notanemail"
    } )
    void testInvalidEmails( String strEmail )
    {
        assertError( strEmail, "email", MESSAGE_ERROR_EMAIL_FORMAT );
    }

    @Test
    void testEmailMandatory( )
    {
        assertError( "", "email", MESSAGE_ERROR_EMAIL_MANDATORY );
        assertError( "   ", "both", MESSAGE_ERROR_EMAIL_MANDATORY );
        assertError( null, "email", MESSAGE_ERROR_EMAIL_MANDATORY );
    }

    @Test
    void testEmailNotMandatoryWithoutEmailNotification( ) throws UnitErrorException
    {
        assertNull( populate( null, "no" ).getEmail( ) );
        assertTrue( populate( "", "list" ).getUseList( ) );
    }

    @Test
    void testEmailMaxLength( ) throws UnitErrorException
    {
        // "aaaa...@b.fr" with exactly EMAIL_MAX_LENGTH characters
        String strDomain = "@b.fr";
        String strMaxEmail = StringUtils.repeat( 'a', NotificationService.EMAIL_MAX_LENGTH - strDomain.length( ) ) + strDomain;

        assertEquals( strMaxEmail, populate( strMaxEmail, "email" ).getEmail( ) );
        assertError( "a" + strMaxEmail, "email", MESSAGE_ERROR_EMAIL_LENGTH );
    }

    private Notification populate( String strEmail, String strNotif ) throws UnitErrorException
    {
        Unit unit = new Unit( );
        _service.populate( unit, buildRequest( strEmail, strNotif ) );

        return (Notification) unit.getAttribute( NotificationUnitAttribute.ATTRIBUTE_NAME ).getAttribute( );
    }

    private void assertError( String strEmail, String strNotif, String strExpectedMessage )
    {
        UnitErrorException e = assertThrows( UnitErrorException.class, ( ) -> _service.populate( new Unit( ), buildRequest( strEmail, strNotif ) ) );
        assertEquals( strExpectedMessage, e.getI18nErrorMessage( ) );
    }

    private static MockHttpServletRequest buildRequest( String strEmail, String strNotif )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        if ( strEmail != null )
        {
            request.addParameter( NotificationService.MARK_UNIT_EMAIL, strEmail );
        }
        request.addParameter( NotificationService.MARK_UNIT_NOTIF, strNotif );

        return request;
    }
}
