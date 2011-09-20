package com.mostlymusic.downloader;

import com.google.inject.Inject;
import com.mostlymusic.downloader.client.IAuthService;
import com.mostlymusic.downloader.client.JsonServiceClient;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 6:26 PM
 */
public class AuthService extends JsonServiceClient implements IAuthService {
    private String serviceUrl;

    @Inject
    public AuthService(@ServiceUrl String serviceUrl) {
        this.serviceUrl = serviceUrl + "/loginPost";
    }

    @Override
    public boolean auth(String name, String pass) throws IOException {
        LinkedList<NameValuePair> nameValuePairs = new LinkedList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair(USERNAME, name));
        nameValuePairs.add(new BasicNameValuePair(PASSWORD, pass));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs, null);
        HttpPost httpPost = new HttpPost(serviceUrl);
        httpPost.setEntity(urlEncodedFormEntity);
        HttpResponse response = getHttpClient().execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        if (HttpStatus.SC_OK
                == statusCode) {
            return true;
        } else if (HttpStatus.SC_FORBIDDEN == statusCode) {
            return false;
        } else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            response.getEntity().writeTo(stream);
            throw new HttpResponseException(response.getStatusLine().getStatusCode(),
                    stream.toString());
        }
    }
}
