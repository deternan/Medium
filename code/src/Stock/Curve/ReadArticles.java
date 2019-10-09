package Stock.Curve;

/*
 * Articles Parsing
 * version: October 09, 2019 00:09 AM
 * Last revision: October 09, 2019 06:54 AM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class ReadArticles 
{
	// Date 
	String startDate = "20180101";
	String endDate = "20190731";
	Vector datelistVec = new Vector();
	
	String article_id;
	String dateStr;
	
	Date articleDate;
	Date standardDate;
	
	// Date
	String year;
	String month;
	String day;
	String monthStr;
	String dayStr;
	
	public ReadArticles(File filenamePath) throws Exception
	{
		String fileextension;
		
		// Date generation
		DateGeneration(startDate, endDate);
		
		
//		fileextension = getFileExtension(filenamePath);
//		if(fileextension.equalsIgnoreCase(".json")) {
//			ReadSourceFile(filenamePath.toString());
//		}
	}
	
	private  void DateGeneration(String startDate, String endDate) throws Exception
	{
		Calendar cal = Calendar.getInstance();
		String start = startDate;
		String end = endDate;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date dBegin = sdf.parse(start);
		Date dEnd = sdf.parse(end);
		List<Date> lDate = findDates(dBegin, dEnd);
		String datelist;
		for (Date date : lDate) 
		{
			datelist = "";
			datelist = sdf.format(date).toString();
			
			datelistVec.add(datelist);
			//System.out.println(datelist);
		}
	}
	
	private static List<Date> findDates(Date dBegin, Date dEnd) 
	{
		List lDate = new ArrayList();
		lDate.add(dBegin);
		Calendar calBegin = Calendar.getInstance();

		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();

		calEnd.setTime(dEnd);

		while (dEnd.after(calBegin.getTime())) {

			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(calBegin.getTime());
		}
		
		return lDate;
	}
	
	private void ReadSourceFile(String filenamePath) throws Exception
	{
		year = "";
		month = "";
		day = "";
		String Line = "";
		FileReader fr = new FileReader(filenamePath);
		BufferedReader bfr = new BufferedReader(fr);

		String strTmp = "";
		while ((Line = bfr.readLine()) != null) 
		{
			strTmp += Line;
		}
		fr.close();
		bfr.close();
		
		String idTmp;
		String newDate = "";
		if (isJSONValid(strTmp)) {
			JSONObject obj = new JSONObject(strTmp);
			if (obj.has("articles")) {
				JSONArray jsonarray = new JSONArray(obj.get("articles").toString());
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject articleobj = new JSONObject(jsonarray.get(i).toString());
					if (articleobj.has("article_id")) 
					{
						idTmp = articleobj.getString("article_id");
							article_id = idTmp;
							// date
							if (articleobj.has("date")) {
								dateStr = articleobj.getString("date");
								Date_Split(dateStr);
								month = monthStr;
								day = dayStr;
								// re-combination
								if((year!=null) && (month!=null) && (day!=null)) {
									newDate = Time_to_String(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
								}
								
								//articleDateTranslation(Time_to_String);
							}
						System.out.println(article_id+"	"+dateStr+"	"+newDate);	
					}
				}
			}
		}
	}
	
	private boolean isJSONValid(String jsonInString) {

		JsonParser parser = new JsonParser();
		JsonElement jsonele = parser.parse(jsonInString);
		boolean check;
		check = jsonele.isJsonObject();
		
		return check;
	}

	private static String getFileExtension(File file) {
		
		String extension = "";
 
        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                extension = name.substring(name.lastIndexOf("."));
            }
        } catch (Exception e) {
            extension = "";
        }
 
        return extension;
 
    }
	
	private void articleDateTranslation(String input) throws Exception
	{
		articleDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(input);
	}
	
	private void Date_Split(String dateStr)
	{
		//"date":"Tue Aug 30 13:38:20 2016",
		String temp[];
		temp = dateStr.split(" ");

		if(temp.length == 6) {
			month = temp[1];
			monthStr = MonthTranslation(month);
			day = temp[3];
			if(day.length() == 1) {
				dayStr = "0"+String.valueOf(day);
			}else {
				dayStr = String.valueOf(day);
			}
			year = temp[5];
		}else if(temp.length == 5) {
			month = temp[1];
			monthStr = MonthTranslation(month);
			day = temp[2];
			if(day.length() == 1) {
				dayStr = "0"+String.valueOf(day);
			}else {
				dayStr = String.valueOf(day);
			}
			year = temp[4];
		}
	}
	
	private String MonthTranslation(String inputStr) {
		
		String monthInt = ""; 
		switch(inputStr)
		{
		case "Jan":
			monthInt = "01";
			break;
		case "Feb":
			monthInt = "02";
			break;
		case "Mar":
			monthInt = "03";
			break;
		case "Apr":
			monthInt = "04";
			break;
		case "May":
			monthInt = "05";
			break;
		case "Jun":
			monthInt = "06";
		break;
		case "Jul":
			monthInt = "07";
		break;
		case "Aug":
			monthInt = "08";
		break;
		case "Sep":
			monthInt = "09";
		break;
		case "Oct":
			monthInt = "10";
		break;
		case "Nov":
			monthInt = "11";
		break;
		case "Dec":
			monthInt = "12";
		break;
		}
		
		return monthInt;
	}
	
	private String Time_to_String(int year, int month, int day) 
	{
		Calendar myCal = Calendar.getInstance();
		myCal.set(Calendar.YEAR, year);
		myCal.set(Calendar.MONTH, month - 1);
		myCal.set(Calendar.DAY_OF_MONTH, day);
		//myCal.set(Calendar.HOUR_OF_DAY, 0);
		//myCal.set(Calendar.MINUTE, 0);
		//myCal.set(Calendar.SECOND, 0);
		Date theDate = myCal.getTime();
		DateTools.dateToString(theDate, Resolution.MONTH);

		return DateTools.dateToString(theDate, Resolution.DAY);
	}
	
}
