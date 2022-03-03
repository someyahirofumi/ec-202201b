package jp.co.example.ecommerce_b.repository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class Itemrepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	


}
