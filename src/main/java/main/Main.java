package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.riversun.slacklet.SlackletService;

import util.ParseUtil;

public class Main {

	public static void main(String[] args) {
		
		if((new File("./data.properties")).exists()) {
			(new Main()).runWithProperties();
		}else {
			(new Main()).runWithEnviromentalVariable();
		}
	}
	
	public static final String[] POSITION = new String[]{"D3", "D2", "D1", "M2", "M1", "B4"};
	
	public void runWithProperties() {
		try {
			Properties properties = new Properties();

			BufferedReader reader = new BufferedReader(new FileReader("./data.properties"));
			properties.load(reader);
			reader.close();

			final String token = properties.getProperty("api_key");
			final String jsonFileName = properties.getProperty("json");

			SlackletService slackService = new SlackletService(token);
			slackService.addSlacklet(new Bot(jsonFileName));
			slackService.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public void runWithEnviromentalVariable() {
		try {
			final String token = System.getenv().get("TOKEN");
			
			final Map<String, List<String>> students = new HashMap<>();
			for(String position : POSITION) {
				students.put(position, ParseUtil.parseStringToList(System.getenv().get(position)));
			}
			SlackletService slackService = new SlackletService(token);
			slackService.addSlacklet(new Bot(students));
			slackService.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
