package com.qing.tea.utils;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class DBMethod {
	public MongoDatabase mongoDatabase;
	public MongoCollection<Document> collection;
	public DBMethod(String collecyonName){
		mongoDatabase = MongoDBUtil.getConnect2();
		collection = mongoDatabase.getCollection(collecyonName);
	}
	
	public void add(Document document){
		collection.insertOne(document);
	}
	
	public void add(List<Document> list){
		collection.insertMany(list);
	}
	
	public void delete(Bson filter){
		 collection.deleteOne(filter);
	}
	
	public void deleteMany(Bson filter){
		collection.deleteMany(filter);
	}
	
	public void update(Bson filter,Document document){
		 collection.updateOne(filter, document);
	}
	
	public void updateMany(Bson filter,Document document){
		 collection.updateMany(filter, document);
	}
	
	public MongoCursor<Document> findAll(){
		FindIterable<Document> findIterable = collection.find();
	    MongoCursor<Document> cursor = findIterable.iterator();
	    return cursor;
	}
	
	public MongoCursor<Document> findByFillter(Bson filter){
		FindIterable<Document> findIterable = collection.find(filter);
	    MongoCursor<Document> cursor = findIterable.iterator();
	    return cursor;
	}
	
	public Document findFirst(Bson filter){
		FindIterable<Document> findIterable = collection.find(filter);
	    Document document = (Document) findIterable.first();
	    return document;
	}
}
