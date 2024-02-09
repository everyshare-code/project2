package board.service;

import java.util.List;
import java.util.Map;

import board.member.model.MemberDTO;

public interface DaoService<T> {
	List<T> selectAll(Map map);
	T selectOne(String ...params);
	int insert(T dto);
	int delete(T dto);
	int update(T dto);
	void close();
}
