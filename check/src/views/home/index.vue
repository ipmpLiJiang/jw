<template>
  <div>
    <h4 class="title">首页</h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
          class="home-el-form"
        >
          <el-col :span="8" style="display: flex;">
            <el-form-item label="提交人姓名">
              <el-input
                placeholder="请输入名字"
                v-model="search.username"
                clearable
                @keyup.enter.native="getList"
              >
              </el-input>
            </el-form-item>
            <el-button
              type="primary"
              @click="searchList"
            >搜索</el-button>
          </el-col>

          <div>
            您有<span class="total">{{total}}</span>个季结需要评分
          </div>
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
          prop="year"
          label="绩效年份"
          show-overflow-tooltip
          width="200px"
        >
        </el-table-column>
        <el-table-column
          prop="monthname"
          label="绩效季度"
          show-overflow-tooltip
          width="150px"
        >
        </el-table-column>
        <el-table-column
          prop="title"
          label="绩效标题"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="username"
          label="提交人"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          prop="statename"
          label="状态"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="100px"
        >
          <template slot-scope="scope">
            <el-button
              @click="assess(scope.row)"
              type="text"
              size="small"
            >考核</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="page.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination> -->
    </el-row>
  </div>
</template>

<script>
import { getList } from "@/api/home/home";
import qs from "qs";
export default {
  data() {
    return {
      quarterOptions: this.common.seasonOptions(),
      search: {
        username: ""
      },
      tableData: [],
      page: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      tableLoading: true
    };
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
      params.username = this.search.username;
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
    //进入考核
    assess(row) {
       var url= "/home/assess2";
      if(this.$store.state.user.user.dbtype=='2'){
        url= "/home/assess";
      }
      this.$router.push({
        path: url,
        query: { serialNo: row.serialno, userCode: row.scorredcode }
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
.home-el-form {
  display: flex;
  justify-content: space-between;
      align-items: center;
      .total{
        font-size:22px;
        color:#f00;
        padding:0 5px;
      }
}
</style>
