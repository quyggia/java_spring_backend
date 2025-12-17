package com.nnq.ketnoidatabase.service;

import com.nnq.ketnoidatabase.dto.request.RoleCreationRequest;
import com.nnq.ketnoidatabase.dto.response.RoleResponse;
import com.nnq.ketnoidatabase.entity.Role;
import com.nnq.ketnoidatabase.mapper.RoleMapper;
import com.nnq.ketnoidatabase.repository.PermissionRepository;
import com.nnq.ketnoidatabase.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class RoleService {
    RoleRepository roleRepository;

    PermissionRepository permissionRepository;
    RoleMapper roleMapper;


    public RoleResponse createRole(RoleCreationRequest request){
            var role = roleMapper.toRole(request);

            var permission = permissionRepository.findAllById(request.getPermissions());

            role.setPermissions(new HashSet<>(permission));

            roleRepository.save(role);

            return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAllRole()
    {
        var roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    public void deleteRole(String role)
    {
        roleRepository.deleteById(role);
    }

}
