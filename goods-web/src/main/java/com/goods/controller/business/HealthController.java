package com.goods.controller.business;

import com.github.pagehelper.PageHelper;
import com.goods.business.service.HealthService;
import com.goods.common.model.business.Health;
import com.goods.common.model.system.User;
import com.goods.common.response.ResponseBean;
import com.goods.common.utils.ListPageUtils;
import com.goods.common.vo.business.HealthVO;
import com.goods.common.vo.system.PageVO;
import com.goods.system.service.UserService;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lx
 * @createTime 2023/08/09 11:19:40
 * @className HealthController
 */
@RestController
@RequestMapping("/business/health")
public class HealthController {
    @Autowired
    private HealthService healthService;
    @Autowired
    private UserService userService;

    /**
     * 提示打卡
     *
     * @param map
     * @return
     */
    @GetMapping("/isReport")
    public ResponseBean isReport(Map map) {
        // 获取用户名
        String username = (String) map.get("username");
        //根据用户名查用户Id
        User user = userService.findUserByName(username);
        // 判断 user 是否为空
        if (null != user) {
            Health health = healthService.isReport(user.getId());
            return ResponseBean.success(health);
        }
        return ResponseBean.error("用户不存在");
    }

    @PostMapping("/report")
    public ResponseBean report(@RequestBody Health health, @RequestParam(value = "username", required = false) String username) {
        User user = userService.findUserByName(username);
        if (null != user) {
            health.setUserId(user.getId());
            health.setCreateTime(new Date());
            Boolean report = healthService.report(health);
            if (report) {
                return ResponseBean.success();
            }
        }
        return ResponseBean.error(false);
    }

    /**
     * 历史记录
     *
     * @param pageSize
     * @param pageNum
     * @param username
     * @return
     */
    //  http://www.localhost:8989/business/health/history?pageSize=4&pageNum=1
    @GetMapping("/history")
    public ResponseBean history(@RequestParam(value = "pageSize", required = false) Integer pageSize,
                                @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                @RequestParam(value = "username", required = false) String username) {
        // 根据用户名查询用户Id
        User user = userService.findUserByName(username);
        //调用服务层查询方法
        List<Health> healthList = healthService.history(user.getId());
        //分页
        List<Health> page = ListPageUtils.page(healthList, pageSize, pageNum);
        Map<String, Object> result = new HashMap<>();
        //rows 物资来源列表
        result.put("rows", page);
        //total 列表数，用于分页
        result.put("total", healthList.size());
        return ResponseBean.success(result);
    }
}
