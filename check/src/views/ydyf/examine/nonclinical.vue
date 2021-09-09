<template>
  <div>
    <h4 class="title">非临床人员考核</h4>
    <el-row class="content">
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
              <el-select
            v-model="form.culture"
            placeholder="请选择文化程度"
            style="width: 350px"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
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
           
        
       <!-- <el-row>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">提交</el-button>
          <el-button>取消</el-button>
        </el-form-item>
         </el-row> -->
             <el-col :span="30" offset="6">
        <el-form-item label="" >
          <el-button
            @click="openAdd(scope.row)"
            type="info" plain
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
     <el-col  :span="20" >
         <el-button type="primary" style="margin-left:45%;" @click="onSubmit">确认提交</el-button>
     </el-col>
    </el-row>
   
     <!-- <el-row class="accessory">
      
        <el-row>
         <AddQuarter
      :parentForms="forms"
      :dialogVisible="dialogVisible"
      :isAdd="1"
      :type="1"
      @childClose="childClose"
      @childGetList="getList"
    ></AddQuarter>
    </el-row>
      </el-row> -->
   
  </div>
</template>

<script>
import { updateUserPassword } from "@/api/user/user";
import AddQuarter from "../../user/addQuarter"
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
        culture:''
      },
      dialogVisible:true,
      forms: {},
      getList:{

      },
    };
  },
  components: {
    AddQuarter,
  },
  mounted() {},
  created() {},
  methods: {
    onSubmit() {
    },
    childClose(val) {
      this.dialogVisible = val;
      this.messageDialogVisible = val;
    },
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
.w200 {
  width: 260px;
}
.accessory{
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
