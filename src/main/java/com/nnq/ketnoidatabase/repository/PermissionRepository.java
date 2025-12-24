package com.nnq.ketnoidatabase.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nnq.ketnoidatabase.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String> {}
