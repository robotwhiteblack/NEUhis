package com.antrain.his.service.impl;

import com.antrain.his.entity.Register;
import com.antrain.his.mapper.RegisterMapper;
import com.antrain.his.service.IRegisterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 诊疗信息 服务实现类
 * </p>
 *
 * @author antrain
 * @since 2020-08-26
 */
@Service
public class RegisterServiceImpl extends ServiceImpl<RegisterMapper, Register> implements IRegisterService {

}
