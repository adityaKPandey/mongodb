package com.mongodb.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.dao.CandidateDao;
import com.mongodb.dao.repository.CandidateRepository;
import com.mongodb.entity.Candidate;
import com.mongodb.entity.ResourceNotFoundException;
import com.mongodb.entity.Skill;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/candidate") //"/v1/schedule"
public class CandidateController {

	@Autowired
	private CandidateRepository candidateRepository ;

	@Autowired
	private CandidateDao candidateDao ;


	//@CrossOrigin(origins = "${application.site.url}" )
	@PutMapping(path = "/",produces=MediaType.APPLICATION_JSON_VALUE , consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("adds/edits candidate")
	public Candidate getList( /*@ApiParam*/ @RequestBody(required=true) Candidate candidate
			) throws Exception{

		candidate = candidateRepository.save(candidate);
		return candidate ;

	}

	@PutMapping(path = "/bulk/" , consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("adds/edits candidates in bulk")
	public void insertBulk( /*@ApiParam*/ @RequestBody(required=true) List<Candidate> candidates
			) throws Exception{

		candidateDao.bulkUpdateOrInsert("candidate", candidates);


	}

	@DeleteMapping(path = "/" )
	@ApiOperation("adds/edits candidates in bulk")
	public void deleteAll() throws Exception{

		candidateRepository.deleteAll();


	}

	@PutMapping(path = "/bulk/batch/{count}" , consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation("adds/edits candidates in bulk")
	public void insertBulk(@PathVariable int count) throws Exception{

		List<Skill> skills =  new ArrayList<>();
		skills.add(new Skill("Java", 5.8));
		skills.add(new Skill("Oracle" , 4.5));
		skills.add(new Skill("Spring Boot" , 2.3)) ;

		List<Candidate> candidates = new ArrayList<>();
		List<String>names = Arrays.asList("Ravi Kumar" , "Pradeep Tiwari" , "Jesse Jones" , "Iqbal Shah" , "Divya Malik") ;

		Random random = new Random();
		random.nextInt(names.size()) ;
		
		
		for(int i = 0; i < count ; i++) {
			int id = 1+i;
			candidates.add(new Candidate(""+id, names.get( random.nextInt(names.size()) ), Math.random(),
					/*UUID.randomUUID().toString()*/id+"@gmail.com", skills));
		}

		long timeStarted = System.currentTimeMillis();
		System.out.println(new Date().toString() + " started");
		candidateDao.bulkUpdateOrInsert("candidate", candidates);

		System.out.println("Time taken(in secs) for inserts :" + (System.currentTimeMillis()-timeStarted)/1000.0 );

	}


	@GetMapping(path = "/",produces=MediaType.APPLICATION_JSON_VALUE )
	@ApiOperation("fetches candidates")
	public List<Candidate> getList() throws Exception{


		return candidateRepository.findAll(Sort.by(Direction.DESC, "experience")) ;

	}

	@GetMapping(path = "/mail/{email}",produces=MediaType.APPLICATION_JSON_VALUE )
	@ApiOperation("fetches candidates")
	public Candidate getCandidate(@PathVariable String email) throws Exception{


		return candidateRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException());

	}

	@GetMapping(path = "/exp/{from}/{to}",produces=MediaType.APPLICATION_JSON_VALUE )
	@ApiOperation("fetches candidates")
	public List<Candidate> getCandidate(@PathVariable String from ,  @PathVariable String to) throws Exception{


		return candidateRepository.findByExperienceBetween(Double.valueOf(from) , Double.valueOf(to));

	}

	@GetMapping(path = "/skills/exp/{exp}",produces=MediaType.APPLICATION_JSON_VALUE )
	@ApiOperation("fetches candidates")
	public List<Candidate> getCandidate( @RequestParam String skill ,  @PathVariable Double exp) throws Exception{

		return candidateRepository.find(skill, exp);
		//return candidateRepository.find(skill) ;

	}

}
