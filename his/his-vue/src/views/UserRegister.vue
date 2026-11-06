<template>
  <div class="register-bg">
    <div class="register-center">
      <div class="register-box">
        <div class="register-title">用户注册</div>
        <el-form ref="registerFormRef" :model="registerForm" :rules="registerFormRules" label-width="0px" class="register-form">
          <el-form-item prop="telephone">
            <el-input v-model="registerForm.telephone" prefix-icon="el-icon-phone" placeholder="请输入手机号" size="large"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="registerForm.password" prefix-icon="el-icon-lock" type="password" placeholder="请输入密码" size="large"></el-input>
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input v-model="registerForm.confirmPassword" prefix-icon="el-icon-lock" type="password" placeholder="请确认密码" size="large"></el-input>
          </el-form-item>
          <el-form-item class="btns">
            <el-button type="primary" size="large" @click="register">注册</el-button>
            <el-button type="info" size="large" @click="resetRegisterForm">重置</el-button>
          </el-form-item>
        </el-form>
        <div class="login-link">
          已有账号？<el-button type="text" @click="goLogin">去登录</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    var checkMobile = (rule, value, cb) => {
      const regMobile = /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/
      if (regMobile.test(value)) return cb()
      cb(new Error('请输入合法的手机号'))
    }
    var checkConfirm = (rule, value, cb) => {
      if (value !== this.registerForm.password) {
        cb(new Error('两次输入密码不一致'))
      } else {
        cb()
      }
    }
    return {
      registerForm: {
        telephone: '',
        password: '',
        confirmPassword: ''
      },
      registerFormRules: {
        telephone: [
          { required: true, message: '请输入11位手机号', trigger: 'blur' },
          { validator: checkMobile, trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 4, max: 15, message: '长度在 4 到 15 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          { validator: checkConfirm, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    resetRegisterForm () {
      this.$refs.registerFormRef.resetFields()
    },
    register () {
      this.$refs.registerFormRef.validate(async valid => {
        if (!valid) return
        // 这里应调用后端注册接口，示例用setTimeout模拟
        setTimeout(() => {
          this.$message.success('注册成功，请登录！')
          this.$router.push('/login')
        }, 800)
      })
    },
    goLogin () {
      this.$router.push('/login')
    }
  }
}
</script>

<style lang="less" scoped>
.register-bg {
  position: fixed;
  left: 0; top: 0;
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #2196f3 0%, #21d4fd 100%);
  overflow: hidden;
  z-index: 0;
}
.register-center {
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}
.register-box {
  width: 420px;
  min-height: 340px;
  background: #fff;
  border-radius: 22px;
  box-shadow: 0 8px 32px 0 rgba(33, 203, 243, 0.18);
  position: relative;
  padding: 40px 0 20px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  transition: box-shadow 0.2s, transform 0.2s;
}
.register-title {
  font-size: 26px;
  font-weight: bold;
  color: #2196f3;
  margin-bottom: 24px;
}
.register-form {
  width: 100%;
  padding: 0 32px 24px 32px;
  box-sizing: border-box;
  margin-top: 0;
}
.btns {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 12px;
}
.login-link {
  margin-top: 16px;
  text-align: center;
  font-size: 15px;
}
</style>

<style>
html, body, #app {
  height: 100%;
  margin: 0;
  padding: 0;
  overflow: hidden;
}
</style>
