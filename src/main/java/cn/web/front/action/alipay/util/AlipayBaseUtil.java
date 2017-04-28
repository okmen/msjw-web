package cn.web.front.action.alipay.util;

import java.util.Map;

import cn.message.model.alipay.AlipayServiceEnvConstants;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.StringUtils;

public class AlipayBaseUtil {
	
	public static String encryptAndSign(String bizContent,
			String alipayPublicKey, String cusPrivateKey, String charset,
			boolean isEncrypt, boolean isSign, String signType)
			throws AlipayApiException {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isEmpty(charset)) {
			charset = AlipayServiceEnvConstants.SIGN_CHARSET;
		}
		sb.append("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>");
		if (isEncrypt) {// 加密
			sb.append("<alipay>");
			String encrypted = AlipaySignature.rsaEncrypt(bizContent,
					alipayPublicKey, charset);
			sb.append("<response>" + encrypted + "</response>");
			sb.append("<encryption_type>AES</encryption_type>");
			if (isSign) {
				String sign = AlipaySignature.rsaSign(encrypted, cusPrivateKey,
						charset, signType);
				sb.append("<sign>" + sign + "</sign>");
				sb.append("<sign_type>");
				sb.append(signType);
				sb.append("</sign_type>");
			}
			sb.append("</alipay>");
		} else if (isSign) {// 不加密，但需要签名
			sb.append("<alipay>");
			sb.append("<response>" + bizContent + "</response>");
			String sign = AlipaySignature.rsaSign(bizContent, cusPrivateKey,
					charset, signType);
			sb.append("<sign>" + sign + "</sign>");
			sb.append("<sign_type>");
			sb.append(signType);
			sb.append("</sign_type>");
			sb.append("</alipay>");
		} else {// 不加密，不加签
			sb.append(bizContent);
		}
		return sb.toString();
	}
	

	/**
	 * 验签
	 * 
	 * @param request
	 *            ‘
	 * @return
	 */
	public static void verifySign(Map<String, String> params)
			throws AlipayApiException {

		if (!AlipaySignature.rsaCheckV2(params,
				AlipayServiceEnvConstants.ALIPAY_PUBLIC_KEY,
				AlipayServiceEnvConstants.SIGN_CHARSET,
				AlipayServiceEnvConstants.SIGN_TYPE)) {
			throw new AlipayApiException("verify sign fail.");
		}
	}
}
