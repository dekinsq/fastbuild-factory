<template>
  <div style="background: #F2F6FC;">
    <div class="factory-content">
      <a-row :gutter="48">
        <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" style="padding-top: 24px;">
          <Logo />
        </a-col>
        <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" style="padding-top: 24px; text-align: right;">
          <Toolbar />
        </a-col>
      </a-row>
      <a-divider></a-divider>
      <a-row :gutter="48">
        <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" style="border-right: 1px solid #DCDFE6;">
          <ProjectConfig></ProjectConfig>
        </a-col>
        <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
          <AppContainer></AppContainer>
        </a-col>
      </a-row>
      <a-drawer
        v-if="visible"
        title="代码预览"
        :visible.sync="visible"
        placement="bottom"
        :mask="false"
        height="100vh"
        @close="visible = false">
        <CodeView @close="visible = false" @download="downloadDialog"></CodeView>
      </a-drawer>
    </div>
    <a-modal :visible.sync="downloadVisible" @cancel="downloadVisible = false" :footer="null" :maskClosable="false" :zIndex="9999">
      <Download v-if="downloadVisible" :progress="downloadProgress" @download="downloadFile"></Download>
    </a-modal>
    <div style="position: sticky; width: 100%; bottom: 0;">
      <div class="factory-footer">
        <div style="text-align: center; width: 100%;">
          <a-space size="large">
            <!-- <a-button type="primary" plain disabled>系统预览</a-button> -->
            <a-button type="primary" size="large" style="width: 8rem;" @click="downloadDialog">源码下载</a-button>
            <a-button type="primary" size="large" style="width: 8rem;" @click="previewDialog" plain>源码预览</a-button>
          </a-space>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Logo from '@/components/Logo'
import Toolbar from '@/components/Toolbar'
import ProjectConfig from './ProjectConfig.vue'
import AppContainer from './AppContainer.vue'
import CodeView from './CodeView.vue'
import Download from './Download.vue'

import { mapState } from 'vuex';
import { saveAs } from 'file-saver'

import { downloadApi } from '@/api/factory'
import device from '@/utils/device'

export default {
  components: {
    Logo,
    Toolbar,
    ProjectConfig,
    AppContainer,
    CodeView,
    Download
  },
  computed: {
    ...mapState({
      config: state => state.factory.config
    })
  },
  data () {
    return {
      visible: false,
      isMobile: device.isMobile(),
      downloadVisible: false,
      downloadProgress: 10
    }
  },
  methods: {
    previewDialog () {
      let valid = this.validator();
      if (valid) {
        this.visible = true;
      }
    },
    downloadDialog () {
      let valid = this.validator();
      if (valid) {
        this.downloadVisible = true;
        this.downloadFile();
      }
    },
    downloadFile () {
      this.downloadProgress = 10;
      downloadApi(this.config, (e) => {
        this.downloadProgress = parseInt((e.loaded / e.total) * 100);
      }).then(async (data) => {
        const blob = new Blob([data]);
        saveAs(blob, 'resource.zip')
      })
    },
    validator () {
      if (!this.config.projectTitle) {
        this.$message.error('请填写项目标题！');
        return false;
      }
      if (!this.config.projectName) {
        this.$message.error('请填写工程名称！');
        return false;
      }
      if (!this.config.packagePrefix) {
        this.$message.error('请填写包名前缀！');
        return false;
      }
      if (this.config.appList.length < 1) {
        this.$message.error('请至少选择一个应用！');
        return false;
      }
      return true;
    }
  }
}
</script>

<style lang="less" scoped>
.factory-content {
  margin: 0 auto;
  min-height: 100vh;
  max-width: 1385px;
  border-left: 1px solid #DCDFE6;
  border-right: 1px solid #DCDFE6;
  padding: 0 24px;
  background: white;
}
/deep/.ant-drawer.ant-drawer-bottom.ant-drawer-open.no-mask {
  max-width: 1383px;
  margin: 0 auto;
  position: inherit;
}
/deep/ .ant-drawer-title {
  color: #000;
  font-size: 18px;
  font-weight: 600;
}
.factory-footer {
  max-width: 1383px;
  margin: 0 auto;
  padding: 24px 0;
  background: #EBEEF5;
  .el-button {
    font-size: 18px !important;
    font-weight: 600;
  }
}
</style>