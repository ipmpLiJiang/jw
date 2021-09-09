

<template>

  <div>
    <el-dialog
      title="滑动设置评分人权重占比"
      :visible.sync="selfDialogVisible"
      width="700px"
      center
    >
      <el-col class="slider">
        <el-col
          :span="5"
          class="title"
        >A类评分人权重:</el-col>
        <el-col :span="19">
          <el-slider
            v-model="form.aratio"
            :format-tooltip="formatTooltip"
            :step="5"
            show-stops
            show-input
          >
          </el-slider>
        </el-col>
      </el-col>
      <el-col class="slider">
        <el-col
          :span="5"
          class="title"
        >B类评分人权重:</el-col>
        <el-col :span="19">
          <el-slider
            v-model="form.bratio"
            :format-tooltip="formatTooltip"
            :step="5"
            show-stops
            show-input
          >
          </el-slider>
        </el-col>
      </el-col>
      <el-col class="slider">
        <el-col
          :span="5"
          class="title"
        >C类评分人权重:</el-col>
        <el-col :span="19">
          <el-slider
            v-model="form.cratio"
            :format-tooltip="formatTooltip"
            :step="5"
            show-stops
            show-input
          >
          </el-slider>
        </el-col>
      </el-col>
      <el-col class="slider">
        <el-col
          :span="5"
          class="title"
        >D类评分人权重:</el-col>
        <el-col :span="19">
          <el-slider
            v-model="form.dratio"
            :format-tooltip="formatTooltip"
            :step="5"
            show-stops
            show-input
          >
          </el-slider>
        </el-col>
      </el-col>
      <span
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="cancel">取 消</el-button>
        <el-button
          type="primary"
          @click="editSubmit"
        >确 定</el-button>
      </span>
    </el-dialog>
  </div>

</template>
<script>
import { treelist } from "@/api/people/people";
import { updateWeight } from "@/api/score/score";
import qs from "qs";
export default {
  data() {
    return {
      selfDialogVisible: this.swDialogVisible,
      form: {}
    };
  },
  props: {
    swDialogVisible: {
      required: true
    },
    parentForms: {
      required: true
    }
  },
  created() {
    this.form = Object.assign({}, this.parentForms);
  },
  methods: {
    formatTooltip(val) {
      return val + "%";
    },
    //取消
    cancel() {
      this.selfDialogVisible = false;
      if (!this.selfDialogVisible) {
        this.$emit("childClose", false);
      }
    },
    //修改评分人
    editSubmit() {
      let totalScore =
        this.form.aratio +
        this.form.bratio +
        this.form.cratio +
        this.form.dratio;
      if (totalScore != 100) {
        this.$message.warning("权重总数必须为100");
        return;
      }
      let data = {
        usercode: this.form.usercode,
        aratio: this.form.aratio,
        bratio: this.form.bratio,
        cratio: this.form.cratio,
        dratio: this.form.dratio
      };
      new Promise((response, reject) => {
        updateWeight(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.$message.success(response.data.msg);
              this.cancel();
              this.$emit("childGetList");
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
    }
  },
  watch: {
    selfDialogVisible(val, oldVal) {
      if (!val) {
        this.$emit("childClose", val);
      }
    },
    swDialogVisible(val, oldVal) {
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
