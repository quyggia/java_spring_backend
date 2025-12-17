package com.nnq.ketnoidatabase.repository;

import com.nnq.ketnoidatabase.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {
}
