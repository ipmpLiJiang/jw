<template>
  <div>

    <el-dialog
      title="添加/编辑人员"
      :visible.sync="dialogVisible"
      :before-close="cancel"
      width="50%"
    >
      <el-form
        label-position="left"
        inline
        label-width="80px"
        size='mini'
        class="table-expand"
      >
        <el-form-item
          label="工号"
          :rules="[
              { required: true},
            ]"
        >
          <el-input
            v-model="form.moneycard"
            placeholder="请输入工号"
            @input="selectUser"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="员工姓名"
          :rules="[
              { required: true},
            ]"
        >
          <el-input
            v-model="form.username"
            placeholder="请输入姓名"
          ></el-input>
        </el-form-item>

        <el-form-item
          label="手机号码"
          :rules="[
            ]"
        >
          <el-input
            v-model="form.mobile"
            placeholder="请输入手机号码"
          ></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input
            v-model="form.email"
            placeholder="请输入邮箱"
          ></el-input>
        </el-form-item>
        <el-form-item label="民族">
          <el-input
            v-model="form.nation"
            placeholder="请填写民族"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="角色权限"
          :rules="[
              { required: true},
            ]"
        >
          <el-select
            v-model="form.rolecode"
            placeholder="请选择"
          >
            <el-option
              v-for="item in roleOption"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="form.userstate"
            placeholder="请选择"
          >
            <el-option
              v-for="item in status"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="性别">
          <el-select
            v-model="form.sex"
            placeholder="请选择"
          >
            <el-option
              v-for="item in sex"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="群众关系">
          <el-select
            v-model="form.political"
            placeholder="请选择群众关系"
          >
            <el-option
              v-for="item in politics"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="学历">
          <el-select
            v-model="form.education"
            placeholder="请选择学历"
          >
            <el-option
              v-for="item in education"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属岗位">
          <PostList
            @childSelectDepartment="getSelectStation"
            :selectedOptions="tempstationcode"
          ></PostList>
        </el-form-item>
        <el-form-item label="所属支部">
          <BranchList
            @childSelectBranch="getSelectBranch"
            :selectedOptions="tempbranchcode"
          ></BranchList>
        </el-form-item>
        <el-form-item label="岗位类型">
          <el-select
            v-model="form.postType"
            clearable
            placeholder="请选择"
          >
            <el-option
              v-for="item in postType"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="党内身份">
          <el-select
            v-model="form.dbbk"
            clearable
            placeholder="请选择"
          >
            <el-option
              v-for="item in dbbk"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="cancel">取 消</el-button>
        <el-button
          type="primary"
          @click="addSubmit"
          v-loading="addDisabled"
        >{{this.form.usercode ? "修 改" : "添 加"}}</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import PostList from "../common/postList";
