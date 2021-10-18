<template>
  <div>
    <h4 class="title">
      <router-link to="/home"><span>首页</span></router-link><i class="el-icon-arrow-right"></i>个人评分管理
    </h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="5">
            <el-form-item label="用户姓名">
              <el-input
                placeholder="请输入姓名"
                v-model="search.username"
                clearable
                @keyup.enter.native="getList"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="岗位">
              <PostList
                @childSelectDepartment="getSelectStation"
                :selectedOptions="fullstationcode"
              ></PostList>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="年份">
              <el-date-picker
                v-model="search.year"
                type="year"
                placeholder="选择年"
                value-format="yyyy"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="季度">
              <el-select
                v-model="search.month"
                clearable
                placeholder="请选择"
              >
                <el-option
                  v-for="item in seasonOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-button
            type="primary"
            @click="searchList"
          >搜索</el-button>
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
          prop="scorredname"
          label="用户姓名"
          show-overflow-tooltip
        >
          <template
            slot-scope="scope"
            v-if="scope.row.scorredname"
          >
            {{scope.row.scorredname}}
          </template>
        </el-table-column>
        <el-table-column
          prop="stationname"
          label="所属岗位"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="departmentname"
          label="所属部门"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="打分季度"
          prop="statename"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            {{scope.row.year}}{{'(第'+scope.row.month+'季度)'}}
          </template>
        </el-table-column>
        <!-- <el-table-column
          fixed="right"
          label="分数"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.status == '评分'">未评分</span>
            <span v-else>已评分</span>
          </template>
        </el-table-column> -->
        <el-table-column
          fixed="right"
          label="状态"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.state == 6">进行中</span>
            <span v-else-if="scope.row.state == 7">已完成</span>
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="提交状态"
        >
          <template slot-scope="scope">
            <span style="background:#FF9797" v-if="scope.row.scoreState == '1'">未提交</span>
            <span style="background:#99FF99" v-else-if="scope.row.scoreState =='2'">已提交</span>
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
        >
          <template slot-scope="scope">
            <el-button
              @click="assess(scope.row)"
              type="text"
              size="small"
              v-if="scope.row.state != 7"
            >修改</el-button>
            <el-button
              @click="assess(scope.row)"
              type="text"
              size="small"
              v-else
            >查看</el-button>
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
  </div>
</template>

<script>
import PostList from "../common/postList";
import { selfGetList } from "@/api/home/home";
import qs from "qs";
export default {
  data() {
    return {
      seasonOptions: this.common.seasonOptions(),
      dbtype: this.$store.state.user.user.dbtype,
      quarterOptions: [
        {
          value: "0",
          label: "未提交"
        },
        {
          value: "1",
          label: "已提交"
        },
        {
          value: "5",
          label: "季结待提交"
        },
        {
          value: "6",
          label: "季结评分"
        },
        {
          value: "7",
          label: "季结评分完成"
        }
      ],
      search: {
        username: "",
        stationcode: "",
        month: "",
        year: ""
      },
      tableData: [],
      page: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      fullstationcode: [""],
      tableLoading: true
    };
  },
  components: {
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
      params.username = this.search.username;
      params.dbtype = this.dbtype;
      if (
        (this.search.month && this.search.year) ||
        (!this.search.month && !this.search.year)
      ) {
        params.month = this.search.month;
        params.year = this.search.year;
      }else{
        this.$message.warning("年份和季度请同时选择");
        return;
      }
      new Promise((response, reject) => {
        selfGetList(qs.stringify(params))
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
    getSelectStation(data, row) {
      this.search.stationcode = [];
      this.search.stationcode.push(data);
      this.fullstationcode = row;
    },
    //进入考核
    assess(row) {
      var url= "/home/assess2";
      if(this.dbtype=='2'){
        url= "/home/assess";
      }
      this.$router.push({
        path: url,
        query: {
          serialNo: row.serialno,
          userCode: row.scorredcode,
          year: row.year,
          month: row.month
        }
      });
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
