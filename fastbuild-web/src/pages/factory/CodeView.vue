<template>
  <div style="padding: 0 24px;">
    <a-row :gutter="24">
      <a-col :lg="6">
        <div class="code-item" :style="{ overflow: 'auto', height: isMobile ? '40vh' : 'calc(100vh - 190px)'}">
          <a-skeleton v-if="loading" :rows="6" animated />
          <a-directory-tree v-else :tree-data="treeData" @select="handleNodeClick"></a-directory-tree>
        </div>
      </a-col>
      <a-col :lg="18">
        <div class="code-item">
          <codemirror v-if="code" v-model="code" :options="cmOptions" style="height: 100%;"></codemirror>
          <a-empty v-else description="请选择文件" style="height: 100%; margin-top: 5rem;"></a-empty>
        </div>
      </a-col>
    </a-row>
    <div
      :style="{
        position: 'absolute',
        bottom: 0,
        width: '100%',
        borderTop: '1px solid #e9e9e9',
        padding: '24px 16px',
        marginLeft: '-48px',
        background: '#EBEEF5',
        textAlign: 'center',
        zIndex: 1,
      }"
    >
      <a-space size="large">
        <a-button size="large" :style="{ marginRight: '8px' }" @click="$emit('close')">关闭</a-button>
        <a-button size="large" type="primary" @click="$emit('download')">源码下载</a-button>
      </a-space>
    </div>
  </div>
</template>

<script>
import device from '@/utils/device'

import { codemirror } from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'
import 'codemirror/mode/xml/xml.js'
import 'codemirror/mode/yaml/yaml.js'
import 'codemirror/mode/sql/sql.js'
import 'codemirror/mode/vue/vue.js'
import 'codemirror/mode/javascript/javascript.js'
import 'codemirror/mode/css/css.js'
import 'codemirror/mode/htmlmixed/htmlmixed.js'

import { mapState } from 'vuex';
import { getFileTreeApi, getFileApi } from '@/api/factory'

export default {
  components: {
    codemirror,
  },
  computed: {
    ...mapState({
      config: state => state.factory.config
    })
  },
  data () {
    return {
      isMobile: device.isMobile(),
      code: null,
      language: 'js',
      loading: true,
      treeData: [],
      cmOptions: {
        mode: 'text/x-sql',
        theme: 'default',
        line: true,
        readOnly: true,
        lineNumbers: true,
        lineWrapping: true,
        tabSize: 2,
      }
    }
  },
  mounted () {
    this.loadFileTree();
  },
  methods: {
    loadFileTree () {
      getFileTreeApi(this.config).then(res => {
        this.loading = false;
        if (res.code == 200) {
          this.treeData = res.data;
        }
      });
    },
    handleNodeClick (data) {
      getFileApi({id: encodeURI(data[0])}).then(res => {
        if (res.code == 200) {
          this.cmOptions.mode =  this.getLanguage(res.data.suffix);
          this.code = res.data.content;
        }
      })
    },
    getLanguage (suffix) {
      switch(suffix) {
        case 'vue': return 'vue';
        case 'js': return 'javascript';
        case 'properties':
        case 'yml': return 'yaml';
        case 'java': return 'javascript';
        default: return suffix;
      }
    }
  }
}
</script>

<style lang="less" scoped>
.code-item {
  border: 1px solid #1890ff;
  border-radius: 4px;
  padding: 8px;
  height: calc(100vh - 190px);
  margin-bottom: 24px;
  /deep/.CodeMirror {
    height: 100%;
  }
}
.factory-footer {
  background: #EBEEF5;
}
</style>