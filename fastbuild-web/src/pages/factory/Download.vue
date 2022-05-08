<template>
  <div class="factory-download">
    <div v-if="progress < 100">
      <h1>下载中，请稍后...</h1>
      <p><a-progress :percent="progress" status="active" /></p>
    </div>
    <div v-else>
      <h1>感谢下载</h1>
      <p>没有开始下载？请 <a-button type="link" @click="$emit('download')" style="padding: 0;">点击这里</a-button> 重试！</p>
    </div>
    <a-divider></a-divider>
    <a-row :gutter="24">
      <a-col :span="12">
        <a-card :body-style="{ padding: '24px 24px 0' }" shadow="hover">
          <img src="/static/wechat_pay.jpg" class="factory-download-image">
          <p style="margin-top: 8px; font-weight: 600;">微信</p>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card :body-style="{ padding: '24px 24px 0' }" shadow="hover">
          <img src="/static/ali_pay.jpg" class="factory-download-image">
          <p style="margin-top: 8px; font-weight: 600;">支付宝</p>
        </a-card>
      </a-col>
    </a-row>
    <p style="margin-top: 8px; font-weight: 600;">如果对您的工作与学习有所帮助，可捐助我们！谢谢！</p>
  </div>
</template>

<script>
export default {
  props: {
    progress: {
      type: Number,
      default: 10
    }
  }
}
</script>

<style lang="less" scoped>
.factory-download {
  text-align: center;
  color: #000;
}
.factory-download-image {
  width: 100%;
}
</style>