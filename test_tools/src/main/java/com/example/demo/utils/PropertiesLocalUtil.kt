package com.example.demo.utils

import com.example.demo.model.EnumProperties
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

object PropertiesLocalUtil {

    val name = "C:\\logs/${EnumProperties.COMPANY.defaultString}/user.properties"



    fun write(map: MutableMap<String,String>) {
        if (map.isNotEmpty()) {
            var prop = Properties()

            var oFile: FileOutputStream = FileOutputStream(name, false)
            map.forEach{
                prop.setProperty(it.key, it.value);
            }


            prop.store(oFile, null);
            oFile.close()
        }

    }


    fun write(k:String,v:String) {
        val readAll = readAll()
        readAll.set(k,v)
        if (readAll.isNotEmpty()) {
            var prop = Properties()

            var oFile: FileOutputStream = FileOutputStream(name, false)
            readAll.forEach{
                prop.setProperty(it.key, it.value);
            }


            prop.store(oFile, null);
//            prop.store(  OutputStreamWriter(oFile, "UTF-8"), null);
            oFile.close()
        }

    }
    fun write(k:EnumProperties,v:String) {
        val readAll = readAll()
        readAll.set(k.name,v)
        if (readAll.isNotEmpty()) {
            var prop = Properties()
            var file=File(name)
            file.getParentFile()
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }


            var oFile: FileOutputStream = FileOutputStream(name, false)
            readAll.forEach{
                prop.setProperty(it.key, it.value);
            }


            prop.store(oFile, null);
//            prop.store(  OutputStreamWriter(oFile, "UTF-8"), null);
            oFile.close()
        }

    }
    fun read(key: String): String? {
        var prop: Properties = Properties()

        if (File(name).exists()) {
            var inputStream = BufferedInputStream(FileInputStream(name))
            prop.load(inputStream);
            var it = prop.stringPropertyNames().iterator()
            while (it.hasNext()) {
                var keyQ = it.next();
                if (key.equals(keyQ)) {
                    return prop.getProperty(key)
                }
            }
            inputStream.close();
        }

        return null
    }
    fun read(enum: EnumProperties): String {
        var prop: Properties = Properties()

        if (File(name).exists()) {
            var inputStream = BufferedInputStream(FileInputStream(name))
            prop.load(inputStream);
            var it = prop.stringPropertyNames().iterator()
            while (it.hasNext()) {
                var keyQ = it.next();
                if (enum.name.equals(keyQ)) {
                    return prop.getProperty(enum.name)
                }
            }
            inputStream.close();
        }

        return enum.defaultString
    }
    fun readBoolean(enum: EnumProperties): Boolean {
        var prop: Properties = Properties()

        if (File(name).exists()) {
            var inputStream = BufferedInputStream(FileInputStream(name))
            prop.load(inputStream);
            var it = prop.stringPropertyNames().iterator()
            while (it.hasNext()) {
                var keyQ = it.next();
                if (enum.name.equals(keyQ)) {
                    return prop.getProperty(enum.name).let {
                        it.equals("true")
                    }
                }
            }
            inputStream.close();
        }

        return enum.defaultBoolean
    }
    fun readAll():  MutableMap<String,String>{
        var map= mutableMapOf<String,String>()
        var prop: Properties = Properties()

        if (File(name).exists()) {
            var inputStream = BufferedInputStream(FileInputStream(name));
            prop.load(inputStream);
            var it = prop.stringPropertyNames().iterator()
            while (it.hasNext()) {
                var key = it.next();
                    map.set(key,prop.getProperty(key))
            }
            inputStream.close();
        }

        return map
    }

    fun readSerialId(): String {
        var serialNo=read(EnumProperties.SERIAL_NO).toLong()
        var serialDate=read(EnumProperties.SERIAL_DATE)
        if (serialDate.equals("0")) {
            serialDate = TimeUtil.getYYYYMMDD()
        }
        if (serialDate.equals(TimeUtil.getYYYYMMDD())) {
            //当天时间 流水号自增
        } else {
            //隔天流水号重置
            serialDate = TimeUtil.getYYYYMMDD()
            serialNo=1
        }
        var serialId="106C-${serialDate}-1-${String.format("%05d",serialNo)}"
        //write(EnumProperties.SERIAL_NO,serialNo.toString())
        write(EnumProperties.SERIAL_DATE,serialDate)
        return serialId
    }
    fun getSerialId(): String {
        var serialNo=read(EnumProperties.SERIAL_NO).toLong()
        var serialDate=read(EnumProperties.SERIAL_DATE)
        if (serialDate.equals("0")) {
            serialDate = TimeUtil.getYYYYMMDD()
        }
        if (serialDate.equals(TimeUtil.getYYYYMMDD())) {
            //当天时间 流水号自增
            serialNo = serialNo + 1
        } else {
            //隔天流水号重置
            serialDate = TimeUtil.getYYYYMMDD()
            serialNo=1
        }
        var serialId="106C-${serialDate}-1-${String.format("%05d",serialNo)}"
        write(EnumProperties.SERIAL_NO,serialNo.toString())
        write(EnumProperties.SERIAL_DATE,serialDate)
        return serialId
    }
}