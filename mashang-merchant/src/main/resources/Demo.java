package me.zohar.java.awesome_java;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.http.HttpUtil;

/**
 * Hello world!
 *
 */
public class Demo {

	public static void test1() {
		String merchantNum = "tencent";
		String orderNo = "123";
		Double amount = 1d;
		String notifyUrl = "https://www.baidu.com/";
		String returnUrl = "https://www.baidu.com/";
		String payType = "wechat";
		String attch = "1";
		String securityKey = "8bee9672098d8e4a53e446e845edf37c";
		String key = merchantNum + orderNo + String.valueOf(amount) + notifyUrl + securityKey;
		System.out.println(key);
		String sign = new Digester(DigestAlgorithm.MD5).digestHex(key);
		System.out.println(sign);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("merchantNum", merchantNum);
		paramMap.put("orderNo", orderNo);
		paramMap.put("amount", amount);
		paramMap.put("notifyUrl", notifyUrl);
		paramMap.put("returnUrl", returnUrl);
		paramMap.put("payType", payType);
		paramMap.put("attch", attch);
		paramMap.put("sign", sign);
		String result = HttpUtil.post("http://localhost:8080/api/startOrder", paramMap);
		System.out.println(result);
	}


	public static void main(String[] args) throws NoSuchAlgorithmException {
		test1();
	}

}
