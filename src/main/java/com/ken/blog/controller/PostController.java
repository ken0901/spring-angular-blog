package com.ken.blog.controller;

import com.ken.blog.dto.PostDto;
import com.ken.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * insert into post from database
     * @param postDto
     * @return ReponseEntity
     */
    @PostMapping
    public ResponseEntity createPost(@RequestBody PostDto postDto){
        System.out.println("create post execute");
        postService.createPost(postDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * select post table from database
     * @return ReponseEntity
     */
    @GetMapping("/all")
    public ResponseEntity<List<PostDto>> showAllPosts(){
        return new ResponseEntity<>(postService.showAllPosts(), HttpStatus.OK);
    }

    /**
     * select post by id from database
     * @param id
     * @return ReponseEntity
     */
    @GetMapping("/get/{id}")
    public ResponseEntity getSinglePost(@PathVariable @RequestBody Long id){
        return new ResponseEntity<>(postService.readSinglePost(id),HttpStatus.OK);
    }
}
