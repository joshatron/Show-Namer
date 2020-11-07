package io.joshatron.downloader.movie;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class TmdbInterface implements MovieInteface {
    private String BASE_URL = "https://api.themoviedb.org/3/";

    private String apiKey;

    public TmdbInterface(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public List<MovieInfo> searchMovieName(String movieName) {
        JSONObject json = makeApiGetCall(BASE_URL + "search/movie?query=" + movieName);
        System.out.println(json.toString(2));
        return null;
    }

    @Override
    public MovieInfo getMovieInfo(String movieId) {
        return null;
    }

    private JSONObject makeApiGetCall(String url) {
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            request.setHeader("Authorization", "Bearer " + apiKey);

            HttpResponse response = client.execute(request);
            String contents = EntityUtils.toString(response.getEntity());
            return new JSONObject(contents);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return new JSONObject();
    }
}
