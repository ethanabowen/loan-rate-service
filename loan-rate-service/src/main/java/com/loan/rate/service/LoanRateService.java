package com.loan.rate.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loan.rate.domain.respository.LoanRateRepository;
import com.loan.rate.pdf.domain.Article;
import com.loan.rate.rest.contract.ArticleResponse;
import com.loan.rate.rest.factory.ArticleResponseFactory;

@Component
public class LoanRateService {

	@Autowired
	private LoanRateRepository articleRepository;
	@Autowired
	private ArticleResponseFactory articleResponseFactory;
	
	public List<ArticleResponse> getArticles() {
		List<ArticleResponse> articleResponses = new ArrayList<>();
		List<Article> articles = articleRepository.findAll();
		
		for(Article article: articles) {
			ArticleResponse response = articleResponseFactory.create(article);
			
			articleResponses.add(response);
		}
		
		return articleResponses;
	}
}
