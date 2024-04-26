package com.augustxun.safe.controller.admin;

import com.augustxun.safe.annotation.AuthCheck;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.UserConstant;
import com.augustxun.safe.model.dto.personal.PersonalQueryRequest;
import com.augustxun.safe.model.entity.Personal;
import com.augustxun.safe.service.PersonalService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "B端-PersonalLoan账户管理接口")
@RequestMapping("admin/personal")
public class PersonalController {
    @Resource
    private PersonalService personalService;
    /**
     * Personal账户信息分页查询
     *
     * @param personalQueryRequest
     * @return
     */
    @Operation(summary = "Personal账户信息分页查询")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Personal>> listPersonalByPage(@RequestBody PersonalQueryRequest personalQueryRequest) {
        long current = personalQueryRequest.getCurrent();
        long size = personalQueryRequest.getPageSize();
        Page<Personal> personalPage = personalService.page(new Page<>(current, size), personalService.getQueryWrapper(personalQueryRequest));
        return ResultUtils.success(personalPage);
    }
}
