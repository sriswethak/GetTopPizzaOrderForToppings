package com.findbest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GetBestToppingComboFromOrders {
    List<ArrayList<String>> toppingscombo = new ArrayList<ArrayList<String>>();
    Map<ArrayList<String>,Integer> data = new HashMap<ArrayList<String>,Integer>();

	public GetBestToppingComboFromOrders() {
		JSONParser jsonParser = new JSONParser();
		try 
		{
			URL file = new URL("http://files.olo.com/pizzas.json");
		    BufferedReader in = new BufferedReader(new InputStreamReader(file.openStream()));
			//Read JSON file
		    Object obj = jsonParser.parse(in);

         JSONArray orderList = (JSONArray) obj;
           for(Object order: orderList){
        	  JSONObject order1 = (JSONObject)order;
        	  parseOrdersObject(order1);
          }
          data = sortByValue(data);
          int i=1;
          for (Map.Entry<ArrayList<String>,Integer> pair: data.entrySet()) {
        	  if(i>20)
        		  break;
              System.out.format("Topping Combo: %s, Number of orders: %d, Rank: %d%n", pair.getKey(), pair.getValue(),i);
              i++;
          }
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      } catch (ParseException e) {
          e.printStackTrace();
      }
	}

	public static void main(String[] args) {
		GetBestToppingComboFromOrders temp = new GetBestToppingComboFromOrders();
  }
  
  private void parseOrdersObject(JSONObject orders)
  {
      JSONArray toppingsArray = (JSONArray) orders.get("toppings");
     
     
    	  ArrayList<String> temp1 = new ArrayList<String>();
    	  for(int i=0;i<toppingsArray.size();i++){
    		  temp1.add(toppingsArray.get(i).toString());

    	  }
    	  Collections.sort(temp1);
    	  if(!toppingscombo.contains(temp1)){
    		  toppingscombo.add(temp1);
    		  data.put(temp1,1);
    	  }
    	  else
    	  {
    		  int count = data.get(temp1);
    		  data.put(temp1, count+1);
    	  }
     
           
  }
  
  public static Map<ArrayList<String>,Integer> sortByValue(final  Map<ArrayList<String>,Integer> orderCounts) {
      return orderCounts.entrySet()
              .stream()
              .sorted((Map.Entry.<ArrayList<String>,Integer>comparingByValue().reversed()))
              .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
  }


}
