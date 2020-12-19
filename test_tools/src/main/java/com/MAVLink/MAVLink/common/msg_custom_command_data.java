/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE CUSTOM_COMMAND_DATA PACKING
package com.MAVLink.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        
/**
* Report status of a command. Includes feedback wether the command was executed.
*/
public class msg_custom_command_data extends MAVLinkMessage{

    public static final int MAVLINK_MSG_ID_CUSTOM_COMMAND_DATA = 79;
    public static final int MAVLINK_MSG_LENGTH = 209;
    private static final long serialVersionUID = MAVLINK_MSG_ID_CUSTOM_COMMAND_DATA;


      
    /**
    * Command ID, as defined by MAV_CMD enum.
    */
    public int command;
      
    /**
    * length of data
    */
    public int len;
      
    /**
    * See MAV_RESULT enum
    */
    public short result;
      
    /**
    * reserved
    */
    public short reserved[] = new short[4];
      
    /**
    * data
    */
    public short data[] = new short[200];
    

    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_CUSTOM_COMMAND_DATA;
              
        packet.payload.putUnsignedShort(command);
              
        packet.payload.putUnsignedShort(len);
              
        packet.payload.putUnsignedByte(result);
              
        
        for (int i = 0; i < reserved.length; i++) {
            packet.payload.putUnsignedByte(reserved[i]);
        }
                    
              
        
        for (int i = 0; i < data.length; i++) {
            packet.payload.putUnsignedByte(data[i]);
        }
                    
        
        return packet;
    }

    /**
    * Decode a custom_command_data message into this class fields
    *
    * @param payload The message to decode
    */
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
              
        this.command = payload.getUnsignedShort();
              
        this.len = payload.getUnsignedShort();
              
        this.result = payload.getUnsignedByte();
              
         
        for (int i = 0; i < this.reserved.length; i++) {
            this.reserved[i] = payload.getUnsignedByte();
        }
                
              
         
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = payload.getUnsignedByte();
        }
                
        
    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_custom_command_data(){
        msgid = MAVLINK_MSG_ID_CUSTOM_COMMAND_DATA;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_custom_command_data(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_CUSTOM_COMMAND_DATA;
        unpack(mavLinkPacket.payload);        
    }

              
    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_CUSTOM_COMMAND_DATA -"+" command:"+command+" len:"+len+" result:"+result+" reserved:"+reserved+" data:"+data+"";
    }
}
        