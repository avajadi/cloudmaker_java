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
import org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.util.RoomScanner;
import org.avajadi.cloudmaker.ui.RoomPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author Eddie Olsson &lt;eddie.olsson@gmail.com&gt;
 */
public class ScannerApp extends JFrame{
    private Room room = new Room("empty room");
    private final RoomScanner scanner;
    private RoomPanel roomPanel;

    public ScannerApp() {
        initUI();
        scanner = new RoomScanner();
    }

    private void initUI() {

        ImageIcon webIcon = new ImageIcon("cloud_icon.png");
        setIconImage(webIcon.getImage());

        JButton quitButton = makeQuitButton();
        JButton scanButton = makeScanButton();
        JButton saveButton = makeSaveButton();
        JTextField roomNameField = makeRoomNameField();
        roomPanel = makeRoomPanel();
        Container pane = getContentPane();


        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup( GroupLayout.Alignment.LEADING )
                        .addComponent(quitButton)
                        .addComponent(roomPanel))
                .addComponent(scanButton)
                .addComponent(roomNameField)
                .addComponent(saveButton)
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(quitButton)
                .addComponent(scanButton)
                .addComponent(roomNameField)
                .addComponent(saveButton))
                .addComponent(roomPanel)
        );

        pack();

        setTitle("Cloudmaker");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private JTextField makeRoomNameField() {
        JTextField roomNameField = new JTextField(20);
        roomNameField.addActionListener((ActionEvent event) ->{ room.setName(roomNameField.getText() );});
        room.addChangeListeners((ChangeEvent event) -> { roomNameField.setText( room.getName() );});
        return roomNameField;
    }

    private JButton makeSaveButton() {
        JButton scanButton = new JButton( "Save room" );
        scanButton.setToolTipText( "Save room file and preview" );
        scanButton.addActionListener((ActionEvent event) -> { saveRoom(); } );
        return scanButton;
    }

    private void saveRoom() {
        try {
            room.save( new File( "/tmp/" + room.getName() + ".room" ) );
            roomPanel.save( new File( "/tmp/" + room.getName() + ".png" ) );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private RoomPanel makeRoomPanel() {
        RoomPanel roomPanel = new RoomPanel( room, RoomPanel.SCALE_CM );
        return roomPanel;
    }

    private JButton makeScanButton() {
        JButton scanButton = new JButton( "Scan" );
        scanButton.setMnemonic(KeyEvent.VK_S);
        scanButton.setToolTipText( "Make a new scan" );
        scanButton.addActionListener((ActionEvent event) -> { runScan(); } );
        return scanButton;
    }

    private void runScan() {
        room.clear();
        room.setName( "new room" );
        scanner.scan( room );
    }

    private JButton makeQuitButton() {
        JButton quitButton = new JButton("Quit");
        quitButton.setMnemonic(KeyEvent.VK_F4);
        quitButton.setToolTipText( "Leave Cloudmaker? You're kidding, right?" );

        quitButton.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });

        return quitButton;
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            ScannerApp scannerApp = new ScannerApp();
            scannerApp.setVisible(true);
        });
    }
}
