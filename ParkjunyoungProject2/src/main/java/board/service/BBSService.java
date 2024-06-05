package board.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import board.bbs.model.BBSDAO;
import board.bbs.model.BBSDTO;
import board.bbs.model.LikeDTO;
import board.util.BoardUtil;
import board.util.ImageVision;
import board.util.TranslateText;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.http.HttpServletRequest;

public class BBSService {
	
	public BBSDTO bbsInsert(HttpServletRequest req) {
		String profile=req.getParameter("profile");
		String username=req.getParameter("username");
		String content=req.getParameter("bbs-content-write");
		String[] fileInfo=null;
		String filename=null;
		String content_type=null;
		System.out.println("username="+username);
		if(req.getParameter("bbs-encoded-file-write")!=null&&
		!req.getParameter("bbs-encoded-file-write").isBlank()) {
			fileInfo=req.getParameter("bbs-encoded-file-write").split(",");
			filename=fileInfo[1];
			content_type=fileInfo[0];
		}
		BBSDTO dto=new BBSDTO(null, username, content, null, null, content_type,filename,profile,null,new Vector<>());
		
		BBSDAO dao=new BBSDAO(req);
		int affected=dao.insert(dto);
		dao.close();
		if(affected!=0) req.setAttribute("no", dto.getNo());
		if(fileInfo!=null) {
			AsyncContext async=req.startAsync();
			async.start(()->{
				try {
					String objects=ImageVision.detectLocalizedObjects(
							req.getParameter("bbs-encoded-file-write").toString());
					String texts=ImageVision.detectText(
							req.getParameter("bbs-encoded-file-write").toString());
					//감지된 객체가 있을 경우 한글 번역 값 추가
					if(objects.length()>0) objects+=TranslateText.translateText(objects);
					//감지된 텍스트가 있을 경우
					if(texts.length()>0) {
						//한글 번역
						String textsToKo=TranslateText.translateText(texts);
						System.out.println("textsToKo="+textsToKo);
						//영어일 경우에만 추가, 한글일 경우 빈문자열 추가
						texts+=textsToKo.equals(texts)?"":textsToKo;
					}
					//감지된 객체 문자열을 담은 objects가 빈 문자열이 아닐 경우에만 값 할당
					String tags=objects.length()>0?objects:"";
					//감지된 텍스트가 있을 경우에만 값 추가
					tags+=texts.length()>0?texts:"";
					
					//감지된 객체 혹은 텍스트가 있을 경우
					if(tags.length()>0) { 
						//#으로 나눔 -> 빈값을 제외 -> 중복값 제거 -> 영어 모두 대문자로 변환 -> 하나의 문자열로 합침
						tags=Arrays.stream(tags.replaceAll("\\r\\n", "")
								.split("#"))
								.filter(x->x.trim().length()>0)
								.distinct()
								.map(x->"#"+x.toUpperCase())
								.collect(Collectors.joining());
						System.out.println("tags="+BoardUtil.subString(tags));
						bbsUpdateTags(req,BoardUtil.subString(tags));
					}
					async.complete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		
		return affected==1?dto:null;
	}

	public BBSDTO bbsUpdate(HttpServletRequest req) {
		String username=req.getParameter("username");
		String no=req.getParameter("no");
		String content=req.getParameter("content");
		String encodedFile=req.getParameter("encoded-file");
		
		BBSDTO dto=new BBSDTO();
		dto.setUsername(username);
		dto.setNo(no);
		dto.setContent(content);
		if(encodedFile!=null&&!encodedFile.isEmpty()) {
			String[] fileInfo=encodedFile.split(",");
			dto.setContent_type(fileInfo[0]);
			dto.setImg(fileInfo[1]);
			req.setAttribute("no", dto.getNo());
			AsyncContext async=req.startAsync();
			async.start(()->{
				try {
					String objects=ImageVision.detectLocalizedObjects(req.getParameter("encoded-file").toString());
					String texts=ImageVision.detectText(req.getParameter("encoded-file").toString());
					System.out.println("texts="+texts);
					//감지된 객체가 있을 경우 한글 번역 값 추가
					if(objects.length()>0) objects+=TranslateText.translateText(objects);
					System.out.println("objects="+objects);
					//감지된 텍스트가 있을 경우
					if(texts.length()>0) {
						//한글 번역
						String textsToKo=TranslateText.translateText(texts);
						System.out.println("textsToKo="+textsToKo);
						//영어일 경우에만 추가, 한글일 경우 빈문자열 추가
						texts+=textsToKo.equals(texts)?"":textsToKo;
					}
					//감지된 객체 문자열을 담은 objects가 빈 문자열이 아닐 경우에만 값 할당
					String tags=objects.length()>0?objects:"";
					//감지된 텍스트가 있을 경우에만 값 추가
					tags+=texts.length()>0?texts:"";
					
					//감지된 객체 혹은 텍스트가 있을 경우
					if(tags.length()>0) { 
						//엔터값 삭제 ->#으로 나눔 -> 빈값을 제외 -> 중복값 제거 -> 영어 모두 대문자로 변환 -> 하나의 문자열로 합침
						tags=Arrays.stream(
								tags
								.replaceAll("[\\r\\n]", " ")
								.split("#"))
								.filter(x->x.trim().length()>0)
								.distinct()
								.map(x->"#"+x.toUpperCase())
								.collect(Collectors.joining());
						System.out.println("tags="+BoardUtil.subString(tags));
						bbsUpdateTags(req,BoardUtil.subString(tags));
					}
					async.complete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}else {
			if(req.getParameter("savedFile")!=null) {
				String[] fileInfo=req.getParameter("savedFile").split(",");
				dto.setContent_type(fileInfo[0]);
				dto.setImg(fileInfo[1]);
			}
		}
		
		
		
		
		BBSDAO dao=new BBSDAO(req);
		dao.update(dto);
		dao.close();
		
		return dto;
	}
	
	public int bbsUpdateTags(HttpServletRequest req,String tags) {
		int affected=0;
		Long no=Long.parseLong(req.getAttribute("no").toString());
		BBSDAO dao=new BBSDAO(req);
		affected=dao.updateTags(no,tags);
		dao.close();
		return affected;
	}
	

	public BBSDTO bbsDelete(HttpServletRequest req) {
		String no=req.getParameter("no");
		BBSDTO dto=new BBSDTO();
		dto.setNo(no);
		BBSDAO dao=new BBSDAO(req);
		int affected=dao.delete(dto);
		dao.close();
		return affected==0?null:dto;
	}

	public List<BBSDTO> bbsSelect(HttpServletRequest req) {
		BBSDAO dao=new BBSDAO(req);
		Map<String,Object> map=new HashMap<>();
		map.put("cursor", req.getParameter("cursor"));
		map.put("searchWord", req.getParameter("searchWord"));
		map.put("searchUser", req.getParameter("searchUser"));
		
		List<BBSDTO> list=dao.selectAll(map);
		dao.close();
		return list;
	}

	public int likeUp(HttpServletRequest req) throws StreamReadException, DatabindException, IOException {
		int affected=0;
		ObjectMapper mapper=new ObjectMapper();
		LikeDTO dto=mapper.readValue(req.getReader(), LikeDTO.class);
		BBSDAO dao=new BBSDAO(req);
		affected=dao.insertLike(dto);
		dao.close();
		return affected;
	}

	public int likeDown(HttpServletRequest req) throws StreamReadException, DatabindException, IOException {
		int affected=0;
		ObjectMapper mapper=new ObjectMapper();
		LikeDTO dto=mapper.readValue(req.getReader(), LikeDTO.class);
		BBSDAO dao=new BBSDAO(req);
		affected=dao.deleteLike(dto);
		dao.close();
		return affected;
	}

	
	
	
}
