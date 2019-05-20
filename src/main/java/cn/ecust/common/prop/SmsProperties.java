package cn.ecust.common.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Auther: SWM
 * @Date: 2019/4/20 16:06
 * @Description: 
 */
@ConfigurationProperties(prefix = "ly.sms")
@Data
public class SmsProperties {
    private String accessKeyId;
    private String accessKeySecret;
    private String signName;
    private String verifyCodeTemplate;

}
