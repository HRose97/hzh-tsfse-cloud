package com.hzh.test.mybatis;

import javax.xml.bind.annotation.XmlElement;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过实体类生成建表语句
 */
public class GenerateSqlFromEntity {
    //
    public static Map<String, String> javaProperty2SqlColumnMap = new HashMap<>();

    static {
        javaProperty2SqlColumnMap.put("Integer", "INTEGER");
        javaProperty2SqlColumnMap.put("Short", "tinyint");
        javaProperty2SqlColumnMap.put("Long", "bigint");
        javaProperty2SqlColumnMap.put("BigDecimal", "decimal(19,2)");
        javaProperty2SqlColumnMap.put("Double", "double precision not null");
        javaProperty2SqlColumnMap.put("Float", "float");
        javaProperty2SqlColumnMap.put("Boolean", "bit");
        javaProperty2SqlColumnMap.put("Timestamp", "datetime");
        javaProperty2SqlColumnMap.put("String", "VARCHAR(255)");
    }


    // main
    public static void main( String[] args ) throws IOException {
        String tableName = "test_user";
        String path = "F:/article/newbee-mall/newbee/test.sql";
        String re = createTable( Testpojo.class, tableName );
        StringToSql(re,path);
        writeInFile(re,path);
        System.out.println("生成成功：" +  re );
    }

    //
    public static String createTable( Class obj, String tableName ) throws IOException {
        Field[] fields = null;
        fields = obj.getDeclaredFields();
        String param = null;
        String column = null;
        Class annotationType = null;
        XmlElement xmlElement = null;

        StringBuilder stb = null;
        stb = new StringBuilder(50);

        if (tableName == null || tableName.equals("")) {
            // 未传表明默认用类名
            tableName = obj.getName();
            tableName = tableName.substring(tableName.lastIndexOf(".") + 1);
        }

        stb.append("create table ").append(tableName).append(" (\r\n");

        //System.out.println( tableName );

        boolean firstId = true;

        for ( Field f : fields ) {
            column = f.getName();

            if (column.equals("serialVersionUID")) {
                continue;
            }

            param = f.getType().getSimpleName();
            stb.append( "    " + column );     // 一般第一个是主键

            stb.append(" ");
            stb.append( javaProperty2SqlColumnMap.get(param) );

            if (firstId) {//类型转换
                stb.append(" PRIMARY KEY");
                firstId = false;
            }
            // 获取字段中包含fieldMeta的注解
            // 2、获取属性上的所有注释
            //@XmlElement(name = "注解")
            Annotation[] allAnnotations = f.getAnnotations();
            for(Annotation an : allAnnotations){
                stb.append(" COMMIT '");
                xmlElement = (XmlElement)an;
                annotationType = an.annotationType();
                param = ((XmlElement) an).name();
                System.out.println("属性 "+f.getName()+"-----的注释类型有: " + param);
                stb.append(param).append("'");
            }
            stb.append(",\r\n");
        }

        String sql = null;
        sql = stb.toString();

        // 去掉最后一个逗号
        int lastIndex = sql.lastIndexOf(",");
        sql = sql.substring(0, lastIndex) + sql.substring(lastIndex + 1);
        sql = sql.substring(0, sql.length() - 1)
                + ") ENGINE = INNODB DEFAULT  CHARSET= utf8;\r\n";

        return sql;
    }

    /**
     * 将string 写入sql文件
     * @author
     * @date	2018年4月11日
     * @param
     * @param
     */
    public static void StringToSql(String re,String path){
        //byte[] sourceByte = str.getBytes();
        try {
            File file = new File(path);     //文件路径（路径+文件名）
            if (!file.exists()) {   //文件不存在则创建文件，先创建目录
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(file);    //文件输出流用于将数据写入文件
            outStream.flush();
            outStream.close();  //关闭文件输出流
            BufferedWriter out = new BufferedWriter(new FileWriter(path));
            out.write(re);
            System.out.println("生成成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //测试
    public static void writeInFile(String re,String path) throws IOException {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(path, true));
            out.write(re);
            System.out.println("write success.");
        } catch (IOException e) {

        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}
