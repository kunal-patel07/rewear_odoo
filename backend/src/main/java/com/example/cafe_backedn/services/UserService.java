package com.example.cafe_backedn.services;

import com.example.cafe_backedn.advices.PaginationResponse;
import com.example.cafe_backedn.config.OrderMapper;
import com.example.cafe_backedn.config.UserMapper;
import com.example.cafe_backedn.dto.OrderDto;
import com.example.cafe_backedn.dto.UserDto;
import com.example.cafe_backedn.dto.UserPaginateDto;
import com.example.cafe_backedn.entity.OrderEntity;
import com.example.cafe_backedn.entity.UserEntity;
import com.example.cafe_backedn.exception.ResourceNotFound;
import com.example.cafe_backedn.repo.UserRepo;
import com.example.cafe_backedn.utils.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    public List<UserDto> getUserInfo(){
        List<UserEntity> dto=userRepo.findAllByOrderByIdDesc();
        if(dto == null) return null;
        return dto.stream()
                .map(UserEntity -> modelMapper.map(UserEntity, UserDto.class))
                .collect(Collectors.toList());
    }

    public UserDto storeUser(UserDto userInfo){
        Optional<UserEntity> userEntity=userRepo.findByEmail(userInfo.getEmail());
        if(userEntity.isPresent()){
            throw new BadCredentialsException("user name already found");
        }
        UserEntity user=modelMapper.map(userInfo,UserEntity.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserEntity finalSave=userRepo.save(user);
        return modelMapper.map(finalSave,UserDto.class);
    }


    public PaginationResponse<UserPaginateDto>getPaginatedUser(Pageable pageable, String keyword) { Page<UserEntity> page;

        if (keyword == null || keyword.trim().isEmpty()) {
            page = userRepo.findAll(pageable);
        } else {
            page = userRepo.findByNameContainingIgnoreCase(keyword, pageable);
        }

        Page<UserPaginateDto> dtoPage = page.map(userMapper::toDto);
        return PaginationUtil.buildPageResponse(dtoPage);
    }

    public UserEntity findByUserId(Long id){
        return userRepo.findById(id).orElseThrow(()->new BadCredentialsException("user not found with this "+id));
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username).orElseThrow(()->new BadCredentialsException("username is not found"));
    }


}
