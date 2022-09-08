package saksham.example.taskmanager.service;

import org.springframework.beans.PropertyValues;

import saksham.example.taskmanager.model.Role;

import java.util.List;

public interface RoleService {
    Role createRole(Role role);

    List<Role> findAll();
}
