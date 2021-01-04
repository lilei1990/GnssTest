package com.example.demo.view.test.print;

import javax.print.PrintService;
import java.awt.*;
import java.awt.print.*;

/**
 * 1）整机测试时，需要写入整机ID。
 * 2）测试结果需要上报服务器，测试结果不包括LED测试项。
 * 3）测试通过则将终端ID、ISSID、IMEI号上报服务器
 * 4）测试通过需要打印设备贴纸,见下图(6X3CM)
 */
public class PrinterTest implements Printable {
    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        if (page > 0) {
            return NO_SUCH_PAGE;
        }



        int mainWidth=280;
        int mainHeight=280;

        int yStart=120;


        //g2d.drawString("1111SIZE=3  ", x, 0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(new Color(0,0,0));
        centerText("软件",mainWidth,yStart,120,g2d);
        centerText("软件",mainWidth,240,120,g2d);




        return PAGE_EXISTS;
    }
    public static void centerText(String text,int mainWidth,int y,int size ,Graphics2D g2d) {
        Font font = new Font("Default", Font.PLAIN, size);
        g2d.setFont(font);
        // 计算文字长度，计算居中的x点坐标
        FontMetrics fm = g2d.getFontMetrics(font);
        int textWidth = fm.stringWidth(text);
        g2d.drawString(text,  (mainWidth - textWidth) / 2, y);
    }

    public static void main(String[] args) {
        //int height = 188 + 3 * 15 + 20;
        int height = 188;
        // 通俗理解就是书、文档
        Book book = new Book();
        // 打印格式
        PageFormat pf = new PageFormat();
        pf.setOrientation(PageFormat.PORTRAIT);
        // 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
        Paper p = new Paper();
        p.setSize(280, 280);
        p.setImageableArea(0, 0, 280, 280);
        pf.setPaper(p);
        // 把 PageFormat 和 Printable 添加到书中，组成一个页面
        book.append(new PrinterTest(), pf);
        // 获取打印服务对象
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(book);

        try {
            job.print();
        } catch (PrinterException e) {
            System.out.println("================打印出现异常");
        }
    }

    public  void printerName() {
        String printerName = "BTP-2300E";
        if (printerName != null) {
            // 查找并设置打印机
            //获得本台电脑连接的所有打印机
            PrintService[] printServices = PrinterJob.lookupPrintServices();
            if (printServices == null || printServices.length == 0) {
                System.out.print("打印失败，未找到可用打印机，请检查。");
                return;
            }
            PrintService printService = null;
            //匹配指定打印机
            for (int i = 0; i < printServices.length; i++) {

                if (printServices[i].getName().contains(printerName)) {
                    System.out.println(printServices[i].getName());
                    return;
                }
            }

            System.out.print("打印失败，未找到名称为" + printerName + "的打印机，请检查。");
            return;


        }
    }
}
