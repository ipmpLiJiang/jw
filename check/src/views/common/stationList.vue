<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogVisible"
      :before-close="cancel"
      width="50%"
      custom-class="score-dialog"
      :loading="loading"
    >
      <div v-show="addType == '3' ? true : false">
        <el-divider content-position="left">指标</el-divider>
        <div v-for="item in dutyArr" :key="item.dutycode">
          <label v-html="item.dutyname"></label>
          <hr />
        </div>
        <el-divider content-position="left">添加评分岗位</el-divider>
      </div>
      <el-input placeholder="输入关键字进行过滤" v-model="filterText">
      </el-input>
      <el-tree
        :data="options"
        show-checkbox
        node-key="commoncode"
        ref="tree"
        highlight-current
        :props="props"
        :filter-node-method="filterNode"
        style="margin-top: 20px"
        v-loading="loading"
        element-loading-text="拼命加载中"
      >
      </el-tree>
      <el-form
        class="demo-ruleForm"
        style="margin-top: 20px"
        v-if="addType == 2"
      >
        <el-form-item label="请选择评分类型：">
          <el-select v-model="byType" placeholder="请选择" style="width: 100%">
            <el-option
              v-for="item in typeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="addSubmit">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { treeStationList } from "@/api/people/people";
import { addScore, addScorred, addDutyScore } from "@/api/score/scoreStation";
import qs from "qs";
export default {
  data() {
    return {
      title: "",
      options: [],
      props: {
        id: "commoncode",
        label: "commonname",
        children: "childDept",
      },
      filterText: "",
      selfDialogVisible: this.dialogVisible,
      selfpaichucode: "",
      typeOptions: [
        {
          value: "A",
          label: "A类评分岗位",
        },
        {
          value: "B",
          label: "B类评分岗位",
        },
        {
          value: "C",
          label: "C类评分岗位",
        },
        {
          value: "D",
          label: "D类评分岗位",
        },
      ],
      byType: "",
      loading: true,
    };
  },
  props: {
    dialogVisible: {
      required: true,
    },
    type: {
      required: true,
    },
    stationcode: {
      required: true,
    },
    paichucode: {
      default: null,
    },
    addType: {
      //1：添加评分岗位 2：添加被评分岗位 3: 添加评分岗位并添加指标
      default: "1",
    },
    dutyArr: {
      default: [],
    },
    isEf: {
      default: 0,
    },
  },
  created() {
    this.getStationList();
  },
  methods: {
    //获取岗位列表
    getStationList() {
      let data = { isEd: this.isEf, stationcode: this.selfpaichucode };
      new Promise((response, reject) => {
        treeStationList(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.options = this.recursionPost(
                this.recursionFunction(response.data.data)
              );
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
            this.loading = false;
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    handleChange(val) {
      this.$emit("childSelectDepartment", val[val.length - 1], val);
    },
    //递归处理后端数据形成岗位tree
    recursionFunction(list) {
      let str = [];
      list.forEach((row) => {
        if (row.stations) {
          if (row.stations.length > 0) {
            if (row.childDept) {
              row.childDept = [...row.childDept, ...row.stations];
            } else {
              row.childDept = [...row.stations];
            }
          }
        }
        if (row.childDept) {
          this.recursionFunction(row.childDept);
        } else {
        }
      });

      return list;
    },
    //递归处理后端数据形成岗位员tree
    recursionPost(list) {
      let str = [];
      list.forEach((row) => {
        if (row.childDept && row.childDept.length > 0) {
          row.childDept.forEach((res) => {
            if (res.userTree) {
              res.childDept = res.userTree;
            }
          });
        }
        if (row.childDept && row.childDept.length > 0) {
          this.recursionFunction(row.childDept);
        }
      });
      return list;
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.commonname.indexOf(value) !== -1;
    },
    //取消
    cancel() {
      this.selfDialogVisible = false;
      if (!this.selfDialogVisible) {
        this.$emit("childClose", false);
      }
    },
    //添加评分岗位
    addSubmit() {
      let data = this.$refs.tree.getCheckedNodes();
      if (data.length > 0) {
        let stationcodeArr = [];
        data.forEach((row) => {
          if (row.stationcode) {
            stationcodeArr.push(row.stationcode);
          }
        });
        data = {};
        data.dbtype = this.$store.state.user.user.dbtype;
        if (this.addType == 1) {
          (data.fullscorringstationcode = stationcodeArr.join(",")),
            (data.scoretype = this.type);
          (data.scorredstationcode = this.stationcode),
            new Promise((response, reject) => {
              addScore(qs.stringify(data))
                .then((response) => {
                  if (response.data.code == 0) {
                    this.$message.success(response.data.msg);
                    this.cancel();
                    this.$emit("childGetLists", "");
                  } else {
                    this.$message({
                      message: response.data.msg,
                      type: "error",
                    });
                  }
                  this.tableLoading = false;
                })
                .catch((error) => {
                  reject(error);
                });
            });
        } else if (this.addType == 2) {
          if (!this.byType) {
            this.$message.warning("请选择评分岗位类型");
            return;
          }
          (data.scorringstationcode = this.stationcode),
            (data.fullscorredstationcode = stationcodeArr.join(",")),
            (data.scoretype = this.byType);
          new Promise((response, reject) => {
            addScorred(qs.stringify(data))
              .then((response) => {
                if (response.data.code == 0) {
                  this.$message.success(response.data.msg);
                  this.cancel();
                  this.$emit("childGetLists", "");
                } else {
                  this.$message({
                    message: response.data.msg,
                    type: "error",
                  });
                }
                this.tableLoading = false;
              })
              .catch((error) => {
                reject(error);
              });
          });
        } else if (this.addType == 3) {
          if (this.dutyArr.length == 0) {
            this.$message.warning("请先勾选指标, 否则无法新增评分岗位.");
            return;
          }
          let dArr = [];
          this.dutyArr.forEach((row) => {
            if (row.dutycode) {
              dArr.push(row.dutycode);
            }
          });
          (data.fulldutycode = dArr.join(",")),
            (data.fullscorringstationcode = stationcodeArr.join(",")),
            (data.scoretype = this.type);
          (data.scorredstationcode = this.stationcode),
            new Promise((response, reject) => {
              addDutyScore(qs.stringify(data))
                .then((response) => {
                  if (response.data.code == 0) {
                    this.$message.success(response.data.msg);
                    this.cancel();
                    this.$emit("childGetLists", "");
                  } else {
                    this.$message({
                      message: response.data.msg,
                      type: "error",
                    });
                  }
                  this.tableLoading = false;
                })
                .catch((error) => {
                  reject(error);
                });
            });
        }
      } else {
        this.$message({
          message: '未选择项，请选择后点击确定.',
          type: "error",
        });
      }
    },
    //清空选中项
    setDefaultChecked() {
      this.$nextTick(() => {
        this.$refs.tree.setCheckedKeys([]); // CheckedArr 一个选中的数组element Ui的东西，关键是看$nextTick的用法
      });
    },
  },
  watch: {
    dialogVisible(val, oldVal) {
      this.selfDialogVisible = val;
      if (val) {
        this.setDefaultChecked();
      }
    },
    filterText(val) {
      this.$refs.tree.filter(val);
    },
    paichucode(val) {
      if (this.selfpaichucode == "" && val != "") {
        this.selfpaichucode = val;
        this.getStationList();
      }
      if (
        this.selfpaichucode != "" &&
        val != "" &&
        this.selfpaichucode != val
      ) {
        this.selfpaichucode = val;
        this.getStationList();
      }
    },
  },
};
</script>
<style scoped>
</style>
