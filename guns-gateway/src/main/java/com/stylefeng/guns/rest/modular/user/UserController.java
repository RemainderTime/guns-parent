package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserModel;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Reference(interfaceClass = UserAPI.class,check = false)
    private UserAPI userAPI;

    //注册
    @RequestMapping(value = "register",method = RequestMethod.POST)
    public ResponseVO register(UserModel userModel){

        if(userModel.getUsername()==null||userModel.getUsername().trim().length()==0){
            return  ResponseVO.serverFail("用户名不能为空!");
        }
        else if(userModel.getPassword()==null||userModel.getPassword().trim().length()==0){
            return ResponseVO.serverFail("密码不能为空!");
        }

        boolean isSuccess=userAPI.register(userModel);
        if(isSuccess){
            return ResponseVO.success("注册成功");
        }else{
            return  ResponseVO.serverFail("注册失败!");
        }
    }

    //判断用户名是否存在
    @RequestMapping(value = "check",method = RequestMethod.POST)
    public ResponseVO check(@RequestParam("username") String userName) {

        if (userName != null || userName.trim().length() != 0) {
            boolean notExists = userAPI.checkUsername(userName);
            System.out.println("校验:"+notExists);
            if (notExists) {
                return ResponseVO.success("用户名不存在");
            } else {
                return ResponseVO.serverFail("用户名已存在");
            }
        } else {
            return ResponseVO.serverFail("用户名不能为空!");
        }
    }

    //注销用户
    @RequestMapping(value = "logout",method = RequestMethod.GET)
    public ResponseVO logout() {

        /**
         * 应用：
         * 1，前端存储JWT[七天]：JWT刷新
         * 2，服务器会存储活动用户【30min】
         * 3，JWT里的userID为Key，查询活跃用户
         * 退出：
         * 1，前端删除JWT
         * 2,后端服务器删除活跃的用户缓存
         * 现状
         * 1,前端删除掉JWT
         * 预留：后边使用redis缓存
         */

        return ResponseVO.serverFail("用户名不能为空!");
    }
    //获取用户信息
    @RequestMapping(value = "getUserInfo",method = RequestMethod.GET)
    public ResponseVO getUserInfo() {

        String userId= CurrentUser.getCurrentUser();

        if(userId!=null||userId.trim().length()>0){
            int id=Integer.parseInt(userId);
            UserInfoModel userInfoModel=userAPI.getUserInfo(id);
            if(userInfoModel!=null){
                return ResponseVO.success(userInfoModel);
            }else{
                return ResponseVO.appFail("用户信息查询失败!");
            }
        }else{
            return ResponseVO.serverFail("用户未登录!");
        }
    }
    //更新用户信息
    @RequestMapping(value = "updateUserInfo",method = RequestMethod.POST)
    public ResponseVO updateUserInfo(UserInfoModel userInfoModel) {

        String userId= CurrentUser.getCurrentUser();

        System.out.println("哈哈"+userId);
        if(userId!=null||userId.trim().length()>0){
            int id=Integer.parseInt(userId);

            if(id!=userInfoModel.getUuid()){
                return ResponseVO.serverFail("请重新更新用户信息!");
            }
            UserInfoModel result=userAPI.updateUserInfo(userInfoModel);
            if(result!=null){
                return ResponseVO.success(result);
            }else{
                return ResponseVO.appFail("用户信息查询失败!");
            }
        }else{
            return ResponseVO.serverFail("用户未登录!");
        }
    }
}
