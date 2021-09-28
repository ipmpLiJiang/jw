<template>
  <div>
    <el-row
      v-loading="pageLoading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
    >
      <el-col :span="24">
        <ul class="personal-information">
          <li>
            <div class="label">姓名:</div>
            <div class="value">{{detailData.username}}({{detailData.moneycard}})</div>
          </li>
          <li>
            <div class="label">标题:</div>
            <div class="value">{{detailData.title}}</div>
          </li>
          <li>
            <div class="label">部门:</div>
            <div class="value">{{detailData.departmentname}}</div>
          </li>
          <li>
            <div class="label">岗位:</div>
            <div class="value">{{detailData.stationname}}</div>
          </li>
          <li>
            <div class="label">年份:</div>
            <div class="value">{{detailData.year}}</div>
          </li>
          <li>
            <div class="label">季度:</div>
            <div class="value">第{{detailData.month}}季度</div>
          </li>
          <!-- <li>
            <div class="label">照片:</div>
            <div class="value">
              <el-button
                type="text"
                style="padding:0"
              >点击查看</el-button>
            </div>
          </li> -->
          <li v-if="detailData.stationcode">
            <div class="label">职责:</div>
            <div class="value">
              <el-button
                type="text"
                style="padding:0"
                @click="respDialogVisible = true"
              >点击查看</el-button>
            </div>
          </li>
          <li v-if="detailData.filename">
            <div class="label">附件:</div>
            <div
              class="value"
              style="cursor:pointer;"
              @click="preview(detailData.savepath)"
            ><a>{{detailData.filename}}</a></div>
          </li>
          <li class="w100">
            <div class="label">季度内容:</div>
            <div
              class="value"
              v-html="detailData.content"
            ></div>
          </li>
          <!-- <li class="w100 operation">
            <el-button type="primary">历史绩效</el-button>
            <el-button type="primary">职责描述</el-button>
          </li> -->
        </ul>
      </el-col>
      <el-col :span="24">
        <ul class="indicator">
          <li class="li-title">
            <div class="title">政治建设(总分15)</div>
          </li>
          <li
            v-for="(item,index) in dutyJichu" :key="'a'+index"
            style="border-bottom:1px solid #409eff"
          >
            <el-row style="padding-bottom:5px;">
              <el-col :span="19">
                <span v-html="item.dutyname"></span>
              </el-col>
            </el-row>
            <el-row class="zpsm">
              <el-col :span="2">
                <font style="color:red"> * </font>自评说明：
                  <br v-if="detailData.isedit == 0"><font v-if="detailData.isedit == 0">(限制80字)</font>
              </el-col>
              <el-col :span="17">
                <el-input v-show="detailData.isedit == 0?true:false" maxlength="80" type="textarea" v-model="item.zpsm"></el-input>
                <div v-show="detailData.isedit == 1?true:false">{{item.zpsm}}</div>
              </el-col>
            </el-row>
          </li>
          <li class="li-title">
            <div class="title">思想建设(总分20)</div>
          </li>
          <li
            v-for="(item,index) in dutyYiban" :key="'b'+index"
            style="border-bottom:1px solid #409eff"
          >
            <el-row style="padding-bottom:5px;">
              <el-col :span="19">
                <span v-html="item.dutyname"></span>
              </el-col>
            </el-row>
            <el-row class="zpsm">
              <el-col :span="2">
                <font style="color:red"> * </font>自评说明：
                  <br v-if="detailData.isedit == 0"><font v-if="detailData.isedit == 0">(限制80字)</font>
              </el-col>
              <el-col :span="17">
                <el-input v-show="detailData.isedit == 0?true:false" maxlength="80" type="textarea" v-model="item.zpsm"></el-input>
                <div v-show="detailData.isedit == 1?true:false">{{item.zpsm}}</div>
              </el-col>
            </el-row>
          </li>
          <li class="li-title">
            <div class="title">组织建设(总分25)</div>
          </li>
          <li
            v-for="(item,index) in dutyZhongdian" :key="'c'+index"
            style="border-bottom:1px solid #409eff"
          >
            <el-row style="padding-bottom:5px;">
              <el-col :span="19">
                <span v-html="item.dutyname"></span>
              </el-col>
            </el-row>
            <el-row class="zpsm">
              <el-col :span="2">
                <font style="color:red"> * </font>自评说明：
                  <br v-if="detailData.isedit == 0"><font v-if="detailData.isedit == 0">(限制80字)</font>
              </el-col>
              <el-col :span="17">
                <el-input v-show="detailData.isedit == 0?true:false" maxlength="80" type="textarea" v-model="item.zpsm"></el-input>
                <div v-show="detailData.isedit == 1?true:false">{{item.zpsm}}</div>
              </el-col>
            </el-row>
          </li>
          <li class="li-title">
            <div class="title">党建创新(总分25)</div>
          </li>
          <li
            v-for="(item,index) in dutyMubiao" :key="'d'+index"
            style="border-bottom:1px solid #409eff"
          >
            <el-row style="padding-bottom:5px;">
              <el-col :span="19">
                <span v-html="item.dutyname"></span>
              </el-col>
            </el-row>
            <el-row class="zpsm">
              <el-col :span="2">
                <font style="color:red"> * </font>自评说明：
                  <br v-if="detailData.isedit == 0"><font v-if="detailData.isedit == 0">(限制80字)</font>
              </el-col>
              <el-col :span="17">
                <el-input v-show="detailData.isedit == 0?true:false" maxlength="80" type="textarea" v-model="item.zpsm"></el-input>
                <div v-show="detailData.isedit == 1?true:false">{{item.zpsm}}</div>
              </el-col>
            </el-row>
          </li>
           <li class="li-title">
            <div class="title">作风建设(总分25)</div>
          </li>
           <li
            v-for="(item,index) in dutyZuofeng" :key="'e'+index"
            style="border-bottom:1px solid #409eff"
          >
            <el-row style="padding-bottom:5px;">
              <el-col :span="19">
                <span v-html="item.dutyname"></span>
              </el-col>
            </el-row>
            <el-row class="zpsm">
              <el-col :span="2">
                <font style="color:red"> * </font>自评说明：
                  <br v-if="detailData.isedit == 0"><font v-if="detailData.isedit == 0">(限制80字)</font>
              </el-col>
              <el-col :span="17">
                <el-input v-show="detailData.isedit == 0?true:false" maxlength="80" type="textarea" v-model="item.zpsm"></el-input>
                <div v-show="detailData.isedit == 1?true:false">{{item.zpsm}}</div>
              </el-col>
            </el-row>
          </li>
          <li
            class="w100 operation"
            v-if="detailData.isedit == 1 ? false : true"
          >
            <div v-if="dutyJichu.length == 0 && dutyJichu.length == 0 && dutyZhongdian.length == 0 && dutyMubiao.length == 0 && dutyZuofeng.length == 0">
              <el-button
                type="default"
                disabled="disabled"
              >没有检测到该岗位题目，无法考核</el-button>
            </div>
            <div v-else>
              <el-button
                type="primary"
                @click="submitAssess"
                :loading="submitLoading"
              >提交</el-button>
            </div>
          </li>
        </ul>
      </el-col>
    </el-row>
    <el-dialog
      title="职责"
      :visible.sync="respDialogVisible"
      width="50%"
      v-if="detailData.stationcode"
    >
      <el-form
        ref="form"
        label-width="130px"
      >
        <el-form-item
          label="职责描述"
          class="form-menu"
        >
          <el-input
            type="textarea"
            v-model="form.dutydesc"
            :rows="4"
            readonly="readonly"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="工作关系-内部"
          class="form-menu"
        >
          <span>{{form.relation1}}</span>
        </el-form-item>
        <el-form-item
          label="工作关系-外部"
          class="form-menu"
        >
          <span>{{form.relation2}}</span>
        </el-form-item>
        <el-form-item
          label="可晋身至此的岗位"
          class="form-menu"
        >
          <span>{{form.station1}}</span>
        </el-form-item>
        <el-form-item
          label="可相互轮换的岗位"
          class="form-menu"
        >
          <span>{{form.station2}}</span>
        </el-form-item>
        <el-form-item
          label="学历要求"
          class="form-menu"
        >
          <span>{{form.xueli}}</span>
        </el-form-item>
        <el-form-item
          label="职业资格要求"
          class="form-menu"
        >
          <span>{{form.zhiyezige}}</span>
        </el-form-item>
        <el-form-item
          label="培训要求"
          class="form-menu"
        >
          <span>{{form.peixun}}</span>
        </el-form-item>
        <el-form-item
          label="工作经验"
          class="form-menu"
        >
          <span>{{form.gongzuojingyan}}</span>
        </el-form-item>
        <el-form-item
          label="个性特征要求"
          class="form-menu"
        >
          <span>{{form.gexingtezheng}}</span>
        </el-form-item>
        <el-form-item
          label="基本技能素质要求"
          class="form-menu"
        >
          <span>{{form.jibenjineng}}</span>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
