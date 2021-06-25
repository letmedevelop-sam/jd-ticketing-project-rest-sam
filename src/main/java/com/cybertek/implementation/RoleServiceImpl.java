package com.cybertek.implementation;

import com.cybertek.dto.RoleDTO;
import com.cybertek.entity.Role;
import com.cybertek.exception.TicketingProjectException;
import com.cybertek.repository.RoleRepository;
import com.cybertek.service.RoleService;
import com.cybertek.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {


    private RoleRepository roleRepository;
    private MapperUtil mapperUtil;

    //create constructor to inject


    public RoleServiceImpl(RoleRepository roleRepository, MapperUtil mapperUtil) {
        this.roleRepository = roleRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<RoleDTO> listAllRoles() {

        List<Role> list = roleRepository.findAll();

        //convert to DTO and return it // we need mappers for it
        //we can do it with setter
        //we can use ready methods //they will come from Role mapper

        return list.stream().map(obj->mapperUtil.convert(obj, new RoleDTO())).collect(Collectors.toList());
    }


    @Override
    public RoleDTO findById(Long id) throws TicketingProjectException {
        Role role = roleRepository.findById(id).orElseThrow(() -> new TicketingProjectException("Role does not exists"));
        return mapperUtil.convert(role,new RoleDTO());
    }
}
