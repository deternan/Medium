package httpGet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class GetValues 
{
	// source URL
	private String baseurl = "https://www.twse.com.tw/exchangeReport/BFT41U?response=csv&date=";
	private String endurl = "&selectType=ALL";
	
	// Date
	String yearStr = "2019";
	String monthStr = "06";
	String dayStr = "21";
	String dateStr = yearStr+monthStr+dayStr;
	
	private String completeURL = baseurl + dateStr + endurl;
	
	//
	private JSONObject get_json = null;
	private String jsonStrurl = "https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=20190624&stockNo=2388";
	
	public GetValues() throws Exception
	{
		//ReadValueJson();
		
		JSONObject json = readJsonFromUrl(jsonStrurl);
	    System.out.println(json.toString());
	    System.out.println(json.get("id"));
	}

	private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException 
	{
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONObject json = new JSONObject(jsonText);
	      return json;
	    } finally {
	      is.close();
	    }
	  }
	
	private static String readAll(Reader rd) throws IOException 
	{
		    StringBuilder sb = new StringBuilder();
		    int cp;
		    while ((cp = rd.read()) != -1) {
		      sb.append((char) cp);
		    }
		    return sb.toString();
	}  
	  
	private void ReadValueJson()
	{
		InputStream is;
		String jsonText = "";
		
		try {
			is = new URL(jsonStrurl).openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			//String jsonText = readAll(rd);
			StringBuilder sb = new StringBuilder();
		    int cp;
		    while ((cp = rd.read()) != -1) {
		      sb.append((char) cp);
		    }
		    jsonText = sb.toString();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		try {
			get_json = new JSONObject(jsonText);
			System.out.println(get_json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void httpConnection()
	{
		InputStreamReader reader = null;
		BufferedReader in = null;
		try {
			URL url = new URL("https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=20190628&stockNo=2388");
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(1000);
			reader = new InputStreamReader(connection.getInputStream(), "UTF-8");
			in = new BufferedReader(reader);
			String line = null; 
			StringBuffer content = new StringBuffer();
			while ((line = in.readLine()) != null) {
				content.append(line);
			}
			if (StringUtils.isNotBlank(content)) {
				String jsonStr = content.toString().replaceAll("\\n", "");
				System.out.println(jsonStr);
				//data = JSONObject.fromObject(jsonStr);
			}
		} catch (SocketTimeoutException e) {
			System.out.println("连接超时!!!");
		} catch (JSONException e) {
			System.out.println("网站响应不是json格式，无法转化成JSONObject!!!");
		} catch (Exception e) {
			System.out.println("连接网址不对或读取流出现异常!!!");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("关闭流出现异常!!!");
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.out.println("关闭流出现异常!!!");
				}
			}
		}
	}
	
	public static void main(String args[])
	{
		try {
			GetValues gv = new GetValues();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
