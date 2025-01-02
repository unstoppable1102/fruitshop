package com.bkap.fruitshop.service.impl;

import com.bkap.fruitshop.dto.request.RoleRequest;
import com.bkap.fruitshop.dto.response.RoleResponse;
import com.bkap.fruitshop.entity.Role;
import com.bkap.fruitshop.exception.AppException;
import com.bkap.fruitshop.exception.ErrorCode;
import com.bkap.fruitshop.repository.RoleRepository;
import com.bkap.fruitshop.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<RoleResponse> getAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map((role) -> modelMapper.map(role, RoleResponse.class))
                .collect(Collectors.toList());
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
    public void delete(String role) {
        Role existingRole = roleRepository.findByName(role)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        roleRepository.delete(existingRole);
    }
}
