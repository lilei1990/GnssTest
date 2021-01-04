package com.example.demo.utils;

import com.example.demo.model.EnumProperties;
import org.junit.Test;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 条形码工具类
 *
 * @author tangzz
 * @createDate 2015年9月17日
 *
 */
public class BarcodeUtil {

    /**
     * 生成文件
     *
     * @param msg
     * @param path
     * @return
     */
    public static File generateFile(String msg, String path) {
        File file = new File(path);
        try {
            generate(msg, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    /**
     * 生成字节
     *
     * @param msg
     * @return
     */
    public static byte[] generate(String msg) {
        ByteArrayOutputStream ous = new ByteArrayOutputStream();
        generate(msg, ous);
        return ous.toByteArray();
    }

    /**
     * 生成到流
     *
     * @param msg
     * @param ous
     */
    public static void generate(String msg, OutputStream ous) {

        if (msg==null || ous == null) {
            return;
        }

        Code39Bean bean38 = new Code39Bean();
        Code128Bean bean=new Code128Bean();
        // 精细度
        final int dpi = 150;
        // module宽度
        final double moduleWidth = UnitConv.in2mm(1.0f / dpi);

        // 配置对象
        bean.setModuleWidth(moduleWidth);
        //bean.setWideFactor(3);

        bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
        bean.doQuietZone(false);

        String format = "image/png";
        try {

            // 输出到流
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, format, dpi,
                    BufferedImage.TYPE_BYTE_BINARY, false, 0);

            // 生成条形码
            bean.generateBarcode(canvas, msg);

            // 结束绘制
            canvas.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     *
     * @param msg
     * @return

     */
    public static BufferedImage generateM(String msg) {

        if (msg==null ) {
            return null;
        }

        //Code39Bean bean38 = new Code39Bean();
        Code128Bean bean=new Code128Bean();
        // 精细度
        final int dpi = 150;
        // module宽度
        final double moduleWidth = UnitConv.in2mm(1.0f / dpi);

        // 配置对象
        bean.setModuleWidth(moduleWidth);
        //bean.setWideFactor(3);
        bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
        bean.doQuietZone(false);

        String format = "image/png";
        try {

            // 输出到流
            BitmapCanvasProvider canvas = new BitmapCanvasProvider( dpi,
                    BufferedImage.TYPE_BYTE_BINARY, false, 0);

            // 生成条形码
            bean.generateBarcode(canvas, msg);

            // 结束绘制
            canvas.finish();
            return canvas.getBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*public static void main(String[] args) {
        String msg = "HD-20200130-1-031";
//        String path = "barcode.png";
        String path = "barcode.png";
        generateFile(msg, path);
        System.out.println("条形码生成=="+generateFile(msg, path));
    }*/

    @Test
    /**
     * 2.5.1 小包装
     * 	1）扫描主机ID
     * 	2）生成两张贴纸，一张贴纸带终端编号及sim卡号，一张贴纸带小包装号和终端编号，见下图（6x3cm）
     */
    public  void test() {
        Graphics2D g2d;

        /*int mainWidth=168;
        int mainHeight=84;
        BufferedImage bufferedImageIn = new BufferedImage(mainWidth, mainHeight, BufferedImage.TYPE_INT_ARGB);

//72dpi 1厘米=28.346像素权

        int yStart=5;
        g2d = bufferedImageIn.createGraphics();
        g2d.setPaint(new Color(0,0,0));


        BufferedImage bufferedImage = BarcodeUtil.generateM("01202003041032");
        g2d.drawImage(bufferedImage,9,yStart,150,16,null);
        yStart+=22;
        centerText("01202003041032",mainWidth,yStart,6,g2d);
        yStart+=6;

        g2d.setFont(new Font("Default", Font.BOLD, 8));
        g2d.drawString("包裝内容:", 15, yStart);
        yStart+=10;
        g2d.setFont(new Font("Default", Font.BOLD, 6));
        g2d.drawString("主机*1，软件版本号V1.0.1", 18, yStart);
        yStart+=10;
        g2d.setFont(new Font("Default", Font.BOLD, 6));
        g2d.drawString("aaa*1，bbb*2,ccc*1:", 18, yStart);
        yStart+=14;

        g2d.setFont(new Font("Default", Font.BOLD, 6));
        g2d.drawString("主机:", 18, yStart);

        BufferedImage bufferedImagebottom = BarcodeUtil.generateM("10100400");
        g2d.drawImage(bufferedImagebottom,35,yStart-10,110,16,null);
        yStart+=12;
        centerText("10100400",mainWidth,yStart, 6,g2d);
*/



        int mainWidth=168;
        int mainHeight=84;
        BufferedImage bufferedImageIn = new BufferedImage(mainWidth, mainHeight, BufferedImage.TYPE_INT_ARGB);

        g2d = bufferedImageIn.createGraphics();
        g2d.setPaint(new Color(0,0,0));

        //72dpi 1厘米=28.346像素权
        String start=PropertiesLocalUtil.INSTANCE.read(EnumProperties.PRINT_START);
        int yStart=Integer.parseInt(start);
        g2d.setPaint(new Color(0,0,0));

        /*g2d.setFont(new Font("Default", Font.PLAIN, 8));
        g2d.drawString("1111SIZE=3  ", x, 0);*/


        g2d.setFont(new Font("fangsong", Font.BOLD, 9));
        //HG106A100 HD106A101

        g2d.drawString("设备型号:"+"deviceName", 15, yStart);
        yStart+=12;
        g2d.setFont(new Font("fangsong", Font.BOLD, 9));
        g2d.drawString("设备编号:"+"deviceId", 15, yStart);
        yStart+=12;
        g2d.setFont(new Font("fangsong", Font.BOLD, 9));
        g2d.drawString("生产日期:"+ TimeUtil.INSTANCE.getYMD(), 15, yStart);
        yStart+=12;
        g2d.setFont(new Font("fangsong", Font.BOLD, 9));
        g2d.drawString("SIM卡号:"+"imsi", 15, yStart);
        yStart+=5;


        BufferedImage bufferedImageSim = BarcodeUtil.generateM("123456789");
        g2d.drawImage(bufferedImageSim,19,yStart,130,28,null);


        g2d.dispose();
        //输出图片
        try {
            ImageIO.write(bufferedImageIn, "png", new FileOutputStream("smallpack1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    /**
     * 2.5.1 小包装
     * 	1）扫描主机ID
     * 	2）生成两张贴纸，一张贴纸带终端编号及sim卡号，一张贴纸带小包装号和终端编号，见下图（6x3cm）
     */
    public  void test2() {
        int mainWidth=168;
        int mainHeight=84;
        BufferedImage bufferedImageIn = new BufferedImage(mainWidth, mainHeight, BufferedImage.TYPE_INT_ARGB);

        //72dpi 1厘米=28.346像素权

        int yStart=5;
        Graphics2D g2d = bufferedImageIn.createGraphics();
        g2d.setPaint(new Color(0,0,0));

        /*g2d.setFont(new Font("Default", Font.PLAIN, 8));
        g2d.drawString("1111SIZE=3  ", x, 0);*/


        g2d.setFont(new Font("Default", Font.BOLD, 8));
        g2d.drawString("设备编号:", 15, yStart+5);
        yStart+=10;

        BufferedImage bufferedImage = BarcodeUtil.generateM("16100400");
        g2d.drawImage(bufferedImage,29,yStart,110,16,null);
        yStart+=22;
        centerText("16100400",mainWidth,yStart,6,g2d);
        yStart+=6;

        g2d.setFont(new Font("Default", Font.BOLD, 8));
        g2d.drawString("SIM卡号:", 15, yStart+5);
        yStart+=10;

        BufferedImage bufferedImageSim = BarcodeUtil.generateM("460047950405102");
        g2d.drawImage(bufferedImageSim,19,yStart,130,16,null);
        yStart+=22;
        centerText("460047950405102",mainWidth,yStart,6,g2d);



      /*  g2d.setFont(new Font("Default", Font.BOLD, 6));
        g2d.drawString("主机*1，软件版本号V1.0.1", 18, yStart);
        yStart+=10;
        g2d.setFont(new Font("Default", Font.BOLD, 6));
        g2d.drawString("aaa*1，bbb*2,ccc*1:", 18, yStart);
        yStart+=14;

        g2d.setFont(new Font("Default", Font.BOLD, 6));
        g2d.drawString("主机:", 18, yStart);

        BufferedImage bufferedImagebottom = BarcodeUtil.generateM("10100400");
        g2d.drawImage(bufferedImagebottom,35,yStart-10,110,15,null);
        yStart+=12;
        centerText("10100400",mainWidth,yStart, 6,g2d);*/

        g2d.dispose();
        //输出图片
        try {
            ImageIO.write(bufferedImageIn, "png", new FileOutputStream("smallpack2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 1）整机测试时，需要写入整机ID。
     * 2）测试结果需要上报服务器，测试结果不包括LED测试项。
     * 3）测试通过则将终端ID、ISSID、IMEI号上报服务器
     * 4）测试通过需要打印设备贴纸,见下图(6X3CM)
     */
    @Test
    public  void getTestTotal() {
        int mainWidth=168;
        int mainHeight=84;
        BufferedImage bufferedImageIn = new BufferedImage(mainWidth, mainHeight, BufferedImage.TYPE_INT_ARGB);

        //72dpi 1厘米=28.346像素权

        int yStart=15;
        Graphics2D g2d = bufferedImageIn.createGraphics();
        g2d.setPaint(new Color(0,0,0));

        /*g2d.setFont(new Font("Default", Font.PLAIN, 8));
        g2d.drawString("1111SIZE=3  ", x, 0);*/


        g2d.setFont(new Font("Default", Font.BOLD, 9));
        g2d.drawString("设备型号:HD106A100", 15, yStart);
        yStart+=12;
        g2d.setFont(new Font("Default", Font.BOLD, 9));
        g2d.drawString("设备编号:HD106A100", 15, yStart);
        yStart+=12;
        g2d.setFont(new Font("Default", Font.BOLD, 9));
        g2d.drawString("生产日期:2020年03月04日", 15, yStart);
        yStart+=12;
        g2d.setFont(new Font("Default", Font.BOLD, 9));
        g2d.drawString("SIM卡号:460047950405011", 15, yStart);
        yStart+=5;


        BufferedImage bufferedImageSim = BarcodeUtil.generateM("460047950405102");
        g2d.drawImage(bufferedImageSim,19,yStart,130,20,null);


        g2d.dispose();
        //输出图片
        try {
            ImageIO.write(bufferedImageIn, "png", new FileOutputStream("totaltest.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    /**
     * 2.5.2 大包装
     * 	1）扫描主机ID，一个大包装支持ID≤20
     * 	2）生成大包装贴纸，见下图（100x100cm）
     * 	3）将对应大包装码所带的终端信息ID信息上传至服务器
     */
    public  void test4() {
        int mainWidth=280;
        int mainHeight=280;
        BufferedImage bufferedImageIn = new BufferedImage(mainWidth, mainHeight, BufferedImage.TYPE_INT_ARGB);

        //72dpi 1厘米=28.346像素权

        int yStart=15;
        Graphics2D g2d = bufferedImageIn.createGraphics();
        g2d.setPaint(new Color(0,0,0));
        Font font = new Font("Default", Font.PLAIN, 6);
        g2d.setFont(font);
        //g2d.drawString("1111SIZE=3  ", x, 0);

        BufferedImage bufferedImageSim = BarcodeUtil.generateM("HD202003181031");
        g2d.drawImage(bufferedImageSim,20,yStart,240,20,null);
        yStart+=25;
        centerText("HD-20200318-1-031",mainWidth,yStart,6,g2d);
        yStart+=15;

        BufferedImage bufferedImageSmall = BarcodeUtil.generateM("16200400");
        int subY=yStart;
        for (int i = 0; i < 10; i++) {
            g2d.drawImage(bufferedImageSmall,20,yStart,70,7,null);
            yStart+=14;

            FontMetrics fm = g2d.getFontMetrics(font);
            String text = "16200400";
            int textWidth = fm.stringWidth(text);
            g2d.drawString("16200400", (70+40-textWidth)/2, yStart);
            yStart+=4;
        }

        yStart=subY;
        for (int i = 0; i < 10; i++) {
            g2d.drawImage(bufferedImageSmall,280-20-70,yStart,70,7,null);
            yStart+=14;

            FontMetrics fm = g2d.getFontMetrics(font);
            String text = "16200400";
            int textWidth = fm.stringWidth(text);

            g2d.drawString("16200400", 280-20-(70-textWidth)/2-textWidth, yStart);
            yStart+=4;
        }




        g2d.dispose();
        //输出图片
        try {
            ImageIO.write(bufferedImageIn, "png", new FileOutputStream("packbig.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    /**
     * 打印软件两个字
     */
    public  void test5() {
        int mainWidth=280;
        int mainHeight=280;
        BufferedImage bufferedImageIn = new BufferedImage(mainWidth, mainHeight, BufferedImage.TYPE_INT_ARGB);

        //72dpi 1厘米=28.346像素权

        int yStart=120;
        Graphics2D g2d = bufferedImageIn.createGraphics();
        g2d.setPaint(new Color(0,0,0));

        //g2d.drawString("1111SIZE=3  ", x, 0);

        centerText("软件",mainWidth,yStart,120,g2d);
        centerText("软件",mainWidth,240,120,g2d);



        g2d.dispose();
        //输出图片
        try {
            ImageIO.write(bufferedImageIn, "png", new FileOutputStream("software.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * 打印软件两个字
     */
    public  void test6() {
        int mainWidth=168;
        int mainHeight=84;
        BufferedImage bufferedImageIn = new BufferedImage(mainWidth, mainHeight, BufferedImage.TYPE_INT_ARGB);

        //72dpi 1厘米=28.346像素权

        Graphics2D g2d = bufferedImageIn.createGraphics();



        //72dpi 1厘米=28.346像素权
        String start= PropertiesLocalUtil.INSTANCE.read(EnumProperties.PRINT_START);
        int yStart=Integer.parseInt(start);
        g2d.setPaint(new Color(0,0,0));

        /*g2d.setFont(new Font("Default", Font.PLAIN, 8));
        g2d.drawString("1111SIZE=3  ", x, 0);*/
        String deviceId=Long.parseLong("16200001",16)+"";

        //HG106A100 HD106A101
        BarcodeUtil.centerText("北斗定位智能车载终端", mainWidth, yStart, 14, g2d);
        yStart+=18;
        BarcodeUtil.centerText("fangsong","Model：HD106A100", mainWidth, yStart, 10, g2d);
        yStart+=4;
        BufferedImage bufferedImageSim = BarcodeUtil.generateM(deviceId);
        g2d.drawImage(bufferedImageSim,19,yStart,130,24,null);
        yStart+=36;
        BarcodeUtil.centerText("fangsong",deviceId, mainWidth, yStart, 14, g2d);



        g2d.dispose();
        //输出图片
        try {
            ImageIO.write(bufferedImageIn, "png", new FileOutputStream("wodo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    /**
     * 打印大包装
     */
    public  void test7() {
        int mainWidth=280;
        int mainHeight=280;
        BufferedImage bufferedImageIn = new BufferedImage(mainWidth, mainHeight, BufferedImage.TYPE_INT_ARGB);

        //72dpi 1厘米=28.346像素权

        Graphics2D g2d = bufferedImageIn.createGraphics();

        int yStart=15;
        int xStart=15;
        g2d.setPaint(new Color(0,0,0));
        Font font = new Font("Default", Font.PLAIN, 6);
        g2d.setFont(font);
        //g2d.drawString("1111SIZE=3  ", x, 0);
        String serialId = PropertiesLocalUtil.INSTANCE.readSerialId();
        BufferedImage bufferedImageSim = BarcodeUtil.generateM(serialId);
        g2d.drawImage(bufferedImageSim,20,yStart,240,28,null);
        yStart+=35;
        centerText(serialId,mainWidth,yStart,6,g2d);
        yStart+=10;
        leftTextFangsong("包装内容：",xStart,yStart,12,g2d,true);
        yStart += 18;
        String mainText = "I601C整机*1  , 定位天线 *1  , 电源线 *1 , 喇叭* 1 ,  CAN数据线6米 *1 , 传感器SD100-A*4 , 控制器-A型*2 ,  CAN一分二数据线 *1 , 其他附件*1";
        mainText = StringUtil.genSplitStr(mainText, 50);
        for (String s : mainText.split("\n")) {
            leftTextFangsong(s,xStart,yStart,11,g2d,false);
            yStart += 11;
        }
        yStart = 130;
        leftTextFangsong("产品名称:",xStart,yStart,10,g2d,true);
        leftTextFangsong("智能播种监控系统",xStart+50,yStart,10,g2d,false);
        leftTextFangsong("产品型号:",xStart+140,yStart,10,g2d,true);
        leftTextFangsong("106C-102",xStart+190,yStart,10,g2d,false);
        yStart += 14;
        leftTextFangsong("终端软件版本:",xStart,yStart,10,g2d,true);
        leftTextFangsong("601C.093.12",xStart+90,yStart,10,g2d,false);
        yStart += 14;
        leftTextFangsong("控制器软件版本:",xStart,yStart,10,g2d,true);
        leftTextFangsong("v1.3.1.5",xStart+90,yStart,10,g2d,false);
       /* leftTextFangsong("控制器软件版本:",xStart+140,yStart,10,g2d,true);
        leftTextFangsong("v1.3.1.5",xStart+210,yStart,10,g2d,false);*/
        yStart += 14;
        leftTextFangsong("主机：",xStart,yStart,12,g2d,true);

        String deviceIdTer = "100000001";
        BufferedImage bufferedImageTer = BarcodeUtil.generateM(deviceIdTer);
        g2d.drawImage(bufferedImageSim,60,yStart-12,120,26,null);
        yStart += 22;
        centerText(deviceIdTer,mainWidth,yStart,6,g2d);
        yStart += 16;
        leftTextFangsong("控制器：",xStart,yStart,12,g2d,true);


        BufferedImage bufferedImageCtrl = BarcodeUtil.generateM("10000001");
        g2d.drawImage(bufferedImageCtrl,60,yStart-12,120,28,null);
        yStart += 22;
        centerText(deviceIdTer,mainWidth,yStart,6,g2d);
        yStart += 16;
        g2d.drawImage(bufferedImageCtrl, 60, yStart - 12, 120, 28, null);
        yStart += 22;
        centerText(deviceIdTer,mainWidth,yStart,6,g2d);
        g2d.dispose();
        //输出图片
//        try {
//            ImageIO.write(bufferedImageIn, "png", new FileOutputStream("bigpack.png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static void centerText(String text,int mainWidth,int y,int size ,Graphics2D g2d) {
        Font font = new Font("微软雅黑", Font.PLAIN, size);
        g2d.setFont(font);
        // 计算文字长度，计算居中的x点坐标
        FontMetrics fm = g2d.getFontMetrics(font);
        int textWidth = fm.stringWidth(text);
        g2d.drawString(text,  (mainWidth - textWidth) / 2, y);
    }
    public static void leftTextFangsong(String text, int xStart, int y, int size , Graphics2D g2d, boolean isBlod) {

        Font font =  new Font("微软雅黑", Font.PLAIN, size);
        if (isBlod) {
            font =  new Font("微软雅黑", Font.BOLD, size);
        }
        g2d.setFont(font);
        g2d.drawString(text, xStart, y);
    }
    public static void leftTextDefault(String text,int xStart,int y,int size ,Graphics2D g2d,boolean isBlod) {

        Font font =  new Font("微软雅黑", Font.PLAIN, size);
        if (isBlod) {
            font =  new Font("微软雅黑", Font.BOLD, size);
        }
        g2d.setFont(font);
        g2d.drawString(text, xStart, y);
    }

    public static void centerText(String fontName,String text,int mainWidth,int y,int size ,Graphics2D g2d) {
        Font font = new Font(fontName, Font.BOLD, size);
        g2d.setFont(font);
        // 计算文字长度，计算居中的x点坐标
        FontMetrics fm = g2d.getFontMetrics(font);
        int textWidth = fm.stringWidth(text);
        g2d.drawString(text,  (mainWidth - textWidth) / 2, y);
    }



    public static void genPackSmall_1() {

    }
}
