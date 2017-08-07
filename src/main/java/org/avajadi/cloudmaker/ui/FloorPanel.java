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

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eddie on 2017-08-03.
 */
public class FloorPanel extends JPanel implements ChangeListener {
    private final List<FloorRoom> rooms = new ArrayList<>();
    private final List<RoomChangeListener> roomChangeListeners = new ArrayList<>();
    private double scale = 0.5;

    public FloorPanel() {
        super();
        setOpaque(true);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("Rooms"));
        setPreferredSize(new Dimension(1000, 1000));
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
        paintAll( getGraphics() );
    }

    public void addRoom(Room room, Color roomColor) {
        rooms.add(new FloorRoom(room, roomColor));
        System.out.println(room.toString());
        roomChangeListeners.forEach(RoomChangeListener::roomsChanged);
        paintComponent(getGraphics());
    }

    /**
     * Lazy way of handling the room collection
     *
     * @return
     */
    public List<FloorRoom> getRooms() {
        return rooms;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        paintAll(getGraphics());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.translate(10, 10);
        for (FloorRoom floorRoom : rooms) {
            if (floorRoom.visible) {
                g.setColor(floorRoom.color);
                for (Point point : floorRoom.room) {
                    Point.Cartesian cart = point.getCartesian().offset(floorRoom.room.getOffset()).offset(floorRoom.offset).scale(scale);
                    g.fillOval((int) cart.x, (int) cart.y, 3, 3);
                }
            }
        }
    }

    public void addChangeListener(RoomChangeListener roomChangeListener) {
        roomChangeListeners.add(roomChangeListener);
    }

    public class FloorRoom {
        public Room room;
        public Color color;
        public Point.Cartesian offset;
        public boolean visible;

        public FloorRoom(Room room, Color color) {
            this.room = room;
            this.color = color;
            offset = new Point.Cartesian(0, 0);
            visible = true;
        }
    }
}
