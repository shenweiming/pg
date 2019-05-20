package cn.ecust.user.service;

import cn.ecust.common.enums.ExceptionEnum;
import cn.ecust.common.exception.LyException;
import cn.ecust.common.utils.*;
import cn.ecust.user.dao.UserRepository;
import cn.ecust.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: SWM
 * @Date: 2019/4/20 14:11
 * @Description: 
 */
@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SmsUtils smsUtils;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private VerifyMailUtils verifyMailUtils;
    private static String VERIFY_STATUS = "user:verify:mail:";
    private static String VERIFY_CODE_PREFIX = "user:verify:mail:code:"; //邮件验证码的前缀
    private static final String KEY_PREFIX = "user:code:phone:";     //短信存储在redis中的前缀
    private static final String KEY_PREFIX2 = "leyou:user:info";
    public Boolean checkData(String data, Integer type) {
        switch (type) {
            case 1:
                log.info("校验的数据为:{}", data);
                return userRepository.findByUsername(data)==null;
            case 2:
                return userRepository.findByMail(data)==null;
            default:
                throw new LyException(ExceptionEnum.INVALID_USER_DATA_TYPE);
        }
    }

   /** public void sendVerifyCode(String phone) {
        String key = KEY_PREFIX + phone;
        //生成6位随机验证码
        String code = NumberUtils.generateCode(6);
        log.info("生成的验证码是：{}", code);
        if (StringUtils.isBlank(phone)) {
            return;
        }
        smsUtils.sendSms(phone, JsonUtils.serialize(code));
        //发送验证码成功后，将验证码存入redis中，5分钟有效
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
    } **/
    public void sendVerifyMailCode(String mail) {
        //发送邮件验证码，先把验证码存储到redis中，然后发送邮件
        String code = NumberUtils.generateCode(6); //生成6位验证码
        log.info("验证码为:{}",code);
        String key = VERIFY_CODE_PREFIX + mail;
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
        verifyMailUtils.sendEmail(mail, code);     //发送验证邮件
        //存储验证码，key为前缀加上邮箱ip,5分钟后自动删除
    }

        //注册的实现步骤，首先校验短信验证码，然后生成盐，对密码进行加密
        // 将所有信息存入数据库，删除redis中的验证码
        public Boolean register(User user, String code) {
            String key = VERIFY_CODE_PREFIX + user.getMail();
            log.info("key值为:{}", key);
            //1.从redis中取出验证码
            String codeCache = redisTemplate.opsForValue().get(key);
            log.info("存储的验证码为:{}", codeCache);
            //2.检查验证码是否正确
            if(codeCache==null){
                return false;
            }
            if(!codeCache.equals(code)){
                //不正确，返回
                return false;
            }
            user.setId(null);
            //先从数据库中查询邮箱，如果邮箱已存在，确定是忘记密码，获得用户名
            User lastUser=this.userRepository.findByMail(user.getMail());
            if(lastUser!=null){
                log.info("邮箱已经存在");
                user.setId(lastUser.getId());
                user.setUsername(lastUser.getUsername());
            }
            user.setCreated(new Date());
            //3.密码加密
            String encodePassword = CodecUtils.passwordBcryptEncode(user.getUsername().trim(),user.getPassword().trim());
            user.setPassword(encodePassword);
            user.setSalt("1111");
            //4.写入数据库
            //开始向数据库中存用户信息
            log.info("打印查询信息:{}",userRepository.save(user)==null);
            boolean result = !(userRepository.save(user)==null);
            //5.如果注册成功，则删掉redis中的code
            //存储到数据库中
            if (result){
                try{
                    redisTemplate.delete(VERIFY_CODE_PREFIX + user.getMail());
                }catch (Exception e){
                    log.error("删除缓存验证码失败，code:{}",code,e);
                }
            }
            return result;
        }

        public User queryUser(String username, String password) {
            //1.缓存中查询
            User user;
                //在缓存中没有查到，去数据库查,查到放入缓存当中
                user = this.userRepository.findByUsername(username);
            //2.校验用户名
            if (user == null){
                return null;
            }
            log.info("用户信息:{}",user);
            //3. 校验密码
            boolean result = CodecUtils.passwordConfirm(username + password,user.getPassword());
            if (!result){
                log.info("密码不正确");
                return null;

            }
            //4.用户名密码都正确
            return user;
        }
    public boolean changePassword(String name,String oldpassword,String newpassword){
        User user=this.userRepository.findByUsername(name);
        if (user == null){
            return false;
        }
        log.info("用户信息:{}",user);

        boolean result = CodecUtils.passwordConfirm(name + oldpassword,user.getPassword());
        if (!result){
            log.info("原密码不正确");
            return false;
        }
        //修改密码
        String encodePassword = CodecUtils.passwordBcryptEncode(user.getUsername().trim(),newpassword.trim());
        user.setPassword(encodePassword);
        User save = this.userRepository.save(user);
        log.info("修改后的用户:{}"+save);
        return true;
    }

    public String findNameByMail(String username) {
        User user = this.userRepository.findByMail(username);
        if(user==null){
            return null;
        }
        return user.getUsername();
    }
}