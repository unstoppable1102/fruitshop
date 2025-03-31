package com.bkap.fruitshop.service.impl;

import com.bkap.fruitshop.dto.request.RoleRequest;
import com.bkap.fruitshop.dto.response.RoleResponse;
import com.bkap.fruitshop.dto.response.UserResponse;
import com.bkap.fruitshop.entity.Role;
import com.bkap.fruitshop.exception.AppException;
import com.bkap.fruitshop.exception.ErrorCode;
import com.bkap.fruitshop.repository.RoleRepository;
import com.bkap.fruitshop.repository.UserRepository;
import com.bkap.fruitshop.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

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
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(role -> {
            RoleResponse roleResponse = modelMapper.map(role, RoleResponse.class);

            // Chuyển đổi danh sách User thành UserResponse và gán roleNames
            Set<UserResponse> userResponses = role.getUsers().stream().map(user -> {
                UserResponse userResponse = modelMapper.map(user, UserResponse.class);
                userResponse.setRoles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())); // Gán danh sách roleNames
                return userResponse;
            }).collect(Collectors.toSet());

            roleResponse.setUsers(userResponses); // Gán danh sách UserResponse vào RoleResponse
            return roleResponse;
        }).collect(Collectors.toList());
    }


    @Override
    public RoleResponse create(RoleRequest request) {
        if (roleRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }

        Role role = modelMapper.map(request, Role.class);
        roleRepository.save(role);
        return modelMapper.map(role, RoleResponse.class);
    }

    @Override
    public void delete(String roleName) {
        Role existingRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        if (userRepository.existsByRoles_Id(existingRole.getId())) {
            throw new AppException(ErrorCode.USER_EXIST_IN_ROLE);
        }
        roleRepository.delete(existingRole);
    }
}
