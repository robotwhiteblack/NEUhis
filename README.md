# 东软云医院 & AI医疗助手

本项目为东软云医院管理系统与AI医疗助手的全栈解决方案，包含医院业务管理、智能问答、RAG大模型、图片多模态等功能。

---

## 目录结构

```
├── his/                  # 医院管理系统（前后端）
│   ├── his/              # Java Spring Boot 后端
│   └── his-vue/          # Vue2 前端
├── RAG/ai-medical-assistant/  # AI医疗助手（RAG大模型）
│   ├── backend/          # FastAPI/Python 后端
│   └── frontend/         # React 前端（可选）
├── README.md             # 项目说明
└── ...
```

---

## 快速启动指南

### 1. 初始化数据库

1. 安装 MySQL（推荐 5.7+）。
2. 使用 `his/doc-sql/his.sql` 脚本创建数据库和表：
   - 打开命令行或Navicat等工具，执行：
     ```sql
     source /neuhis/his/doc-sql/his.sql
     ```
3. 修改 `his/his/src/main/resources/application.yml` 数据库配置为你的实际账号密码。

### 2. 启动医院管理系统后端（Java）

```sh
cd his/his
# 使用IDEA等工具运行 WebApp.java，或命令行：
mvn spring-boot:run
```

### 3. 启动RAG大模型后端（Python）

```sh
cd RAG/ai-medical-assistant/backend
pip install -r requirements.txt
# 启动FastAPI服务
python main.py
```

### 4. 启动前端（Vue）

```sh
cd his/his-vue
npm install
npm run serve
```

前端默认地址：http://localhost:8080/

---

## 访问说明

- 访问 http://localhost:8080/ 登录医院管理系统。
- 登录后可选择“医院管理系统”或“AI医疗助手”模块。
- AI助手支持文本与图片多模态问答。

---

## 其他说明

- RAG/ai-medical-assistant/backend/full1.md 为AI知识库示例。
- 如需自定义AI模型或API Key，请修改 `main.py` 配置。
- 详细接口文档见 `his/接口文档.md`。

---


