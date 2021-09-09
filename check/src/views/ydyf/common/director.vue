<template>
  <div>
    <el-row class="content">
      <el-col :span="24" class="titleform">请部门负责人填写意见</el-col>
      <el-col :span="3"> <h1>部门负责人意见：</h1> </el-col>
      <el-col :span="8">
        <el-input
          class="opinion"
          type="textarea"
          placeholder="请部门负责人填写意见"
          v-model="textarea"
          show-word-limit
          style="width: 600px"
          :rows="6"
        >
        </el-input>
        <el-button type="primary" class="submitOpinion">提交</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { updateUserPassword } from "@/api/user/user";
import qs from "qs";
export default {
  data() {
    return {
      form: {
        name: "",
        sex: "",
        date: "",
        politicsStatus: "",
        station: "",
        status: "",
        workingTtime: "",
        job: "",
      },
      dialogVisible: true,
      forms: {},
      getList: {},
      textarea: "",
      checkList: ["选中且禁用", "复选框 A"],
    };
  },
  components: {

  },
  mounted() {},
  created() {},
  methods: {
 
  
  },
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
  margin: 20px;
  border-radius: 4px;
  .titleform {
    height: 50px;
    line-height: 50px;
    border-bottom: 1px solid #eaeaea;
    padding: 0px 10px;
    font-weight: 500;
    font-size: 16px;
    color: #fff;
    margin-bottom: 20px;
    background-color: #409eff;
  }
  .el-checkbox-group {
    height: 200px;
  }
  .el-pagination {
    margin: 20px auto;
    text-align: center;
  }
  .table-expand {
    padding: 0px;
    .el-form-item {
      margin-right: 0;
      margin-bottom: 10px;
      width: 33.33%;
    }
    .el-select {
      width: 185px;
    }
  }
  h1 {
    font-size: 16px;
  }
  .submitOpinion {
    position: absolute;
    right: 15%;
    top: 67%;
    width: 107px;
  }
}
.w200 {
  width: 260px;
}
.accessory {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px;
  margin: 20px;
  border-radius: 4px;
  .el-pagination {
    margin: 20px auto;
    text-align: center;
  }
  .table-expand {
    padding: 0px;
    .el-form-item {
      margin-right: 0;
      margin-bottom: 10px;
      width: 33.33%;
    }
    .el-select {
      width: 185px;
    }
  }
}
</style>
