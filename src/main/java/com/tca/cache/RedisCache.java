package com.tca.cache;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.tca.util.JsonUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

@SuppressWarnings("deprecation")
@PropertySource("classpath:properties-config/redis.properties")
@Component
public class RedisCache {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.maxActive}")
    private int maxActive;
    @Value("${redis.maxIdle}")
    private int maxIdle;
    @Value("${redis.maxWait}")
    private int maxWait;
    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${redis.timeout}")
    private int timeout;
    
    private String password;
    private JedisPool jedisPool;

    @PostConstruct
    public void initial() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxWaitMillis(maxWait);
        poolConfig.setTestOnBorrow(testOnBorrow);
        poolConfig.setMaxTotal(maxActive);
        if (StringUtils.isEmpty(password)) {
            jedisPool = new JedisPool(poolConfig, host, port, timeout);
        } else {
            jedisPool = new JedisPool(poolConfig, host, port, timeout, password);
        }
    }

    @PreDestroy
    public void destroy() {
        if (null != jedisPool) {
            jedisPool.destroy();
        }
    }


	public <T> void set(String key, T value, int timeout) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (value instanceof String
                    || value instanceof Long
                    || value instanceof Integer) {
                jedis.set(key, value.toString());
            } else {
                jedis.set(key, JsonUtil.objectToJson(value));
            }
            if (timeout > 0) {
                jedis.expire(key, timeout);
            }
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public boolean checkKey(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(key);
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
        return false;
    }

	@SuppressWarnings("unchecked")
	public <T> T get(String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            if (StringUtils.isNotEmpty(value)) {
                if (String.class.isAssignableFrom(clazz)) {
                    return (T) value;
                } else if (Long.class.isAssignableFrom(clazz)) {
                    return (T) Long.valueOf(value);
                } else if (Integer.class.isAssignableFrom(clazz)) {
                    return (T) Integer.valueOf(value);
                } else {
                    return JsonUtil.jsonToObject(value, clazz);
                }
            }
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
        return null;
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            if (null != value && !"".equals(value)) {
                return value;
            }
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
	public <T> List<T> getList(String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            List<String> jsons = jedis.lrange(key, 0, -1);
            if (jsons != null && jsons.size() > 0) {
                List<T> list = new ArrayList<>();
                for (String json : jsons) {
                    if (String.class.isAssignableFrom(clazz)) {
                        list.add((T) json);
                    } else if (Integer.class.isAssignableFrom(clazz)) {
                        list.add((T) Integer.valueOf(json));
                    } else if (Long.class.isAssignableFrom(clazz)) {
                        list.add((T) Long.valueOf(json));
                    } else {
                        list.add(JsonUtil.jsonToObject(json, clazz));
                    }
                }
                return list;
            }

        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
        return null;
    }

    public <T> void addList(String key, List<T> list, int timeout) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Pipeline pl = jedis.pipelined();
            pl.multi();
            if (list != null && list.size() > 0) {
                for (T value : list) {
                    if (value instanceof String
                            || value instanceof Integer
                            || value instanceof Long) {
                        pl.rpush(key, value.toString());
                    } else {
                        pl.rpush(key, JsonUtil.objectToJson(value));
                    }
                }
                if (timeout > 0) {
                    pl.expire(key, timeout);
                }
                pl.exec();
                pl.close();
            }
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public <T> void updateList(String key, long index, T val) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (val instanceof String
                    || val instanceof Integer
                    || val instanceof Long) {
                jedis.lset(key, index, val.toString());
            } else {
                jedis.lset(key, index, JsonUtil.objectToJson(val));
            }

        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @SuppressWarnings("unchecked")
	public <T> T popList(String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.lpop(key);
            if (StringUtils.isNotEmpty(value)) {
                if (String.class.isAssignableFrom(clazz)) {
                    return (T) value;
                } else if (Long.class.isAssignableFrom(clazz)) {
                    return (T) Long.valueOf(value);
                } else if (Integer.class.isAssignableFrom(clazz)) {
                    return (T) Integer.valueOf(value);
                } else {
                    return JsonUtil.jsonToObject(value, clazz);
                }
            }
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
        return null;
    }

    public void delKey(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));

        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public void expireKey(String key, int time) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.expire(key, time);
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public <T> Set<T> getSets(String key, Class<? extends T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Set<String> jsonSet = jedis.smembers(key);
            if (jsonSet != null && jsonSet.size() > 0) {
                Iterator<String> it = jsonSet.iterator();
                Set<T> list = new HashSet<>();
                while (it.hasNext()) {
                    list.add(JsonUtil.jsonToObject(it.next(), clazz));
                }
                return list;
            }
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
        return null;
    }

    public <T> void addSet(String key, List<T> list, int timeout) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Pipeline pl = jedis.pipelined();
            pl.multi();
            if (list.size() > 0) {
                for (T t : list) {
                    pl.sadd(key, JsonUtil.objectToJson(t));
                }
                //设置过期时间
                if (timeout > 0) {
                    pl.expire(key, timeout);
                }
                pl.exec();
                pl.close();
            }
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));

        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public <T> void addSet(String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (value instanceof Integer
                    || value instanceof Long
                    || value instanceof String) {
                jedis.sadd(key, value.toString());
            } else {
                jedis.sadd(key, JsonUtil.objectToJson(value));
            }
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));

        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public <T> void delSet(String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (value instanceof Integer
                    || value instanceof Long
                    || value instanceof String) {
                jedis.srem(key, value.toString());
            } else {
                jedis.srem(key, JsonUtil.objectToJson(value));
            }
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));

        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    /**
     * 检查value是否是 set集合成员
     */
    public <T> boolean checkSet(String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (value instanceof Integer
                    || value instanceof Long
                    || value instanceof String) {
                return jedis.sismember(key, value.toString());
            } else {
                return jedis.sismember(key, JsonUtil.objectToJson(value));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
        return false;
    }

    public Long getSetSize(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));

        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
        return 0L;
    }

    public long setnx(String key, Long time) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            return jedis.setnx(key, time.toString());
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
        return 0;
    }

    public Long getset(String key, Long newTime) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String oldTime = jedis.getSet(key, newTime.toString());
            return Long.valueOf(oldTime);
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
        return null;
    }

    public void addHash(String key, Map<String, String> valueMap) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            for (Map.Entry<String, String> entry : valueMap.entrySet()) {
                Map<String, String> map = new HashMap<>();
                map.put(entry.getKey(), entry.getValue());
                jedis.hmset(key, map);
            }
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public String getHash(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hget(key, field);
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
        return null;
    }

    /**
     * 对key的值进行加一操作
     *
     * @param key
     * @return
     */
    public long incrKey(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.incr(key);
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
        return 0;
    }

    public long incrKey(String key, int timeout) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            long res = jedis.incr(key);
            //设置过期时间
            if (timeout > 0) {
                jedis.expire(key, timeout);
            }
            return res;
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
        return 0;
    }

    /**
     * 对key的值进行减一操作
     *
     * @param key
     * @return
     */
    public long decrKey(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.decr(key);
        } catch (Exception e) {
            logger.error("key:{} failure.exception:{}", key, ExceptionUtils.getStackTrace(e));
        } finally {
            if (null != jedis) {
                jedisPool.returnResource(jedis);
            }
        }
        return 0;
    }
}
