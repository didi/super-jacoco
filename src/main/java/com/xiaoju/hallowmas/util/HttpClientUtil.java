package com.xiaoju.hallowmas.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @ClassName: HttpUtil
 * @Description: 利用HttpClient封装GET/POST请求 连接超时时间，默认5s;请求超时时间，默认10s;默认参数编码为UTF-8；
 * 如果上述3个属性的默认值不能满足需求，可以先调用set方法来修改，再发送GET/POST请求； 目前未使用连接池管理连接！
 * [可以用连接池管理连接，在发送GET/POST请求之前，先初始化http_client一次：intiHttpClient()，
 * 发送一系列请求后，断开连接池中的连接：disconHttpClient()]
 * <p>
 * 说明：关于语音文件的接口，需要了解后再补充方法实现。
 */
public class HttpClientUtil {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClientUtil.class);
    static HttpClient http_client;

    static {
        ClientConnectionManager connectionManager = new PoolingClientConnectionManager();
        http_client = new DefaultHttpClient(connectionManager);
        http_client.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
        http_client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                30000);
    }

    private static int con_time_out = 30000; // 连接超时时间，默认5s
    private static int req_time_out = 30000; // 请求超时时间，默认10s
    private static String enc = "UTF-8";

    /**
     * POST方式提交数据----不含文件类型的参数（ 默认编码为UTF-8）
     *
     * @param url        待请求的URL
     * @param jsonParams json字符串参数, post body的值
     * @param headers    请求头参数;key-value 形式，用map保存。 key为参数名，value为参数值。
     * @return String 返回响应结果字符串
     * @throws Exception
     */
    public static String doPost(String url, String jsonParams,
                                 Map<String, ? extends Object> headers) throws Exception {
        http_client.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, con_time_out);
        http_client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                req_time_out);
        String strResult = null;
        HttpPost postRequest = new HttpPost(url);

        // 设置请求头参数值
        postRequest.addHeader("Content-Type", "application/json");
        if (headers != null) {
            for (Entry<String, ? extends Object> header : headers.entrySet()) {
                if (header.getValue() != null) {
                    postRequest.addHeader(header.getKey(), header.getValue()
                            .toString());
                }
            }
        }
        if (jsonParams == null) {
            jsonParams = "";
        }
        StringEntity strEntity = new StringEntity(jsonParams, enc);
        postRequest.setEntity(strEntity);
        // 发送请求
        HttpResponse httpResponse = http_client.execute(postRequest);
        HttpEntity respEntity = httpResponse.getEntity();

        // 取得返回字符串字符串
        if (respEntity != null) {
            strResult = EntityUtils.toString(respEntity);
            EntityUtils.consume(respEntity);
        } else {
            strResult = "HttpStatus:"
                    + httpResponse.getStatusLine().getStatusCode();
        }
        // HttpStatus.SC_OK ==200
        postRequest.releaseConnection(); // 并未真的释放连接，只是归还线程池
        // http_client.getConnectionManager().shutdown();

        return strResult;
    }


    /**
     * POST方式提交数据----不含文件类型的参数（ 默认编码为UTF-8）
     *
     * @param url       待请求的URL
     * @param strParams key-value形式的参数，使用map传参,放入到url中,便于调试
     * @param headers   请求头参数;key-value 形式，用map保存。 key为参数名，value为参数值。
     * @return String 返回响应结果字符串
     * @throws Exception
     */
    private static String doPost(String url,
                                 Map<String, ? extends Object> strParams, Map<String, String> headers) {
        http_client.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, con_time_out);
        http_client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                req_time_out);
        String strResult = null;
        String urlParams = "";
        if (strParams != null) {
            urlParams = getUrl(strParams);
        }
        url = url + urlParams;
        HttpPost postRequest = new HttpPost(url);

        // 设置请求头参数值
        if (headers != null) {
            for (Entry<String, String> header : headers.entrySet()) {
                postRequest.addHeader(header.getKey(), header.getValue());
            }
        }
        // 发送请求
        HttpResponse httpResponse;
        try {
            httpResponse = http_client.execute(postRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        HttpEntity respEntity = httpResponse.getEntity();

        // 取得返回字符串字符串
        if (respEntity != null) {
            try {
                strResult = EntityUtils.toString(respEntity);
                EntityUtils.consume(respEntity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        // HttpStatus.SC_OK ==200
        postRequest.releaseConnection(); // 并未真的释放连接，只是归还线程池
        // http_client.getConnectionManager().shutdown();

        return strResult;
    }

    /**
     * POST方式提交数据----不含文件类型的参数（ 默认编码为UTF-8）
     *
     * @param url        待请求的URL
     * @param jsonparams json字符串参数, post body的值
     * @return String 返回响应结果字符串
     * @throws Exception
     * @deprecated 使用 doPostWithObject(String url, Object param)
     */
    @Deprecated
    public static String doPost(String url, String jsonparams) throws Exception {
        return doPost(url, jsonparams, null);
    }

    public static String postMutilPart(String url, Map<String, Object> params,
                                       File file, String fileProp) throws ParseException, IOException {
        http_client.getParams().setParameter(
                CoreProtocolPNames.HTTP_CONTENT_CHARSET,
                Charset.forName("UTF-8"));
        HttpPost post = new HttpPost(url);
        MultipartEntity multipartEntity = new MultipartEntity();
        FileBody fileBody = new FileBody(file);
        multipartEntity.addPart(fileProp, fileBody);
        for (Entry<String, Object> entry : params.entrySet()) {
            multipartEntity.addPart(entry.getKey(), new StringBody(entry
                    .getValue().toString()));
        }
        post.setEntity(multipartEntity);
        HttpResponse response = http_client.execute(post);

        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == HttpStatus.SC_OK) {

            HttpEntity resEntity = response.getEntity();

            return EntityUtils.toString(resEntity);
        }

        return null;
    }

    /**
     * @param url
     * @param param 序列化为json,作为post body
     * @return
     * @throws Exception
     */
    public static String post(String url, Object param) throws Exception {
        return doPost(url, JsonUtil.getJsonStrFromObject(param), null);
    }

    /**
     * POST方式提交数据----不含文件类型的参数（ 默认编码为UTF-8）
     *
     * @param url       待请求的URL
     * @param strParams key-value形式的参数，使用map传参,放入到url中,便于调试
     * @return String 返回响应结果字符串
     * @throws Exception
     */
    public static String post(String url,
                              Map<String, ? extends Object> strParams) {
        return doPost(url, strParams, null);
    }

    /**
     * GET请求 ,默认采用UTF-8编码
     *
     * @param url     待请求的URL(不带参数部分)
     * @param params  key-value参数对， 以map形式传递
     * @param headers 请求头参数;key-value 形式，用map保存。 key为参数名，value为参数值。
     * @return String 返回响应结果字符串
     * @throws Exception
     */
    private static String doGet(String url,
                                Map<String, ? extends Object> params, Map<String, String> headers)
            throws Exception {
        http_client.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, con_time_out);
        http_client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                req_time_out);
        String strResult = null;

        // 处理参数
        if (params != null) {
            url = url + getUrl(params);
        }
        HttpGet httpget = new HttpGet(url);
        // 设置请求头参数值
        if (headers != null) {
            for (Entry<String, String> header : headers.entrySet()) {
                httpget.addHeader(header.getKey(), header.getValue());
            }
        }
        // 发送请求
        HttpResponse httpResponse = http_client.execute(httpget);
        HttpEntity respEntity = httpResponse.getEntity();
        // 取得返回字符串字符串
        if (respEntity != null) {
            strResult = EntityUtils.toString(respEntity);
            EntityUtils.consume(respEntity);
        } else {
            strResult = "HttpStatus:"
                    + httpResponse.getStatusLine().getStatusCode();
        }
        httpget.releaseConnection();
        // http_client.getConnectionManager().shutdown();

        return strResult;
    }

    /**
     * GET请求 ,默认采用UTF-8编码
     *
     * @param url    待请求的URL
     * @param params key-value参数对， 以map形式传递
     * @return String 返回响应结果字符串
     * @throws Exception
     */
    public static String doGet(String url, Map<String, ? extends Object> params)
            throws Exception {
        return doGet(url, params, null);
    }

    {

    }

    /**
     * 据Map生成get请求URL中的参数字符串
     *
     * @param map
     * @return String
     * @throws UnsupportedEncodingException
     */
    public static String getUrl(Map<String, ? extends Object> map) {

        if (null == map || map.size() == 0) {
            return "";
        }
        StringBuffer urlParams = new StringBuffer("?");

        if (map != null && !map.isEmpty()) {
            for (Entry<String, ? extends Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value == null) {
                } else if (!value.getClass().isArray()) {
                    try {
                        value = URLEncoder.encode(value.toString(), enc);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    urlParams.append(key).append("=").append(value).append("&");
                } else {// 数组要作特殊处理
                    int len = Array.getLength(value);
                    for (int i = 0; i < len; i++) {
                        Object element = Array.get(value, i);
                        if (element != null) {
                            try {
                                value = URLEncoder
                                        .encode(value.toString(), enc);
                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            }
                            urlParams.append(key).append("=").append(value)
                                    .append("&");
                        } else {
                        }
                    }
                }
            }
        }
        String paramStr = urlParams.toString();
        paramStr = paramStr.substring(0, paramStr.length() - 1);

        return paramStr;
    }

    public static int getCon_time_out() {
        return con_time_out;
    }

    /**
     * 如需修改HTTP连接超时时间，先调用该方法 默认值为：5s
     */
    public static void setCon_time_out(int con_time_out) {
        HttpClientUtil.con_time_out = con_time_out;
    }

    public static int getReq_time_out() {
        return req_time_out;
    }

    /**
     * 如需修改HTTP请求超时时间，先调用该方法 默认值为：10s
     */
    public static void setReq_time_out(int req_time_out) {
        HttpClientUtil.req_time_out = req_time_out;
    }

    public static String getEnc() {
        return enc;
    }

    /**
     * 如需修改HTTP请求参数的编码格式，先调用该方法，再调用请求处理方法 默认编码为：“UTF-8”
     */
    public static void setEnc(String enc) {
        HttpClientUtil.enc = enc;
    }

    private static HttpURLConnection sendFormdata(String reqUrl,
                                                  HashMap<String, Object> parameters, String fileParamName,
                                                  String filename, String contentType, byte[] data) throws Exception {
        // System.out.print("\n reqUrl  -------> " + reqUrl + "\n");
        HttpURLConnection urlConn = (HttpURLConnection) new URL(reqUrl)
                .openConnection();
        urlConn.setRequestMethod("POST");
        urlConn.setConnectTimeout(30000);// （单位：毫秒）jdk
        urlConn.setReadTimeout(60000);// （单位：毫秒）jdk 1.5换成这个,读操作超时
        urlConn.setDoOutput(true);
        urlConn.setRequestProperty("connection", "keep-alive");
        String boundary = "-----------------------------114975832116442893661388290519"; // 分隔符
        urlConn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
        urlConn.setRequestProperty("Accept", "*/*");
        boundary = "--" + boundary;
        // System.out.println("boundary---->" + boundary);
        StringBuffer params = new StringBuffer();
        if (parameters != null) {
            for (Iterator<String> iter = parameters.keySet().iterator(); iter
                    .hasNext(); ) {
                String name = iter.next();
                // System.out.println("name ------>  " + name);
                params.append(boundary + "\r\n");
                params.append("Content-Disposition: form-data; name=\"" + name
                        + "\"\r\n\r\n");
                // System.out.println("params---->" + params);
                // params.append(URLEncoder.encode(value, "UTF-8"));
                if (parameters.get(name) instanceof String) {
                    String value = (String) parameters.get(name);
                    params.append(value);
                    // System.out.println("params1---->" + params);
                } else if (parameters.get(name) instanceof Double) {
                    Double value = (Double) parameters.get(name);
                    params.append(value);
                    // System.out.println("params2---->" + params);
                } else if (parameters.get(name) instanceof Integer) {
                    int value = (Integer) parameters.get(name);
                    params.append(value);
                    // System.out.println("params3---->" + params);
                } else if (parameters.get(name) instanceof Long) {
                    long value = (Long) parameters.get(name);
                    params.append(value);
                    // System.out.println("params4---->" + params);
                }

                if (iter.hasNext() || !filename.isEmpty()) {
                    params.append("\r\n");
                    // System.out.println("params5---->" + params);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        if (!filename.isEmpty()) {
            sb.append(boundary);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data; name=\"" + fileParamName
                    + "\"; filename=\"" + filename + "\"\r\n");
            sb.append("Content-Type: " + contentType + "\r\n\r\n");
            // System.out.println("sb---->" + sb);
        }
        byte[] endData = ("\r\n" + boundary + "--\r\n").getBytes();
        byte[] ps = params.toString().getBytes();
        OutputStream os = urlConn.getOutputStream();
        os.write(ps);
        if (!filename.isEmpty()) {
            byte[] fileDiv = sb.toString().getBytes();
            os.write(fileDiv);
            os.write(data);
        }
        os.write(endData);
        os.flush();
        os.close();
        return urlConn;
    }

    private static String read(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

    /**
     * String to Gzip
     *
     * @param Url
     * @param str
     */
    public static String postGD(String Url, String str) {
        String resp = "";
        try {
            URL url = new URL(Url);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type",
                    "application/octet-stream");
            connection.setRequestProperty("Content-Encoding", "gzip");
            connection.connect();
            // 请求
            GZIPOutputStream out = new GZIPOutputStream(
                    connection.getOutputStream());
            out.write(str.getBytes("UTF-8"));
            // System.out.println(str.toString());
            out.flush();
            out.close();
            // 读取响应
            BufferedReader in2 = new BufferedReader(new InputStreamReader(
                    new GZIPInputStream(connection.getInputStream())));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = in2.readLine()) != null) {
                lines = new String(lines.getBytes(), "UTF-8");
                sb.append(lines);
            }
            resp = sb.toString();
            in2.close();
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }


    public static String postGDB(String Url, byte[] bt) {
        String resp = "";
        try {
            URL url = new URL(Url);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type",
                    "application/octet-stream");
            connection.setRequestProperty("Content-Encoding", "gzip");
            connection.connect();
            // 请求
            GZIPOutputStream out = new GZIPOutputStream(
                    connection.getOutputStream());
            out.write(bt);
            out.flush();
            out.close();
            // 读取响应
            BufferedReader in2 = new BufferedReader(new InputStreamReader(
                    new GZIPInputStream(connection.getInputStream())));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = in2.readLine()) != null) {
                lines = new String(lines.getBytes(), "UTF-8");
                sb.append(lines);
            }
            resp = sb.toString();
            in2.close();
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }


    public static boolean httpDownload(String httpUrl, String saveFile) {
        int bytesum = 0;
        int byteread = 0;

        URL url = null;
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return false;
        }

        try {

            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(20000);
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream(saveFile);

            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}