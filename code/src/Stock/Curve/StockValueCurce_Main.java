package Stock.Curve;

/*
 * Get values (Main)
 * version: October 08, 2019 10:59 AM
 * Last revision: October 15, 2019 06:02 AM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StockValueCurce_Main 
{
	// Articles
	private final String articleFolder = "/Users/phelps/data/git/DataSet/ptt/Stock data/";
	//private final String articleFolder = "/Users/phelps/data/git/DataSet/ptt/test/";
	// Read articles
		File folder = new File(articleFolder);
		File[] listOfFiles = folder.listFiles();
	// Date 
	String startDate = "20180101";
	String endDate = "20190731";
	List<String> datelistArray = new ArrayList<String>();
	List<Integer> dateMesCountArrayTmp;
	List<Integer> dateMesCountArray = new ArrayList<Integer>();
	List<Integer> dateArticleCountArrayTmp;
	List<Integer> dateArticleCountArray = new ArrayList<Integer>();
	
	String fileextension;
	
	
	public StockValueCurce_Main() throws Exception
	{
		Arrays.sort(listOfFiles);
		
		// Date generation
		DateGeneration(startDate, endDate);
			
		// get message count
		//getMessageCount();
		// get article count
		getArticleCount();
	}
	
	private void getMessageCount() throws Exception
	{
		String Line = "";
		int tempCount;
		for (File file : listOfFiles) {
			fileextension = getFileExtension(file);
			
			if (fileextension.equalsIgnoreCase(".json")) {
				System.out.println(file);
				// Read articles
				ReadArticlesMessageCount ra = new ReadArticlesMessageCount(file, datelistArray);
				dateMesCountArrayTmp = ra.ReturndateMesCountArrayTmp();

				for (int i = 0; i < datelistArray.size(); i++) {
					tempCount = dateMesCountArray.get(i);
					//dateMesCountArrayTmp.set(i, tempCount+mesCount);
					dateMesCountArray.set(i, tempCount + dateMesCountArrayTmp.get(i));
				}
			}
		}
		
		
		// Display
		System.out.println("-------------------------------");
		for(int i=0; i<datelistArray.size(); i++)
		{
			System.out.println(datelistArray.get(i)+"	"+dateMesCountArray.get(i));
		}
	}
	
	private void getArticleCount() throws Exception
	{
		String Line = "";
		int tempCount;
		for (File file : listOfFiles) {
			fileextension = getFileExtension(file);
			
			if (fileextension.equalsIgnoreCase(".json")) {
				//System.out.println(file);
				// Read articles
				ReadArticlesCount ra = new ReadArticlesCount(file, datelistArray);
				dateArticleCountArrayTmp = ra.ReturnArticlesCountArrayTmp();

				for (int i = 0; i < datelistArray.size(); i++) {
					tempCount = dateArticleCountArray.get(i);
					dateArticleCountArray.set(i, tempCount + dateArticleCountArrayTmp.get(i));
				}
			}
		}
		
		// Display
		System.out.println("-------------------------------");
		for(int i=0; i<datelistArray.size(); i++)
		{
			System.out.println(datelistArray.get(i)+"	"+dateArticleCountArray.get(i));
		}
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
			
			datelistArray.add(datelist);
			dateMesCountArray.add(0);
			dateArticleCountArray.add(0);
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
	
	public static void main(String args[])
	{
		try {
			StockValueCurce_Main curve = new StockValueCurce_Main();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
