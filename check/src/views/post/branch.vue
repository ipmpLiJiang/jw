<template>
  <div>
    <h4 class="title"><router-link to="/home"><span>首页</span></router-link><i class="el-icon-arrow-right"></i>支部管理</h4>
    <el-row style="padding:20px;">
      <el-col
        :span="7"
        class="left"
      >
        <el-col :span="16">
          <el-input
            clearable
            placeholder="搜索支部"
            v-model="filterText"
          >
          </el-input>
        </el-col>
        <el-col :span="8">
          <el-button
            type="primary"
            @click="add"
          >添加支部</el-button>
        </el-col>
        <el-col :span="24">
          <el-tree
            class="filter-tree"
            :data="data5"
            default-expand-all
            :filter-node-method="filterNode"
            @node-click="handleNodeClick"
            :props="defaultProps"
            ref="tree2"
          >
          </el-tree>
        </el-col>
      </el-col>
      <el-col
        :span="16"
        class="right"
      >
        <el-form
          ref="form"
          :model="form"
          label-width="80px"
        >
          <el-form-item label="支部名称">
            <el-input v-model="form.branchname"></el-input>
          </el-form-item>
          <el-form-item label="支部描述">
            <el-input
              type="textarea"
              :rows="5"
              v-model="form.branchdesc"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              :disabled="saveDisabled"
              @click="addBranch"
            >保存修改</el-button>
            <el-button
              type="warning"
              :disabled="delDisabled"
              @click="openSubdivision"
            >添加子支部</el-button>
            <el-button
              type="danger"
              :disabled="delDisabled"
              @click="deleteBranch"
            >删除支部</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <!-- 添加子支部 -->
    <el-dialog
      title="添加子支部"
      :visible.sync="subdivisionVisible"
    >
      <el-form :model="form">
        <el-form-item label="支部名称">
          <el-input v-model="childForm.branchname"></el-input>
        </el-form-item>
        <el-form-item label="支部描述">
          <el-input
            type="textarea"
            :rows="5"
            v-model="childForm.branchdesc"
          ></el-input>
        </el-form-item>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="subdivisionVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="addChildBranch"
        >确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
let id = 1000;
import {
  treelist,
  addBranch,
  updateBranch,
  deleteBranch
} from "@/api/post/branch";
import qs from "qs";
export default {
  data() {
    return {
      filterText: "",
      form: {
        branchcode: "",
        upbranchcode: "",
        branchname: "",
        branchdesc: ""
      },
      childForm: {
        branchcode: "",
        branchname: "",
        branchdesc: ""
      },
      data5: [],
      defaultProps: {
        children: "childBranch",
        label: "branchname"
      },
      saveDisabled: true,
      delDisabled: true,
      subdivisionVisible: false
    };
  },
  components: {},
  mounted() {},
  created() {
    this.getList();
  },
  methods: {
    //树形列表查找功能
    filterNode(value, data) {
      if (!value) return true;
      return data.branchname.indexOf(value) !== -1;
    },
    //树形图点击回调
    handleNodeClick(data) {
      this.form = data;
    },
    //初始化数据
    into() {
      this.form = {
        branchcode: "",
        upbranchcode: "",
        branchname: "",
        branchdesc: ""
      };
    },
    //支部列表
    getList() {
      new Promise((response, reject) => {
        treelist()
          .then(response => {
            console.log(response)
            if (response.data.code == 0) {
              this.data5 = response.data.data;
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
    },
    //添加支部按钮事件
    add() {
      this.into();
    },
    //添加/修改支部
    addBranch() {
      if (this.form.branchcode) {
        //修改
        delete this.form.childBranch;
        new Promise((response, reject) => {
          updateBranch(qs.stringify(this.form))
            .then(response => {
              if (response.data.code == 0) {
                this.$message({
                  message: response.data.msg,
                  type: "success"
                });
                this.getList();
                this.into();
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
      } else {
        //添加
        new Promise((response, reject) => {
          addBranch(qs.stringify(this.form))
            .then(response => {
              if (response.data.code == 0) {
                this.$message({
                  message: response.data.msg,
                  type: "success"
                });
                this.getList();
                this.into();
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
      }
    },
    //删除支部
    deleteBranch() {
      this.$confirm("此操作将永久删除该支部, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let data = {
            branchcode: this.form.branchcode
          };
          new Promise((response, reject) => {
            deleteBranch(qs.stringify(data))
              .then(response => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success"
                  });
                  this.getList();
                  this.into();
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
    //打开添加子支部弹框
    openSubdivision() {
      this.subdivisionVisible = true;
    },
    //添加子支部
    addChildBranch() {
      if (!this.childForm.branchname) {
        this.$message({
          message: "请填写支部名称",
          type: "warning"
        });
        return;
      }
      if (!this.childForm.branchdesc) {
        this.$message({
          message: "请填写支部描述",
          type: "warning"
        });
        return;
      }
      this.childForm.branchcode = this.form.branchcode;
      //添加
      new Promise((response, reject) => {
        addBranch(qs.stringify(this.childForm))
          .then(response => {
            if (response.data.code == 0) {
              this.$message({
                message: response.data.msg,
                type: "success"
              });
              this.getList();
              this.subdivisionVisible = false;
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
    }
  },
  //监听数据
  watch: {
    filterText(val) {
      this.$refs.tree2.filter(val);
    },
    "form.branchname": {
      handler(val, oldVal) {
        if (this.form.branchname && this.form.branchdesc) {
          this.saveDisabled = false;
        } else {
          this.saveDisabled = true;
        }
      }
    },
    "form.branchdesc": {
      handler(val, oldVal) {
        if (this.form.branchname && this.form.branchdesc) {
          this.saveDisabled = false;
        } else {
          this.saveDisabled = true;
        }
      }
    },
    "form.branchcode": {
      handler(val, oldVal) {
        if (val) {
          this.delDisabled = false;
        } else {
          this.delDisabled = true;
        }
      }
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
.left {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px;
  margin-right: 20px;
  border-radius: 5px;
  .el-input {
    margin-bottom: 20px;
  }
  .el-button {
    width: 95%;
    margin-left: 5%;
  }
  .filter-tree {
    height: 430px;
    overflow-y: scroll;
  }
}
.right {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px;
  border-radius: 5px;
  height: 530px;
}
</style>
