package org.eurake;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.feign.api.User;
import com.feign.service.PersonClient;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class PersonController implements PersonClient{
	
	@Override
	@GetMapping("/toHello")
	@ResponseBody
	public String toHello(String name) {
		return "hello "+name;
	}


	@Override
	@GetMapping("/toHello1")
	@ResponseBody
	public String toHello1(User user) {
		return user.getName()+":"+user.getDesc()+":"+user.getSalary();
	}


	@Override
	@GetMapping("/toHello2")
	@ResponseBody
	public User toHello2(String name, String desc) {
		return User.builder().id(1).name(name+";"+desc).build();
	}


	@Override
	@GetMapping("/toHello3")
	@ResponseBody
	public User toHello3(User user) {
		user.setId(1);
		return user;
	}


	@Override
	@PostMapping("/postUser")
	@ResponseBody
	public User postUser(String name, String desc) {
		log.info(name);
		log.info(desc);
		return User.builder().id(1).name(name+";"+desc).build();
	}


	@Override
	@PostMapping("/postUser1")
	@ResponseBody
	public String postUser1(User user) {
		return "post "+user.getName()+" "+user.getDesc();
	}


	@Override
	@PostMapping("/postUser2")
	@ResponseBody
	public User postUser2(String name, String desc) {
		return User.builder().id(1).name(name+";"+desc).build();
	}


	@Override
	@PostMapping("/postUser3")
	@ResponseBody
	public User postUser3(User user) {
		log.info("addUser "+JSONObject.toJSONString(user));
		user.setId(1);
		return user;
	}


	@Override
	@PostMapping("/postUser4")
	@ResponseBody
	public String postUser4(String name, String desc) {
		return "post "+name+" "+desc;
	}
}
