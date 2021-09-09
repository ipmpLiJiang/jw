<template>
  <div>
    <h4 class="title">
      <router-link to="/home"><span>首页</span></router-link><i class="el-icon-arrow-right"></i>岗位管理
    </h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="5">
            <el-form-item label="所在部门">
              <DepartmentList
                @childSelectDepartment="getSelectDepartment"
                :selectedOptions="fulldepartmentcode"
              ></DepartmentList>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="岗位名称">
              <el-input
                placeholder="请输入岗位名称"
                v-model="search.stationName"
                clearable
                @keyup.enter.native="getList"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-button
            type="primary"
            @click="searchList"
          >搜索</el-button>
          <el-button
            type="primary"
            @click="openAdd"
          >新增岗位</el-button>
        </el-form>
      </el-col>
    </el-row>
    <el-row class="content">
      <el-table
        :data="tableData"
        border
        style="width: 100%"
        v-loading="tableLoading"
        element-loading-text="拼命加载中"
      >
        <el-table-column
          prop="departmentname"
          label="所属部门"
          show-overflow-tooltip
          width="150px"
        >
        </el-table-column>
        <el-table-column
          prop="stationname"
          label="岗位名称"
          show-overflow-tooltip
          width="200px"
        >
        </el-table-column>
        <el-table-column
          prop="stationdesc"
          label="岗位描述"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span v-html="scope.row.stationdesc"></span>
          </template>
        </el-table-column>
        <el-table-column
          prop="dutydesc"
          label="职要描述"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span v-html="scope.row.dutydesc"></span>
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="100px"
        >
          <template slot-scope="scope">
            <el-button
              @click="editPost(scope.row)"
              type="text"
              size="small"
            >编辑</el-button>
            <el-button
              type="text"
              size="small"
              @click="deletePost(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-row>
    <!-- 添加岗位 -->
    <AddPost
      :parentForms="forms"
      :departmentcode="departmentcode"
      :dialogVisible="dialogVisible"
      @childClose="childClose"
      @childGetList="getList"
    ></AddPost>

  </div>
</template>

<script>
import DepartmentList from "../common/departmentList";
import { getList, deleteStation } from "@/api/post/post";
import AddPost from "./addPost";
import qs from "qs";
export default {
  data() {
    return {
      forms: {
        departmentcode: "",
        stationname: "",
        stationdesc: "",
        stationcode: "",
        dutydesc: ""
      },
      departmentcode: [],
      search: {
        stationName: "",
        departmentcode: []
      },
      tableData: [],
      page: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      dialogVisible: false,
      dbtype: this.$store.state.user.user.dbtype,
      fulldepartmentcode: [""],
      tableLoading: true
    };
  },
  components: {
    AddPost,
    DepartmentList
  },
  mounted() {},
  created() {
    this.getList();
  },
  methods: {
    //初始化
    into() {
      this.page.pageNum = 1;
      this.page.pageSize = 10;
    },
    //设置每页多少条数据
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.getList();
    },
    //翻页
    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.getList();
    },
    //搜索
    searchList() {
      this.into();
      this.getList();
    },
    //查询列表
    getList() {
      let params = this.page;
      if (this.search.departmentcode.length > 0) {
        params.departmentcode = this.search.departmentcode[0];
      } else {
        params.departmentcode = "";
      }
      params.stationname = this.search.stationName;
      new Promise((response, reject) => {
        getList(qs.stringify(params))
          .then(response => {
            if (response.data.code == 0) {
              this.tableData = response.data.data;
              this.total = response.data.totalPages;
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
            this.tableLoading = false;
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    //关闭列表
    childClose(val) {
      this.dialogVisible = val;
    },
    //打开添加岗位
    openAdd() {
      this.forms = {
        departmentcode: "",
        stationname: "",
        stationdesc: "",
        stationcode: "",
        dutydesc: ""
      };
      this.departmentcode = [""];
      this.dialogVisible = true;
    },
    //删除岗位
    deletePost(val) {
      this.$confirm("此操作将永久删除该岗位, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let data = {
            stationcode: val.stationcode,
            dbtype: this.dbtype
          };
          new Promise((response, reject) => {
            deleteStation(qs.stringify(data))
              .then(response => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success"
                  });
                  this.getList();
                } else {
                  this.$message({
                    message: response.data.msg,
                    type: "error"
                  });
                }
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
    },
    //编辑岗位
    editPost(row) {
      this.forms = row;
      this.departmentcode = row.fulldepartmentcode.split(",");
      this.dialogVisible = true;
    },
    //获取部门选择
    getSelectDepartment(data, row) {
      this.search.departmentcode = [];
      this.search.departmentcode.push(data);
      this.fulldepartmentcode = row;
    }
  }
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
.content {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px;
  margin: 0px 20px;
  border-radius: 4px;
  .el-pagination {
    margin: 20px auto;
    text-align: center;
  }
}
</style>
<style lang="css">
.el-tooltip__popper {
  max-width: 50%;
}
</style>