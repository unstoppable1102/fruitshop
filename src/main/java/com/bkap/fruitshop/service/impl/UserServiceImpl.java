package com.bkap.fruitshop.service.impl;

import com.bkap.fruitshop.dto.request.UserRequest;
import com.bkap.fruitshop.dto.response.UserResponse;
import com.bkap.fruitshop.entity.Role;
import com.bkap.fruitshop.entity.User;
import com.bkap.fruitshop.exception.AppException;
import com.bkap.fruitshop.exception.ErrorCode;
import com.bkap.fruitshop.repository.RoleRepository;
import com.bkap.fruitshop.repository.UserRepository;
import com.bkap.fruitshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserResponse save(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        // Chuyển đổi từ UserRequestDto sang User entity
        User user = modelMapper.map(request, User.class);

        //set Role default is USER
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        user.setRoles(Set.of(userRole));
        user.setStatus(true);
        // Lưu người dùng vào database
        user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        // Chuyển đổi từ User entity sang UserResponse
        return modelMapper.map(savedUser, UserResponse.class);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("returnObject.username == authentication.name")
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
       User byUsername = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return modelMapper.map(byUsername, UserResponse.class);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        // Tìm người dùng theo id
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Cập nhật thông tin từ request lên đối tượng user
        modelMapper.map(request, user);

        user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));

        // Lưu thay đổi vào database
        User updatedUser = userRepository.save(user);

        // Trả về kết quả dưới dạng UserResponse
        return modelMapper.map(updatedUser, UserResponse.class);
    }

    @Override
    public void deleteUser(Long id) {
        // Kiểm tra xem người dùng có tồn tại không
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Xóa người dùng khỏi database
        userRepository.delete(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }
}
