package Stock.Curve;

/*
 * Get values (Main)
 * version: October 08, 2019 10:59 AM
 * Last revision: October 09, 2019 00:24 AM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.File;
import java.util.Arrays;

public class StockValueCurce_Main 
{
	// Articles
	private final String articleFolder = "/Users/phelps/data/git/DataSet/ptt/Stock data/";
	
	
	public StockValueCurce_Main() throws Exception
	{
		File folder = new File(articleFolder);
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles);

		String articlenameTmp;
		String idTmp = "";
		String strTmp = "";
		String authorTmp = "";
			int authorTagIndex;
		
		String Line = "";
		for (File file : listOfFiles) {
			//System.out.println(file);
			// Read articles
			ReadArticles ra = new ReadArticles(file);
		}
		
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
