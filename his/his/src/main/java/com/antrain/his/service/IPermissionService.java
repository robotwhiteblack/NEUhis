package com.antrain.his.service;

import com.antrain.his.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author antrain
 * @since 2020-08-26
 */
public interface IPermissionService extends IService<Permission> {
  Object listByTree();
  Object userPermissionList(int id);
}
