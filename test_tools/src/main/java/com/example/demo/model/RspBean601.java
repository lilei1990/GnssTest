package com.example.demo.model;


import com.google.gson.annotations.SerializedName;

public class RspBean601 {

    /**
     * type : getstatistic
     * result : true
     * object : {"2G":0,"4G":2,"camStatus":1,"camaddStatus":1,"deviceId":268462162,"elapsedTime":7014,"engineType":0,"gprsImageConCnt":0,"gprsImageConSucCnt":0,"gprsImageSendCnt":0,"gprsImageSendSucCnt":0,"gprsPdpActiveCnt":0,"gprsPdpActiveSucCnt":0,"gprsStatus":0,"gprsStatusConCnt":0,"gprsStatusConSucCnt":0,"gprsStatusSendCnt":1297,"gprsStatusSendSucCnt":1297,"gpsReportStatusCnt":0,"gpsSearchTime":0,"gpsStatus":1,"gpsValidStatusCnt":0,"latitude":"0°0'0\"","localTime":"20.11.02 13:08:59","longitude":"0°0'0\"","midStatus":1,"result":true,"ret":0,"simStatus":0,"tfAvail":6340384,"tfImageUnsynCnt":0,"tfStatus":0,"tfStatusUnsynCnt":2,"tfTotal":6720520,"thisImageCnt":0,"thisStatusCnt":1282,"thisStatusSynCnt":1281,"time":"20.11.02 13:08:59"}
     */

    private String type;
    private boolean result;
    private ObjectBean object;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public ObjectBean getObject() {
        return object;
    }

    public void setObject(ObjectBean object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "RspBean601{" +
                "type='" + type + '\'' +
                ", result=" + result +
                ", object=" + object +
                '}';
    }

    public static class ObjectBean {
        /**
         * 2G : 0
         * 4G : 2
         * camStatus : 1
         * camaddStatus : 1
         * deviceId : 268462162
         * elapsedTime : 7014
         * engineType : 0
         * gprsImageConCnt : 0
         * gprsImageConSucCnt : 0
         * gprsImageSendCnt : 0
         * gprsImageSendSucCnt : 0
         * gprsPdpActiveCnt : 0
         * gprsPdpActiveSucCnt : 0
         * gprsStatus : 0
         * gprsStatusConCnt : 0
         * gprsStatusConSucCnt : 0
         * gprsStatusSendCnt : 1297
         * gprsStatusSendSucCnt : 1297
         * gpsReportStatusCnt : 0
         * gpsSearchTime : 0
         * gpsStatus : 1
         * gpsValidStatusCnt : 0
         * latitude : 0°0'0"
         * localTime : 20.11.02 13:08:59
         * longitude : 0°0'0"
         * midStatus : 1
         * result : true
         * ret : 0
         * simStatus : 0
         * tfAvail : 6340384
         * tfImageUnsynCnt : 0
         * tfStatus : 0
         * tfStatusUnsynCnt : 2
         * tfTotal : 6720520
         * thisImageCnt : 0
         * thisStatusCnt : 1282
         * thisStatusSynCnt : 1281
         * time : 20.11.02 13:08:59
         */

