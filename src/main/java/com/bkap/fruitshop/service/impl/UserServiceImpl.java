package com.bkap.fruitshop.service.impl;

import com.bkap.fruitshop.common.util.Const;
import com.bkap.fruitshop.common.util.UploadFileUtil;
import com.bkap.fruitshop.dto.request.ResetPasswordRequest;
import com.bkap.fruitshop.dto.request.RoleNameRequest;
import com.bkap.fruitshop.dto.request.UserRequest;
import com.bkap.fruitshop.dto.request.UserUpdateInforRequest;
import com.bkap.fruitshop.dto.response.PageResponse;
import com.bkap.fruitshop.dto.response.UserResponse;
import com.bkap.fruitshop.entity.PasswordResetToken;
import com.bkap.fruitshop.entity.Role;
import com.bkap.fruitshop.entity.User;
import com.bkap.fruitshop.exception.AppException;
import com.bkap.fruitshop.exception.ErrorCode;
import com.bkap.fruitshop.mapper.UserMapper;
import com.bkap.fruitshop.repository.PasswordResetTokenRepository;
import com.bkap.fruitshop.repository.RoleRepository;
import com.bkap.fruitshop.repository.UserRepository;
import com.bkap.fruitshop.service.EmailService;
import com.bkap.fruitshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final UploadFileUtil uploadFileUtil;
    private final UserMapper userMapper;

    @Value("${app.reset-password-url}")
    private String resetBaseUrl;


    @Override
    @Transactional
    public UserResponse save(UserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTED);
        }

        //set Role default is USER
        Role userRole = roleRepository.findByName(Const.USER)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .avatar("user.png")
                .status(true)
                .roles(Set.of(userRole))
                .build();

        // Chuyển đổi từ User entity sang UserResponse
        return modelMapper.map(userRepository.save(user), UserResponse.class);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<UserResponse> getAllUsers(String keyword, Pageable pageable) {

        Page<User> userPage = userRepository.findUsersWithRoles(keyword, pageable);

        List<UserResponse> responses = userPage.getContent()
                .stream()
                .map(this::toUserResponse)
                .toList();

        return PageResponse.<UserResponse>builder()
                .pageSize(userPage.getNumber())
                .pageSize(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .isLast(userPage.isLast())
                .content(responses)
                .build();
    }

    @Override
    @PostAuthorize("returnObject.username == authentication.name || hasRole('ADMIN')")
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toResponse(user);

    }

    @Override
    public UserResponse getMyInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toResponse(user);
    }

    @Override
    @PreAuthorize("authentication.name != null && authentication.name == @userServiceImpl.findUsernameById(#id) || hasRole('ADMIN')")
    public UserResponse updateUser(Long id, UserUpdateInforRequest request) {

        // Tìm người dùng theo id
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())){
            if (userRepository.existsByEmail(request.getEmail())){
                throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTED);
            }
            user.setEmail(request.getEmail());
        }

        Optional.ofNullable(request.getFullName()).ifPresent(user::setFullName);
        Optional.ofNullable(request.getPhone()).ifPresent(user::setPhone);
        Optional.ofNullable(request.getAddress()).ifPresent(user::setAddress);
        Optional.ofNullable(request.getBirthday()).ifPresent(user::setBirthday);

        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long id) {

        // Kiểm tra xem người dùng có tồn tại không
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> Const.ADMIN.equalsIgnoreCase(role.getName()));

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
    public UserResponse updateUserRoles(Long userId, RoleNameRequest roleNames) {

        if (roleNames == null || roleNames.getRoleNames().isEmpty()) {
            throw new AppException(ErrorCode.ROLE_INVALID);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // get roles
        Set<Role> roles = roleNames.getRoleNames().stream()
                .filter(name -> name != null && !name.trim().isEmpty())
                .map(name -> roleRepository.findByName(name.trim())
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)))
                .collect(Collectors.toSet());

        if (roles.isEmpty()) throw new AppException(ErrorCode.ROLE_INVALID);
        user.setRoles(roles);

        return userMapper.toResponse(userRepository.save(user));
    }

    //TODO
    @Override
    public void forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Xóa token cũ nếu có
        passwordResetTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(1))
                .build();
        passwordResetTokenRepository.save(resetToken);

        String resetLink = resetBaseUrl + "/reset-password?token=" + token;
        emailService.sendEmail(
                user.getEmail(),
                "Password Reset Request",
                "Click vào đường link sau để đặt lại mật khẩu: " + resetLink
        );
    }

    //TODO
    @Override
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }

        User user = resetToken.getUser();

        //Encrypt new password
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        // Xóa token sau khi đặt lại mật khẩu thành công
        passwordResetTokenRepository.delete(resetToken);
    }

    @Override
    @PreAuthorize("authentication.name != null && authentication.name == @userServiceImpl.findUsernameById(#userId)")
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

    @Override
    public UserResponse toggleUserStatus(Long userId, boolean status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setStatus(status);
        userRepository.save(user);
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public UserResponse updateUserAvatar(Long userId, MultipartFile avatarFile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarPath = uploadFileUtil.saveImage(avatarFile);
            user.setAvatar(avatarPath);
        }
        userRepository.save(user);
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public long countUsers() {
        return userRepository.count();
    }

    @Override
    public boolean verifyUserAccount(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setStatus(true);
        user.setVerificationToken(null);
        userRepository.save(user);
        return true;
    }

    private UserResponse toUserResponse(User user) {
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        userResponse.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        return userResponse;
    }
}
