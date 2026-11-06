<template>
  <div>
    <Breadcrumb msg1="挂号收费" msg2="添加挂号" />
    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 提示区域 -->
      <el-alert title="请依次填写用户、病例、挂号信息，所有项必填，点击'立即创建'完成挂号。" type="info" center show-icon :closable="false" style="margin-bottom: 12px;"/>
      <el-steps :active="parseInt(activeIndex)" finish-status="success">
        <el-step title="用户信息"></el-step>
        <el-step title="病例情况"></el-step>
        <el-step title="挂号信息"></el-step>
      </el-steps>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="110px" label-position="left">
        <el-tabs
          v-model="activeIndex"
          :tab-position="'left'"
        >
          <el-tab-pane label="用户信息" name="0">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" :disabled="loading"></el-input>
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="form.gender">
                <el-radio :label="0">男</el-radio>
                <el-radio :label="1">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="身份证号" prop="idno">
              <el-input v-model="form.idno" :disabled="loading"></el-input>
            </el-form-item>
            <el-form-item label="出生日期" prop="birth">
              <el-date-picker
                v-model="form.birthday"
                type="date"
                placeholder="选择日期"
                value-format="yyyy-MM-dd"
                :disabled="loading"
              ></el-date-picker>
            </el-form-item>
            <el-form-item label="年龄">
              <el-input v-model="form.age" type="number" :disabled="loading"></el-input>
            </el-form-item>
            <el-form-item label="地址" prop="address">
              <el-input v-model="form.address" :disabled="loading"></el-input>
            </el-form-item>
          </el-tab-pane>

          <el-tab-pane label="病例情况" name="1">
            <el-form-item label="健康描述" prop="readme">
              <el-input v-model="form.readme" type="textarea" :disabled="loading"></el-input>
            </el-form-item>
            <el-form-item label="现病史">
              <el-input v-model="form.present" type="textarea" :disabled="loading"></el-input>
            </el-form-item>
            <el-form-item label="现病史治疗情况">
              <el-input v-model="form.presentTreat" type="textarea" :disabled="loading"></el-input>
            </el-form-item>
            <el-form-item label="既往史">
              <el-input v-model="form.history" type="textarea" :disabled="loading"></el-input>
            </el-form-item>
            <el-form-item label="过敏史">
              <el-input v-model="form.allergy" type="textarea" :disabled="loading"></el-input>
            </el-form-item>
          </el-tab-pane>

          <el-tab-pane label="挂号信息" name="2">
            <el-form-item label="挂号级别">
              <el-select v-model="form.regsitLevelId" placeholder="请选择" :popper-append-to-body="false" @change="fee" :disabled="loading">
                <el-option
                  v-for="(value, id) in register"
                  :key="id"
                  :label="value.name"
                  :value="id">
                </el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="挂号科室" >
              <el-select v-model="form.deptId" placeholder="请选择" :popper-append-to-body="false" @change="doctor={}" :disabled="loading">
                <el-option
                  v-for="(value, id) in department"
                  :key="id"
                  :label="value.name"
                  :value="id">
                </el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="看诊医生">
              <el-select v-model="form.doctorId" placeholder="请选择" :popper-append-to-body="false" @focus="getDoctor" :disabled="loading">
                <el-option
                  v-for="(value, id) in doctor"
                  :key="id"
                  :label="value.name"
                  :value="id">
                </el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="是否要病历本" prop="book">
              <el-radio-group v-model="form.book" @change="fee" :disabled="loading">
                <el-radio-button :label="0">否</el-radio-button>
                <el-radio-button :label="1">是</el-radio-button>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="应收金额" prop="fee">
              <el-input v-model="form.fee" disabled></el-input>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="save" :loading="loading" :disabled="loading">立即创建</el-button>
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
export default {
  components: {
    Breadcrumb
  },
  data () {
    var checkId = (rule, value, cb) => {
      const reg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|31)|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}([0-9]|x|X)$/
      if (reg.test(value)) {
        return cb()
      }
      cb(new Error('请输入合法的身份证号'))
    }
    return {
      activeIndex: 0,
      form: {
        name: '李白',
        gender: 0,
        idno: '110101199003079550',
        birthday: '1990-03-07',
        age: 30,
        address: '北京市-东城区',
        regsitLevelId: null,
        deptId: null,
        doctorId: null,
        book: 0,
        fee: 0,
        readme: '体检',
        present: '无',
        presentTreat: '无',
        history: '无',
        allergy: '无',
        status: 1
      },
      rules: {
        name: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        idno: [
          { required: true, message: '请输入身份证号码', trigger: 'blur' },
          { validator: checkId, trigger: 'blur' }
        ],
        address: [
          { required: true, message: '请输入地址', trigger: 'blur' }
        ],
        readme: [
          { required: true, message: '请输入健康描述', trigger: 'blur' }
        ],
        regsitLevelId: [
          { required: true, message: '请选择挂号级别', trigger: 'change' }
        ],
        deptId: [
          { required: true, message: '请选择挂号科室', trigger: 'change' }
        ],
        doctorId: [
          { required: true, message: '请选择看诊医生', trigger: 'change' }
        ]
      },
      doctor: {},
      loading: false
    }
  },
  mounted () {
    this.$store.dispatch('getRegisterLevel')
    this.$store.dispatch('getDepartment')
  },
  computed: {
    ...mapState(['register', 'department'])
  },
  methods: {
    fee () {
      if (this.form.regsitLevelId) {
        this.form.fee = this.register[this.form.regsitLevelId].fee
        if (this.form.book === 1) {
          this.form.fee += 5
        }
        this.doctor = {}
      }
    },
    save () {
      this.loading = true
      this.$refs.formRef.validate(valid => {
        if (!valid) {
          this.loading = false
          return
        }
        this.$axios.post('/registers', () => { this.loading = false }, this.form)
      })
    },
    getDoctor () {
      if (Object.keys(this.doctor).length !== 0) return
      if (this.form.regsitLevelId && this.form.deptId) {
        this.$axios.http.get('/users/list', {
          params: {
            active: 1,
            user_type: this.register[this.form.regsitLevelId].roleId,
            dept_id: this.form.deptId
          }
        }).then(response => {
          this.doctor = response.data.data
        })
      } else {
        this.$message.error('请先选择挂号级别和科室')
      }
    }
  }
}
</script>
<style scoped>
.el-select-dropdown {
  top: 38px !important;
  left: 0 !important;
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
