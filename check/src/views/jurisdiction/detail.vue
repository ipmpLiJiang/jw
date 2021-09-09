<template>
  <div class="configure white-bg">
    <h4 class="title">
      问卷权限管理
    </h4>
    <el-row class="search">
      <h3>权限配置</h3>
      <ul>
        <li class="basic">
          <p>
            <i>姓名</i>
            <span v-if="u_name ">{{u_name}}</span>
            <span v-else style="color:#ccc;">暂无数据</span>
          </p>
          <p>
            <i>工号</i>
            <span v-if="u_job_number ">{{u_job_number}}</span>
            <span v-else style="color:#ccc;">暂无数据</span>
          </p>
          <p>
            <i>发薪号</i>
            <span v-if="u_id ">{{u_id}}</span>
            <span v-else style="color:#ccc;">暂无数据</span>
          </p>
          <p>
            <i>所属科室</i>
            <span v-if="u_check_department ">{{u_check_department}}</span>
            <span v-else style="color:#ccc;">暂无数据</span>
          </p>
          <p>
            <i>职务</i>
            <span v-if="u_technical_position1 ">{{u_technical_position1}}</span>
            <span v-else style="color:#ccc;">暂无数据</span>
          </p>
        </li>
        <li class="other">
          <p>角色权限配置</p>
          <div class="menu">
            <!-- <div class="list">
              <p>人事处考核</p>
              <div style="margin: 15px 0;"></div>
              <el-radio-group v-model="peopleMenu">
                <el-radio
                  v-for="item in one"
                  :label="item.id"
                  :key="item.id"
                  @click.native.prevent="clickitem(item.id, 1)"
                >{{item.name}}</el-radio>
              </el-radio-group>
            </div>
            <div class="list">
              <p>组织部考核</p>
              <div style="margin: 15px 0;"></div>
              <el-radio-group v-model="projectMenu">
                <el-radio
                  v-for="item in two"
                  :label="item.id"
                  :key="item.id"
                  @click.native.prevent="clickitem(item.id, 2)"
                >{{item.name}}</el-radio>
              </el-radio-group>
            </div> -->
            <div class="list">
              <p>调查问卷</p>
              <div style="margin: 15px 0;"></div>
              <el-radio-group v-model="achievementMenu">
                <el-radio
                  v-for="item in three"
                  :label="item.id"
                  :key="item.id"
                  @click.native.prevent="clickitem(item.id, 3)"
                >{{item.name}}</el-radio>
              </el-radio-group>
            </div>
          </div>
        </li>
      </ul>
      <div class="edit-btn">
        <el-button type="primary" size="small" @click="open">修改保存</el-button>
      </div>
    </el-row>
  </div>
</template>


<script>
import TopNav from "../layout/TopNav";
import qs from "qs";
import { showDetails, operation } from "@/api/jurisdiction/jurisdiction";

//前台配置菜单和id
const peopleOptions = [
  //人事处考核
  { name: "人事处管理员", id: "400" },
  { name: "人事处部门长", id: "500" },
];
const projectOptions = [
  //组织部考核
  { name: "组织部管理员", id: "100" },
  { name: "组织部打分用户", id: "150" },
  { name: "组织部部门长", id: "200" },
  { name: "组织部普通用户", id: "300" },
];
const achievementOptions = [
  //问卷调查
  { name: "调查问卷管理员", id: "600" },
];

