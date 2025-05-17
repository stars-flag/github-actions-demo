package com.example.remote;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.api.QWResult;
import com.example.api.WXTokenDTO;
import com.example.api.WxPushTmpl;
import com.example.common.WxConstant;
import com.example.config.prop.WxProp;
import com.example.utils.RestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class WxClient {


    /**
     * @param wxProp
     * @return
     */
    public static String getToken(WxProp wxProp) {

        MultiValueMap<String, String> queryMaps = new LinkedMultiValueMap<>(new HashMap<>());
        queryMaps.put(WxConstant.APP_ID, Collections.singletonList(wxProp.getAppId()));
        queryMaps.put(WxConstant.APP_SECRET, Collections.singletonList(wxProp.getAppSecret()));

        WXTokenDTO tokenDTO = RestClient.get(WxConstant.WX_TOKEN_URL, null, null, queryMaps, WXTokenDTO.class);
        if (Objects.nonNull(tokenDTO)) {
            return tokenDTO.getAccessToken();
        }
        throw new IllegalStateException("token 获取失败");
    }

    /**
     * http请求方式: GET（请使用https协议）
     * https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID
     * {
     * "total": 1,
     * "count": 1,
     * "data": {
     * "openid": [
     * "oQ8K87SvZ0Y8CZ98OScyu6pFD5FE"
     * ]
     * },
     * "next_openid": "oQ8K87SvZ0Y8CZ98OScyu6pFD5FE"
     * }
     *
     * @return
     */
    public static List<String> getUsers(String token) {
        MultiValueMap<String, String> queryMaps = new LinkedMultiValueMap<>(new HashMap<>());
        queryMaps.put(WxConstant.ACCESS_TOKEN, Collections.singletonList(token));
        String result = RestClient.get(WxConstant.WX_USERS_URL, null, null, queryMaps, String.class);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray openid = data.getJSONArray("openid");


        return openid.stream().map(Object::toString).collect(Collectors.toList());
    }

    /**
     * https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN
     *
     * @param token
     * @param user
     * @param weatherMapping
     */
    public static void pushWeather(String token, String user, String tmplId, Map<String, QWResult> weatherMapping) {

        SimpleDateFormat hourSdf = new SimpleDateFormat("HH");
        MultiValueMap<String, String> queryMaps = new LinkedMultiValueMap<>(new HashMap<>());
        queryMaps.put(WxConstant.ACCESS_TOKEN, Collections.singletonList(token));

        weatherMapping.forEach((cityName, weatherResult) -> {
            WxPushTmpl<JSONObject> pushTmpl = new WxPushTmpl<>();
            pushTmpl.setTouser(user);
            pushTmpl.setTemplateId(tmplId);
            JSONObject dataJson = new JSONObject();
            JSONObject cityJson = new JSONObject();
            cityJson.putOnce("value", cityName);
            JSONObject cityJsonOut = new JSONObject();
            cityJsonOut.putOnce("cityName", cityJson);
            dataJson.putAll(cityJsonOut);
            weatherResult.getHourly()
                    .forEach(hour -> {
                        JSONObject weatherJson = new JSONObject();
                        weatherJson.putOnce("value", hour.toLine());
                        JSONObject timeJson = new JSONObject();
                        timeJson.putOnce("_" + hourSdf.format(hour.getFxTime()), weatherJson);
                        dataJson.putAll(timeJson);
                    });

            pushTmpl.setData(dataJson);

            RestClient.postJson(WxConstant.WX_PUSH_TMPL, null, null, queryMaps, pushTmpl, String.class);
        });

    }
}
