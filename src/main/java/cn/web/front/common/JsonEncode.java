package cn.web.front.common;

public class JsonEncode {
	public static String encodeJson(String input) {
		return javaScriptEscape(input);
	}
		
	public static String javaScriptEscape(String input) {
		if (input == null) {
			return input;
		}

		StringBuilder filtered = new StringBuilder(input.length());
		char prevChar = '\u0000';
		char c;
		for (int i = 0; i < input.length(); i++) {
			c = input.charAt(i);
			if (c == '"') {
				filtered.append("\\\"");
			}
			else if (c == '\\') {
				filtered.append("\\\\");
			}
			else if (c == '/') {
				filtered.append("\\/");
			}
			else if (c == '\t') {
				filtered.append("\\t");
			}
			else if (c == '\n') {
				if (prevChar != '\r') {
					filtered.append("\\n");
				}
			}
			else if (c == '\r') {
				filtered.append("\\n");
			}
			else if (c == '\f') {
				filtered.append("\\f");
			}
			else if(c < 31 || c == 127){
				filtered.append("");
			}
			else {
				filtered.append(c);
			}
			prevChar = c;

		}
		return filtered.toString();
	}

}
