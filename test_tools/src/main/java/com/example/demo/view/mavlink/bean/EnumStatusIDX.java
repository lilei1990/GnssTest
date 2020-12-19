package com.example.demo.view.mavlink.bean;

public enum EnumStatusIDX {

    // 实时监控的状态
    IDX_RT_1_IS_AUTOPILOTING(0), // 是否正在自动驾驶
    IDX_RT_1_ANGLESENSOR_IS_CONNECTED(1), // 角度传感器是否连接
    IDX_RT_1_GPS_HAVE_DATA(2), // gps是否有数据
    IDX_RT_1_RADIO_HAVE_DATA(3), // 电台是否有数据
    IDX_RT_1_GPS_IS_FIXED(4), // gps是否为fixed
    IDX_RT_1_IMU_HAVE_DATA(5), // imu是否有数据
    IDX_RT_1_IMU_IS_NORMAL(6), // imu是否正常
    IDX_RT_2_PRESSURE_IS_CONNECTED(7), // 压力传感器是否连接
    IDX_RT_1_N_VOLTAGE_IS_NORMAL(8), // 控制箱电压值是否正常
    IDX_RT_2_Y_VERSION_IS_TRANSFERING(9), // 是否在换版本
    IDX_RT_1_N_YAW_IS_INIT(10), // 是否初始化过
    IDX_RT_1_N_ANGLESENSOR_IS_INIT(11), // 前轮转角是否初始化过
    IDX_RT_2_N_TERM_UART_IS_CONNECTED(12),//控制箱串口是否连接
    IDX_RT_2_N_TERM_CAN_IS_CONNECTED(13),//控制箱can口是否连接
    IDX_RT_1_N_MOTOR_IS_CONNECTED(14),//电机是否连接
    IDX_RT_1_N_MOTOR_IS_NORMAL(15),//电机是否正常
    IDX_RT_2_N_TERM_BLUE_IS_CONNECTED(16), // 蓝牙是否已连接
    IDX_RT_1_GPS2_HAVE_DATA(17), // 扩展gps是否有数据
    IDX_RT_1_GPS2_IS_FIXED(18), // 扩展gps是否已定位
    IDX_RT_1_Y_MAG_HAVE_DATA(19), // 磁力计是否连接
    IDX_RT_2_Y_INS_IS_SUPPORT(20), // 是否可用惯导
    IDX_RT_2_Y_GPS_VERSION_IS_TRANSFERING(21), // GNSS板卡是否在升级中
    IDX_RT_2_Y_FRONT_GYRO_IS_CONNECTED(22), // 是否连接陀螺仪
    IDX_RT_1_N_WISE40_IS_CONNECTED(23),//履带控制器连接状态
    IDX_RT_1_N_WISE40_IS_NORMAL(24),//履带控制器工作状态

    // 自动驾驶中监控状态
    IDX_AP_1_ANGLESENSOR_IS_NORMAL(64),  // 角度传感器状态
    IDX_AP_1_Y_VALVE_VOLTAGE_IS_NORMAL(65), // 液压阀电压

    IDX_NUM(128);

    private int value;

    private EnumStatusIDX(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }
}
