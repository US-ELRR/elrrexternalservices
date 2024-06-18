package com.deloitte.elrr.dto;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.net.URL;

import gov.adlnet.xapi.client.StatementClient;

public class XapiStatementClient extends StatementClient {
    public XapiStatementClient(String uri, String user, String password) throws java.net.MalformedURLException {
        super(uri, user, password);
    }

    public XapiStatementClient(URL uri, String user, String password) throws MalformedURLException {
        super(uri, user, password);
    }

    public XapiStatementClient(String uri, String encodedUsernamePassword) throws MalformedURLException {
        super(uri, encodedUsernamePassword);
    }

    public XapiStatementClient(URL uri, String encodedUsernamePassword) throws MalformedURLException {
        super(uri, encodedUsernamePassword);
    }

    @Override
    protected HttpURLConnection initializeConnection(URL url)
            throws IOException {
        HttpURLConnection conn = super.initializeConnection(url);

        conn.setRequestProperty("Accept", "application/json");

        return conn;
    }
}
