<template>
  <div
    class="nav-bar aside"
    id="leftlabel"
  >
    <div class="ui-sidebar">
      <div class="return-wrapper" @click="home">
        <i class="icon iconfont icon-xiazai44"></i>
        <p>我的首页</p>
      </div>
      <div
        class="nav-items"
        :class="path == '/detail/relationship' ? 'clicked' : ''"
        @click="changePage(1)"
      >
        <i class="icon iconfont icon-zuzhijiagou"></i>
        <p>评价关系</p>
      </div>
      <!-- <div
        class="nav-items"
        :class="path == '/detail/questionnaire' ? 'clicked' : ''"
        @click="changePage(2)"
      >
        <i class="icon iconfont icon-shijuan"></i>
        <p>测评考核</p>
      </div> -->
      <div
        class="nav-items"
        :class="path == '/detail/assessmentReport' ? 'clicked' : ''"
        @click="changePage(3)"
      >
        <i class="icon iconfont icon-baogao"></i>
        <p>评估报告</p>
      </div>
    </div>
    <!--<div class="ui-sidebottom">
      <ul>
        <li alt="帮助中心">
          <a href="#" title="帮助中心">
            <div class="icon-box">
              <i class="icon iconfont icon-bangzhu"></i>
              <p>帮助中心</p>
            </div>
          </a>
        </li>
      </ul>
    </div>-->
  </div>
</template>

<script>
export default {
  data() {
    return {
      path: ""
    };
  },
  components: {},
  created() {
    this.path = this.$route.matched[1]['path'];
  },

  methods: {
    login() {},
    changePage(num) {
      let tempPath;
      if (num == 1) {
        tempPath = "/detail/relationship";
      } else if (num == 2) {
        tempPath = "/detail/questionnaire";
      } else if (num == 3) {
        tempPath = "/detail/assessmentReport";
      }
      this.$router.push({
        path: tempPath,
        query: {}
      });
    },
    home(){
        this.$router.push({
            path: '/home',
            query: {
            }
        })
    }
  },
  watch:{
    '$route'(to,from) {
      this.path = to.matched[1]['path'];
    }
  }
};
</script>


<style lang="scss" scoped>
.nav-bar {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  z-index: 1;
  width: 100px;
  min-height: 615px;
  background-color: #0e2b47;
}

.nav-bar .return-wrapper {
  position: relative;
  display: block;
  width: 100px;
  text-align: center;
  background-color: #164370;
  height: 70px;
  color: #fff;
  padding-top: 8px;
  cursor: pointer;
  i{
    font-size:20px;
    color: #afb1b3;
  }
  p{
    color: #afb1b3;
  }
}

.nav-bar .nav-items {
  width: 100%;
  cursor: pointer;
  background-color: #0e2b47;
  height: 100px;
  font-size: 14px;
  color: #afb1b3;
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: center;
  i{
    font-size:36px;
  }
}
.nav-bar .nav-items:hover {
  background: #164370;
}
.nav-bar .nav-items.clicked {
  background: #409EFF;
  color: #fff;
}
.nav-bar .ui-sidebottom {
  position: absolute;
  bottom: 15px;
  border-top: 1px solid #1e3d66;
  width: 100%;
  left: 0;
}
.nav-bar .ui-sidebottom .icon-box {
  text-align: center;
  height: 75px;
}
.nav-bar .ui-sidebottom a {
  color: #afb1b3;
  font-size: 12px;
}
.nav-bar .ui-sidebottom a:hover {
  color: #1ea0fa;
}
.nav-bar .ui-sidebottom ul li:first-child {
  margin-top: 20px;
}
</style>
