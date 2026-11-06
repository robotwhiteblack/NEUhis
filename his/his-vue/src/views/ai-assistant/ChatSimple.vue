<template>
  <div class="chat-outer">
    <div class="header">
      <div class="logo">🧠</div>
      <div class="title">AI医疗助手</div>
      <el-button class="logout-btn" type="text" @click="handleLogout" circle>
        <i class="el-icon-switch-button"></i>
      </el-button>
    </div>
    <div class="model-select-card">
      <select v-model="selectedModel" class="model-select">
        <option disabled value="">Select a model</option>
        <option value="qwen-turbo">qwen-turbo（文本）</option>
        <option value="qwen-plus">qwen-plus（文本）</option>
        <option value="qwen-max">qwen-max（文本）</option>
        <option value="qwen-vl-plus">qwen-vl-plus（多模态，支持图片）</option>
        <option value="qwen-vl-max">qwen-vl-max（多模态，支持图片）</option>
      </select>
    </div>
    <div class="messages-scroll-area">
      <div class="chat-container">
        <div class="messages-container">
          <div
            v-for="(message, index) in messages"
            :key="index"
            :class="['message', message.role]"
          >
            <div class="message-bubble">
              <div v-if="message.image_url" class="message-image-container">
                <img
                  :src="message.image_url"
                  alt="用户上传的图片"
                  class="message-image"
                  @click="openImage(message.image_url)"
                />
              </div>
              <div v-if="message.role === 'assistant'" class="markdown-content" v-html="renderMarkdown(message.content)"></div>
              <div v-else>{{ message.content }}</div>
            </div>
            <div class="message-info">
              <span v-if="message.role === 'user'">您</span>
              <span v-else-if="message.role === 'system'">系统消息</span>
              <span v-else>AI医疗助手</span>
              ·
              {{ formatTime(message.timestamp) }}
            </div>
          </div>
          <div v-if="isLoading" class="message assistant">
            <div class="message-info">AI医疗助手 · 正在思考...</div>
          </div>
          <div ref="messagesEndRef" />
        </div>
        <div v-if="previewImage" class="image-preview-container">
          <img :src="previewImage" alt="预览" class="image-preview" />
          <button class="clear-image-button" @click="clearSelectedImage">×</button>
        </div>
      </div>
    </div>
    <div class="input-container">
      <input
        type="file"
        accept="image/*"
        @change="handleImageSelect"
        style="display: none"
        ref="fileInputRef"
      />
      <button class="image-upload-button plus-btn" @click="triggerImageUpload" :disabled="isLoading" style="width: 44px; height: 44px; font-size: 1.5rem; padding: 0;">
        +
      </button>
      <input
        type="text"
        v-model="input"
        :disabled="isLoading"
        :class="{ 'with-image': selectedImage }"
        class="chat-input"
        placeholder="请输入您的健康问题..."
        @keydown.enter.exact.prevent="handleSendMessage"
        @keydown.shift.enter="insertNewline"
        style="flex: 1; min-height: 44px; max-height: 200px; font-size: 15px; border-radius: 22px; border: 1.5px solid #e3e6eb; background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.04); padding: 0 20px; margin: 0 10px;"
      />
      <button
        v-if="!isLoading || selectedImage"
        class="send-button circle"
        @click="handleSendMessage"
        :disabled="isLoading || (!input.trim() && !selectedImage)"
        style="width: 44px; height: 44px; border-radius: 50%; background: #222; color: #fff; display: flex; align-items: center; justify-content: center; font-size: 1.3rem; min-width: 0; margin-left: 0; box-shadow: 0 2px 8px rgba(0,0,0,0.08); border: 1.5px solid #222;"
      >
        <svg width="22" height="22" viewBox="0 0 22 22" fill="none" xmlns="http://www.w3.org/2000/svg"><circle cx="11" cy="11" r="11" fill="none"/><path d="M6 12l5-5 5 5" stroke="#fff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>
      </button>
      <button
        v-else
        class="send-button circle stop-btn"
        @click="handleStopStream"
        style="width: 44px; height: 44px; border-radius: 50%; background: #e53935; color: #fff; display: flex; align-items: center; justify-content: center; font-size: 1.3rem; min-width: 0; margin-left: 0; box-shadow: 0 2px 8px rgba(0,0,0,0.08); border: 1.5px solid #e53935;"
      >
        <svg width="22" height="22" viewBox="0 0 22 22" fill="none" xmlns="http://www.w3.org/2000/svg"><circle cx="11" cy="11" r="11" fill="none"/><rect x="7" y="7" width="8" height="8" rx="2" fill="#fff"/></svg>
      </button>
    </div>
    <div class="input-help-text">按 Enter 发送，Shift + Enter 换行</div>
  </div>
</template>

<script>
import { aiAxios } from '@/utils/axiosUtil'
import MarkdownIt from 'markdown-it'
import DOMPurify from 'dompurify'
import markdownItTaskLists from 'markdown-it-task-lists'
import markdownItFootnote from 'markdown-it-footnote'
import markdownItMark from 'markdown-it-mark'

