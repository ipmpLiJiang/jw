<template>
  <div>
    <h4 class="title">
      问卷权限管理
    </h4>
    <el-row class="search">
      <el-col>
        <el-form label-width="140px" show-overflow-tooltip="true">
          <el-col :span="7">
          <el-form-item label="姓名/工号/发薪号">
            <el-input placeholder="请输入姓名/工号/发薪号" size="small" clearable v-model="search.f_condition"></el-input>
          </el-form-item>
          </el-col>
          <el-col :span="5" class="list">
            <el-form-item label="权限选择">
              <el-select v-model="search.role" placeholder="请选择权限" size="small" clearable>
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-button type="primary" @click="searchList">查询</el-button>
          <el-button type="primary" @click="personnelSyn">人事同步</el-button>
        </el-form>
      </el-col>
    </el-row>
    <el-row class="content">
      <el-col class="table-content">
        <el-table
          :data="tableData"
          size="small"
          stripe
          border
          style="width: 100%"
          :header-cell-style="{background:'#eef1f6',fontWeight:'600'}"
          v-loading="tableLoading"
        >
          <el-table-column prop="u_name" label="姓名" align="center" width="200px"></el-table-column>
          <el-table-column prop="u_job_number" label="工号" align="center" width="120px"></el-table-column>
          <el-table-column prop="u_id" label="发薪号" align="center" width="120px"></el-table-column>
          <el-table-column
            prop="u_technical_position1"
            label="职务"
            align="center"
            width="200px"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="u_check_department"
            label="所属科室"
            align="center"
            width="200px"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column label="拥有角色" show-overflow-tooltip align="center">
            <template slot-scope="scope">
              <span
                v-for="(item,index) in (scope.row.role_code ? scope.row.role_code : '').split(',')"
                :key="index"
              >
                <!-- <el-tag
                  effect="dark"
                  size='mini'
                  style="background:#f75a5a;margin-left:5px;"
                >{{scope.row.role_code[index]}}</el-tag>-->
                <span
                  v-if="scope.row.role_code == null "
                  style="color:#ccc;"
                >无权限</span>
                <el-tag
                  v-if="scope.row.role_code != null && scope.row.role_code.split(',')[index] == 50"
                  effect="dark"
                  size='mini'
                  style="background:#f75a5a;margin-left:5px;color:#fff;"
                >超级管理员</el-tag>
                <el-tag
                  v-if="scope.row.role_code != null && scope.row.role_code.split(',')[index] == 400"
                  effect="dark"
                  size='mini'
                  style="background:#f75a5a;margin-left:5px;color:#fff;"
                >人事处管理员</el-tag>
                <el-tag
                  v-if="scope.row.role_code != null && scope.row.role_code.split(',')[index] == 500"
                  effect="dark"
                  size='mini'
                  style="background:#444444;margin-left:5px;color:#fff;"
                >人事处部门长</el-tag>
                <el-tag
                  v-if="scope.row.role_code != null && scope.row.role_code.split(',')[index] == 100"
                  effect="dark"
                  size='mini'
                  style="background:#ffc800;margin-left:5px;color:#fff;"
                >组织部管理员</el-tag>
                <el-tag
                  v-if="scope.row.role_code != null && scope.row.role_code.split(',')[index] == 150"
                  effect="dark"
                  size='mini'
                  style="background:#409eff;margin-left:5px;color:#fff;"
                >组织部打分用户</el-tag>
                <el-tag
                  v-if="scope.row.role_code != null && scope.row.role_code.split(',')[index] == 200"
                  effect="dark"
                  size='mini'
                  style="background:#409EFF;margin-left:5px;color:#fff;"
                >组织部部门长</el-tag>
                <el-tag
                  v-if="scope.row.role_code != null && scope.row.role_code.split(',')[index] == 300"
                  effect="dark"
                  size='mini'
                  style="background:#cccccc;margin-left:5px;color:#fff;"
                >组织部普通用户</el-tag>
                <el-tag
                  v-if="scope.row.role_code != null && scope.row.role_code.split(',')[index] == 600"
                  effect="dark"
                  size='mini'
                  style="background:#b9b9b9;margin-left:5px;color:#fff;"
                >调查问卷管理员</el-tag>
              </span>
            </template>
          </el-table-column>
          <!-- <el-table-column label="拥有权限" align="center">
            <template slot-scope="scope">
              <span
                v-for="(item,index) in (scope.row.role_code ? scope.row.role_code : '').split(',')"
                :key="index"
              >
                <span v-if="scope.row.role_code == null " style="color:#ccc;">无权限</span>
                <el-tag
                  v-if="scope.row.role_code != null && scope.row.role_code.split(',')[index] == 50"
                  effect="dark"
                  size="mini"
                  style="background:#f75a5a;margin-left:5px;color:#fff;"
                >最高权限</el-tag>
                <el-tag
                  v-if="scope.row.role_code != null && scope.row.role_code.split(',')[index] == 400"
                  effect="dark"
                  size="mini"
                  style="background:#f75a5a;margin-left:5px;color:#fff;"
                >人事部所拥有的最高权限</el-tag>
                <el-tag
                  v-if="scope.row.role_code != null && scope.row.role_code.split(',')[index] == 500"
                  effect="dark"
                  size="mini"
                  style="background:#444444;margin-left:5px;color:#fff;"
                >人事部各部门长所拥有的权限</el-tag>
                <el-tag
                  v-if="scope.row.role_code != null && scope.row.role_code.split(',')[index] == 100"
                  effect="dark"
                  size="mini"
                  style="background:#ffc800;margin-left:5px;color:#fff;"
                >次于组织部部长的最高权限，查看所有菜单</el-tag>
                <el-tag
                  v-if="scope.row.role_code != null && scope.row.role_code.split(',')[index] == 150"
                  effect="dark"
                  size="mini"
                  style="background:#409eff;margin-left:5px;color:#fff;"
                >打分用户</el-tag>
                <el-tag
                  v-if="scope.row.role_code != null && scope.row.role_code.split(',')[index] == 200"
                  effect="dark"
                  size="mini"
                  style="background:#409EFF;margin-left:5px;color:#fff;"
                >次于组织部的权限，只能查看自己部门下的菜单</el-tag>
                <el-tag
                  v-if="scope.row.role_code != null && scope.row.role_code.split(',')[index] == 300"
                  effect="dark"
                  size="mini"
                  style="background:#ccc;margin-left:5px;color:#fff;"
                >普通用户</el-tag>
                <el-tag
                  v-if="scope.row.role_code != null && scope.row.role_code.split(',')[index] == 600"
                  effect="dark"
                  size="mini"
                  style="background:#b9b9b9;margin-left:5px;color:#fff;"
                >调查问卷最高权限</el-tag>
              </span>
            </template>
          </el-table-column> -->
          <el-table-column fixed="right" width="180px" align="center" label="操作">
            <template slot-scope="scope">
              <el-button type="primary" plain size="mini" @click="config(scope.row)">权限配置</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="show-page">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page.sync="page.draw"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="page.length"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
          >
          </el-pagination>
          <!-- <el-pagination
            @current-change="handleCurrentChange"
            :current-page.sync="page.draw"
            :page-size="page.length"
            layout="total, prev, pager, next"
            :total="total"
          ></el-pagination> -->
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import TopNav from "../layout/TopNav";
import { getList } from "@/api/jurisdiction/jurisdiction";
import { ToUpdate } from "@/api/questionnaire/index";
import qs from "qs";
export default {
  data() {
    return {
      input: "",
      u_name: "",
      uid: "",
      section: "",
      options: [
        {
          label: "全部",
          value: "",
        },
        {
          label: "人事处管理员",
          value: "400",
        },
        {
          label: "人事处部门长",
          value: "500",
        },
        {
          label: "组织部管理员",
          value: "100",
        },
        {
          label: "组织部打分用户",
          value: "150",
        },
        {
          label: "组织部部门长",
          value: "200",
        },
        {
          label: "组织部普通用户",
          value: "300",
        },
        {
          label: "调查问卷管理员",
          value: "600",
        },
      ],
      value: "1",
      tableData: [],
      page: {
        draw: 1,
        length: 10,
      },
      total: 0,
      tableLoading: false,
      search: {
        dept: "",
        user: "",
        job: "",
        role: "",
        f_condition: "",
      },
      u_r_name_array: [],
    };
  },
  components: {
    TopNav,
  },
  created() {
    this.getList();
  },
  methods: {
    //重置搜索条件
    reset() {
      this.search.dept = "";
      this.search.user = "";
      this.search.job = "";
      this.search.role = "";
      this.search.f_condition = "";
    },
    //初始化
    into() {
      this.page.draw = 1;
      this.page.length = 10;
    },
    //查询
    searchList() {
      this.getList();
    },
    //翻页
    handleCurrentChange(val) {
      this.page.draw = val;
      this.getList();
    },
    //设置每页多少条数据
    handleSizeChange(val) {
      this.page.length = val;
      this.getList();
    },
    //查询人员列表
    getList() {
      let data = this.page;
      data.f_condition = this.search.f_condition;
      data.role_code = this.search.role;
      this.tableLoading = true;
      return new Promise((response, reject) => {
        getList(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.tableData = response.data.data;
              this.total = parseInt(response.data.recordsTotal);
            } else {
              this.$message.warning(response.data.msg);
            }
            this.tableLoading = false;
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    config(row) {
      this.$router.push({
        path: "/admin/peopleConfig",
        query: { u_id: row.u_id },
      });
    },
    //人事同步
    personnelSyn(val) {
      this.$confirm(
        "此操作将同步人事库最新人员信息,该接口请求时间漫长预计需要5-10分钟 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          const loading = this.$loading({
            lock: true,
            text: "拼命同步中，请稍等",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)",
          });
          new Promise((response, reject) => {
            ToUpdate()
              .then((response) => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success",
                  });
                } else {
                  this.$message({
                    message: response.data.msg,
                    type: "error",
                  });
                }
                loading.close();
              })
              .catch((error) => {
                reject(error);
              });
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
  },
  watch: {
    $route: {
      handler(route) {
        this.getList();
      },
      deep: true,
    },
  },
};
</script>


<style lang="scss" scoped>
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

.table-content {
  padding: 15px;
  .el-button {
    padding: 5px;
  }
  .el-tag {
    border: none;
    line-height: 20px;
    border-radius: 0px;
  }
}
.show-page {
  text-align: center;
  margin-top: 15px;
}
.content {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px;
  margin: 0px 20px 20px 20px;
  border-radius: 4px;
  .el-pagination {
    margin: 20px auto;
    text-align: center;
  }
  .table-expand {
    padding: 0px;
    .el-form-item {
      margin-right: 0;
      margin-bottom: 10px;
      width: 33.33%;
    }
    .el-select {
      width: 185px;
    }
  }
}
</style>
