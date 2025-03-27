package com.bkap.fruitshop.service.impl;

import com.bkap.fruitshop.dto.request.UserRequest;
import com.bkap.fruitshop.dto.request.UserUpdateInforRequest;
import com.bkap.fruitshop.dto.response.PageResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        // Chuyển đổi từ User entity sang UserResponse
        return modelMapper.map(savedUser, UserResponse.class);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponse> responses = userPage.getContent()
                .stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .toList();

        return new PageResponse<>(userPage.getNumber(), userPage.getSize(),
                userPage.getTotalElements(), userPage.getTotalPages(), userPage.isLast(), responses);
    }

    @Override
    @PreAuthorize("authentication.name == @userServiceImpl.findUsernameById(#id) || hasRole('ADMIN')")
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        if (!currentUsername.equals(user.getUsername()) && !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
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
    @PreAuthorize("authentication.name != null && authentication.name == @userServiceImpl.findUsernameById(#id) || hasRole('ADMIN')")
    public UserResponse updateUser(Long id, UserUpdateInforRequest request) {
        // Tìm người dùng theo id
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Cập nhật thông tin từ request lên đối tượng user
        if (request.getFullName() != null){
            user.setFullName(request.getFullName());
        }
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())){
            if (userRepository.existsByEmail(request.getEmail())){
                throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTED);
            }
            user.setEmail(request.getEmail());
        }
        if (request.getPhone() != null){
            user.setPhone(request.getPhone());
        }
        if (request.getBirthday() != null){
            user.setBirthday(request.getBirthday());
        }

        // Lưu thay đổi vào database
        User updatedUser = userRepository.save(user);

        // Trả về kết quả dưới dạng UserResponse
        return modelMapper.map(updatedUser, UserResponse.class);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long id) {
        // Kiểm tra xem người dùng có tồn tại không
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));

        if (isAdmin) {
            throw new AppException(ErrorCode.CANNOT_DELETE_ADMIN);
        }
        // Xóa người dùng khỏi database
        userRepository.delete(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public UserResponse updateUserRoles(Long userId, Set<String> roleNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        // get roles
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName.trim())
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
            roles.add(role);
        }

        user.setRoles(roles);

        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserResponse.class);
    }

    //TODO
    @Override
    public void forgotPassword(String email) {

    }

    //TODO
    @Override
    public void resetPassword(String token, String newPassword) {

    }

    @Override
    @PreAuthorize("returnObject.username == authentication.name || hasRole('ADMIN')")
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        //check oldPassword
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_OLD_PASSWORD);
        }

        //update newPassword
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public String findUsernameById(Long id) {
        return userRepository.findById(id)
                .map(User::getUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }


}
