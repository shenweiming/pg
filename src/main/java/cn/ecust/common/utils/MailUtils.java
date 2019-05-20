package cn.ecust.common.utils;

import cn.ecust.common.enums.ExceptionEnum;
import cn.ecust.common.exception.LyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

/**
 * @Auther: SWM
 * @Date: 2019/4/20 20:28
 * @Description: 
 */
@Slf4j
@Component
public class MailUtils  {
    @Autowired
    private JavaMailSender javaMailSender;
    public void sendEmail(String to,String code){
        try{
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper=new MimeMessageHelper(mimeMessage, true,"GBK");
        messageHelper.setFrom("17765124320@163.com");
        messageHelper.setTo(to);
        messageHelper.setSubject("来自宝洁的激活邮件");
        String context="<a href=\"www.baidu.com+\">激活请点击</a>";
        //String context="<a href=\"localhost:8081/api/user/changeStatus?code="+code+"+\"?mail="+to+"+\">激活请点击</a>";
        messageHelper.setText(context, true);
        javaMailSender.send(mimeMessage);
        }
        catch (Exception e){
            log.error("发送邮件出错：{}"+e);
            throw new LyException(ExceptionEnum.ERROR_MAIL_ADDRESS);
        }
    }
}
