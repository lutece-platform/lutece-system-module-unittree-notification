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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.unittree.business.unit.IUnitAttribute;
import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.modules.notification.business.Notification;
import fr.paris.lutece.plugins.unittree.modules.notification.business.NotificationHome;
import fr.paris.lutece.plugins.unittree.modules.notification.business.NotificationUnitAttribute;
import fr.paris.lutece.plugins.unittree.service.UnitErrorException;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitService;
import fr.paris.lutece.plugins.unittree.service.unit.IUnitUserService;
import fr.paris.lutece.portal.business.user.AdminUser;

/**
 *
 */
@ApplicationScoped
@Named( NotificationService.BEAN_NAME )
public class NotificationService implements INotificationService
{
    public static final String BEAN_NAME = "unittree-notification.notificationService";

    // Markers
    public static final String MARK_UNIT_NOTIF = "unit_notif";
    public static final String MARK_UNIT_EMAIL = "unit_email";

    // Constant
    private static final String NOTIF_NO = "no";
    private static final String NOTIF_EMAIL = "email";
    private static final String NOTIF_LIST = "list";
    private static final String NOTIF_BOTH = "both";

    // Message
    private static final String MESSAGE_ERROR_EMAIL_MANDATORY = "module.unittree.notification.message.error.email.mandatory";
    private static final String MESSAGE_ERROR_EMAIL_FORMAT = "module.unittree.notification.message.error.email.format";
    
    // Email pattern
    private static final String CONSTANT_EMAIL = "(^([a-zA-Z0-9]+(([\\.\\-\\_]?[a-zA-Z0-9]+)+)?)\\@(([a-zA-Z0-9]+[\\.\\-\\_])+[a-zA-Z]{2,4})$)|(^$)";
    
    @Inject
    private IUnitService _unitService;
    
    @Inject
    private IUnitUserService _unitUserService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void doCreateUnit( Unit unit, HttpServletRequest request )
    {
        // the unit in paramater HAS BEEN populate by the request before this call
        // but the id of the unit is not set in the UnitAttribute because the unit wasn't created at the populate call.
        if ( unit.getAttribute( NotificationUnitAttribute.ATTRIBUTE_NAME ) != null )
        {
            Notification notif = (Notification) unit.getAttribute( NotificationUnitAttribute.ATTRIBUTE_NAME ).getAttribute( );
            notif.setIdUnit( unit.getIdUnit( ) );
            NotificationHome.mergeConfiguration( notif );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doModifyUnit( Unit unit, HttpServletRequest request )
    {
        // the unit in parameter HAS BEEN populate by the request before this call
        if ( unit.getAttribute( NotificationUnitAttribute.ATTRIBUTE_NAME ) != null )
        {
            Notification notif = (Notification) unit.getAttribute( NotificationUnitAttribute.ATTRIBUTE_NAME ).getAttribute( );
            NotificationHome.mergeConfiguration( notif );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doRemoveUnit( int nIdUnit, HttpServletRequest request )
    {
        NotificationHome.removeForUnit( nIdUnit );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void populate( Unit unit )
    {
        if ( unit == null )
        {
            return;
        }

        NotificationUnitAttribute notifUnitAtt = new NotificationUnitAttribute( );
        Notification notificationUnit = NotificationHome.loadByUnit( unit );
        if ( notificationUnit == null )
        {
            notificationUnit = new Notification( );
            notificationUnit.setIdUnit( unit.getIdUnit( ) );
        }
        notifUnitAtt.setAttribute( notificationUnit );

        unit.addAttribute( notifUnitAtt );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void populate( Unit unit, HttpServletRequest request ) throws UnitErrorException
    {
        // use to control request and populate unit before calling dao
        String strUnitEmail = request.getParameter( MARK_UNIT_EMAIL );
        String strUnitNotif = request.getParameter( MARK_UNIT_NOTIF );

        if ( StringUtils.isEmpty( strUnitEmail ) && ( NOTIF_BOTH.equals( strUnitNotif ) || NOTIF_EMAIL.equals( strUnitNotif ) ) )
        {
            throw new UnitErrorException( MESSAGE_ERROR_EMAIL_MANDATORY );
        }
        if ( StringUtils.isNotEmpty( strUnitEmail ) )
        {
            if ( !isValidEmail( strUnitEmail ) )
            {
                throw new UnitErrorException( MESSAGE_ERROR_EMAIL_FORMAT );
            }
        }

        Notification notif = new Notification( );
        notif.setIdUnit( unit.getIdUnit( ) );
        notif.setEmail( strUnitEmail );
        notif.setHasNotif( !NOTIF_NO.equals( strUnitNotif ) );
        notif.setUseEmail( NOTIF_EMAIL.equals( strUnitNotif ) || NOTIF_BOTH.equals( strUnitNotif ) );
        notif.setUseList( NOTIF_LIST.equals( strUnitNotif ) || NOTIF_BOTH.equals( strUnitNotif ) );

        NotificationUnitAttribute notifAttribute = new NotificationUnitAttribute( );
        notifAttribute.setAttribute( notif );
        unit.addAttribute( notifAttribute );
    }

    /**
     * Checks if the string in parameter is a valid email address
     * 
     * @param strEmail
     *            The email address to check
     * @return true if strEmail is a valid email address, false otherwise
     */
    private boolean isValidEmail( String strEmail )
    {
    	return Pattern.matches( CONSTANT_EMAIL, strEmail );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canCreateSubUnit( int nIdUnit )
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveSubTree( Unit unitToMove, Unit newUnitParent )
    {
        // NOTHING TO DO, email is linked to the unit no correlation with parent
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getUnitUsersEmail( int nUnitId )
    {
        Unit unit = _unitService.getUnit( nUnitId, true );

        List<String> listMail = new ArrayList<String>( );
        IUnitAttribute<Notification> notifUnitAtt = unit.getAttribute( NotificationUnitAttribute.ATTRIBUTE_NAME );

        if ( notifUnitAtt != null )
        {
            Notification notification = notifUnitAtt.getAttribute( );

            if ( notification != null && notification.getHasNotif( ) )
            {
                if ( notification.getUseEmail( ) )
                {
                    listMail.add( notification.getEmail( ) );
                }

                if ( notification.getUseList( ) )
                {
                    List<AdminUser> listUsers = _unitUserService.getUsers( notification.getIdUnit( ), new HashMap<String, Unit>( ), false );

                    for ( AdminUser adminUser : listUsers )
                    {
                        listMail.add( adminUser.getEmail( ) );
                    }
                }
            }
        }

        return listMail;
    }
}
