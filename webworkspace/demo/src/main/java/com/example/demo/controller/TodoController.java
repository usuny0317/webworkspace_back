package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TestRequestBodyDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;

@RestController
@RequestMapping("todo")
public class TodoController {
	
	@Autowired
	private TodoService service;
	
	@PostMapping
	public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto){
		try {
			String temporaryUserId="temporary-user";
			
			//TodoEntity�� ��ȯ
			TodoEntity entity=TodoDTO.toEntity(dto);
			//id�� null�� �ʱ�ȭ
			entity.setId(null);
			//�ӽ� ����� ���̵� ����,4�忡�� ���� ������, �ϴ� �α��� ���� �����ϵ���
			entity.setUserId(temporaryUserId);
			//���񽺸� �̿��Ͽ� Todo��ƼƼ ���
			List<TodoEntity> entities=service.create(entity);
			//�ڹ� ��Ʈ�� �̿�, ���ϵ� ����Ƽ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ
			List<TodoDTO> dtos=entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			//��ȯ�� TodoDTO ����Ʈ�� �̿��Ͽ� ResponseDTO �ʱ�ȭ
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			//ResponseDTO ����
			return ResponseEntity.ok().body(response);
		}catch(Exception e) {
			//Ȥ�� ���ܰ� �ִ� ��� dto ��� error�� �־��ش�.
			String error=e.getMessage();
			ResponseDTO<TodoDTO> response=ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	@PutMapping
	public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto){
		String temporaryUserId="temporary-user";
		//(1)
		TodoEntity entity=TodoDTO.toEntity(dto);
		//(2)
		entity.setUserId(temporaryUserId);
		//(3)
		List<TodoEntity> entities=service.update(entity);
		//(4)
		List<TodoDTO> dtos=entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		//(5)
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		return ResponseEntity.ok().body(response);
	}
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto){
		try {
			String temporaryUserId="temporary-user";
			//(1)
			TodoEntity entity=TodoDTO.toEntity(dto);
			//(2)
			entity.setUserId(temporaryUserId);
			//(3)
			List<TodoEntity> entities=service.delete(entity);
			//(4)
			List<TodoDTO> dtos=entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			//(5)
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			//(6)
			return ResponseEntity.ok().body(response);
			}catch(Exception e) {
				//(7)
				String error=e.getMessage();
				ResponseDTO<TodoDTO> response=ResponseDTO.<TodoDTO>builder().error(error).build();
				return ResponseEntity.badRequest().body(response);
			}
	}
	@GetMapping
	public ResponseEntity<?> retrieveTodoList(){
		String temporaryUserId="temporary-user";
		
		//(1)
		List<TodoEntity> entities =service.retrieve(temporaryUserId);
		
		//(2)
		List<TodoDTO> dtos= entities.parallelStream().map(TodoDTO:: new).collect(Collectors.toList());
		
		//(3)
		ResponseDTO<TodoDTO> response=ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		//(4)
		return ResponseEntity.ok().body(response);
	}

	/*
	@GetMapping("/test")
	public ResponseEntity<?> testTodo() {
		String str=service.testService();
		
		List<String> list=new ArrayList<>();
		list.add(str);
		
		ResponseDTO<String> response=ResponseDTO.<String>builder().data(list).build();
		return ResponseEntity.ok().body(response);
	}
	//����
	@GetMapping("/testTodo")
	public String testControllerWithRequestBody(@RequestBody TodoDTO dto) {
		return "Id: "+ dto.getId()+", title: "+dto.getTitle()+", done: " + dto.isDone();
	}
	
	
	@GetMapping("/testTodo1")
	public String testControllerWithRequestBody1(@RequestBody TestRequestBodyDTO dto) {
		//return "Id: "+ dto.getId()+", title: "+dto.getMessage()+", done: true";
		String str1=service.testService();
		List<String> list=new ArrayList<>();
		list.add(str1);
		
		ResponseDTO<String> response=ResponseDTO.<String>builder().data(list).build();
		return "Id: "+ dto.getId()+", title: "+dto.getMessage()+", done: " ;
	}*/
}
