<template>
  <d2-container>
    <template slot="header">
      <!--      <el-input-->
      <!--        type="text"-->
      <!--        v-model="formLogin.code"-->
      <!--        placeholder="验证码">-->
      <!--        <template slot="append">-->
      <!--          <img class="login-code" :src="imgSrc" alt="kaptcha_pic" @click="refreshKaptcha">-->
      <!--          &lt;!&ndash;                      <img class="login-code" src="./image/login-code.png" alt="kaptcha_pic" @click="refreshKaptcha">&ndash;&gt;-->
      <!--        </template>-->
      <!--      </el-input>-->
    </template>
    <d2-crud-x
      ref="departments"
      :columns="columns"
      :addTemplate="addTemplate"
      :data="data"
      :rowHandle="rowHandle"
      :pagination="pagination"
      :form-options="formOptions"
      :loading="loading"
      @show-detail="showDetail"
      @on-edit-click="onEditClick"
      @on-delete="onDelete"
      @pagination-current-change="paginationCurrentChange"
      @dialog-cancel="handleDialogCancel"
      @dialog-open="handleDialogOpen"
      @row-add="handleRowAdd"
      @row-edit="handleRowEdit"
      @row-remove="handleRowRemove"
    >
      <el-button
        slot="header"
        type="success"
        style="margin: 7px"
        @click="onAdd"
      >新增用户
      </el-button>
      <el-input
        slot="header"
        type="text"
        v-model="searchKeywords"
        placeholder="根据姓名搜索用户..."
        style="width: 20%; margin: 7px"
      >
        <template slot="append">
          <el-button
            slot="header"
            @click="onSearch"
          >搜索
          </el-button>
        </template>
      </el-input>
      <el-input-number
        slot="header"
        v-model="pagination.pageSize"
        @change="handlePageLimitChange"
        :min="3"
        :max="15"
        label="每页显示"
        style="width: 120px; margin: 7px"
      >
        1
      </el-input-number>
    </d2-crud-x>
  </d2-container>
</template>

