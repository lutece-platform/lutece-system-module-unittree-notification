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

/**
 *
 */
public class Notification
{
	private int _nIdUnit;
	private String _strEmail = "";
	private boolean _bHasNotif = false;
	private boolean _bUseEmail = false;
	private boolean _bUseList = false;
	/**
	 * @return the id of the unit
	 */
	public int getIdUnit()
	{
		return _nIdUnit;
	}
	/**
	 * @param nIdUnit the id of the unit
	 */
	public void setIdUnit( int nIdUnit )
	{
		this._nIdUnit = nIdUnit;
	}
	/**
	 * @return the set Email
	 */
	public String getEmail()
	{
		return _strEmail;
	}
	/**
	 * @param strEmail the Email to set
	 */
	public void setEmail( String strEmail )
	{
		this._strEmail = strEmail;
	}
	/**
	 * @return true if the unit accept notification
	 */
	public boolean getHasNotif()
	{
		return _bHasNotif;
	}
	/**
	 * @param bHasNotif true if the unit accept notification
	 */
	public void setHasNotif( boolean bHasNotif )
	{
		this._bHasNotif = bHasNotif;
	}
	/**
	 * @return true if the unit has is own email and use it for notification (email should not be null)
	 */
	public boolean getUseEmail()
	{
		return _bUseEmail;
	}
	/**
	 * @param bUseEmail true if the unit has is own email and use it for notification (email should not be null)
	 */
	public void setUseEmail( boolean bUseEmail )
	{
		this._bUseEmail = bUseEmail;
	}
	/**
	 * @return true if the unit use all user email of the unit for notification
	 */
	public boolean getUseList()
	{
		return _bUseList;
	}
	/**
	 * @param bUseList true if the unit use all user email of the unit for notification
	 */
	public void setUseList( boolean bUseList )
	{
		this._bUseList = bUseList;
	}
	
	
}
