/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE ANGLE_PID_TUNING PACKING
package com.MAVLink.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        
/**
* Steering angle infomation.
*/
public class msg_angle_pid_tuning extends MAVLinkMessage{

    public static final int MAVLINK_MSG_ID_ANGLE_PID_TUNING = 58;
    public static final int MAVLINK_MSG_LENGTH = 16;
    private static final long serialVersionUID = MAVLINK_MSG_ID_ANGLE_PID_TUNING;


      
    /**
    * Timestamp (microseconds since UNIX epoch or microseconds since system boot)
    */
    public long time_usec;
      
    /**
    * expected wheel angle
    */
    public float expected_angle;
      
    /**
    * achieved wheel angle
    */
    public float achieved_angle;
    

    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_ANGLE_PID_TUNING;
              
        packet.payload.putUnsignedLong(time_usec);
              
        packet.payload.putFloat(expected_angle);
              
        packet.payload.putFloat(achieved_angle);
        
        return packet;
    }

    /**
    * Decode a angle_pid_tuning message into this class fields
    *
    * @param payload The message to decode
    */
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
              
        this.time_usec = payload.getUnsignedLong();
              
        this.expected_angle = payload.getFloat();
              
        this.achieved_angle = payload.getFloat();
        
    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_angle_pid_tuning(){
        msgid = MAVLINK_MSG_ID_ANGLE_PID_TUNING;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_angle_pid_tuning(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_ANGLE_PID_TUNING;
        unpack(mavLinkPacket.payload);        
    }

          
    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_ANGLE_PID_TUNING -"+" time_usec:"+time_usec+" expected_angle:"+expected_angle+" achieved_angle:"+achieved_angle+"";
    }
}
        