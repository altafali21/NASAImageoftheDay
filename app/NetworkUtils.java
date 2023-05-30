import java.net.URL;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import org.json.JSONException;

public class NetworkUtils {

    public static URL buildUrl(String selectedDate) {
        String formattedDate = selectedDate.replaceAll("-", "/");
        String apiKey = "ghzQ0ZcVMdDWjkgYKCEyqAuGob2itzv3K2HoJC1M";
        String urlString = "https://api.nasa.gov/planetary/apod?api_key=" + apiKey + "&date=" + formattedDate;
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000); // 10 seconds
        InputStream inputStream = urlConnection.getInputStream();
        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter("\\A"); // Read the entire response
        String response = scanner.hasNext() ? scanner.next() : "";
        scanner.close();
        inputStream.close();
        urlConnection.disconnect();
        return response;
    }

    public static String parseImageUrlFromJson(String jsonResponse) {
        String imageUrl = null;
        try {
            // Parse the JSON response to retrieve the necessary information
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imageUrl;
    }
}
