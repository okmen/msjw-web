package cn.web.wechat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

public class HttpRequest {
	private static Logger logger = Logger.getLogger(HttpRequest.class);
    /**
     * 向指定URL发送GET方法的请求
     * @param url
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            logger.info(urlNameString);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            conn.setConnectTimeout(20000);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            // 建立实际的连接
            conn.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
            		conn.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        	logger.error("发送GET请求出现异常"+url,e);
        }finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        logger.info(result);
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
        	logger.info(url);
        	logger.info(param);
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            conn.setConnectTimeout(20000);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        	logger.error("发送 POST 请求出现异常"+url,e);
        }finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        logger.info(result);
        return result;
    }    
    
    public static String sendPost4Wechat(String requestUrl ,String params){
    	StringBuffer buffer= new StringBuffer();
		try {
        	URL url = new URL(requestUrl);
        	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        	conn.setConnectTimeout(20000);
        	conn.setDoOutput(true);
        	conn.setDoInput(true);
        	conn.setUseCaches(false);
        	// 设置请求方式（GET/POST）
        	conn.setRequestMethod("POST");
        	// 当 outputStr 不为 null 时，向输出流写数据
        	if (null != params) {
        		OutputStream outputStream = conn.getOutputStream();
        		// 注意编码格式
        		outputStream.write(params.getBytes("UTF-8"));
        		outputStream.close();
        	}
        	// 从输入流读取返回内容
        	InputStream inputStream = conn.getInputStream();
        	InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        	BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        	String str = null;
        	while ((str = bufferedReader.readLine()) != null) {
        		buffer.append(str);
        	}
        	// 释放资源
        	bufferedReader.close();
        	inputStreamReader.close();
        	inputStream.close();
        	inputStream = null;
        	conn.disconnect();
		} catch (ConnectException ce) {
			logger.error("连接超时"+requestUrl, ce);
        } catch (Exception e) {
        	logger.error("发送微信post请求异常"+requestUrl,e);
		}
        return buffer.toString();
    }
}
