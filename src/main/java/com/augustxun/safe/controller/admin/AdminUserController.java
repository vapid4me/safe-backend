package com.augustxun.safe.controller.admin;

import com.augustxun.safe.annotation.AuthCheck;
import com.augustxun.safe.common.BaseResponse;
import com.augustxun.safe.common.DeleteRequest;
import com.augustxun.safe.common.ErrorCode;
import com.augustxun.safe.common.ResultUtils;
import com.augustxun.safe.constant.UserConstant;
import com.augustxun.safe.exception.BusinessException;
import com.augustxun.safe.exception.ThrowUtils;
import com.augustxun.safe.mapper.UserMapper;
import com.augustxun.safe.model.dto.user.UserAddRequest;
import com.augustxun.safe.model.dto.user.UserQueryRequest;
import com.augustxun.safe.model.dto.user.UserUpdateRequest;
import com.augustxun.safe.model.entity.Account;
import com.augustxun.safe.model.entity.User;
import com.augustxun.safe.model.vo.UserVO;
import com.augustxun.safe.service.AccountService;
import com.augustxun.safe.service.CustomerService;
import com.augustxun.safe.service.UserService;
import com.augustxun.safe.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.augustxun.safe.service.impl.UserServiceImpl.SALT;

@Slf4j
@RestController
@RequestMapping("admin/user")
@Api(tags = "B端-用户管理接口")
public class AdminUserController {
    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CustomerService customerService;
    @Resource
    private AccountService accountService;

    /**
     * 新建用户
     *
     * @param userAddRequest
     * @param request
     * @return
     */
    @Operation(summary = "新建用户")
    @PostMapping("/add")
    public BaseResponse<String> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        // 默认密码 12345678
        String encryptPassword;
        if (StringUtils.isAnyBlank(user.getUserPassword())) {
            String defaultPassword = "12345678";
            encryptPassword = DigestUtils.md5DigestAsHex((SALT + defaultPassword).getBytes());
        } else {
            encryptPassword = DigestUtils.md5DigestAsHex((SALT + user.getUserPassword()).getBytes());
        }
        String phone = userAddRequest.getUserPhone();
        user.setUserPassword(encryptPassword);
        user.setUserPhone(phone);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(String.valueOf(user.getId()));
    }

    /**
     * 删除用户
     *
     * @param deleteRequest
     * @return
     */
    @Operation(summary = "删除用户")
    @PostMapping("/delete")
    @Transactional
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        // 1.校验请求体是否为空
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2.校验 id 合法性
        long id = Long.parseLong(deleteRequest.getId());
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 3.删除user 和 customer 表的相关数据
        User user = userService.getById(id);
        Long customerId = user.getCustomerId();
        customerService.removeById(customerId);
        userService.removeById(id);
        // 4.删除 account 表相关数据
        LambdaQueryWrapper<Account> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Account::getUserId, id);
        List<Account> list = accountService.list(lambdaQueryWrapper);
        for (Account account : list) {
            Long acctNo = account.getAcctNo();
            accountService.deleteAccounts(acctNo);
        }
        return ResultUtils.success(true);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @return
     */
    @Operation(summary = "更新用户")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        return userService.updateUser(userUpdateRequest);
    }

    /**
     * 根据 id 获取用户
     *
     * @param userId
     * @param request
     * @return
     */
    @Operation(summary = "根据 ID 查询用户接口")
    @GetMapping("/get")
    public BaseResponse<User> getUserById(@RequestParam String userId, HttpServletRequest request) {
        long id = Long.parseLong(userId);
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }


    /**
     * 客户信息分页查询
     *
     * @param userQueryRequest
     * @return
     */
    @Operation(summary = "客户信息分页查询")
    @PostMapping("/list/page/customers")
    public BaseResponse<Page<UserVO>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest) {
        int current = userQueryRequest.getCurrent();
        int size = userQueryRequest.getPageSize();
        List<UserVO> userVOList = userMapper.selectUserVO(userQueryRequest);
        return ResultUtils.success(PageUtils.getPages(current, size, userVOList));
    }

    @Operation(summary = "管理员信息分页查询")
    @PostMapping("list/page/admins")
    public BaseResponse<Page<User>> listEmpByPage(@RequestBody UserQueryRequest userQueryRequest) {
        int current = userQueryRequest.getCurrent();
        int size = userQueryRequest.getPageSize();
        List<User> empList = userMapper.selectEmp(userQueryRequest);
        return ResultUtils.success(PageUtils.getPages(current, size, empList));
    }
}
