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

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.unittree.business.unit.Unit;
import fr.paris.lutece.plugins.unittree.modules.notification.business.Notification;
import fr.paris.lutece.plugins.unittree.modules.notification.business.NotificationHome;
import fr.paris.lutece.plugins.unittree.modules.notification.business.NotificationUnitAttribute;
import fr.paris.lutece.plugins.unittree.service.UnitErrorException;

/**
 *
 */
public class NotificationService implements INotificationService
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doCreateUnit( Unit unit, HttpServletRequest request )
	{
		//NOTHING TO DO, default notification is nothing
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doModifyUnit( Unit unit, HttpServletRequest request )
	{
		//the unit in parameter HAS BEEN populate by the request before this call
		//TODO
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
		if( unit == null )
		{
			return;
		}
		
		NotificationUnitAttribute notifUnitAtt = new NotificationUnitAttribute( );
		Notification notificationUnit = NotificationHome.loadByUnit( unit );
		if( notificationUnit == null )
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
		//TODO use to control request and populate unit before dao 
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

}
