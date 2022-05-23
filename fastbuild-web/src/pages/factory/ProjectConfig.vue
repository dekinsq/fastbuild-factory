<template>
  <div class="project-config">
    <div class="project-language-config">
      <a-form :form="form">
        <a-row :gutter="16">
          <a-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12">
            <a-form-item label="Web">
              <div class="item-left">
                <a-select v-model="form.webFramework" size="large" style="width: 100%;" @select="webFrameworkSelect">
                  <a-select-option value="vue2">Vue2</a-select-option>
                  <a-select-option value="vue3">Vue3</a-select-option>
                  <a-select-option value="react">React</a-select-option>
                  <a-select-option value="thymeleaf">Thymeleaf</a-select-option>
                </a-select>
              </div>
              <div class="item-right">
                <a-select v-model="form.webUI" size="large" style="width: 100%;" @select="webUISelect">
                  <a-select-option
                    v-for="item in webUiList[form.webFramework]"
                    :key="item.value"
                    :value="item.value">
                    {{item.label}}
                  </a-select-option>
                </a-select>
              </div>
            </a-form-item>
          </a-col>
          <a-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12">
            <a-form-item label="服务端">
              <a-radio-group v-model="form.serverMode" size="large">
                <a-radio value="single" border>单应用</a-radio>
                <a-radio value="cloud" border>微服务</a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12">
            <a-form-item label="移动端" size="large">
              <a-radio-group v-model="form.mobileFramework">
                <a-radio value="uniApp" border>UniApp</a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12">
            <a-form-item label="数据库">
              <div class="item-left">
                <a-select v-model="form.database" size="large" placeholder="请选择数据库" @change="dbChanged">
                  <a-select-option value="mysql">MySQL</a-select-option>
                  <a-select-option value="oracle">Oracle</a-select-option>
                  <a-select-option value="sqlserver">SQLServer</a-select-option>
                </a-select>
              </div>
              <div class="item-right">
                <a-select v-model="form.databaseVersion" size="large" placeholder="请选择版本">
                  <a-select-option
                    v-for="item in dbVersion[form.database]"
                    :key="item.value"
                    :value="item.value">
                    {{item.label}}
                  </a-select-option>
                </a-select>
              </div>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div class="project-base-config">
      <div class="header">项目配置</div>
      <a-divider></a-divider>
      <a-form :model="form" :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
        <a-form-item label="项目标题">
          <a-input v-model="form.projectTitle" size="large" placeholder="请填写30个字以内的项目标题。"></a-input>
        </a-form-item>
        <a-form-item label="工程名称">
          <a-input v-model="form.projectName" size="large" placeholder="可填写英文字母或下划线，长度不能超过30个字符。"></a-input>
        </a-form-item>
        <a-form-item label="包名前缀">
          <a-input v-model="form.packagePrefix" size="large" placeholder="工程包名前缀，例如：com.fastbuild"></a-input>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';

export default {
  computed: {
    ...mapState({
      form: state => state.factory.config
    })
  },
  data () {
    return {
      dbVersion: {
        mysql: [{ value: '5.6', label: '5.6+' }],
        oracle: [{ value: '10g', label: '10g+' }],
        sqlserver: [{ value: '2012', label: '2012+' }],
      },
      webUiList: {
        vue2: [{ value: 'element', label: 'Element UI' }, { value: 'antd', label: 'Ant Desigin' }],
        vue3: [{ value: 'element', label: 'Element UI' }, { value: 'antd', label: 'Ant Desigin' }],
        react: [{ value: 'antd', label: 'Ant Desigin' }],
        thymeleaf: [{ value: 'bootstrap', label: 'Bootstrap UI' }],
      }
    }
  },
  methods: {
    webFrameworkSelect () {
      this.form.webUI = this.webUiList[this.form.webFramework][0].value;
    },
    webUISelect () {
    },
    dbChanged () {
      this.form.databaseVersion = this.dbVersion[this.form.database][0].value;
    },
  }
}
</script>

<style lang="less" scoped>
.project-language-config {
  /deep/.ant-form-item-label > label {
    font-size: 18px !important;
    font-weight: 600;
    color: #303133;
  }
}
.project-base-config {
  /deep/ .ant-form-item-label > label {
    font-size: 16px;
    font-weight: 500;
    color: #303133;
  }
  /deep/ .ant-input {
    border-color: #40a9ff;
    border-right-width: 1px !important;
  }
}
.header {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 16px;
}
.item-left {
  width: 50%;
  padding-right: 8px;
  display: inline-block;
  /deep/ .ant-select-selection {
    border-color: #40a9ff;
    border-right-width: 1px !important;
  }
}
.item-right {
  width: 50%;
  padding-left: 8px;
  display: inline-block;
  /deep/ .ant-select-selection {
    border-color: #40a9ff;
    border-right-width: 1px !important;
  }
}
</style>