export default {
  data() {
    return {
      u_name: "",
      u_job_number: "",
      u_id: "",
      u_check_department: "",
      u_technical_position1: "",
      role_code: "",
      adminRadio: 0,
      isChange: -1,

      peopleMenu: "",
      one: peopleOptions,

      projectMenu: "",
      two: projectOptions,

      achievementMenu: "",
      three: achievementOptions,
    };
  },
  components: {
    TopNav,
  },
  created() {
    this.u_id = this.$route.query.u_id;
    this.showDetails();
  },
  watch: {
    $route(to, from) {
      if (to.path == "/peopleConfig") {
        this.u_id = this.$route.query.u_id;
        this.showDetails();
      }
    },
  },
  methods: {
    //查询
    showDetails() {
      this.loading = true;
      let data = {
        uId: this.u_id,
        draw: 1,
        length: 10,
      };
      return new Promise((response, reject) => {
        showDetails(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              let res = response.data.data;
              //console.log(res);
              //----------------------获取基本信息-------------------------
              this.u_name = res.u_name;
              this.u_job_number = res.u_job_number;
              this.u_id = res.u_id;
              this.u_check_department = res.u_check_department;
              this.u_technical_position1 = res.u_technical_position1;
              this.role_code = res.role_code;
              if (res.person_permission.length > 0) {
                this.peopleMenu = res.person_permission[0].key;
              }
              if (res.organization_permission.length > 0) {
                this.projectMenu = res.organization_permission[0].key;
              }
              if (res.questionnaire_permission.length > 0) {
                this.achievementMenu = res.questionnaire_permission[0].key;
              }
              this.loading = false;
            } else {
              this.$message.warning(response.data.msg);
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //保存修改验证
    open() {
      this.$confirm("此操作将修改该用户的角色权限, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        showClose: false,
        closeOnClickModal: false,
      })
        .then(() => {
          this.save();
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消修改",
          });
        });
    },
    //保存修改
    save() {
      let data = {
        uId: this.u_id, //发薪号
        person_permission: this.peopleMenu, //科室代码
        organization_permission: this.projectMenu, //角色代码 逗号拼接
        questionnaire_permission: this.achievementMenu,
      };
      this.loading = true;
      return new Promise((response, reject) => {
        operation(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.loading = false;
              this.$alert("该角色权限修改成功,重新登录后配置生效", "修改成功", {
                confirmButtonText: "确定",
                type: "success",
                showClose: false,
                showCancelButton: false,
                closeOnClickModal: false,
              }).then(() => {
                this.$router.push({
                  path: "/admin/jurisdiction",
                });
              });
            } else {
              this.$message.warning(response.data.msg);
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    clickitem(e, type) {
      switch (type) {
        case 1:
          e === this.peopleMenu
            ? (this.peopleMenu = "")
            : (this.peopleMenu = e);
          break;
        case 2:
          e === this.projectMenu
            ? (this.projectMenu = "")
            : (this.projectMenu = e);
          break;
        case 3:
          e === this.achievementMenu
            ? (this.achievementMenu = "")
            : (this.achievementMenu = e);
          break;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.search {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px;
  margin: 20px;
  border-radius: 4px;
  .el-form-item {
    margin: 0px;
  }
  .el-button {
    margin-left: 10px;
  }
}
.title {
  height: 50px;
  line-height: 50px;
  border-bottom: 1px solid #eaeaea;
  padding: 0px 10px;
  font-weight: 600;
  color: #424242;
  background: #fff;
  span {
    font-weight: normal;
    color: #9b9b9b;
  }
  i {
    margin: 0px 4px;
    color: #9b9b9b;
  }
}
.configure {
  h3 {
    margin-bottom: 25px;
  }
}
.white-bg h3 {
  font-size: 16px;
  font-weight: 600;
  color: #111;
  height: 40px;
  line-height: 40px;
  border-bottom: 1px solid #eaeaea;
  padding: 0 15px;
}
ul {
  overflow: hidden;
  li {
    margin-bottom: 20px;
    padding-bottom: 20px;
    padding-left: 50px;
    border-bottom: 1px dashed #eaeaea;
  }
  .basic {
    p {
      line-height: 25px;
    }
    i {
      display: inline-block;
      width: 120px;
      font-weight: 600;
      color: #404040;
    }
  }
  .other {
    p {
      font-weight: 600;
      color: #404040;
      margin-bottom: 10px;
    }
    .menu {
      overflow: hidden;
      .list {
        width: 200px;
        float: left;
      }
    }
  }

  .must {
    margin-left: 20px;
    color: #f56c6c;
  }
  .el-input {
    width: 400px;
  }
}
.edit-btn {
  padding: 20px 0px 40px 0px;
  text-align: center;
}
.user-message {
  background: #fafafa;
  border: 1px dashed #e2e2e2;
  width: 730px;
  margin-top: 20px;
  h4 {
    line-height: 40px;
    padding-left: 20px;
  }
  p {
    padding-left: 20px;
    span {
      background: #409eff;
      color: #fff;
      font-weight: normal;
      padding: 2px 4px;
      margin-right: 10px;
    }
  }
}
</style>
<style lang="scss">
.configure {
  .el-radio_label {
    width: 150px;
  }
  .el-radio {
    margin-bottom: 10px;
    margin-right: 70px;
  }
}
</style>