package com.example.demo.view.mavlink.bean;

import com.MAVLink.MAVLink.common.msg_sys_status;

/**
 * 设备状态
 *
 * @author hg
 */
public class NavStatus {

    public boolean isAutoPilot; // 是否正在自动驾驶
    public boolean isConnAngleSensor; // 角度传感器是否连接
    public boolean isGpsData; // gps是否有数据
    public boolean isRadioData; // 电台是否有数据
    public boolean isGpsFixed; // gps是否为fixed
    public boolean isImuData; // imu是否有数据
    public boolean isImuNormal; // imu是否正常
    public boolean isConnPressure; // 压力传感器是否连接
    public boolean isVoltageNormal; // 控制箱电压是否正常
    public boolean isVersionIng; // 是否在换版本
    public boolean isYawInit; // 方向角是否初始化过
    // 自动驾驶中监控状态
    public boolean isAnglesensorNormal;  // 角度传感器状态
    public boolean isVoltageValue; // 液压阀电压值是否正常
    public boolean isAngleSensorInit; //前轮转角初始化状态
    public boolean isUartConnect; //控制箱串口是否连接
    public boolean isCanConnect; //控制箱can口是否连接
    public boolean isMotorConnect; //电机是否已连接
    public boolean isMotorNormal; //电机是否正常
    public boolean isGps2Data; // 扩展gps是否有数据
    public boolean isGps2Fixed; // 扩展gps是否为fixed
    public boolean isMagData; // 磁力计是否连接
    public boolean isSupportIns; // 是否支持惯性导航
    public boolean isGyroConnect; // 陀螺仪是否已连接
    public boolean isWiseConnect;//履带控制器连接状态
    public boolean isWiseNormal;//履带控制器工作状态

    public NavStatus() {
        isAutoPilot = false;
        isConnAngleSensor = false;
        isGpsData = false;
        isRadioData = false;
        isGpsFixed = false;
        isImuData = false;
        isImuNormal = false;
        isConnPressure = false;
        isAnglesensorNormal = false;
        isVoltageNormal = false;
        isVersionIng = false;
        isYawInit = false;
        isAngleSensorInit = false;
        isGps2Data = false;
        isGps2Fixed = false;
        isMagData = false;
        isSupportIns = false;
        isGyroConnect = false;
        isWiseConnect = false;
        isWiseNormal = false;
    }

    private boolean setBitValue(int value, short[] status) {
        int index;
        int indexbit;
        int bitvalue;

        index = value / 8;
        indexbit = value % 8;
        bitvalue = (status[index] >> indexbit) & 0x01;
        if (0 != bitvalue) {
            return true;
        } else {
            return false;
        }
    }

    public void setValue(msg_sys_status msg) {
        int value;

        value = EnumStatusIDX.IDX_RT_1_IS_AUTOPILOTING.getValue();
        isAutoPilot = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_1_ANGLESENSOR_IS_CONNECTED.getValue();
        isConnAngleSensor = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_1_GPS_HAVE_DATA.getValue();
        isGpsData = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_1_RADIO_HAVE_DATA.getValue();
        isRadioData = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_1_GPS_IS_FIXED.getValue();
        isGpsFixed = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_1_IMU_HAVE_DATA.getValue();
        isImuData = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_1_IMU_IS_NORMAL.getValue();
        isImuNormal = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_2_PRESSURE_IS_CONNECTED.getValue();
        isConnPressure = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_1_N_VOLTAGE_IS_NORMAL.getValue();
        isVoltageNormal = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_2_Y_VERSION_IS_TRANSFERING.getValue();
        isVersionIng = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_1_N_YAW_IS_INIT.getValue();
        isYawInit = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_AP_1_ANGLESENSOR_IS_NORMAL.getValue();
        isAnglesensorNormal = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_AP_1_Y_VALVE_VOLTAGE_IS_NORMAL.getValue();
        isVoltageValue = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_1_N_ANGLESENSOR_IS_INIT.getValue();
        isAngleSensorInit = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_2_N_TERM_UART_IS_CONNECTED.getValue();
        isUartConnect = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_2_N_TERM_CAN_IS_CONNECTED.getValue();
        isCanConnect = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_1_N_MOTOR_IS_CONNECTED.getValue();
        isMotorConnect = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_1_N_MOTOR_IS_NORMAL.getValue();
        isMotorNormal = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_1_GPS2_HAVE_DATA.getValue();
        isGps2Data = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_1_GPS2_IS_FIXED.getValue();
        isGps2Fixed = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_1_Y_MAG_HAVE_DATA.getValue();
        isMagData = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_2_Y_INS_IS_SUPPORT.getValue();
        isSupportIns = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_2_Y_FRONT_GYRO_IS_CONNECTED.getValue();
        isGyroConnect = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_1_N_WISE40_IS_CONNECTED.getValue();
        isWiseConnect = setBitValue(value, msg.status);

        value = EnumStatusIDX.IDX_RT_1_N_WISE40_IS_NORMAL.getValue();
        isWiseNormal = setBitValue(value, msg.status);

    }

    /**
     * 是否可以自动驾驶
     *
     * @return
     */
    public boolean isAutoEnabled() {
        if ((false == isConnAngleSensor) ||
                (false == isGpsData) ||
                (false == isRadioData) ||
                (false == isGpsFixed) ||
                (false == isImuData) ||
                (false == isImuNormal) ||
                (false == isVoltageNormal) ||
                (false == isAngleSensorInit) ||
                (false == isYawInit)) {
            return false;
        }
        return true;
    }

    /**
     * 是否正在自动驾驶
     *
     * @return
     */
    public boolean isAutoPiloting() {
        return isAutoPilot;
    }

    public void setAutoPiloting(boolean isAutoPiloting) {
        this.isAutoPilot = isAutoPiloting;
    }

}
