package board.bbs.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import com.google.api.client.http.HttpRequest;

import board.member.model.MemberDTO;
import board.service.DaoService;
import board.util.BoardUtil;
import board.util.QueryKeys;
import jakarta.servlet.http.HttpServletRequest;

public class BBSDAO implements DaoService<BBSDTO>{

	private Connection conn;
	private ResultSet rs;
	private PreparedStatement psmt;
	
	public BBSDAO(HttpServletRequest req) {
		try {
			DataSource source=(DataSource)req.getServletContext().getAttribute("DataSource");
			conn=source.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getTotalCount() {
		String sqlBbs=BoardUtil.getQuery(QueryKeys.BBS_TOTAL_COUNT.getValue());
		int count=0;
		try {
			psmt=conn.prepareStatement(sqlBbs);
			rs=psmt.executeQuery();
			if(rs.next()) count=rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	
	public void selectComments(List<BBSDTO> bbs){
		List<LikeDTO> likes=new Vector<LikeDTO>();
		StringBuilder sqlbuilder=new StringBuilder(BoardUtil.getQuery(QueryKeys.LIKES_SELECT.getValue()));
		
		for(BBSDTO bb:bbs) {
			sqlbuilder.append(Long.parseLong(bb.getNo())+",");
		}
		sqlbuilder.setCharAt(sqlbuilder.length()-1,')');
		try {
			psmt=conn.prepareStatement(sqlbuilder.toString());
			rs=psmt.executeQuery();
			while(rs.next()) {
				LikeDTO dto=new LikeDTO();
				dto.setNo(rs.getString(1));
				dto.setUsername(rs.getString(2));
				likes.add(dto);
			}
			
			bbs.forEach(bb->{
				List<LikeDTO> likes_=likes.stream().filter(like->bb.getNo().equals(like.getNo())).collect(Collectors.toList());
				bb.setLikes(likes_);
			});
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int insertLike(LikeDTO dto) {
		int affected=0;
		
		String sql=BoardUtil.getQuery(QueryKeys.LIKES_INSERT.getValue());
		try {
			psmt=conn.prepareStatement(sql);
			psmt.setLong(1,Long.parseLong(dto.getNo()));
			
			psmt.setString(2, dto.getUsername());
			
			affected=psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return affected;
	}
	
	public int deleteLike(LikeDTO dto) {
		int affected=0;
		String sql=BoardUtil.getQuery(QueryKeys.LIKES_DELETE.getValue());
		try {
			psmt=conn.prepareStatement(sql);
			psmt.setLong(1,Long.parseLong(dto.getNo()));
			psmt.setString(2, dto.getUsername());
			affected=psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return affected;
	}
	
	public List<BBSDTO> selectAllbyUsername(MemberDTO member){
		String sql=BoardUtil.getQuery(QueryKeys.BBS_SELECT_ALL_USER.getValue());
		List<BBSDTO> list=new Vector<>();
		try {
			psmt=conn.prepareStatement(sql);
			psmt.setString(1, member.getUsername());
			rs=psmt.executeQuery();
			while(rs.next()) {
				BBSDTO dto=new BBSDTO();
				dto.setNo(rs.getString(1));
				dto.setUsername(rs.getString(2));
				dto.setContent(rs.getString(3));
				if(rs.getBytes(4)!=null) {
					dto.setImg(Base64.getEncoder().encodeToString(rs.getBytes(4)));
					dto.setContent_type(rs.getString(5));
				}
				dto.setPostdate(rs.getString(6));
				list.add(dto);
			}
			if(list.size()>0)
				selectComments(list);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	@Override
	public List<BBSDTO> selectAll(Map map) {
		String sqlBbs=BoardUtil.getQuery(QueryKeys.BBS_SELECT_ALL.getValue());
		String sqlBbsFirst=BoardUtil.getQuery(QueryKeys.BBS_SELECT_ALL_FIRST.getValue());
		String searchSql="";
		List<BBSDTO> list=new Vector<BBSDTO>();
		try {
			
			long cursor=Long.parseLong(map.get("cursor").toString());
			String searchWord=map.get("searchWord")!=null?map.get("searchWord").toString():null;
			String searchUser=map.get("searchUser")!=null?map.get("searchUser").toString():null;
			
			if(searchWord!=null&&searchWord.length()!=0) {
				if(cursor==0) {
					searchSql=" WHERE CONTENT LIKE UPPER('%"+searchWord+"%') OR REPLACE(REPLACE(TAGS,'#',''),' ','') LIKE UPPER('%"+searchWord+"%') ";
					StringBuffer bf=new StringBuffer(sqlBbsFirst);
					bf.insert(bf.lastIndexOf("FETCH")-1, searchSql);
					sqlBbsFirst=bf.toString();
				}else {
					searchSql=" AND (CONTENT LIKE UPPER('%"+searchWord+"%') OR REPLACE(REPLACE(TAGS,'#',''),' ','') LIKE UPPER('%"+searchWord+"%') )";
					StringBuffer bf=new StringBuffer(sqlBbs);
					bf.insert(bf.lastIndexOf("ORDER")-1, searchSql);
					sqlBbs=bf.toString();
				}
			}else if(searchUser!=null&&searchUser.length()!=0) {
				if(cursor==0) {
					searchSql=" WHERE USERNAME='"+searchUser+"' ";
					StringBuffer bf=new StringBuffer(sqlBbsFirst);
					bf.insert(bf.lastIndexOf("FETCH")-1, searchSql);
					sqlBbsFirst=bf.toString();
				}else {
					searchSql=" AND USERNAME='"+searchUser+"' ";
					StringBuffer bf=new StringBuffer(sqlBbs);
					bf.insert(bf.lastIndexOf("ORDER")-1, searchSql);
					sqlBbs=bf.toString();
				}
			}
			
			if(cursor==-1) return null;
			else if(cursor==0) {
				psmt=conn.prepareStatement(sqlBbsFirst);
				psmt.setLong(1, BoardUtil.PAGE_SIZE);
			}
			else {
				psmt=conn.prepareStatement(sqlBbs);
				psmt.setLong(1, cursor);
				psmt.setLong(2, BoardUtil.PAGE_SIZE);
			}

			rs=psmt.executeQuery();
			
			while(rs.next()) {
				BBSDTO dto=new BBSDTO();
				dto.setNo(rs.getString(1));
				dto.setUsername(rs.getString(2));
				dto.setContent(rs.getString(3));
				dto.setView_count(rs.getString(4));
				dto.setPostdate(rs.getString(5));
				if(rs.getBytes(6)!=null) {
					dto.setProfile(rs.getString(7)+","+Base64.getEncoder().encodeToString(rs.getBytes(6)));
				}
				if(rs.getBytes(8)!=null) {
					dto.setImg(rs.getString(9)+","+Base64.getEncoder().encodeToString(rs.getBytes(8)));
				}
				list.add(0,dto);
			}
			
			if(list!=null&&!list.isEmpty()) {
				selectComments(list);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	@Override
	public BBSDTO selectOne(String... params) {
		
		return null;
	}

	@Override
	public int insert(BBSDTO dto) {
		String sqlBbs=BoardUtil.getQuery(QueryKeys.BBS_INSERT.getValue());
		String sqlBbsImg=BoardUtil.getQuery(QueryKeys.BBS_IMG_INSERT.getValue());
		int affected=0;
		try {
			psmt=conn.prepareStatement(sqlBbs,new String[] {"no"});
			psmt.setString(1, dto.getUsername());
			psmt.setString(2, dto.getContent());
			affected=psmt.executeUpdate();
			if(affected!=0) {
				rs=psmt.getGeneratedKeys();
				long affectedNo=0;
				if(rs.next()) 
					affectedNo=rs.getLong(1);
				psmt.close();
				rs.close();
				dto.setNo(String.valueOf(affectedNo));
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date=format.format(new java.util.Date());
				dto.setPostdate(date);
				if(dto.getImg()!=null) {
					psmt=conn.prepareStatement(sqlBbsImg);
					psmt.setLong(1, affectedNo);
					if(dto.getImg()!=null) {
						psmt.setBytes(2, Base64.getDecoder().decode(dto.getImg()));
						psmt.setString(3, dto.getContent_type());
					}else {
						psmt.setNull(2, Types.BLOB);
						psmt.setNull(3, Types.VARCHAR);
					}
					affected=psmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return affected;
	}

	@Override
	public int delete(BBSDTO dto) {
		int affected=0;
		String sqlBbs= BoardUtil.getQuery(QueryKeys.BBS_DELETE.getValue());
		String sqlBbsImg= BoardUtil.getQuery(QueryKeys.BBS_IMG_DELETE.getValue());
		String sqlBbsImgSelect=BoardUtil.getQuery(QueryKeys.BBS_IMG_SELECT.getValue());
		String sqlLikeDelete=BoardUtil.getQuery(QueryKeys.LIKES_DELETE_ALL.getValue());
		try {
			psmt=conn.prepareStatement(sqlLikeDelete);
			psmt.setLong(1, Long.parseLong(dto.getNo()));
			affected=psmt.executeUpdate();
			
			
			psmt=conn.prepareStatement(sqlBbsImgSelect);
			psmt.setLong(1, Long.parseLong(dto.getNo()));
			rs=psmt.executeQuery();
			if(rs.next()) {
				psmt=conn.prepareStatement(sqlBbsImg);
				psmt.setLong(1, Long.parseLong(dto.getNo()));
				affected=psmt.executeUpdate();
			}
			psmt.close();
			rs.close();
			psmt=conn.prepareStatement(sqlBbs);
			psmt.setLong(1, Long.parseLong(dto.getNo()));
			affected=psmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return affected;
	}
	
	public int updateTags(Long no,String tags) {
		int affected=0;
		String sql=BoardUtil.getQuery(QueryKeys.BBS_TAGS_UPDATE.getValue());
		try {
			psmt=conn.prepareStatement(sql);
			psmt.setString(1, tags);
			psmt.setLong(2, no);
			affected=psmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return affected;
	}
	

	@Override
	public int update(BBSDTO dto) {
		String sqlBbs=BoardUtil.getQuery(QueryKeys.BBS_UPDATE.getValue());
		String sqlBbsImgSelect=BoardUtil.getQuery(QueryKeys.BBS_IMG_SELECT.getValue());
		String sqlBbsImgInsert=BoardUtil.getQuery(QueryKeys.BBS_IMG_INSERT.getValue());
		String sqlBbsImg=BoardUtil.getQuery(QueryKeys.BBS_IMG_UPDATE.getValue());
		String sqlBbsImgDelete=BoardUtil.getQuery(QueryKeys.BBS_IMG_DELETE.getValue());
		int affected=0;
		try {
			psmt=conn.prepareStatement(sqlBbs);
			psmt.setString(1, dto.getContent());
			psmt.setString(2, dto.getNo());
			psmt.setString(3, dto.getUsername());
			affected=psmt.executeUpdate();
			if(affected!=0) {
				if(dto.getImg()!=null) {
					psmt=conn.prepareStatement(sqlBbsImgSelect);
					psmt.setLong(1, Long.parseLong(dto.getNo()));
					rs=psmt.executeQuery();
					if(rs.next()) {
						psmt.close();
						rs.close();
						psmt=conn.prepareStatement(sqlBbsImg);
						psmt.setString(3, dto.getNo());
						if(dto.getImg()!=null) {
							psmt.setBytes(1, Base64.getDecoder().decode(dto.getImg()));
							psmt.setString(2, dto.getContent_type());
						}else {
							psmt.setNull(1, Types.BLOB);
							psmt.setNull(2, Types.VARCHAR);
						}
						affected=psmt.executeUpdate();
					}else {
						psmt=conn.prepareStatement(sqlBbsImgInsert);
						psmt.setString(1, dto.getNo());
						if(dto.getImg()!=null) {
							psmt.setBytes(2, Base64.getDecoder().decode(dto.getImg()));
							psmt.setString(3, dto.getContent_type());
						}else {
							psmt.setNull(2, Types.BLOB);
							psmt.setNull(3, Types.VARCHAR);
						}
						affected=psmt.executeUpdate();
					}
				}else {
					psmt=conn.prepareStatement(sqlBbsImgDelete);
					psmt.setString(1, dto.getNo());
					affected=psmt.executeUpdate();
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return affected;
	}

	@Override
	public void close() {
		try {
			if(conn!=null) conn.close();
			if(rs!=null) rs.close();
			if(psmt!=null) psmt.close();
		}catch(SQLException e) {}
		
	}

}
