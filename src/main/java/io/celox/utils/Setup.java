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

import java.util.prefs.Preferences;

/**
 * @author Martin Pfeffer
 * <a href="mailto:martin.pfeffer@celox.io">martin.pfeffer@celox.io</a>
 * @see <a href="https://celox.io">https://celox.io</a>
 */
public class Setup {

    public static void setLogFile(String logFile) {
        Preferences.userNodeForPackage(Setup.class).put("logfile", logFile);
    }

    public static String getLogFile() {
        return Preferences.userNodeForPackage(Setup.class).get("logfile", "");
    }

    public static void setDeviceIp(String deviceIp) {
        Preferences.userNodeForPackage(Setup.class).put("device_ip", deviceIp);
    }

    public static String getDeviceIp() {
        return Preferences.userNodeForPackage(Setup.class).get("device_ip", "");
    }

    public static void setUsername(String username) {
        Preferences.userNodeForPackage(Setup.class).put("username", username);
    }

    public static String getUsername() {
        return Preferences.userNodeForPackage(Setup.class).get("username", "");
    }

    public static boolean hasValidUsername() {
        return !Preferences.userNodeForPackage(Setup.class).get("username", "").isEmpty();
    }
}
