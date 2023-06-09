package com.cherry.lucky.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpUtil;
import com.cherry.lucky.model.dto.SessionKeyOpenIdDTO;
import com.cherry.lucky.model.dto.WeChatUserInfoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName WeChatUtil
 * @Description
 * @createTime 2023年03月22日 16:02:00
 */
public class WeChatUtil {

    /**
     * 开发者服务器 登录凭证校验接口 appId + appSecret + 接收小程序发送的code
     *
     * @param code wx code
     * @return openid, session_key
     * @throws JsonProcessingException json parse exception
     */
    public static SessionKeyOpenIdDTO getSessionKeyOrOpenId(String code) throws JsonProcessingException {
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
        HashMap<String, Object> requestUrlParam = new HashMap<>(16);
        // wx args
        requestUrlParam.put("appid", "wx8cb4f1e9bb0e7a48");
        requestUrlParam.put("secret", "e1ca1383b4b568adbcca1e5a416c7bde");
        requestUrlParam.put("js_code", code);
        requestUrlParam.put("grant_type", "authorization_code");
        String result = HttpUtil.get(requestUrl, requestUrlParam);
        // json parse
        return new ObjectMapper().readValue(result, new TypeReference<>() {
        });
    }

    /**
     * parse encryptedData
     *
     * @param encryptedData encryptedData
     * @param sessionKey sessionKey
     * @param iv iv
     */
    public static WeChatUserInfoDTO getUserInfo(String encryptedData, String sessionKey, String iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException,
            InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, JsonProcessingException
    {
        byte[] encData = Base64.decode(encryptedData);
        byte[] vi = Base64.decode(iv);
        byte[] key = Base64.decode(sessionKey);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(vi);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] resultByte = cipher.doFinal(encData);
        if (null != resultByte && resultByte.length > 0) {
            String result = new String(resultByte, StandardCharsets.UTF_8);
            return new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).readValue(result, new TypeReference<>() {
            });
        }
        return null;
    }

}
