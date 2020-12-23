/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE PATH_DETAIL PACKING
package com.MAVLink.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;

/**
* PLAN DETAIL.
*/
public class msg_path_detail extends MAVLinkMessage{

    public static final int MAVLINK_MSG_ID_PATH_DETAIL = 16;
    public static final int MAVLINK_MSG_LENGTH = 58;
    private static final long serialVersionUID = MAVLINK_MSG_ID_PATH_DETAIL;



    /**
    * reserved.
    */
    public int A_lng;

    /**
    * reserved.
    */
    public int A_lat;

    /**
    * reserved.
    */
    public int B_lng;

    /**
    * reserved.
    */
    public int B_lat;

    /**
    * reserved.
    */
    public float next_curve;

    /**
    * reserved.
    */
    public int loc_rear_lng;

    /**
    * reserved.
    */
    public int loc_rear_lat;

    /**
    * reserved.
    */
    public float speed_rear;

    /**
    * reserved.
    */
    public float yaw_rear;

    /**
    * reserved.
    */
    public float roll;

    /**
    * reserved.
    */
    public float pitch;

    /**
    * reserved.
    */
    public float xte_rear;

    /**
    * reserved.
    */
    public float xte_control;

    /**
    * reserved.
    */
    public float heading_error;

    /**
    * reserved.
    */
    public byte traj_num;

    /**
    * reserved.
    */
    public byte traj_type;


    /**
    * Generates the payload for a mavlink message for a message of this type
    * @return
    */
    public MAVLinkPacket pack(){
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH);
        packet.sysid = 255;
        packet.compid = 190;
        packet.msgid = MAVLINK_MSG_ID_PATH_DETAIL;

        packet.payload.putInt(A_lng);

        packet.payload.putInt(A_lat);

        packet.payload.putInt(B_lng);

        packet.payload.putInt(B_lat);

        packet.payload.putFloat(next_curve);

        packet.payload.putInt(loc_rear_lng);

        packet.payload.putInt(loc_rear_lat);

        packet.payload.putFloat(speed_rear);

        packet.payload.putFloat(yaw_rear);

        packet.payload.putFloat(roll);

        packet.payload.putFloat(pitch);

        packet.payload.putFloat(xte_rear);

        packet.payload.putFloat(xte_control);

        packet.payload.putFloat(heading_error);

        packet.payload.putByte(traj_num);

        packet.payload.putByte(traj_type);

        return packet;
    }

    /**
    * Decode a path_detail message into this class fields
    *
    * @param payload The message to decode
    */
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();

        this.A_lng = payload.getInt();

        this.A_lat = payload.getInt();

        this.B_lng = payload.getInt();

        this.B_lat = payload.getInt();

        this.next_curve = payload.getFloat();

        this.loc_rear_lng = payload.getInt();

        this.loc_rear_lat = payload.getInt();

        this.speed_rear = payload.getFloat();

        this.yaw_rear = payload.getFloat();

        this.roll = payload.getFloat();

        this.pitch = payload.getFloat();

        this.xte_rear = payload.getFloat();

        this.xte_control = payload.getFloat();

        this.heading_error = payload.getFloat();

        this.traj_num = payload.getByte();

        this.traj_type = payload.getByte();

    }

    /**
    * Constructor for a new message, just initializes the msgid
    */
    public msg_path_detail(){
        msgid = MAVLINK_MSG_ID_PATH_DETAIL;
    }

    /**
    * Constructor for a new message, initializes the message with the payload
    * from a mavlink packet
    *
    */
    public msg_path_detail(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_PATH_DETAIL;
        unpack(mavLinkPacket.payload);
    }


    /**
    * Returns a string with the MSG name and data
    */
    public String toString(){
        return "MAVLINK_MSG_ID_PATH_DETAIL -"+" A_lng:"+A_lng+" A_lat:"+A_lat+" B_lng:"+B_lng+" B_lat:"+B_lat+" next_curve:"+next_curve+" loc_rear_lng:"+loc_rear_lng+" loc_rear_lat:"+loc_rear_lat+" speed_rear:"+speed_rear+" yaw_rear:"+yaw_rear+" roll:"+roll+" pitch:"+pitch+" xte_rear:"+xte_rear+" xte_control:"+xte_control+" heading_error:"+heading_error+" traj_num:"+traj_num+" traj_type:"+traj_type+"";
    }
}
        