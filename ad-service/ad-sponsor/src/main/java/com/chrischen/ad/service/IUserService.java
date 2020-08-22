package com.chrischen.ad.service;

import com.chrischen.ad.exception.AdException;
import com.chrischen.ad.vo.CreateUserRequest;
import com.chrischen.ad.vo.CreateUserResponse;

/**
 * Created by Chris Chen
 */
public interface IUserService {
    /**
     * create user
     * @param request
     * @return
     * @throws AdException
     */
    CreateUserResponse createUser(CreateUserRequest request) throws AdException;
}
