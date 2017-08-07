package org.avajadi.cloudmaker.app;

import org.avajadi.cloudmaker.ui.RoomPanel;
import org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.util.RoomScanner;
import org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.building.Room;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ScanRoom extends JPanel {

    public ScanRoom(String roomName) throws InterruptedException, FileNotFoundException {
        JFrame frame = new JFrame();
        Room room = new Room( roomName );

        RoomPanel roomPanel = new RoomPanel(room, RoomPanel.SCALE_CM);
        frame.getContentPane().add(roomPanel);

        RoomScanner scanner = new RoomScanner();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
        scanner.scan(room);
        room.save( new File( "/tmp/" + room.getName() + ".room" ) );
        roomPanel.save( new File("/tmp/" + room.getName() + ".png") );
    }

    public static void main(String[] args) {
        try {
            new ScanRoom( args[0] );
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
        }
    }
}
