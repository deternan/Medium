package get_StockIdName;

/*
 * Get TAIWAN shares list (TWSE)
 * Copyright (C) 2019 Chao-Hsuan Ke, phelpske.dev at gmail dot com
 * 
 * Last revision: October 19, 2020 09:58 PM
 * 
 */

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class get_Company_list {
	
	// output folder
	private String sourceFolder = "/Users/phelps/Documents/github/Medium/code/data/";		// output data location
	
	String TWSE_outputTag = "TWSE";
	String TPEX_outputTag = "TPEX";
	String extension = ".txt";
	
	String todayStr;
	
	Vector companyId = new Vector();
	Vector companyName = new Vector();
	
	public get_Company_list(String htmlStr, String todayStr, String tag) throws Exception
	{
		this.todayStr = todayStr;
		
		System.setProperty("file.encoding", "UTF-8");
		String UTF_8_str = new String(htmlStr.getBytes("UTF-8"), "UTF-8");
		
		Document doc = Jsoup.parse(UTF_8_str);
		Elements trs = doc.select("tr");
		
		String temp[];
		for(int i=0; i<trs.size(); i++) {
			Elements tds = trs.get(i).select("td");
			if(tds.size() == 7) {
				temp = tds.get(0).text().split("　");
				if(temp[0].trim().length() == 4) {
					companyId.add(temp[0].trim());
					companyName.add(temp[1].trim());
				}
			}						
		}		
		
		// output		
		if(tag.equals("twse")) {
			output(TWSE_outputTag);
		}else if(tag.equals("tpex")) {
			output(TPEX_outputTag);
		}
		
	}
	
	private void output(String tag)
	{
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sourceFolder + tag +"_" + todayStr + extension), "utf-8"));
			
			for(int i=0; i<companyId.size(); i++) {
				writer.write(companyId.get(i)+"	"+companyName.get(i)+"\n");
			}
			
			writer.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
