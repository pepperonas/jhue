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

package io.celox.model;

/**
 * @author Martin Pfeffer
 * <a href="mailto:martin.pfeffer@celox.io">martin.pfeffer@celox.io</a>
 * @see <a href="https://celox.io">https://celox.io</a>
 */
public class HueXyColor {

    @SuppressWarnings("unused")
    private static final String TAG = "HueXyColor";

    private double x;
    private double y;

    HueXyColor(String xyData) {
        String xy[] = xyData.replace("[", "").replace("]", "").replace(" ", "").split(",");
        this.x = Double.parseDouble(xy[0]);
        this.y = Double.parseDouble(xy[1]);
    }

    public HueXyColor(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "HueXyColor{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
