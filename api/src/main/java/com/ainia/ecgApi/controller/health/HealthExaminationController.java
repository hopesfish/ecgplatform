package com.ainia.ecgApi.controller.health;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ainia.ecgApi.core.crud.BaseController;
import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.core.security.AuthUser;
import com.ainia.ecgApi.core.security.AuthenticateService;
import com.ainia.ecgApi.core.web.AjaxResult;
import com.ainia.ecgApi.domain.health.HealthExamination;
import com.ainia.ecgApi.domain.health.HealthReply;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.service.common.UploadService;
import com.ainia.ecgApi.service.common.UploadService.Type;
import com.ainia.ecgApi.service.health.HealthExaminationService;
import com.ainia.ecgApi.service.health.HealthReplyService;
import com.ainia.ecgApi.service.sys.UserService;
import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * <p>HealthExamination controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthExaminationController.java
 * @author pq
 * @createdDate 2013-07-07
 * @version 0.1
 */
@Controller
@RequestMapping("/api/examination")
public class HealthExaminationController extends BaseController<HealthExamination , Long> {

    @Autowired
    private HealthExaminationService healthExaminationService;
    @Autowired
    private HealthReplyService healthReplyService;
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private UserService userService;
    
    @Override
    public BaseService<HealthExamination , Long> getBaseService() {
        return healthExaminationService;
    }

	@Override
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<AjaxResult> create(HealthExamination domain) {
		return new ResponseEntity<AjaxResult>(HttpStatus.METHOD_NOT_ALLOWED);
	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseBody
	public ResponseEntity<AjaxResult> delete(Long id) {
		return new ResponseEntity<AjaxResult>(HttpStatus.METHOD_NOT_ALLOWED);
	}
    /**
     * <p>创建健康监测回复</p>
     * @param id
     * @param reply
     * @return
     * ResponseEntity<AjaxResult>
     */
	@RequestMapping(value = "/{examinationId}/reply" , method = RequestMethod.POST , produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public ResponseEntity<AjaxResult> reply(@PathVariable("examinationId") Long id , HealthReply reply) {
    	HealthExamination examination = healthExaminationService.get(id);
    	if (examination == null) {
    		return new ResponseEntity<AjaxResult>(HttpStatus.NOT_FOUND);
    	}
    	AuthUser authUser = authenticateService.getCurrentUser();
    	reply.setEmployeeId(authUser.getId());
    	reply.setExaminationId(id);
    	
    	healthExaminationService.reply(id , reply);
    	return new ResponseEntity<AjaxResult>(HttpStatus.CREATED);
    }

    /**
     * <p>获取健康监测回复</p>
     * @param id
     * @param reply
     * @return
     * ResponseEntity<AjaxResult>
     */
	@RequestMapping(value = "/{id}/reply" , method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public ResponseEntity<?> findReply(@PathVariable("id") Long id) {
    	HealthExamination examination = healthExaminationService.get(id);
    	if (examination == null) {
    		return new ResponseEntity(HttpStatus.NOT_FOUND);
    	}
    	Query<HealthReply> query = new Query();
    	query.eq(HealthReply.EXAMINATION_ID , id);
    	
    	return new ResponseEntity(healthReplyService.findAll(query) , HttpStatus.OK);
    }
	
	/**
	 * <p>统计用户健康测试平均值</p>
	 * @param id
	 * @param start
	 * @param end
	 * @return
	 * ResponseEntity
	 */
	@RequestMapping(value = "/avg" , method = RequestMethod.GET)
	public ResponseEntity testStatisticsByUserAndDay(
		@RequestParam(value = "userId" , required = true) Long userId ,
		@RequestParam(value = "start" , required = false) Date start ,
		@RequestParam(value = "end" , required = false) Date end
	) {
		// 不指定start的话，用前7天数据
		if (start == null) {
			start = new DateTime().minusDays(7).toDate();
		}
		// 不指定end,直至明天
		if (end == null) {
			end = new DateTime().plusDays(1).toDate();
		}
		return new ResponseEntity( healthExaminationService.statisticsByUserAndDay(userId , start, end) , HttpStatus.OK);
	}
}
