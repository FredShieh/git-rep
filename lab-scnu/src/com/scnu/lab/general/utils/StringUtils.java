package com.scnu.lab.general.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * 字符串工具
 * 
 * @author: dk
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {

	private static Logger log = Logger.getLogger(StringUtils.class.getName());

	//private static Gson gson;

	public static enum ENCODING {
		GBK, UTF_8, ISO_8859_1;
	}

	public static Byte parseByte(Object str) {
		return str == null ? 0 : Byte.valueOf((isNumeric(str.toString())) ? Byte.parseByte(str.toString()) : 0);
	}

	public static Integer parseInt(Object str) {
		return str == null ? 0 : Integer.valueOf((isNumeric(str.toString())) ? Integer.parseInt(str.toString()) : 0);
	}

	public static Long parseLong(Object str) {
		return str == null ? 0 : Long.valueOf((isNumeric(str.toString())) ? Long.parseLong(str.toString()) : 0);
	}

	public static Float parseFloat(Object str) {
		return str == null ? 0 : Float.valueOf((isNumeric(str.toString())) ? Float.parseFloat(str.toString()) : 0);
	}

	public static Double parseDouble(Object str) {
		return str == null ? 0 : Double.valueOf((isNumeric(str.toString())) ? Double.parseDouble(str.toString()) : 0);
	}

	public static Boolean parseBoolean(Object str) {
		if (str == null) {
			return false;
		}
		String s = str.toString().toLowerCase();
		if (("y".equalsIgnoreCase(s)) || ("yes".equalsIgnoreCase(s)) || ("true".equalsIgnoreCase(s)) || ("t".equalsIgnoreCase(s)) || ("1".equalsIgnoreCase(s))) {
			return true;
		}
		return false;
	}

	/**
	 * null 或者 'null' 转换成 ""
	 */
	public static final String null2Blank(Object str) {
		return ((null == str || "null".equals(str)) ? "" : str.toString());
	}

	/**
	 * split时候处理空字符的情况
	 * 
	 * @param str
	 * @param splitor
	 * @return
	 */
	public static String[] split(String str, String splitor) {
		return StringUtils.isBlank(str) ? new String[] {} : str.split(splitor);
	}

	/**
	 * 判断是否为数字
	 */
	public static boolean isNumeric(String str) {
		Matcher isNum = Pattern.compile("(-|\\+)?[0-9]+(.[0-9]+)?").matcher(str);
		return isNum.matches();
	}

	final static DecimalFormat decimalFormat = new DecimalFormat("0");

	/**
	 * 数字格式化<br>
	 * 
	 * @param obj
	 * @param scale
	 * @return
	 * @throws Exception
	 */
	public static String fmtNumeric(Object obj, int scale) throws Exception {
		String objStr = defaultIfEmpty(null2Blank(obj), "0");
		objStr = "NaN".equals(objStr) ? "0" : objStr;
		double doubleValue = new BigDecimal(Double.valueOf(objStr).doubleValue()).setScale(scale, BigDecimal.ROUND_HALF_EVEN).doubleValue();

		StringBuffer endWith = new StringBuffer();
		for (int i = 0; i < scale; i++) {
			endWith.append("0");
		}
		String pattern = "0";
		if (endWith.length() > 0) {
			pattern = pattern.concat(".").concat(endWith.toString());
		}
		decimalFormat.applyPattern(pattern);

		return decimalFormat.format(doubleValue);
	}

	/**
	 * 字符编码转换
	 */
	public static String fromEncoding2Encoding(String content, ENCODING fromEncoding, ENCODING toEncoding) {
		String from = fromEncoding.toString().replace("_", "-");
		String to = toEncoding.toString().replace("_", "-");
		if (null != content)
			try {
				byte[] tmpbyte = content.getBytes(from);
				content = new String(tmpbyte, to);
			} catch (Exception e) {
				log.error("Error: from: " + from + " To: " + to + " :" + e.getMessage());
			}

		return content;
	}

	/**
	 * 转换成html编码
	 */
	public static String htmlEncode(String txt) {
		if (null != txt) {
			txt = txt.replace("&", "&amp;").replace("&amp;amp;", "&amp;").replace("&amp;quot;", "&quot;").replace("\"", "&quot;").replace("&amp;lt;", "&lt;").replace("<", "&lt;").replace("&amp;gt;", "&gt;").replace(">", "&gt;").replace("&amp;nbsp;", "&nbsp;");
		}
		return txt;
	}

	/**
	 * 去除html编码
	 */
	public static String unHtmlEncode(String txt) {
		if (null != txt) {
			txt = txt.replace("&amp;", "&").replace("&quot;", "\"").replace("&lt;", "<").replace("&gt;", ">").replace("&nbsp;", " ");
		}
		return txt;
	}

	/**
	 * 去出html&js
	 */
	public static String replaceHtmlJs(String txt) {
		if (null != txt) {
			txt = txt.replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", "").replaceAll("</[a-zA-Z]+[1-9]?>", "").replaceAll("eval\\((.*)\\)", "");
		}
		return txt;
	}

	/**
	 * 针对数据库查询参数中的特殊字符(" ' % )进行转义
	 * 
	 * @param txt
	 * @return
	 */
	public static String replaceSpecialChars(String txt) {
		if (null != txt) {
			txt = txt.replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\"").replaceAll("%", "\\\\%");
		}
		return txt;
	}

	/**
	 * 将回车换行符转换成html标签
	 * 
	 * @param txt
	 * @return
	 */
	public static String changeEscapeCharacter2Tag(String txt) {
		if (null != txt) {
			txt = txt.replaceAll("\r\n", "<br>").replaceAll("\r", "<br>").replaceAll("\n", "<br>").replaceAll("\b", "&nbsp;");
		}
		return txt;
	}

	/**
	 * 半角转全角
	 * 
	 * @param input String.
	 * @return 全角字符串.
	 */
	public static String toSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000'; // 采用十六进制,相当于十进制的12288

			} else if (c[i] < '\177') { // 采用八进制,相当于十进制的127
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}

	/**
	 * 全角转半角
	 * 
	 * @param input String.
	 * @return 半角字符串
	 */
	public static String toDBC(String input) {

		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}

		return new String(c);
	}

	/**
	 * 获取一个gson工具示例
	 * 
	 * @return
	 */
