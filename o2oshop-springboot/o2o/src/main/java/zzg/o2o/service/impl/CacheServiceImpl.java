package zzg.o2o.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzg.o2o.cache.JedisUtil;
import zzg.o2o.service.CacheService;

import java.util.Set;

@Service
public class CacheServiceImpl implements CacheService {
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Override
    public void removeFromCache(String keyPrefix) {
        Set<String> keySet=jedisKeys.keys(keyPrefix+"*");
        for(String key:keySet){
            jedisKeys.del(key);
        }
    }
}
