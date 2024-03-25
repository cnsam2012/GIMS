<template>
  <d2-container>
    <template slot="header">
    </template>
    <d2-crud-x
      ref="reports"
      :columns="columns"
      :data="data"
      :rowHandle="rowHandle"
      :pagination="pagination"
      :form-options="formOptions"
      :loading="loading"
      selection-row
      @show-detail="showDetail"
      @on-edit-click="onEditClick"
      @on-delete="onDelete"
      @pagination-current-change="paginationCurrentChange"
      @dialog-cancel="handleDialogCancel"
      @dialog-open="handleDialogOpen"
      @row-edit="handleRowEdit"
      @row-remove="handleRowRemove"
      @selection-change="handleSelectionChange"
    >
      <el-button
        slot="header"
        type="success"
        style="margin: 7px"
        @click="onAdd"
        v-if="info.userType === 1"
      >新建报告
      </el-button>

      <template
        v-if="info.userType === 1"
      >
        <el-input
          slot="header"
          type="text"
          v-model="searchKeywords"
          placeholder="搜索我的报告..."
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
      </template>
      <template
        v-else
      >
        <el-input
          slot="header"
          type="text"
          v-model="searchKeywords"
          placeholder="搜索提交人..."
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
      </template>
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
import { mapState } from 'vuex'

