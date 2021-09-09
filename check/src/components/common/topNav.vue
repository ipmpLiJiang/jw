<template>
  <div
    class="inside-head clearfix"
    style=""
  >
    <div class="nav-left">
      <el-select
        v-model="value"
        placeholder="请选择"
        class="name-select"
      >
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
      <el-button
        type="primary"
        plain
        class="preview"
        @click="pcShow"
      >预览问卷</el-button>
    </div>
    <div
      class="nav-show clearfix"
      id="toplabel"
    >
      <div
        class="nav-box clearfix"
        :class="path == '/detail/relationship' ? 'd-block' : ''"
      >
      </div>
      <div
        class="nav-box clearfix"
        :class="path == '/detail/questionnaire' ? 'd-block' : ''"
      >
        <!-- <div class="nav-items pull-left clicked">
          <label class="icon design-icon"></label><br>
          <span>设计向导</span>
        </div> -->
        <!-- <div class="nav-items pull-left">
          <label class="icon edit-icon"></label><br>
          <span>编辑问卷</span>
        </div>
        <div class="nav-items pull-left">
          <label class="icon grant-icon"></label><br>
          <span>发放问卷</span>
        </div>
        <div class="nav-items pull-left">
          <label class="icon set-icon"></label><br>
          <span>问卷设置</span>
        </div>
        <div class="nav-items pull-left">
          <label class="icon surface-icon"></label><br>
          <span>问卷外观</span>
        </div> -->
      </div>
      <div
        class="nav-box clearfix"
        :class="path == '/detail/assessmentReport' ? 'd-block' : ''"
      >
        <!-- <div class="nav-items pull-left Lengthened-100 clicked">
          <label class="icon overall__report-360-icon"></label><br>
          <span>360度评估报告</span>
        </div> -->
        <!-- <div class="nav-items pull-left Lengthened-100">
          <label class="icon implant-icon"></label><br>
          <span>原始答卷数据</span>
        </div>
        <div class="nav-items pull-left Lengthened-100">
          <label class="icon wechat-icon"></label><br>
          <span>报告详细数据</span>
        </div> -->
      </div>
    </div>
    <div class="view" v-if="view">
      <div class="bg"></div>
      <div style="position: absolute;
    top: 0px;
    left: 0px;
width: 100%;height:100%;">
        <pcView></pcView>
        <el-button style="    position: absolute;
        top: 100px;
    left: 450px;" @click="hideView">关闭预览</el-button>
      </div>

    </div>
  </div>
</template>

<script>
    import pcView from "@/views/webShow/pcView";
export default {
  data() {
    return {
        view:false,
      options: [
        {
          value: "1",
          label: "测试考核1"
        },
        {
          value: "2",
          label: "测试考核2"
        },
        {
          value: "3",
          label: "测试考核3"
        }
      ],
      value: "测试考核1",
      path: ""
    };
  },
  components: {
      pcView,
  },
  created() {
    // this.path = this.$route.path;
    this.path = this.$route.matched[1]['path'];
  },

  methods: {
    login() {
      this.$router.push({
        path: "/home",
        query: {}
      });
    },
    pcShow() {
        this.view = true
    },
    hideView() {
        this.view = false
    },
  },
  watch:{
    '$route'(to,from) {
      this.path = to.matched[1]['path'];
    }
  }
};
</script>


<style lang="scss" scoped>
.icon {
  margin: 0 6px 0 0;
  display: inline-block;
  vertical-align: middle;
  width: 24px;
  height: 24px;
  margin: 12px auto 8px;
}

.inside-head {
  position: fixed;
  top: 0;
  left: 100px;
  right: 0;
  z-index: 5;
  min-width: 864px;
  height: 70px;
  padding: 0 30px;
  background-color: #fff;
  -moz-box-shadow: 0 0 15px rgba(29, 50, 71, 0.15);
  -webkit-box-shadow: 0 0 15px rgba(29, 50, 71, 0.15);
  box-shadow: 0 0 15px rgba(29, 50, 71, 0.15);
  text-align: center;
}
.name-select {
  display: inline-block;
}
.preview {
  margin-left: 10px;
}
.nav-show {
  display: inline-block;
  *display: inline;
  .d-block {
    display: block;
  }
}
.nav-box {
  display: none;
}
.nav-show .nav-items {
  width: 88px;
  height: 70px;
  text-align: center;
  cursor: pointer;
  color: #676466;
  font-size: 12px;
  line-height: 1;
  display: inline-block;
  &.Lengthened-100 {
    width: 100px;
  }
}
.nav-show .nav-items:hover {
  background-color: #ebf0f5;
}
.nav-show .nav-items.clicked {
  background-color: #1ea0fa;
  color: #fff;
}
.nav-show .nav-items.clicked:hover {
  background-color: #1ea0fa;
  color: #fff;
}

.nav-left {
  display: inline-block;
  height: 70px;
  vertical-align: top;
  line-height: 70px;
  position: absolute;
  left: 30px;
}
  .view{
    position: fixed;
    width: 100%;
    height: 100%;
    left:0px;
    top:0px;
    .bg{
      width: 100%;
      height: 100%;
      left:0px;
      top:0px;
      background: rgba(0,0,0,0.5);
    }
  }
</style>
