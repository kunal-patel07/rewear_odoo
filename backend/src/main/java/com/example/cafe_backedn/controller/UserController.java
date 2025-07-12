package com.example.cafe_backedn.controller;

import com.example.cafe_backedn.advices.PaginationResponse;
import com.example.cafe_backedn.dto.LoginDto;
import com.example.cafe_backedn.dto.UpdateUserDto;
import com.example.cafe_backedn.dto.UserDto;
import com.example.cafe_backedn.dto.UserPaginateDto;
import com.example.cafe_backedn.services.AuthService;
import com.example.cafe_backedn.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping(path = "/")
    public String getAll(){
        return "working ";
    }

    @GetMapping(path = "/get-all-user")
    public ResponseEntity<List<UserDto>> getUser(){
        List<UserDto> response=userService.getUserInfo();
        if(response== null) ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<UserDto> storeUser(@Valid @RequestBody() UserDto body){
        UserDto dto=userService.storeUser(body);
        System.out.println("the dto="+body);
        if(dto==null) return ResponseEntity.badRequest().build();
        System.out.println("output="+dto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping(path = "/update-user/{id}")
    public ResponseEntity<UpdateUserDto> updateUser(@PathVariable Long id, @Valid @RequestBody() UpdateUserDto body){
        body.setId(id);
        UpdateUserDto dto = userService.updateUser(body);
        if(dto == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(dto);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginDto> loginUser(@Valid @RequestBody() LoginDto body){
        LoginDto dto=authService.loginUser(body);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/api/users")
    public ResponseEntity<PaginationResponse<UserPaginateDto>> getOrder(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String keyword
    ) {
        Pageable pageable = PageRequest.of(page - 1, size,Sort.by(Sort.Direction.DESC, "id"));
        PaginationResponse<UserPaginateDto> response = userService.getPaginatedUser(pageable,keyword);
        return ResponseEntity.ok(response);
    }
}
