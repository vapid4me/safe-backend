package com.augustxun.safe.model.dto.account;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 *

 */
@Data
@ApiModel
public class AccountUpdateRequest implements Serializable {
    private String acctNo;
    private String acctName;
    private String zipcode;
    private String unit;
    private String street;
    private String city;
    private String state;
}