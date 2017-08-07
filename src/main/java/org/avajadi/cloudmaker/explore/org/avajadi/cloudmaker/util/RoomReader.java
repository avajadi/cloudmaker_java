package org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.util;

import org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.building.Point;
import org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.building.Room;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;

/**
 * RoomReader reads a room from a room file using jaxb
 */
public class RoomReader {

    public static Room readRoom(String roomFileName ) throws IOException, JAXBException {
        return readRoom( new File(roomFileName) );
    }

    public static Room readRoom(File roomFile) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(generated.Room.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        generated.Room xmlRoom = (generated.Room) jaxbUnmarshaller.unmarshal( roomFile );
        Room room = new Room( xmlRoom.getName() );
        for( generated.Room.Points.Point xmlPoint : xmlRoom.getPoints().getPoint() ) {
            Point point = new Point( xmlPoint.getAngle().getValue(), xmlPoint.getDistance().getValue() );
            room.addPoint( point );
        }
        return room;
    }
}
