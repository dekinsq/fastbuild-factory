<template>
  <div>
    <div>
      <span class="header">应用列表</span>
      <a-button type="primary" style="float: right; width: 8rem;" @click="visible = true" plain>应用选择</a-button>
    </div>
    <a-divider></a-divider>
    <div>
      <AppList v-if="chooseAppList && chooseAppList.length > 0" style="margin-top: -24px;" dataScope="choose"></AppList>
      <a-empty v-else description="尚未选择应用"></a-empty>
    </div>
    <a-modal title="应用选择列表" :visible.sync="visible" @cancel="visible = false" :footer="null" width="60rem" :maskClosable="false">
      <AppList style="margin-top: -30px;" dataScope="all"></AppList>
    </a-modal>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import AppList from './AppList.vue'

export default {
  components: {
    AppList
  },
  computed: {
    ...mapState({
      chooseAppList: state => state.factory.config.appList,
    })
  },
  data () {
    return {
      visible: false
    }
  },
  methods: {}
}
</script>

<style lang="less" scoped>
.header {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}
</style>