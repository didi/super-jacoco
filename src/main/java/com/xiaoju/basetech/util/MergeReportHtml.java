package com.xiaoju.basetech.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @description:
 * @author: gaoweiwei_v
 * @time: 2019/12/12 8:41 AM
 */
public class MergeReportHtml {
    public static Integer[] mergeHtml(ArrayList<String> fileList, String destFile) {
        Integer[] result=new Integer[3];
        result[0]=0;
        result[1]=-1;
        result[2]=-1;
        String htmlSchema = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\">\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\" />\n" +
                "    <link rel=\"stylesheet\" href=\"jacoco-resources/report.css\" type=\"text/css\" />\n" +
                "    <link rel=\"shortcut icon\" href=\"jacoco-resources/report.gif\" type=\"image/gif\" />\n" +
                "    <title>manualDiffCoverageReport</title>\n" +
                "    <script type=\"text/javascript\" src=\"jacoco-resources/sort.js\"></script>\n" +
                "</head>\n" +
                "<body onload=\"initialSort(['breadcrumb', 'coveragetable'])\">\n" +
                "    <div class=\"breadcrumb\" id=\"breadcrumb\"><span class=\"info\"><a href=\"jacoco-sessions.html\" class=\"el_session\">Sessions</a></span><span class=\"el_report\">manualDiffCoverageReport</span></div>\n" +
                "    <h1>manualDiffCoverageReport</h1>\n" +
                "    <table class=\"coverage\" cellspacing=\"0\" id=\"coveragetable\">\n" +
                "        <thead>\n" +
                "            <tr>\n" +
                "                <td class=\"sortable\" id=\"a\" onclick=\"toggleSort(this)\">Element</td>\n" +
                "                <td class=\"down sortable bar\" id=\"b\" onclick=\"toggleSort(this)\">Missed Instructions</td>\n" +
                "                <td class=\"sortable ctr2\" id=\"c\" onclick=\"toggleSort(this)\">Cov.</td>\n" +
                "                <td class=\"sortable bar\" id=\"d\" onclick=\"toggleSort(this)\">Missed Branches</td>\n" +
                "                <td class=\"sortable ctr2\" id=\"e\" onclick=\"toggleSort(this)\">Cov.</td>\n" +
                "                <td class=\"sortable ctr1\" id=\"f\" onclick=\"toggleSort(this)\">Missed</td>\n" +
                "                <td class=\"sortable ctr2\" id=\"g\" onclick=\"toggleSort(this)\">Cxty</td>\n" +
                "                <td class=\"sortable ctr1\" id=\"h\" onclick=\"toggleSort(this)\">Missed</td>\n" +
                "                <td class=\"sortable ctr2\" id=\"i\" onclick=\"toggleSort(this)\">Lines</td>\n" +
                "                <td class=\"sortable ctr1\" id=\"j\" onclick=\"toggleSort(this)\">Missed</td>\n" +
                "                <td class=\"sortable ctr2\" id=\"k\" onclick=\"toggleSort(this)\">Methods</td>\n" +
                "                <td class=\"sortable ctr1\" id=\"l\" onclick=\"toggleSort(this)\">Missed</td>\n" +
                "                <td class=\"sortable ctr2\" id=\"m\" onclick=\"toggleSort(this)\">Classes</td>\n" +
                "            </tr>\n" +
                "        </thead>\n" +
                "        <tbody>\n" +
                "        </tbody>\n" +
                "<tfoot></tfoot>"+
                "    </table>\n" +
                "    <div class=\"footer\"><span class=\"right\">Created with <a href=\"http://www.jacoco.org/jacoco\">JaCoCo</a> 1.0.1.201909190214</span></div>\n" +
                "</body>\n" +
                "</html>";
        try {
            Document docSchema = Jsoup.parse(htmlSchema);
            Integer[] array = new Integer[15];
            array[0] = 0;
            array[1] = 0;
            array[2] = 0;
            array[3] = 0;
            array[4] = 0;
            array[5] = 0;
            array[6] = 0;
            array[7] = 0;
            array[8] = 0;
            array[9] = 0;
            array[10] = 0;
            array[11] = 0;
            array[12] = 0;
            array[13] = 0;
            array[14] = 0;
            Element tbodySchema = docSchema.getElementsByTag("table").first();
            for (String fileName : fileList) {
                File file=new File(fileName);
                String module=new File(file.getParent()).getName();
                Document docc = Jsoup.parse(new File(fileName), "UTF-8", "");
                Document doc=Jsoup.parse(docc.toString().replace("<a href=\"","<a href=\""+module+"/"));
                if(doc.getElementsByTag("tbody").first()==null){
                    continue;
                }
                Elements trs = doc.getElementsByTag("tbody").first().getElementsByTag("tr");
                for (Element ele : trs) {
                    tbodySchema.getElementsByTag("tbody").first().append(ele.html());
                }
                String[] a = doc.getElementsByTag("tfoot").first().child(0).text().split(" ");
                array[1] = array[1] + Integer.parseInt(a[1].replace(",", ""));
                array[2] = array[2] + Integer.parseInt(a[3].replace(",", ""));
                //array[3] = array[3] + Integer.parseInt(a[4].replace("%", ""));
                array[4] = array[4] + Integer.parseInt(a[5].replace(",", ""));
                array[5] = array[5] + Integer.parseInt(a[7].replace(",", ""));
                //array[6] = array[6] + Integer.parseInt(a[8].replace("%", ""));
                array[7] = array[7] + Integer.parseInt(a[9].replaceAll(",",""));
                array[8] = array[8] + Integer.parseInt(a[10].replace(",", ""));
                array[9] = array[9] + Integer.parseInt(a[11].replace(",", ""));
                array[10] = array[10] + Integer.parseInt(a[12].replace(",", ""));
                array[11] = array[11] + Integer.parseInt(a[13].replace(",", ""));
                array[12] = array[12] + Integer.parseInt(a[14].replace(",", ""));
                array[13] = array[13] + Integer.parseInt(a[15].replace(",", ""));
                array[14] = array[14] + Integer.parseInt(a[16].replace(",", ""));
            }
            if(array[2]==0){
                array[1]=1;
                array[2]=1;
            }
            if(array[5]==0){
                array[4]=1;
                array[5]=1;
            }
            if(array[10]==0){
                array[9]=1;
                array[10]=1;
            }
            String tfoot = "         <tr>\n" +
                    "                <td>Total</td>\n" +
                    "                <td class=\"bar\">" + array[1] + " of " + array[2] + "</td>\n" +
                    "                <td class=\"ctr2\">" + (array[2]-array[1])*100/array[2] + "%</td>\n" +
                    "                <td class=\"bar\">" + array[4] + " of " + array[5] + "</td>\n" +
                    "                <td class=\"ctr2\">" + (array[5]-array[4])*100/array[5] + "%</td>\n" +
                    "                <td class=\"ctr1\">" + array[7] + "</td>\n" +
                    "                <td class=\"ctr2\">" + array[8] + "</td>\n" +
                    "                <td class=\"ctr1\">" + array[9] + "</td>\n" +
                    "                <td class=\"ctr2\">" + array[10] + "</td>\n" +
                    "                <td class=\"ctr1\">" + array[11] + "</td>\n" +
                    "                <td class=\"ctr2\">" + array[12] + "</td>\n" +
                    "                <td class=\"ctr1\">" + array[13] + "</td>\n" +
                    "                <td class=\"ctr2\">" + array[14] + "</td>\n" +
                    "            </tr>\n";
            tbodySchema.getElementsByTag("tfoot").first().append(tfoot);
            FileWriter writer = new FileWriter(destFile);
            writer.write(docSchema.toString());
            writer.flush();
            result[0]=1;
            result[1]=(array[5]-array[4])*100/array[5];
            result[2]=(array[10]-array[9])*100/array[10];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}