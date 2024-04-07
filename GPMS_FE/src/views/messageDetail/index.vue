<template>
  <d2-container>
    <template slot="header">
      <h1>{{ this.myNameFormatter(name1) }} 与 {{ this.myNameFormatter(name2) }} 的会话</h1>
    </template>
    <d2-crud-x
      ref="messageDetail"
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
      selection-row
      @selection-change="handleSelectionChange"
      @reply-message="handleReplyAMessage"
    >
      <el-button
        slot="header"
        type="success"
        style="margin: 7px"
        @click="onAdd"
      >新建信息
      </el-button>
      <el-input
        slot="header"
        type="text"
        v-model="searchKeywords"
        placeholder="搜索信息..."
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
import { mapState } from 'vuex'

export default {
  computed: {
    ...mapState('d2admin/user', [
      'info'
    ])
  },
  data () {
    return {
      key: 0,
      currentUserInfo: {},
      cid: 0,
      name1: '',
      name2: '',
      loading: false,
      addTemplate: {
        toId: {
          title: '收信方'
        },
        content: {
          title: '内容',
          component: {
            name: 'el-input',
            type: 'textarea',
            rows: '6'
          }
        }
      },
      columns: [
        // {
        //   title: '状态',
        //   key: 'status',
        //   formatter: (row, column, cellValue, index) => {
        //     return cellValue === 0 ? '未读' : '已读'
        //   },
        //   sortable: true
        // },
        {
          title: '发送者',
          key: '_fromId',
          formatter: (row, column, cellValue, index) => {
            if (cellValue === this.info.rolename) {
              return '我'
            }
            return cellValue
          }
        },
        {
          title: '收信者',
          key: '_toId',
          formatter: (row, column, cellValue, index) => {
            if (cellValue === this.info.rolename) {
              return '我'
            }
            return cellValue
          }
        },
        {
          title: '内容',
          key: 'content',
          showOverflowTooltip: true
        },
        {
          title: '创建时间',
          key: 'createTime',
          formatter: this.colCreateTimeFormatter
        }
      ],
      data: [
        {
          id: '210002',
          fromId: '910018',
          toId: '910007',
          conversationId: '910007_910018',
          content: '这是一条发给用户admin的测试信息',
          status: 0,
          createTime: '2024-04-03T09:26:41.000+00:00',
          _fromId: 'cc1'
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
            text: '回复',
            type: 'warn',
            size: 'small',
            emit: 'reply-message',
            disabled: (index, row) => {
              if (row._fromId === this.currentUserInfo.rolename || row.fromId === 910000) {
                return true
              }
              return false
            }
          }
        ],
        // remove: {
        //   size: 'small',
        //   confirm: true,
        //   order: 9
        // },
        fixed: 'right',
        width: '180'
      },
      pagination: {
        currentPage: 1,
        pageSize: 7,
        total: 15
      },
      formOptions: {
        maxHeight: '90%',
        defaultSort: {
          prop: 'status'
        }
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
  async mounted () {
    this.modifiedAddTemplate = this.addTemplate
    this.cid = this.$route.params.cid
    this.name1 = this.$route.params.name1
    this.name2 = this.$route.params.name2
    this.currentUserInfo = this.info
    console.log(this.currentUserInfo)
    await this.fetchData()
    document.addEventListener('keypress', this.handleWatchEnter)
    console.log(this.typeNumToStr('1'))
  },
  methods: {
    async fetchData () {
      const page = {
        current: this.pagination.currentPage,
        limit: this.pagination.pageSize,
        conversationId: this.cid
      }
      this.loading = true
      let res = await this.$api.CONVERSATION_DETAIL(page)
      res = res.data
      await this.updateData(res)
      this.loading = false
    },
    showDetail ({
      index,
      row
    }) {
      this.$refs.messageDetail.showDialog({
        mode: 'view',
        rowIndex: index,
        template: {
          fromId: {
            title: '发送者',
            value: 'fromId',
            formatter: this.toOriginStrFormatter
          },
          content: {
            title: '内容',
            value: 'content',
            formatter: this.toOriginStrFormatter
          },
          createTime: {
            title: '创建时间',
            value: 'createTime',
            formatter: this.colCreateTimeFormatter
          }
        }
      })
    },
    onEditClick ({
      index,
      row
    }) {
      this.$refs.messageDetail.showDialog({
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
      // this.data = res.messageDetail

      const dataPushed = []
      res.letters.map(item => {
        dataPushed.push(item.letter)
      })
      this.data = dataPushed
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
      this.addTemplate.toId.value = ''
      this.$refs.messageDetail.showDialog({
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
        toName: row.toId,
        content: row.content
      }
      await this.$api.SEND_MESSAGE(data)
      done()
      await this.fetchData()
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
    async handleReplyAMessage ({
      index,
      row
    }, done) {
      this.addTemplate.toId.value = row._fromId
      this.$refs.messageDetail.showDialog({
        mode: 'add'
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
    handleSelectionChange (selection) {
      console.log(selection)
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
    toOriginStrFormatter (row, column, cellValue, index) {
      return cellValue
    },
    typeNumToStr (value) {
      const result = this.typeDict.find(item => item.value === value)
      return result ? result.label : ''
    },
    colCreateTimeFormatter (row, column, cellValue, index) {
      var dateRegex = /^\d{4}-\d{2}-\d{2}/
      try {
        var match = cellValue.match(dateRegex)
        if (match) {
          return match[0] // 返回匹配到的日期部分
        } else {
          return 'NULL' // 如果没有匹配到日期部分，则返回空字符串
        }
      } catch (e) {
        return 'NULL'
      }
    },
    myNameFormatter (cellValue) {
      if (cellValue === this.info.rolename) {
        return '我'
      }
      return cellValue
    }
  }
}
</script>
<style>
</style>
