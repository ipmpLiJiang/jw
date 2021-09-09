<template>
  <div>
    <el-row class="content">
      <el-col :span="24" class="titleform">填写自我考评信息</el-col>
      <el-form ref="form" :model="form">
        <el-col :span="12">
          <el-form-item label="姓名">
            <el-input
              v-model="form.name"
              style="width: 350px; margin-left: 28px"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="性别">
            <el-input
              v-model="form.sex"
              style="width: 350px; margin-left: 28px"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="出生年月">
            <el-input v-model="form.date" style="width: 350px"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="政治面貌">
            <el-input
              v-model="form.politicsStatus"
              style="width: 350px"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="文化程度">
            <el-input v-model="form.station" style="width: 350px"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="现聘岗位">
            <el-input v-model="form.status" style="width: 350px"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="聘用时间">
            <el-input
              v-model="form.workingTtime"
              style="width: 350px"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="从事工作">
            <el-input v-model="form.job" style="width: 350px"></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="30" :offset="6">
          <el-form-item label="">
            <el-button
              @click="openAdd(scope.row)"
              type="info"
              plain
              style="margin-left: 55px"
              >请填写以下本年度医德医风自评及工作总结</el-button
            >
          </el-form-item>
        </el-col>
      </el-form>

      <el-col>
        <AddQuarter
          :parentForms="forms"
          :dialogVisible="dialogVisible"
          :isAdd="1"
          :type="1"
          @childClose="childClose"
          @childGetList="getList"
        ></AddQuarter>
      </el-col>
      <el-col :span="20">
        <el-button type="primary" style="margin-left: 45%">确认提交</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { updateUserPassword } from "@/api/user/user";
import AddQuarter from "../../user/addQuarter";
import qs from "qs";
export default {
  props: {},
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
  computed: {
      AddQuarter,
  },
  created() {},
  mounted() {},
  watch: {},
  methods: {
       onSubmit() {
  
    },
    childClose(val) {
      this.dialogVisible = val;
      this.messageDialogVisible = val;
    },
  },
  components: {},
};
</script>

<style scoped lang="scss">
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
