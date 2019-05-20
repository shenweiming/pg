package cn.ecust.common.utils;/**
 * @Auther: SWM
 * @Date: 2019/4/20 15:20
 * @Description: 
 */

import cn.ecust.common.prop.SmsProperties;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;


/**
 * @Author: 98050
 * @Time: 2018-10-22 18:59
 * @Feature: 短信服务工具类
 */
@Component
@EnableConfigurationProperties(SmsProperties.class)
    public class SmsUtils {
    @Autowired
    private SmsProperties properties;
    /**
     * 产品名称:云通信短信API产品,开发者无需替换
     */
    static final String product = "Dysmsapi";
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX="sms:phone:";
    /**
     * 产品域名,开发者无需替换
     */
    static final String domain = "dysmsapi.aliyuncs.com";
    static final Logger logger = LoggerFactory.getLogger(SmsUtils.class);
    public SendSmsResponse sendSms(String phone,String code) {
        String key=KEY_PREFIX+phone;
        //获取之前存储在redis中的验证码
        String lastTime = redisTemplate.opsForValue().get(key);
        if(StringUtils.isNotBlank(lastTime)){
            Long last = Long.valueOf(lastTime);
            if(System.currentTimeMillis()-last<60000){
                return null;
            }
        }
        try {
            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", properties.getAccessKeyId(), properties.getAccessKeySecret());
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            request.setMethod(MethodType.POST);
            //必填:待发送手机号
            request.setPhoneNumbers(phone);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(properties.getSignName());
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(properties.getVerifyCodeTemplate());
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam(code);

            //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");

            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId("123456");

            //hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if(!"OK".equals(sendSmsResponse.getMessage())){
                logger.error("[短信发送失败]：{}",phone);
            }
            //发送短信成功后，写入redis,一分钟后自动删除
            redisTemplate.opsForValue().set(key,String.valueOf(System.currentTimeMillis()),1,TimeUnit.MINUTES);
            return sendSmsResponse;
    }
    catch (Exception e){
        logger.error("[短信服务发送异常]：{}，原因：{}",phone,e);
    }
    return null;
}
}