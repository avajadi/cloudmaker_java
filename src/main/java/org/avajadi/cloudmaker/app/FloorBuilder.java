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

package org.avajadi.cloudmaker.app;

import org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.building.Room;
import org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.util.RoomReader;
import org.avajadi.cloudmaker.ui.FloorPanel;
import org.avajadi.cloudmaker.ui.RoomControlPanel;
import org.avajadi.cloudmaker.ui.RoomPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by eddie on 2017-08-03.
 */
public class FloorBuilder extends JFrame {
    private JFileChooser fc;
    private FloorPanel allRoomsPanel;
    private RoomControlPanel roomControlPanel;
    private JMenuBar menuBar;
    private java.util.List<Color> roomColors = new ArrayList<>();

    public FloorBuilder() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        roomColors.add( Color.black );
        roomColors.add( Color.blue );
        roomColors.add( Color.green );

        initUI();
    }

    public static void main(String[] argv) {
        EventQueue.invokeLater(() -> {
            FloorBuilder floorBuilder = null;
            try {
                floorBuilder = new FloorBuilder();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            floorBuilder.setVisible(true);
        });
    }

    private void initUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("ROOM FILES", "room", "png");
        fc.setFileFilter( filter );
        Container pane = getContentPane();

        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent( makeMenuBar() )
                        .addComponent(createRoomPanel()))
                .addComponent( createRoomControlPanel() )

        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addComponent( makeMenuBar() )
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(createRoomPanel())
                        .addComponent(createRoomControlPanel())
                )
        );


        pack();

        setTitle("FloorBuilder");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private JMenuBar makeMenuBar() {
        if( menuBar == null ) {
            menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu("File" );
            JMenuItem openItem = new JMenuItem( "Open" );
            openItem.addActionListener((ActionEvent event) -> {
                int returnVal = fc.showOpenDialog(FloorBuilder.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        Room room = RoomReader.readRoom(file);
                        allRoomsPanel.addRoom(room, roomColors.remove(0));
                    } catch (JAXBException e) {
                        e.printStackTrace();
                    }
                }
            });

            JMenuItem quitItem = new JMenuItem( "Quit" );
            quitItem.addActionListener( (ActionEvent event ) -> {
                System.exit( 0 );
            });
            fileMenu.add(openItem);
            fileMenu.add( quitItem );
            menuBar.add( fileMenu );

            JMenu viewMenu = new JMenu( "View" );
            JMenuItem showRoomControlItem = new JMenuItem( "Room Control" );
            showRoomControlItem.addActionListener( (ActionEvent event) -> {
                System.out.println( "Show or hide room controls" );
            } );
        }
        return menuBar;
    }

    private FloorPanel createRoomPanel() {
        if (allRoomsPanel == null) {
            allRoomsPanel = new FloorPanel();
        }
        return allRoomsPanel;
    }

    private RoomControlPanel createRoomControlPanel() {
        if( roomControlPanel == null ) {
            FloorPanel fp = createRoomPanel();
            roomControlPanel = new RoomControlPanel(fp);
        }
        return roomControlPanel;
    }

}
