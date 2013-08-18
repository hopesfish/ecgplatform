package com.ainia.ecgApi.controller.apk;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.multipart.MultipartFile;

import com.ainia.ecgApi.core.crud.Page;
import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.core.crud.Query.OrderType;
import com.ainia.ecgApi.core.security.AuthUser;
import com.ainia.ecgApi.core.security.AuthenticateService;
import com.ainia.ecgApi.core.web.AjaxResult;
import com.ainia.ecgApi.domain.apk.Apk;
import com.ainia.ecgApi.service.apk.ApkService;

/**
 * <p>Apk controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * ApkController.java
 * @author pq
 * @createdDate 2013-08-07
 * @version 0.1
 */
@Controller
@RequestMapping("/api/apk")
public class ApkController {

    @Autowired
    private ApkService apkService;
    
    @Autowired
    private AuthenticateService authenticateService;
	/**
	 * <p>load the domain list</p>
	 * Page<T>
	 * @param query
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Page<Apk> index(Query<Apk> query) {
		query.addOrder(Apk.VERSION , OrderType.desc);
		long total = apkService.count(query);
		query.getPage().setTotal(total);
		query.getPage().setDatas(apkService.findAll(query));
		
		return query.getPage();
	}
    /**
     * <p>创建APK</p>
     * @param apk
     * @param file
     * @return
     * @throws IOException
     * ResponseEntity
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public String create(Apk apk , @RequestParam(value = "file" , required = true) MultipartFile file, @RequestParam(value="token", required = true) String token ) throws IOException {
    	AuthUser authUser = authenticateService.loadUserByToken(token);
    	if (authUser == null || authUser.isUser()) {
    		return "apk/failed";
    	}
    	
    	try {
    		apkService.upload(apk , file.getBytes());
    		return "apk/success";
    	}
    	catch(Exception e) {
    		return "apk/failed";
    	}
    }


    @RequestMapping(value = "{id}" , method = RequestMethod.GET)
    public void down(@PathVariable Long id , HttpServletResponse res) throws IOException {
    	Apk apk = apkService.get(id);
    	if (apk == null) {
    		res.setStatus(HttpStatus.NOT_FOUND.value());
    		res.flushBuffer();
    	} else {
    		OutputStream os = res.getOutputStream();
            try {  
                res.reset();  
                res.setHeader("Content-Disposition", "attachment; filename=" + apk.getName());  
                res.setContentType("application/octet-stream; charset=utf-8");  
                os.write(apkService.load(apk));
            } finally {  
            	os.close();
            }  
    	}
    }
    
	/**
	 * <p>delete Domain Object</p>
	 * AjaxResult
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}" , method = RequestMethod.DELETE , 
										produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<AjaxResult> delete(@PathVariable("id") Long id) {
		Apk apk = apkService.get(id);
		if (apk == null) {
			return new ResponseEntity<AjaxResult>(HttpStatus.NOT_FOUND);
		}
		apkService.delete(apk);
		return new ResponseEntity<AjaxResult>(HttpStatus.OK);		
	}
}
