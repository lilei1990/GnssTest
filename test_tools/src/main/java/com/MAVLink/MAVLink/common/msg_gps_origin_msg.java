/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE GPS_ORIGIN_MSG PACKING
package com.MAVLink.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        
/**
*  
*/
public class msg_gps_origin_msg extends MAVLinkMessage{

    public static final int MAVLINK_MSG_ID_GPS_ORIGIN_MSG = 18;
    public static final int MAVLINK_MSG_LENGTH = 131;
    private static final long serialVersionUID = MAVLINK_MSG_ID_GPS_ORIGIN_MSG;


      
    /**
    *  
    */
    public short gps_index;
      
    /**
    * msg type: 0,GGA;1,RMC
    */
    public short type;
      
    /**
    *  
    */
    public short len;
      
    /**
    * msg content
    */
    public short data[] = new short[128];
    

    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_GPS_ORIGIN_MSG;
              
        packet.payload.putUnsignedByte(gps_index);
              
        packet.payload.putUnsignedByte(type);
              
        packet.payload.putUnsignedByte(len);
              
        
        for (int i = 0; i < data.length; i++) {
            packet.payload.putUnsignedByte(data[i]);
        }
                    
        
        return packet;
    }

    /**
    * Decode a gps_origin_msg message into this class fields
    *
    * @param payload The message to decode
    */
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
              
        this.gps_index = payload.getUnsignedByte();
              
        this.type = payload.getUnsignedByte();
              
        this.len = payload.getUnsignedByte();
              
         
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = payload.getUnsignedByte();
        }
                
        
    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_gps_origin_msg(){
        msgid = MAVLINK_MSG_ID_GPS_ORIGIN_MSG;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_gps_origin_msg(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_GPS_ORIGIN_MSG;
        unpack(mavLinkPacket.payload);        
    }

            
    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_GPS_ORIGIN_MSG -"+" gps_index:"+gps_index+" type:"+type+" len:"+len+" data:"+data+"";
    }
}
        