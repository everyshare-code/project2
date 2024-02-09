package board.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTOkens {
	
	
	
	private static SecretKey accessKey;
	private static SecretKey refreshKey;
	private static final String KEY_PATH="/resources/tokens";//확장자는 생략(.properties)
	private static final String ACCESS_KEY="access-key";
	private static final String REFRESH_KEY="refresh-key";
	public static final int ACCESS=1;
	public static final int REFRESH=2;
	public static final int SIGNED=200;
	public static final int UNSIGNED=300;
	public static final int EXPIRED=400;
	
	
	static {
		ResourceBundle resource=ResourceBundle.getBundle(KEY_PATH);
		String accesskey = resource.getString(ACCESS_KEY);
		byte[] secret=Base64.getEncoder().encodeToString(accesskey.getBytes()).getBytes(StandardCharsets.UTF_8);
		accessKey= Keys.hmacShaKeyFor(secret);
		String refreshkey = resource.getString(REFRESH_KEY);
		secret=Base64.getEncoder().encodeToString(refreshkey.getBytes()).getBytes(StandardCharsets.UTF_8);
		refreshKey= Keys.hmacShaKeyFor(secret);
	}	
	
	/**
	 * JWT토큰을 생성해서 반환하는 메소드
	 * @param username 사용자 아이디	
	 * @param payloads 추가로 사용자 정보를 저장하기 위한 Claims
	 * @param expirationTime 토큰 만료 시간(15분에서 몇 시간이 적당).단위는 천분의 1초
	 * @return
	 */
	public static String createToken(String username,Map<String, Object> payloads,long expirationTime,int status) {
			
		//JWT 토큰의 만료 시간 설정
		long currentTimeMillis = System.currentTimeMillis();//토큰의 생성시간
		expirationTime = currentTimeMillis + expirationTime; //토큰의 만료시간		
		
		//Header 부분 설정
		Map<String, Object> headers = new HashMap<>();
		headers.put("typ", "JWT");
		headers.put("alg", "HS256");
		
		/*
		 아래 세줄 사용시 .setClaims(payloads)->.setClaims(claims로 변경)
		Claims claims= Jwts.claims().setSubject(username);		
		claims.putAll(payloads);
		claims.put("roles", "권한");
		
		*/
		
		JwtBuilder builder = Jwts.builder()
				.header().add(headers).and()// Headers 설정
				.claims(payloads)// Claims 설정(기타 페이로드)
				.subject(username)//사용자 ID 설정
				.issuedAt(new Date())//생성 시간을 설정
				.expiration(new Date(expirationTime))//만료 시간 설정(필수로 설정하자.왜냐하면 토큰(문자열이라)은 세션처럼 제어가 안된다)
				.signWith(status==ACCESS?accessKey:refreshKey,Jwts.SIG.HS256);//비밀 키로 JWT를 서명
		
		//JWT 생성
		String jwt = builder.compact();		
		return jwt;
	}
	

	
	/**
	 * 발급한 토큰의 payloads부분을 반환하는 메소드
	 * @param token  발급토큰	
	 * @return 토큰의 payloads부분 반환
	 */
	
	public static Map<String, Object> getTokenPayloads(String token,int status) {
		
		Map<String, Object> claims = new HashMap<>();
	
		
		try {
			//JWT토큰 파싱 및 검증
			claims = Jwts.parser()
					.verifyWith(status==ACCESS?accessKey:refreshKey).build()//서명한 비밀키로 검증
					.parseSignedClaims(token)//parseClaimsJws메소드는 만기일자 체크
					.getPayload();	
			return claims;
		} 		
		catch (Exception e) {
			System.out.println("유효하지 않은 토큰");
			//유효하지 않는 토큰
			claims.put("invalid","Invalid Token");		
		}
		return claims;
	}/////////////////////////////////
	
	
	
	/**
	 * 유효한 토큰인지 검증하는 메소드
	 * @param token  발급토큰	
	 * @return 유효한 토큰이면 true,만료가됬거나 변조된 토큰인 경우 false반환
	 */
	
	public static int verifyToken(String token,int status) {		
		try {
			
			//JWT토큰 파싱 및 검증
			Jws<Claims> accessClaims = Jwts.parser()
					.verifyWith(status==ACCESS?accessKey:refreshKey).build()//서명한 비밀키로 검증
					.parseSignedClaims(token);//parseClaimsJws메소드는 만기일자 체크
			//토큰의 유효성과 만료일자 확인
			System.out.println("만기일자:"+accessClaims.getPayload().getExpiration());
			
		} 	
		catch (ExpiredJwtException e) {
			System.out.println("토큰 시간 만료");
			return EXPIRED;
			//e.printStackTrace();
			//System.out.println("유효하지 않은 토큰입니다:"+e.getMessage());
		}catch(SignatureException|MalformedJwtException e) {
			System.out.println("서명 검증 실패");
			return UNSIGNED;
		}
		return SIGNED;
	}/////////////////////////////////
	
	
	/**
	 * 문자열인 발급된 토큰을 요청헤더의 쿠키에서 읽어오는 메소드
	 * @param request 요청헤더에서 쿠키를 읽어오기 위한 HttpServletRequest객체
	 * @param cookieName  토큰 발급시 설정한 쿠키명
	 * @return 발급된 토큰
	 */
	
	public static String getToken(HttpServletRequest request,String cookieName) {
		//발급한 토큰 가져오기
		Cookie[] cookies=request.getCookies();
		String token="";
		if(cookies !=null){
			for(Cookie cookie:cookies){
				if(cookie.getName().equals(cookieName)){
					token= cookie.getValue();
				}
			}
		}
		return token;
	}///////////
	
	/**
	 * 토큰을 삭제하는 메소드
	 * @param request HttpServletRequest객체
	 * @param response HttpServletRequest객체
	 */

	public static void removeToken(HttpServletRequest request,HttpServletResponse response) {
		Cookie accessCookie = new Cookie(request.getServletContext().getInitParameter("ACCESS"),"");
		accessCookie.setPath("/");
		accessCookie.setMaxAge(0);
		Cookie refreshCookie = new Cookie(request.getServletContext().getInitParameter("REFRESH"),"");
		accessCookie.setPath("/");
		accessCookie.setMaxAge(0);
		response.addCookie(accessCookie);
		response.addCookie(refreshCookie);
	}////////////////////////////	
	
	

}
