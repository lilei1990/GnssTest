package com.example.demo.model;

import java.util.List;

public class Config1For601 {

    /**
     * type : getconfig&1
     * result : true
     * object : {"addcamFlag":false,"areaFlag":false,"armLength":100,"camFlag":true,"ccid":"89860400011871408776","cheatWarn":false,"checkFlag":false,"clientid":"c10006852","deviceId":268462162,"deviceType":0,"deviceVersion":"601S.1.3.2A","host":"io.linkio.cn:1883","imsi":"460042069008776","oil":{"flag":false,"oiltype":1,"sensortype":0,"upulse":[]},"password":"hdi601","photoDiff":1800,"photoDiff1":1800,"updateDiff":5,"username":"i601"}
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
        return "Config1For601{" +
                "type='" + type + '\'' +
                ", result=" + result +
                ", object=" + object +
                '}';
    }

    public static class ObjectBean {
        /**
         * addcamFlag : false
         * areaFlag : false
         * armLength : 100
         * camFlag : true
         * ccid : 89860400011871408776
         * cheatWarn : false
         * checkFlag : false
         * clientid : c10006852
         * deviceId : 268462162
         * deviceType : 0
         * deviceVersion : 601S.1.3.2A
         * host : io.linkio.cn:1883
         * imsi : 460042069008776
         * oil : {"flag":false,"oiltype":1,"sensortype":0,"upulse":[]}
         * password : hdi601
         * photoDiff : 1800
         * photoDiff1 : 1800
         * updateDiff : 5
         * username : i601
         */

        private boolean addcamFlag;
        private boolean areaFlag;
        private int armLength;
        private boolean camFlag;
        private String ccid;
        private boolean cheatWarn;
        private boolean checkFlag;
        private String clientid;
        private int deviceId;
        private int deviceType;
        private String deviceVersion;
        private String host;
        private String imsi;
        private OilBean oil;
        private String password;
        private int photoDiff;
        private int photoDiff1;
        private int updateDiff;
        private String username;

        public boolean isAddcamFlag() {
            return addcamFlag;
        }

        @Override
        public String toString() {
            return "ObjectBean{" +
                    "addcamFlag=" + addcamFlag +
                    ", areaFlag=" + areaFlag +
                    ", armLength=" + armLength +
                    ", camFlag=" + camFlag +
                    ", ccid='" + ccid + '\'' +
                    ", cheatWarn=" + cheatWarn +
                    ", checkFlag=" + checkFlag +
                    ", clientid='" + clientid + '\'' +
                    ", deviceId=" + deviceId +
                    ", deviceType=" + deviceType +
                    ", deviceVersion='" + deviceVersion + '\'' +
                    ", host='" + host + '\'' +
                    ", imsi='" + imsi + '\'' +
                    ", oil=" + oil +
                    ", password='" + password + '\'' +
                    ", photoDiff=" + photoDiff +
                    ", photoDiff1=" + photoDiff1 +
                    ", updateDiff=" + updateDiff +
                    ", username='" + username + '\'' +
                    '}';
        }

        public void setAddcamFlag(boolean addcamFlag) {
            this.addcamFlag = addcamFlag;
        }

        public boolean isAreaFlag() {
            return areaFlag;
        }

        public void setAreaFlag(boolean areaFlag) {
            this.areaFlag = areaFlag;
        }

        public int getArmLength() {
            return armLength;
        }

        public void setArmLength(int armLength) {
            this.armLength = armLength;
        }

        public boolean isCamFlag() {
            return camFlag;
        }

        public void setCamFlag(boolean camFlag) {
            this.camFlag = camFlag;
        }

        public String getCcid() {
            return ccid;
        }

        public void setCcid(String ccid) {
            this.ccid = ccid;
        }

        public boolean isCheatWarn() {
            return cheatWarn;
        }

        public void setCheatWarn(boolean cheatWarn) {
            this.cheatWarn = cheatWarn;
        }

        public boolean isCheckFlag() {
            return checkFlag;
        }

        public void setCheckFlag(boolean checkFlag) {
            this.checkFlag = checkFlag;
        }

        public String getClientid() {
            return clientid;
        }

        public void setClientid(String clientid) {
            this.clientid = clientid;
        }

        public int getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(int deviceId) {
            this.deviceId = deviceId;
        }

        public int getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(int deviceType) {
            this.deviceType = deviceType;
        }

        public String getDeviceVersion() {
            return deviceVersion;
        }

        public void setDeviceVersion(String deviceVersion) {
            this.deviceVersion = deviceVersion;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getImsi() {
            return imsi;
        }

        public void setImsi(String imsi) {
            this.imsi = imsi;
        }

        public OilBean getOil() {
            return oil;
        }

        public void setOil(OilBean oil) {
            this.oil = oil;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getPhotoDiff() {
            return photoDiff;
        }

        public void setPhotoDiff(int photoDiff) {
            this.photoDiff = photoDiff;
        }

        public int getPhotoDiff1() {
            return photoDiff1;
        }

        public void setPhotoDiff1(int photoDiff1) {
            this.photoDiff1 = photoDiff1;
        }

        public int getUpdateDiff() {
            return updateDiff;
        }

        public void setUpdateDiff(int updateDiff) {
            this.updateDiff = updateDiff;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public static class OilBean {
            /**
             * flag : false
             * oiltype : 1
             * sensortype : 0
             * upulse : []
             */

            private boolean flag;
            private int oiltype;
            private int sensortype;
            private List<?> upulse;

            public boolean isFlag() {
                return flag;
            }

            public void setFlag(boolean flag) {
                this.flag = flag;
            }

            public int getOiltype() {
                return oiltype;
            }

            public void setOiltype(int oiltype) {
                this.oiltype = oiltype;
            }

            public int getSensortype() {
                return sensortype;
            }

            public void setSensortype(int sensortype) {
                this.sensortype = sensortype;
            }

            public List<?> getUpulse() {
                return upulse;
            }

            public void setUpulse(List<?> upulse) {
                this.upulse = upulse;
            }
        }
    }
}
