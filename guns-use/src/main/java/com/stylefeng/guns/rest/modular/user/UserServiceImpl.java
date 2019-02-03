package com.stylefeng.guns.rest.modular.user;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserModel;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.MoocUserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocUserT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Service(interfaceClass = UserAPI.class,loadbalance = "roundrobin")
public class UserServiceImpl implements UserAPI{

    @Autowired
    MoocUserTMapper moocUserTMapper;

    @Override
    public int login(String username, String password) {
        //通过用户名获取数据
        MoocUserT moocUserT=new MoocUserT();
        moocUserT.setUserName(username);

        MoocUserT result=moocUserTMapper.selectOne(moocUserT);
        //获取数据库中的密码进行比较
        if(result!=null&&result.getUuid()>0){
            String md5Pwd=MD5Util.encrypt(password);
            if(md5Pwd.equals(result.getUserPwd())){
                return result.getUuid();
            }
        }
        return 0;
    }

    @Override
    public boolean register(UserModel userModel) {
        //将注册信息实体装换为数据实体
        MoocUserT moocUserT=new MoocUserT();
        moocUserT.setUserName(userModel.getUsername());
        moocUserT.setEmail(userModel.getEmail());
        moocUserT.setAddress(userModel.getAddress());
        moocUserT.setUserPhone(userModel.getPhone());

        //创建时间和修改时间-》current_timestamp

        //数据加密【MD5混淆加密+盐值】
        String md5Password= MD5Util.encrypt(userModel.getPassword());
        moocUserT.setUserPwd(md5Password);

        //将数据实体存入数据库
        Integer insert=moocUserTMapper.insert(moocUserT);

        if(insert>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean checkUsername(String username) {
        EntityWrapper entityWrapper=new EntityWrapper();

        entityWrapper.eq("user_name", username);

        Integer result=moocUserTMapper.selectCount(entityWrapper);

        if(result!=null&&result>0){
            return false;
        }else {
            return true;
        }
    }

    public UserInfoModel getMd2UserInfo(MoocUserT moocUserT){
        UserInfoModel userInfoModel=new UserInfoModel();

        userInfoModel.setAddress(moocUserT.getAddress());
        userInfoModel.setBeginTime(moocUserT.getBeginTime().getTime());
        userInfoModel.setBiography(moocUserT.getBiography());
        userInfoModel.setBirthday(moocUserT.getBirthday());
        userInfoModel.setEmail(moocUserT.getEmail());
        userInfoModel.setHeadAddress(moocUserT.getHeadUrl());
        userInfoModel.setLifeState(moocUserT.getLifeState()+"");
        userInfoModel.setNickname(moocUserT.getNickName());
        userInfoModel.setPhone(moocUserT.getUserPhone());
        userInfoModel.setSex(moocUserT.getUserSex());
        userInfoModel.setUpdateTime(moocUserT.getUpdateTime().getTime());
        userInfoModel.setUsername(moocUserT.getUserName());


        return userInfoModel;
    }

    @Override
    public UserInfoModel getUserInfo(int uuid) {
        //根据主键查询MoocUserT
        MoocUserT moocUserT=moocUserTMapper.selectById(uuid);

        //将moocUserT转换为userInfoModel
        UserInfoModel userInfoModel=getMd2UserInfo(moocUserT);

        //返回userInfoModel
        return userInfoModel;
    }

    public Date time2Date(long time){
        Date date=new Date(time);

        return date;
    }

    //更新用户信息
    @Override
    public UserInfoModel updateUserInfo(UserInfoModel userInfoModel) {
        //将修改的数据转换到MoocUserT中
        MoocUserT moocUserT=new MoocUserT();
        moocUserT.setUuid(userInfoModel.getUuid());
        moocUserT.setNickName(userInfoModel.getNickname());
        moocUserT.setLifeState(Integer.parseInt(userInfoModel.getLifeState()));
        moocUserT.setBirthday(userInfoModel.getBirthday());
        moocUserT.setBiography(userInfoModel.getBiography());
        moocUserT.setBeginTime(null);
        moocUserT.setHeadUrl(userInfoModel.getHeadAddress());
        moocUserT.setEmail(userInfoModel.getEmail());
        moocUserT.setAddress(userInfoModel.getAddress());
        moocUserT.setUserPhone(userInfoModel.getPhone());
        moocUserT.setUserSex(userInfoModel.getSex());
        moocUserT.setUpdateTime(null);

        //将数据存入数据库中
        Integer result=moocUserTMapper.updateById(moocUserT);
        if(result>0){

            //查询数据
            UserInfoModel info=getUserInfo(moocUserT.getUuid());

            //返回数据
            return info;

        }else{
            return null;
        }
    }
}