//	public static Gson getGson() {
//		return gson == null ? gson = new Gson() : gson;
//	}

	public static String base64Encode(String source) throws UnsupportedEncodingException {
		return new String(Base64.encodeBase64(source.getBytes(ENCODING.UTF_8.toString())), ENCODING.UTF_8.toString());
	}

	public static String base64Decode(String source) throws UnsupportedEncodingException {
		return new String(Base64.decodeBase64(source.getBytes(ENCODING.UTF_8.toString())), ENCODING.UTF_8.toString());
	}
	
	/**
	 * 去掉特殊字符, 针对url请求参数
	 * 
	 * @param value
	 * @return
	 */
	public static String filterDangerString(String value) {
		if (value == null) {
			return null;
		}
		value = value.replaceAll("\\|", "");
		value = value.replaceAll("//", "");
		// value = value.replaceAll("&", "&amp;");
		value = value.replaceAll(";", "");
		value = value.replaceAll("@", "");
		value = value.replaceAll("'", "");
		value = value.replaceAll("\"", "");
		value = value.replaceAll("\\'", "");
		value = value.replaceAll("\\\"", "");
		value = value.replaceAll("<", "&lt;");
		value = value.replaceAll(">", "&gt;");
		value = value.replaceAll("\\(", "");
		value = value.replaceAll("\\)", "");
		value = value.replaceAll("\\+", "");
		value = value.replaceAll("\r", "");
		value = value.replaceAll("\n", "");
		value = value.replaceAll("script", "");
		value = value.replaceAll("%27", "");
		value = value.replaceAll("%22", "");
		value = value.replaceAll("%3E", "");
		value = value.replaceAll("%3C", "");
		value = value.replaceAll("%3D", "");
		value = value.replaceAll("%2F", "");
		value = value.replaceAll("%20", "");
		return value;
	}
}