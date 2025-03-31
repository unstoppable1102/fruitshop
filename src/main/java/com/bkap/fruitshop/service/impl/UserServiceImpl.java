package com.bkap.fruitshop.service.impl;

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
import com.bkap.fruitshop.repository.PasswordResetTokenRepository;
import com.bkap.fruitshop.repository.RoleRepository;
import com.bkap.fruitshop.repository.UserRepository;
import com.bkap.fruitshop.service.EmailService;
import com.bkap.fruitshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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
    public PageResponse<UserResponse> getAllUsers(String keyword, Pageable pageable) {
        Page<User> userPage;
        if (keyword != null && !keyword.isEmpty()) {
            userPage = userRepository.findUsersByUsernameContainingIgnoreCase(keyword, pageable);
        }else {
            userPage = userRepository.findAll(pageable);
        }

        List<UserResponse> responses = userPage.getContent()
                .stream()
                .map(user ->{
                    UserResponse userResponse = modelMapper.map(user, UserResponse.class);
                    userResponse.setRoles(user.getRoles().stream()
                            .map(Role::getName)
                            .collect(Collectors.toSet())); // Thêm danh sách roleNames vào UserResponse
                    return userResponse;
                }).toList();

        return new PageResponse<>(userPage.getNumber(), userPage.getSize(),
                userPage.getTotalElements(), userPage.getTotalPages(), userPage.isLast(), responses);
    }

    @Override
    @PreAuthorize("authentication.name == @userServiceImpl.findUsernameById(#id) || hasRole('ADMIN')")
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        // Thêm danh sách roles vào response
        userResponse.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        return userResponse;

    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
       User byUsername = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        UserResponse userResponse = modelMapper.map(byUsername, UserResponse.class);
        // Thêm danh sách roles vào response
        userResponse.setRoles(byUsername.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        return userResponse;
    }

    @Override
    @PreAuthorize("authentication.name != null && authentication.name == @userServiceImpl.findUsernameById(#id) || hasRole('ADMIN')")
    public UserResponse updateUser(Long id, UserUpdateInforRequest request) {
        // Tìm người dùng theo id
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        // Cập nhật thông tin từ request lên đối tượng user
        user.setFullName(request.getFullName() != null ? request.getFullName() : user.getFullName());
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())){
            if (userRepository.existsByEmail(request.getEmail())){
                throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTED);
            }
            user.setEmail(request.getEmail());
        }
        user.setPhone(request.getPhone() != null ? request.getPhone() : user.getPhone());
        user.setAddress(request.getAddress() != null ? request.getAddress() : user.getAddress());
        user.setBirthday(request.getBirthday() != null ? request.getBirthday() : user.getBirthday());

        // Lưu thay đổi vào database
        User updatedUser = userRepository.save(user);

        // Chuyển đổi thành UserResponse và thêm danh sách roles
        UserResponse userResponse = modelMapper.map(updatedUser, UserResponse.class);
        userResponse.setRoles(updatedUser.getRoles().stream()
                .map(Role::getName) // Chuyển Role Enum thành String
                .collect(Collectors.toSet()));

        return userResponse;
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
    public UserResponse updateUserRoles(Long userId, RoleNameRequest roleNames) {
        if (roleNames == null && roleNames.getRoleNames().isEmpty()) {
            throw new AppException(ErrorCode.ROLE_INVALID);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        // get roles
        Set<Role> roles = new HashSet<>();
            for (String roleName : roleNames.getRoleNames()) {
                if (roleName == null || roleName.trim().isEmpty()) {
                    throw new AppException(ErrorCode.ROLE_INVALID);
                }
                Role role = roleRepository.findByName(roleName.trim())
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
                roles.add(role);
            }
        user.setRoles(roles);

        User updatedUser = userRepository.save(user);
        // Chuyển đổi User -> UserResponse
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);

        // Thêm danh sách roleNames vào response
        userResponse.setRoles(roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));

        return userResponse;
    }

    //TODO
    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        passwordResetTokenRepository.save(resetToken);

        // Gửi email cho user
        String resetLink = "https://your-app.com/reset-password?token=" + token;
        emailService.sendEmail(user.getEmail(), "Password Reset Request",
                "Click vào đường link sau để đặt lại mật khẩu: " + resetLink);
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
}
