package io.joshatron.downloader.series;

import io.joshatron.downloader.App;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class TvdbInterfaceTest {

    @Test
    public void searchSeriesName() {
        TvdbInterface tvdb = getInterface();

        List<SeriesInfo> series = tvdb.searchSeriesName("Modern_Family");
        Assert.assertFalse(series.isEmpty());
        Assert.assertEquals(series.get(0).getSeriesId(), "95011");
        Assert.assertEquals(series.get(0).getSeriesTitle(), "Modern Family");
        Assert.assertEquals(series.get(0).getStartYear(), 2009);
        Assert.assertFalse(series.get(0).getSeriesDescription().isEmpty());
    }

    private TvdbInterface getInterface() {
        try {
            Properties properties = new Properties();
            properties.load(App.class.getClassLoader().getResourceAsStream("application.properties"));
            return new TvdbInterface(properties.getProperty("tvdb.api-key"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}