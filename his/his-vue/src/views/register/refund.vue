<template>
  <div>
    <Breadcrumb msg1="挂号收费" msg2="退费" />

    <!-- 卡片视图区域 -->
    <el-card>
      <el-alert title="请输入病例号后查询，选择要退费的项目后点击'退费'按钮。" type="info" center show-icon :closable="false" style="margin-bottom: 12px;"/>
      <el-row :gutter="10">
        <el-col :span="10">
          <el-input v-model="id" placeholder="请输入病例号" clearable :disabled="loading">
            <el-button slot="append" icon="el-icon-search" @click="getData" :loading="loading"></el-button>
          </el-input>
        </el-col>
        <el-col :span="14">
          <el-button type="primary" icon="el-icon-circle-plus-outline" plain @click="refund()" :disabled="!id || loading || fee===0" :loading="loading">退费</el-button>
        </el-col>
      </el-row>

      <el-row>
         <el-divider content-position="left">用户信息</el-divider>
          姓名：<el-tag>{{form.name}}</el-tag>
          性别： <el-tag>{{form.gender===0?'男':'女'}}</el-tag>
          身份证号:  <el-tag>{{form.idno}}</el-tag>
          退费金额：<el-tag type="success">{{fee}}</el-tag>
      </el-row>

      <el-divider content-position="left">检查费用列表</el-divider>

      <el-table
        :data="checkTableData"
        style="width: 100%"
        @selection-change="checkSelectChange"
      >
        <el-table-column type="selection"></el-table-column>
        <el-table-column type="index"> </el-table-column>
        <el-table-column label="项目名称" prop="itemName"></el-table-column>
        <el-table-column label="检查费用" prop="fee"></el-table-column>
        <el-table-column label="创建时间" prop="createtime"></el-table-column>
        <el-table-column label="状态">
          <template slot-scope="scope">
            {{check_status[scope.row.status]}}
          </template>
        </el-table-column>
      </el-table>

      <el-divider content-position="left">检验费用列表</el-divider>

      <el-table
        :data="inspectTableData"
        style="width: 100%"
        @selection-change="InspectSelectChange"
      >
        <el-table-column type="selection"></el-table-column>
        <el-table-column type="index"> </el-table-column>
        <el-table-column label="项目名称" prop="itemName"></el-table-column>
        <el-table-column label="检验费用" prop="fee"></el-table-column>
        <el-table-column label="创建时间" prop="createtime"></el-table-column>
        <el-table-column label="状态">
          <template slot-scope="scope">
            {{inspect_status[scope.row.status]}}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import Breadcrumb from '@/components/Breadcrumb'
import { mapState } from 'vuex'
export default {
  data () {
    return {
      id: '',
      form: {},
      checkTableData: [],
      inspectTableData: [],
      checkSel: [],
      inspectSel: [],
      checkFee: 0,
      inspectFee: 0,
      loading: false
    }
  },
  components: {
    Breadcrumb
  },
  computed: {
    ...mapState(['check_status', 'inspect_status']),
    fee: function () {
      return this.checkFee + this.inspectFee
    }
  },
  methods: {
    getData () {
      if (!this.id) {
        this.$message.error('请输入病例号')
        return
      }
      if (!/^\d+$/.test(this.id)) {
        this.$message.error('病例号格式错误，只能为数字')
        return
      }
      this.loading = true
      this.getCase(parseInt(this.id)).finally(() => { this.loading = false })
    },
    getCase (id) {
      return this.$axios.http.get(
        `/registers/${id}`
      ).then(repsonse => {
        if (repsonse.data.status !== 200) {
          Promise.reject(repsonse.data.message)
        } else {
          this.form = repsonse.data.data
          return this.$axios.http.get(`/checkApplys/list/${id}?status=2`)
        }
      }).then(repsonse => {
        this.checkTableData = repsonse.data.data
        return this.$axios.http.get(`/inspectApplys/list/${id}?status=2`)
      }).then(repsonse => {
        this.inspectTableData = repsonse.data.data
      }).catch(error => {
        this.$message.error('' + error)
      })
    },
    checkSelectChange (val) {
      this.checkSel = val
      this.checkFee = this.checkSel.map(val => val.fee).reduce((acc, val) => acc + val, 0)
    },
    InspectSelectChange (val) {
      this.inspectSel = val
      this.inspectFee = this.inspectSel.map(val => val.fee).reduce((acc, val) => acc + val, 0)
    },
    refund () {
      if (this.fee === 0) {
        this.$message('没有任何被选中的数据')
        return
      }
      this.loading = true
      const promises = []
      if (this.checkFee !== 0) {
        const ids = this.checkSel.map(val => val.id)
        promises.push(this.$axios.put('/checkApplys/refund', () => {}, { ids: ids.join(',') }))
      }
      if (this.inspectFee !== 0) {
        const ids = this.inspectSel.map(val => val.id)
        promises.push(this.$axios.put('/inspectApplys/refund', () => {}, { ids: ids.join(',') }))
      }
      Promise.all(promises).then(() => {
        this.getData()
      }).finally(() => {
        this.loading = false
      })
    }
  }
}
</script>
<style scoped>
.el-divider {
  margin-top: 30px;
}
.el-tag {
  margin-right: 10px;
  margin-left: 5px;
}
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
