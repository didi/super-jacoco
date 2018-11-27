package com.xiaoju.hallowmas.util;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by yehonggang on 17/7/13.
 */
public class XMLUtils {
    private static final Logger LOG = LoggerFactory.getLogger(XMLUtils.class);

    public static String generateXML(String xmlStr, String fileName) throws Exception {
        Document document = str2XML(xmlStr);
        String tempDir = System.getProperty("user.dir") + System.getProperty("file.separator") + "tempCodeXml/";
        LOG.info("tempDir is " + tempDir);
        //文件保存位置
        File saveDir = new File(tempDir);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        FileOutputStream fos = null;
        try {
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setIndent(""));
            fos = new FileOutputStream(new File(tempDir + fileName + ".xml"));
            outputter.output(document, fos);
            fos.close();
            return tempDir + fileName + ".xml";
        } catch (Exception e) {
            throw e;
        } finally {
            if (fos != null) {
                fos.close();
            }
        }

    }

    /**
     * String 转 xml
     *
     * @param string
     * @return
     * @throws Exception
     */
    public static Document str2XML(String string) throws Exception {

        SAXBuilder buider = new SAXBuilder();

        Document document = buider.build(new StringReader(string));

        return document;

    }

    /**
     * 将XML转换成String输出
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String xml2Str(String file) throws Exception {

        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(new FileInputStream(new File(file)));

        Format format = Format.getCompactFormat();
        format.setEncoding("UTF-8");// 设置xml文件的字符为UTF-8，解决中文问题
        XMLOutputter xmlout = new XMLOutputter();

        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        xmlout.output(document, bo);
        return bo.toString().trim();

    }

    /**
     * 将XML以键值对的形式输出,对于有两层以上的XML文件
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static Map<String, String> paraseXML(String file) throws Exception {

        Map<String, String> map = new HashMap<String, String>();

        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(new File(file));

        Element root = document.getRootElement();

        List<Element> list = root.getChildren();
        for (Iterator<Element> iterator = list.iterator(); iterator.hasNext(); ) {

            Element firstChild = iterator.next();
            List<Element> secondElements = firstChild.getChildren();

            for (Iterator<Element> iterator2 = secondElements.iterator(); iterator2
                    .hasNext(); ) {

                Element secondElement = iterator2.next();
                map.put(secondElement.getName(), secondElement.getText());
            }

        }

        return map;
    }


    public static void main(String[] args) throws Exception {

        String str = xml2Str("/Users/yehonggang/Documents/git/test/fusion-test/setting.xml");
       /* Document document = str2XML(str);
        String tempDir = System.getProperty("user.dir") + System.getProperty("file.separator") + "tempCodeXml/";
        LOG.info("tempDir is" + tempDir);
        //文件保存位置
        File saveDir = new File(tempDir);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setIndent(""));
        FileOutputStream fos = new FileOutputStream(new File(tempDir + "setting.xml"));
        outputter.output(document, fos);
        fos.close();*/

        String xmlFilePath = XMLUtils.generateXML(str, "order");
        Scpclient scp = Scpclient.getInstance("10.0.50.180", 22, "root", "qOjfnjbDOewZoILCiqGE");
        scp.putFile(xmlFilePath + "order.xml", "order.xml.tmp", "/home/tmpcodexml/", null);
       /*/// String xmlContent = xml2Str("/Users/yehonggang/Documents/git/test/fusion-test/setting");
       // System.out.println("XML的内容为： "+ "\n" + xmlContent);

        Map<String, String> xmlMap = new HashMap<String, String>();
        xmlMap = paraseXML("/Users/yehonggang/Documents/git/test/fusion-test/setting");
        Set<String> keysSet = xmlMap.keySet();
        for(String key: keysSet){

            String value = xmlMap.get(key);
            System.out.println(key + " = " + value);
        }*/


    }
}
