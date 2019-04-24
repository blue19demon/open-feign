package com.server;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.feign.core.EurakeServer;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class SycMicroServiceConf {
	@Value("${server.port}")
	private  String port;
	@Value("${srv.name}")
	private  String srvName;
	@Bean
	public Map<String,EurakeServer> sycMicroService() {
		Map<String,EurakeServer> eurakeServerFromZookeeperMaps=null;
		String userDir = System.getProperty("user.dir");
		File eurake_file=new File(userDir+"/src/main/resources/zookeeper.properties");
		if(eurake_file.exists()) {
			String localip=null;
			log.info(">>>>>>file exists>>>>>>>");
			Properties zookeeperPro=new Properties();
			try {
				InetAddress addr = InetAddress.getLocalHost();  
				localip=addr.getHostAddress().toString(); //获取本机ip  
				zookeeperPro.load(new FileInputStream(eurake_file));
			} catch (Exception e) {
				e.printStackTrace();
			}
			ZkClient zk = new ZkClient(zookeeperPro.getProperty("zookeeper.host")+":"+zookeeperPro.getProperty("zookeeper.port"));
			log.info(String.valueOf(zk.exists("/zkConfig")));
			if(!zk.exists("/zkConfig")){
				zk.createPersistent("/zkConfig",true);
				eurakeServerFromZookeeperMaps=new HashMap<>();
				eurakeServerFromZookeeperMaps.put(srvName, new EurakeServer(srvName, localip+":"+port));
				zk.writeData("/zkConfig", eurakeServerFromZookeeperMaps);
			}else {
				eurakeServerFromZookeeperMaps=zk.readData("/zkConfig");
				eurakeServerFromZookeeperMaps.put(srvName, new EurakeServer(srvName, localip+":"+port));
				zk.writeData("/zkConfig", eurakeServerFromZookeeperMaps);
			}
			zk.close();
		}else {
			log.info(">>>>>>file["+userDir+"/src/main/resources/zookeeper.properties"+"] not found!!");
		}
		return eurakeServerFromZookeeperMaps;
	}
}
