package com.mongodb.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mongodb.entity.Candidate;

@Repository
public interface CandidateDao {

	
	
    <T> void bulkUpdate(String collectionName, List<T> documents, Class<T> tClass) ;

	
	void bulkUpdateOrInsert(String collectionName, List<Candidate> candidates) throws Exception ;
}
