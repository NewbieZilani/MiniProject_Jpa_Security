package com.spring.main.services;

import com.spring.main.constants.AppConstants;
import com.spring.main.dtoClasses.UserDto;
import com.spring.main.entity.BookEntity;
import com.spring.main.entity.BorrowingEntity;
import com.spring.main.entity.UserEntity;
import com.spring.main.exception.CustomException;
import com.spring.main.repository.BorrowingRepository;
import com.spring.main.repository.UserRepository;
import com.spring.main.utils.JWTUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
@Transactional
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    private  BorrowingRepository borrowingRepository;


//    public UserService(UserRepository userRepository, BorrowingRepository borrowingRepository) {
//        this.userRepository = userRepository;
//        this.borrowingRepository = borrowingRepository;
//    }


    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDto createUser(UserDto user) throws Exception {
        if(userRepository.findByEmail(user.getEmail()).isPresent())
            throw new Exception("Record already exists");

        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(user.getEmail());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setAddress(user.getAddress());
        userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        String publicUserId = JWTUtils.generateUserID(10);
        userEntity.setRole(user.getRole());
        UserEntity storedUserDetails = userRepository.save(userEntity);
        UserDto returnedValue = modelMapper.map(storedUserDetails,UserDto.class);
        String accessToken = JWTUtils.generateToken(userEntity.getEmail());
        returnedValue.setAccessToken(AppConstants.TOKEN_PREFIX + accessToken);
        return returnedValue;
    }


    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).get();
        if(userEntity == null) throw new UsernameNotFoundException("No record found");
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity,returnValue);
        return returnValue;
    }


    public UserDto getUserByUserId(Long userId) throws Exception {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(Exception::new);
        BeanUtils.copyProperties(userEntity,returnValue);
        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email).get();
        if(userEntity==null) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(),userEntity.getPassword(),
                true,true,true,true,new ArrayList<>());
    }



    public UserEntity getUserById(Long userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        return userOptional.orElse(null);
    }

    public List<BookEntity> getAllBorrowedBooksByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));

        List<BorrowingEntity> borrowingEntities = borrowingRepository.findByUser(user);

        List<BookEntity> books = borrowingEntities.stream()
                .map(BorrowingEntity::getBook)
                .collect(Collectors.toList());

        return books;
    }


    public List<BookEntity> getCurrentlyBorrowedBooksByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));

        List<BorrowingEntity> borrowingEntities = borrowingRepository.findByUser(user);

        List<BookEntity> books = new ArrayList<>();

        Date currentDate = new Date();
        for (BorrowingEntity borrowingEntity : borrowingEntities) {
            Date returnDate = borrowingEntity.getReturnDate();
            if (returnDate != null && returnDate.after(new Date(currentDate.getTime() - 5 * 24 * 60 * 60 * 1000))) {
                books.add(borrowingEntity.getBook());
            }
        }

        return books;
    }


}
