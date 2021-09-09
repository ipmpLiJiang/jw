<template>
  <div class="leader-add">
    <el-form
      ref="form"
      :model="form"
      label-width="100px"
    >
      <el-form-item label="开始时间">
        <el-date-picker
          v-model="form.starttime"
          type="date"
          placeholder="选择开始时间"
          value-format="yyyy-MM-dd"
          clearable
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间">
        <el-date-picker
          v-model="form.endtime"
          type="date"
          placeholder="选择结束时间"
          value-format="yyyy-MM-dd"
          clearable
        >
        </el-date-picker>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
export default {
  data() {
    return {
      form: {},
      isCode: false
    };
  },
  props: ["detailData"],
  created() {
    this.form = this.detailData;
    if (this.form.scorringcode) {
      this.isCode = true;
    } else {
      this.isCode = false;
    }
  },
  methods: {},
  watch: {
    detailData(val) {
      this.form = this.detailData;
      if (this.form.scorringcode) {
        this.isCode = true;
      } else {
        this.isCode = false;
      }
    }
  }
};
</script>
<style lang="scss" scoped>
</style>
<style>
.leader-add .el-form-item__label {
  text-align: left;
}
</style>