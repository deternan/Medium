package Stock;

/*
 * Parser values by stock Id
 * version: November 30, 2019 09:59 PM
 * Last revision: December 07, 2019 00:42 AM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

/*
 * "fields":[
"日期",
"成交股數",
"成交金額",
"開盤價",
"最高價",
"最低價",
"收盤價",
"漲跌價差",
"成交筆數"
]
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class GetValueandProcessingByStockId 
{
	int sleepTime = 5200;	// delay timer (5.2 secs)
	// Values source
	private String TWSEvalueUrl = "https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=";
	private String TPEXvalueUrl = "https://www.tpex.org.tw/web/stock/aftertrading/daily_trading_info/st43_result.php?d=";
	private String ADDate_pattern = "yyyyMMdd";
	DateFormat df = new SimpleDateFormat(ADDate_pattern, Locale.getDefault());	
	// twse tpex list
	private boolean twselist_check = false;
	private boolean tpexlist_check = false; 
	private String twselistFile = "/Users/phelps/Documents/github/PTT_Stock/source/TWSE.txt";
	private String tpexlistFile = "/Users/phelps/Documents/github/PTT_Stock/source/TPEX.txt";
	ArrayList twseidlist = new ArrayList();
 	ArrayList tpexidlist = new ArrayList();
 	String stockTag = "";
 	
	String today_str;
	String date_str;
	
	private String stockId;
	private String startYear;
	private String startMonth;
	private String startDay;
	// Get JsonResponse
	private String sourceLine = "";
	
	// Date
	private int monthGap = 0;
	// storage
	ArrayList dateoutput = new ArrayList();
	ArrayList valueoutput = new ArrayList();
	
	public GetValueandProcessingByStockId(String startDate, String ID) throws Exception 
	{
		this.stockId = ID;

		// 	1. 判斷輸入 id 是"上市"(TWSE)或是"上櫃"(TPEX)
		// Read TWSE list
		Read_TWSE();
		// Read TPEX list
		Read_TPEX();
		// Judge TWSE or TPEX
		stockTag = checkstockId_Tag(stockId);
		
		// 2. 計算起始日期(該月份)至今要擷取的日期
		startYear = startDate.substring(0, 4);
		startMonth = startDate.substring(4, 6);
		startDay = "01";
		// Date
		Date today = Calendar.getInstance().getTime();
		SimpleDateFormat sdf  = new SimpleDateFormat(ADDate_pattern);
		String todayStr = sdf.format(today);
		String specificDateStr = startYear + startMonth + startDay;
		monthGap = getMonthGap(todayStr, specificDateStr);							// 月份差距
		List<String> monthList = MonthIncrement(startYear + startMonth, monthGap);	// 月份列表
		
		// 3. 擷取該股收盤數值
		int TWYear;
		String TWMonthStr;
		for(int i=0; i<monthList.size(); i++)
		{
			sourceLine = "";
			
			// Processing (get data from URL)
			if ("twse".equalsIgnoreCase(stockTag)) {
				GetValues(monthList.get(i) + startDay, stockTag);
				if (isJSONValid(sourceLine)) {
					Processing_TWSE(sourceLine);
				}
			} else if ("tpex".equalsIgnoreCase(stockTag)) {
				TWYear = Integer.parseInt(startYear);
				TWYear = TWYear - 1911;
				TWMonthStr = monthList.get(i).toString().substring(4, 6);
				GetValues(String.valueOf(TWYear) + "/" + TWMonthStr, stockTag);
				if (isJSONValid(sourceLine)) {
					Processing_TPEX(sourceLine);
				}
			}		
			// Thread sleep
			Thread.sleep((int) sleepTime);
		}
		
		// Display
		for(int i=0; i<dateoutput.size(); i++)
		{
			System.out.println(dateoutput.get(i)+"	"+valueoutput.get(i));
		}
	}
	
	private void Read_TWSE() throws Exception 
	{
		String Line = "";
		String tmp[];
		
		File file = new File(twselistFile);
		if (file.exists()) {
			FileReader fr = new FileReader(twselistFile);
			BufferedReader bfr = new BufferedReader(fr);

			while ((Line = bfr.readLine()) != null) {
				tmp = Line.split("\t");
				twseidlist.add(tmp[0]);
			}

			fr.close();
			bfr.close();

			twselist_check = true;

		} else {
			System.out.println("No TWSE id list");
			twselist_check = false;
		}
	}
	
	private void Read_TPEX() throws Exception
	{
		String Line = "";
		String tmp[];

		File file = new File(tpexlistFile);
		if (file.exists()) {
			FileReader fr = new FileReader(tpexlistFile);
			BufferedReader bfr = new BufferedReader(fr);

			while ((Line = bfr.readLine()) != null) {
				tmp = Line.split("\t");
				tpexidlist.add(tmp[0]);
			}

			fr.close();
			bfr.close();

			tpexlist_check = true;

		} else {
			System.out.println("No TPEX id list");
			tpexlist_check = false;
		}
	}
	
	private String checkstockId_Tag(String id)
	{
		String Tag = "";
		boolean checkTag = false;
		
		for(int i=0; i<twseidlist.size(); i++) {
			if(id.equalsIgnoreCase(twseidlist.get(i).toString())) {
				Tag = "twse";
				checkTag = true;
				break;
			}
		}
		
		if(checkTag == false) {
			for(int i=0; i<tpexidlist.size(); i++) {
				if(id.equalsIgnoreCase(tpexidlist.get(i).toString())) {
					Tag = "tpex";
					break;
				}
			}
		}
		
		return Tag;
	}
	
	private List<String> MonthIncrement(String startDate, int addMonths) throws Exception
	{
		List<String> monthList = new ArrayList();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMM");
        
        for(int i=0; i<=addMonths; i++){
        	Date dt=sdf.parse(startDate);
        	Calendar rightNow = Calendar.getInstance();
        	rightNow.setTime(dt);
    		if(i != 0){
    			rightNow.add(Calendar.MONTH,1);
    		}
        	Date dt1=rightNow.getTime();
        	String reStr = sdf.format(dt1);
        	
        	startDate = reStr;
        	monthList.add(reStr);
        }
        
        return monthList;
	}

	// Date Processing
	private int getMonthGap(String d1, String d2) throws Exception 
	{
		SimpleDateFormat sdf = new SimpleDateFormat(ADDate_pattern);
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(d1));
		int year1 = c.get(Calendar.YEAR);
		int month1 = c.get(Calendar.MONTH);

		c.setTime(sdf.parse(d2));
		int year2 = c.get(Calendar.YEAR);
		int month2 = c.get(Calendar.MONTH);

		int result;
		if (year1 == year2) {
			result = month1 - month2;
		} else {
			result = 12 * (year1 - year2) + month1 - month2;
		}

		return result;
	}
	
	private void GetValues(String DateStr, String tag)
	{
		sourceLine = "";
		String URL = "";
		if("twse".equalsIgnoreCase(tag)) {
			// TWSE
			URL = TWSEvalueUrl + DateStr + "&stockNo=" + stockId;
		}else if("tpex".equalsIgnoreCase(tag)) {
			// TPEX
			URL = TPEXvalueUrl + DateStr + "&stkno=" + stockId;
		}
		
		try {
			HttpsGet https = new HttpsGet();
			
			if(https.responseJSON(URL).isEmpty() == false) {
				sourceLine = https.responseJSON(URL);
			}
								
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean isJSONValid(String jsonInString) 
	{
		JsonParser parser = new JsonParser();
		JsonElement jsonele = parser.parse(jsonInString);
		boolean check;
		check = jsonele.isJsonObject();
		
		return check;
	}
	
	private void Processing_TWSE(String jsonStr) throws Exception
	{
		String dateStr;
		JSONObject obj = new JSONObject(jsonStr);
		if(obj.has("data")) {
			JSONArray jsonarray = new JSONArray(obj.get("data").toString());
			for(int i=0; i<jsonarray.length(); i++)
			{
				JSONArray arrayData = new JSONArray(jsonarray.get(i).toString());
				dateStr = arrayData.get(0).toString().replace("/", "");
				
				dateoutput.add(dateStr);
				valueoutput.add(arrayData.get(6).toString());
			}
		}
	}
	
	private void Processing_TPEX(String jsonStr) throws Exception
	{
		String dateStr;
		JSONObject obj = new JSONObject(jsonStr);
		if(obj.has("aaData")) {
			JSONArray jsonarray = new JSONArray(obj.get("aaData").toString());
			for(int i=0; i<jsonarray.length(); i++)
			{
				JSONArray arrayData = new JSONArray(jsonarray.get(i).toString());
				//Storage(arrayData.get(0).toString(), arrayData.get(6).toString());
				dateStr = arrayData.get(0).toString().replace("/", "");
				//System.out.println(arrayData.get(0)+"	"+arrayData.get(6)+"	"+dateStr);
				dateoutput.add(dateStr);
				valueoutput.add(arrayData.get(6).toString());
			}
		}
	}
	
	public static void main(String args[])
	{
		String startDate = "20191106";
		String ID = "2330";
		
		try {
			GetValueandProcessingByStockId value = new GetValueandProcessingByStockId(startDate, ID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
