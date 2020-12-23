/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE FILE_TRANSFER_PROTOCOL_REQ PACKING
package com.MAVLink.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        
/**
* File transfer message req
*/
public class msg_file_transfer_protocol_req extends MAVLinkMessage{

    public static final int MAVLINK_MSG_ID_FILE_TRANSFER_PROTOCOL_REQ = 111;
    public static final int MAVLINK_MSG_LENGTH = 12;
    private static final long serialVersionUID = MAVLINK_MSG_ID_FILE_TRANSFER_PROTOCOL_REQ;


      
    /**
    * Offsets for List and Read commands
    */
    public long offset;
      
    /**
    * Network ID (0 for broadcast)
    */
    public short target_network;
      
    /**
    * Component ID (0 for broadcast)
    */
    public short target_component;
      
    /**
    * Command opcode
    */
    public short opcode;
      
    /**
    * Size of data
    */
    public short size;
      
    /**
    * Variable length payload. The length is defined by the remaining message length when subtracting the header and other fields.  The entire content of this block is opaque unless you understand any the encoding message_type.  The particular encoding used can be extension specific and might not always be documented as part of the mavlink specification.
    */
    public short payload[] = new short[4];
    

    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_FILE_TRANSFER_PROTOCOL_REQ;
              
        packet.payload.putUnsignedInt(offset);
              
        packet.payload.putUnsignedByte(target_network);
              
        packet.payload.putUnsignedByte(target_component);
              
        packet.payload.putUnsignedByte(opcode);
              
        packet.payload.putUnsignedByte(size);
              
        
        for (int i = 0; i < payload.length; i++) {
            packet.payload.putUnsignedByte(payload[i]);
        }
                    
        
        return packet;
    }

    /**
    * Decode a file_transfer_protocol_req message into this class fields
    *
    * @param payload The message to decode
    */
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
              
        this.offset = payload.getUnsignedInt();
              
        this.target_network = payload.getUnsignedByte();
              
        this.target_component = payload.getUnsignedByte();
              
        this.opcode = payload.getUnsignedByte();
              
        this.size = payload.getUnsignedByte();
              
         
        for (int i = 0; i < this.payload.length; i++) {
            this.payload[i] = payload.getUnsignedByte();
        }
                
        
    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_file_transfer_protocol_req(){
        msgid = MAVLINK_MSG_ID_FILE_TRANSFER_PROTOCOL_REQ;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_file_transfer_protocol_req(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_FILE_TRANSFER_PROTOCOL_REQ;
        unpack(mavLinkPacket.payload);        
    }

                
    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_FILE_TRANSFER_PROTOCOL_REQ -"+" offset:"+offset+" target_network:"+target_network+" target_component:"+target_component+" opcode:"+opcode+" size:"+size+" payload:"+payload+"";
    }
}
        