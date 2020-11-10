package io.joshatron.downloader.movie;

import io.joshatron.downloader.series.SeriesInfo;
import io.joshatron.downloader.series.SeriesInterface;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TmdbInterface implements MovieInteface, SeriesInterface {
    private String BASE_URL = "https://api.themoviedb.org/3/";

    private String apiKey;

    public TmdbInterface(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public List<MovieInfo> searchMovieName(String movieName) {
        JSONObject json = makeApiGetCall(BASE_URL + "search/movie?query=" + movieName);

        List<MovieInfo> info = new ArrayList<>();
        JSONArray movies = json.getJSONArray("results");

        for(int i = 0; i < movies.length(); i++) {
            info.add(jsonToMovieInfo(movies.getJSONObject(i)));
        }

        return info;
    }

    @Override
    public MovieInfo getMovieInfo(String movieId) {
        JSONObject json = makeApiGetCall(BASE_URL + "movie/" + movieId);
        return jsonToMovieInfo(json);
    }

    private MovieInfo jsonToMovieInfo(JSONObject movieJson) {
        MovieInfo info = new MovieInfo();
        info.setMovieId(Integer.toString(movieJson.getInt("id")));
        info.setMovieTitle(movieJson.getString("title"));
        info.setMovieDescription(movieJson.getString("overview"));
        if(movieJson.getString("release_date").length() >= 4) {
            info.setMovieYear(Integer.parseInt(movieJson.getString("release_date").substring(0, 4)));
        }

        return info;
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

    @Override
    public List<SeriesInfo> searchSeriesName(String seriesName) {
        JSONObject json = makeApiGetCall(BASE_URL + "search/tv?query=" + seriesName);

        List<SeriesInfo> info = new ArrayList<>();
        JSONArray series = json.getJSONArray("results");

        for(int i = 0; i < series.length(); i++) {
            info.add(jsonToSeriesInfo(series.getJSONObject(i)));
        }

        return info;
    }

    @Override
    public SeriesInfo getSeriesInfo(String seriesId) {
        return null;
    }

    @Override
    public String getEpisodeTitle(String seriesId, int season, int episode) {
        return null;
    }

    private SeriesInfo jsonToSeriesInfo(JSONObject movieJson) {
        SeriesInfo info = new SeriesInfo();
        info.setSeriesId(Integer.toString(movieJson.getInt("id")));
        info.setSeriesTitle(movieJson.getString("name"));
        if(movieJson.getString("first_air_date").length() >= 4) {
            info.setStartYear(Integer.parseInt(movieJson.getString("first_air_date").substring(0, 4)));
        }

        return info;
    }
}