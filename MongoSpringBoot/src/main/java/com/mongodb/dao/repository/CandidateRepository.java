package com.mongodb.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;

import com.mongodb.entity.Candidate;

//@Repository
@Component
public interface CandidateRepository extends MongoRepository<Candidate, Integer> {

	Optional<Candidate> findByEmail(String mail );
	
	List<Candidate> findByExperienceBetween(double from , double to);
	
	//@Query(value = "{ 'skills.name':'?0','skills.experience':?1}")
	@Query(value = "{ 'skills.name':'?0','skills.experience': { '$gt' : ?1 } }")
	List<Candidate> find(String name , Double experience);
	
	@Query(value = "{'skills.name':'?0'}")
	List<Candidate> find(String name );
	 
}
