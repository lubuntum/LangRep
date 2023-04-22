package com.stud.langrep;

import org.junit.Test;

import static org.junit.Assert.*;

import com.stud.langrep.api.translateapi.TranslateAPI;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect()
    {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void testAPI() throws IOException {
        TranslateAPI api = new TranslateAPI();
        String text = api.translateText("Hello","en","ru").toLowerCase();
        assertEquals(text,"здравствуйте!");
    }
}