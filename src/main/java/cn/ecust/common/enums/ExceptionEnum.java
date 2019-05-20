package cn.ecust.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Auther: SWM
 * @Date: 2019/4/13 15:46
 * @Description: 
 */
@Getter
@NoArgsConstructor
public enum ExceptionEnum {
     BRAND_NOT_FIND(404, "品牌未查到"),
     PRICE_CANNOT_BE_NULL(400, "价格不能为空"),
     CATEGORY_NOT_FIND(404, "商品分类未查到"),
     UPLOAD_FILE_ERROR(500, "文件上传失败"),
     INVALID_FILE_TYPE(500, "无效的文件类型"),
     INVALID_USER_DATA_TYPE(400, "用户数据类型无效"),
     ERROR_MAIL_ADDRESS(500, "错误的邮箱地址"),
     UNAUTHORIZED(403,"用户未登陆")
     ;
     private int code;
     private String msg;

     ExceptionEnum(int code, String msg) {
          this.code = code;
          this.msg = msg;
     }
}