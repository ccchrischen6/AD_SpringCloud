package com.chrischen.ad.controller;

import com.alibaba.fastjson.JSON;
import com.chrischen.ad.exception.AdException;
import com.chrischen.ad.service.IUserService;
import com.chrischen.ad.vo.CreateUserRequest;
import com.chrischen.ad.vo.CreateUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Chris Chen
 */
@RestController
@Slf4j
public class UserOpController {
    private final IUserService userService;

    public UserOpController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * since context path has been set in application.yml,
     * the full path should be /ad-sponsor/create/user
     * @return
     */

    @PostMapping("/create/user")
    public CreateUserResponse createUser(@RequestBody CreateUserRequest request) throws AdException
    {
        log.info("ad-sponsor: createUser -> {}",
                JSON.toJSONString(request));
        return userService.createUser(request);
    }
}