        @SerializedName("2G")
        private int _$2G;
        @SerializedName("4G")
        private int _$4G;
        private int camStatus;
        private int camaddStatus;
        private int deviceId;
        private int elapsedTime;
        private int engineType;
        private int gprsImageConCnt;
        private int gprsImageConSucCnt;
        private int gprsImageSendCnt;
        private int gprsImageSendSucCnt;
        private int gprsPdpActiveCnt;
        private int gprsPdpActiveSucCnt;
        private int gprsStatus;
        private int gprsStatusConCnt;
        private int gprsStatusConSucCnt;
        private int gprsStatusSendCnt;
        private int gprsStatusSendSucCnt;
        private int gpsReportStatusCnt;
        private int gpsSearchTime;
        private int gpsStatus;
        private int code;
        private int gpsValidStatusCnt;
        private String latitude;
        private String operation;
        private String localTime;
        private String longitude;
        private int midStatus;
        private boolean result;
        private int ret;
        private int simStatus;
        private int tfAvail;
        private int tfImageUnsynCnt;
        private int tfStatus;
        private int tfStatusUnsynCnt;
        private int tfTotal;
        private int thisImageCnt;
        private int thisStatusCnt;
        private int thisStatusSynCnt;
        private String time;

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "ObjectBean{" +
                    "_$2G=" + _$2G +
                    ", _$4G=" + _$4G +
                    ", camStatus=" + camStatus +
                    ", camaddStatus=" + camaddStatus +
                    ", deviceId=" + deviceId +
                    ", elapsedTime=" + elapsedTime +
                    ", engineType=" + engineType +
                    ", gprsImageConCnt=" + gprsImageConCnt +
                    ", gprsImageConSucCnt=" + gprsImageConSucCnt +
                    ", gprsImageSendCnt=" + gprsImageSendCnt +
                    ", gprsImageSendSucCnt=" + gprsImageSendSucCnt +
                    ", gprsPdpActiveCnt=" + gprsPdpActiveCnt +
                    ", gprsPdpActiveSucCnt=" + gprsPdpActiveSucCnt +
                    ", gprsStatus=" + gprsStatus +
                    ", gprsStatusConCnt=" + gprsStatusConCnt +
                    ", gprsStatusConSucCnt=" + gprsStatusConSucCnt +
                    ", gprsStatusSendCnt=" + gprsStatusSendCnt +
                    ", gprsStatusSendSucCnt=" + gprsStatusSendSucCnt +
                    ", gpsReportStatusCnt=" + gpsReportStatusCnt +
                    ", gpsSearchTime=" + gpsSearchTime +
                    ", gpsStatus=" + gpsStatus +
                    ", code=" + code +
                    ", gpsValidStatusCnt=" + gpsValidStatusCnt +
                    ", latitude='" + latitude + '\'' +
                    ", operation='" + operation + '\'' +
                    ", localTime='" + localTime + '\'' +
                    ", longitude='" + longitude + '\'' +
                    ", midStatus=" + midStatus +
                    ", result=" + result +
                    ", ret=" + ret +
                    ", simStatus=" + simStatus +
                    ", tfAvail=" + tfAvail +
                    ", tfImageUnsynCnt=" + tfImageUnsynCnt +
                    ", tfStatus=" + tfStatus +
                    ", tfStatusUnsynCnt=" + tfStatusUnsynCnt +
                    ", tfTotal=" + tfTotal +
                    ", thisImageCnt=" + thisImageCnt +
                    ", thisStatusCnt=" + thisStatusCnt +
                    ", thisStatusSynCnt=" + thisStatusSynCnt +
                    ", time='" + time + '\'' +
                    '}';
        }

        public int get_$2G() {
            return _$2G;
        }

        public void set_$2G(int _$2G) {
            this._$2G = _$2G;
        }

        public int get_$4G() {
            return _$4G;
        }

        public void set_$4G(int _$4G) {
            this._$4G = _$4G;
        }

        public int getCamStatus() {
            return camStatus;
        }

        public void setCamStatus(int camStatus) {
            this.camStatus = camStatus;
        }

        public int getCamaddStatus() {
            return camaddStatus;
        }

        public void setCamaddStatus(int camaddStatus) {
            this.camaddStatus = camaddStatus;
        }

        public int getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(int deviceId) {
            this.deviceId = deviceId;
        }

        public int getElapsedTime() {
            return elapsedTime;
        }

        public void setElapsedTime(int elapsedTime) {
            this.elapsedTime = elapsedTime;
        }

        public int getEngineType() {
            return engineType;
        }

        public void setEngineType(int engineType) {
            this.engineType = engineType;
        }

        public int getGprsImageConCnt() {
            return gprsImageConCnt;
        }

        public void setGprsImageConCnt(int gprsImageConCnt) {
            this.gprsImageConCnt = gprsImageConCnt;
        }

        public int getGprsImageConSucCnt() {
            return gprsImageConSucCnt;
        }

        public void setGprsImageConSucCnt(int gprsImageConSucCnt) {
            this.gprsImageConSucCnt = gprsImageConSucCnt;
        }

        public int getGprsImageSendCnt() {
            return gprsImageSendCnt;
        }

        public void setGprsImageSendCnt(int gprsImageSendCnt) {
            this.gprsImageSendCnt = gprsImageSendCnt;
        }

        public int getGprsImageSendSucCnt() {
            return gprsImageSendSucCnt;
        }

        public void setGprsImageSendSucCnt(int gprsImageSendSucCnt) {
            this.gprsImageSendSucCnt = gprsImageSendSucCnt;
        }

        public int getGprsPdpActiveCnt() {
            return gprsPdpActiveCnt;
        }

        public void setGprsPdpActiveCnt(int gprsPdpActiveCnt) {
            this.gprsPdpActiveCnt = gprsPdpActiveCnt;
        }

