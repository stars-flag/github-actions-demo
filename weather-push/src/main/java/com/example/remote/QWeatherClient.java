package com.example.remote;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.example.api.QWResult;
import com.example.common.QWeatherConstant;
import com.example.config.prop.QWeatherProp;
import com.example.utils.RestClient;
import lombok.extern.slf4j.Slf4j;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

@Slf4j
public class QWeatherClient {

    private static final TimedCache<String, String> timedCache;

    static {
        timedCache = CacheUtil.newTimedCache(895000);
        timedCache.schedulePrune(900000);
    }

    /**
     * curl -X GET --compressed \
     * -H 'Authorization: Bearer your_token' \
     * 'https://your_api_host/v7/weather/3d?location=101010100'
     * <p>
     * curl -X GET --compressed \
     * -H 'Authorization: Bearer your_token' \
     * 'https://your_api_host/v7/weather/24h?location=101010100'
     *
     * @param weatherProp
     * @return
     */
    public static QWResult getWeather(QWeatherProp weatherProp, String location, String type) {
        MultiValueMap<String, String> queryMaps = new LinkedMultiValueMap<>(new HashMap<>());
        queryMaps.put(QWeatherConstant.LOCATION, Collections.singletonList(location));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + auth(weatherProp.getPrivateKey(), weatherProp.getKeyId(), weatherProp.getProjectId()));

        QWResult result = RestClient.get("https://" + weatherProp.getHost(), type, headers, queryMaps, QWResult.class);

        if (Objects.nonNull(result)) {
            return result;
        }
        throw new IllegalStateException("天气获取失败");
    }


    /**
     * 生成凭据
     *
     * @param privateKeyString 私钥
     * @param keyId            凭据id
     * @param projectId        凭据所在项目id
     * @return
     * @throws Exception
     */
    public static String auth(String privateKeyString, String keyId, String projectId) {
        // Private key
        try {
            String token = timedCache.get("token");
            if (StringUtils.hasText(token)) return token;


            privateKeyString = privateKeyString.trim().replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").trim();
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
            PKCS8EncodedKeySpec encoded = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privateKey = new EdDSAPrivateKey(encoded);

            // Header
            String headerJson = "{\"alg\": \"EdDSA\", \"kid\": \"" + keyId + "\"}";

            // Payload
            long iat = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond() - 30;
            long exp = iat + 900;
            String payloadJson = "{\"sub\": \"" + projectId + "\", \"iat\": " + iat + ", \"exp\": " + exp + "}";

            // Base64url header+payload
            String headerEncoded = Base64.getUrlEncoder().encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));
            String payloadEncoded = Base64.getUrlEncoder().encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));
            String data = headerEncoded + "." + payloadEncoded;

            EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);

            // Sign
            final Signature s = new EdDSAEngine(MessageDigest.getInstance(spec.getHashAlgorithm()));
            s.initSign(privateKey);
            s.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] signature = s.sign();

            String signatureString = Base64.getUrlEncoder().encodeToString(signature);

            //  Token
            token = data + "." + signatureString;
            timedCache.put("token", token);
            return token;
        } catch (Exception e) {
            log.error("获取认证消息异常", e);
        }
        throw new IllegalStateException("获取认证消息异常");
    }
}
