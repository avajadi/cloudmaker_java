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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by eddie on 2017-08-03.
 */
public class RoomControlPanel extends JPanel implements RoomChangeListener {

    private final FloorPanel floorPanel;

    public RoomControlPanel(FloorPanel floorPanel ) {
        this.floorPanel = floorPanel;
        setLayout( new BoxLayout(this, BoxLayout.Y_AXIS) );
        setBorder(BorderFactory.createTitledBorder("Room controls"));
        setPreferredSize(new Dimension(100,500));
        floorPanel.addChangeListener( this );
        setVisible( true );
    }


    @Override
    public void roomsChanged() {
        this.removeAll();
        for(FloorPanel.FloorRoom room : floorPanel.getRooms() ) {
            JCheckBox roomControl = new JCheckBox( room.room.getName() );
            roomControl.setSelected( room.visible );
            roomControl.setForeground( room.color );
            roomControl.addActionListener( (ActionEvent event ) -> {
                        room.visible = roomControl.isSelected();
                        floorPanel.repaint();
                    }
            );
            roomControl.setVisible(true);
            this.add(roomControl);
        }
        paint(getGraphics());
    }

}
