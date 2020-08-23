package com.chrischen.ad.service.impl;

import com.chrischen.ad.constant.Constants;
import com.chrischen.ad.dao.AdUserRepository;
import com.chrischen.ad.entity.AdUser;
import com.chrischen.ad.exception.AdException;
import com.chrischen.ad.service.IUserService;
import com.chrischen.ad.utils.CommonUtils;
import com.chrischen.ad.vo.CreateUserRequest;
import com.chrischen.ad.vo.CreateUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Chris Chen
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private final AdUserRepository userRepository;

    public UserServiceImpl(AdUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) throws AdException {

        if(!request.validate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        AdUser oldUser = userRepository.findByUsername(request.getUsername());
        if (oldUser != null){
            throw new AdException(Constants.ErrorMsg.SAME_NAME_ERROR);
        }

        AdUser newUser = userRepository.save(new AdUser(
            request.getUsername(), CommonUtils.md5(request.getUsername())
        ));
        return new CreateUserResponse(
                newUser.getId(),
                newUser.getUsername(),
                newUser.getToken(),
                newUser.getCreateTime(),
                newUser.getUpdateTime()
        );
    }
}
