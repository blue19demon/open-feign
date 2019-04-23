package com.feign.service;

import com.feign.anno.FeignClient;
import com.feign.anno.RequestMapping;
import com.feign.anno.RequestMethod;
import com.feign.anno.RequestParam;
import com.feign.api.User;

@FeignClient("feign-server")
public interface PersonClient {

	@RequestMapping(value="/toHello",method=RequestMethod.GET)
	public String toHello(@RequestParam(name="name") String name);
	
	@RequestMapping(value="/toHello1",method=RequestMethod.GET)
	public String toHello1(User user);
	
	@RequestMapping(value="/toHello2",method=RequestMethod.GET)
	public User toHello2(@RequestParam(name="name") String name,@RequestParam(name="desc") String desc);
	
	@RequestMapping(value="/toHello3",method=RequestMethod.GET)
	public User toHello3(User user);
	
	@RequestMapping(value="/postUser",method=RequestMethod.POST)
	public User postUser(@RequestParam(name="name") String name,@RequestParam(name="desc") String desc);

	@RequestMapping(value="/postUser1",method=RequestMethod.POST)
	public String postUser1(User user);

	@RequestMapping(value="/postUser2",method=RequestMethod.POST)
	public User postUser2(@RequestParam(name="name") String name,@RequestParam(name="desc") String desc);

	@RequestMapping(value="/postUser3",method=RequestMethod.POST)
	public User postUser3(User user);
	
	@RequestMapping(value="/postUser4",method=RequestMethod.POST)
	public String postUser4(@RequestParam(name="name") String name,@RequestParam(name="desc") String desc);
}
