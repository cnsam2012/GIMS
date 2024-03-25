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
        placeholder="搜索用户..."
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
        type: {
          title: '部门类型',
          value: '1',
          component: {
            name: 'dict-select',
            props: {
              dict: {
                data: [
                  {
                    value: '1',
                    label: '院系部门 - AD'
                  },
                  {
                    value: '2',
                    label: '实习单位 - COM'
                  }
                ]
              }
            },
            span: 12
          }
        },
        name: {
          title: '部门名称',
          value: 'name'
        },
        content: {
          title: '备注',
          value: 'content'
        }
      },
      columns: [
        {
          title: '部门id',
          key: 'id'
        },
        {
          title: '部门类型',
          key: 'type'
        },
        {
          title: '部门名称',
          key: 'name'
        },
        {
          title: '备注',
          key: 'content',
          showOverflowTooltip: true
        },
        {
          title: '创建者',
          key: 'belongTo'
        }
      ],
      data: [
        {
          id: 'dep001',
          type: '研发',
          name: '技术部',
          content: '负责公司的技术开发和创新',
          belongTo: '张三'
        },
        {
          id: 'dep002',
          type: '市场',
          name: '营销部',
          content: '负责市场推广和销售策略',
          belongTo: '李四'
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
      let res = await this.$api.FETCH_ALL_DEPARTMENTS(page)
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
          id: {
            title: '部门id',
            value: 'id'
          },
          type: {
            title: '部门类型',
            value: 'type'
          },
          name: {
            title: '部门名称',
            value: 'name'
          },
          content: {
            title: '备注',
            value: 'content'
          },
          belongTo: {
            title: '创建者',
            value: 'belongTo'
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
          type: {
            title: '部门类型',
            value: row.type,
            component: {
              name: 'dict-select',
              props: {
                dict: {
                  data: this.typeDict
                }
              },
              span: 12
            }
          },
          name: {
            title: '部门名称',
            value: row.name
          },
          content: {
            title: '备注',
            value: row.content
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
      this.data = res.departments
      // this.data = this.data.map(item => {
      //   const updatedType = this.typeDict[item.type] || item.type
      //   return {
      //     ...item,
      //     type: updatedType
      //   }
      // })
    },
    async onSearch () {
      if (this.searchKeywords) {
        const pageWithKeywords = {
          keywords: this.searchKeywords,
          current: this.pagination.currentPage,
          limit: this.pagination.pageSize
        }
        let res = await this.$api.FETCH_FUZZY_DEPARTMENTS(pageWithKeywords)
        res = res.data
        this.updateData(res)
      } else {
        await this.fetchData()
      }
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
      this.formOptions.saveLoading = false
      const data = {
        type: row.type,
        name: row.name,
        content: row.content
      }
      const res = await this.$api.ADD_DEPARTMENTS(data)
      console.log(res)
      done()
      this.fetchData()
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
          departmentId: row.id,
          type: row.type,
          name: row.name,
          content: row.content
        }
        const res = await this.$api.UPDATE_DEPARTMENTS(data)
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
