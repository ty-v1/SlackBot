package main;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.riversun.slacklet.Slacklet;
import org.riversun.slacklet.SlackletRequest;
import org.riversun.slacklet.SlackletResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class Bot extends Slacklet {

	private final Map<String, List<String>> students;
	private static final Type STUDENT_MAP_TYPE = new TypeToken<Map<String, List<String>>>(){}.getType();
	private static final Pattern POSITION_PATTERN = Pattern.compile("@[BMDbmd][1-4]");
	
	public Bot(String jsonFileName) throws IOException{	
		final Gson gson = new Gson();
		final JsonReader reader = new JsonReader(new FileReader("./student.json"));
		students = gson.fromJson(reader, STUDENT_MAP_TYPE);
	}

	public Bot(Map<String, List<String>> _students) {
		students = _students;
	}
	
	@Override
	public void onDirectMessagePosted(SlackletRequest request, SlackletResponse response) {		
		final String content = request.getContent();
		final String position = getPosition(content);
		if(position == null){
			response.reply("リクエストが間違えています");
			return;
		}

		if(students.get(position).size() == 0) {
			response.reply("その身分の人はいません");
			return;	
		}
		
		final String message = content.replaceAll("^@[BMDbmd][1-4]\\s+", "");
		final String sender = request.getSender().getId();
		if(message == null || message.length() == 0){
			response.reply("本文がありません");
			return;
		}else {
			String mentions = "";
			for(String student : students.get(position)) {
				if(!student.equals(sender))
					mentions += "<@" + student.toString() + ">";
			}
			final String post = mentions + "\n" + message + "\n\n" + "from:<@" + sender + ">";
			request.getService().sendMessageTo("random", post);
		}
	}

	private String getPosition(String message) {
		Matcher positionMatcher = POSITION_PATTERN.matcher(message);
		if(!positionMatcher.find()) 
			return null;
		else
			return positionMatcher.group(0).replaceAll("@", "").toUpperCase();
	}
}
