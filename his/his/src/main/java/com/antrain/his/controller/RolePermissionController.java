package com.antrain.his.controller;

import javax.annotation.Resource;

import com.antrain.his.entity.RolePermission;
import com.antrain.his.service.IRolePermissionService;
import com.antrain.his.utils.Result;
import com.antrain.his.utils.ResultGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 角色权限 前端控制器
 * </p>
 *
 * @author antrain
 * @since 2020-08-26
 */
@RestController
@RequestMapping("/role-permission")
public class RolePermissionController {

  @Resource
  private IRolePermissionService roles;

  @GetMapping("/{id}")
  public Result getRightsById(@PathVariable int id) {
    QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
    wrapper.eq("role_id", id);
    RolePermission role = roles.getOne(wrapper);
    if (role == null) {
      return ResultGenerator.getFailResult("该角色尚未分配权限");
    }
    return ResultGenerator.getSuccessResult(role);
  }

  @PostMapping()
  public Result add(@RequestBody RolePermission entity) {
    if (roles.save(entity))
      return ResultGenerator.getSuccessResult("", "分配权限成功！");
    return ResultGenerator.getFailResult("", "分配权限失败");
  }

  @PutMapping("/{id}")
  public Result add(@RequestBody RolePermission entity,@PathVariable int id) {
    entity.setId(id);
    if (roles.updateById(entity))
      return ResultGenerator.getSuccessResult("", "修改分配权限成功！");
    return ResultGenerator.getFailResult("", "修改分配权限失败");
  }

}
