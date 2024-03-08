(window.webpackJsonp=window.webpackJsonp||[]).push([[75],{450:function(t,a,n){"use strict";n.r(a);var s=n(42),e=Object(s.a)({},(function(){var t=this,a=t.$createElement,n=t._self._c||a;return n("ContentSlotsDistributor",{attrs:{"slot-key":t.$parent.slotKey}},[n("h1",{attrs:{id:"事件监听"}},[n("a",{staticClass:"header-anchor",attrs:{href:"#事件监听"}},[t._v("#")]),t._v(" 事件监听")]),t._v(" "),n("h2",{attrs:{id:"dialog-open"}},[n("a",{staticClass:"header-anchor",attrs:{href:"#dialog-open"}},[t._v("#")]),t._v(" dialog-open")]),t._v(" "),n("p",[n("code",[t._v('@dialog-open="handleDialogOpen"')]),n("br"),t._v("\n编辑对话框打开事件，可以在此事件中初始化form表单值")]),t._v(" "),n("h2",{attrs:{id:"row-edit"}},[n("a",{staticClass:"header-anchor",attrs:{href:"#row-edit"}},[t._v("#")]),t._v(" row-edit")]),t._v(" "),n("p",[n("code",[t._v('@row-edit="handleRowEdit"')]),n("br"),t._v("\n编辑对话框确认按钮点击事件")]),t._v(" "),n("h2",{attrs:{id:"row-add"}},[n("a",{staticClass:"header-anchor",attrs:{href:"#row-add"}},[t._v("#")]),t._v(" row-add")]),t._v(" "),n("p",[n("code",[t._v('@row-add="handleRowAdd"')]),n("br"),t._v("\n添加对话框确认按钮点击事件")]),t._v(" "),n("h2",{attrs:{id:"row-remove"}},[n("a",{staticClass:"header-anchor",attrs:{href:"#row-remove"}},[t._v("#")]),t._v(" row-remove")]),t._v(" "),n("p",[n("code",[t._v('@row-remove="handleRowRemove"')]),n("br"),t._v("\n删除对话框确认点击事件")]),t._v(" "),n("h2",{attrs:{id:"dialog-cancel"}},[n("a",{staticClass:"header-anchor",attrs:{href:"#dialog-cancel"}},[t._v("#")]),t._v(" dialog-cancel")]),t._v(" "),n("p",[n("code",[t._v('@dialog-cancel="handleDialogCancel"')]),n("br"),t._v("\n对话框遮罩层点击关闭事件，覆盖此方法，可以去掉遮罩层点击关闭对话框功能。")]),t._v(" "),n("h2",{attrs:{id:"form-data-change"}},[n("a",{staticClass:"header-anchor",attrs:{href:"#form-data-change"}},[t._v("#")]),t._v(" form-data-change")]),t._v(" "),n("p",[n("code",[t._v('@form-data-change="handleFormDataChange"')]),n("br"),t._v("\n表单值更新事件")]),t._v(" "),n("h2",{attrs:{id:"form-component-ready"}},[n("a",{staticClass:"header-anchor",attrs:{href:"#form-component-ready"}},[t._v("#")]),t._v(" form-component-ready")]),t._v(" "),n("p",[n("code",[t._v('@form-component-ready="handleFormComponentReady"')]),n("br"),t._v("\n表单组件初始化完成事件（部分组件会发送此事件，例如富文本编辑器）")]),t._v(" "),n("h2",{attrs:{id:"自定义事件监听"}},[n("a",{staticClass:"header-anchor",attrs:{href:"#自定义事件监听"}},[t._v("#")]),t._v(" 自定义事件监听")]),t._v(" "),n("p",[t._v("当表单组件发送除以上事件之外的事件时,可以通过以下方法监听")]),t._v(" "),n("div",{staticClass:"language-js extra-class"},[n("pre",{pre:!0,attrs:{class:"language-js"}},[n("code",[n("span",{pre:!0,attrs:{class:"token keyword"}},[t._v("let")]),t._v(" crudOption"),n("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=")]),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("\n  columns"),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("[")]),t._v(" \n    "),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v(" \n      title "),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v(" "),n("span",{pre:!0,attrs:{class:"token string"}},[t._v("'内容'")]),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("\n      key "),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v(" "),n("span",{pre:!0,attrs:{class:"token string"}},[t._v("'content'")]),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("\n      type"),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),n("span",{pre:!0,attrs:{class:"token string"}},[t._v("'editor-quill'")]),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("\n      form"),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),t._v(" "),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v(" \n        component"),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("\n          events"),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("\n            "),n("span",{pre:!0,attrs:{class:"token string"}},[t._v("'selection-change'")]),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(":")]),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("(")]),n("span",{pre:!0,attrs:{class:"token parameter"}},[n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("vm"),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(",")]),t._v("event"),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")])]),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v(")")]),n("span",{pre:!0,attrs:{class:"token operator"}},[t._v("=>")]),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("{")]),t._v("\n              "),n("span",{pre:!0,attrs:{class:"token comment"}},[t._v("//监听选择事件")]),t._v("\n            "),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),t._v(" \n          "),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),t._v("   \n        "),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),t._v("\n      "),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),t._v("\n    "),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),t._v("\n  "),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("]")]),t._v("\n"),n("span",{pre:!0,attrs:{class:"token punctuation"}},[t._v("}")]),t._v("\n\n")])])])])}),[],!1,null,null,null);a.default=e.exports}}]);