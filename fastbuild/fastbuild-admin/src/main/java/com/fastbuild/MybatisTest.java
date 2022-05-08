package com.fastbuild;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.fastbuild.common.utils.sign.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.executor.result.DefaultResultHandler;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MybatisTest {

    public static void main (String[] args) throws Exception {

        String str = "c:/etc/abcd/EFGH123467850/wer/wer/v/1234/fjker@#$.xml";
//        char[] chars = str.toCharArray();
//        for (int i = 0; i < chars.length; i++) {
//            chars[i] = (char) (chars[i] << 3);
//        }
//
//        System.out.println(new String(chars));
//
//        for (int i = 0; i < chars.length; i++) {
//            chars[i] = (char) (chars[i] >> 3);
//        }
//
//        System.out.println(new String(chars));

        String encode = Base64.encode(str.getBytes());
        System.out.println(encode);
        System.out.println(new String(Base64.decode(encode)));

    }


}
