/*
 * Copyright (c) 2018 Martin Pfeffer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.celox.utils;

import com.pepperonas.jbasx.log.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.pepperonas.jbasx.Jbasx.TAG;

/**
 * @author Martin Pfeffer
 * <a href="mailto:martin.pfeffer@celox.io">martin.pfeffer@celox.io</a>
 * @see <a href="https://celox.io">https://celox.io</a>
 */
public class HttpUtils {

    public static String executePost(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;

        try {
            // create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            // send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            // get response
            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            reader.close();

            if (Const.LOG_HTTP_REQUESTS) {
                try {
                    if (!response.toString().isEmpty()) {
                        JSONArray jsonArray = new JSONArray(response.toString());
                        Log.w(TAG, "executePost: " + targetURL + "?" + urlParameters + "\n" + jsonArray.toString(2));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String executeGet(String targetURL) {
        HttpURLConnection connection = null;

        try {
            // create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            if (Const.LOG_HTTP_REQUESTS) {
                try {
                    if (!response.toString().isEmpty()) {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        Log.w(TAG, "executeGet: " + targetURL + "\n" + jsonObject.toString(2));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String executePut(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;

        try {
            // create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(urlParameters);
            out.close();
            connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append(" ");
            }

            if (Const.LOG_HTTP_REQUESTS) {
                try {
                    if (!response.toString().isEmpty()) {
                        JSONObject jsonArray = new JSONObject(response);
                        Log.w(TAG, "executePut: " + targetURL + "\n" + jsonArray.toString(2));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}
