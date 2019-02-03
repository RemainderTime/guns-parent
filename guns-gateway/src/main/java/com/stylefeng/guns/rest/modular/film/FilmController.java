package com.stylefeng.guns.rest.modular.film;

import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/film/")
public class FilmController {

    /**
     * 查询影片首页信息接口（使用聚合服务）
     * API网关
     * 1，功能聚合【API聚合】
     * @return
     */
    @RequestMapping(value = "getIndex",method = RequestMethod.GET)
    public ResponseVO getIndex(){

        //获取banner信息

        //获取正在热映的电影

        //即将上映的电影

        //票房排行

        //获取受欢迎的榜单

        //获取前一百

        return null;
    }
}
