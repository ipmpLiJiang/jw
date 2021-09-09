<template>
  <div>

    <el-dialog
      title="添加岗位"
      :visible.sync="dialogVisible"
      :before-close="cancel"
      width="50%"
    >
      <el-form label-width="80px">
        <el-form-item label="指标名称">
          <!-- <el-input
            type="textarea"
            v-model="form.dutyname"
            :rows="4"
          ></el-input> -->
          <Ckeditor ref="ckditor" :type="1" :fatherContent="form.dutyname"></Ckeditor>
        </el-form-item>
        <el-form-item label="所属岗位" v-if="$store.state.user.user.dbtype==2">
          <PostList
            ref="postList"
            @childSelectDepartment="getSelectStation"
            :selectedOptions="stationcode"
          ></PostList>
        </el-form-item>
        <el-form-item label="排序">
          <el-input
            v-model="form.orderid"
            placeholder="请填写序号"
          ></el-input>
        </el-form-item>
        <el-form-item label="指标类型">
          <el-select
            v-model="form.dutytype"
            placeholder="请选择"
            style="width:100%;"
          >
            <el-option
              v-for="item in indicatorOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="优秀分值">
          <el-input
            v-model="form.ascore"
          ></el-input>
        </el-form-item>
        <el-form-item label="良好分值">
          <el-input
            v-model="form.bscore"
          ></el-input>
        </el-form-item>
        <el-form-item label="一般分值">
          <el-input
            v-model="form.cscore"
          ></el-input>
        </el-form-item>
        <el-form-item label="较差分值">
          <el-input
            v-model="form.dscore"
          ></el-input>
        </el-form-item>

      </el-form>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="cancel">取 消</el-button>
        <el-button
          type="primary"
          @click="addSubmit"
        >{{this.form.dutycode ? "修 改" : "添 加"}}</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import PostList from "../common/postList";
import { addDuty, updateDuty } from "@/api/post/indicator";
import qs from "qs";
import Ckeditor from "../common/ckeditor";
const indicatorOptions_1 = [
        {
          value: "0",
          label: "基础指标"
        },
        {
          value: "1",
          label: "岗位职责"
        },
        {
          value: "2",
          label: "重点任务"
        },
        {
          value: "3",
          label: "目标任务"
        }
      ];
      const indicatorOptions_2 = [
        {
          value: "4",
          label: "政治建设"
        },
        {
          value: "5",
          label: "思想建设"
        },
        {
          value: "6",
          label: "组织建设"
        },
        {
          value: "7",
          label: "党建创新"
        },
        {
          value: "8",
          label: "作风建设"
        }
      ];
export default {
  data() {
    return {
      indicatorOptions: this.$store.state.user.user.dbtype==1 ?indicatorOptions_2 :indicatorOptions_1,
      form: {},
      selfDialogVisible: this.dialogVisible
    };
  },
  props: {
    dialogVisible: {
      required: true
    },
    parentForms: {
      required: true
    },
    stationcode: {
      type: Array,
      default: () => [""]
    }
  },
  components: {
    PostList,
    Ckeditor
  },
  created() {
    this.form = Object.assign({}, this.parentForms);
  },
  methods: {
    //初始化
    into() {
      this.score.a = "";
      this.score.b = "";
      this.score.c = "";
      this.score.d = "";
      this.form={}
    },
    //获取部门选择
    getSelectStation(data, row) {
      this.form.stationcode = data;
      this.form.fullstationcode = row.join(",");
    },
    //添加/修改岗位
    addSubmit() {
      if (!this.$refs.ckditor.content) {
        this.$message.warning("请填写指标名称");
        return;
      }
      this.form.dutyname = this.$refs.ckditor.content;
      this.form.dbtype = this.$store.state.user.user.dbtype
      if (!this.form.stationcode && this.$store.state.user.user.dbtype==2) {
        this.$message.warning("请选择岗位名称");
        return;
      }
      if (!this.form.dutytype) {
        this.$message.warning("请选择指标类型");
        return;
      }

      if (this.form.dutycode) {
        //修改
        new Promise((response, reject) => {
          updateDuty(qs.stringify(this.form))
            .then(response => {
              if (response.data.code == 0) {
                this.$message.success(response.data.msg);
                this.cancel();
                this.getList();
                this.into();
              } else {
                this.$message.error(response.data.msg);
              }
            })
            .catch(error => {
              reject(error);
            });
        });
      } else {
        //添加
        new Promise((response, reject) => {
          addDuty(qs.stringify(this.form))
            .then(response => {
              if (response.data.code == 0) {
                this.$message.success(response.data.msg);
                this.cancel();
                this.getList();
                this.into();
              } else {
                this.$message.error(response.data.msg);
              }
            })
            .catch(error => {
              reject(error);
            });
        });
      }
    },
    //取消
    cancel() {
      this.selfDialogVisible = false;
      if (!this.selfDialogVisible) {
        this.$emit("childClose", false);
      }
    },
    //调用父亲查询列表方法
    getList() {
      this.$emit("childGetList", false);
    },
    //指标类型改变
    typeChange(val) {
      if (val == "3") {
        this.form.ascore = "3";
        this.form.bscore = "2";
        this.form.cscore = "1";
        this.form.dscore = "0";
      } else if (val == "2") {
        this.form.ascore = "3";
        this.form.bscore = "2";
        this.form.cscore = "1";
        this.form.dscore = "0";
      } else if (val == "1") {
        this.form.ascore = "3";
        this.form.bscore = "2";
        this.form.cscore = "1";
        this.form.dscore = "0";
      } else if (val == "0") {
        this.form.ascore = "3";
        this.form.bscore = "2";
        this.form.cscore = "1";
        this.form.dscore = "0";
      }
    }
  },
  watch: {
    dialogVisible(val, oldVal) {
      this.selfDialogVisible = val;
    },
    parentForms(val, oldVal) {
      this.form = Object.assign({}, val);
    }
  }
};
</script>
<style scoped>
</style>