export default {
  name: 'ChatSimple',
  data () {
    return {
      messages: [],
      input: '',
      isLoading: false,
      selectedImage: null,
      previewImage: null,
      fileInputRef: null,
      conversationId: '',
      selectedModel: 'qwen-turbo', // 默认选中
      eventSource: null // 新增：用于保存EventSource对象
    }
  },
  mounted () {
    this.fileInputRef = this.$refs.fileInputRef
    this.initConversationId()
    this.scrollToBottom()
    this.loadHistory()
  },
  updated () {
    this.scrollToBottom()
  },
  methods: {
    // 初始化/获取会话ID
    initConversationId () {
      let cid = sessionStorage.getItem('medicalAssistantConversationId')
      if (!cid) {
        cid = Date.now().toString() + Math.random().toString(36).slice(2)
        sessionStorage.setItem('medicalAssistantConversationId', cid)
      }
      this.conversationId = cid
    },
    // 保存会话ID
    saveConversationId (cid) {
      if (cid) {
        sessionStorage.setItem('medicalAssistantConversationId', cid)
        this.conversationId = cid
      }
    },
    renderMarkdown (content) {
      const md = new MarkdownIt({
        html: false,
        linkify: true,
        breaks: true
      })
        .use(markdownItTaskLists)
        .use(markdownItFootnote)
        .use(markdownItMark)
      return DOMPurify.sanitize(md.render(content || ''))
    },
    formatTime (ts) {
      if (!ts) return ''
      const date = new Date(ts)
      return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    },
    async handleSendMessage () {
      if (this.isLoading || (!this.input.trim() && !this.selectedImage)) return
      const userMsg = { role: 'user', content: this.input, timestamp: new Date().toISOString() }
      if (this.selectedImage) userMsg.image_url = this.previewImage
      this.messages.push(userMsg)
      this.isLoading = true
      this.input = ''
      // 发送图片+文本
      if (this.selectedImage) {
        const formData = new FormData()
        formData.append('message', userMsg.content)
        formData.append('file', this.selectedImage)
        formData.append('model', this.selectedModel)
        try {
          const res = await aiAxios.post(`/api/chat/multimodal?conversation_id=${this.conversationId}`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
          })
          if (res.data && res.data.conversation_id) {
            this.saveConversationId(res.data.conversation_id)
          }
          this.messages.push({ role: 'assistant', content: res.data.response || 'AI暂无回复', timestamp: new Date().toISOString() })
        } catch (e) {
          this.messages.push({ role: 'system', content: 'AI助手服务异常，请稍后再试。', timestamp: new Date().toISOString() })
        }
        this.clearSelectedImage()
        this.isLoading = false
        return
      }
      // 流式输出
      const aiMsg = { role: 'assistant', content: '', timestamp: new Date().toISOString() }
      this.messages.push(aiMsg)
      // 构造流式请求URL，带conversation_id和model
      const url = `${aiAxios.defaults.baseURL}/api/chat/stream?conversation_id=${this.conversationId}&message=${encodeURIComponent(userMsg.content)}&model=${this.selectedModel}`
      try {
        this.eventSource = new window.EventSource(url)
        this.eventSource.onmessage = (e) => {
          if (e.data && e.data !== '[DONE]') {
            aiMsg.content += e.data
          }
        }
        this.eventSource.onerror = (err) => {
          console.error('EventSource error:', err)
          this.eventSource.close()
          this.isLoading = false
        }
        this.eventSource.onopen = () => {}
        this.eventSource.addEventListener('done', (evt) => {
          // 解析done事件，提取conversation_id
          this.isLoading = false
          this.eventSource && this.eventSource.close()
        })
      } catch (e) {
        this.isLoading = false
      }
    },
    handleStopStream () {
      if (this.eventSource) {
        this.eventSource.close()
        this.eventSource = null
      }
      this.isLoading = false
    },
    handleImageSelect (e) {
      const files = e.target.files
      if (files && files.length > 0) {
        const file = files[0]
        if (file.size > 5 * 1024 * 1024) {
          alert('图片大小不能超过5MB')
          return
        }
        if (!file.type.startsWith('image/')) {
          alert('请选择图片文件')
          return
        }
        this.selectedImage = file
        this.previewImage = URL.createObjectURL(file)
      }
    },
    clearSelectedImage () {
      this.selectedImage = null
      this.previewImage = null
      if (this.fileInputRef) this.fileInputRef.value = ''
    },
    triggerImageUpload () {
      if (this.fileInputRef) this.fileInputRef.click()
    },
    insertNewline (e) {
      // Shift+Enter换行
      const input = e.target
      const start = input.selectionStart
      const end = input.selectionEnd
      this.input = this.input.slice(0, start) + '\n' + this.input.slice(end)
      this.$nextTick(() => {
        input.selectionStart = input.selectionEnd = start + 1
      })
    },
    openImage (url) {
      window.open(url, '_blank')
    },
    scrollToBottom () {
      this.$nextTick(() => {
        const el = this.$refs.messagesEndRef
        if (el && el.scrollIntoView) el.scrollIntoView({ behavior: 'smooth', block: 'end' })
      })
    },
    async loadHistory () {
      if (!this.conversationId) {
        this.initConversationId()
      }
      try {
        const res = await aiAxios.get(`/api/history/${this.conversationId}`)
        if (res.data && res.data.history) {
          this.messages = res.data.history
        } else {
          this.messages = [{
            role: 'assistant',
            content: '您好！我是AI医疗助手，可以为您解答医疗健康方面的基本问题。请注意：我提供的信息仅供参考，不构成医疗建议，如有紧急情况请立即就医。',
            timestamp: new Date().toISOString()
          }]
        }
      } catch (e) {
        this.messages = [{
          role: 'assistant',
          content: '您好！我是AI医疗助手，可以为您解答医疗健康方面的基本问题。请注意：我提供的信息仅供参考，不构成医疗建议，如有紧急情况请立即就医。',
          timestamp: new Date().toISOString()
        }]
      }
    },
    handleLogout () {
      this.$router.push({ path: '/dashboard' })
    }
  }
}
</script>

<style scoped>
@import './ChatInterface.css';
@import './MessageList.css';
@import './ChatInput.css';
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
