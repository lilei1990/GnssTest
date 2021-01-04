package com.example.demo.view.test.print;



import com.example.demo.model.EnumProperties;
import com.example.demo.utils.BarcodeUtil;
import com.example.demo.utils.PropertiesLocalUtil;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;

/**
 * 沃得品牌
 * 	1）扫描主机ID
 * 	2）生成五张贴纸，带沃得特需的终端编号（16进制转10进制）及其条形码（6X3cm）
 */
public class PrinterWoDe implements Printable {
    String deviceName;String deviceId;
    public PrinterWoDe(String deviceName, String deviceId) {
        //HG100A HD106A101
        //HG100B HD106A100
        char ch=deviceId.charAt(2);
        int num=0;
        if (Character.isDigit(ch)){  // 判断是否是数字
            num = Integer.parseInt(String.valueOf(ch));
        }

        if (deviceName.equals("HG")) {
            //4G设备型号：HG100A;2G设备型号：HG100B
            if (num == 1) {//P1 2G
                this.deviceName = "HG100B";
            } else {//P2 4G
                this.deviceName = "HG100A";
            }

        } else {
            this.deviceName = "HD106A101";
        }

        this.deviceId=Long.parseLong(deviceId,16)+"";

    }

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        if (page > 0) {
            return NO_SUCH_PAGE;
        }






        int mainWidth=168;
        int mainHeight=84;

        //72dpi 1厘米=28.346像素权
        String start= PropertiesLocalUtil.INSTANCE.read(EnumProperties.PRINT_START);
        int yStart=Integer.parseInt(start);
        Graphics2D g2d  =(Graphics2D) g;
        g2d.setPaint(new Color(0,0,0));

        /*g2d.setFont(new Font("Default", Font.PLAIN, 8));
        g2d.drawString("1111SIZE=3  ", x, 0);*/


        //HG106A100 HD106A101
        String deviceId=Long.parseLong("16200001",16)+"";

        //HG106A100 HD106A101
        BarcodeUtil.centerText("default","北斗定位智能车载终端", mainWidth, yStart, 15, g2d);

        yStart+=18;
        BarcodeUtil.centerText("fangsong","Model：HD106A100", mainWidth, yStart, 10, g2d);
        yStart+=4;
        BufferedImage bufferedImageSim = BarcodeUtil.generateM(deviceId);
        g2d.drawImage(bufferedImageSim,19,yStart,130,24,null);
        yStart+=36;
        BarcodeUtil.centerText("fangsong",deviceId, mainWidth, yStart, 14, g2d);








        return PAGE_EXISTS;
    }

    /**
     *
     * @param deviceName 设备型号
     * @param deviceId 设备编号
     */
    public static void printTotal(String deviceName,String deviceId) {
        //int height = 188 + 3 * 15 + 20;

        // 通俗理解就是书、文档
        Book book = new Book();
        // 打印格式
        PageFormat pf = new PageFormat();
        pf.setOrientation(PageFormat.PORTRAIT);
        // 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
        Paper p = new Paper();
        p.setSize(168, 90);
        p.setImageableArea(0, 0, 168, 90);
        pf.setPaper(p);
        // 把 PageFormat 和 Printable 添加到书中，组成一个页面
        //for (int i = 0; i < 5; i++) {
            book.append(new PrinterWoDe(deviceName,deviceId), pf);
        //}


        // 设置打印参数
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(new Copies(5)); //份数
        //aset.add(MediaSize.ISO.A4); //纸张
        // aset.add(Finishings.STAPLE);//装订
        //aset.add(Sides.DUPLEX);//单双面


        // 获取打印服务对象
        PrinterJob job = PrinterJob.getPrinterJob();

        job.setPageable(book);


        try {
            job.print(aset);
        } catch (PrinterException e) {
            System.out.println("================打印出现异常");
        }
    }

    public static void main(String[] args) {
        PrinterWoDe.printTotal("HD","16200001");
        //test();
    }

    public static void test() {
        String deviceId = "162";
        char ch=deviceId.charAt(2);
        if (Character.isDigit(ch)){  // 判断是否是数字
            int num = Integer.parseInt(String.valueOf(ch));
            System.out.println(num);
            System.out.println(num==2);
        }

    }
}
