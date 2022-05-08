<template>
  <div>
    <a-space size="large">
      <a-tooltip>
        <template slot="title">
          联系我
        </template>
        <a-button shape="circle" size="large" icon="mail" @click="contactVisible = true" />
      </a-tooltip>
      <a-tooltip>
        <template slot="title">
          声明
        </template>
        <a-button shape="circle" size="large" icon="info" @click="infoVisible = true" />
      </a-tooltip>
    </a-space>
    <a-modal :visible.sync="contactVisible" @cancel="contactVisible = false" :footer="null">
      <h2>联系我</h2>
      <p>如您有任何宝贵意见或需求，可发送信息至以下邮箱。</p>
      <p>
        <a-space>
          <a-icon type="mail" theme="twoTone" />
          <span>fastbuild@163.com</span>
        </a-space>
      </p>
      <p style="text-align: center;">
        <a-button @click="contactVisible = false">关闭</a-button>
      </p>
    </a-modal>
    <a-modal :visible.sync="infoVisible" @cancel="infoVisible = false" :footer="null">
      <h2>声明</h2>
      <p>1. 本站下载的源代码没有特殊标注版权的，没有使用限制，可用于个人学习、私活，也可商用，无需另行告知。</p>
      <p>2. 因本站下载的部分源代码来源于开源网站，有版权使用限制的，会特殊说明。请遵守原作者开源协议。</p>
      <p style="text-align: center;">
        <a-button @click="infoVisible = false">关闭</a-button>
      </p>
    </a-modal>
  </div>
</template>

<script>
export default {
  data () {
    return {
      contactVisible: false,
      infoVisible: false
    }
  }
}
</script>