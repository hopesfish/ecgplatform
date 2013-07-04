package com.ainia.ecgApi.controller.health;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ainia.ecgApi.core.crud.BaseController;
import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.core.crud.Condition;
import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.domain.health.HealthRule;
import com.ainia.ecgApi.domain.health.HealthRuleReply;
import com.ainia.ecgApi.service.health.HealthRuleReplyService;
import com.ainia.ecgApi.service.health.HealthRuleService;

/**
 * <p>HealthRule controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthRuleController.java
 * @author pq
 * @createdDate 2013-07-04
 * @version 0.1
 */
@Controller
@RequestMapping("/api/rule")
public class HealthRuleController extends BaseController<HealthRule , Long> {

    @Autowired
    private HealthRuleService healthRuleService;
    @Autowired
    private HealthRuleReplyService healthRuleReplyService;
    
    @Override
    public BaseService<HealthRule , Long> getBaseService() {
        return healthRuleService;
    }
    /**
     * <p>获得规则回复</p>
     * @param code
     * @return
     * ResponseEntity<List<HealthRuleReply>>
     */
    @RequestMapping(value = "{id}/replyconfig" , method = RequestMethod.GET)
    public ResponseEntity<List<HealthRuleReply>> findReply(@PathVariable("id") Long id) {
    	HealthRule healthRule = healthRuleService.get(id);
    	if (healthRule == null) {
    		return new ResponseEntity(HttpStatus.NOT_FOUND);
    	}
    	Query<HealthRuleReply> query = new Query<HealthRuleReply>();
    	query.addCondition(Condition.eq(HealthRuleReply.RULE_ID , id));
    	
    	return new ResponseEntity<List<HealthRuleReply>>(healthRuleReplyService.findAll(query) , HttpStatus.OK);
    }
    /**
     * <p>规则回复提交</p>
     * @param code
     * @return
     * ResponseEntity<List<HealthRuleReply>>
     */
    @RequestMapping(value = "{id}/replyconfig/{replyId}" , method = RequestMethod.POST)
    public ResponseEntity<HealthRuleReply> createReply(@PathVariable("id") Long id ,  HealthRuleReply reply) {
    	HealthRule healthRule = healthRuleService.get(id);
    	if (healthRule == null) {
    		return new ResponseEntity(HttpStatus.NOT_FOUND);
    	}
    	reply.setRuleId(id);
    	HealthRuleReply createdReply = healthRuleReplyService.create(reply);
    	return new ResponseEntity<HealthRuleReply>(createdReply , HttpStatus.CREATED);
    }
    /**
     * <p>规则回复修改</p>
     * @param code
     * @return
     * ResponseEntity<List<HealthRuleReply>>
     */
    @RequestMapping(value = "{id}/replyconfig" , method = RequestMethod.PUT)
    public ResponseEntity<HealthRuleReply> updateReply(@PathVariable("id") Long id ,
    												   @PathVariable("replyId") Long replyId , 
    												   HealthRuleReply reply) {
    	HealthRuleReply healthRuleReply = healthRuleReplyService.get(replyId);
    	if (healthRuleReply == null) {
    		return new ResponseEntity(HttpStatus.NOT_FOUND);
    	}
    	reply.setRuleId(id);
    	healthRuleReply = healthRuleReplyService.update(reply);
    	return new ResponseEntity<HealthRuleReply>(healthRuleReply , HttpStatus.CREATED);
    }
    /**
     * <p>规则提交</p>
     * @param code
     * @return
     * ResponseEntity<List<HealthRuleReply>>
     */
    @RequestMapping(value = "{id}/replyconfig/{replyId}" , method = RequestMethod.DELETE)
    public ResponseEntity<HealthRuleReply> deleteReply(@PathVariable("id") Long id , 
    											   	   @PathVariable("replyId") Long replyId,
    											   	   HealthRuleReply reply) {
    	HealthRuleReply healthRuleReply = healthRuleReplyService.get(replyId);
    	if (healthRuleReply == null) {
    		return new ResponseEntity(HttpStatus.NOT_FOUND);
    	}
    	healthRuleReplyService.delete(replyId);
    	return new ResponseEntity<HealthRuleReply>(HttpStatus.CREATED);
    }
}
