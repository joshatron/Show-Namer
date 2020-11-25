package io.joshatron.downloader.backend;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TvdbInterface implements SeriesInterface {
    private static final String BASE_URL = "https://api.thetvdb.com/";

    private final String apiKey;
    private String jwtToken;

    public TvdbInterface(String apiKey) {
        this.apiKey = apiKey;
        getJwtToken();
    }

    @Override
    public List<SeriesInfo> searchSeriesName(String seriesName) {
        List<SeriesInfo> info = new ArrayList<>();

        String url = BASE_URL + "search/series?name=" + replaceIllegalCharacters(seriesName);
        JSONObject json = makeApiGetCall(url);

        if(json.has("data")) {
            JSONArray data = json.getJSONArray("data");
            for(int i = 0; i < data.length(); i++) {
                info.add(seriesInfoFromJson(data.getJSONObject(i)));
            }
        }

        return info;
    }

    private String replaceIllegalCharacters(String original) {
        return original.replace(" ", "%20");
    }

    @Override
    public SeriesInfo getSeriesInfo(String seriesId) {
        String url = BASE_URL + "series/" + seriesId;
        JSONObject json = makeApiGetCall(url);

        if(json.has("data")) {
            return seriesInfoFromJson(json.getJSONObject("data"));
        }

        return new SeriesInfo();
    }

    private SeriesInfo seriesInfoFromJson(JSONObject json) {
        SeriesInfo info = new SeriesInfo();

        if(json.has("id")) {
            info.setSeriesId(Integer.toString(json.getInt("id")));
        }
        if(json.has("seriesName")) {
            info.setSeriesTitle(json.getString("seriesName"));
        }
        if(json.has("firstAired")) {
            info.setStartYear(Integer.parseInt(json.getString("firstAired").substring(0, 4)));
        }
        if(json.has("overview")) {
            info.setSeriesDescription(json.getString("overview"));
        }

        return info;
    }

    @Override
    public String getEpisodeTitle(String seriesId, int season, int episode) {
        String url = BASE_URL + "series/" + seriesId + "/episodes/query?"
                + "airedSeason=" + season + "&airedEpisode=" + episode;
        JSONObject json = makeApiGetCall(url);

        return getEpisodeTitleFromJson(json);
    }

    private String getEpisodeTitleFromJson(JSONObject json) {
        if(json.has("data")) {
            JSONArray data = json.getJSONArray("data");
            if(data.length() > 0) {
                JSONObject value = data.getJSONObject(0);
                if(value.has("episodeName")) {
                    return value.getString("episodeName");
                }
            }
        }

        return "";
    }

    private JSONObject makeApiGetCall(String url) {
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            request.setHeader("Authorization", "Bearer " + jwtToken);

            HttpResponse response = client.execute(request);
            String contents = EntityUtils.toString(response.getEntity());
            return new JSONObject(contents);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return new JSONObject();
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
