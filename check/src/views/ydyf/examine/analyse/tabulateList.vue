<template>
  <div class="department" style="padding: 15px">
    <el-row>
      <el-col :span="5" class="left">
        <div class="white-bg">
          <h3>党支部列表</h3>
          <div class="tree">
            <el-select
              v-model="year"
              placeholder="请选择考核年度"
              style="width: 100%"
              clearable
              @change="currentSel"
            >
              <el-option
                v-for="item in yearoptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>

            <el-scrollbar style="height: 100%">
              <el-tree
                class="filter-tree"
                :data="treeData"
                :props="props"
                :filter-node-method="filterNode"
                @node-click="handleNodeClick"
                ref="tree"
                node-key="id"
                :default-expanded-keys="defaultKeys"
                :default-checked-keys="defaultKeys"
              ></el-tree>
            </el-scrollbar>
          </div>
        </div>
      </el-col>
      <el-col :span="19">
        <el-col class="people">
          <div class="white-bg">
            <h3>
              党支部人员列表

              <!-- <el-button
                type="primary"
                class="s-btn"
                size="small"
                @click="searchList"
                >搜索</el-button
              >
              <el-input
                v-model="filterText"
                placeholder="搜索党支部"
                size="small"
                class="s-input"
              ></el-input> -->
            </h3>

            <div class="content">
              <el-table
                :data="tableData"
                size="small"
                stripe
                border
                style="width: 100%"
                :header-cell-style="{
                  background: '#eef1f6',
                  fontWeight: '600',
                }"
                v-loading="tableLoading"
                element-loading-text="拼命加载中"
              >
                <el-table-column
                  prop="relationsName"
                  label="党支部名称"
                  align="center"
                ></el-table-column>
                <el-table-column
                  prop="totalCount"
                  label="总人数"
                  align="center"
                  width="120"
                >
                  <template slot-scope="scope">
                               <span style="color: #999">{{scope.row.totalCount}}人</span> 
                              
                  </template>
                </el-table-column>
                <el-table-column
                  prop="leaderName"
                  label="优秀"
                  align="center"
                  width="195"
                >
                  <template slot-scope="scope">
                    人数：
                    <span style="color: #409EFF" @click="excellent(scope.row)">{{scope.row.totalLevelOneCount}}人</span>            
                    <span style="margin-left: 5px"
                      >百分比：<span style="color: #e6a23c">{{scope.row.levelOnePercent}}%</span></span
                    >            
                  </template>
                </el-table-column>
                <el-table-column
                  prop="leaderName"
                  label="良好"
                  align="center"
                  width="195"
                >
                  <template slot-scope="scope">
                    人数：
                    <span style="color: #409EFF" @click="good(scope.row)">{{scope.row.totalLevelTwoCount}}人</span>            
                    <span style="margin-left: 5px"
                      >百分比：<span style="color: #e6a23c">{{scope.row.levelTwoPercent}}%</span></span
                    >            
                  </template>
                </el-table-column>
                <el-table-column
                  prop="leaderName"
                  label="一般"
                  align="center"
                  width="195"
                >
                  <template slot-scope="scope">
                    人数：
                    <span style="color: #409EFF" @click="general(scope.row)">{{scope.row.totalLevelThreeCount}}人</span>            
                    <span style="margin-left: 5px"
                      >百分比：<span style="color: #e6a23c">{{scope.row.levelThreePercent}}%</span></span
                    >            
                  </template>
                </el-table-column>
                <el-table-column
                  prop="leaderName"
                  label="较差"
                  align="center"
                  width="195"
                >
                  <template slot-scope="scope">
                    人数：
                    <span style="color: #409EFF" @click="range(scope.row)">{{scope.row.totalLevelFourCount}}人</span>            
                    <span style="margin-left: 5px"
                      >百分比：<span style="color: #e6a23c">{{scope.row.levelFourPercent}}%</span></span
                    >            
                  </template>
                </el-table-column>

                   <el-table-column
                  prop="leaderName"
                  label="未考核完成"
                  align="center"
                  width="195"
                >
                  <template slot-scope="scope">
                    人数：
                    <span style="color: #409EFF" @click="unfinished(scope.row)">{{scope.row.notFinishedCount}}人</span>            
                    <span style="margin-left: 5px"
                      >百分比：<span style="color: #e6a23c">{{scope.row.notFinishedPercent}}%</span></span
                    >            
                  </template>
                </el-table-column>
              </el-table>
              <div class="show-page">
                <el-pagination
                  @current-change="handleCurrentChange"
                  :current-page.sync="page.draw"
                  :page-size="page.length"
                  layout="total, prev, pager, next"
                  :total="total"
                ></el-pagination>
              </div>
            </div>
          </div>
        </el-col>
      </el-col>
    </el-row>
  </div>
</template>

