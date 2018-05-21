package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.riversun.slacklet.SlackletService;

public class Main {

	public static void main(String[] args) {

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
}
