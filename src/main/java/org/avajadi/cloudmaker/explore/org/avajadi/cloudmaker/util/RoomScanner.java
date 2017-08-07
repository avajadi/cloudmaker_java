package org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.util;

import com.fazecast.jSerialComm.*;
import org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.building.Point;
import org.avajadi.cloudmaker.explore.org.avajadi.cloudmaker.building.Room;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fazecast.jSerialComm.SerialPort.NO_PARITY;

public class RoomScanner {

    private final OutputStream command;
    private static final Pattern DATA_PATTERN = Pattern.compile("position:([0-9]+),distance:([0-9]+)");
    private final BufferedReader data;
    private final InputStream inStream;
    private final SerialPort comPort;

    public RoomScanner(){
        // TODO: Handle missing comports gracefully
        comPort = SerialPort.getCommPorts()[0];
        comPort.openPort();
        comPort.setBaudRate(115200);
        comPort.setParity(NO_PARITY);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
        inStream = comPort.getInputStream();
        data = new BufferedReader( new InputStreamReader(inStream) );
        command = comPort.getOutputStream();

    }
    public void scan( Room room ) {

        try {
            while (true) {
                String data = "";
                while( ! data.startsWith( "Awaiting" ) ) {
                    while( inStream.available() < 10 ) {};
                    data = this.data.readLine();
                }
                System.out.println( "Asking for sweep" );
                command.write( 's' );
                while( ! data.startsWith( "#END" )) {
                    while( inStream.available() < 5 ) {};
                    data = this.data.readLine();
                    System.out.println( data );
                    Matcher m = DATA_PATTERN.matcher( data );
                    if( m.find() ) {
                        double angle = Integer.valueOf( m.group(1)) * 1.8;
                        double distance = Integer.valueOf( m.group(2) );
                        room.addPoint( new Point( angle, distance ) );
                    }
                }
                System.out.println( "Finished!" );
                System.out.println( "Room scanned:" );
                System.out.println( room.toString() );
                return;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if( comPort != null ){
            comPort.closePort();
        }
    }
}