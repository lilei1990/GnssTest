package com.example.demo.model;


import com.example.demo.utils.PropertiesLocalUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 校准
 */
public class CalibrateBean {

    /**
     * identification : 33555485
     * type : 58
     * width : 200
     * sowings : [{"plant":25,"sow":65,"inlist":[{"id":33555485,"num":2,"type":3},{"id":33555486,"num":2,"type":3}]},{"plant":25,"sow":65,"inlist":[{"id":33555485,"num":2,"type":3},{"id":33555486,"num":2,"type":3}]}]
     */

    private int identification;
    private int type=58;
    private int width;
    private List<SowingsBean> sowings;

    public CalibrateBean(int identification, List<SowingsBean> sowings) {
        this.identification = identification;
        this.sowings = sowings;
    }

    public int getIdentification() {
        return identification;
    }

    public void setIdentification(int identification) {
        this.identification = identification;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public List<SowingsBean> getSowings() {
        return sowings;
    }

    public void setSowings(List<SowingsBean> sowings) {
        this.sowings = sowings;
    }

    @Override
    public String toString() {
        return "CalibrateBean{" +
                "identification=" + identification +
                ", type=" + type +
                ", width=" + width +
                ", sowings=" + sowings +
                '}';
    }

    public static class SowingsBean {
        /**
         * plant : 25
         * sow : 65
         * inlist : [{"id":33555485,"num":2,"type":3},{"id":33555486,"num":2,"type":3}]
         */

        private int plant= Integer.parseInt(PropertiesLocalUtil.INSTANCE.read(EnumProperties.GROW_WIDTH));//默认株距25
        private int sow=Integer.parseInt(PropertiesLocalUtil.INSTANCE.read(EnumProperties.ROW_WIDTH));//行距 65
        private List<InlistBean> inlist;

        public int getPlant() {
            return plant;
        }

        public void setPlant(int plant) {
            this.plant = plant;
        }

        public int getSow() {
            return sow;
        }

        public void setSow(int sow) {
            this.sow = sow;
        }

        public List<InlistBean> getInlist() {
            return inlist;
        }

        public void setInlist(List<InlistBean> inlist) {
            this.inlist = inlist;
        }
        public void addInlist(int id, int num, int type) {
            if (this.inlist == null) {
                this.inlist = new ArrayList<>();
            }
            this.inlist.add(new InlistBean(id, num, type));
        }

        @Override
        public String toString() {
            return "SowingsBean{" +
                    "plant=" + plant +
                    ", sow=" + sow +
                    ", inlist=" + inlist +
                    '}';
        }

        public static class InlistBean {
            /**
             * id : 33555485
             * num : 2
             * type : 3
             */

            private int id;
            private int num;
            private int type;

            @Override
            public String toString() {
                return "InlistBean{" +
                        "id=" + id +
                        ", num=" + num +
                        ", type=" + type +
                        '}';
            }

            public InlistBean(int id, int num, int type) {
                this.id = id;
                this.num = num;
                this.type = type;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
