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
package fr.paris.lutece.plugins.unittree.modules.notification.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 *
 */
public class NotificationDAO implements INotificationDAO
{
	private final String SQL_SELECT_BY_UNIT = "SELECT id_unit, email, has_notif, use_email, use_list FROM unittree_unit_notification WHERE id_unit = ?";
	private final String SQL_DELETE_BY_UNIT = "DELETE FROM unittree_unit_notification WHERE id_unit = ?";
	//WARNING, UPDATE and INSERT HAVE TO have the same parameter order
	private final String SQL_UPDATE_NOTIFICATION = "UPDATE unittree_unit_notification SET email = ?, has_notif = ?, use_email = ?, use_list = ? WHERE id_unit = ?";
	private final String SQL_INSERT_NOTIFICATION = "INSERT INTO unittree_unit_notification ( email, has_notif, use_email, use_list, id_unit ) VALUES ( ?, ?, ?, ?, ? )";
	/**
	 * {@inheritDoc}
	 */
    @Override
    public Notification loadByIdUnit( int nIdUnit, Plugin plugin )
    {
    	Notification notification = null;

        DAOUtil daoUtil = new DAOUtil( SQL_SELECT_BY_UNIT, plugin );
        daoUtil.setInt( 1, nIdUnit );
        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            int nIndex = 1;

            notification = new Notification( );
            notification.setIdUnit( daoUtil.getInt( nIndex++ ) );
            notification.setEmail( daoUtil.getString( nIndex++ ) );
            notification.setHasNotif( daoUtil.getBoolean( nIndex++ ) );
            notification.setUseEmail( daoUtil.getBoolean( nIndex++ ) );
            notification.setUseList( daoUtil.getBoolean( nIndex++ ) );
        }

        daoUtil.free( );

        return notification;
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void removeForUnit( int nIdUnit, Plugin plugin )
    {
    	DAOUtil daoUtil = new DAOUtil( SQL_DELETE_BY_UNIT, plugin );
        daoUtil.setInt( 1, nIdUnit );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void mergeConfiguration( Notification notification, Plugin plugin )
    {
	    if( notification!=null )
	    {
	    	DAOUtil daoUtil;
	    	if( loadByIdUnit( notification.getIdUnit( ), plugin ) != null )
	    	{
	    		//UPDATE
	    		daoUtil = new DAOUtil( SQL_UPDATE_NOTIFICATION, plugin );
	    	}
	    	else
	    	{
	    		//INSERT
	    		daoUtil = new DAOUtil( SQL_INSERT_NOTIFICATION, plugin );
	    	}
	    	
	    	int nIndex = 1;
	    	daoUtil.setString( nIndex++, notification.getEmail( ) );
	    	daoUtil.setBoolean( nIndex++, notification.getHasNotif( ) );
	    	daoUtil.setBoolean( nIndex++, notification.getUseEmail( ) );
	    	daoUtil.setBoolean( nIndex++, notification.getUseList( ) );
	    	daoUtil.setInt( nIndex++, notification.getIdUnit( ) );
	    	
	        daoUtil.executeUpdate( );
	        daoUtil.free( );
	    }	    
    }

	
	
}
