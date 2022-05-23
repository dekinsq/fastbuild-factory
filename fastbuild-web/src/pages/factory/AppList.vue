<template>
    <a-table :data-source="dataScope == 'choose' ? chooseAppList : appList" :pagination="false" :showHeader="false" rowKey="keyId">
      <a-table-column key="title" title="Action">
        <template slot-scope="text, record">
          <div :class="record.disable ? ['app-item', 'app-item-disabled'] : ['app-item']">
            <h3 class="title">
              {{record.title}} 
              <a-tag color="blue" size="small">
                <span v-if="record.appType == 'basic-platform'">基础平台</span>
              </a-tag>
            </h3>
            <div class="word-break-2" :title="record.desc">{{record.desc}}</div>
            <p v-if="record.disable" class="error">
              <span>{{record.assertMsg}}</span>
            </p>
          </div>
        </template>
      </a-table-column>
      <a-table-column key="keyId">
        <template slot-scope="text, record">
          <p><a-button @click="handleView(record)">查看详情</a-button></p>
          <a-button v-if="dataScope == 'choose'" type="danger" @click="handleRemove(record)" :disabled="record.disable">移除应用</a-button>
          <a-button v-else type="primary" @click="handleAdd(record)" :disabled="record.disable" plain>选择应用</a-button>
        </template>
      </a-table-column>
    </a-table>
</template>

<script>
import { mapState } from 'vuex';
import { validRules } from './validApp';

export default {
  props: ['dataScope'],
  computed: {
    ...mapState({
      appList (state) {
        return state.factory.appList.filter((app, index) => {
          app.keyId = new Date().getTime() + '-' + index;
          let valid = validRules(app);
          app = Object.assign(app, valid);
          return state.factory.config.appList.findIndex(c => {
            return app.appId == c.appId;
          }) < 0;
        });
      },
      chooseAppList: state => state.factory.config.appList,
    })
  },
  methods: {
    handleAdd(row) {
      this.chooseAppList.push(row);
    },
    handleRemove(row) {
      let index = this.chooseAppList.findIndex(d => { return d.appId == row.appId });
      this.chooseAppList.splice(index, 1);
    },
    handleView (row) {
      window.open(row.link);
    }
  },
}
</script>

<style lang="less" scoped>
.app-item {
  color: #303133;
  .title {
    font-size: 18px;
    font-weight: 600;
  }
  .word-break-2 {
    word-break: break-all;
    text-overflow: ellipsis;
    overflow: hidden;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }
  .error {
    color: #F56C6C;
  }
}
.app-item-disabled {
  color: rgba(0, 0, 0, 0.4) !important;
  cursor: not-allowed;
}
</style>