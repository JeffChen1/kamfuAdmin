/*
 *  Copyright (C) 2018  Wanghaobin<463540703@qq.com>

 *  AG-Enterprise 企业版源码
 *  郑重声明:
 *  如果你从其他途径获取到，请告知老A传播人，奖励1000。
 *  老A将追究授予人和传播人的法律责任!

 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package kf.plt.admin.auth.runner;

import kf.plt.admin.auth.configuration.KeyConfiguration;
import kf.plt.admin.auth.jwt.AECUtil;
import kf.plt.admin.jwt.core.util.RsaKeyHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

/**
 * @author ace
 * @version 2017/12/17.
 */
@Configuration
@Slf4j
public class AuthServerRunner implements CommandLineRunner {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final String REDIS_USER_PRI_KEY = "AG:AUTH:JWT:PRI";
    private static final String REDIS_USER_PUB_KEY = "AG:AUTH:JWT:PUB";
    private static final String REDIS_SERVICE_PRI_KEY = "AG:AUTH:CLIENT:PRI";
    private static final String REDIS_SERVICE_PUB_KEY = "AG:AUTH:CLIENT:PUB";

    @Autowired
    private KeyConfiguration keyConfiguration;

    @Autowired
    private AECUtil aecUtil;

    @Autowired
    private RsaKeyHelper rsaKeyHelper;

    @Override
    public void run(String... args) throws Exception {
        boolean flag = false;
        if (redisTemplate.hasKey(REDIS_USER_PRI_KEY)&&redisTemplate.hasKey(REDIS_USER_PUB_KEY)&&redisTemplate.hasKey(REDIS_SERVICE_PRI_KEY)&&redisTemplate.hasKey(REDIS_SERVICE_PUB_KEY)) {
            try {
                keyConfiguration.setUserPriKey(rsaKeyHelper.toBytes(aecUtil.decrypt(redisTemplate.opsForValue().get(REDIS_USER_PRI_KEY).toString())));
                keyConfiguration.setUserPubKey(rsaKeyHelper.toBytes(redisTemplate.opsForValue().get(REDIS_USER_PUB_KEY).toString()));
                keyConfiguration.setServicePriKey(rsaKeyHelper.toBytes(aecUtil.decrypt(redisTemplate.opsForValue().get(REDIS_SERVICE_PRI_KEY).toString())));
                keyConfiguration.setServicePubKey(rsaKeyHelper.toBytes(redisTemplate.opsForValue().get(REDIS_SERVICE_PUB_KEY).toString()));
            }catch (Exception e){
                log.error("初始化公钥/密钥异常...",e);
                flag = true;
            }
        } else {
            flag = true;
        }
        if(flag){
            Map<String, byte[]> keyMap = rsaKeyHelper.generateKey(keyConfiguration.getUserSecret());
            keyConfiguration.setUserPriKey(keyMap.get("pri"));
            keyConfiguration.setUserPubKey(keyMap.get("pub"));
            redisTemplate.opsForValue().set(REDIS_USER_PRI_KEY, aecUtil.encrypt(rsaKeyHelper.toHexString(keyMap.get("pri"))));
            redisTemplate.opsForValue().set(REDIS_USER_PUB_KEY, rsaKeyHelper.toHexString(keyMap.get("pub")));
            keyMap = rsaKeyHelper.generateKey(keyConfiguration.getServiceSecret());
            keyConfiguration.setServicePriKey(keyMap.get("pri"));
            keyConfiguration.setServicePubKey(keyMap.get("pub"));
            redisTemplate.opsForValue().set(REDIS_SERVICE_PRI_KEY, aecUtil.encrypt(rsaKeyHelper.toHexString(keyMap.get("pri"))));
            redisTemplate.opsForValue().set(REDIS_SERVICE_PUB_KEY, rsaKeyHelper.toHexString(keyMap.get("pub")));
        }
        log.info("完成公钥/密钥的初始化...");
    }
}
