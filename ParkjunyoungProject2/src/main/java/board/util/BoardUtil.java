package board.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Stream;


public class BoardUtil {
	
	private static final String PATH_BUNDLE="/resources/path";
	private static final String QUERY_BUNDLE="/resources/query";
	private static final String GOOGLE_BUNDLE="/resources/googleuser";
	
	private static final Map<String,String> MEMBER_PARAM_MAP;
	private static final String[] MEMBER_PARAM_KEYS={"username","password","gender","inters","education"};
	private static final String[] BBS_PARAM_KEYS={"bbs-content-write","bbs-encoded-file-write"};
	private static final String[] MEMBER_PARAM_KEYS_KR={"아이디","비밀번호","성별","관심사항","학력"};
	private static final int TEXT_MAX_LEGNTH=2000;
	private static final String ENC_TYPE="UTF-8";
	
	private static ResourceBundle pathBundle;
	private static ResourceBundle queryBundle;
	private static ResourceBundle googleBundle;
	
	
	public static final long PAGE_SIZE=15;
	static {
		pathBundle=ResourceBundle.getBundle(PATH_BUNDLE);
		queryBundle=ResourceBundle.getBundle(QUERY_BUNDLE);
		googleBundle=ResourceBundle.getBundle(GOOGLE_BUNDLE);
		MEMBER_PARAM_MAP=new HashMap<String, String>();
		combineMap(MEMBER_PARAM_KEYS,MEMBER_PARAM_KEYS_KR);
	}
	
	public static void combineMap(String[] key,String[] value) {
		for(int i=0; i<key.length; i++) {
			MEMBER_PARAM_MAP.put(key[i], value[i]);
		}
	}
	
	
	public static String getPath(String key) {
		return pathBundle.getString(key);
	}
	
	public static String getQuery(String key) {
		return queryBundle.getString(key);
	}
	
	public static String getUser(String key) {
		return googleBundle.getString(key);
	}
	
	public static String isEmpty(Map<String,String[]> map,String key) {
		String[] requiredKeys=key.equals("MEMBER")?MEMBER_PARAM_KEYS:BBS_PARAM_KEYS;
		Arrays.sort(requiredKeys);
		Set<String> keys=map.keySet();
		Stream<String> stream=keys.stream().filter(x->x==null);
		if(key.equals("MEMBER")) {
			return stream.count()>0?MEMBER_PARAM_MAP.get(stream.findFirst().get()):null;
		}else {
			return stream.count()>1?"empty":null;
		}
	}
	
	public static String removePath(String filename,String sep) {
		return filename.substring(filename.lastIndexOf(sep)+1,filename.length());
	}
	
	public static String subString(String text) {
		byte[] textToByte;
		try {
			textToByte = text.getBytes(ENC_TYPE);
			if(textToByte.length>TEXT_MAX_LEGNTH) {
				byte[] trimArr=new byte[TEXT_MAX_LEGNTH];
				System.arraycopy(textToByte, 0, trimArr, 0, TEXT_MAX_LEGNTH);
				return new String(trimArr,Charset.forName(ENC_TYPE));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return text;
	}
	
	
	
}
