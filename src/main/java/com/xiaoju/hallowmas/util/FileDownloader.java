package com.xiaoju.hallowmas.util;

import org.apache.http.*;
import org.apache.http.auth.*;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by yehonggang on 17/6/20.
 */
public class FileDownloader {
    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException {

        // Credentials
        String username = args[0];
        String password = args[1];

        // Jenkins url
        String parameter1 = args[3];
        String parameter2 = args[4];
        // Create your httpclient
        DefaultHttpClient client = new DefaultHttpClient();

        // Then provide the right credentials
        client.getCredentialsProvider().setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                new UsernamePasswordCredentials(username, password));

        // Generate BASIC scheme object and stick it to the execution context
        BasicScheme basicAuth = new BasicScheme();
        BasicHttpContext context = new BasicHttpContext();
        context.setAttribute("preemptive-auth", basicAuth);

        // Add as the first (because of the zero) request interceptor
        // It will first intercept the request and preemptively initialize the
        // authentication scheme if there is not
        client.addRequestInterceptor(new PreemptiveAuth(), 0);

        HttpGet get = new HttpGet(parameter1);

        try {
            // Execute your request with the given context
            HttpResponse response = client.execute(get, context);
            InputStream input = response.getEntity().getContent();
            byte[] buffer = new byte[8 * 1024];

            try {
                OutputStream output = new FileOutputStream(parameter2);
                try {
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                } finally {
                    output.close();
                }
            } finally {
                input.close();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Preemptive authentication interceptor
     */
    static class PreemptiveAuth implements HttpRequestInterceptor {

        /*
         * (non-Javadoc)
         *
         * @see org.apache.http.HttpRequestInterceptor#process(org.apache.http.
         * HttpRequest, org.apache.http.protocol.HttpContext)
         */
        @SuppressWarnings("deprecation")
        public void process(HttpRequest request, HttpContext context)
                throws HttpException, IOException {
            // Get the AuthState
            AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);

            // If no auth scheme available yet, try to initialize it
            // preemptively
            if (authState.getAuthScheme() == null) {
                AuthScheme authScheme = (AuthScheme) context.getAttribute("preemptive-auth");
                CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
                HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                if (authScheme != null) {
                    Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()));
                    if (creds == null) {
                        throw new HttpException(
                                "No credentials for preemptive authentication");
                    }
                    authState.setAuthScheme(authScheme);
                    authState.setCredentials(creds);
                }
            }

        }

    }


    /**
     * http下载
     */
    public static boolean downLoad(String httpUrl, String saveFile) {
        int bytesum = 0;
        int byteread = 0;

        URL url = null;
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return false;
        }
        FileOutputStream fs = null;
        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            fs = new FileOutputStream(saveFile);

            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
