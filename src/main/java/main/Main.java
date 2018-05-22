package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.riversun.slacklet.SlackletService;

import util.ParseUtil;

public class Main {

	private ServerSocket server;
	
	public static void main(String[] args) {
				
		int port = Integer.parseInt(args[0]);
		if((new File("./data.properties")).exists()) {
			(new Main(port)).runWithProperties();
		}else {
			(new Main(port)).runWithEnviromentalVariable();
		}
	}
	
	public static final String[] POSITION = new String[]{"D3", "D2", "D1", "M2", "M1", "B4"};
	
	public Main(int port) {
		// herokuでのR10防止
		// 意味はないが引数のポートにバインドする
		try {
			server = new ServerSocket(port);
			if(!server.isBound())
				System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