<script>
//负责人公用组件
import {branchTree,} from "../../api/party/index";
import {listDetail} from "../../api/analyse/analyse";
import qs from "qs";
export default {
  data() {
    return {
      treeData: [],
      filterText: "",
      props: {
        children: "children",
        label: "relationsName",
      },
      year: "",
      tableData: [],
      dialogSwitchType: "未激活",
      page: {
        pageNum: 1,
        pageSize: 10,
      },
      total: 0,
      tableLoading: false,
      isLoginLoading: false,
      defaultKeys: [],
      name: "",
      parentId: "-1",
      yearoptions: [
        {
          value: "1",
          label: "2020年度",
        },
        {
          value: "2",
          label: "2021年度",
        },
        {
          value: "3",
          label: "2022年度",
        },
        {
          value: "4",
          label: "2023年度",
        },
        {
          value: "5",
          label: "2024年度",
        },
      ],
    };
  },
  components: {},
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    },
    $route: {
      handler(route) {
        this.getSection();
      },
      deep: true,
    },
  },
  created() {
    this.getSection();
    this.getList();
  },
  methods: {
    //初始化
    into() {
      this.page.pageNum = 1;
      this.page.length = 10;
    },
    //翻页
    handleCurrentChange(val) {
      this.page.pageNum = val;
      this.getList();
    },
    //党支部人员列表模糊搜索
    searchList() {
      this.getList();
    },
    //查询党支部人员列表
    getList() {
      let data = this.page;
      data.parentId = this.parentId;
      data.filterText = this.filterText;
      data.parentId = this.parentId;
      data.year = this.year;
      // data.parentId=this.parentId;
      this.tableLoading = true;
      return new Promise((response, reject) => {
        listDetail(qs.stringify(data))
          .then((response) => {
       
            if (response.data.code == 0) {
              this.tableData = response.data.data;
            
         
              this.total = parseInt(response.data.totalPages);
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
    //查询党支部列表
    getSection() {
      return new Promise((response, reject) => {
        branchTree()
          .then((response) => {
            if (response.data.code == 0) {
              this.treeData = response.data.data;
            } else {
              this.$message.warning(response.data.msg);
            }
            this.loading = false;
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    //党支部搜索
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    //党支部选择
    handleNodeClick(data) {
    
      if (data.level == 3) {
        this.parentId = data.parentId;
      } else {
        this.parentId = data.id;
      }
      this.into();
      this.getList();
    },
     //优秀人数
    excellent(row){
    
       let url= this.$router.resolve({
          path: "/ydyf/informationList",
          query: { userId: row.id,level:1 },
        });
         window.open(url.href, '_blank');
    },
       //良好人数
    good(row){
    
       let url= this.$router.resolve({
          path: "/ydyf/informationList",
          query: { userId: row.id,level:2 },
        });
         window.open(url.href, '_blank');
    },
       //一般人数
    general(row){
    
       let url= this.$router.resolve({
          path: "/ydyf/informationList",
          query: { userId: row.id,level:3 },
        });
         window.open(url.href, '_blank');
    },
       //较差人数
    range(row){
    
       let url= this.$router.resolve({
          path: "/ydyf/informationList",
          query: { userId: row.id,level:4 },
        });
         window.open(url.href, '_blank');
    },
    //未完成
      unfinished(row){
    
       let url= this.$router.resolve({
          path: "/ydyf/informationList",
          query: { userId: row.id,level:0 },
        });
         window.open(url.href, '_blank');
    },
    currentSel(val) {
      this.year = val;
      this.getList();
    },
  },
};
</script>


<style lang="scss" scoped>
.tree {
  padding: 2%;
  height: 650px;
  width: 96%;
  display: inline-block;
  .el-tree {
    margin-top: 10px;
  }
}
.el-scrollbar .el-scrollbar__wrap {
  overflow-x: hidden;
}

.left {
  padding-right: 15px;
}
.basic {
  .content {
    padding: 5px 15px 15px 0px;
    .list {
      margin-top: 10px;
      .title {
        line-height: 32px;
        font-weight: 600;
        color: #868686;
        padding: 0px 15px;
      }
      .el-input {
        width: 190px;
      }
    }
  }
}
.people {
  .content {
    padding: 15px;
  }
  .el-button {
    padding: 5px;
  }
}
.white-bg h3 {
  .el-button {
    float: right;
    position: relative;
    top: 4px;
  }
  .s-input {
    width: 200px;
    float: right;
  }
  .s-btn {
    float: right;
    height: 32px;
    margin-left: 8px;
  }
}
.show-page {
  text-align: center;
  margin-top: 15px;
}
.el-dialog {
  p {
    font-size: 12px;
    border-bottom: 1px solid #ededed;
    line-height: 50px;
    span {
      font-weight: 600;
      display: inline-block;
      width: 120px;
    }
  }
  .party-slider {
    width: 78%;
    display: inline-block;
    vertical-align: middle;
  }
  .party-input {
    width: 78%;
    input {
      border: 0;
    }
  }
}
</style>
<style lang="scss">
.department {
  .el-tree-node__label {
    font-size: 12px;
  }
}
.department .edit-dialog .el-input__inner {
  border: 0 none;
  border-radius: 0px;
}
</style>
