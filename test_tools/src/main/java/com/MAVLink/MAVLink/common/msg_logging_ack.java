/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE LOGGING_ACK PACKING
package com.MAVLink.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        
/**
* An ack for a LOGGING_DATA_ACKED message
*/
public class msg_logging_ack extends MAVLinkMessage{

    public static final int MAVLINK_MSG_ID_LOGGING_ACK = 268;
    public static final int MAVLINK_MSG_LENGTH = 4;
    private static final long serialVersionUID = MAVLINK_MSG_ID_LOGGING_ACK;


      
    /**
    * sequence number (must match the one in LOGGING_DATA_ACKED)
    */
    public int sequence;
      
    /**
    * system ID of the target
    */
    public short target_system;
      
    /**
    * component ID of the target
    */
    public short target_component;
    

    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_LOGGING_ACK;
              
        packet.payload.putUnsignedShort(sequence);
              
        packet.payload.putUnsignedByte(target_system);
              
        packet.payload.putUnsignedByte(target_component);
        
        return packet;
    }

    /**
    * Decode a logging_ack message into this class fields
    *
    * @param payload The message to decode
    */
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
              
        this.sequence = payload.getUnsignedShort();
              
        this.target_system = payload.getUnsignedByte();
              
        this.target_component = payload.getUnsignedByte();
        
    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_logging_ack(){
        msgid = MAVLINK_MSG_ID_LOGGING_ACK;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_logging_ack(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_LOGGING_ACK;
        unpack(mavLinkPacket.payload);        
    }

          
    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_LOGGING_ACK -"+" sequence:"+sequence+" target_system:"+target_system+" target_component:"+target_component+"";
    }
}
        