package com.augustxun.safe.controller.admin;

import com.augustxun.safe.annotation.AuthCheck;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.UserConstant;
import com.augustxun.safe.model.dto.home.HomeQueryRequest;
import com.augustxun.safe.model.entity.Home;
import com.augustxun.safe.service.HomeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "B端-HomeLoan账户接口")
@RequestMapping("admin/home")
public class HomeController {
    @Resource
    private HomeService homeService;
    /**
     * HOME账户信息分页查询
     *
     * @param homeQueryRequest
     * @return
     */
    @Operation(summary = "HOME账户信息分页查询")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Home>> listHomeByPage(@RequestBody HomeQueryRequest homeQueryRequest) {
        int current = homeQueryRequest.getCurrent();
        int size = homeQueryRequest.getPageSize();
        Page<Home> homePage = homeService.page(new Page<>(current, size), homeService.getQueryWrapper(homeQueryRequest));
        return ResultUtils.success(homePage);
    }
}
