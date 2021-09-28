

<template>

  <div>
    <el-dialog
      title="滑动设置评分人权重占比"
      :visible.sync="selfDialogVisible"
      width="700px"
      center
    >
      <el-col class="slider" v-show="dbtype=='1'?true:false">
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
      <el-col class="slider" v-show="dbtype=='2'?true:false">
        <el-col
          :span="5"
          class="title"
        >A类评分人权重:</el-col>
        <el-col :span="19">
          <el-slider
            v-model="form.aratio2"
            :format-tooltip="formatTooltip"
            :step="5"
            show-stops
            show-input
          >
          </el-slider>
        </el-col>
      </el-col>
      <el-col class="slider" v-show="dbtype=='1'?true:false">
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
      <el-col class="slider" v-show="dbtype=='2'?true:false">
        <el-col
          :span="5"
          class="title"
        >B类评分人权重:</el-col>
        <el-col :span="19">
          <el-slider
            v-model="form.bratio2"
            :format-tooltip="formatTooltip"
            :step="5"
            show-stops
            show-input
          >
          </el-slider>
        </el-col>
      </el-col>
      <el-col class="slider" v-show="dbtype=='1'?true:false">
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
      <el-col class="slider" v-show="dbtype=='2'?true:false">
        <el-col
          :span="5"
          class="title"
        >C类评分人权重:</el-col>
        <el-col :span="19">
          <el-slider
            v-model="form.cratio2"
            :format-tooltip="formatTooltip"
            :step="5"
            show-stops
            show-input
          >
          </el-slider>
        </el-col>
      </el-col>
      <el-col class="slider" v-show="dbtype=='1'?true:false">
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
      <el-col class="slider" v-show="dbtype=='2'?true:false">
        <el-col
          :span="5"
          class="title"
        >D类评分人权重:</el-col>
        <el-col :span="19">
          <el-slider
            v-model="form.dratio2"
            :format-tooltip="formatTooltip"
            :step="5"
            show-stops
            show-input
          >
          </el-slider>
        </el-col>
      </el-col>
      <el-col class="slider" v-show="dbtype=='1'?true:false">
        <el-col
          :span="5"
          class="title"
        >E类评分人权重:</el-col>
        <el-col :span="19">
          <el-slider
            v-model="form.eratio"
            :format-tooltip="formatTooltip"
            :step="5"
            show-stops
            show-input
          >
          </el-slider>
        </el-col>
      </el-col>
      <el-col class="slider" v-show="dbtype=='2'?true:false">
        <el-col
          :span="5"
          class="title"
        >E类评分人权重:</el-col>
        <el-col :span="19">
          <el-slider
            v-model="form.eratio2"
            :format-tooltip="formatTooltip"
            :step="5"
            show-stops
            show-input
          >
          </el-slider>
        </el-col>
      </el-col>
      <el-col class="slider" v-show="dbtype=='1'?true:false">
        <el-col
          :span="5"
          class="title"
        >F类评分人权重:</el-col>
        <el-col :span="19">
          <el-slider
            v-model="form.fratio"
            :format-tooltip="formatTooltip"
            :step="5"
            show-stops
            show-input
          >
          </el-slider>
        </el-col>
      </el-col>
      <el-col class="slider" v-show="dbtype=='2'?true:false">
        <el-col
          :span="5"
          class="title"
        >F类评分人权重:</el-col>
        <el-col :span="19">
          <el-slider
            v-model="form.fratio2"
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
      dbtype: this.$store.state.user.user.dbtype,
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
      let totalScore = 0
      if(this.dbtype == '1'){
        totalScore = this.form.aratio +
        this.form.bratio +
        this.form.cratio +
        this.form.dratio +
        this.form.eratio +
        this.form.fratio;
      } else{
        totalScore = this.form.aratio2 +
        this.form.bratio2 +
        this.form.cratio2 +
        this.form.dratio2 +
        this.form.eratio2 +
        this.form.fratio2;
      }
      if (totalScore != 100) {
        this.$message.warning("权重总数必须为100");
        return;
      }
      let data = {
        usercode: this.form.usercode};
        data.aratio = this.form.aratio,
        data.bratio = this.form.bratio,
        data.cratio = this.form.cratio,
        data.dratio = this.form.dratio,
        data.eratio = this.form.eratio,
        data.fratio = this.form.fratio
        data.aratio2 = this.form.aratio2,
        data.bratio2 = this.form.bratio2,
        data.cratio2 = this.form.cratio2,
        data.dratio2 = this.form.dratio2,
        data.eratio2 = this.form.eratio2,
        data.fratio2 = this.form.fratio2
        
      
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
