/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

package com.MAVLink.MAVLink.common;

/**
* X.25 CRC calculation for MAVlink messages. The checksum must be initialized,
* updated with witch field of the message, and then finished with the message
* id.
*
*/
public class CRC {
    private static final int[] MAVLINK_MESSAGE_CRCS = {50, 22, 137, 93, 237, 217, 104, 119, 114, 144, 79, 89, 250, 18, 247, 184, 7, 56, 109, 206, 214, 159, 220, 168, 24, 23, 170, 144, 67, 115, 39, 246, 185, 104, 237, 244, 222, 212, 9, 102, 230, 28, 28, 132, 221, 232, 11, 153, 41, 39, 78, 196, 244, 0, 15, 3, 239, 130, 163, 184, 0, 167, 183, 119, 191, 118, 148, 21, 130, 243, 124, 241, 0, 38, 20, 158, 152, 143, 110, 0, 0, 106, 49, 22, 143, 140, 5, 150, 0, 231, 183, 63, 54, 47, 242, 0, 0, 0, 0, 0, 175, 102, 158, 208, 56, 93, 138, 108, 32, 185, 243, 227, 174, 124, 237, 4, 76, 128, 56, 116, 134, 237, 203, 250, 87, 203, 220, 25, 226, 46, 29, 223, 85, 6, 229, 203, 1, 195, 109, 168, 181, 47, 72, 131, 127, 0, 103, 154, 78, 200, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 228, 95, 25, 77, 45, 159, 241, 250, 49, 155, 39, 63, 4, 167, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 163, 105, 151, 35, 150, 210, 0, 0, 0, 0, 0, 90, 104, 85, 95, 130, 184, 81, 8, 204, 49, 170, 44, 83, 46, 0};
    private static final int CRC_INIT_VALUE = 0xffff;
    private int crcValue;

    /**
    * Accumulate the X.25 CRC by adding one char at a time.
    *
    * The checksum function adds the hash of one char at a time to the 16 bit
    * checksum (uint16_t).
    *
    * @param data
    *            new char to hash
    **/
    public  void update_checksum(int data) {
        data = data & 0xff; //cast because we want an unsigned type
        int tmp = data ^ (crcValue & 0xff);
        tmp ^= (tmp << 4) & 0xff;
        crcValue = ((crcValue >> 8) & 0xff) ^ (tmp << 8) ^ (tmp << 3) ^ ((tmp >> 4) & 0xf);
    }

    /**
    * Finish the CRC calculation of a message, by running the CRC with the
    * Magic Byte. This Magic byte has been defined in MAVlink v1.0.
    *
    * @param msgid
    *            The message id number
    */
    public void finish_checksum(int msgid) {
        update_checksum(MAVLINK_MESSAGE_CRCS[msgid & 0xff]);
    }

    /**
    * Initialize the buffer for the X.25 CRC
    *
    */
    public void start_checksum() {
        crcValue = CRC_INIT_VALUE;
    }

    public int getMSB() {
        return ((crcValue >> 8) & 0xff);
    }

    public int getLSB() {
        return (crcValue & 0xff);
    }

    public CRC() {
        start_checksum();
    }

}
        