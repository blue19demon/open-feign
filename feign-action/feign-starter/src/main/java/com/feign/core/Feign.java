package com.feign.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.I0Itec.zkclient.ZkClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.feign.anno.FeignClient;
import com.feign.anno.RequestMapping;
import com.feign.anno.RequestMethod;
import com.feign.anno.RequestParam;
import com.feign.logger.Logger;
import com.feign.logger.LoggerFactory;
import com.feign.util.ReflectUtils;

public class Feign implements InvocationHandler {
	private static Logger log = LoggerFactory.getLogger(Feign.class);
	
	private Class<?> targetClazz;

	private static Feign feign=new Feign();
	
	private static Map<String,EurakeServer> eurakeServerMaps=null;
	
	private static Map<String,EurakeServer> eurakeServerFromZookeeperMaps=null;
	private Feign() {

	}
	public static Feign builder() {
		return feign;
	}
	public static Feign builder(Map<String,EurakeServer> eurakeServerMaps) {
		Feign.eurakeServerMaps=eurakeServerMaps;
		return feign;
	}
	public static Feign builderByZookeeper() {
		String userDir = System.getProperty("user.dir");
		File eurake_file=new File(userDir+"/src/main/resources/zookeeper.properties");
		if(eurake_file.exists()) {
			log.info(">>>>>>file exists>>>>>>>");
			Properties zookeeperPro=new Properties();
			try {
				zookeeperPro.load(new FileInputStream(eurake_file));
			} catch (Exception e) {
				e.printStackTrace();
			}
			ZkClient zk = new ZkClient(zookeeperPro.getProperty("zookeeper.host")+":"+zookeeperPro.getProperty("zookeeper.port"));
			log.info(JSONObject.toJSONString(zk.readData("/zkConfig")));
			Feign.eurakeServerFromZookeeperMaps=zk.readData("/zkConfig");
		}else {
			log.info(">>>>>>file["+userDir+"/src/main/resources/zookeeper.properties"+"] not found!!");
		}
		
		return feign;
	}