        public int getGprsPdpActiveSucCnt() {
            return gprsPdpActiveSucCnt;
        }

        public void setGprsPdpActiveSucCnt(int gprsPdpActiveSucCnt) {
            this.gprsPdpActiveSucCnt = gprsPdpActiveSucCnt;
        }

        public int getGprsStatus() {
            return gprsStatus;
        }

        public void setGprsStatus(int gprsStatus) {
            this.gprsStatus = gprsStatus;
        }

        public int getGprsStatusConCnt() {
            return gprsStatusConCnt;
        }

        public void setGprsStatusConCnt(int gprsStatusConCnt) {
            this.gprsStatusConCnt = gprsStatusConCnt;
        }

        public int getGprsStatusConSucCnt() {
            return gprsStatusConSucCnt;
        }

        public void setGprsStatusConSucCnt(int gprsStatusConSucCnt) {
            this.gprsStatusConSucCnt = gprsStatusConSucCnt;
        }

        public int getGprsStatusSendCnt() {
            return gprsStatusSendCnt;
        }

        public void setGprsStatusSendCnt(int gprsStatusSendCnt) {
            this.gprsStatusSendCnt = gprsStatusSendCnt;
        }

        public int getGprsStatusSendSucCnt() {
            return gprsStatusSendSucCnt;
        }

        public void setGprsStatusSendSucCnt(int gprsStatusSendSucCnt) {
            this.gprsStatusSendSucCnt = gprsStatusSendSucCnt;
        }

        public int getGpsReportStatusCnt() {
            return gpsReportStatusCnt;
        }

        public void setGpsReportStatusCnt(int gpsReportStatusCnt) {
            this.gpsReportStatusCnt = gpsReportStatusCnt;
        }

        public int getGpsSearchTime() {
            return gpsSearchTime;
        }

        public void setGpsSearchTime(int gpsSearchTime) {
            this.gpsSearchTime = gpsSearchTime;
        }

        public int getGpsStatus() {
            return gpsStatus;
        }

        public void setGpsStatus(int gpsStatus) {
            this.gpsStatus = gpsStatus;
        }

        public int getGpsValidStatusCnt() {
            return gpsValidStatusCnt;
        }

        public void setGpsValidStatusCnt(int gpsValidStatusCnt) {
            this.gpsValidStatusCnt = gpsValidStatusCnt;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLocalTime() {
            return localTime;
        }

        public void setLocalTime(String localTime) {
            this.localTime = localTime;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public int getMidStatus() {
            return midStatus;
        }

        public void setMidStatus(int midStatus) {
            this.midStatus = midStatus;
        }

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public int getRet() {
            return ret;
        }

        public void setRet(int ret) {
            this.ret = ret;
        }

        public int getSimStatus() {
            return simStatus;
        }

        public void setSimStatus(int simStatus) {
            this.simStatus = simStatus;
        }

        public int getTfAvail() {
            return tfAvail;
        }

        public void setTfAvail(int tfAvail) {
            this.tfAvail = tfAvail;
        }

        public int getTfImageUnsynCnt() {
            return tfImageUnsynCnt;
        }

        public void setTfImageUnsynCnt(int tfImageUnsynCnt) {
            this.tfImageUnsynCnt = tfImageUnsynCnt;
        }

        public int getTfStatus() {
            return tfStatus;
        }

        public void setTfStatus(int tfStatus) {
            this.tfStatus = tfStatus;
        }

        public int getTfStatusUnsynCnt() {
            return tfStatusUnsynCnt;
        }

        public void setTfStatusUnsynCnt(int tfStatusUnsynCnt) {
            this.tfStatusUnsynCnt = tfStatusUnsynCnt;
        }

        public int getTfTotal() {
            return tfTotal;
        }

        public void setTfTotal(int tfTotal) {
            this.tfTotal = tfTotal;
        }

        public int getThisImageCnt() {
            return thisImageCnt;
        }

        public void setThisImageCnt(int thisImageCnt) {
            this.thisImageCnt = thisImageCnt;
        }

        public int getThisStatusCnt() {
            return thisStatusCnt;
        }

        public void setThisStatusCnt(int thisStatusCnt) {
            this.thisStatusCnt = thisStatusCnt;
        }

        public int getThisStatusSynCnt() {
            return thisStatusSynCnt;
        }

        public void setThisStatusSynCnt(int thisStatusSynCnt) {
            this.thisStatusSynCnt = thisStatusSynCnt;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
