package cn.ecust.common.utils;
import cn.ecust.common.enums.ExceptionEnum;
import cn.ecust.common.exception.LyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.util.concurrent.TimeUnit;


/**
 * @Auther: SWM
 * @Date: 2019/4/20 20:28
 * @Description:
 */
@Slf4j
@Component
public class VerifyMailUtils  {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String MAIL_VERIFY_PREFIX="verify:mail:";
    public void sendEmail(String to,String code){
        String key=MAIL_VERIFY_PREFIX+to;
        String lastTime = redisTemplate.opsForValue().get(key);
        log.info("打印上次的时间：{}", lastTime);
        //判断redis中是否还存储着邮件ip，如果存着，并且时间小于1000ms，直接返回
        if(StringUtils.isNotBlank(lastTime)){
            Long last = Long.valueOf(lastTime);
            log.info("之前存储的时间：{}",last);
            long currentTimeMillis = System.currentTimeMillis();
            log.info("当前时间：{}",currentTimeMillis);
            if(System.currentTimeMillis()-last<60000){
                log.info("直接退出");
                return;
            }
        }
        redisTemplate.opsForValue().set(key, String.valueOf(System.currentTimeMillis()),1,TimeUnit.MINUTES);
        log.info("开始发送邮件");
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper=new MimeMessageHelper(mimeMessage, true,"GBK");
            messageHelper.setFrom("17765124320@163.com");
            messageHelper.setTo(to);
            messageHelper.setSubject("来自宝洁的验证邮件");
            String context="您的验证码为:"+code;
            messageHelper.setText(context);
            javaMailSender.send(mimeMessage);
            //发送成功后，把邮箱ip存储到redis中,持续时长1分钟， 一分钟内如果再次发送验证码，则直接返回
        }
        catch (Exception e){
            log.error("发送邮件出错：{}"+e);
            throw new LyException(ExceptionEnum.ERROR_MAIL_ADDRESS);
        }
    }
}
