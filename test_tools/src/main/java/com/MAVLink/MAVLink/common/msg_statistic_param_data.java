/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE STATISTIC_PARAM_DATA PACKING
package com.MAVLink.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        
/**
* statistics params.
*/
public class msg_statistic_param_data extends MAVLinkMessage{

    public static final int MAVLINK_MSG_ID_STATISTIC_PARAM_DATA = 154;
    public static final int MAVLINK_MSG_LENGTH = 32;
    private static final long serialVersionUID = MAVLINK_MSG_ID_STATISTIC_PARAM_DATA;


      
    /**
    * device id
    */
    public long deviceid;
      
    /**
    * message id
    */
    public long index;
      
    /**
    * UNIX time (+8),yyyyMMddHHmmss,sample 2017.5.15 11:00:00, timestamp is 20170515110000
    */
    public long timestamp;
      
    /**
    * entry time
    */
    public float entry_time;
      
    /**
    * xtrack
    */
    public float xtrack;
    

    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_STATISTIC_PARAM_DATA;
              
        packet.payload.putUnsignedLong(deviceid);
              
        packet.payload.putUnsignedLong(index);
              
        packet.payload.putUnsignedLong(timestamp);
              
        packet.payload.putFloat(entry_time);
              
        packet.payload.putFloat(xtrack);
        
        return packet;
    }

    /**
    * Decode a statistic_param_data message into this class fields
    *
    * @param payload The message to decode
    */
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
              
        this.deviceid = payload.getUnsignedLong();
              
        this.index = payload.getUnsignedLong();
              
        this.timestamp = payload.getUnsignedLong();
              
        this.entry_time = payload.getFloat();
              
        this.xtrack = payload.getFloat();
        
    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_statistic_param_data(){
        msgid = MAVLINK_MSG_ID_STATISTIC_PARAM_DATA;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_statistic_param_data(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_STATISTIC_PARAM_DATA;
        unpack(mavLinkPacket.payload);        
    }

              
    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_STATISTIC_PARAM_DATA -"+" deviceid:"+deviceid+" index:"+index+" timestamp:"+timestamp+" entry_time:"+entry_time+" xtrack:"+xtrack+"";
    }
}
        