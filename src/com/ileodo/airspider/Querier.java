package com.ileodo.airspider;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


/**
 * A querier to query data from http://jc.bjmemc.com.cn/AirQualityDaily/DataSearch.aspx
 * @author LeoDong
 *
 */
public class Querier{
	
	public static final String queryURL ="http://jc.bjmemc.com.cn/AirQualityDaily/DataSearch.aspx";
	
	public BasicCookieStore cookieStore = new BasicCookieStore();
	
    public CloseableHttpClient httpclient = HttpClients.custom()
            .setDefaultCookieStore(cookieStore)
            .build();
    
    public String __EVENTVALIDATION ="";
    
	public String __VIEWSTATE ="";
	
	/**
	 * response body
	 */
	public String resbody;
    
    /**
     * first get, obtain EVENTVALIDATION and VIEWSTATE
     * @throws ParseException
     * @throws IOException
     */
    public void firstGet() throws ParseException, IOException{
    	HttpGet httpget = new HttpGet(queryURL);
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        this.resbody = EntityUtils.toString(entity);
        this.__EVENTVALIDATION = getEV();
        this.__VIEWSTATE = getVS();
    }
    
    /**
     * query the result for given type, name and date
     * @param type
     * @param name
     * @param date
     * @return
     */
    public String query(String type,String name,String date){
    	String result="";
    	try {
    		if(__EVENTVALIDATION ==""||__VIEWSTATE ==""){
    			this.firstGet();
    			if(!type.equals("城市环境评价点")){
    				this.requestResult("ddlType",__VIEWSTATE, __EVENTVALIDATION, type, "东城东四", "2014-03-16", false);
    			}
    		}
			result = this.requestResult("",__VIEWSTATE, __EVENTVALIDATION, type, name, date, true);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
    	return result;
    }
    public void shutdown(){
    	try {
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * conduct post request
     * @param __EVENTTAGET
     * @param __VIEWSTATE
     * @param __EVENTVALIDATION
     * @param type
     * @param name
     * @param date
     * @param search
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String requestResult(String __EVENTTAGET,String __VIEWSTATE,String __EVENTVALIDATION,String type,String name,String date, boolean search) throws ClientProtocolException, IOException{
    	List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("__EVENTTARGET", __EVENTTAGET));
        formparams.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
        formparams.add(new BasicNameValuePair("__LASTFOCUS", ""));
        formparams.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
        formparams.add(new BasicNameValuePair("__EVENTVALIDATION", __EVENTVALIDATION));
        formparams.add(new BasicNameValuePair("ddlType", type));
        formparams.add(new BasicNameValuePair("ddlName", name));
        formparams.add(new BasicNameValuePair("txtTime", date));
        if(search)
        	formparams.add(new BasicNameValuePair("btnSearch", "搜索"));
        
        UrlEncodedFormEntity entityt = new UrlEncodedFormEntity(formparams, Charset.forName("gb2312"));
        HttpPost query = getPost();
        query.setEntity(entityt);

        CloseableHttpResponse response = httpclient.execute(query);
        HttpEntity entity = response.getEntity();
        if(__EVENTTAGET.equals(""))
        	System.out.println("Status of Query: " + response.getStatusLine());
        this.resbody =EntityUtils.toString(entity);
        response.close();
        this.__EVENTVALIDATION = getEV();
        this.__VIEWSTATE = getVS();
        return resbody;
    }
    
    /**
     * get an instance of HttpPost
     * @return
     */
    public HttpPost getPost(){
    	return new HttpPost(queryURL);
    }

	/**
	 * get the EVENTVALIDATION included in the page source
	 * @return
	 */
	public String getEV(){
		Pattern p = Pattern.compile("(?<=<input type=\"hidden\" name=\"__EVENTVALIDATION\" id=\"__EVENTVALIDATION\" value=\").*(?=\" />)");	
		Matcher m = p.matcher(resbody);
	    while (m.find()) {
	    	return m.group();
	    }
	    return null;
	}

	/**
	 * get the VIEWSTATE included in the page source
	 * @return
	 */
	public String getVS(){
		Pattern p = Pattern.compile("(?<=<input type=\"hidden\" name=\"__VIEWSTATE\" id=\"__VIEWSTATE\" value=\").*(?=\" />)");
	    Matcher m = p.matcher(resbody);
	    while (m.find()) {
	    	return m.group();
	    }
	    return null;
	}
	
}
