package io.joshatron.downloader;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AppUtilsTest {
    @Test
    public void prettyNumberLessThan10() {
        Assert.assertEquals(AppUtils.getPrettyNumber(5), "05");
    }

    @Test
    public void prettyNumberMoreThan10() {
        Assert.assertEquals(AppUtils.getPrettyNumber(12), "12");
    }
}