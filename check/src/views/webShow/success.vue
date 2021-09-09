<template>
  <div class="success">
    <div class="icon">
      <i class="el-icon-check"></i>
    </div>
    <p>考核提交成功<br />武汉市精神卫生中心感谢您的参与<br />您可以直接关闭此页面</p>
  </div>
</template>

<script>
export default {
  data() {
    return {};
  },
  components: {},
  mounted() {},
  created() {},

  methods: {}
};
</script>


<style lang="scss" scoped>
.success {
  text-align: center;
  padding-top: 6rem;

  .icon {
    width: 100px;
    height: 100px;
    margin: 0 auto;
    background: #409EFF;
    border-radius: 50%;
    line-height: 100px;
    text-align: center;
    margin-bottom: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
    i {
      font-size: 2.6rem;
      color: #fff;
    }
  }
}
</style>
