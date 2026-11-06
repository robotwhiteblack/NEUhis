<template>
  <div class="home-bg">
    <video class="bg-video" src="../assets/index.mp4" autoplay loop muted playsinline></video>
    <el-button class="logout-btn" type="text" @click="handleLogout" circle>
      <i class="el-icon-switch-button"></i>
    </el-button>
    <div class="art-text">
      <div class="main-title">Innovating for you</div>
      <div class="sub-title">创新&nbsp;&nbsp;|&nbsp;&nbsp;成就&nbsp;&nbsp;|&nbsp;&nbsp;价值</div>
      <div class="desc-title">集成AI解决方案的东软云医院</div>
    </div>
    <div class="module-card-group">
      <div class="module-card" @click="goHome">
        <i class="el-icon-s-home module-card-icon" />
        <div class="module-card-title">医院管理系统</div>
        <div class="module-card-desc">一站式医院业务管理</div>
      </div>
      <div class="module-card" @click="goAIAssistant">
        <i class="el-icon-cpu module-card-icon" />
        <div class="module-card-title">AI医疗助手</div>
        <div class="module-card-desc">智能问答与辅助决策</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    goHome () {
      this.$router.push('/home')
    },
    goAIAssistant () {
      this.$router.push('/ai-assistant')
    },
    handleLogout () {
      this.$router.push('/login')
    }
  }
}
</script>

<style lang="less" scoped>
.home-bg {
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
.module-card-group {
  position: absolute;
  left: 8vw;
  top: 34vh;
  display: flex;
  flex-direction: row;
  gap: 48px;
  z-index: 3;
}
.module-card {
  width: 160px;
  height: 160px;
  background: rgba(255,255,255,0.10);
  border-radius: 18px;
  box-shadow: 0 4px 24px #0002;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background 0.25s, color 0.25s, box-shadow 0.25s, transform 0.18s;
  border: 1.5px solid rgba(0,0,0,0.10);
  color: #222;
}
.module-card:hover {
  background: rgba(30,30,30,0.92);
  color: #fff;
  box-shadow: 0 8px 32px 0 #0005;
  transform: translateY(-6px) scale(1.04);
}
.module-card-icon {
  font-size: 3.2rem;
  margin-bottom: 12px;
  color: inherit;
}
.module-card-title {
  font-size: 1.1rem;
  font-weight: bold;
  margin-bottom: 6px;
  letter-spacing: 1.5px;
  color: inherit;
}
.module-card-desc {
  font-size: 0.95rem;
  color: #888;
  margin-top: 2px;
  letter-spacing: 1px;
  color: inherit;
  opacity: 0.7;
}
.logout-btn {
  position: absolute;
  top: 10px;
  right: 14px;
  background: transparent;
  color: #b0b8c9;
  border: none;
  box-shadow: none;
  font-size: 20px;
  transition: color 0.2s, background 0.2s;
  z-index: 10;
}
.logout-btn:hover {
  color: #e53935;
  background: #fff;
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
