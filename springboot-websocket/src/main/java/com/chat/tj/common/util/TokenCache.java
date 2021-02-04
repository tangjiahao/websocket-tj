package com.chat.tj.common.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 用guava实现缓存存取的类
 *
 * @author tangjing
 * @date 2020/11/12 16:43
 */
public class TokenCache {
    //1创建logback的logger
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);
    //2声明一个静态的内存块,存放guava里面的本地缓存
    //3.构建本地缓存，调用链的方式 ,1000是设置缓存的初始化容量，maximumSize是设置缓存最大容量，当超过了最大容量，guava将使用LRU算法（最少使用算法），来移除缓存项
    //expireAfterAccess设置缓存有效期为7天
    private static LoadingCache<String, String> localcache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(7, TimeUnit.DAYS)
            //build里面要实现一个匿名抽象类
            .build(new CacheLoader<String, String>() {
//                   这个方法是默认的数据加载实现,get的时候，如果key没有对应的值，就调用这个方法进行加载

                @Override
                public String load(String s) throws Exception {
                    //为什么要把return的null值写成字符串，因为到时候用null去.equal的时候，会报空指针异常
                    return "null";
                }
            });

    /**
     * 设置缓存参数
     *
     * @param key   缓存的key
     * @param value 缓存的value
     */
    public static void setKey(String key, String value) {
        localcache.put(key, value);
    }

    /**
     * 得到缓存参数
     *
     * @param key 缓存的key
     * @return 缓存的value
     */
    public static String getKey(String key) {
        String value = null;
        try {
            value = localcache.get(key);
            if ("null".equals(value)) {
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            logger.error("getKey()方法错误", e);
        }
        return null;
    }

    /**
     * 删除缓存参数
     *
     * @param key 缓存的key
     */
    public static void remove(String key) {
        localcache.invalidate(key);
    }

    public static String getToken(HttpServletRequest request) {
        String headerToken = request.getHeader("token");
        return StringUtils.isNotBlank(headerToken) ? headerToken : request.getParameter("token");
    }

}
