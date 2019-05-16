package com.crawl.api;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageReader;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawl.dto.BadRecord;
import com.crawl.dto.BaseInfo;
import com.crawl.dto.BaseList;
import com.crawl.dto.GoodRecord;
import com.crawl.utils.HttpUtils;
import com.crawl.utils.OCRUtils;
import com.crawl.utils.RedisUtils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IicCrawlAPIImpl implements IicCrawlAPI {

	private static final String PERSIONID_KEY_PREFIX = "PERSIONID_KEY";
	private static final String BASEINFO_KEY_PREFIX = "BASEINFO_KEY";
	private static final String GOODRECORD_KEY_PREFIX = "GOODRECORD_KEY";
	private static final String BADREORD_KEY_PREFIX = "BADREORD_KEY";

	/*
	 * 模拟登录系统
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String findPersonid(String name, String cardno) {
		String redisValue = RedisUtils.read(PERSIONID_KEY_PREFIX + "_" + name + "_" + cardno);
		if (redisValue != null && !"".equals(redisValue)) {
			log.info("findPersonid from redis="+redisValue);
			return redisValue;
		}
		WebClient webClient = null;
		// 登录
		try {
			String sUrl = "http://iir.circ.gov.cn/ipq/insuranceEmp.html";// 网址
			// webclient设置
			webClient = new WebClient(BrowserVersion.CHROME);
			webClient.getOptions().setJavaScriptEnabled(true); // 启动JS
			webClient.getOptions().setUseInsecureSSL(true);// 忽略ssl认证
			webClient.getOptions().setCssEnabled(false);// 禁用Css，可避免自动二次请求CSS进行渲染
			webClient.getOptions().setThrowExceptionOnScriptError(false);// 运行错误时，不抛出异常
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());// 设置Ajax异步
			webClient.getCookieManager().setCookiesEnabled(true);// 开启cookie管理

			HtmlPage targetLink = (HtmlPage) webClient.getPage(sUrl);
			if (targetLink != null) {
				HtmlImage valiCodeImg = (HtmlImage) targetLink.getElementById("captcha");
				ImageReader imageReader = valiCodeImg.getImageReader();
				BufferedImage bufferedImage = imageReader.read(0);
				String code = OCRUtils.saveImageToDiskAndDoOCR(bufferedImage);
				code = code.trim();
				log.info("------------------");
				log.info(code);
				log.info("------------------");

				HtmlTextInput txtUName = (HtmlTextInput) targetLink.getElementById("name");
				txtUName.setValueAttribute(name);
				HtmlTextInput txtCardno = (HtmlTextInput) targetLink.getElementById("cardno");
				txtCardno.setValueAttribute(cardno);
				HtmlTextInput txtYzm = (HtmlTextInput) targetLink.getElementById("yzm");
				txtYzm.setValueAttribute(code);
				HtmlElement button = ((List<HtmlElement>) targetLink.getByXPath("/html/body/div/ul/li[4]/a[1]")).get(0);
				// Thread.sleep(3000);
				button.click();// 写入cookies
				webClient.waitForBackgroundJavaScript(3000);// 等待1秒
				CookieManager CM = webClient.getCookieManager(); // WC = Your WebClient's name
				Set<Cookie> cookies_ret = CM.getCookies();// 返回的Cookie在这里，下次请求的时候可能可以用上啦。
				for (Cookie cookie : cookies_ret) {
					if ("personid".equals(cookie.getName())) { // PE_FIN18188245
						String personid = cookie.getValue();
						log.info("personid from net="+personid);
						RedisUtils.write(PERSIONID_KEY_PREFIX + "_" + name + "_" + cardno, personid);
						return personid;
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 webClient.close();
		}
		return null;
	}

	/**
	 * 基本信息
	 */
	@Override
	public BaseInfo queryBasicInfo(String personid) {
		String redisValue = RedisUtils.read(BASEINFO_KEY_PREFIX + "_" + personid);
		if (redisValue != null && !"".equals(redisValue)) {
			log.info("queryBasicInfo from redis="+redisValue);
			BaseList BaseList=JSONObject.parseObject(redisValue, BaseList.class);
			if(BaseList!=null) {
				BaseInfo[] list=BaseList.getList();
				if(list!=null&&list.length>0) {
					return list[0];
				}
			}
			return null;
			
		}
		String res = HttpUtils.excutePost(personid, "http://iir.circ.gov.cn/ipq/insurance.do?query");
		log.info("queryBasicInfo from net="+res);
		RedisUtils.write(BASEINFO_KEY_PREFIX + "_" + personid, res);
		BaseList BaseList=JSONObject.parseObject(res, BaseList.class);
		if(BaseList!=null) {
			BaseInfo[] list=BaseList.getList();
			if(list!=null&&list.length>0) {
				return list[0];
			}
		}
		return null;
	}

	/**
	 * 表彰奖励记录
	 * 
	 * @param personid
	 */
	@Override
	public GoodRecord queryGoodRecord(String personid) {
		String redisValue = RedisUtils.read(GOODRECORD_KEY_PREFIX + "_" + personid);
		if (redisValue != null && !"".equals(redisValue)) {
			log.info("queryGoodRecord from redis="+redisValue);
			JSONArray array=JSONArray.parseArray(redisValue);
			if(array!=null&&array.size()>0) {
				return JSONObject.parseObject(JSONObject.toJSONString(array.get(0)), GoodRecord.class);
			}
			return null;
		}
		String res = HttpUtils.excutePost(personid, "http://iir.circ.gov.cn/ipq/insurance.do?findGood");
		log.info("queryGoodRecord from net="+res);
		RedisUtils.write(GOODRECORD_KEY_PREFIX + "_" + personid, res);
		JSONArray array=JSONArray.parseArray(res);
		if(array!=null&&array.size()>0) {
			return JSONObject.parseObject(JSONObject.toJSONString(array.get(0)), GoodRecord.class);
		}
		return null;
	}

	/**
	 * 违规违章记录
	 */
	@Override
	public BadRecord queryFindBad(String personid) {
		String redisValue = RedisUtils.read(BADREORD_KEY_PREFIX + "_" + personid);
		if (redisValue != null && !"".equals(redisValue)) {
			log.info("queryFindBad from redis="+redisValue);
			JSONArray array=JSONArray.parseArray(redisValue);
			if(array!=null&&array.size()>0) {
				return JSONObject.parseObject(JSONObject.toJSONString(array.get(0)), BadRecord.class);
			}
			return null;
		}
		String res = HttpUtils.excutePost(personid, "http://iir.circ.gov.cn/ipq/insurance.do?findBad");
		log.info("queryFindBad from net="+res);
		RedisUtils.write(BADREORD_KEY_PREFIX + "_" + personid, res);
		JSONArray array=JSONArray.parseArray(res);
		if(array!=null&&array.size()>0) {
			return JSONObject.parseObject(JSONObject.toJSONString(array.get(0)), BadRecord.class);
		}
		return null;
	}
}