package com.ainia.ecgApi.controller.charge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ainia.ecgApi.core.crud.BaseController;
import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.charge.Card;
import com.ainia.ecgApi.service.charge.CardService;

/**
 * <p>Card controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * CardController.java
 * @author pq
 * @createdDate 2013-07-29
 * @version 0.1
 */
@Controller
@RequestMapping("/card")
public class CardController extends BaseController<Card , Long> {

    @Autowired
    private CardService cardService;
    
    @Override
    public BaseService<Card , Long> getBaseService() {
        return cardService;
    }

}
