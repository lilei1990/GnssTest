/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE LOG_EKF3 PACKING
package com.MAVLink.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        
/**
* ekf1
*/
public class msg_log_ekf3 extends MAVLinkMessage{

    public static final int MAVLINK_MSG_ID_LOG_EKF3 = 162;
    public static final int MAVLINK_MSG_LENGTH = 18;
    private static final long serialVersionUID = MAVLINK_MSG_ID_LOG_EKF3;


      
    /**
    * a
    */
    public long time_us;
      
    /**
    * a
    */
    public short innovVN;
      
    /**
    * a
    */
    public short innovVE;
      
    /**
    * a
    */
    public short innovPN;
      
    /**
    * a
    */
    public short innovPE;
      
    /**
    * a
    */
    public short innovYaw;
    

    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_LOG_EKF3;
              
        packet.payload.putUnsignedLong(time_us);
              
        packet.payload.putShort(innovVN);
              
        packet.payload.putShort(innovVE);
              
        packet.payload.putShort(innovPN);
              
        packet.payload.putShort(innovPE);
              
        packet.payload.putShort(innovYaw);
        
        return packet;
    }

    /**
    * Decode a log_ekf3 message into this class fields
    *
    * @param payload The message to decode
    */
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
              
        this.time_us = payload.getUnsignedLong();
              
        this.innovVN = payload.getShort();
              
        this.innovVE = payload.getShort();
              
        this.innovPN = payload.getShort();
              
        this.innovPE = payload.getShort();
              
        this.innovYaw = payload.getShort();
        
    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_log_ekf3(){
        msgid = MAVLINK_MSG_ID_LOG_EKF3;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_log_ekf3(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_LOG_EKF3;
        unpack(mavLinkPacket.payload);        
    }

                
    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_LOG_EKF3 -"+" time_us:"+time_us+" innovVN:"+innovVN+" innovVE:"+innovVE+" innovPN:"+innovPN+" innovPE:"+innovPE+" innovYaw:"+innovYaw+"";
    }
}
        