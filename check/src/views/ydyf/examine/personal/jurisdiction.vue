<template>
  <div>
    <h4 class="title">人员权限管理</h4>

    <el-row class="search">
      <el-col>
        <el-form label-width="100px" show-overflow-tooltip="true">
          <el-col :span="5">
            <el-form-item label="姓名">
              <el-input
                placeholder="请输入员工姓名"
                v-model="search.userName"
                clearable
                @keyup.enter.native="getList"
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="5" label-width="100px">
            <el-form-item label="发薪号">
              <el-input
                placeholder="请选择发薪号"
                v-model="search.userId"
                clearable
                @keyup.enter.native="getList"
              >
              </el-input>
            </el-form-item>
          </el-col>
        

          <el-col :span="24" class="edit-btn">
            <el-form-item>
              <el-button
                style="margin-left: -90px"
                type="primary"
                @click="searchList"
                ><i class="el-icon-search"></i>搜索
              </el-button>
              <el-button type="primary" @click="addPerson"
                ><i class="el-icon-plus"></i>添加</el-button
              >
            </el-form-item>
          </el-col>
        </el-form>
      </el-col>
    </el-row>

    <el-row class="content">
      <el-table :data="tableData" border style="width: 100%">
        <!-- <el-table-column type="selection" width="40" align="center" >
        </el-table-column> -->

                      

        <el-table-column
          prop="username"
          label="用户姓名"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="userId"
          label="发薪号"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="roleName"
          label="用户权限"
          show-overflow-tooltip
          align="center"
        >
        </el-table-column>

       

        <el-table-column fixed="right" label="操作" width="160" align="center">
          <template slot-scope="scope">
            <!-- <el-button
              @click="editUser(scope.row)"
              type="text"
              size="small"
              v-if="scope.row.status == 0"
              >编辑</el-button
            > -->
            <el-button @click="deleteUser(scope.row)" type="danger" size="small" v-if="scope.row.username!=='welb'"
              >删除
            </el-button>
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

    <!-- 添加人员 -->
    <el-dialog
      title="添加人员"
      :visible.sync="amendDialogFormVisible"
      width="25%"
    >
      <el-form :model="modificationForm">
        <!-- <el-form-item label="用户名" :label-width="formLabelWidth"> -->
        <!-- <el-input
            v-model="modificationForm.userName"
            autocomplete="off"
          ></el-input> -->
        <el-form-item label="用户名：" >
          <el-autocomplete
            class="inline-input"
            v-model="modificationForm.username"
            :fetch-suggestions="querySearchAsync"
            placeholder="请输入内容"
            @select="handleSelect"
          ></el-autocomplete>
         
        </el-form-item>

        <el-form-item label="发薪号：" >
          <el-col :span="11">
          <el-input
            v-model="modificationForm.userId" 
            autocomplete="off"
          
          ></el-input>
            </el-col>
        </el-form-item>

        <el-form-item label="选择权限" >
       <el-col :span="11">
          <el-select
            v-model="modificationForm.personType"
            placeholder="请选择员工类型"
          >
            <el-option
              v-for="item in permissionlList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
          </el-col>
        
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="amendDialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="redact" :loading="addLoading"
          >确 定</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import qs from "qs";
import { examineList, searchUser,insert,jurisdictionDelete } from "../../api/addData/addData";

const delay = (function () {
  let timer = 0;
  return function (callback, ms) {
    clearTimeout(timer);
    timer = setTimeout(callback, ms);
  };
})();

export default {
  props: {},
  data() {
    return {
      search: {
        userName: "",
        userId: "",
        status: "",
        personType: "",
      },
      modificationForm: {
     
      },
      value: "",
      options: [
        {
          value: "",
          label: "全部类型",
        },
        {
          value: 8,
          label: "普通管理员",
        },
        {
          value: 1,
          label: "超级管理员",
        },
      ],
      userList: [],
      personValue: [],
      loading: false,

      permissionlList: [
        {
          value: 8,
          label: "普通管理员",
        },
        {
          value: 1,
          label: "超级管理员",
        },
      ],

      condition: "", //选择状态
      amendDialogFormVisible: false,

      // submitShow:false, //提交按钮显示隐藏
      tableData: [],
      formLabelWidth: "80px",

      page: {
        pageNum: 1,
        pageSize: 10,
      },
      total: 0,
      tableLoading: true,

      addLoading: false,
    };
  },
  computed: {},
  created() {
    this.getList();
  },
  mounted() {
   
  },
  watch: {
   
  },
  methods: {
    querySearchAsync(queryString, callback) {
      var list = [{}];
      let data = {};
      data.key = queryString;
      new Promise((response, reject) => {
        searchUser(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              list = response.data.data;

              for (let i of list) {
                i.value = i.userName + i.userId;
              }
         
              callback(list);
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    handleSelect(item) {
      this.modificationForm.username=item.userName
      this.modificationForm.userId=item.userId
      
    },
   

 
   
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
       params.username = this.search.userName;
      params.userId = this.search.userId;
      
      new Promise((response, reject) => {
        examineList(qs.stringify(params))
          .then((response) => {
            if (response.data.code == 0) {
              this.tableData = response.data.data;

              this.total = response.data.totalPages;
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    addPerson() {
       this.modificationForm = {};
      this.amendDialogFormVisible = true;
    },

   //删除
    deleteUser(val){
       this.$confirm("此操作将永久删除该人员, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let data = {
           userId: val.userId,
          };

          new Promise((response, reject) => {
            jurisdictionDelete(qs.stringify(data))
              .then((response) => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success",
                  });
                  this.getList();
                } else {
                  this.$message({
                    message: response.data.msg,
                    type: "error",
                  });
                }
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
    change(item) {
      
    },
    redact() {
    
     let data = {
        userId: this.modificationForm.userId,
        roleId: this.modificationForm.personType,
        status: "0",
      };
     
      return new Promise((response, reject) => {
        insert(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.amendDialogFormVisible = false;
              this.modificationForm = {};
              this.$message({
                message: response.data.msg,
                type: "success",
              });
              this.getList();
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
   
    
    fiveScore() {},
  },
  components: {},
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
  margin: 20px;
  border-radius: 4px;
  padding-top: 15px;

  .el-form-item {
    margin: 0px;
  }

  .el-button {
    margin-left: 10px;
  }

  .edit-btn {
    padding: 15px 0px;
    margin-top: 15px;
    border: 1px solid #ededee;
    background: #fcfcfc;

    .el-button {
      margin-left: 10px;
    }

    span {
      color: #8a919b;
      font-size: 0.9em;
      float: right;

      .icon-jinggao {
        color: #f3ad0e;
        position: relative;
        top: 1px;
      }

      i {
        margin-right: 3px;
      }

      .icon-daochu-tianchong {
        font-size: 12px;
      }

      .icon-dayin {
        font-size: 12px;
      }
    }
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

.score-dialog {
  max-height: 500px;
  overflow: auto;
}

.upload-grade-leading-in {
  display: inline-block;
}

.monitor {
  color: #f00;
  font-size: 18px;
  margin-bottom: 10px;
  display: block;
  text-align: left;
}

.a-demo {
  display: block;
  color: #e6a23c;
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
  text-decoration: underline;
  text-align: left;
  cursor: pointer;
}
.self-evaluation {
  width: 100px;
  height: 40px;
}
</style>
<style>
.score-dialog {
  max-height: 500px;
  overflow: auto;
}
</style>