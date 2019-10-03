package Stock;

/*
 * Parser Stock value by Date
 * version: October 02, 2019 09:30 PM
 * Last revision: October 03, 2019 06:56 AM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */


public class getStockValue_Main 
{
	private static int sleepTime = 5200;	// 5.2 secs
	private String sourceFolder = "";
	private String StockValue = "";
	
	
	public getStockValue_Main() throws Exception
	{
		
		String startDate = "20180806";		// start date
		
		GetValueandProcessing_StockValue stockvalue = new GetValueandProcessing_StockValue(startDate, sourceFolder, StockValue, sleepTime);
		
	}
	
	public static void main(String args[])
	{
		try {
			getStockValue_Main gsv = new getStockValue_Main();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
