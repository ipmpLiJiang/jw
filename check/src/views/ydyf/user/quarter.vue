<template>
  <div>
    <h4 class="title"><router-link to="/home"><span>首页</span></router-link><i class="el-icon-arrow-right"></i>个人季结管理</h4>
    <el-row class="search">
      <el-col>
        <el-form
          label-width="100px"
          show-overflow-tooltip="true"
        >
          <el-col :span="6">
            <el-form-item label="季结标题">
              <el-input
                placeholder="请输入标题"
                v-model="search.title"
                clearable
                @keyup.enter.native="getList"
              >
              </el-input>
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
                  v-for="item in quarterOptions"
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
          <el-button
            type="primary"
            @click="openAdd"
            v-if="isAdd==0"
          >添加</el-button>
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
          prop="serialno"
          label="季结编码"
          show-overflow-tooltip
          width="200px"
        >
        </el-table-column>
        <el-table-column
          prop="title"
          label="季结标题"
          show-overflow-tooltip
          width="150px"
        >
        </el-table-column>
        <el-table-column
          label="季结季度"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            {{scope.row.year}}-{{scope.row.month}}
          </template>
        </el-table-column>
        <el-table-column
          prop="statename"
          label="状态"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="创建人"
          show-overflow-tooltip
        >
          <span>{{employeename}}</span>
        </el-table-column>
        <el-table-column
          prop="pubdate"
          label="修改日期"
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
              @click="editQuarter(scope.row)"
              type="text"
              size="small"
              v-if="scope.row.state!=7 && scope.row.state!=6"
            >编辑</el-button>
            <el-button
              @click="editQuarter(scope.row)"
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
    <!-- 添加月结 -->
    <AddQuarter
      :parentForms="forms"
      :dialogVisible="dialogVisible"
      :isAdd="isAdd"
      :type="type"
      @childClose="childClose"
      @childGetList="getList"
    ></AddQuarter>
  </div>
</template>

<script>
import { getList } from "@/api/user/quarter";
import AddQuarter from "./addQuarter";
import qs from "qs";
export default {
  data() {
    return {
      quarterOptions: [
        {
          value: "1",
          label: "第一月份"
        },
        {
          value: "2",
          label: "第二月份"
        },
        {
          value: "3",
          label: "第三月份"
        },
        {
          value: "4",
          label: "第四月份"
        }
      ],
      forms: {
        title: "",
        content: "",
        serialno: "",
      },
      search: {
        title: "",
        year: "",
        month: ""
      },
      tableData: [],
      page: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      dialogVisible: false,
      tableLoading: true,
      isAdd:"",
      type:1,
      employeename:"",
    };
  },
  components: {
    AddQuarter
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
      params.title = this.search.title;
      params.year = this.search.year;
      params.month = this.search.month;
      params.dbtype = this.$store.state.user.user.dbtype
      new Promise((response, reject) => {
        getList(qs.stringify(params))
          .then(response => {
            if (response.data.code == 0) {
              //处理时间戳
              response.data.data.forEach(row => {
                if (row.pubdate) {
                  row.pubdate = this.common.format(row.pubdate, 2);
                }
              });
              this.tableData = response.data.data;
              this.employeename = response.data.employeename;
              this.total = response.data.totalPages;
              this.isAdd = response.data.isAdd;
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
        title: "",
        content: "",
        serialno: "",
        savepath:"",
        filename:""
      };
      this.dialogVisible = true;
    },
    //编辑岗位
    editQuarter(row) {
      this.forms = {
        serialno: row.serialno,
        title: row.title,
        content: row.content,
        savepath: row.savepath,
        filename: row.filename
      };
      if(row.state == 6 || row.state == 7){
        this.type = 2;
      }else{
        this.type = 1;
      }
      this.dialogVisible = true;
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
