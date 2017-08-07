package org.avajadi.cloudmaker.app;

import org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.building.Room;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

import static org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.util.RoomReader.readRoom;

/**
 * Created by eddie on 2017-08-02.
 */
public class RectifyRoomFile {

    public static void main( String[] argv ) {
        String inFile = argv[0];
        String outFile = argv[1];
        Room room;
        try {
            room = readRoom( inFile );
            room.save( new File( outFile ) );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}