import BranchList from "../common/branchList";
import { addUser, updateUser, findHrpUserById } from "@/api/people/people";
import { verifyUser } from "@/api/questionnaire/index";
import qs from "qs";
import { setTimeout } from "timers";
export default {
  data() {
    return {
      dbbk: [
         {
          value: "1",
          label: "组织委员纪检委员"
        },
        {
          value: "2",
          label: "宣传委员青年委员"
        },
        {
          value: "3",
          label: "党支部书记"
        },
        {
          value: "4",
          label: "总党支部书记"
        }
      ],
      sex: [
        {
          value: "1",
          label: "男"
        },
        {
          value: "2",
          label: "女"
        }
      ],
      politics: [
        {
          value: "0",
          label: "中共党员"
        },
        {
          value: "1",
          label: "民主党派"
        },
        {
          value: "2",
          label: "无党派人士"
        },
        {
          value: "3",
          label: "群众"
        }
      ],
      roleOption: [],
      status: [
        {
          value: "1",
          label: "启用"
        },
        {
          value: "0",
          label: "停用"
        }
      ],
      education: [
        {
          value: "1",
          label: "博士"
        },
        {
          value: "2",
          label: "硕士"
        },
        {
          value: "3",
          label: "本科"
        },
        {
          value: "4",
          label: "专科"
        },
        {
          value: "5",
          label: "高中"
        }
      ],
      postType: [
        {
          value: "1",
          label: "科主任"
        },
        {
          value: "2",
          label: "护士长"
        },
        {
          value: "3",
          label: "行政"
        }
      ],
      form: {},
      selfDialogVisible: this.dialogVisible,
      addDisabled: false,
      tempstationcode: [],
      tempbranchcode: []
    };
  },
  props: {
    dialogVisible: {
      required: true
    },
    parentForms: {
      required: true
    },
    stationcode: {
      type: Array,
      default: () => [""]
    },
    branchcode: {
      type: Array,
      default: () => [""]
    }
  },
  components: {
    PostList,
    BranchList
  },
  created() {
    this.form = Object.assign({}, this.parentForms);
    if (Array.isArray(this.stationcode)) {
      this.tempstationcode = this.stationcode;
    } else {
      this.tempstationcode = [];
    }
    if (Array.isArray(this.branchcode)) {
      this.tempbranchcode = this.branchcode;
    } else {
      this.tempbranchcode = [];
    }

    if (
      this.$store.state.user.user.rolecode == "100" ||
      this.$store.state.user.user.rolecode == "50"
    ) {
      this.roleOption = [
        {
          value: "100",
          label: "组织部"
        },
        {
          value: "150",
          label: "打分用户"
        },
        {
          value: "200",
          label: "部门长"
        },
        {
          value: "300",
          label: "普通用户"
        }
      ];
    } else {
      this.roleOption = [
        {
          value: "400",
          label: "人事处"
        }
      ];
    }
  },
  methods: {
    into() {},
    //获取部门选择
    getSelectDepartment(data, row) {
      this.form.departmentcode = data;
      this.form.fulldepartmentcode = row.join(",");
    },
    //添加/修改岗位
    addSubmit() {
      let _this = this;
      if (this.addDisabled) {
        return false;
      }
      if (!this.form.username) {
        this.$message.warning("请填写用户姓名");
        return;
      }
      if (!this.form.moneycard) {
        this.$message.warning("请填写发薪号");
        return;
      }
      if (!this.form.userstate) {
        this.$message.warning("请选择用户状态");
        return;
      }
      if (!this.form.rolecode) {
        this.$message.warning("请选择角色权限");
        return;
      }
       if (this.form.mobile) {
      if (!this.common.isPoneAvailable(this.form.mobile)) {
        this.$message.warning("请填写正确的手机号码");
        return;
      }
      
       }
      if (this.form.email) {
        if (!this.common.isEmailAvailable(this.form.email)) {
          this.$message.warning("请填写正确的邮箱");
          return;
        }
      }
      // return;
      this.addDisabled = true;
      this.form.dbtype = this.$store.state.user.user.dbtype
      if (this.form.usercode) {
        //修改
        new Promise((response, reject) => {
          updateUser(qs.stringify(this.form))
            .then(response => {
              if (response.data.code == 0) {
                this.$message.success(response.data.msg);
                this.cancel();
                this.getList();
              } else {
                this.$message.error(response.data.msg);
              }
              setTimeout(() => {
                _this.addDisabled = false;
              }, 500);
            })
            .catch(error => {
              reject(error);
            });
        });
      } else {
        //添加
        new Promise((response, reject) => {
          addUser(qs.stringify(this.form))
            .then(response => {
              if (response.data.code == 0) {
                this.$message.success(response.data.msg);
                this.cancel();
                this.getList();
              } else {
                this.$message.error(response.data.msg);
              }
              setTimeout(() => {
                _this.addDisabled = false;
              }, 500);
            })
            .catch(error => {
              reject(error);
            });
        });
      }
    },
    //取消
    cancel() {
      this.selfDialogVisible = false;
      if (!this.selfDialogVisible) {
        this.$emit("childClose", false);
      }
    },
    //调用父亲查询列表方法
    getList() {
      this.$emit("childGetList", false);
    },
    //获取岗位选择
    getSelectStation(data, row) {
      this.form.stationcode = data;
      this.form.fullstationcode = row.join(",");
    },
    //获取支部选择
    getSelectBranch(data, row) {
      debugger
      this.form.branchcode = data === undefined ? '' : data;
      this.form.fullbranchcode = row.join(",");
    },
    //根据id查询用户
    selectUser() {
      let data = {
        uId: this.form.moneycard
      };
      new Promise((response, reject) => {
        verifyUser(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.form = response.data.data;
              if (response.data.data.education == "博士") {
                this.form.education = "1";
              } else if (response.data.data.education == "硕士") {
                this.form.education = "2";
              } else if (response.data.data.education == "本科") {
                this.form.education = "3";
              } else if (response.data.data.education == "专科") {
                this.form.education = "4";
              } else if (response.data.data.education == "高中") {
                this.form.education = "5";
              }else{
                this.form.education = "";
              }
              this.form.political = "";
              this.tempstationcode = [""];
              this.tempbranchcode = [""];
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
    dialogVisible(val, oldVal) {
      this.selfDialogVisible = val;
    },
    parentForms(val, oldVal) {
      this.form = Object.assign({}, val);
      if (Array.isArray(this.stationcode)) {
        this.tempstationcode = this.stationcode;
      } else {
        this.tempstationcode = [];
      }
      if (Array.isArray(this.branchcode)) {
        this.tempbranchcode = this.branchcode;
      } else {
        this.tempbranchcode = [];
      }
    }
  }
};
</script>
<style scoped lang="scss">
.table-expand {
  padding: 0px;
  .el-form-item {
    margin-right: 0;
    margin-bottom: 10px;
    width: 50%;
  }
  .el-select {
    // width: 185px;
  }
}
</style>
