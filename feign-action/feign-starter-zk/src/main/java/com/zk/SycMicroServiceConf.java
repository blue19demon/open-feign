package com.zk;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.feign.core.EurakeServer;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SycMicroServiceConf {
	@Value("${server.port}")
	private  String port;
	@Value("${srv.name}")
	private  String srvName;
	@Value("${mrc.zookeeper.host}")
	private  String zkHost;
	@Value("${mrc.zookeeper.port}")
	private  String zkPort;
	@Bean
	public Object sycMicroService() {
		Map<String,EurakeServer> eurakeServerFromZookeeperMaps=null;
		String localip=null;
		try {
			InetAddress addr = InetAddress.getLocalHost();  
			localip=addr.getHostAddress().toString(); //获取本机ip 
		} catch (Exception e) {
			e.printStackTrace();
		}
		ZkClient zk = new ZkClient(zkHost+":"+zkPort);
		log.info(String.valueOf(zk.exists("/zkConfig")));
		if(!zk.exists("/zkConfig")){
			zk.createPersistent("/zkConfig",true);
			eurakeServerFromZookeeperMaps=new HashMap<String, EurakeServer>();
			eurakeServerFromZookeeperMaps.put(srvName, new EurakeServer(srvName, localip+":"+port));
			zk.writeData("/zkConfig", eurakeServerFromZookeeperMaps);
		}else {
			eurakeServerFromZookeeperMaps=zk.readData("/zkConfig");
			eurakeServerFromZookeeperMaps.put(srvName, new EurakeServer(srvName, localip+":"+port));
			zk.writeData("/zkConfig", eurakeServerFromZookeeperMaps);
		}
		zk.close();
		return null;
	}
}
