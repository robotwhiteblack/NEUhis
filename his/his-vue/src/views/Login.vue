<template>
  <div class="login-bg">
    <video class="bg-video" src="../assets/index.mp4" autoplay loop muted playsinline></video>
    <div class="art-text">
      <div class="main-title">Innovating for you</div>
      <div class="sub-title">创新&nbsp;&nbsp;|&nbsp;&nbsp;成就&nbsp;&nbsp;|&nbsp;&nbsp;价值</div>
      <div class="desc-title">集成AI解决方案的东软云医院</div>
    </div>
    <div class="login-center left-center">
      <div class="login-box glass-effect">
        <div class="avatar-box">
          <img src="../assets/logo.png" alt="logo">
        </div>
        <el-form ref="loginFormRef" :model="loginForm" :rules="loginFormRules" label-width="0px" class="login-form">
          <el-form-item prop="telephone">
            <el-input v-model="loginForm.telephone" prefix-icon="el-icon-phone" placeholder="请输入手机号" size="large"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="loginForm.password" prefix-icon="el-icon-lock" type="password" placeholder="请输入密码" size="large"></el-input>
          </el-form-item>
          <el-form-item class="btns">
            <el-button type="primary" size="large" @click="login">登录</el-button>
            <el-button type="info" size="large" @click="resetLoginForm">重置</el-button>
          </el-form-item>
        </el-form>
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
    return {
      loginForm: {
        telephone: '18905621669',
        password: 'admin'
      },
      loginFormRules: {
        telephone: [
          { required: true, message: '请输入11位手机号', trigger: 'blur' },
          { validator: checkMobile, trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入登录密码', trigger: 'blur' },
          { min: 4, max: 15, message: '长度在 4 到 15 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    resetLoginForm () {
      this.$refs.loginFormRef.resetFields()
    },
    login () {
      this.$refs.loginFormRef.validate(valid => {
        if (!valid) return
        this.$axios.post('/login', response => {
          if (response.status === 200) {
            window.sessionStorage.setItem('token', response.data.token)
            window.sessionStorage.setItem('id', response.data.id)
            window.sessionStorage.setItem('deptId', response.data.deptId)
            this.$router.push('/dashboard')
          } else {
            this.$message.error(response.message || '登录失败，请检查账号密码')
          }
        }, this.loginForm)
      })
    }
  }
}
</script>

<style lang="less" scoped>
.login-bg {
  position: fixed;
  left: 0; top: 0;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  z-index: 0;
}
.bg-video {
  position: absolute;
  left: 0; top: 0;
  width: 100vw;
  height: 100vh;
  object-fit: cover;
  z-index: -1;
}
.art-text {
  position: absolute;
  right: 18vw;
  top: 38vh;
  z-index: 2;
  text-align: center;
  color: #fff;
  user-select: none;
  pointer-events: none;
}
.main-title {
  font-family: 'Pacifico', 'Segoe Script', 'Comic Sans MS', cursive, sans-serif;
  font-size: 3.6rem;
  font-weight: 400;
  letter-spacing: 2px;
  text-shadow: 0 4px 24px #0008, 0 1px 0 #fff3;
  margin-bottom: 18px;
}
.sub-title {
  font-size: 1.8rem;
  letter-spacing: 6px;
  text-shadow: 0 2px 8px #0006;
  font-weight: 400;
}
.desc-title {
  font-size: 1.3rem;
  color: #eee;
  margin-top: 10px;
  letter-spacing: 2px;
  text-shadow: 0 2px 8px #0006;
}
.login-center {
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}
.left-center {
  justify-content: flex-start;
  padding-left: 8vw;
}
.module-select {
  position: absolute;
  right: 18vw;
  top: 38vh;
  display: flex;
  flex-direction: column;
  gap: 36px;
  z-index: 3;
}
.module-btn {
  font-size: 1.5rem;
  padding: 22px 60px;
  border-radius: 18px;
  font-weight: bold;
  letter-spacing: 2px;
  box-shadow: 0 4px 24px #0002;
}
.login-box {
  width: 320px;
  min-height: 340px;
  background: rgba(255,255,255,0.25);
  border-radius: 22px;
  box-shadow: 0 8px 32px 0 rgba(33, 203, 243, 0.18);
  position: relative;
  padding-top: 60px;
  display: flex;
  flex-direction: column;
  align-items: center;
  transition: box-shadow 0.2s, transform 0.2s;
  border: 1px solid rgba(255,255,255,0.3);
}
.glass-effect {
  backdrop-filter: blur(16px) saturate(180%);
  -webkit-backdrop-filter: blur(16px) saturate(180%);
  background: rgba(255,255,255,0.35);
  border-radius: 22px;
  border: 1px solid rgba(255,255,255,0.3);
}
.login-box:hover {
  box-shadow: 0 16px 48px 0 rgba(33, 203, 243, 0.28);
  transform: translateY(-4px) scale(1.02);
}
.avatar-box {
  height: 100px;
  width: 100px;
  border-radius: 50%;
  box-shadow: 0 2px 12px rgba(33,150,243,0.10);
  position: absolute;
  left: 50%;
  top: 0;
  transform: translate(-50%, -50%);
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
}
.avatar-box img {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: #e3f0ff;
}
.login-form {
  width: 100%;
  padding: 0 32px 24px 32px;
  box-sizing: border-box;
  margin-top: 40px;
}
.el-input__inner {
  background: rgba(255,255,255,0.18) !important;
  border-radius: 12px;
  font-size: 16px;
  padding: 12px 16px;
  box-shadow: 0 2px 8px #0001;
  border: 1.5px solid rgba(0,0,0,0.10);
  color: #222;
  transition: box-shadow 0.2s, border-color 0.2s, background 0.2s, color 0.2s;
}
.el-input__inner:focus {
  background: rgba(255,255,255,0.32) !important;
  color: #111;
  box-shadow: 0 4px 16px #0002;
  border-color: #222;
}
.btns {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 12px;
}
.el-button--primary, .el-button--info {
  border-radius: 18px;
  font-size: 16px;
  padding: 8px 32px;
  transition: background 0.2s, box-shadow 0.2s, color 0.2s;
  background: rgba(255,255,255,0.18);
  color: #222;
  border: 1.5px solid rgba(0,0,0,0.10);
  box-shadow: 0 2px 8px #0001;
}
.el-button--primary:hover, .el-button--info:hover {
  background: rgba(30,30,30,0.92);
  color: #fff;
  box-shadow: 0 8px 32px 0 #0005;
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
