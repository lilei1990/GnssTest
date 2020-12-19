package com.example.demo.model
enum class EnumProperties(val defaultString:String, val defaultBoolean: Boolean=true){
    // 公司名称
    // HD :惠达整机测试
    // HG:寰果整机测试
    // 默认惠达
    // HD_BOARD ：单板
    COMPANY("RECEIVER"),//此为接收机测试工具抬头
    TEST_MODEL ( "02"),//测试模式，区分 02整机测试 和  01单板测试  03 传感器测试
    USER_ID ( "001"),//操作员
    MONITOR_TEST_TYPE ( "A"),//控制器测试类型A B
    SENSOR_TEST_TYPE ( "A"),//传感器测试类型A B
    EQUIPMENTID ( "16200001"),//EQUIPMENTID 设备号
    UPLOAD_URL ( "http://120.132.12.76:8090/hd_product"),//上报地址
    IS_TEST_PANE_REMOVE("",false),//是否隐藏测试面板
    PRINT_START("10"),//默认打印y轴偏移
    SUPPORT_UPDATE("APP升级",true),//默认打印y轴偏移
    WODE_SWITCH("沃得配置开关",true),//默认打印y轴偏移
    APP_VERSION("1.5.1",false),// HD100 APP version
    UPDATE_PATH(""),//更新地址
    SERIAL_NO("1"),//流水号
    SERIAL_DATE("0"),//流水号日期
    PRINT_REMARK("自定义文字1"),
    ROW_WIDTH("65"),
    GROW_WIDTH("25"),
    BOM_TEXT("106C-102"),
}


enum class GPRS_STATUS {
    无数据,
    sim未识别,
    GSM注册,
    GPRS注册,
    sim卡已识别,
    未知;
}

/**
 * 两个串口标识
 */
enum class SERIAR_PORT {
    CAN1,
    CAN2
}
enum class PWM_ADC {
    LEFT,
    RIGHT;
}


enum class TEST_MODEL {
    退出测试模式,//00
    单板测试模式,//01
    整机测试模式,//02
    传感器测试
}
enum class SEEDING_CONFIG {
    //9	配置项索引
    设备ID,
    详细AD数据开关,
    `软件版本号（只可读，不可写，版本号由另外的版本制作工具生成）`,
    硬件版本号,
    单板ID,
    校准目标电压,
}

enum class PACKAGE_TYPE(var context: String = "") {
    UNKNOW,//0
    IMEI,//1:IMEI，字符串
    IMSI,//2:IMSI，字符串
    ICCID,//3:CCID，字符串
    gprs版本号,//4:gprs版本号，字符串
    GPS时间,//5:GPS时间，时间戳
    RAW数据;//6:RAW数据，short int[3];;

    override fun toString(): String {
        return "$name = $context"
    }

}
enum class EnumWodeSwitch(){
    关闭,
    打开
}
enum class EnumConfigStatue(){
    成功,
    Index超范围,
    写Flash失败
}
//3、单板ID写接口：0x3C，索引=4；读接口：0x3D，索引=4
//4、整机ID（即设备ID）写接口：0x3C，索引=0；读接口：0x3D，索引=0
enum class EnumSeedingModel( val index:Byte){
    单板(4),
    整机(0)
}
enum class EnumUpdateStatus( ){
    待补充,
    单板测试,
    整机测试
}
enum class EnumBigPackBoms(val value:String ){
    `控制器类型：`("智能播种监控系统"),
   // `产品型号：`("106C-102"),
    `终端软件版本：`("601C.093.12"),
    `控制器软件版本：`("c93720.308.01"),
}
enum class EnumSetting(val value:String ){
    `控制器类型：`("智能播种监控系统"),
   // `产品型号：`("106C-102"),
    `终端软件版本：`("601C.093.12"),
    `控制器软件版本：`("c93720.308.01"),
}