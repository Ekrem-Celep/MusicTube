package com.example.musictube.Download;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;

import info.guardianproject.netcipher.NetCipher;

/**
 * Created by Ekrem on 2016-05-08.
 */
public class Downloader implements com.example.musictube.Extractor.Downloader {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0";

    /* Ladda ner textfilen på den medföljande webbadress som nedladdning ( String )
     * Men ställa HTTP rubrikfältet " Accept- Language" till den medföljande strängen
     * @param URL i textfilen för att återställa innehållet i
     * @param language the language (usually a 2-character code) // för att ställa som önskat språk
     * @return innehållet i den angivna textfilen */

    public String download(String siteUrl, String language) throws IOException {
        URL url = new URL(siteUrl);
        //HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        HttpsURLConnection con = NetCipher.getHttpsURLConnection(url);
        con.setRequestProperty("Accept-Language", language);
        return dl(con);
    }

    /* Gemensam funktionalitet mellan nedladdning ( String url ) och ladda ner ( String url,
    String språk)*/

    private static String dl(HttpsURLConnection con) throws IOException {
        StringBuilder response = new StringBuilder();
        BufferedReader in = null;

        try {
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;

            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch(UnknownHostException uhe) { // kastas när det finns ingen internet-anslutning
            throw new IOException("unknown host or no network", uhe);
        } catch(Exception e) {
            throw new IOException(e);
        } finally {
            if(in != null) {
                in.close();
            }
        }

        return response.toString();
    }

    /* ladda ner ( via HTTP ) textfilen som finns på den medföljande URL , och returnera dess innehåll .
     * I första hand avsedd för nedladdning webbsidor .
     * @param siteUrl URL:en i textfilen för att ladda ner
     * @return innehållet i den angivna textfilen*/
    public String download(String siteUrl) throws IOException {
        URL url = new URL(siteUrl);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        //HttpsURLConnection con = NetCipher.getHttpsURLConnection(url);
        return dl(con);
    }
}
