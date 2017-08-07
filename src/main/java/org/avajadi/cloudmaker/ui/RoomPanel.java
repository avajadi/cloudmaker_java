/*
 * Copyright 2017 Eddie Olsson
 * This file is part of the cloudmaker toolset
 *
 * Cloudmaker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.avajadi.cloudmaker.ui;

import org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.building.Point;
import org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.building.Room;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class RoomPanel extends JPanel implements ChangeListener {
    public final static double SCALE_MM = 10;
    public final static double SCALE_CM = 1;
    private final Room room;
    private double scale;
    private Color roomColor = Color.BLACK;

    /**
     * Create a RoomPanel with the default scale 1mm/pixel
     *
     * @param room Room this panel displays
     */
    public RoomPanel(Room room) {
        this(room, SCALE_MM);
    }

    /**
     * Create a RoomPanel with a specific scale.
     *
     * @param room  Room this panel displays
     * @param scale 1 means 1 cm/pixel 10 means 1 mm/pixel. See SCALE_MM nad SCALE_CM
     */
    public RoomPanel(Room room, double scale) {
        super();
        this.scale = scale;
        setOpaque(true);
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setPreferredSize(new Dimension(1000, 1000));
        this.room = room;
        room.addChangeListeners(this);
    }

    public Color getRoomColor() {
        return roomColor;
    }

    public void setRoomColor(Color roomColor) {
        this.roomColor = roomColor;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
        paintComponent(getGraphics());
    }

    public Room getRoom() {
        return room;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.translate(10, 10);
        g.setColor(roomColor);
        for (Point point : room) {
            Point.Cartesian cart = point.getCartesian().offset(room.getOffset()).scale(scale);
            g.fillOval((int) cart.x, (int) cart.y, 5, 5);
        }
    }

    public void save(File saveFile) {
        BufferedImage bImg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D cg = bImg.createGraphics();
        paint(cg);
        try {
            if (ImageIO.write(bImg, "png", saveFile)) {
                System.out.println("-- saved");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        setSize((int) (room.getWidth() * scale + 20), (int) (room.getLength() * scale + 20));
        paintAll(getGraphics());
    }
}
