package com.loan.rate.domain.respository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.loan.rate.domain.Article;

public interface LoanRateRepository extends MongoRepository<Article, String> {

	@Override
    public List<Article> findAll();
	
//	public List<Article> readAll() {
//
//		// Article a = new Article();
//		// a.setDescription("TEST");
//		// a.setName("TEST_NAME");
//		// a.setImageUrl("Fort_Worth_TX.jpg");
//		// Article b = new Article();
//		// b.setDescription("TEST2");
//		// b.setName("TEST_NAME");
//		// b.setImageUrl("me.jpg");
//		// return Arrays.asList(a, b);
//		return findAll();
//	}
}