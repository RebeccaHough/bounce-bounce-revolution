package uk.ac.reading.rt016631.game;

import android.os.AsyncTask;
import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HighScoresClient {

    private static String host = "100.76.51.41";

    /**
     * Send high score to server
     * @param name the high scoring player name
     * @param score the player's score
     */
    public static boolean sendURLSerializable(String name, int score) {

        String fileName = "highScores.json";
        String protocol = "http";
        int port = 8080;

        String highScore = ("?" + name + "&" + Integer.toString(score));

        boolean success = false;
        sendJSONToServer sendToServer = new sendJSONToServer();
        try {
            success = sendToServer.execute(protocol, host, port, fileName, highScore).get();

            System.out.println("Sent HighScores: " + success);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    private static class sendJSONToServer extends AsyncTask <Object, Void, Boolean> {

        /**
         * Send a JSON object to the high scores server
         * @param params object containing URL parameters
         */
        @Override
        protected Boolean doInBackground(Object[] params) {
            //params are: protocol, host, port, fileName, highScore
            OutputStream outputStream;
            String highScoreAsJSON = (String) params[4];

            String fileAndParameters = params[3] + highScoreAsJSON;
            System.out.println(fileAndParameters);

            URL url = null;
            try {
                url = new URL((String) params[0], (String) params[1], (int) params[2], fileAndParameters);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(2000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setFixedLengthStreamingMode(highScoreAsJSON.length());

                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); //;charset=utf-8

                // opens output stream from the HTTP connection
                outputStream = urlConnection.getOutputStream();
                System.out.println("----- Sending JSON Object -----");

                BufferedWriter br = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                System.out.println("JSON String: " + highScoreAsJSON);
                System.out.println("JSON String Length: " + highScoreAsJSON.length());

                // SEND DATA to client
                System.out.println("Content-Length: " + highScoreAsJSON.length());
                br.write(highScoreAsJSON);
                br.write("");
                br.flush();

                System.out.println("SENT data/file");
                br.close();
                outputStream.close();
                return true;

            } catch (ProtocolException e) {
                System.out.println("Protocol Exception");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("IO Exception");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("NullPointerException Exception");
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            //process message
        }
    }

    /**
     *  Read in object from URL
     *  @param classType the type of the object to be read
     *  @param fileName the file name
     *  @return an object whose type corresponds to classType
     */
    public static Object readURLSerializable(Type classType, String fileName) {
        System.out.println("GETTING FILE: " + fileName);
        String protocol = "http";
        int port = 8080;

        Object jsonObject = null;

        getObjectFromServer returnedObject = new getObjectFromServer();
        try {
            jsonObject = returnedObject.execute(protocol, host, port, fileName, classType).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private static class getObjectFromServer extends AsyncTask <Object, Void, Object> {

        /**
         * Get the JSON object from the server
         * @param params array of Objects detailing URL parameters
         * @return a JSON object
         */
        @Override
        protected Object doInBackground(Object[] params) {
            InputStream inputStream = null;
            Object jsonObj = null;

            URL url = null;
            try {
                url = new URL((String) params[0], (String) params[1], (int) params[2], (String) params[3]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Connection", "close");
                int responseCode = urlConnection.getResponseCode();
                System.out.println("Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {    // check response code
                    String fileName = (String) params[3];
                    String disposition = urlConnection.getHeaderField("Content-Disposition");
                    String contentType = urlConnection.getContentType();
                    int contentLength = urlConnection.getContentLength();

                    System.out.println("Content-Type = " + contentType);
                    System.out.println("Content-Disposition = " + disposition);
                    System.out.println("Content-Length = " + contentLength);
                    System.out.println("File-Name = " + fileName);

                    // opens input stream from the HTTP connection
                    inputStream = urlConnection.getInputStream();

                    //Reader reader = new InputStreamReader(inputStream);
                    System.out.println("----- Getting JSON Object -----");

                    StringBuilder sb = new StringBuilder(contentLength);
                    Reader reader = new InputStreamReader(inputStream);

                    int ch = 0;
                    while ((ch = reader.read()) != -1) {	// read returns one character as an integer
                        sb.append((char) ch);	// cast to char
                    }

                    String jsonString = sb.toString();
                    System.out.println("JSON String: " + jsonString);
                    System.out.println("JSON String Length: " + jsonString.length());

                    if (contentLength == jsonString.length()) {
                        jsonObj = new Gson().fromJson(jsonString, ((Type) params[4]));
                    }
                    else
                    {
                        System.out.println("Not all data received!");
                        throw new EOFException();
                    }
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return jsonObj;
        }

        @Override
        protected void onPostExecute(Object content) {
            //process message
        }
    }
}
