package com.mongodb.dao.impl;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.dao.CandidateDao;
import com.mongodb.entity.Candidate;

@Repository
public class CandidateDaoImpl implements CandidateDao{

	@Autowired
	private MongoTemplate mongoTemplate ;

	private static final int BATCH_SIZE = 80000 ; // 250000   1000  100000 200000 60000

	@Override
	public <T> void bulkUpdate(String collectionName, List<T> documents, Class<T> tClass) {
		String [] UNDERSCORE_ID = {"_id"} ;
		//mongoTemplate.
		BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, tClass, collectionName);
		int i = 0 , j = 0;
		for (T document : documents) {
			i++;
			Document doc = new Document();
			mongoTemplate.getConverter().write((Candidate)document, (Bson) doc);
			org.springframework.data.mongodb.core.query.Query  query = new org.springframework
					.data.mongodb.core.query.Query(Criteria.where("_id").is(doc.get(UNDERSCORE_ID)));
			Document updateDoc = new Document();
			updateDoc.append("$set", doc);
			Update update = Update.fromDocument(updateDoc, UNDERSCORE_ID);
			bulkOps.upsert(query, update);
			if(i == BATCH_SIZE) {
				j++;
				BulkWriteResult result = bulkOps.execute();
				System.out.println("Records inserted: "+result.getInsertedCount() + " for batch: "+j);
				i = 0;
			} 
			//	mongoTemplate.insertAll(documents);

		}

		if(i > 0) {
			j++;
			BulkWriteResult result = bulkOps.execute();
			System.out.println("Records inserted: "+result.getInsertedCount() + " for batch: "+j);
		}

	}


	// https://ashutosh-srivastav-mongodb.blogspot.com/2017/09/mongodb-bulkwrite-java-api.html
	@Override
	public  void bulkUpdateOrInsert(String collectionName, List<Candidate> candidates) throws Exception{


		//mongoTemplate.insertAll(candidates);

		try {

			String [] UNDERSCORE_ID = {"_id"} ;
			//mongoTemplate.

			int i = 0 , j = 0;
			BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Candidate.class, collectionName);
			for (Candidate candidate : candidates) {
				i++;
				
				Document doc = new Document();
				mongoTemplate.getConverter().write((Candidate)candidate, (Bson) doc);
				/*
			org.springframework.data.mongodb.core.query.Query  query = new org.springframework
					.data.mongodb.core.query.Query(Criteria.where("_id").is(doc.get(UNDERSCORE_ID[0])));
			Document updateDoc = new Document();
			updateDoc.append("$set", doc);
			Update update = Update.fromDocument(updateDoc, UNDERSCORE_ID);

			bulkOps.upsert(query, update);
				 */
				bulkOps.insert(doc);

				if(i == BATCH_SIZE) {
					j++;
					BulkWriteResult result = bulkOps.execute();
					System.out.println(new Date().toString() + " Records inserted in last batch : "+result.getInsertedCount() + "," +result.getMatchedCount() + "," +result.getModifiedCount()+ " for batch: "+j);
					i = 0;
					bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Candidate.class, collectionName);
				} 

			}
			
			
			//bulkOps.execute();

			if(i > 0) {
				j++;
				BulkWriteResult result = bulkOps.execute();
				System.out.println( new Date().toString() +" Records inserted in last batch : "+result.getInsertedCount() + "," +result.getMatchedCount() + "," +result.getModifiedCount()+ " for batch: "+j);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

}
