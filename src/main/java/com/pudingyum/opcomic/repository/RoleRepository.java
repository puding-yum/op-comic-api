package com.pudingyum.opcomic.repository;

import com.pudingyum.opcomic.constant.ERole;
import com.pudingyum.opcomic.domain.dao.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
