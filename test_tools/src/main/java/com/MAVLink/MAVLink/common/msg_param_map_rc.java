/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE PARAM_MAP_RC PACKING
package com.MAVLink.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        
/**
* Bind a RC channel to a parameter. The parameter should change accoding to the RC channel value.
*/
public class msg_param_map_rc extends MAVLinkMessage{

    public static final int MAVLINK_MSG_ID_PARAM_MAP_RC = 50;
    public static final int MAVLINK_MSG_LENGTH = 37;
    private static final long serialVersionUID = MAVLINK_MSG_ID_PARAM_MAP_RC;


      
    /**
    * Initial parameter value
    */
    public float param_value0;
      
    /**
    * Scale, maps the RC range [-1, 1] to a parameter value
    */
    public float scale;
      
    /**
    * Minimum param value. The protocol does not define if this overwrites an onboard minimum value. (Depends on implementation)
    */
    public float param_value_min;
      
    /**
    * Maximum param value. The protocol does not define if this overwrites an onboard maximum value. (Depends on implementation)
    */
    public float param_value_max;
      
    /**
    * Parameter index. Send -1 to use the param ID field as identifier (else the param id will be ignored), send -2 to disable any existing map for this rc_channel_index.
    */
    public short param_index;
      
    /**
    * System ID
    */
    public short target_system;
      
    /**
    * Component ID
    */
    public short target_component;
      
    /**
    * Onboard parameter id, terminated by NULL if the length is less than 16 human-readable chars and WITHOUT null termination (NULL) byte if the length is exactly 16 chars - applications have to provide 16+1 bytes storage if the ID is stored as string
    */
    public byte param_id[] = new byte[16];
      
    /**
    * Index of parameter RC channel. Not equal to the RC channel id. Typically correpsonds to a potentiometer-knob on the RC.
    */
    public short parameter_rc_channel_index;
    

    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_PARAM_MAP_RC;
              
        packet.payload.putFloat(param_value0);
              
        packet.payload.putFloat(scale);
              
        packet.payload.putFloat(param_value_min);
              
        packet.payload.putFloat(param_value_max);
              
        packet.payload.putShort(param_index);
              
        packet.payload.putUnsignedByte(target_system);
              
        packet.payload.putUnsignedByte(target_component);
              
        
        for (int i = 0; i < param_id.length; i++) {
            packet.payload.putByte(param_id[i]);
        }
                    
              
        packet.payload.putUnsignedByte(parameter_rc_channel_index);
        
        return packet;
    }

    /**
    * Decode a param_map_rc message into this class fields
    *
    * @param payload The message to decode
    */
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
              
        this.param_value0 = payload.getFloat();
              
        this.scale = payload.getFloat();
              
        this.param_value_min = payload.getFloat();
              
        this.param_value_max = payload.getFloat();
              
        this.param_index = payload.getShort();
              
        this.target_system = payload.getUnsignedByte();
              
        this.target_component = payload.getUnsignedByte();
              
         
        for (int i = 0; i < this.param_id.length; i++) {
            this.param_id[i] = payload.getByte();
        }
                
              
        this.parameter_rc_channel_index = payload.getUnsignedByte();
        
    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_param_map_rc(){
        msgid = MAVLINK_MSG_ID_PARAM_MAP_RC;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_param_map_rc(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_PARAM_MAP_RC;
        unpack(mavLinkPacket.payload);        
    }

                   
    /**
    * Sets the buffer of this message with a string, adds the necessary padding
    */
    public void setParam_Id(String str) {
        int len = Math.min(str.length(), 16);
        for (int i=0; i<len; i++) {
            param_id[i] = (byte) str.charAt(i);
        }

        for (int i=len; i<16; i++) {            // padding for the rest of the buffer
            param_id[i] = 0;
        }
    }

    /**
    * Gets the message, formated as a string
    */
    public String getParam_Id() {
        String result = "";
        for (int i = 0; i < 16; i++) {
            if (param_id[i] != 0)
                result = result + (char) param_id[i];
            else
                break;
        }
        return result;

    }
                           
    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_PARAM_MAP_RC -"+" param_value0:"+param_value0+" scale:"+scale+" param_value_min:"+param_value_min+" param_value_max:"+param_value_max+" param_index:"+param_index+" target_system:"+target_system+" target_component:"+target_component+" param_id:"+param_id+" parameter_rc_channel_index:"+parameter_rc_channel_index+"";
    }
}
        