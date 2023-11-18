package weather.http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import weather.geolocation.Geolocation;
import weather.pojo.WeatherApp;


public class Http {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static WeatherApp getClima(String cidade) {
        try {
            String charset = StandardCharsets.UTF_8.name();
            String url = "http://api.openweathermap.org/data/2.5/weather";
            String appid = "xxxxxxx"; //Cada um deve ter a sua chave
            String units = "metric";
            String lang = "en";
            Geolocation geo = getGeolocation(cidade);
            if (geo == null) {
                return null;
            }

            String query = String.format("lat=%s&lon=%s&appid=%s&units=%s&lang=%s",
                    geo.getLat(),
                    geo.getLon(),
                    appid,
                    units,
                    lang);

            URL uri = new URL(url + "?" + query);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=" + charset);
            conn.setDoOutput(true);
            WeatherApp clima;

            if (conn.getResponseCode() == 200) {
                try (InputStream response = conn.getInputStream()) {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response, charset));
                    String json = readAll(buffer);

                    Gson gson = new Gson();
                    clima = gson.fromJson(json, WeatherApp.class);
                }
                return clima;
            }

        } catch (JsonSyntaxException | IOException e) {
        }
        return null;
    }

    private static Geolocation getGeolocation(String cidade) {
        try {
            String charset = StandardCharsets.UTF_8.name();
            String url = "http://api.openweathermap.org/geo/1.0/direct";
            String appid = "xxxxxx";

            String query = String.format("q=%s&appid=%s",
                    URLEncoder.encode(cidade, charset),
                    appid);

            URL uri = new URL(url + "?" + query);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=" + charset);
            conn.setDoOutput(true);
            Geolocation geo = new Geolocation();

            if (conn.getResponseCode() == 200) {
                try (InputStream response = conn.getInputStream()) {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(response, charset));
                    String json = readAll(buffer);

                    Gson gson = new Gson();
                    Type collectionType = new TypeToken<List<Geolocation>>() {
                    }.getType();
                    List<Geolocation> result = (List<Geolocation>) new Gson()
                            .fromJson(json, collectionType);
                    if (result.size() != 0) {
                        geo.setLat(result.get(0).getLat());
                        geo.setLon(result.get(0).getLon());
                        return geo;
                    } else {
                        return null;
                    }
                }
            }
        } catch (JsonSyntaxException | IOException e) {
            return null;
        }
        return null;
    }
}