<script>
export default {
  data () {
    return {
      loading: false,
      addTemplate: {
        roleName: {
          title: '姓名',
          value: ''
        },
        username: {
          title: '用户名',
          value: ''
        },
        password: {
          title: '密码',
          value: '2012'
        },
        email: {
          title: '邮箱',
          value: ''
        },
        phone: {
          title: '手机号',
          value: ''
        },
        type: {
          title: '类型',
          value: '',
          placeHolder: 'hello'
        },
        status: {
          title: '状态',
          value: ''
        },
        createTime: {
          title: '创建时间',
          value: ''
        },
        departmentId: {
          title: '部门',
          value: ''
        }
      },
      columns: [
        {
          title: '角色名称',
          key: 'roleName',
          fixed: 'left'
        },
        {
          title: '用户名',
          key: 'username'
        },
        {
          title: '邮箱',
          key: 'email'
        },
        {
          title: '手机号',
          key: 'phone'
        },
        {
          title: '类型',
          key: 'type'
        },
        {
          title: '状态',
          key: 'status'
        },
        {
          title: '创建时间',
          key: 'createTime',
          formatter: this.colCreateTimeFormatter
        },
        {
          title: '部门',
          key: 'departmentId'
        }
      ],
      data: [
        {
          id: 910018,
          username: 'cc1',
          email: '111@qq.com',
          phone: null,
          type: 1,
          status: 1,
          activationCode: '',
          createTime: '2024-03-09T10:04:42.000+00:00',
          departmentId: 810011,
          roleName: '陈晶滑'
        }
      ],
      rowHandle: {
        custom: [
          {
            text: '查看',
            type: 'success',
            size: 'small',
            emit: 'show-detail'
          },
          {
            text: '更改',
            type: 'light',
            size: 'small',
            emit: 'on-edit-click'
          }
        ],
        remove: {
          size: 'small',
          confirm: true,
          order: 9
        },
        fixed: 'right',
        width: '210'
      },
      pagination: {
        currentPage: 1,
        pageSize: 7,
        total: 15
      },
      formOptions: {
        maxHeight: '90%'
      },
      searchKeywords: '',
      typeDict: [
        {
          value: '1',
          label: '院系部门 - AD'
        },
        {
          value: '2',
          label: '实习单位 - COM'
        }
      ],
      editingType: ''
    }
  },
  mounted () {
    this.fetchData()
    document.addEventListener('keypress', this.handleWatchEnter)
    console.log(this.typeNumToStr('1'))
  },
  methods: {
    async fetchData () {
      const page = {
        current: this.pagination.currentPage,
        limit: this.pagination.pageSize
      }
      this.loading = true
      let res = await this.$api.SYS_USER_GET_ALL_USERLIST(page)
      console.log(res)
      res = res.data
      this.updateData(res)
      this.loading = false
    },
    showDetail ({
      index,
      row
    }) {
      this.$refs.departments.showDialog({
        mode: 'view',
        rowIndex: index,
        template: {
          roleName: {
            title: '角色名称',
            value: 'roleName'
          },
          username: {
            title: '用户名',
            value: 'username'
          },
          email: {
            title: '邮箱',
            value: 'email'
          },
          phone: {
            title: '手机号',
            value: 'phone'
          },
          type: {
            title: '类型',
            value: 'type'
          },
          status: {
            title: '状态',
            value: 'status'
          },
          createTime: {
            title: '创建时间',
            value: 'createTime'
          },
          departmentId: {
            title: '部门',
            value: 'departmentId'
          }
        }
      })
    },
    onEditClick ({
      index,
      row
    }) {
      this.$refs.departments.showDialog({
        mode: 'edit',
        rowIndex: index,
        template: {
          password: {
            title: '用户密码'
          },
          roleName: {
            title: '角色名称',
            value: 'roleName'
          },
          username: {
            title: '用户名',
            value: 'username'
          },
          email: {
            title: '邮箱',
            value: 'email'
          },
          phone: {
            title: '手机号',
            value: 'phone'
          },
          type: {
            title: '类型',
            value: 'type'
          },
          status: {
            title: '状态',
            value: 'status'
          },
          createTime: {
            title: '创建时间',
            value: 'createTime'
          },
          departmentId: {
            title: '部门',
            value: 'departmentId'
          }
        }
      })
    },
    async onDelete ({
      index,
      row
    }) {

    },
    paginationCurrentChange (currentPage) {
      this.pagination.currentPage = currentPage
      this.fetchData()
    },
    updateData (res) {
      const pageRec = res.page
      this.pagination.currentPage = pageRec.current
      this.pagination.total = pageRec.rows
      this.pagination.pageSize = pageRec.limit
      this.data = res.users
      this.originalData = res.users
    },
    async onSearch () {
      if (this.searchKeywords === '') {
        this.data = this.originalData
        return
      }
      this.loading = true
      const searchResults = this.data.filter(obj => obj.roleName.includes(this.searchKeywords))
      this.data = searchResults
      this.loading = false
    },
    onAdd () {
      this.$refs.departments.showDialog({
        mode: 'add'
      })
    },
    handleDialogCancel (done) {
      this.$message({
        message: '取消保存',
        type: 'warning'
      })
      done()
    },
    async handleRowAdd (row, done) {
      this.formOptions.saveLoading = true
      await this.$message({
        message: '保存成功',
        type: 'success'
      })
      // // done可以传入一个对象来修改提交的某个字段
      // done({
      //   address: '我是通过done事件传入的数据！'
      // })
      const data = {
        username: row.username,
        password: row.password,
        email: row.email,
        phone: row.phone,
        departmentId: -1,
        roleName: row.roleName,
        type: row.roleType
      }
      const res = await this.$api.SYS_USER_SIGNUP(data)
      console.log(res)
      done()
      this.fetchData()
      this.formOptions.saveLoading = false
    },
    async handleRowRemove ({
      index,
      row
    }, done) {
      setTimeout(async () => {
        await this.$api.DROP_DEPARTMENTS(row.id)
        this.$message({
          message: '删除成功',
          type: 'success'
        })
        done()
      }, 300)
    },
    handleDialogOpen ({ mode }) {
      this.$message({
        message: mode,
        type: 'success'
      })
    },
    async handleRowEdit ({
      index,
      row
    }, done) {
      this.formOptions.saveLoading = true
      setTimeout(async () => {
        if (JSON.stringify(this.data[index]) === JSON.stringify(row)) {
          this.$message({
            message: '未作编辑，取消操作',
            type: 'warning'
          })
          this.formOptions.saveLoading = false
          done()
          return
        }
        this.$message({
          message: '编辑成功',
          type: 'success'
        })
        const data = {
          id: row.id,
          username: row.username,
          password: row.password,
          salt: row.salt,
          email: row.email,
          phone: row.phone,
          type: row.type,
          status: row.status,
          departmentId: row.departmentId,
          roleName: row.roleName
        }
        const res = await this.$api.SYS_USER_ALTER_USER_BY_ID(data)
        console.log(res)
        // done可以传入一个对象来修改提交的某个字段
        done({
          // content: '我是通过done事件传入的数据！'
        })
        this.formOptions.saveLoading = false
      }, 300)
    },
    handlePageLimitChange () {
      this.pagination.currentPage = 1
      this.fetchData()
    },
    handleWatchEnter (e) {
      var key = window.event ? e.keyCode : e.which
      if (key === 13) {
        // 这里执行相应的行为动作
        this.onSearch()
      }
    },
    colCreateTimeFormatter (row, column, cellValue, index) {
      var dateRegex = /^\d{4}-\d{2}-\d{2}/
      var match = cellValue.match(dateRegex)
      if (match) {
        return match[0] // 返回匹配到的日期部分
      } else {
        return 'NULL' // 如果没有匹配到日期部分，则返回空字符串
      }
    },
    typeNumToStr (value) {
      const result = this.typeDict.find(item => item.value === value)
      return result ? result.label : ''
    }
  }
}
</script>
<style>
.el-button--primary {
  color: white;
  background-color: #3fb355;
  border-color: #3fb355;
}

.el-button--primary:hover {
  //color: #3fb355;
  //background-color: rgba(63, 179, 85, 0.12);
}

.el-button:hover {
  border-color: #3fb355;
}
</style>
