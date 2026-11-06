<template>
  <div>
    <Breadcrumb msg1="挂号收费" msg2="退号" />

    <!-- 卡片视图区域 -->
    <el-card>
      <el-alert title="请输入病人姓名或病例号后查询，点击'退号'按钮可进行退号操作。" type="info" center show-icon :closable="false" style="margin-bottom: 12px;"/>
      <el-row >
        <el-col :span="10">
          <el-input v-model="queryInfo.name" placeholder="请输入要查的病人姓名或病例号" clearable @clear="getData" :disabled="loading">
              <el-button slot="append" icon="el-icon-search" @click="getData" :loading="loading"></el-button>
          </el-input>
        </el-col>
      </el-row>

      <el-table
        :data="tableData"
        style="width: 100%"
      >
      <el-table-column type="selection"></el-table-column>
      <el-table-column label="病历号"  prop="id" width="80px"></el-table-column>
      <el-table-column label="姓名" prop="name" ></el-table-column>
      <el-table-column label="性别" width="50px">
        <template slot-scope="scope">
          {{scope.row.gender===0?'男':'女'}}
        </template>
      </el-table-column>
      <el-table-column label="身份证号" prop="idno"> </el-table-column>
      <el-table-column label="年龄" prop="age" width="50px"> </el-table-column>
      <el-table-column label="挂号费用" prop="fee" width="100px"> </el-table-column>
      <el-table-column label="创建时间" prop="createtime" > </el-table-column>
      <el-table-column label="操作" width="220px">
      ddd  <template slot-scope="scope">
          <el-button type="danger" round @click="del(scope.row.id)">退号</el-button>
        </template>
      </el-table-column>
      </el-table>
      <!-- 分页区域 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.page"
        :page-sizes="[3, 5, 10, 20]"
        :page-size="queryInfo.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      ></el-pagination>
    </el-card>
  </div>
</template>

<script>
import Breadcrumb from '@/components/Breadcrumb'
import { MessageBox } from 'element-ui'
export default {
  data () {
    return {
      queryInfo: {
        name: '',
        page: 1,
        limit: 10,
        status: 1,
        active: 1
      },
      tableData: [],
      total: 0,
      loading: false
    }
  },
  components: {
    Breadcrumb
  },
  mounted () {
    this.getData()
  },
  methods: {
    getData () {
      if (!this.queryInfo.name) {
        this.$message.error('请输入病人姓名或病例号')
        return
      }
      this.loading = true
      this.$axios.get('/registers',
        response => {
          this.tableData = response.data.records
          this.total = parseInt(response.data.total)
          this.loading = false
        },
        this.queryInfo
      )
    },
    del (id) {
      MessageBox.confirm('确定要退号吗？', '退号操作', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        this.$axios.put(`/registers/num/${id}`, () => {
          this.getData()
          this.loading = false
        })
      }).catch(() => {})
    },
    handleSizeChange (newSize) {
      this.queryInfo.limit = newSize
      this.getData()
    },
    // 监听 页码值 改变的事件
    handleCurrentChange (newPage) {
      this.queryInfo.page = newPage
      this.getData()
    }
  }
}
</script>
<style scoped>
.el-card {
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(33,150,243,0.10);
  background: linear-gradient(135deg, #e3f0ff 0%, #f8fbff 100%);
}
.el-alert {
  border-radius: 10px;
  margin-bottom: 12px;
}
.el-input, .el-select, .el-button {
  border-radius: 8px !important;
}
.el-table {
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 12px;
}
.el-pagination {
  margin-top: 18px;
  padding: 12px 0;
  border-radius: 12px;
  background: linear-gradient(90deg, #e3f0ff 0%, #f8fbff 100%);
  box-shadow: 0 2px 12px #2196f344;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: box-shadow 0.3s;
}
.el-pagination .el-pager li {
  border-radius: 8px;
  margin: 0 2px;
  transition: background 0.2s, color 0.2s;
}
.el-pagination .el-pager li.active {
  background: linear-gradient(90deg, #2196f3 0%, #21d4fd 100%);
  color: #fff;
  box-shadow: 0 2px 8px #2196f355;
}
.el-pagination .el-pager li:hover {
  background: #e3f0ff;
  color: #2196f3;
}
</style>
