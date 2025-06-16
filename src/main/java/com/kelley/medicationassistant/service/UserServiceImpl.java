package com.kelley.medicationassistant.service;

import com.kelley.medicationassistant.exception.APIException;
import com.kelley.medicationassistant.model.User;
import com.kelley.medicationassistant.payload.SignUpRequest;
import com.kelley.medicationassistant.payload.UserResponse;
import com.kelley.medicationassistant.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserResponse signUp(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(signUpRequest.getPassword());
        user.setEmail(signUpRequest.getEmail());

        userRepository.save(user);

        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public UserResponse deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new APIException("User not found with username: " + username));

        userRepository.delete(user);

        return modelMapper.map(user, UserResponse.class);
    }
}
