<template>
  <div class="view-page">
    <el-row class="view-header">
      <div class="header-content">
        <div class="left-hc">
          <span style="color:#FF5500">提示： </span>
          此为预览页面，不能参与作答！
        </div>
        <div class="center-hc">
          <div
            class="center-item"
            @click="choice(1)"
            :class="active == 1 ? 'active' : ''"
          >
            <i class="iconfont icon-shouji"></i>
            <span>手机预览</span>
          </div>
          <div
            class="center-item"
            @click="choice(2)"
            :class="active == 2 ? 'active' : ''"
          >
            <i class="iconfont icon-icon-test2"></i>
            <span>电脑预览</span>
          </div>
          <!-- <div
            class="center-item"
            @click="choice(3)"
            :class="active == 3 ? 'active' : ''"
          >
            <i class="iconfont icon-guanbi"></i>
            <span>关闭预览</span>
          </div> -->
        </div>
      </div>
    </el-row>
    <el-row
      class="view-content"
      v-show="active == 1"
    >
      <el-col
        :span='24'
        class="content"
      >
        <div class="phone">
          <div class="phone-content">
            <mobile ref="mobile"></mobile>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row
      class="view-content"
      v-show="active == 2"
    >
      <el-col
        :span='24'
        class="content"
      >
        <computers ref="computers"></computers>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import mobile from "./preview/mobile";
import computers from "./preview/pc";
export default {
  data() {
    return {
      active: 1,
      id: "",
    };
  },
  components: {
    mobile,
    computers,
  },
  created() {
    this.id = this.$route.query.id;
  },
  mounted() {
    this.$refs.mobile.id = this.id;
    this.$refs.mobile.getDetail();
    this.$refs.computers.id = this.id;
    this.$refs.computers.getDetail();
    // this.$nextTick(() => {
    //   this.$refs.computers.id = this.id;
    //   this.$refs.computers.getDetail();
    // });
  },
  methods: {
    open() {
      this.show = true;
    },
    close() {
      this.show = false;
    },
    choice(val) {
      this.active = val;
      if (val == 1) {
      } else if (val == 2) {
      } else if (val == 3) {
        window.location.href = "about:blank";
        window.opener.location.reload();
        window.close();
      }
    },
  },
  watch: {},
};
</script>

<style lang="scss" scoped>
.view-header {
  height: 70px;
  box-shadow: 0px 1px 0px 0px rgba(232, 232, 232, 1);
  background-color: #fff;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 99;
  .header-content {
    width: 1024px;
    margin: 0 auto;
    position: relative;
    .left-hc {
      height: 70px;
      line-height: 70px;
      position: absolute;
      left: 0;
      top: 0;
      color: #888888;
      font-size: 14px;
    }
    .center-hc {
      width: 264px;
      height: 70px;
      margin: 0 auto;
      display: flex;
      justify-content: center;
      align-items: center;
      .center-item {
        width: 88px;
        height: 70px;
        box-shadow: -1px 0px 0px 0px rgba(240, 240, 240, 1),
          1px 0px 0px 0px rgba(240, 240, 240, 1);
        display: flex;
        justify-content: center;
        align-items: center;
        flex-wrap: wrap;
        margin-left: 1px;
        box-sizing: border-box;
        cursor: pointer;
        .iconfont {
          font-size: 28px;
        }
        span {
          display: block;
          width: 100%;
          text-align: center;
          margin-top: -20px;
          font-size: 12px;
        }
      }
      .active {
        background: #409eff;
        color: #fff;
      }
    }
  }
}
.view-page {
  .view-btn {
    position: fixed;
    right: 10%;
    top: 100px;
    z-index: 2;
  }
  .view-content {
    .content {
      .phone {
        width: 450px;
        height: 800px;
        background: url("../../assets/img/phone-bg.png") no-repeat 0% 0%;
        background-size: 100% 100%;
        margin: 120px auto 0 auto;
        .phone-content {
          position: relative;
          width: 328px;
          height: 651px;
          top: 42px;
          left: 61px;
          overflow-y: auto;
        }
      }
      .close-btn {
        .el-button {
          position: fixed;
          left: 50%;
          margin-left: 220px;
          top: 100px;
          z-index: 99;
        }
      }
    }
  }
}
</style>

