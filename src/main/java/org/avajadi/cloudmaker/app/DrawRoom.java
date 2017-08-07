package org.avajadi.cloudmaker.app;

import org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.building.Point;
import org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.building.Room;
import org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.util.RoomReader;
import org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.util.RoomScanner;
import org.avajadi.cloudmaker.ui.RoomPanel;

import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DrawRoom extends JPanel {

    public DrawRoom(String roomFileName) throws InterruptedException, IOException, JAXBException {
        JFrame frame = new JFrame();
        System.out.println( "Reading room from " + roomFileName );
        Room room = RoomReader.readRoom(roomFileName);
        System.out.println( "Found room " + room.getName() );
        System.out.println( room.getLength() + " X " + room.getWidth() );
        RoomPanel roomPanel = new RoomPanel(room, RoomPanel.SCALE_CM);
        frame.getContentPane().add(roomPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,1000);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            new DrawRoom( args[0] );
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
