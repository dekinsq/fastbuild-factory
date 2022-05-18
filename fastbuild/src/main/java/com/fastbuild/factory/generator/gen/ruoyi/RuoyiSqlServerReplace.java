package com.fastbuild.factory.generator.gen.ruoyi;

import com.fastbuild.factory.generator.common.FactoryConst;
import com.fastbuild.factory.generator.domain.AppConfig;
import com.fastbuild.factory.generator.gen.AbstractFormat;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuoyiSqlServerReplace extends AbstractFormat {

    private final String GEN_ID = "ruoyi#sqlserver";

    public RuoyiSqlServerReplace(AppConfig app) {
        super(app);
    }

    @Override
    protected String getGenId() {
        return GEN_ID;
    }

    @Override
    protected boolean validate() {
        return FactoryConst.db.SQL_SERVER.equals(project.getDatabase())
                && FactoryConst.db.ORACLE.equals(project.getDatabase())
                && !FactoryConst.web.THYMELEAF.equals(project.getWebFramework());
    }

    @Override
    protected void dependency() {}

    @Override
    protected void fileGenerator() throws Exception {
        File root = new File(project.getWorkPath() + File.separator + project.getProjectName());
        String[] suffix = new String[]{ "xml", "yml" };
        List<File> fileList = (List<File>) FileUtils.listFiles(root, suffix, true);
        for (File file : fileList) {
            if (file.getName().contains("Mapper.xml")) {
                String fileContent = FileUtils.readFileToString(file);
                Map<String, String> sqlElementList = this.getSqlElementList(fileContent);
                fileContent = this.sqlFormatTopOne(fileContent, sqlElementList);
                fileContent = this.genTableColumnMapper(fileContent, file.getName());
                fileContent = this.genTableMapper(fileContent, file.getName());
                fileContent = this.mapper(fileContent);
                FileUtils.writeStringToFile(file, fileContent);
            } else if ("pom.xml".equals(file.getName())) {
                String fileContent = FileUtils.readFileToString(file);
                fileContent = this.pom(fileContent);
                FileUtils.writeStringToFile(file, fileContent);
            } else if (file.getName().endsWith("yml")) {
                String fileContent = FileUtils.readFileToString(file);
                fileContent = this.yml(fileContent, file.getName());
                FileUtils.writeStringToFile(file, fileContent);
            }
        }
    }

    private Map<String, String> getSqlElementList(String fileContent) {
        Map<String, String> sqlElementList = new HashMap<>();

        Pattern p = Pattern.compile("(<sql[^>])([\\s\\S]*?)</sql>");
        Matcher m = p.matcher(fileContent);
        while (m.find()) {
            String sql = m.group();
            String key = sql.substring(sql.indexOf("id=\"") + 4, sql.indexOf("\">"));
            sqlElementList.put(key, sql.substring(sql.indexOf(">") + 1, sql.indexOf("</")));
        }

        return sqlElementList;
    }

    private String mapper(String fileContent) throws Exception {
        return fileContent.replaceAll("sysdate\\(\\)", "getdate()")
                .replaceAll("`query`", "query")
                .replaceAll("ifnull\\(", "isnull(")
                .replaceAll("find_in_set\\(", "CHARINDEX(cast(")
                .replaceAll(", ancestors\\)", " as nvarchar),ancestors)>0")
                .replaceAll("date_format\\(", "CONVERT(varchar(100), ")
                .replaceAll(",'%y%m%d'\\)", ", 112)");
    }

    private String sqlFormatTopOne(String fileContent, Map<String, String> sqlElementList) {
        Pattern p = Pattern.compile("(<select[^>])([\\s\\S]*?)</select>");
        Matcher m = p.matcher(fileContent);
        while (m.find()) {
            String sql = m.group();
            if (sql.indexOf(" limit ") > -1) {
                if (sql.indexOf("checkPhoneUnique") > -1 || sql.indexOf("checkEmailUnique") > -1) {
                    fileContent = fileContent.replace(sql, sql.replace("\tselect", "\tselect top 1").replace("limit 1", ""));
                } else {
                    Set<String> keys = sqlElementList.keySet();
                    boolean hasInclude = false;
                    for (String key : keys) {
                        String includeSql = "<include refid=\"" + key + "\"/>";
                        if (sql.indexOf(includeSql) > -1) {
                            String topOne = sqlElementList.get(key);
                            if (topOne.indexOf("select distinct") > -1) {
                                topOne = topOne.replace("select distinct", "select distinct top 1");
                            } else {
                                topOne = topOne.replace("select", "select top 1");
                            }
                            fileContent = fileContent.replace(sql, sql.replace(includeSql, topOne).replace("limit 1", ""));
                            hasInclude = true;
                            break;
                        }
                    }
                    if (!hasInclude) fileContent = fileContent.replace(sql, sql.replace("limit 1", ""));
                }
            }
        }
        return fileContent;
    }

    private String genTableColumnMapper(String fileContent, String fileName) {
        if ("GenTableColumnMapper.xml".equals(fileName)) {
            String sql = "<select id=\"selectDbTableColumnsByName\" parameterType=\"String\" resultMap=\"GenTableColumnResult\">\n" +
                    "\t\tSELECT\n" +
                    "            cast(A.NAME as nvarchar) as column_name,\n" +
                    "            cast(B.NAME as nvarchar) + (case when B.NAME ='numeric' then '('+cast(A.prec as nvarchar)+','+cast(A.scale as nvarchar)+')' else '' end) as column_type,\n" +
                    "            cast(G.[VALUE] as nvarchar) as column_comment,\n" +
                    "            (SELECT 1  FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE Z  WHERE TABLE_NAME=D.NAME and A.NAME = Z.column_name  ) as is_pk,\n" +
                    "            colorder as sort\n" +
                    "        FROM SYSCOLUMNS A\n" +
                    "            LEFT JOIN SYSTYPES B ON A.XTYPE=B.XUSERTYPE\n" +
                    "            INNER JOIN SYSOBJECTS D ON A.ID=D.ID AND D.XTYPE='U' AND D.NAME!='DTPROPERTIES'\n" +
                    "            LEFT JOIN SYS.EXTENDED_PROPERTIES G ON A.ID=G.MAJOR_ID AND A.COLID=G.MINOR_ID\n" +
                    "            LEFT JOIN SYS.EXTENDED_PROPERTIES F ON D.ID=F.MAJOR_ID AND F.MINOR_ID   =0\n" +
                    "        WHERE D.NAME = #{tableName}\n" +
                    "        ORDER BY A.COLORDER\n" +
                    "\t</select>";
            fileContent = fileContent.replaceAll("(<select id=\"selectDbTableColumnsByName\"[^>])([\\s\\S]*?)</select>", sql);
        }
        return fileContent;
    }

    private String genTableMapper(String fileContent, String fileName) {
        if ("GenTableMapper.xml".equals(fileName)) {
            String selectDbTableListSql = "<select id=\"selectDbTableList\" parameterType=\"GenTable\" resultMap=\"GenTableResult\">\n" +
                    "\t\tSELECT cast(D.NAME as nvarchar) as table_name,cast(F.VALUE as nvarchar) as table_comment,\n" +
                    "\t\tcrdate as create_time,refdate as update_time FROM SYSOBJECTS   D\n" +
                    "\t\tinner JOIN SYS.EXTENDED_PROPERTIES F ON D.ID=F.MAJOR_ID\n" +
                    "\t\tAND F.MINOR_ID=0 AND D.XTYPE='U' AND D.NAME!='DTPROPERTIES'\n" +
                    "\t\tAND D.NAME NOT LIKE 'qrtz_%' AND D.NAME NOT LIKE 'gen_%'\n" +
                    "\t\tAND D.NAME NOT IN (select table_name from gen_table)\n" +
                    "\t\t<if test=\"tableName != null and tableName != ''\">\n" +
                    "\t\t\tAND lower(CAST(D.NAME AS VARCHAR)) like lower(concat('%', #{tableName}, '%'))\n" +
                    "\t\t</if>\n" +
                    "\t\t<if test=\"tableComment != null and tableComment != ''\">\n" +
                    "\t\t\tAND lower(CAST(F.value AS VARCHAR)) like lower(concat('%', #{tableComment}, '%'))\n" +
                    "\t\t</if>\n" +
                    "\t\t<if test=\"params.beginTime != null and params.beginTime != ''\"><!-- 开始时间检索 -->\n" +
                    "\t\t\tAND CONVERT(varchar(100), create_time, 112) &gt;= CONVERT(varchar(100), #{params.beginTime}, 112)\n" +
                    "\t\t</if>\n" +
                    "\t\t<if test=\"params.endTime != null and params.endTime != ''\"><!-- 结束时间检索 -->\n" +
                    "\t\t\tAND CONVERT(varchar(100), create_time, 112) &lt;= CONVERT(varchar(100), #{params.endTime}, 112)\n" +
                    "\t\t</if>\n" +
                    "        order by create_time desc\n" +
                    "\t</select>";
            fileContent = fileContent.replaceAll("(<select id=\"selectDbTableList\"[^>])([\\s\\S]*?)</select>", selectDbTableListSql);

            String selectDbTableListByNamesSql = "<select id=\"selectDbTableListByNames\" resultMap=\"GenTableResult\">\n" +
                    "\t\tSELECT cast(D.NAME as nvarchar) as table_name,cast(F.VALUE as nvarchar) as table_comment,\n" +
                    "\t\tcrdate as create_time,refdate as update_time FROM SYSOBJECTS D\n" +
                    "\t\tinner JOIN SYS.EXTENDED_PROPERTIES F ON D.ID=F.MAJOR_ID\n" +
                    "\t\tAND F.MINOR_ID=0 AND   D.XTYPE='U' AND D.NAME!='DTPROPERTIES'\n" +
                    "\t\tAND D.NAME NOT LIKE 'qrtz_%' AND D.NAME NOT LIKE 'gen_%'\n" +
                    "\t\tAND D.NAME in\n" +
                    "\t    <foreach collection=\"array\" item=\"name\" open=\"(\" separator=\",\" close=\")\">\n" +
                    " \t\t\t#{name}\n" +
                    "        </foreach>\n" +
                    "\t</select>";
            fileContent = fileContent.replaceAll("(<select id=\"selectDbTableListByNames\"[^>])([\\s\\S]*?)</select>", selectDbTableListByNamesSql);

            String selectTableByNameSql = "<select id=\"selectTableByName\" parameterType=\"String\" resultMap=\"GenTableResult\">\n" +
                    "\t\tselect table_name, table_comment, create_time, update_time from information_schema.tables\n" +
                    "\t\twhere table_comment <![CDATA[ <> ]]> '' and table_schema = (Select Name From Master..SysDataBases Where DbId=(Select Dbid From Master..SysProcesses Where Spid = @@spid))\n" +
                    "\t\tand table_name = #{tableName}\n" +
                    "\t</select>";
            fileContent = fileContent.replaceAll("(<select id=\"selectTableByName\"[^>])([\\s\\S]*?)</select>", selectTableByNameSql);
        }
        return fileContent;
    }

    private String pom(String fileContent) {
        if (fileContent.indexOf("<groupId>mysql</groupId>") > 0) {
            String ms = "        <!-- SqlServer驱动包 8.4.1.jre8-->\n" +
                    "        <dependency>\n" +
                    "            <groupId>com.microsoft.sqlserver</groupId>\n" +
                    "            <artifactId>mssql-jdbc</artifactId>\n" +
                    "        </dependency>\n" +
                    "    </dependencies>";
            fileContent = fileContent.replace("</dependencies>", ms);
        }
        return fileContent;
    }

    private String yml(String fileContent, String fileName) {
        if ("application.yml".equals(fileName)) {
            fileContent = fileContent.replace("helperDialect: mysql", "helperDialect: sqlserver");
        } else if ("application-druid.yml".equals(fileName)) {
            String url = "url: jdbc:sqlserver://localhost:1433;SelectMethod=cursor;DatabaseName=ry-vue";
            fileContent = fileContent.replaceAll("url:[\\s\\S]*GMT%2B8", url);
        }
        return fileContent;
    }

}
