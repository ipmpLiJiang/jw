<template>
  <div id="app">
    <el-row>
      <el-col
        :span="12"
        style="line-height: 60px;font-size: 18px;color: #409EFF;"
        v-show="this.title"
      >{{title}}</el-col>
       <el-col
        :span="12"
        style="line-height: 60px;font-size: 18px;color: #409EFF;" v-if="!this.title"
      >武汉市精神卫生中心360°考核系统</el-col>
      <el-col
        :span="3"
      >&nbsp;
      </el-col>
      <el-col
        :span="3"
        style="line-height: 60px;font-size: 18px;"
      ><router-link style="color: #409EFF;" to="/index">{{this.$store.state.user.user.dbtype==''?'&nbsp;':this.$store.state.user.user.dbtype=='1'?'党支部考核':'干部考核'}}
      </router-link>
      </el-col>
      <el-col :span="6">
        <el-menu
          mode="horizontal"
          @select="handleSelect"
          active-text-color="#409EFF"
        >
          <el-menu-item
            index="4"
            @click="loginout"
          ><i class="icon iconfont icon-dingbudaohang-zhangh"></i>退出登录</el-menu-item>
          <!-- <el-menu-item index="3"><i class="icon iconfont icon-xiaoxi"></i>消息</el-menu-item> -->
          <el-menu-item index="2">
          <el-popover
            placement="left"
            width="380"
            trigger="hover">
            <el-row class="userxxdiv">
              <el-col :span="6">
                姓　　名：
              </el-col>
              <el-col :span="15">
                {{$store.state.user.user.username}}
              </el-col>
            </el-row>
            <el-row class="userxxdiv">
              <el-col :span="6">
                群众关系：
              </el-col>
              <el-col :span="15">
                {{$store.state.user.user.politicalname}}
              </el-col>
            </el-row>
            <el-row class="userxxdiv">
              <el-col :span="6">
                所属支部：
              </el-col>
              <el-col :span="15">
                {{$store.state.user.user.branchname=='' || $store.state.user.user.branchname==null?"无":$store.state.user.user.branchname}}
              </el-col>
            </el-row>
            <el-row class="userxxdiv">
              <el-col :span="6">
                部　　门：
              </el-col>
              <el-col :span="15">
                {{$store.state.user.user.departmentname}}
              </el-col>
            </el-row>
            <el-row class="userxxdiv">
              <el-col :span="6">
                岗　　位：
              </el-col>
              <el-col :span="15">
                {{$store.state.user.user.stationname}}
              </el-col>
            </el-row>
            <!-- <el-row class="userxxdiv">
              <el-col :span="6">
                岗位类型：
              </el-col>
              <el-col :span="15">
                {{$store.state.user.user.postTypeName=='' || $store.state.user.user.postTypeName==null?"无":$store.state.user.user.postTypeName}}
              </el-col>
            </el-row> -->
            <i slot="reference" class="icon iconfont icon-yonghu">
              {{$store.state.user.user.username}}
            </i>
          </el-popover>
          </el-menu-item>
          <!-- <el-menu-item
            index="3"
            v-if="$store.state.user.user.roleList.length>1"
          >
            <router-link
              to="/index"
              style="height:59px;display:inline-block;"
            >
              <i
                class="icon el-icon-s-home"
                style=""
              ></i>
              <span style="vertical-align: middle;">选择系统</span>
            </router-link>
          </el-menu-item> -->
          <!-- <el-menu-item
            index="5"
            v-if="synShow"
            @click="personnelSyn"
            
          >
            <i class="el-icon-sort"></i>
            <span style="vertical-align: middle;">人事同步</span>
          </el-menu-item> -->
        </el-menu>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { ToUpdate } from "@/api/questionnaire/index";
import qs from "qs";
export default {
  props: {
    title: {
      title: String,
      default:''
     
    },
  },
  
  data() {
    return {
      synShow: false,
      // title:"武汉市精神卫生中心360°考核系统"
    };
  },
  created() {
    this.roleCode();
    
  },
  methods: {
    handleSelect(key, keyPath) {},
    //判断用户角色
    roleCode() {
      let rList = this.$store.state.user.user.roleList;
      let rolecode = this.$store.state.user.user.rolecode;
      if (rList.length > 0) {
        rList.forEach(row => {
          if (
            row.rolecode == "50" ||
            row.rolecode == "100" ||
            row.rolecode == "400"
          ) {
            this.synShow = true;
          }
        });
      } else {
        if (rolecode == "50" || rolecode == "100" || rolecode == "400") {
          this.synShow = true;
        }
      }
    },
    //退出登录
    loginout() {
      this.$confirm("此操作将退出系统, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.$store.commit("$_removeStorage");
          this.$router.push({ path: "/" });
        })
        .catch(() => {});
    },
    //人事同步
    personnelSyn(val) {
      this.$confirm("此操作将同步人事库最新人员信息,该接口请求时间漫长预计需要5-10分钟 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          const loading = this.$loading({
            lock: true,
            text: "拼命同步中，请稍等",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)"
          });
          new Promise((response, reject) => {
            ToUpdate()
              .then(response => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success"
                  });
                } else {
                  this.$message({
                    message: response.data.msg,
                    type: "error"
                  });
                }
                loading.close();
              })
              .catch(error => {
                reject(error);
              });
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    }
  }
};
</script>
<style lang="scss" scoped>
.el-menu > .el-menu-item {
  float: right;
}
.el-menu.el-menu--horizontal {
  border-bottom: none;
}
.el-menu--horizontal > .el-menu-item {
  height: 59px;
  line-height: 59px;
}
.float-right {
  float: right;
}
i {
  font-size: 16px;
  margin-right: 5px;
}
.userxxdiv {
  padding: 10px 5px 5px 40px;
}
</style>