	@SuppressWarnings("unchecked")
	public <T> T target(Class<? extends T> clazz) {
		this.targetClazz = clazz;
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] { clazz }, this);
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		StringBuilder urlBuilder = new StringBuilder("http://");
		if (targetClazz.isAnnotationPresent(FeignClient.class)) {
			FeignClient FeignClient = targetClazz.getAnnotation(FeignClient.class);
			log.info("FeignClient>>>>>>"+FeignClient.value());
			if(eurakeServerFromZookeeperMaps!=null) {
				EurakeServer EurakeServer=eurakeServerFromZookeeperMaps.get(FeignClient.value());
				if(EurakeServer!=null) {
					urlBuilder.append(EurakeServer.getServerURL());
				}
			}
			else if(eurakeServerMaps!=null) {
				EurakeServer EurakeServer=eurakeServerMaps.get(FeignClient.value());
				if(EurakeServer!=null) {
					urlBuilder.append(EurakeServer.getServerURL());
				}
			}else {
				loadFromFile(urlBuilder, FeignClient);
			}
		}
		RequestMethod reqMethod = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		Class<?> rt = method.getReturnType();
		if (method.isAnnotationPresent(RequestMapping.class)) {
			RequestMapping RequestMapping = method.getAnnotation(RequestMapping.class);
			urlBuilder.append(RequestMapping.value());
			reqMethod = RequestMapping.method();
			Parameter[] parameters = method.getParameters();
			if (parameters != null && parameters.length > 0 && args.length > 0) {
				if (RequestMethod.GET.equals(reqMethod)) {
					for (int i = 0; i < parameters.length; i++) {
						Parameter p = parameters[i];
						RequestParam pParam = p.getAnnotation(RequestParam.class);
						if (isBaseType(p.getType())) {
							if (i == 0) {
								urlBuilder.append("?" + pParam.name() + "=" + args[i]);
							} else {
								urlBuilder.append("&" + pParam.name() + "=" + args[i]);
							}
						} else {
							log.info("参数队列（键值对列表,对象参数）" + JSONObject.toJSONString(args[i]));
							handleParam(args[i], urlBuilder);
						}
					}
					log.info(urlBuilder.toString());
					HttpGet request = new HttpGet(urlBuilder.toString());
					HttpResponse response = HttpClients.createDefault().execute(request);
					// 检验返回码
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode != HttpStatus.SC_OK) {
						log.info("请求出错: " + statusCode);
						return null;
					} else {
						HttpEntity responseEntity = response.getEntity();
						if (responseEntity != null) {
							String jsonString = EntityUtils.toString(responseEntity);
							if (!isBaseType(rt)) {
								return JSONObject.parseObject(jsonString, rt);
							}
							return jsonString;
						}
					}
				} else {
					log.info(urlBuilder.toString());
					// 3. 创建参数队列（键值对列表）
					List<NameValuePair> paramPairs = new ArrayList<NameValuePair>();
					for (int i = 0; i < parameters.length; i++) {
						Parameter p = parameters[i];
						RequestParam pParam = p.getAnnotation(RequestParam.class);
						if (isBaseType(p.getType())) {
							paramPairs.add(new BasicNameValuePair(pParam.name(), String.valueOf(args[i])));
						} else {
							log.info("参数队列（键值对列表,对象参数）" + JSONObject.toJSONString(args[i]));
							handleParam(args[i], paramPairs);
						}
					}
					log.info("参数队列（键值对列表）" + JSONObject.toJSONString(paramPairs));
					HttpPost post = new HttpPost(urlBuilder.toString());
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramPairs, "UTF-8");
					post.setEntity(entity);

					HttpResponse response = httpClient.execute(post);
					// 检验返回码
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode != HttpStatus.SC_OK) {
						log.info("请求出错: " + statusCode);
						return null;
					} else {
						HttpEntity responseEntity = response.getEntity();
						if (responseEntity != null) {
							String jsonString = EntityUtils.toString(responseEntity);
							if (!isBaseType(rt)) {
								return JSONObject.parseObject(jsonString, rt);
							}
							return jsonString;
						}
					}
				}
			}
		}
		return null;
	}


	private void loadFromFile(StringBuilder urlBuilder, FeignClient FeignClient)
			throws IOException, FileNotFoundException {
		String userDir = System.getProperty("user.dir");
		File eurake_file=new File(userDir+"/src/main/resources/eurake.properties");
		if(eurake_file.exists()) {
			log.info("FeignClient>>>>>>file exists>>>>>>>");
			Properties eurakePro=new Properties();
			eurakePro.load(new FileInputStream(eurake_file));
			for (int i = 0; i < eurakePro.size(); i++) {
				if(eurakePro.getProperty("server.eurake").startsWith(FeignClient.value())) {
					urlBuilder.append(eurakePro.getProperty("server.eurake").split(":")[1]);
					break;
				}
			}
		}else {
			log.info("FeignClient>>>>>>file["+userDir+"/src/main/resources/eurake.properties"+"] not found!!");
		}
	}

	private void handleParam(Object object, StringBuilder urlBuilder) {
		Field[] fs = object.getClass().getDeclaredFields();
		int i = 0;
		for (Field field : fs) {
			if (i == 0) {
				urlBuilder.append(
						"?" + field.getName() + "=" + String.valueOf(ReflectUtils.invokeGet(object, field.getName())));
			} else {
				urlBuilder.append(
						"&" + field.getName() + "=" + String.valueOf(ReflectUtils.invokeGet(object, field.getName())));
			}
			i++;
		}
	}

	private void handleParam(Object object, List<NameValuePair> paramPairs) {
		Field[] fs = object.getClass().getDeclaredFields();
		for (Field field : fs) {
			paramPairs.add(new BasicNameValuePair(field.getName(),
					String.valueOf(ReflectUtils.invokeGet(object, field.getName()))));
		}
	}

	/**
	 * 判断object是否为基本类型
	 * 
	 * @param object
	 * @return
	 */
	public static boolean isBaseType(Class<?> className) {
		if (className.equals(java.lang.Integer.class) || className.equals(java.lang.Byte.class)
				|| className.equals(java.lang.Long.class) || className.equals(java.lang.Double.class)
				|| className.equals(java.lang.Float.class) || className.equals(java.lang.Character.class)
				|| className.equals(java.lang.Short.class) || className.equals(java.lang.String.class)
				|| className.equals(java.lang.Boolean.class)) {
			return true;
		}
		return false;
	}
}
