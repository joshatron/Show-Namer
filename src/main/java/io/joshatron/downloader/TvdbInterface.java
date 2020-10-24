package io.joshatron.downloader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

public class TvdbInterface {

    private static final String BASE_URL = "https://api.thetvdb.com/";

    private String jwtToken;
    private String apiKey;

    public TvdbInterface(String apiKey) {
        this.apiKey = apiKey;
        getJwtToken();
    }

    public String getSeriesId(String seriesName) {
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            String url = BASE_URL + "search/series?name=" + seriesName;
            HttpGet request = new HttpGet(url);
            request.setHeader("Authorization", "Bearer " + jwtToken);

            HttpResponse response = client.execute(request);
            String contents = EntityUtils.toString(response.getEntity());
            JSONObject json = new JSONObject(contents);
            return Integer.toString(json.getJSONArray("data").getJSONObject(0).getInt("id"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public String getEpisode(String id, int season, int episode) {
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            String url = BASE_URL + "series/" + id + "/episodes/query?airedSeason=" + season + "&airedEpisode=" + episode;
            HttpGet request = new HttpGet(url);
            request.setHeader("Authorization", "Bearer " + jwtToken);

            HttpResponse response = client.execute(request);
            String contents = EntityUtils.toString(response.getEntity());
            JSONObject json = new JSONObject(contents);

            return json.getJSONArray("data").getJSONObject(0).getString("episodeName");
        } catch(IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private void getJwtToken() {
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            String url = "https://api.thetvdb.com/login";
            String body = "{\"apikey\":\"" + apiKey + "\"}";

            HttpPost request = new HttpPost(url);
            request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
            HttpResponse response = client.execute(request);
            String contents = EntityUtils.toString(response.getEntity());
            JSONObject json = new JSONObject(contents);
            jwtToken = json.getString("token");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