import { getDetail2, scoring2 } from "@/api/home/home";
import { download } from "@/api/common/common";
import qs from "qs";
export default {
  data() {
    return {
      form: {},
      totalScore: 0,
      scoreShow: false,
      respDialogVisible: false,
      detailData: {},
      dutyJichu: [],
      dutyYiban: [],
      dutyZhongdian: [],
      dutyMubiao: [],
      dutyZuofeng: [],
      dutySm: [],
      pageLoading: true,
      submitLoading: false,
      searchLoading: false
    };
  },
  created() {
    this.getDetail();
  },
  methods: {
    getDetail() {
      let data = {
        employeecode: this.$route.query.userCode,
        dbtype: this.$store.state.user.user.dbtype
      };
      if (this.$route.query.year && this.$route.query.month) {
        data.year = this.$route.query.year;
        data.month = this.$route.query.month;
      }
      new Promise((response, reject) => {
        getDetail2(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.detailData = response.data.data.detail;
              this.form = response.data.data.stations;
              this.dutyJichu = [];
              this.dutyYiban = [];
              this.dutyZhongdian = [];
              this.dutyMubiao = [];
              this.dutyZuofeng = [];
              this.dutySm = [];
              this.dutyJichu = response.data.data.dutyJichu;
              this.dutyYiban = response.data.data.dutyYiban;
              this.dutyZhongdian = response.data.data.dutyZhongdian;
              this.dutyMubiao = response.data.data.dutyMubiao;
              this.dutyZuofeng = response.data.data.dutyZuofeng;
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
            this.tableLoading = false;
            this.pageLoading = false;
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    //提交考核
    submitAssess() {
      this.submitLoading = true;
      let data = {
        employeecode: this.detailData.employeecode
      };
      //遍历基础和关键打分数据
      data.dutySm = [];
      this.dutyJichu.forEach(row => {
        data.dutySm.push({ topicId: row.dutycode, zpsm: row.zpsm });
      });
      this.dutyYiban.forEach(row => {
        data.dutySm.push({ topicId: row.dutycode, zpsm: row.zpsm });
      });
      this.dutyZhongdian.forEach(row => {
        data.dutySm.push({ topicId: row.dutycode, zpsm: row.zpsm });
      });
      this.dutyMubiao.forEach(row => {
        data.dutySm.push({ topicId: row.dutycode, zpsm: row.zpsm });
      });
        this.dutyZuofeng.forEach(row => {
        data.dutySm.push({ topicId: row.dutycode, zpsm: row.zpsm });
      });
      data.dutySm = JSON.stringify(data.dutySm);
      data.month = this.detailData.month;
      data.year =this.detailData.year;
      data.dbtype =this.$store.state.user.user.dbtype;
      new Promise((response, reject) => {
        scoring2(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.$message({
                message: response.data.msg,
                type: "success"
              });
              this.$router.push({
                path: "/home/userSelf",
                query: {}
              });
            } else {
              this.$message({
                message: response.data.msg,
                type: "error"
              });
            }
            this.tableLoading = false;
            this.submitLoading = false;
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    //预览
    preview(path) {
      window.location.href =
        process.env.VUE_APP_ITEM_NAME +
        "summaryattachment/download?savepath=" +
        path;
    }
  }
};
</script>
<style scoped lang="scss">
.personal-information {
  display: flex;
  flex-wrap: wrap;
  background: #fff;
  margin: 10px;
  padding-bottom: 20px;
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  .w100 {
    width: 100%;
  }
  li {
    width: 50%;
    display: flex;
    margin-top: 20px;
    padding: 0 20px;
    .label {
      margin-right: 10px;
      min-width: 70px;
      text-align: right;
    }
  }
}
.operation {
  border-top: 1px solid #f1f1f1;
  padding-top: 20px !important;
}
.indicator {
  display: flex;
  flex-wrap: wrap;
  background: #fff;
  margin: 10px;
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  .li-title {
    background: #409eff;
    padding: 15px 20px;

    color: #fff;
    margin-bottom: 20px;
  }
  .title {
    font-size: 16px;
    font-weight: 500;
  }
  li {
    width: 100%;
    margin-bottom: 10px;
    padding: 0 20px;
    .label {
      font-weight: 500;
      margin-bottom: 10px;
    }
    .core {
      width: 20%;
      display: inline-block;
    }
    .core-input {
      width: 150px;
    }
  }
}
.search {
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  background: #fff;
  padding: 20px 0;
  margin: 10px;
  box-sizing: border-box;
  .el-form-item {
    margin: 0px;
  }
  .el-button {
    margin-left: 10px;
  }
}
.form-menu {
  margin-bottom: 0px;
}
.zpsm {
  padding-top:10px;
  padding-bottom:15px;
}
</style>
