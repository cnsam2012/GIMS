<template>
  <d2-container>
    <template v-if="allPlanListDis">
      <template slot="header">
        <h1>已选实习</h1>
      </template>
      <d2-crud-x
        ref="reports"
        :columns="columns"
        :data="data"
        :rowHandle="rowHandle"
        :pagination="pagination"
        :form-options="formOptions"
        :loading="loading"
        :addTemplate="addTemplate"
        selection-row
        @show-detail="showDetail"
        @on-edit-click="onEditClick"
        @on-delete="onDelete"
        @pagination-current-change="paginationCurrentChange"
        @dialog-cancel="handleDialogCancel"
        @dialog-open="handleDialogOpen"
        @row-edit="handleRowEdit"
        @row-remove="handleRowRemove"
        @row-add="handleRowAdd"
        @selection-change="handleSelectionChange"
      >
        <el-button
          slot="header"
          type="success"
          style="margin: 7px"
          @click="onAdd"
          v-if="info.userType != 1"
        >新建联系
        </el-button>

        <el-input
          slot="header"
          type="text"
          v-model="searchKeywords"
          placeholder="搜索实习..."
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
    </template>
    <template v-if="specPlanDis">
      <h1 slot="header">已选实习： {{ currentUserPlanc._planId }}</h1>
      <el-descriptions title="实习信息">
        <el-descriptions-item label="类型">{{ data.type }}</el-descriptions-item>
        <el-descriptions-item label="主要方向">{{ data.majorOrieId }}</el-descriptions-item>
        <el-descriptions-item label="等级">{{ data.grade }}</el-descriptions-item>
        <el-descriptions-item label="学分">{{ data.credit }}</el-descriptions-item>
        <el-descriptions-item label="内容">{{ data.content }}</el-descriptions-item>
        <el-descriptions-item label="目标">{{ data.objective }}</el-descriptions-item>
        <el-descriptions-item label="需求">{{ data.demand }}</el-descriptions-item>
        <el-descriptions-item label="评分计算类型">{{ data.scoreCalType }}</el-descriptions-item>
        <el-descriptions-item label="日报占比">{{ data.percentInDailyReport }}%</el-descriptions-item>
        <el-descriptions-item label="周报占比">{{ data.percentInWeeklyReport }}%</el-descriptions-item>
        <el-descriptions-item label="月报占比">{{ data.percentInMonthlyReport }}%</el-descriptions-item>
        <el-descriptions-item label="总结占比">{{ data.percentInSummary }}%</el-descriptions-item>
        <el-descriptions-item label="截止日期">{{ data.deadline }}</el-descriptions-item>
        <el-descriptions-item label="创建者">{{ data._creator }}</el-descriptions-item>
      </el-descriptions>
      <template>
        <el-button @click="submit" type="danger">取消参加</el-button>
        <!--        <el-button @click="cancel" type="warn">取消</el-button>-->
      </template>
      <template slot="footer">
        <random-motto/>
      </template>
    </template>
  </d2-container>
</template>

<script>
import { mapActions, mapState } from 'vuex'

