package cn.web.front.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

public class MoneyUtils {
    
    /**
     * 随机16为数值
     * 
     * @return
     */
    public static String buildRandom() {
        String currTime = getCurrTime();
        String strTime = currTime.substring(8, currTime.length());
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < 4; i++) {
            num = num * 10;
        }
        return (int) ((random * num)) + strTime;
    }

    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。 sign
     */
    @SuppressWarnings("rawtypes")
    public static String createSign(Map<String, String> map, String partnerkey) {
        final Map<String, String> packageParams = new TreeMap<>(new Comparator<String>() {
            public int compare(String p1, String p2) {
                return p1.compareTo(p2);
            }
        });
        
        for (Map.Entry<String, String> m : map.entrySet()) {
            packageParams.put(m.getKey(), m.getValue());
        }

        StringBuffer sb = new StringBuffer();
        Set<?> es = packageParams.entrySet();
        Iterator<?> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!StringUtils.isEmpty(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + partnerkey);

        String sign = MD5Encode(sb.toString(), "UTF-8").toUpperCase();
        return sign;
    }

    public static String getOrderNo(String partner) {
        String order = partner + new SimpleDateFormat("yyyyMMdd").format(new Date());
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            order += r.nextInt(9);
        }
        return order;
    }

    public static String doSendMoney(String url, String data, String KEYSTORE_PASSWORD, String cert) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(cert));// P12文件目录
        // InputStream instream =
        // MoneyUtils.class.getResourceAsStream("/apiclient_cert.p12");
        try {
            keyStore.load(instream, KEYSTORE_PASSWORD.toCharArray());
        } finally {
            instream.close();
        }

        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, KEYSTORE_PASSWORD.toCharArray())// 这里也是写密码的
                .build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
            HttpPost httpost = new HttpPost(url); // 设置响应头信息
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();
                String jsonStr = toStringInfo(response.getEntity(), "UTF-8");

                // 微信返回的报文时GBK，直接使用httpcore解析乱码
                // String jsonStr =
                // EntityUtils.toString(response.getEntity(),"UTF-8");
                EntityUtils.consume(entity);
                return jsonStr;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    private static String toStringInfo(HttpEntity entity, String defaultCharset) throws Exception, IOException {
        final InputStream instream = entity.getContent();
        if (instream == null) {
            return null;
        }
        try {
            Args.check(entity.getContentLength() <= Integer.MAX_VALUE, "HTTP entity too large to be buffered in memory");
            int i = (int) entity.getContentLength();
            if (i < 0) {
                i = 4096;
            }
            Charset charset = null;

            if (charset == null) {
                charset = Charset.forName(defaultCharset);
            }
            if (charset == null) {
                charset = HTTP.DEF_CONTENT_CHARSET;
            }
            final Reader reader = new InputStreamReader(instream, charset);
            final CharArrayBuffer buffer = new CharArrayBuffer(i);
            final char[] tmp = new char[1024];
            int l;
            while ((l = reader.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
            return buffer.toString();
        } finally {
            instream.close();
        }
    }

    public static String createXML(Map<String, String> map) {
        String xml = "<xml>";
        Set<String> set = map.keySet();
        Iterator<String> i = set.iterator();
        while (i.hasNext()) {
            String str = i.next();
            xml += "<" + str + ">" + "<![CDATA[" + map.get(str) + "]]>" + "</" + str + ">";
        }
        xml += "</xml>";
        return xml;
    }

    /**
     * 获取当前时间 yyyyMMddHHmmss
     * 
     * @return String
     */
    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }


    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }

    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

}
