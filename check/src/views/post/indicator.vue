<template>
  <div>
    <h4 class="title"><router-link to="/home"><span>首页</span></router-link><i class="el-icon-arrow-right"></i>岗位管理</h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="5">
            <el-form-item label="所在岗位">
              <PostList
                @childSelectDepartment="getSelectStation"
                :selectedOptions="fullstationcode"
              ></PostList>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="指标名称">
              <el-input
                placeholder="请输入指标名称"
                v-model="search.dutyName"
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
          >新增指标</el-button>
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
          prop="stationname"
          label="所属岗位"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="dutyname"
          label="指标名称"
          show-overflow-tooltip
          width="600"
        >
          <template slot-scope="scope">
            <span v-html="scope.row.dutyname"></span>
          </template>
        </el-table-column>
        <el-table-column
          prop="typename"
          label="指标类型"
          show-overflow-tooltip
        >
        <template slot-scope="scope">
            <span v-if="scope.row.dutytype==0">基础指标</span>
            <span v-if="scope.row.dutytype==1">岗位职责</span>
            <span v-if="scope.row.dutytype==2">重点任务</span>
            <span v-if="scope.row.dutytype==3">目标任务</span>
            <span v-if="scope.row.dutytype==4">政治建设</span>
            <span v-if="scope.row.dutytype==5">思想建设</span>
            <span v-if="scope.row.dutytype==6">组织建设</span>
            <span v-if="scope.row.dutytype==7">党建创新</span>
            <span v-if="scope.row.dutytype==8">作风建设</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="ascore"
          label="优秀分值"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="bscore"
          label="良好分值"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="cscore"
          label="一般分值"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="dscore"
          label="较差分值"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="defScore"
          label="默认评分"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="100"
        >
          <template slot-scope="scope">
            <el-button
              @click="editDuty(scope.row)"
              type="text"
              size="small"
            >编辑</el-button>
            <el-button
              type="text"
              size="small"
              @click="deleteDuty(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="10"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-row>
    <!-- 添加岗位 -->
    <AddIndicator
      :parentForms="forms"
      :stationcode="stationcode"
      :dialogVisible="dialogVisible"
      @childClose="childClose"
      @childGetList="getList"
    ></AddIndicator>

  </div>
</template>

<script>
import PostList from "../common/postList";
import { getList, deleteDuty } from "@/api/post/indicator";
import AddIndicator from "./addIndicator";
import qs from "qs";
export default {
  data() {
    return {
      forms: {
        dutyname: "",
        stationcode: "",
        dutytype: ""
      },
      stationcode: [""],
      search: {
        dutyName: "",
        stationcode: [""]
      },
      tableData: [],
      page: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      dialogVisible: false,
      fullstationcode: [""],
      tableLoading: true
    };
  },
  components: {
    AddIndicator,
    PostList
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
      if (this.search.stationcode.length > 0) {
        params.stationcode = this.search.stationcode[0];
      } else {
        params.stationcode = "";
      }
      params.dutyname = this.search.dutyName;
      params.dbtype = this.$store.state.user.user.dbtype
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
        dutyname: "",
        stationcode: "",
        dutytype: ""
      };
      if (this.search.stationcode.length > 0) {
        this.forms.stationcode = this.search.stationcode[0];
      } else {
        this.forms.stationcode = "";
      }
     // this.forms.fullstationcode = this.fullstationcode.join(",");
      this.stationcode = [""];
      this.dialogVisible = true;
    },
    //删除岗位
    deleteDuty(val) {
      this.$confirm("此操作将永久删除该岗位, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let data = {
            dutycode: val.dutycode
          };
          new Promise((response, reject) => {
            deleteDuty(qs.stringify(data))
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
    editDuty(row) {
      this.forms = row;
      this.stationcode = [];
      this.stationcode = row.fullstationcode.split(",");
      this.dialogVisible = true;
    },
    //获取部门选择
    getSelectStation(data, row) {
      this.search.stationcode = [];
      this.search.stationcode.push(data);
      this.fullstationcode = row;
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
