package kf.plt.admin.jwt.core.context;


import kf.plt.admin.jwt.core.constants.CommonConstants;
import kf.plt.admin.jwt.core.util.StringHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * 后台当前上下文
 *
 * @author ace
 * @version 2017/9/8
 */
public class BaseContextHandler {
    public static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

    public static void set(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        map.put(key, value);
    }

    public static Object get(String key) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        return map.get(key);
    }

    public static String getDepartID(){
        Object value = get(CommonConstants.CONTEXT_KEY_GROUP_ID);
        return returnObjectValue(value);
    }

    public static void setDepartID(String departID){
        set(CommonConstants.CONTEXT_KEY_GROUP_ID, departID);
    }

    public static String getTenantID(){
        Object value = get(CommonConstants.CONTEXT_KEY_TENANT_ID);
        return returnObjectValue(value);
    }

    public static void setTenantID(String tenantID){
        set(CommonConstants.CONTEXT_KEY_TENANT_ID, tenantID);
    }

    public static String getUserID() {
        Object value = get(CommonConstants.CONTEXT_KEY_USER_ID);
        return returnObjectValue(value);
    }

    public static String getUsername() {
        Object value = get(CommonConstants.CONTEXT_KEY_USERNAME);
        return returnObjectValue(value);
    }


    public static String getName() {
        Object value = get(CommonConstants.CONTEXT_KEY_USER_NAME);
        return StringHelper.getObjectValue(value);
    }

    public static String getToken() {
        Object value = get(CommonConstants.CONTEXT_KEY_USER_TOKEN);
        return StringHelper.getObjectValue(value);
    }

    public static String getRequestUri() {
        Object value = get(CommonConstants.CONTEXT_KEY_REQUEST_URI);
        return StringHelper.getObjectValue(value);
    }

    public static void setToken(String token) {
        set(CommonConstants.CONTEXT_KEY_USER_TOKEN, token);
    }

    public static void setName(String name) {
        set(CommonConstants.CONTEXT_KEY_USER_NAME, name);
    }

    public static void setUserID(String userID) {
        set(CommonConstants.CONTEXT_KEY_USER_ID, userID);
    }

    public static void setUsername(String username) {
        set(CommonConstants.CONTEXT_KEY_USERNAME, username);
    }

    public static void setRequestUri(String requestUri) {set(CommonConstants.CONTEXT_KEY_REQUEST_URI,requestUri); }

    private static String returnObjectValue(Object value) {
        return value == null ? null : value.toString();
    }

    public static void remove() {
        threadLocal.remove();
    }
}