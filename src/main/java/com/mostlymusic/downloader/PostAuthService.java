package com.mostlymusic.downloader;

import com.google.inject.Inject;
import com.mostlymusic.downloader.client.AuthService;
import com.mostlymusic.downloader.client.JsonServiceClient;
import com.mostlymusic.downloader.client.exceptions.ForbiddenException;
import com.mostlymusic.downloader.client.exceptions.RequestException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.LinkedList;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 6:26 PM
 */
public class PostAuthService extends JsonServiceClient implements AuthService {
    private String serviceUrl;

    @Inject
    public PostAuthService(@ServiceUrl String serviceUrl) {
        this.serviceUrl = serviceUrl + "/download-manager/sync/loginPost/";
    }

    @Override
    public boolean auth(String name, String pass) throws IOException, RequestException {
        LinkedList<NameValuePair> nameValuePairs = new LinkedList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair(USERNAME, name));
        nameValuePairs.add(new BasicNameValuePair(PASSWORD, pass));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs, null);
        HttpPost httpPost = new HttpPost(serviceUrl);
        httpPost.setEntity(urlEncodedFormEntity);
        HttpResponse response = getHttpClient().execute(httpPost);
        try {
            verifyStatus(response);
            return true;
        } catch (ForbiddenException e) {
            return false;
        }
    }
}
