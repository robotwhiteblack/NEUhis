<template>
  <el-breadcrumb separator="/" class="ai-breadcrumb">
    <slot></slot>
  </el-breadcrumb>
</template>

<script>
export default {
  props: ['msg1', 'msg2']
}
</script>

<style scoped>
.ai-breadcrumb {
  font-size: 15px;
  color: #2196f3;
  text-shadow: 0 2px 8px #2196f355;
  padding: 8px 0 18px 0;
  background: none;
  letter-spacing: 0.5px;
}
.ai-breadcrumb .el-breadcrumb__item a {
  color: #2196f3;
  font-weight: 500;
  transition: color 0.2s, text-shadow 0.2s;
  border-radius: 6px;
  padding: 2px 8px;
}
.ai-breadcrumb .el-breadcrumb__item a:hover {
  color: #21d4fd;
  text-shadow: 0 2px 8px #21d4fd55;
  background: #e3f0ff;
}
.ai-breadcrumb .el-breadcrumb__separator {
  color: #b0c4de;
  font-size: 16px;
  margin: 0 6px;
}
</style>
