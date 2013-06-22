package com.ainia.ecgApi.controller.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ainia.ecgApi.core.crud.BaseController;
import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.domain.sys.Chief;
import com.ainia.ecgApi.service.sys.ChiefService;

@Controller
@RequestMapping("api/chief")
public class ChiefController extends BaseController<Chief , Long> {

	@Autowired
	private ChiefService chiefService;
	
	@Override
	public BaseService<Chief, Long> getBaseService() {
		return chiefService;
	}

	public void setChiefService(ChiefService chiefService) {
		this.chiefService = chiefService;
	}
	

}
