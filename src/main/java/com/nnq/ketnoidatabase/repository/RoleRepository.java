package com.nnq.ketnoidatabase.repository;


import com.nnq.ketnoidatabase.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    void deleteRoleByName(String name);

    //List<Role> findByNameIn(List<String> names);
}
