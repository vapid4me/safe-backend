package com.augustxun.safe.controller.admin;

import com.augustxun.safe.annotation.AuthCheck;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.UserConstant;
import com.augustxun.safe.model.dto.savings.SavingsQueryRequest;
import com.augustxun.safe.model.entity.Savings;
import com.augustxun.safe.service.SavingsService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "B端-Savings账户管理接口")
@RequestMapping("admin/savings")
public class SavingsController {
    @Resource
    private SavingsService savingsService;
    /**
     * Savings账户信息分页查询
     *
     * @param savingsQueryRequest
     * @return
     */
    @Operation(summary = "Savings账户信息分页查询")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Savings>> listSavingsByPage(@RequestBody SavingsQueryRequest savingsQueryRequest) {
        long current = savingsQueryRequest.getCurrent();
        long size = savingsQueryRequest.getPageSize();
        Page<Savings> savingsPage = savingsService.page(new Page<>(current, size), savingsService.getQueryWrapper(savingsQueryRequest));
        return ResultUtils.success(savingsPage);
    }
}