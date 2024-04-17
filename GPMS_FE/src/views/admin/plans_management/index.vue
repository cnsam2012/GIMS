<template>
  <d2-container>
    <template slot="header">
      <h1>全部实习</h1>
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
      >新建实习
      </el-button>

      <el-button
        slot="header"
        type="warning"
        style="margin: 7px"
        @click="onAdd"
        v-if="info.userType == 1"
      ><i class="el-icon-warning"></i>找不到理想的实习？点此申请自主实习
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
      originalData: [],
      loading: false,
      addTemplate: {
        type: {
          title: '类型',
          value: '顶岗实习'
        },
        name: {
          title: '名称',
          value: '网络工程师'
        },
        major_orie_id: {
          title: '专业方向ID',
          value: '网络工程方向',
          display: false
        },
        grade: {
          title: '年级',
          value: '4'
        },
        start_d: {
          title: '开始日期',
          value: '2024-01-01'
        },
        end_d: {
          title: '结束日期',
          value: '2024-07-01'
        },
        class_hour: {
          title: '课时',
          value: '0'
        },
        credit: {
          title: '学分',
          value: '4'
        },
        percent_in: {
          title: '内部百分比',
          value: '0'
        },
        percent_ex: {
          title: '外部百分比',
          value: '0'
        },
        content: {
          title: '内容',
          component: {
            name: 'el-input',
            type: 'textarea',
            rows: '6',
            value: '熟悉网络设备和技术：通过实习，深入了解并熟悉各种网络设备和技术，包括路由器、交换机、防火墙等，并能熟练配置和管理网络设备。\n' +
              '\n' +
              '网络故障排除和维护：通过实践经验，掌握常见的网络故障排除方法，能够快速定位和解决网络故障，并能进行网络维护和优化。\n' +
              '\n' +
              '网络安全知识和技能：学习和了解网络安全的基本概念和原理，熟悉常见的网络攻击和防御方法，并能够实施一些基本的网络安全防护措施。\n' +
              '\n' +
              '网络规划和设计：参与网络规划和设计项目，学习并掌握网络规划和设计的基本原则和方法，能够根据需求进行网络规划和设计，并提出改进建议。\n' +
              '\n' +
              '网络监控和性能优化：了解网络监控系统和工具的使用，学习并掌握网络性能优化的方法，能够进行网络性能监控和优化，提高网络的稳定性和性能。'
          }
        },
        objective: {
          title: '目标',
          component: {
            name: 'el-input',
            type: 'textarea',
            rows: '6',
            value: '熟悉网络设备和技术：通过实习，深入了解并熟悉各种网络设备和技术，包括路由器、交换机、防火墙等，并能熟练配置和管理网络设备。\n' +
              '\n' +
              '网络故障排除和维护：通过实践经验，掌握常见的网络故障排除方法，能够快速定位和解决网络故障，并能进行网络维护和优化。\n' +
              '\n' +
              '网络安全知识和技能：学习和了解网络安全的基本概念和原理，熟悉常见的网络攻击和防御方法，并能够实施一些基本的网络安全防护措施。\n' +
              '\n' +
              '网络规划和设计：参与网络规划和设计项目，学习并掌握网络规划和设计的基本原则和方法，能够根据需求进行网络规划和设计，并提出改进建议。\n' +
              '\n' +
              '网络监控和性能优化：了解网络监控系统和工具的使用，学习并掌握网络性能优化的方法，能够进行网络性能监控和优化，提高网络的稳定性和性能。'
          }
        },
        demand: {
          title: '要求',
          component: {
            name: 'el-input',
            type: 'textarea',
            rows: '6',
            value: '网络知识基础：具备扎实的计算机网络基础知识，包括网络协议、网络拓扑、网络设备和技术等方面的知识。\n' +
              '\n' +
              '熟悉网络设备和工具：熟悉常见的网络设备和工具，如路由器、交换机、防火墙、网络监控工具等，并能进行基本的配置和管理。\n' +
              '\n' +
              '网络故障排除能力：具备一定的网络故障排除能力，能够分析和解决常见的网络故障，并具备良好的问题解决能力。\n' +
              '\n' +
              '网络安全意识：具备一定的网络安全意识，了解网络安全的基本概念和原理，并能够采取一些基本的网络安全防护措施。'
          }
        },
        score_cal_type: {
          title: '分数计算类型',
          value: '0'
        },
        percent_in_daily_report: {
          title: '每日报告内部百分比',
          value: '5'
        },
        percent_in_weekly_report: {
          title: '每周报告内部百分比',
          value: '15'
        },
        percent_in_monthly_report: {
          title: '每月报告内部百分比',
          value: '30'
        },
        percent_in_summary: {
          title: '总结报告内部百分比',
          value: '50'
        },
        deadline: {
          title: '截止日期',
          value: '2024-05-01'
        }
      },
      columns: [
        {
          title: '类型',
          key: 'type'
        },
        {
          title: '名称',
          key: 'name',
          fixed: 'left'
        },
        {
          title: '专业方向',
          key: 'majorOrieId'
        },
        {
          title: '年级',
          key: 'grade'
        },
        {
          title: '开始日期',
          key: 'startD',
          formatter: this.colCreateTimeFormatter
        },
        {
          title: '结束日期',
          key: 'endD',
          formatter: this.colCreateTimeFormatter
        },
        {
          title: '课时',
          key: 'classHour'
        },
        {
          title: '学分',
          key: 'credit'
        },
        // {
        //   title: '内部百分比',
        //   key: 'percentIn'
        // },
        // {
        //   title: '外部百分比',
        //   key: 'percentEx'
        // },
        {
          title: '内容',
          key: 'content',
          showOverflowTooltip: true
        },
        {
          title: '目标',
          key: 'objective',
          showOverflowTooltip: true
        },
        {
          title: '要求',
          key: 'demand',
          showOverflowTooltip: true
        },
        {
          title: '分数计算类型',
          key: 'scoreCalType'
        },
        {
          title: '每日报告内部百分比',
          key: 'percentInDailyReport'
        },
        {
          title: '每周报告内部百分比',
          key: 'percentInWeeklyReport'
        },
        {
          title: '每月报告内部百分比',
          key: 'percentInMonthlyReport'
        },
        {
          title: '总结报告内部百分比',
          key: 'percentInSummary'
        },
        {
          title: '创建者',
          key: '_creator',
          showOverflowTooltip: true,
          fixed: 'left'
        },
        {
          title: '截止日期',
          key: 'deadline',
          formatter: this.colCreateTimeFormatter
        }
      ],
      data: [
        {
          id: 0,
          type: 0,
          name: 'string',
          major_orie_id: 0,
          grade: 0,
          start_d: null,
          end_d: null,
          class_hour: 0,
          credit: 0,
          percent_in: 0,
          percent_ex: 0,
          content: 'string',
          objective: 'string',
          demand: 'string',
          score_cal_type: 0,
          percent_in_daily_report: 0,
          percent_in_weekly_report: 0,
          percent_in_monthly_report: 0,
          percent_in_summary: 0,
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
  },
  methods: {
    async fetchData () {
      const pageAndMode = {
        current: this.pagination.currentPage,
        limit: this.pagination.pageSize
      }
      this.loading = true
      // eslint-disable-next-line no-unused-vars
      let res = null
      // AUTH START取
      res = await this.$api.FETCH_ALL_PLANS(pageAndMode)
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
      this.data = res.plan
      this.originalData = res.plan
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
