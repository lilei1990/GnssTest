package com.example.demo.view;

import com.example.demo.view.test.bean.IniFileEntity;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者 : lei
 * 时间 : 2020/12/19.
 * 邮箱 :416587959@qq.com
 * 描述 :这是个配置文件操作类，用来读取和设置ini配置文件
 */

/**
 * 存储文件中的内容
 */


public class Ini4jUtils {

    //我把这个写在了工具类里面（Ini4jUtils）
    public static boolean creatIniFile(String filePath, List<IniFileEntity> filecontent) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            return false;
        }
        File fileParent = file.getParentFile();
        fileParent.mkdirs();
        file.createNewFile();
        Ini ini = new Ini();
        ini.load(file);

        //将文件内容保存到ini对象中
        filecontent.forEach((entity) -> {
            ini.add(entity.getSection(), entity.getKey(), entity.getValue() == null ? "" : entity.getValue());
        });
        //将文件内容保存到文件中
        ini.store(file);
        return true;
    }

    /**
     * 读取ini文件的内容
     *
     * @param iniFile     ini文件
     * @param fileContent ini文件中的key对应文件中的section，value对应i你文件section下的一个或多个key值
     * @return
     * @throws IOException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Ini readIniFile(File iniFile, Map<String, List<String>> fileContent) throws IOException, NoSuchFieldException, IllegalAccessException {
        Ini ini = new Ini();
         ini.load(iniFile);
        return ini;

    }

    /**
     * 修改文件内容
     *
     * @param iniFile    ini文件
     * @param updateData 更新的数据
     * @throws IOException
     */
    public static void updateIniFile(File iniFile, Map<String, Map<String, String>> updateData) throws IOException {
        Ini ini = new Ini();
        ini.load(iniFile);

        Profile.Section section = null;
        Map<String, String> dataMap = null;
        for (String sect : updateData.keySet()) {
            section = ini.get(sect);
            dataMap = updateData.get(sect);
            for (String key : dataMap.keySet()) {
                section.put(key, dataMap.get(key) == null ? "" :
                        dataMap.get(key));
            }
        }
        ini.store(iniFile);
    }

    /**
     * 修改文件内容 删除原来的文件,重新生成
     *
     * @throws IOException
     */
    public static void updateIniFile(String filePath, List<IniFileEntity> filecontent) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        creatIniFile(filePath, filecontent);


    }

    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException, IOException {
        testReadFile();
    }

    //测试

    public static void testReadFile() throws IllegalAccessException, NoSuchFieldException, IOException {
        List<IniFileEntity> list = Arrays.asList(new IniFileEntity("ldap", "ip", "1.1.1.1222"),
                new IniFileEntity("ldap", "ipPort", "856722333"),
                new IniFileEntity("test", "isUsed", "true222222"));
//        System.out.println(Ini4jUtils.creatIniFile("D:\\test.ini",list));
//        //修改
        Ini4jUtils.updateIniFile("D:\\test.ini", list);
//
//
//        Map<String,List<String>> fileContent = new HashMap<>();
//        fileContent.put("ldap",Arrays.asList("ip","ipPort"));
//        fileContent.put("test",Arrays.asList("isUsed"));
//        Ini4jFileVo fileVo = Ini4jUtils.readIniFile(file,fileContent);
//        System.out.println(fileVo);
        File file = new File("D:\\test.ini");
        Map<String, List<String>> fileContent = new HashMap<>();
        fileContent.put("ldap", Arrays.asList("ip", "ipPort"));
        fileContent.put("test", Arrays.asList("isUsed"));
//        Ini4jFileVo fileVo = Ini4jUtils.readIniFile(file, fileContent);
//        System.out.println(fileVo);
    }
}