export default {
  computed: {
    ...mapState('d2admin/user', [
      'info'
    ])
  },
  data () {
    return {
      allPlanListDis: true,
      specPlanDis: false,
      currentUserPlanc: {},
      originalData: [],
      loading: false,
      addTemplate: {},
      columns: [
        {
          title: 'ID',
          key: 'id'
        },
        {
          title: '用户',
          key: '_userId'
        },
        {
          title: '计划名',
          key: '_planId'
        },
        {
          title: '状态',
          key: 'status'
        },
        {
          title: '创建时间',
          key: 'createTime'
        }
      ],
      data: [
        {
          id: 0,
          type: 0,
          name: 'string',
          majorOrieId: 0,
          grade: 0,
          startD: null,
          endD: null,
          classHour: 0,
          credit: 0,
          percentIn: 0,
          percentEx: 0,
          content: 'string',
          objective: 'string',
          demand: 'string',
          scoreCalType: 0,
          percentInDailyReport: 0,
          percentInWeeklyReport: 0,
          percentInMonthlyReport: 0,
          percentInSummary: 0,
          deadline: '2024-03-30T16:00:00.000+00:00',
          creator: 910007,
          _deleted: false
        }
      ],
      rowHandle: {
        custom: [
          {
            text: '查看',
            type: 'success',
            size: 'small',
            emit: 'show-detail'
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
        pageSize: 4,
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
      this.rowHandle = {
        custom: [
          {
            text: '查看',
            type: 'success',
            size: 'small',
            emit: 'show-detail'
          }
        ],
        fixed: 'right',
        width: '80'
      }
    }
    this.checkIfStudentHasPlan()
  },
  methods: {
    ...mapActions('d2admin/page', [
      'close'
    ]),
    async fetchData () {
      const pageAndMode = {
        current: this.pagination.currentPage,
        limit: this.pagination.pageSize
      }
      this.loading = true
      // eslint-disable-next-line no-unused-vars
      let res = null
      // AUTH START取
      res = await this.$api.FETCH_ALL_PLANS_C(pageAndMode)
      // AUTH END
      res = res.data
      this.updateData(res)
      this.loading = false
    },
    showDetail ({
      index,
      row
    }) {
      this.goToPlanDetail(row.id)
    },
    goToPlanDetail (planId) {
      this.$router.push({
        path: 'planDetail/' + planId
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
      // var dataPushed = []
      // dataPushed = res.planc.map(obj => {
      //   console.log(obj)
      // })
      this.data = res.planc
      this.originalData = res.planc
    },
    async onSearch () {
      if (this.searchKeywords === '') {
        this.data = this.originalData
        return
      }
      this.loading = true
      const searchResults = this.data.filter(obj => obj.name.includes(this.searchKeywords))
      this.data = searchResults
      this.loading = false
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
        await this.$api.DROP_A_PLAN(row.id)
        this.$message({
          message: '删除成功',
          type: 'success'
        })
        done()
      }, 300)
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
        major_orie_id: row.major_orie_id,
        grade: row.grade,
        start_d: row.start_d,
        end_d: row.end_d,
        class_hour: row.class_hour,
        credit: row.credit,
        percent_in: row.percent_in,
        percent_ex: row.percent_ex,
        content: row.content,
        objective: row.objective,
        demand: row.demand,
        score_cal_type: row.score_cal_type,
        percent_in_daily_report: row.percent_in_daily_report,
        percent_in_weekly_report: row.percent_in_weekly_report,
        percent_in_monthly_report: row.percent_in_monthly_report,
        percent_in_summary: row.percent_in_summary,
        deadline: row.deadline,
        is_deleted: row.is_deleted
      }
      console.log(data)
      const res = await this.$api.ADD_A_PLAN(data)
      console.log(res)
      done()
      this.fetchData()
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
      try {
        var dateRegex = /^\d{4}-\d{2}-\d{2}/
        var match = cellValue.match(dateRegex)
        if (match) {
          return match[0] // 返回匹配到的日期部分
        } else {
          return 'NULL' // 如果没有匹配到日期部分，则返回空字符串
        }
      } catch (e) {
        console.warn(e)
      }
    },
    handleSelectionChange (selection) {
      console.log(selection)
    },
    async checkIfStudentHasPlan () {
      if (this.info.userType === 1) {
        console.warn('student')
        var data = this.info.userId
        var res = await this.$api.FETCH_SPEC_PLAN_C(data)
        if (res.data.planc != null) {
          console.warn('has plan')
          this.currentUserPlanc = res.data.planc
          this.allPlanListDis = false
          this.specPlanDis = true

          var pid = this.currentUserPlanc.planId
          var requestBody = {
            planId: pid
          }
          const res1 = await this.$api.FETCH_SPEC_PLAN(requestBody)
          this.data = res1.data.plan
        } else {
          await this.$router.push({ name: 'plans_management' })
        }
      }
    },
    async submit () {
      await this.$confirm('此操作将取消参加该实习, 操作不可逆且会跳转回实习选择页面, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        confirmButtonClass: 'el-button el-button--default el-button--small el-button--success',
        showClose: false
      }).then(async () => {
        // console.log(this.currentUserPlanc.id)
        await this.$api.DROP_A_PLAN_C(this.currentUserPlanc.id)
        this.checkIfStudentHasPlan()
        await this.close({ tagName: '/planchoose_management' })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消'
        })
      })
    },
    cancel () {

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
