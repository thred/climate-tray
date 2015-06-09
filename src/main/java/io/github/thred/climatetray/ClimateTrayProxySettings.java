/*
 * Copyright 2015 Manfred Hantschel
 * 
 * This file is part of Climate-Tray.
 * 
 * Climate-Tray is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Climate-Tray is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Climate-Tray. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package io.github.thred.climatetray;

import io.github.thred.climatetray.util.Persistent;
import io.github.thred.climatetray.util.ProxyType;
import io.github.thred.climatetray.util.Utils;
import io.github.thred.climatetray.util.prefs.Prefs;

public class ClimateTrayProxySettings implements Persistent
{

    private static final String KEY = "correct horse battery staple";

    private ProxyType proxyType = ProxyType.NONE;
    private String proxyHost = "";
    private Integer proxyPort = 80;
    private boolean proxyAuthorizationNeeded = false;
    private String proxyUser = "";
    private String proxyPassword = "";
    private String proxyExcludes = "";

    public ClimateTrayProxySettings()
    {
        super();
    }

    public ProxyType getProxyType()
    {
        return proxyType;
    }

    public void setProxyType(ProxyType proxyType)
    {
        this.proxyType = proxyType;
    }

    public String getProxyHost()
    {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost)
    {
        this.proxyHost = proxyHost;
    }

    public Integer getProxyPort()
    {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort)
    {
        this.proxyPort = proxyPort;
    }

    public boolean isProxyAuthorizationNeeded()
    {
        return proxyAuthorizationNeeded;
    }

    public void setProxyAuthorizationNeeded(boolean proxyAuthorizationNeeded)
    {
        this.proxyAuthorizationNeeded = proxyAuthorizationNeeded;
    }

    public String getProxyUser()
    {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser)
    {
        this.proxyUser = proxyUser;
    }

    public String getProxyPassword()
    {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword)
    {
        this.proxyPassword = proxyPassword;
    }

    public String getProxyExcludes()
    {
        return proxyExcludes;
    }

    public void setProxyExcludes(String proxyExcludes)
    {
        this.proxyExcludes = proxyExcludes;
    }

    @Override
    public void read(Prefs prefs)
    {
        proxyType = prefs.getEnum(ProxyType.class, "proxyType", proxyType);
        proxyHost = prefs.getString("proxyHost", proxyHost);
        proxyPort = prefs.getInteger("proxyPort", proxyPort);
        proxyAuthorizationNeeded = prefs.getBoolean("proxyAuthorizationNeeded", proxyAuthorizationNeeded);
        proxyUser = prefs.getString("proxyUser", proxyUser);
        proxyPassword = Utils.decrypt(KEY, prefs.getBytes("proxyPassword"));
        proxyExcludes = prefs.getString("proxyExcludes", proxyExcludes);
    }

    @Override
    public void write(Prefs prefs)
    {
        prefs.setEnum("proxyType", proxyType);
        prefs.setString("proxyHost", proxyHost);
        prefs.setInteger("proxyPort", proxyPort);
        prefs.setBoolean("proxyAuthorizationNeeded", proxyAuthorizationNeeded);
        prefs.setString("proxyUser", proxyUser);
        prefs.setBytes("proxyPassword", Utils.crypt(KEY, "proxyPassword"));
        prefs.setString("proxyExcludes", proxyExcludes);
    }

    @Override
    public String toString()
    {
        return "ClimateTrayProxy [proxyType=" + proxyType + ", proxyHost=" + proxyHost + ", proxyPort=" + proxyPort
            + ", proxyAuthorizationNeeded=" + proxyAuthorizationNeeded + ", proxyUser=" + proxyUser
            + ", proxyExcludes=" + proxyExcludes + "]";
    }

}
