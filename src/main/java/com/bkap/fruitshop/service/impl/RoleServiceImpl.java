package com.bkap.fruitshop.service.impl;

import com.bkap.fruitshop.dto.request.RoleRequest;
import com.bkap.fruitshop.dto.response.RoleResponse;
import com.bkap.fruitshop.dto.response.UserResponse;
import com.bkap.fruitshop.entity.Role;
import com.bkap.fruitshop.entity.User;
import com.bkap.fruitshop.exception.AppException;
import com.bkap.fruitshop.exception.ErrorCode;
import com.bkap.fruitshop.repository.RoleRepository;
import com.bkap.fruitshop.repository.UserRepository;
import com.bkap.fruitshop.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleResponse> getAll() {
        return roleRepository.findAllWithUsers()
                .stream()
                .map(this::toRoleResponse)
                .toList();
    }


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public RoleResponse create(RoleRequest request) {
        if (roleRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }

        Role role = modelMapper.map(request, Role.class);
        roleRepository.save(role);
        return modelMapper.map(role, RoleResponse.class);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String roleName) {
        Role existingRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        if (userRepository.existsByRoles_Id(existingRole.getId())) {
            throw new AppException(ErrorCode.USER_EXIST_IN_ROLE);
        }

        roleRepository.delete(existingRole);
    }

    private RoleResponse toRoleResponse(Role role) {
        RoleResponse roleResponse = modelMapper.map(role, RoleResponse.class);
        roleResponse.setUsers(role.getUsers().stream()
                .map(this::toUserResponse)
                .collect(Collectors.toSet()));
        return roleResponse;
    }

    private UserResponse toUserResponse(User user) {
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        userResponse.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        return userResponse;
    }
}
