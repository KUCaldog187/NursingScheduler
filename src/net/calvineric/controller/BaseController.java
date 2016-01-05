package net.calvineric.controller;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class BaseController implements ApplicationContextAware{

	protected ApplicationContext applicationContext = null;
	
	@RequestMapping(method = RequestMethod.GET)
	public String welcome(ModelMap model){
		model.addAttribute("message", "Welcome!");
		return "welcome";
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
