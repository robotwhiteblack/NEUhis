<template>
  <el-container class="home-container">
    <!-- 头部区域 -->
    <el-header class="ai-header">
      <div class="ai-header-left">
        <img src="../assets/logo.png" alt />
        <span>东软云医院后台管理系统</span>
      </div>
      <el-button class="ai-logout-icon-btn" type="text" @click="logout" circle>
        <i class="el-icon-switch-button"></i>
      </el-button>
    </el-header>
    <!-- 页面主体区域 -->
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ? '64px' : '200px'" class="aside-animate ai-aside">
        <el-tooltip :content="isCollapse ? '展开侧边栏' : '收起侧边栏'" placement="right">
          <div class="toggle-button" @click="toggleCollapse">
            <i :class="isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'"/>
          </div>
        </el-tooltip>
        <!-- 侧边栏菜单区域 -->
        <Menu/>
      </el-aside>
      <!-- 右侧内容主体 -->
      <el-main class="ai-main">
        <!-- 路由占位符 -->
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Menu from '../components/Menu.vue'
import { mapState, mapActions } from 'vuex'
export default {
  components: {
    Menu
  },
  methods: {
    logout () {
      window.sessionStorage.clear()
      this.$router.push({ path: '/dashboard' })
    }, // 退出登录
    ...mapActions([
      'toggleCollapse'
    ])
  },
  computed: {
    ...mapState({
      isCollapse: 'isCollapse'
    })
  }
}
</script>

<style scoped>
.home-container {
  height: 100%;
  background: var(--main-bg);
}
.ai-header {
  background: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 12px rgba(33,150,243,0.06);
  padding: 0 32px;
  min-height: 64px;
}
.ai-header-left {
  display: flex;
  align-items: center;
}
.ai-header-left img {
  height: 40px;
  width: 40px;
  margin-right: 18px;
}
.ai-header-left span {
  font-size: 22px;
  font-weight: 600;
  color: #2196f3;
  letter-spacing: 1.5px;
}
.ai-logout-btn {
  border-radius: 20px;
  font-size: 15px;
  padding: 8px 24px;
  background: linear-gradient(90deg, #2196f3 0%, #21d4fd 100%);
  color: #fff;
  border: none;
  box-shadow: 0 2px 8px #2196f355;
  transition: background 0.2s, box-shadow 0.2s;
}
.ai-logout-btn:hover {
  background: linear-gradient(90deg, #21d4fd 0%, #2196f3 100%);
  box-shadow: 0 4px 16px #21d4fd55;
}
.ai-aside {
  background: #fff;
  border-right: 1.5px solid #e3f0ff;
  box-shadow: 2px 0 12px 0 rgba(33,150,243,0.04);
  min-height: calc(100vh - 64px);
}
.ai-main {
  background: #f4f7fa;
  border-radius: 24px 24px 0 0;
  margin: 18px 18px 0 18px;
  min-height: calc(100vh - 100px);
  box-shadow: 0 4px 24px rgba(33,150,243,0.08);
  padding: 24px 32px 32px 32px;
  overflow: auto;
}
.el-aside {
  background: linear-gradient(135deg, #e3f0ff 0%, #f8fbff 100%);
  transition: width 0.3s cubic-bezier(.4,0,.2,1);
}
.el-main {
  background-color: #eaedf1;
}
.aside-animate {
  transition: width 0.3s cubic-bezier(.4,0,.2,1);
}
.toggle-button {
  background-color: #4a5064;
  font-size: 18px;
  line-height: 36px;
  color: #fff;
  text-align: center;
  letter-spacing: 0.2em;
  cursor: pointer;
  border-radius: 8px;
  margin: 8px 0;
  transition: background 0.2s, transform 0.3s;
  box-shadow: 0 2px 8px #2196f355;
}
.toggle-button:hover {
  background: linear-gradient(90deg, #2196f3 0%, #21d4fd 100%);
  transform: scale(1.08) rotate(-6deg);
}
.ai-logout-icon-btn {
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
.ai-logout-icon-btn:hover {
  color: #e53935;
  background: #fff;
}
</style>