export default {
  computed: {
    ...mapState('d2admin/user', [
      'info'
    ])
  },
  data () {
    return {
      loading: false,
      addTemplate: {},
      columns: [
        {
          title: '提交人',
          key: '_id',
          fixed: true
        },
        {
          title: '报告类型',
          key: 'type',
          formatter: this.typeNumToStrFormatter
        },
        {
          title: '标题',
          key: 'title',
          showOverflowTooltip: true
        },
        {
          title: '内容',
          key: 'content',
          showOverflowTooltip: true
        },
        {
          title: '提交时间',
          key: 'createTime',
          formatter: this.colCreateTimeFormatter
        },
        {
          title: '阅读状态',
          key: 'isRead',
          formatter: this.readNumToStrFormatter
        },
        {
          title: '最后操作',
          key: '_lastedEditUserId'
        }
      ],
      data: [
        {
          id: '1',
          title: '通知标题1',
          key: 'title',
          content: '通知内容1',
          createTime: '2021-01-01 10:00:00',
          isRead: '1',
          lastedEditUserId: '910018',
          type: '1',
          _id: 'username1',
          _type: 'type1',
          _isRead: '已读'
        },
        {
          id: '2',
          title: '通知标题2',
          key: 'title',
          content: '通知内容2',
          createTime: '2021-02-01 14:30:00',
          isRead: '0',
          lastedEditUserId: '910018',
          type: '2',
          _id: 'username2',
          _type: 'type2',
          _isRead: '未读'
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
          label: '周记'
        },
        {
          value: '2',
          label: '月记'
        },
        {
          value: '3',
          label: '总结'
        }
      ],
      readDict: [
        {
          value: '0',
          label: '未读'
        },
        {
          value: '1',
          label: '已读'
        }
      ],
      editingType: '',
      editingTemplate: {
        isRead: {
          title: '阅读状态',
          value: '_isRead'
        },
        type: {
          title: '报告类型',
          value: '_type'
        }
      }
    }
  },
  async mounted () {
    this.fetchData()
    document.addEventListener('keypress', this.handleWatchEnter)
    if (this.info.userType === 1) {
      this.editingTemplate = {
        // TODO 报告类型应为多选框，内容应为长文本框
        type: {
          title: '报告类型',
          value: 'type'
        },
        title: {
          title: '标题',
          value: 'title'
        },
        content: {
          title: '内容',
          value: 'content'
        }
      }
    }
  },
  methods: {
    async fetchData () {
      const pageAndMode = {
        current: this.pagination.currentPage,
        limit: this.pagination.pageSize,
        orderMode: 0
      }
      this.loading = true
      let res = null
      // AUTH START
      // TODO 教师2 实习单位3 查看其所属单位、所管理学生的报告
      // 仅管理员可拉取所有报告，其他用户根据特定情况拉取
      if (this.info.userType === 9) {
        res = await this.$api.FETCH_ALL_REPORTS(pageAndMode)
      } else {
        pageAndMode.id = this.info.userId
        res = await this.$api.FETCH_SPEC_USER_REPORTS(pageAndMode)
      }
      // AUTH END
      res = res.data
      this.updateData(res)
      this.loading = false
    },
    showDetail ({
      index,
      row
    }) {
      this.$refs.reports.showDialog({
        mode: 'view',
        rowIndex: index,
        template: {
          id: {
            title: '提交人',
            value: '_id',
            formatter: this.id2NameFormatter
          },
          type: {
            title: '报告类型',
            value: 'type',
            formatter: this.typeNumToStrFormatter
          },
          title: {
            title: '标题',
            value: 'title',
            formatter: this.generalFormatter
          },
          content: {
            title: '内容',
            value: 'content',
            formatter: this.generalFormatter
          },
          createTime: {
            title: '提交时间',
            value: 'createTime',
            formatter: this.colCreateTimeFormatter
          },
          isRead: {
            title: '阅读状态',
            value: 'isRead',
            formatter: this.readNumToStrFormatter
          },
          lastedEditUserId: {
            title: '最后操作',
            value: 'lastedEditUserId',
            formatter: this.latestIdToUsernameFormatter
          }
        }
      })
    },
    onEditClick ({
      index,
      row
    }) {
      this.$refs.reports.showDialog({
        mode: 'edit',
        rowIndex: index,
        template: this.editingTemplate
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
      // this.data = res.reports.map(item => item.reports)
      // var reports = res.reports.map(item => item.reports)
      // var users = res.reports.map(item => item.user)
      // console.log(reports)
      // console.log(users)

      var dataToThis = []
      res.reports.map(item => {
        var dataPushed = item.reports
        dataPushed._id = item.user.roleName + ' (' + item.user.username + ')'
        dataPushed._lastedEditUserId = item.latestEditRoleName + ' (' + item.latestEditUserName + ')'
        // dataPushed._type = this.typeNumToStr(item.reports.type + '')
        // dataPushed._isRead = this.readNumToStr(item.reports.isRead + '')
        dataToThis.push(dataPushed)
      })
      this.data = dataToThis
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
      this.$refs.reports.showDialog({
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
    // async handleRowAdd (row, done) {
    //   this.formOptions.saveLoading = true
    //   await this.$message({
    //     message: '保存成功',
    //     type: 'success'
    //   })
    //   // // done可以传入一个对象来修改提交的某个字段
    //   // done({
    //   //   address: '我是通过done事件传入的数据！'
    //   // })
    //   this.formOptions.saveLoading = false
    //   const data = {
    //     type: row.type,
    //     name: row.name,
    //     content: row.content
    //   }
    //   // const res = await this.$api.ADD_DEPARTMENTS(data)
    //   // console.log(res)
    //   done()
    //   this.fetchData()
    // },
    async handleRowRemove ({
      index,
      row
    }, done) {
      setTimeout(async () => {
        var data = {
          id: row.id
        }
        await this.$api.DROP_REPORTS(data)
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
    id2NameFormatter (row, column, cellValue, index) {
      // console.log(row)
      var res = this.data.find(item => item.id === row.id)
      return res._id
    },
    generalFormatter (row, column, cellValue, index) {
      return cellValue
    },
    typeNumToStrFormatter (row, column, cellValue, index) {
      const result = this.typeDict.find(item => item.value === cellValue + '')
      return result ? result.label : ''
    },
    readNumToStrFormatter (row, column, cellValue, index) {
      const result = this.readDict.find(item => item.value === cellValue + '')
      return result ? result.label : ''
    },
    latestIdToUsernameFormatter (row, column, cellValue, index) {
      var res = this.data.find(item => item.id === row.id)
      return res._lastedEditUserId
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
    handleSelectionChange (selection) {
      console.log(selection)
    }
  }
}
</script>
<style >
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
