package com.augustxun.safe.service.impl;

import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.CommonConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.mapper.CheckingMapper;
import com.augustxun.safe.model.dto.checking.CheckingQueryRequest;
import com.augustxun.safe.model.entity.Account;
import com.augustxun.safe.model.entity.Checking;
import com.augustxun.safe.model.vo.CheckingAccountVO;
import com.augustxun.safe.service.AccountService;
import com.augustxun.safe.service.CheckingService;
import com.augustxun.safe.service.UserService;
import com.augustxun.safe.utils.SqlUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author augustxun
 * @description 针对表【CheckingQueryRequest(账户)】的数据库操作Service实现
 * @createDate 2024-04-21 11:59:05
 */
@Service
public class CheckingServiceImpl extends ServiceImpl<CheckingMapper, Checking>
        implements CheckingService {
    @Override
    public BaseResponse<String> addCheckingAccount(Long newAccountNo) {
        Checking checking = new Checking();
        checking.setAcctNo(newAccountNo);
        checking.setServiceFee(new BigDecimal("300.00"));
        checking.setBalance(new BigDecimal("0"));
        //        CheckingQueryRequest.setCustomerId(userCustomerId);
        this.save(checking); // 保存账户信息到 CheckingQueryRequest 表
        return ResultUtils.success("创建成功");
    }
    @Override
    public QueryWrapper<Checking> getQueryWrapper(CheckingQueryRequest checkingQueryRequest) {
        if (checkingQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String sortField = checkingQueryRequest.getSortField();
        String sortOrder = checkingQueryRequest.getSortOrder();
        QueryWrapper<Checking> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }
}




