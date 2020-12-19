/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE GPS_RAW_INT_EX PACKING
package com.MAVLink.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        
/**
* The global position, as returned by the Global Positioning System (GPS). This is
                NOT the global position estimate of the system, but rather a RAW sensor value. See message GLOBAL_POSITION for the global position estimate. Coordinate frame is right-handed, Z-axis up (GPS frame).
*/
public class msg_gps_raw_int_ex extends MAVLinkMessage{

    public static final int MAVLINK_MSG_ID_GPS_RAW_INT_EX = 71;
    public static final int MAVLINK_MSG_LENGTH = 34;
    private static final long serialVersionUID = MAVLINK_MSG_ID_GPS_RAW_INT_EX;


      
    /**
    * Timestamp (microseconds since UNIX epoch or microseconds since system boot)
    */
    public long time_usec;
      
    /**
    * Latitude (WGS84), in degrees * 1E7
    */
    public int lat;
      
    /**
    * Longitude (WGS84), in degrees * 1E7
    */
    public int lon;
      
    /**
    * Altitude (AMSL, NOT WGS84), in meters * 1000 (positive for up). Note that virtually all GPS modules provide the AMSL altitude in addition to the WGS84 altitude.
    */
    public int alt;
      
    /**
    * Roll angle (degree, -180...180)
    */
    public float roll;
      
    /**
    * GPS HDOP horizontal dilution of position (unitless). If unknown, set to: UINT16_MAX
    */
    public int eph;
      
    /**
    * GPS VDOP vertical dilution of position (unitless). If unknown, set to: UINT16_MAX
    */
    public int epv;
      
    /**
    * GPS ground speed (m/s * 100). If unknown, set to: UINT16_MAX
    */
    public int vel;
      
    /**
    * Course over ground (NOT heading, but direction of movement) in degrees * 100, 0.0..359.99 degrees. If unknown, set to: UINT16_MAX
    */
    public int cog;
      
    /**
    * See the GPS_FIX_TYPE enum.
    */
    public short fix_type;
      
    /**
    * Number of satellites visible. If unknown, set to 255
    */
    public short satellites_visible;
    

    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_GPS_RAW_INT_EX;
              
        packet.payload.putUnsignedLong(time_usec);
              
        packet.payload.putInt(lat);
              
        packet.payload.putInt(lon);
              
        packet.payload.putInt(alt);
              
        packet.payload.putFloat(roll);
              
        packet.payload.putUnsignedShort(eph);
              
        packet.payload.putUnsignedShort(epv);
              
        packet.payload.putUnsignedShort(vel);
              
        packet.payload.putUnsignedShort(cog);
              
        packet.payload.putUnsignedByte(fix_type);
              
        packet.payload.putUnsignedByte(satellites_visible);
        
        return packet;
    }

    /**
    * Decode a gps_raw_int_ex message into this class fields
    *
    * @param payload The message to decode
    */
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
              
        this.time_usec = payload.getUnsignedLong();
              
        this.lat = payload.getInt();
              
        this.lon = payload.getInt();
              
        this.alt = payload.getInt();
              
        this.roll = payload.getFloat();
              
        this.eph = payload.getUnsignedShort();
              
        this.epv = payload.getUnsignedShort();
              
        this.vel = payload.getUnsignedShort();
              
        this.cog = payload.getUnsignedShort();
              
        this.fix_type = payload.getUnsignedByte();
              
        this.satellites_visible = payload.getUnsignedByte();
        
    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_gps_raw_int_ex(){
        msgid = MAVLINK_MSG_ID_GPS_RAW_INT_EX;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_gps_raw_int_ex(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_GPS_RAW_INT_EX;
        unpack(mavLinkPacket.payload);        
    }

                          
    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_GPS_RAW_INT_EX -"+" time_usec:"+time_usec+" lat:"+lat+" lon:"+lon+" alt:"+alt+" roll:"+roll+" eph:"+eph+" epv:"+epv+" vel:"+vel+" cog:"+cog+" fix_type:"+fix_type+" satellites_visible:"+satellites_visible+"";
    }
}
        