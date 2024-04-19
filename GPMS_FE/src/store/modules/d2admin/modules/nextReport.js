// 在 Vuex store 中
const state = {
  reports: [], // 所有报告的列表
  currentReportIndex: -1 // 当前报告的索引，初始值设为 -1 表示未选择
}

const mutations = {
  setReports (state, reports) {
    state.reports = reports
  },
  setCurrentReportIndex (state, index) {
    state.currentReportIndex = index
  }
}

const actions = {
  updateReports ({ commit }, reports) {
    commit('setReports', reports)
  },
  updateCurrentReportIndex ({ commit }, index) {
    commit('setCurrentReportIndex', index)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
