/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE ANGLE_SENSOR_RAW PACKING
package com.MAVLink.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        
/**
* angle sensor RAW value
*/
public class msg_angle_sensor_raw extends MAVLinkMessage{

    public static final int MAVLINK_MSG_ID_ANGLE_SENSOR_RAW = 235;
    public static final int MAVLINK_MSG_LENGTH = 4;
    private static final long serialVersionUID = MAVLINK_MSG_ID_ANGLE_SENSOR_RAW;


      
    /**
    * steering angle
    */
    public float angle;
    

    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_ANGLE_SENSOR_RAW;
              
        packet.payload.putFloat(angle);
        
        return packet;
    }

    /**
    * Decode a angle_sensor_raw message into this class fields
    *
    * @param payload The message to decode
    */
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
              
        this.angle = payload.getFloat();
        
    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_angle_sensor_raw(){
        msgid = MAVLINK_MSG_ID_ANGLE_SENSOR_RAW;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_angle_sensor_raw(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_ANGLE_SENSOR_RAW;
        unpack(mavLinkPacket.payload);        
    }

      
    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_ANGLE_SENSOR_RAW -"+" angle:"+angle+"";
    }
}
        