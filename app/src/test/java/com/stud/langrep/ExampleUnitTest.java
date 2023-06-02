package com.stud.langrep;

import org.junit.Test;

import static org.junit.Assert.*;

import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.stud.langrep.api.translateapi.TranslateAPI;
import com.stud.langrep.database.entity.Word;
import com.stud.langrep.test.Tester;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
    @Test
    public void regex_isCorrect(){
        assertTrue("500".matches("\\d*"));
    }
    @Test
    public void testRegard(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.regard.ru/reciept/458612/processory-intel-core-i5")
                .build();
        try(Response response = client.newCall(request).execute()){
            //JSONObject json = new JSONObject(response.body().string());
            String testStr = response.body().string();
            boolean intel = testStr.contains("Card_row__6_JG5");
            System.out.println(intel);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    @Test
    public void TTSTest() throws IOException {
        List<Word> wordList = Tester.generateWords();
        TextToSpeechTest ttsTest = new TextToSpeechTest();
        /* Увы не пригодилось, потому что только с ru => en,
        если бы были все языки то можно было бы разойтись покруче
        List<LanguageProfile> languageProfileList = new LanguageProfileReader().readAllBuiltIn();
        LanguageDetector detector = LanguageDetectorBuilder
                .create(NgramExtractors.standard())
                .withProfiles(languageProfileList)
                .build();

         */

        for(Word word: wordList){
            StringBuilder text = new StringBuilder();
            text.append("<speak>");
            text.append(word.getNativeWord()).append("<break time=\"500ms\"/>").append("</speak>");
            ttsTest.setLanguage(new Locale("ru"));
            ttsTest.speak(text.toString());//for example
            assertEquals(ttsTest.language.getLanguage(),"ru");

            text.setLength(0);
            text.append("<speak>");
            text.append(word.getTranslatedWord()).append("<break time=\"500ms\"/>").append("</speak>");
            ttsTest.setLanguage(new Locale("en"));
            ttsTest.speak(text.toString());
            assertEquals(ttsTest.language.getLanguage(),"en");//for example
        }
    }
    static class TextToSpeechTest{
        private Locale language;
        public void speak(String text){
            System.out.println(text + language.getCountry());
        }

        public void setLanguage(Locale language) {
            this.language = language;
        }
    }
}