<template>
  <div class="leader-add">
    <el-form
      ref="form"
      :model="form"
      label-width="100px"
    >
      <el-form-item label="发薪号">
        <el-input
          v-model="form.scorringcode"
          @input="selectUser"
        ></el-input>
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="form.scorringname"></el-input>
      </el-form-item>
      <el-form-item label="科室">
        <el-input v-model="form.department"></el-input>
      </el-form-item>
      <el-form-item label="主任手机号">
        <el-input v-model="form.leaderphone"></el-input>
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="form.remarks"></el-input>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { verifyUser } from "@/api/questionnaire/index";
import qs from "qs";
export default {
  data() {
    return {
      form: {},
      isCode: false
    };
  },
  props: ["detailData"],
  created() {
    this.form = this.detailData;
    if (this.form.scorringcode) {
      this.isCode = true;
    } else {
      this.isCode = false;
    }
  },
  methods: {
    //根据id查询用户
    selectUser() {
      let data = {
        uId: this.form.scorringcode
      };
      new Promise((response, reject) => {
        verifyUser(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.form.scorringname = response.data.data.username;
              this.form.leaderphone = response.data.data.mobile;
              // this.form.department = response.data.data.u_check_department;
            } else if (response.data.code == 1) {
            } else {
              this.$message.error(response.data.msg);
            }
          })
          .catch(error => {
            reject(error);
          });
      });
    }
  },
  watch: {
    detailData(val) {
      this.form = this.detailData;
      if (this.form.scorringcode) {
        this.isCode = true;
      } else {
        this.isCode = false;
      }
    }
  }
};
</script>
<style lang="scss" scoped>
</style>
<style>
.leader-add .el-form-item__label {
  text-align: left;
}
</style>