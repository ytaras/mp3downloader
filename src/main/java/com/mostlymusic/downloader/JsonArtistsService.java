package com.mostlymusic.downloader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;

import com.google.gson.reflect.TypeToken;
import com.mostlymusic.downloader.client.Artist;
import com.mostlymusic.downloader.client.ArtistsService;
import com.mostlymusic.downloader.client.JsonServiceClient;
import com.mostlymusic.downloader.client.exceptions.RequestException;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author ytaras
 *         Date: 9/26/11
 *         Time: 4:37 PM
 */
public class JsonArtistsService extends JsonServiceClient implements ArtistsService {

    private static final Type ARTISTS_TYPE = new TypeToken<List<Artist>>() {
    }.getType();
    private final String serviceUrl;

    @Inject
    public JsonArtistsService(@ServiceUrl String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Override
    public List<Artist> getArtists(List<Long> artistIds) throws IOException, RequestException {
        LinkedList<NameValuePair> params = new LinkedList<NameValuePair>();
        StringBuilder builder = new StringBuilder();
        for (Long artistId : artistIds) {
            builder.append(artistId).append(",");
        }
        params.add(new BasicNameValuePair(ArtistsService.IDS_PARAM_NAME, builder.toString()));
        HttpPost post = new HttpPost(serviceUrl + "/download-manager/sync/artistsInfo/");
        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        return getResult(post, ARTISTS_TYPE);
    }
}
