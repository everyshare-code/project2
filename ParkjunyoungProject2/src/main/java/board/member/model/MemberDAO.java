package board.member.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import board.service.DaoService;
import board.util.BoardUtil;
import board.util.PathKeys;
import board.util.QueryKeys;
import jakarta.servlet.http.HttpServletRequest;

public class MemberDAO implements DaoService<MemberDTO>{
	
	private Connection conn;
	private ResultSet rs;
	private PreparedStatement psmt;
	
	public MemberDAO(HttpServletRequest req) {
		try {
			DataSource source=(DataSource)req.getServletContext().getAttribute("DataSource");
			conn=source.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<MemberDTO> selectAll(Map map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemberDTO selectOne(String ...params) {
		MemberDTO dto=null;
		String sql=BoardUtil.getQuery(QueryKeys.MEMBER_SELECT_ONE.getValue());
		try {
			psmt=conn.prepareStatement(sql);
			
			psmt.setString(1, params[0]);
			
			rs=psmt.executeQuery();
			if(rs.next()) {
				dto=new MemberDTO();
				dto.setUsername(rs.getString(1));
				dto.setPassword(rs.getString(2));
				dto.setGender(rs.getString(3));
				dto.setInters(rs.getString(4));
				dto.setEducation(rs.getString(5));
				dto.setSelf_intro(rs.getString(6));
				dto.setJoinDate(rs.getString(7));
				dto.setContent_type(rs.getString(9));
				if(dto.getContent_type()!=null) {
					dto.setSelf_image(dto.getContent_type()+","
							+Base64.getEncoder().encodeToString(rs.getBytes(8)));
				}
				return dto;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dto;
	}

	@Override
	public int insert(MemberDTO dto) {
		int affected=0;
		String sql=BoardUtil.getQuery(QueryKeys.MEMBER_INSERT.getValue());
		try {
			psmt=conn.prepareStatement(sql);
			psmt.setString(1, dto.getUsername());
			psmt.setString(2, dto.getPassword());
			psmt.setString(3, dto.getGender());
			psmt.setString(4, dto.getInters());
			psmt.setString(5, dto.getEducation());
			psmt.setString(6, dto.getSelf_intro());
			if(dto.getSelf_image()!=null) {
				psmt.setBytes(7, Base64.getDecoder().decode(dto.getSelf_image()));
				psmt.setString(8, dto.getContent_type());
			}else {
				psmt.setNull(7, Types.BLOB);
				psmt.setNull(8, Types.VARCHAR);
			}
			
			affected=psmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return affected;
	}
	
	public int selectCount(MemberDTO dto) {
		int count=0;
		String sql=BoardUtil.getQuery(QueryKeys.MEMBER_GET_COUNT.getValue());
		try {
			psmt=conn.prepareStatement(sql);
			psmt.setString(1, dto.getUsername());
			psmt.setString(2, dto.getPassword());
			rs=psmt.executeQuery();
			if(rs.next()) {
				count=rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int delete(MemberDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(MemberDTO dto) {
		int affected=0;
		String sql=BoardUtil.getQuery(QueryKeys.MEMBER_UPDATE.getValue());
		try {
			psmt=conn.prepareStatement(sql);
			psmt.setString(1, dto.getPassword());
			psmt.setString(2, dto.getInters());
			psmt.setString(3, dto.getEducation());
			psmt.setString(4, dto.getSelf_intro());
			if(dto.getSelf_image()!=null) {
				psmt.setBytes(5, Base64.getDecoder().decode(dto.getSelf_image()));
				psmt.setString(6, dto.getContent_type());
			}else {
				psmt.setNull(5, Types.BLOB);
				psmt.setNull(6, Types.VARCHAR);
			}
			psmt.setString(7, dto.getUsername());
			affected=psmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
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
