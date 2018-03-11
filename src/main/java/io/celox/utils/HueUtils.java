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

import java.util.HashMap;

import javafx.scene.paint.Color;

/**
 * @author Martin Pfeffer
 * <a href="mailto:martin.pfeffer@celox.io">martin.pfeffer@celox.io</a>
 * @see <a href="https://celox.io">https://celox.io</a>
 */
public class HueUtils {

    public static HashMap<String, Double> rgbToXy(Color c) {
        // for the hue bulb the corners of the triangle are:
        // -red: 0.675, 0.322
        // -green: 0.4091, 0.518
        // -blue: 0.167, 0.04
        double[] normalizedToOne = new double[3];
        double cRed, cGreen, cBlue;
        cRed = c.getRed();
        cGreen = c.getGreen();
        cBlue = c.getBlue();
        normalizedToOne[0] = (cRed / 255);
        normalizedToOne[1] = (cGreen / 255);
        normalizedToOne[2] = (cBlue / 255);
        float red, green, blue;

        // make red more vivid
        if (normalizedToOne[0] > 0.04045) {
            red = (float) Math.pow((normalizedToOne[0] + 0.055) / (1f + 0.055), 2.4);
        } else {
            red = (float) (normalizedToOne[0] / 12.92);
        }

        // make green more vivid
        if (normalizedToOne[1] > 0.04045) {
            green = (float) Math.pow((normalizedToOne[1] + 0.055) / (1f + 0.055), 2.4);
        } else {
            green = (float) (normalizedToOne[1] / 12.92);
        }

        // make blue more vivid
        if (normalizedToOne[2] > 0.04045) {
            blue = (float) Math.pow((normalizedToOne[2] + 0.055) / (1f + 0.055), 2.4);
        } else {
            blue = (float) (normalizedToOne[2] / 12.92);
        }

        float _x = (float) (red * 0.649926 + green * 0.103455 + blue * 0.197109);
        float _y = (float) (red * 0.234327 + green * 0.743075 + blue * 0.022598);
        float _z = (float) (red * 0.0000000 + green * 0.053077 + blue * 1.035763);

        float x = _x / (_x + _y + _z);
        float y = _y / (_x + _y + _z);

        double[] xy = new double[2];
        xy[0] = x;
        xy[1] = y;

        HashMap<String, Double> map = new HashMap<>();
        map.put("x", xy[0]);
        map.put("y", xy[1]);
        return map;
    }

    public static String formatXyToString(HashMap<String, Double> colorMap) {
        return "[" + colorMap.get("x") + "," + colorMap.get("y") + "]";
    }

}
