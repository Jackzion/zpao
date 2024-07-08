package com.ziio.zpao.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ziio.zpao.model.domain.Tag;
import com.ziio.zpao.model.domain.User;
import com.ziio.zpao.service.TagService;
import com.ziio.zpao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存预热任务
 *
 * 
 * 
 */
@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserService userService;

    @Resource
    private TagService tagService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    // 重点用户(白名单，定死)--> todo 设置user最近登录时间字段？ 判断白名单?
    private List<Long> mainUserList = Arrays.asList(1L);

    // redisson 自帶watchDog -》 判斷線程是否純在，純在則續期
    // 每天执行，预热推荐用户(缓存预热) ,
    @Scheduled(cron = "0 31 0 * * *")
    public void doCacheRecommendUser() {
        RLock lock = redissonClient.getLock("yupao:precachejob:docache:lock");
        try {
            // 只有一个线程能获取到锁
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                System.out.println("getLock: " + Thread.currentThread().getId());
                for (Long userId : mainUserList) {
                    // 从数据库拿到分页
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
                    String redisKey = String.format("yupao:user:recommend:%s", userId);

                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
                    // 直接将UserPage写缓存
                    try {
                        valueOperations.set(redisKey, userPage, 30000, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                        log.error("redis set key error", e);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("doCacheRecommendUser error", e);
        } finally {
            // 只能释放自己的锁 isHeldByCurrentThread -> 判断当前线程名
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }
    // 每天执行一次，清除多余tags(counts == 0)
    @Scheduled(cron = "0 0 0 1/1 * ? ")
    public void deleteNonTags(){
        RLock lock = redissonClient.getLock("yupao:precachejob:deleteTags:lock");
        try {
            // 只有一个线程能获取到锁
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                System.out.println("getLock: " + Thread.currentThread().getId());
                // 从数据拿到 tagList
                List<Tag> list = tagService.list();
                // todo ： 流式操作
                for(Tag tag : list) {
                    if(tag.getCounts()<=0){
                        tag.setIsDelete(1);
                    }
                }
                tagService.updateBatchById(list);
            }
        } catch (InterruptedException e) {
            log.error("doDeleteNonTags error", e);
        } finally {
            // 只能释放自己的锁 isHeldByCurrentThread -> 判断当前线程名
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }

}
