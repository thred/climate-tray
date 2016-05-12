/*
 * Copyright 2015, 2016 Manfred Hantschel
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
import io.github.thred.climatetray.util.WildcardPattern;
import io.github.thred.climatetray.util.prefs.Prefs;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.HttpContext;

public class ClimateTrayProxySettings implements Persistent
{

    private static final String KEY = "correct horse battery staple";

    private ProxyType proxyType = ProxyType.SYSTEM_DEFAULT;
    private String proxyHost = "";
    private Integer proxyPort = 80;
    private boolean proxyAuthorizationNeeded = false;
    private String proxyUser = "";
    private String proxyPassword = "";
    private String proxyExcludes = "localhost";

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

    public CloseableHttpClient createHttpClient(String... additionalProxyExcludes)
    {
        if (proxyType == ProxyType.NONE)
        {
            return HttpClients.createDefault();
        }

        if (proxyType == ProxyType.SYSTEM_DEFAULT)
        {
            return HttpClients.createSystem();
        }
        
        HttpHost proxy = new HttpHost(getProxyHost(), getProxyPort());
        HttpClientBuilder builder = HttpClientBuilder.create().setProxy(proxy);

        if (isProxyAuthorizationNeeded())
        {
            Credentials credentials = new UsernamePasswordCredentials(getProxyUser(), getProxyPassword());
            AuthScope authScope = new AuthScope(getProxyHost(), getProxyPort());
            CredentialsProvider credsProvider = new BasicCredentialsProvider();

            credsProvider.setCredentials(authScope, credentials);

            builder.setDefaultCredentialsProvider(credsProvider);
        }

        String excludes = proxyExcludes;

        if (Utils.isBlank(excludes))
        {
            excludes = "";
        }

        for (String additionalProxyExclude : additionalProxyExcludes)
        {
            if (excludes.length() > 0)
            {
                excludes += ", ";
            }

            excludes += additionalProxyExclude;
        }

        if (!Utils.isBlank(excludes))
        {
            WildcardPattern pattern = new WildcardPattern(excludes.split("\\s*,\\s*"));
            HttpRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy)
            {
                @Override
                public HttpRoute determineRoute(HttpHost host, HttpRequest request, HttpContext context)
                    throws HttpException
                {
                    InetAddress address = host.getAddress();

                    if (address == null)
                    {
                        try
                        {
                            address = InetAddress.getByName(host.getHostName());
                        }
                        catch (UnknownHostException e)
                        {
                            ClimateTray.LOG.info("Failed to determine address of host \"%s\"", host.getHostName());
                        }
                    }

                    if (address != null)
                    {
                        String hostAddress = address.getHostAddress();

                        if (pattern.matches(hostAddress))
                        {
                            return new HttpRoute(host);
                        }
                    }

                    String hostName = host.getHostName();

                    if (pattern.matches(hostName))
                    {
                        return new HttpRoute(host);
                    }

                    return super.determineRoute(host, request, context);
                }
            };

            builder.setRoutePlanner(routePlanner);
        }

        return builder.build();
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
