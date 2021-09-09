<template>
  <div class="question-container">
    <div class="question-content">
      <img
        src="../../assets/img/dream-seeker@2x.jpg"
        class="question-header"
      >
      <div class="question-list">
        <el-row class="edit">
          <el-col
            :span="24"
            class="right"
          >
            <el-col class="title">
              您的答卷已经提交，感谢您的参与！
            </el-col>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {};
  },
  beforeCreate() {
    document
      .querySelector("body")
      .setAttribute(
        "style",
        "background-color: #f3f6fa;background-image: url(../../assets/img/dream-seeker-bg@2x.jpg);background-repeat: repeat-x; background-size: 1px 300px;"
      );
  },
  created() {},
  methods: {}
};
</script>
<style lang="scss" scoped>
@media screen and (max-width: 640px) {
  .question-container {
    .question-content {
      width: 100% !important;
      font-size: 0;
    }
    .question-header {
      width: 100% !important;
      font-size: 0;
    }
  }
}
.question-container {
  background-color: #f3f6fa;
  background-image: url(../../assets/img/dream-seeker-bg@2x.jpg);
  background-repeat: repeat-x;
  background-size: 1px 300px;
  .question-content {
    width: 920px;
    margin: 0 auto;
    font-size: 0;
  }
  .question-header {
    width: 920px;
    font-size: 0;
  }
  .question-list {
    box-shadow: 0px 1px 6px 0px rgba(205, 220, 245, 1);
    font-size: 14px;
    background: #fff;
  }
}

.edit {
  margin-top: 10px;
  border-radius: 2px;
  border: 1px solid rgba(217, 217, 217, 1);
  padding-bottom: 5px;
  margin: 0;
  overflow: auto;
  background: #ffffff;
  box-shadow: 0px 2px 5px 0px #d9d9d9;
  position: relative;
  .title {
    text-align: center;
    font-size: 16px;
    font-weight: bold;
    padding: 50px 0 100px 0;
    color:#595959;
  }
}
</style>