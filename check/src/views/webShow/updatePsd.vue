<template>
  <div class="index">
    <van-row class="content">
      <van-cell-group>
        <van-field
          v-model="oldPsd"
          type="password"
          clearable
          label="旧密码"
          placeholder="请输入旧密码"
        />
        <van-field
          v-model="newPsd"
          type="password"
          clearable
          label="新密码"
          placeholder="请输入新密码"
        />
        <van-field
          v-model="confirmPsd"
          type="password"
          clearable
          label="确认新密码"
          placeholder="请再次输入新密码"
        />
      </van-cell-group>
      <van-col :span="24">
        <van-button
          size="large"
          type="primary"
          @click="onClickRight"
          :loading="submitLoading"
          loading-text="修改中..."
        >保存修改</van-button>
      </van-col>
    </van-row>
  </div>
</template>
<script>
import { updateUserPassword } from "@/api/mobile/check";
import qs from "qs";
export default {
  data() {
    return {
      oldPsd:"",
      newPsd: "",
      confirmPsd: "",
      submitLoading:false,
    };
  },
  created() {},

  methods: {
    onClickRight() {
      if (this.newPsd !== this.confirmPsd) {
        this.$toast("两次密码不一致");
        return;
      }
      let data = {
        usercode: this.$route.query.usercode,
        password: this.oldPsd,
        newpassword: this.newPsd,
        respassword: this.confirmPsd,
      };
      this.submitLoading = true;
      return new Promise((response, reject) => {
        updateUserPassword(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.$toast.success(response.data.msg);
              this.$router.back(-1);
            } else {
              this.$toast.fail(response.data.msg);
            }
            this.submitLoading = false;
          })
          .catch(error => {
            reject(error);
          });
      });
    }
  }
};
</script>
<style scoped lang="scss">
.index {
  .content {
    .van-button {
      width: 40%;
      margin: 30px 30%;
    }
  }
}
</style>
