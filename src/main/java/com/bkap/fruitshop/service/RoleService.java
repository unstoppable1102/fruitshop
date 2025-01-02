package com.bkap.fruitshop.service;

import com.bkap.fruitshop.dto.request.RoleRequest;
import com.bkap.fruitshop.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {

    List<RoleResponse> getAll();
    RoleResponse create(RoleRequest request);
    void delete(String role);

}
