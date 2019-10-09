package Stock.Curve;

/*
 * Articles Parsing
 * version: October 09, 2019 00:09 AM
 * Last revision: October 09, 2019 00:24 AM
 * 
 * Author : Chao-Hsuan Ke
 * E-mail : phelpske.dev at gmail dot com
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class ReadArticles 
{
	String article_id;
	String dateStr;
	
	public ReadArticles(File filenamePath) throws Exception
	{
		String fileextension;
		
		fileextension = getFileExtension(filenamePath);
		if(fileextension.equalsIgnoreCase(".json")) {
			ReadSourceFile(filenamePath.toString());
		}
	}
	
	private void ReadSourceFile(String filenamePath) throws Exception
	{
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
							}
						//System.out.println(article_id+"	"+dateStr);	
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
	
}
