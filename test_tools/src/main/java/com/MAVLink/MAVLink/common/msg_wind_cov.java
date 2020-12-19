/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE WIND_COV PACKING
package com.MAVLink.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        
/**
* 
*/
public class msg_wind_cov extends MAVLinkMessage{

    public static final int MAVLINK_MSG_ID_WIND_COV = 231;
    public static final int MAVLINK_MSG_LENGTH = 40;
    private static final long serialVersionUID = MAVLINK_MSG_ID_WIND_COV;


      
    /**
    * Timestamp (micros since boot or Unix epoch)
    */
    public long time_usec;
      
    /**
    * Wind in X (NED) direction in m/s
    */
    public float wind_x;
      
    /**
    * Wind in Y (NED) direction in m/s
    */
    public float wind_y;
      
    /**
    * Wind in Z (NED) direction in m/s
    */
    public float wind_z;
      
    /**
    * Variability of the wind in XY. RMS of a 1 Hz lowpassed wind estimate.
    */
    public float var_horiz;
      
    /**
    * Variability of the wind in Z. RMS of a 1 Hz lowpassed wind estimate.
    */
    public float var_vert;
      
    /**
    * AMSL altitude (m) this measurement was taken at
    */
    public float wind_alt;
      
    /**
    * Horizontal speed 1-STD accuracy
    */
    public float horiz_accuracy;
      
    /**
    * Vertical speed 1-STD accuracy
    */
    public float vert_accuracy;
    

    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_WIND_COV;
              
        packet.payload.putUnsignedLong(time_usec);
              
        packet.payload.putFloat(wind_x);
              
        packet.payload.putFloat(wind_y);
              
        packet.payload.putFloat(wind_z);
              
        packet.payload.putFloat(var_horiz);
              
        packet.payload.putFloat(var_vert);
              
        packet.payload.putFloat(wind_alt);
              
        packet.payload.putFloat(horiz_accuracy);
              
        packet.payload.putFloat(vert_accuracy);
        
        return packet;
    }

    /**
    * Decode a wind_cov message into this class fields
    *
    * @param payload The message to decode
    */
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
              
        this.time_usec = payload.getUnsignedLong();
              
        this.wind_x = payload.getFloat();
              
        this.wind_y = payload.getFloat();
              
        this.wind_z = payload.getFloat();
              
        this.var_horiz = payload.getFloat();
              
        this.var_vert = payload.getFloat();
              
        this.wind_alt = payload.getFloat();
              
        this.horiz_accuracy = payload.getFloat();
              
        this.vert_accuracy = payload.getFloat();
        
    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_wind_cov(){
        msgid = MAVLINK_MSG_ID_WIND_COV;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_wind_cov(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_WIND_COV;
        unpack(mavLinkPacket.payload);        
    }

                      
    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_WIND_COV -"+" time_usec:"+time_usec+" wind_x:"+wind_x+" wind_y:"+wind_y+" wind_z:"+wind_z+" var_horiz:"+var_horiz+" var_vert:"+var_vert+" wind_alt:"+wind_alt+" horiz_accuracy:"+horiz_accuracy+" vert_accuracy:"+vert_accuracy+"";
    }
}
        