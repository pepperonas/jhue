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

import com.pepperonas.jbasx.Jbasx;
import com.pepperonas.jbasx.log.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javafx.scene.Node;

/**
 * @author Martin Pfeffer <a href="mailto:martin.pfeffer@celox.io">martin.pfeffer@celox.io</a>
 */
public class Utils {

    @SuppressWarnings("unused")
    private static final String TAG = "Utils";

    public static void initLogger(String appName) {
        String home = System.getProperty("user.home");
        Date logDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");

        String logFilePath = home + File.separator + "jlog" + File.separator;
        String logFileName = appName + "-" + sdf.format(logDate) + ".log";
        Jbasx.setLogWriter(logFilePath, logFileName, true);
        Setup.setLogFile(logFilePath + logFileName);

        Thread.setDefaultUncaughtExceptionHandler(Utils::logError);
    }

    // method catches errors globally and prints them into the log-file you'll find in user.home/jlog
    private static void logError(@SuppressWarnings("unused") Thread thread, Throwable throwable) {
        StringWriter errorMsg = new StringWriter();
        throwable.printStackTrace(new PrintWriter(errorMsg));
        Log.e(TAG, errorMsg.toString());
    }

    public static void disableNodes(boolean disable, Node... nodes) {
        for (Node node : nodes) {
            node.setDisable(disable);
        }
    }

    public static String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return sdf.format(date);
    }

    public static int getRandomInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max) + min;
    }

    public static int getRandomInt(int min, int max, int multiply) {
        Random rand = new Random();
        return (rand.nextInt(max) + min) * multiply;
    }

    public static int getVersionFromProperties(Class clazz) {
        InputStream in = clazz.getResourceAsStream("app.properties");
        Properties props = new Properties();
        try {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = (String) props.get("version");
        return Integer.parseInt(s);
    }

    public static String getAppNameFromProperties(Class clazz) {
        InputStream in = clazz.getResourceAsStream("app.properties");
        Properties props = new Properties();
        try {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (String) props.get("app_name");
    }

}
