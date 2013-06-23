package com.ainia.ecgApi.controller.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ainia.ecgApi.core.crud.BaseController;
import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.service.sys.UserService;

/**
 * <p>User controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * UserController.java
 * @author pq
 * @createdDate 2013-06-22
 * @version 0.1
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController<User , Long> {

    @Autowired
    private UserService userService;
    
    @Override
    public BaseService<User , Long> getBaseService() {
        return userService;
    }

}
