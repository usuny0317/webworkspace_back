package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TodoService {
	
	@Autowired
	private TodoRepository repository;
	
	public List<TodoEntity> create(final TodoEntity entity){
	
		if(entity==null) {
			log.warn("Entity cannot be null.");
			throw new RuntimeException("Entity cannot be null.");
		}
		
		if(entity.getUserId()==null) {
			log.warn("Unknown User");
			throw new RuntimeException("Unknown User");
		}
		
		repository.save(entity);
		
		log.info("Entity Id : {} is saved.", entity.getId());
		
		return repository.findByUserId(entity.getUserId());
	}
	
	public List<TodoEntity> retrieve(final String userId){
		return repository.findByUserId(userId);
	}
	
	public List<TodoEntity> update(final TodoEntity entity){
		//(1)
		validate(entity);
		
		try {
			//(2)
			repository.delete(entity);
		}catch(Exception e) {
			//(3)
			log.error("error deleting entity", entity.getId(),e);
			//(4)
			throw new RuntimeException("error deleting entity"+entity.getId());
		}
		//(5)새 Todo 리스트를 가져와 리턴한다.
		return retrieve(entity.getUserId());
	}
	public List<TodoEntity> delete(final TodoEntity entity){
		//(1)
		validate(entity);
		try {
			//(2)
			repository.delete(entity);
		}catch(Exception e) {
			//(3)
			log.error("error deleting entity", entity.getId(), e);
		
		//(4)
		throw new RuntimeException("error deleting entity "+ entity.getId());
		}
		return retrieve(entity.getUserId());
	}
	
	public void validate(final TodoEntity entity) {
		if(entity ==null ) {
			log.warn("Entity cannot be null");
			throw new RuntimeException("Entity cannot be null");
		}
		if(entity.getUserId()==null) {
			log.warn("Unknown user.");
			throw new RuntimeException("Unknown user");
		}
	}
	
	//public String testService() {
		//TodoEntity entity= TodoEntity.builder().title("유선영 첫 타이틀").build();
		//repository.save(entity);
		//TodoEntity savedEntity=repository.findById(entity.getId()).get();
		
		//return savedEntity.getTitle();
	//}
}
