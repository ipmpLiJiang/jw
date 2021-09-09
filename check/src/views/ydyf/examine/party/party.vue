<template>
  <div class="department" style="padding: 15px">
    <el-row>
      <el-col :span="6" class="left">
        <div class="white-bg">
          <h3>党支部列表</h3>
          <div class="tree">
            <el-input
              placeholder="搜索党支部"
              v-model="filterText"
              clearable
              size="small"
            ></el-input>
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
      <el-col :span="18">
        <el-col class="people">
          <div class="white-bg">
            <h3>
              党支部人员列表
              <el-button
                type="primary"
                class="s-btn"
                size="small"
                @click="searchList"
                >搜索</el-button
              >
              <el-input
                v-model="name"
                placeholder="请输入名字"
                size="small"
                class="s-input"
              ></el-input>
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
                  prop="parentRelationName"
                  label="上级党委"
                  align="center"
                >
                  <template slot-scope="scope">
                    <span v-if="scope.row.parentRelationName">{{
                      scope.row.parentRelationName
                    }}</span
                    >             <span v-else style="color: #999">暂无</span> 
                              
                  </template>
                </el-table-column>
                <el-table-column
                  prop="leaderName"
                  label="书记姓名"
                  align="center"
                ></el-table-column>
                <el-table-column
                  prop="excellentPercent"
                  label="评优占比"
                  align="center"
                >
                  <template slot-scope="scope">
                    <span style="color: #409EFF"
                      >{{
                        scope.row.excellentPercent
                          ? scope.row.excellentPercent
                          : 0
                      }}%</span
                    >            
                  </template>
                </el-table-column>
                <el-table-column
                  label="员工类型"
                  align="center"
                  show-overflow-tooltip
                >
                  <template slot-scope="scope">
                    <span>{{
                      scope.row.personType == 0 ? "临床人员" : "非临床人员"
                    }}</span
                    >            
                  </template>
                </el-table-column>
                <el-table-column width="120px" label="操作">
                  <template slot-scope="scope">
                    <el-button
                      type="primary"
                      plain
                      size="mini"
                      @click="edit(scope.row)"
                      >编辑</el-button
                    >
                    <el-button
                      type="danger"
                      plain
                      size="mini"
                      @click="deleteUser(scope.row)"
                      >删除</el-button
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

    <el-dialog
      title="编辑党支部"
      :visible.sync="dialogEdit"
      width="600px"
      class="edit-dialog"
    >
      <div>
        <p>
          <span>党支部名字</span>
          <el-input
            class="party-input"
            v-model="detailData.relationsName"
            placeholder="请输入内容"
          ></el-input>
        </p>
        <p>
          <span>书记姓名</span>
          <el-input
            class="party-input"
            v-model="detailData.leaderName"
            placeholder="请输入内容"
          ></el-input>
        </p>
        <p>
          <span>书记发薪号</span>
          <el-input
            class="party-input"
            v-model="detailData.leaderUserId"
            placeholder="请输入内容"
          ></el-input>
        </p>
        <p>
          <span>打分书记姓名</span>
          <el-input
            class="party-input"
            v-model="detailData.directorName"
            placeholder="请输入内容"
          ></el-input>
        </p>
        <p>
          <span>打分书记发薪号</span>
          <el-input
            class="party-input"
            v-model="detailData.directorUserId"
            placeholder="请输入内容"
          ></el-input>
        </p>
        <p>
          <span>评优百分比</span>
          <el-slider
            class="party-slider"
            v-model="detailData.excellentPercent"
            show-input
          >
          </el-slider>
        </p>
        <p>
          <span>员工类型</span>
          <el-select
            v-model="detailData.personType"
            placeholder="请选择员工类型"
            @change="modification"
            style="width: 78%"
          >
            <el-option
              v-for="item in personnelList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </p>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogEdit = false">取消</el-button>
        <el-button size="small" type="primary" @click="updateStatus" :loading="isLoginLoading"
          >确定</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
//负责人公用组件
import {
  branchTree,
  list,
  Delete,
  update,
  Add,
  tree,
  headquartersList,
  AddGeneral,
  partyList,
  search,
} from "../../api/party/index";
import qs from "qs";
export default {
  data() {
    return {
      treeData: [],
      disabled: true,
      filterText: "",
      props: {
        children: "children",
        label: "relationsName",
      },
      tableData: [],
      baseData: {},
      dialogEdit: false,
      dialogSwitchType: "未激活",
      dt_id: "",
      page: {
        pageNum: 1,
        pageSize: 10,
      },
      total: 0,
      tableLoading: false,
      isLoginLoading:false,
      detailData: {
        excellentPercent:15
      },
      status: false,
      d_upper_id: "13000001",
      defaultKeys: [],
      name: "",
      parentId: "",
      personnelList: [
        {
          value: "",
          label: "全部类型",
        },
        {
          value: 0,
          label: "临床人员",
        },
        {
          value: 1,
          label: "非临床人员",
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
      this.page.draw = 1;
      this.page.length = 10;
    },
    //翻页
    handleCurrentChange(val) {
      this.page.draw = val;
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
      this.tableLoading = true;
      return new Promise((response, reject) => {
        list(qs.stringify(data))
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
    //打开人员编辑
    edit(row) {
      this.detailData={}
      row.excellentPercent = row.excellentPercent ? row.excellentPercent : 0;
      let data=JSON.parse(JSON.stringify(row))
      this.detailData = data;
      this.dialogEdit = true;
    },
    //修改激活文字
    dialogSwitch(val) {
      if (val) {
        this.dialogSwitchType = "已激活";
      } else {
        this.dialogSwitchType = "未激活";
      }
    },
    //修改人员状态
    updateStatus() {
      let data = this.detailData;
      this.isLoginLoading=true;
      return new Promise((response, reject) => {
        update(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.$message.success(response.data.msg);
              this.getList();
              this.tableLoading = false;
            } else {
              this.$message.warning(response.data.msg);
            }
            this.dialogEdit = false;
            this.isLoginLoading=false;
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //修改党支部基本信息
    submitBase() {
      let data = this.baseData;
      return new Promise((response, reject) => {
        updepartinfo(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.$message.success(response.data.msg);
            } else {
              this.$message.warning(response.data.msg);
            }
            this.dialogEdit = false;
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    modification() {},
    //删除
    deleteUser(row) {
      this.$confirm("此操作将永久删除该人员, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let id = row.id;
          new Promise((response, reject) => {
            Delete(id)
              .then((response) => {
                if (response.data.code == 0) {
                  this.$message.success(response.data.msg);
                  this.getList();
                  this.getParty();
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
            type: "warning",
            message: "已取消删除",
          });
        });
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
