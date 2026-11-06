package com.antrain.his.service.impl;

import com.antrain.his.entity.Role;
import com.antrain.his.mapper.RoleMapper;
import com.antrain.his.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author antrain
 * @since 2020-08-26
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
