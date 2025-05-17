package com.example.common;

public class WxConstant {

    private WxConstant() {
    }

    /**
     * https请求方式: GET https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
     */
    public static final String WX_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

    public static final String WX_USERS_URL = "https://api.weixin.qq.com/cgi-bin/user/get";

    public static final String WX_PUSH_TMPL = "https://api.weixin.qq.com/cgi-bin/message/template/send";

    public static final String APP_ID = "appid";

    public static final String APP_SECRET = "secret";

    public static final String ACCESS_TOKEN = "access_token";



}
