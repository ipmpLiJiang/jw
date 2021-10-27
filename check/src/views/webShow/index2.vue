<template>
  <div class="question">
    
    <el-row>
      <el-col class="title">精神卫生中心360°考核系统</el-col>
      <el-col class="content">
        <el-col class="toptic-title">
          基本信息
        </el-col>
        <el-col>
          <div class="season-text">
            <div class="form-msg">
              <span>姓名：</span>
              <span>{{detailData.username}}</span>
            </div>
            <div class="form-msg">
              <span>账号：</span>
              <span>{{detailData.usercode}}</span>
            </div>
            <div class="form-msg">
              <span>部门：</span>
              <span>{{detailData.departmentname}}</span>
            </div>
            <div class="form-msg">
              <span>岗位：</span>
              <span>{{detailData.stationname}}</span>
            </div>
          </div>
        </el-col>
        <!-- <el-col class="toptic-title">
          季结内容
        </el-col>
        <el-col>
          <div class="season-text">
            <el-collapse
              v-model="activeName"
              style="border:0"
            >
              <el-collapse-item
                :title="detailData.title"
                name="1"
              >
                <div class="season-content" v-html="detailData.content"></div>
              </el-collapse-item>
            </el-collapse>
          </div>
        </el-col> -->
        <el-col class="toptic-title">
          政治建设
        </el-col>
        <el-col
          class="list"
          v-for="(item,index) in dutyJichu" :key="'a'+index"
        >
          <el-col class="question-title">
            <el-col
              :span="24"
              class="toptic"
            ><span v-html="item.dutyname"></span></el-col>
          </el-col>
          <el-col class="question-title">
            <el-col 
              :span="23"
              class="toptic">
              自评：&nbsp;&nbsp;
              {{item.zpsm}}
            </el-col>
          </el-col>
          <el-col class="answer on-line-top">
            <el-col class="radio">
              <el-radio-group
                v-model="item.score"
                size="small"
                @change="redioChange"
                :disabled="detailData.isedit == 1 ? true : false"
              >
                <el-radio-button :label="item.ascore">优秀</el-radio-button>
                <el-radio-button :label="item.bscore">良好</el-radio-button>
                <el-radio-button :label="item.cscore">一般</el-radio-button>
                <el-radio-button :label="item.dscore">较差</el-radio-button>
              </el-radio-group>
            </el-col>
          </el-col>
          <el-col class="question-title" v-show="item.score == item.dscore?true:false">
            <el-col 
              :span="24"
              class="toptic">
              <font style="color:red"> * </font>差评：
              <el-input v-show="detailData.isedit == 0 ? true :false" style="width:82%" 
              maxlength="80" rows="3" show-word-limit type="textarea" v-model="item.cpsm"></el-input>
              <span v-show="detailData.isedit == 1 ? true :false">{{item.cpsm}}</span>
            </el-col>
          </el-col>
        </el-col>
        <el-col
          class="toptic-title"
          style="margin-top:.5rem;"
        >
          思想建设
        </el-col>
        <el-col
          class="list"
          v-for="(item,index) in dutyYiban" :key="'b'+index"
        >
          <el-col class="question-title">
            <el-col
              :span="24"
              class="toptic"
            ><span v-html="item.dutyname"></span></el-col>
          </el-col>
          <el-col class="question-title">
            <el-col 
              :span="23"
              class="toptic">
              自评：&nbsp;&nbsp;
              {{item.zpsm}}
            </el-col>
          </el-col>
          <el-col class="answer on-line-top">
            <el-col class="radio">
              <el-radio-group
                v-model="item.score"
                size="small"
                @change="redioChange"
                :disabled="detailData.isedit == 1 ? true : false"
              >
                <el-radio-button :label="item.ascore">优秀</el-radio-button>
                <el-radio-button :label="item.bscore">良好</el-radio-button>
                <el-radio-button :label="item.cscore">一般</el-radio-button>
                <el-radio-button :label="item.dscore">较差</el-radio-button>
              </el-radio-group>
            </el-col>
          </el-col>
          <el-col class="question-title" v-show="item.score == item.dscore?true:false">
            <el-col 
              :span="24"
              class="toptic">
              <font style="color:red"> * </font>差评：
              <el-input v-show="detailData.isedit == 0 ? true :false" style="width:82%" 
              maxlength="80" rows="3" show-word-limit type="textarea" v-model="item.cpsm"></el-input>
              <span v-show="detailData.isedit == 1 ? true :false">{{item.cpsm}}</span>
            </el-col>
          </el-col>
        </el-col>
        <el-col
          class="toptic-title"
          style="margin-top:.5rem;"
        >
          组织建设
        </el-col>
        <el-col
          class="list"
          v-for="(item,index) in dutyZhongdian" :key="'c'+index"
        >
          <el-col class="question-title">
            <el-col
              :span="24"
              class="toptic"
            ><span v-html="item.dutyname"></span></el-col>
          </el-col>
          <el-col class="question-title">
            <el-col 
              :span="23"
              class="toptic">
              自评：&nbsp;&nbsp;{{item.zpsm}}
            </el-col>
          </el-col>
          <el-col class="answer on-line-top">
            <el-col class="radio">
              <el-radio-group
                v-model="item.score"
                size="small"
                @change="redioChange"
                :disabled="detailData.isedit == 1 ? true : false"
              >
                <el-radio-button :label="item.ascore">优秀</el-radio-button>
                <el-radio-button :label="item.bscore">良好</el-radio-button>
                <el-radio-button :label="item.cscore">一般</el-radio-button>
                <el-radio-button :label="item.dscore">较差</el-radio-button>
              </el-radio-group>
            </el-col>
          </el-col>
          <el-col class="question-title" v-show="item.score == item.dscore?true:false">
            <el-col 
              :span="24"
              class="toptic">
              <font style="color:red"> * </font>差评：
              <el-input v-show="detailData.isedit == 0 ? true :false" style="width:82%" 
              maxlength="80" rows="3" show-word-limit type="textarea" v-model="item.cpsm"></el-input>
              <span v-show="detailData.isedit == 1 ? true :false">{{item.cpsm}}</span>
            </el-col>
          </el-col>
        </el-col>
        <el-col
          class="toptic-title"
          style="margin-top:.5rem;"
        >
          党建创新
        </el-col>
        <el-col
          class="list"
          v-for="(item,index) in dutyMubiao" :key="'d'+index"
        >
          <el-col class="question-title">
            <el-col
              :span="24"
              class="toptic"
            ><span v-html="item.dutyname"></span></el-col>
          </el-col>
          <el-col class="question-title">
            <el-col 
              :span="23"
              class="toptic">
              自评：&nbsp;&nbsp;{{item.zpsm}}
            </el-col>
          </el-col>
          <el-col class="answer on-line-top">
            <el-col class="radio">
              <el-radio-group
                v-model="item.score"
                size="small"
                @change="redioChange"
                :disabled="detailData.isedit == 1 ? true : false"
              >
                <el-radio-button :label="item.ascore">优秀</el-radio-button>
                <el-radio-button :label="item.bscore">良好</el-radio-button>
                <el-radio-button :label="item.cscore">一般</el-radio-button>
                <el-radio-button :label="item.dscore">较差</el-radio-button>
              </el-radio-group>
            </el-col>
          </el-col>
          <el-col class="question-title" v-show="item.score == item.dscore?true:false">
            <el-col 
              :span="24"
              class="toptic">
              <font style="color:red"> * </font>差评：
              <el-input v-show="detailData.isedit == 0 ? true :false" style="width:82%" 
              maxlength="80" rows="3" show-word-limit type="textarea" v-model="item.cpsm"></el-input>
              <span v-show="detailData.isedit == 1 ? true :false">{{item.cpsm}}</span>
            </el-col>
          </el-col>
        </el-col>
        <el-col
          class="toptic-title"
          style="margin-top:.5rem;"
        >
          作风建设
        </el-col>
        <el-col
          class="list"
          v-for="(item,index) in dutyZuofeng" :key="'e'+index"
        >
          <el-col class="question-title">
            <el-col
              :span="24"
              class="toptic"
            ><span v-html="item.dutyname"></span></el-col>
          </el-col>
          <el-col class="question-title">
            <el-col 
              :span="23"
              class="toptic">
              自评：&nbsp;&nbsp;{{item.zpsm}}
            </el-col>
          </el-col>
          <el-col class="answer on-line-top">
            <el-col class="radio">
              <el-radio-group
                v-model="item.score"
                size="small"
                @change="redioChange"
                :disabled="detailData.isedit == 1 ? true : false"
              >
                <el-radio-button :label="item.ascore">优秀</el-radio-button>
                <el-radio-button :label="item.bscore">良好</el-radio-button>
                <el-radio-button :label="item.cscore">一般</el-radio-button>
                <el-radio-button :label="item.dscore">较差</el-radio-button>
              </el-radio-group>
            </el-col>
          </el-col>
          <el-col class="question-title" v-show="item.score == item.dscore?true:false">
            <el-col 
              :span="24"
              class="toptic">
              <font style="color:red"> * </font>差评：
              <el-input v-show="detailData.isedit == 0 ? true :false" style="width:82%" 
              maxlength="80" rows="3" show-word-limit type="textarea" v-model="item.cpsm"></el-input>
              <span v-show="detailData.isedit == 1 ? true :false">{{item.cpsm}}</span>
            </el-col>
          </el-col>
        </el-col>
      </el-col>
      <el-col class="edit-btn" v-show="detailData.isedit == 0 ? true :false">
        <div v-if="laberror==''?false:true">
          <font style="color:red">{{laberror}}</font>
        </div>
        <el-button
          type="primary"
          size="small"
          @click="submitAssess"
        >确认提交</el-button>
      </el-col>
    </el-row>

  </div>
</template>

<script>
// import { getTotalScore, mobilGetDetail } from "@/api/mobile/check";
import { getDetail, scoring } from "@/api/home/home";
import qs from "qs";
export default {
  data() {
    return {
      activeName: "1",
      show: false,
      value: "",
      detailData: {},
      dutyJichu: [],
      dutyYiban: [],
      dutyZhongdian: [],
      dutyMubiao: [],
      dutyZuofeng: [],
      laberror: '',
      submitLoading: false,
      totalScore: 0,
      year:"",
      month:""
    };
  },
  components: {},
  mounted() {},
  created() {
    this.getDetail();
  },
  methods: {
    //select打开禁止页面滑动
    showHide(val) {
      if (val) {
        this.show = true;
      } else {
        this.show = false;
      }
    },
    redioChange () {
      this.laberror = ''
    },
    // applyMakeSure() {
    //   if (this.compute()) {
    //     this.dialogVisible = true;
    //   }
    // },
    getDetail() {
      let data = {
        employeecode: this.$route.query.userCode,
        scorringcode: this.$route.query.scorringCode,
        isvalidation: 1,
        dbtype: this.$store.state.user.user.dbtype
      };
      new Promise((response, reject) => {
        getDetail(qs.stringify(data))
          .then(response => {
            if (response.data.code == 0) {
              this.detailData = response.data.data.detail;
              this.dutyJichu = [];
              this.dutyYiban = [];
              this.dutyZhongdian = [];
              this.dutyMubiao = [];
              this.dutyZuofeng = [];
              this.dutyJichu = response.data.data.dutyJichu;
              this.dutyYiban = response.data.data.dutyYiban;
              this.dutyZhongdian = response.data.data.dutyZhongdian;
              this.dutyMubiao = response.data.data.dutyMubiao;
              this.dutyZuofeng = response.data.data.dutyZuofeng;
              this.year = response.data.data.detail.year;
              this.month = response.data.data.detail.month;
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
    getMaxMin (obj) {
      // 优秀ascore,良好bscore,一般cscore,较差dscore,填入score
      let yx = 0,lh = 0,yb = 0,jc = 0,yx1 = -100,lh1 = -100,yb1 = -100,jc1 = -100;
      let arScore = [];
      if (obj.ascore != "" && obj.ascore != null && obj.ascore != undefined) {
        if (isNaN(obj.ascore)) {
          arScore= obj.ascore.split('-');
          if (arScore.length >= 2) {
            yx = arScore[0];
            yx1 = arScore[1];
          } 
          if (arScore.length == 1) {
            yx = arScore[0];
          }
        } else {
          yx = obj.ascore;
        }
      } else {
        yx = -100
      }
      arScore = [];
      if (obj.bscore != "" && obj.bscore != null && obj.bscore != undefined) {
        if (isNaN(obj.bscore)) {
          arScore = obj.bscore.split('-');
          if (arScore.length >= 2) {
            lh = arScore[0];
            lh1 = arScore[1];
          } 
          if (arScore.length == 1) {
            lh = arScore[0];
          }
        } else {
          lh = obj.bscore;
        }
      } else {
        lh = -100
      }
      arScore = [];
      if (obj.cscore != "" && obj.cscore != null && obj.cscore != undefined) {
        if (isNaN(obj.cscore)) {
          arScore = obj.cscore.split('-');
          if (arScore.length >= 2) {
            yb = arScore[0];
            yb1 = arScore[1];
          } 
          if (arScore.length == 1) {
            yb = arScore[0];
          }
        } else {
          yb = obj.cscore;
        }
      } else {
        yb = -100
      }
      arScore = [];
      if (obj.dscore != "" && obj.dscore != null && obj.dscore != undefined) {
        if (isNaN(obj.dscore)) {
          arScore = obj.dscore.split('-');
          if (arScore.length >= 2) {
            jc = arScore[0];
            jc1 = arScore[1];
          } 
          if (arScore.length == 1) {
            jc = arScore[0];
          }
        } else {
          jc = obj.dscore;
        }
      } else {
        jc = -100
      }
      arScore = []
      if (yx >= 0) {
        arScore.push(yx);
      }
      if (yx1 >= 0) {
        arScore.push(yx1);
      }
      if (lh >= 0) {
        arScore.push(lh);
      }
      if (lh1 >= 0) {
        arScore.push(lh1);
      }
      if (yb >= 0) {
        arScore.push(yb);
      }
      if (yb1 >= 0) {
        arScore.push(yb1);
      }
      if (jc >= 0) {
        arScore.push(jc);
      }
      if (jc1 >= 0) {
        arScore.push(jc1);
      }
      if (arScore.length > 1) {
        let maxN = Math.max.apply(null,arScore);
        let minN = Math.min.apply(null,arScore);
        arScore = [];
        arScore.push(minN);
        arScore.push(maxN);
      } else {
        arScore = [];
      }
      return arScore;
    },
    //计算总分
    compute() {
      let totalScore = 0;
      let scoreArr = [];
      for (let i = 0; i < this.dutyJichu.length; i++) {
        let val = this.dutyJichu[i];
          if (!isNaN(val.score)) {
            let arScore = this.getMaxMin(val)
            if (arScore.length == 2) {
              let min = arScore[0];
              let max = arScore[1];
              if (val.score > max || val.score < min) {
                this.$message.warning("政治建设请填写"+ min +"-"+max+"之间数字");
                return false;
              }
              totalScore += parseFloat(val.score);
              scoreArr.push(val.score);
            } else {
              this.$message.warning("政治建设在创建指标时,设置有误.");
              return false;
            }
          } else {
            this.$message.warning("政治建设请填写正确数字");
          }
        
      }
      for (let i = 0; i < this.dutyYiban.length; i++) {
        let val = this.dutyYiban[i];
          if (!isNaN(val.score)) {
            let arScore = this.getMaxMin(val)
            if (arScore.length == 2) {
              let min = arScore[0];
              let max = arScore[1];
              if (val.score > max || val.score < min) {
                this.$message.warning("思想建设请填写"+ min +"-"+max+"之间数字");
                return false;
              }
              totalScore += parseFloat(val.score);
              scoreArr.push(val.score);
            } else {
              this.$message.warning("思想建设在创建指标时,设置有误.");
              return false;
            }
          } else {
            this.$message.warning("思想建设请填写正确数字");
          }
      }
      for (let i = 0; i < this.dutyZhongdian.length; i++) {
        let val = this.dutyZhongdian[i];
          if (!isNaN(val.score)) {
            let arScore = this.getMaxMin(val)
            if (arScore.length == 2) {
              let min = arScore[0];
              let max = arScore[1];
              if (val.score > max || val.score < min) {
                this.$message.warning("组织建设请填写"+ min +"-"+max+"之间数字");
                return false;
              }
              totalScore += parseFloat(val.score);
              scoreArr.push(val.score);
            } else {
              this.$message.warning("组织建设在创建指标时,设置有误.");
              return false;
            }
          } else {
            this.$message.warning("组织建设请填写正确数字");
          }
      }
      for (let i = 0; i < this.dutyMubiao.length; i++) {
        let val = this.dutyMubiao[i];
          if (!isNaN(val.score)) {
            let arScore = this.getMaxMin(val)
            if (arScore.length == 2) {
              let min = arScore[0];
              let max = arScore[1];
              if (val.score > max || val.score < min) {
                this.$message.warning("党建创新请填写"+ min +"-"+max+"之间数字");
                return false;
              }
              totalScore += parseFloat(val.score);
              scoreArr.push(val.score);
            } else {
              this.$message.warning("党建创新在创建指标时,设置有误.");
              return false;
            }
          } else {
            this.$message.warning("党建创新请填写正确数字");
          }
        
      }
       for (let i = 0; i < this.dutyZuofeng.length; i++) {
        let val = this.dutyZuofeng[i];
          if (!isNaN(val.score)) {
            let arScore = this.getMaxMin(val)
            if (arScore.length == 2) {
              let min = arScore[0];
              let max = arScore[1];
              if (val.score > max || val.score < min) {
                this.$message.warning("作风建设请填写"+ min +"-"+max+"之间数字");
                return false;
              }
              totalScore += parseFloat(val.score);
              scoreArr.push(val.score);
            } else {
              this.$message.warning("作风建设在创建指标时,设置有误.");
              return false;
            }
          } else {
            this.$message.warning("作风建设请填写正确数字");
          }
        
      }
      this.scoreShow = true;
      this.totalScore = totalScore;
      return true;
    },
    //提交考核
    submitAssess() {
      if (this.compute()) {
        let data = {
          serialno: this.detailData.serialno,
          employeecode: this.detailData.employeecode,
          scorringcode: this.$route.query.scorringCode,
          total: this.totalScore,
          year:this.year,
          month:this.month
        };
        //遍历基础和关键打分数据
        data.dutyJiChu = [];
        data.dutyYiban = [];
        data.dutyZhongdian = [];
        data.dutyMubiao = [];
        data.dutyZuofeng = [];
        this.laberror = ''
        this.dutyJichu.forEach(row => {
          data.dutyJiChu.push({ topicId: row.dutycode, score: row.score, cpsm: row.score != row.dscore?'':row.cpsm, zpsm: row.zpsm });
          if (row.score == row.dscore && (row.cpsm == null || row.cpsm == '' || row.cpsm == undefined)) {
            this.laberror = '政治建设，差评原因 不能为空.'
          }
        });
        if(this.laberror == ''){
          this.dutyYiban.forEach(row => {
            data.dutyYiban.push({ topicId: row.dutycode, score: row.score, cpsm: row.score != row.dscore?'':row.cpsm, zpsm: row.zpsm });
            if (row.score == row.dscore && (row.cpsm == null || row.cpsm == '' || row.cpsm == undefined)) {
              this.laberror = '思想建设，差评原因 不能为空.'
            }
          });
        }
        if(this.laberror == ''){
          this.dutyZhongdian.forEach(row => {
            data.dutyZhongdian.push({ topicId: row.dutycode, score: row.score, cpsm: row.score != row.dscore?'':row.cpsm, zpsm: row.zpsm });
            if (row.score == row.dscore && (row.cpsm == null || row.cpsm == '' || row.cpsm == undefined)) {
              this.laberror = '组织建设，差评原因 不能为空.'
            }
          });
        }
        if(this.laberror == ''){
          this.dutyMubiao.forEach(row => {
            data.dutyMubiao.push({ topicId: row.dutycode, score: row.score, cpsm: row.score != row.dscore?'':row.cpsm, zpsm: row.zpsm });
            if (row.score == row.dscore && (row.cpsm == null || row.cpsm == '' || row.cpsm == undefined)) {
              this.laberror = '党建创新，差评原因 不能为空.'
            }
          });
        }
        if(this.laberror == ''){
          this.dutyZuofeng.forEach(row => {
            data.dutyZuofeng.push({ topicId: row.dutycode, score: row.score, cpsm: row.score != row.dscore?'':row.cpsm, zpsm: row.zpsm });
            if (row.score == row.dscore && (row.cpsm == null || row.cpsm == '' || row.cpsm == undefined)) {
              this.laberror = '作风建设，差评原因 不能为空.'
            }
          });
        }
        if (this.laberror != ''){
          this.$message.warning(this.laberror);
          this.tableLoading = false;
          this.submitLoading = false;
          return
        }
        data.dutyJiChu = JSON.stringify(data.dutyJiChu);
        data.dutyYiban = JSON.stringify(data.dutyYiban);
        data.dutyZhongdian = JSON.stringify(data.dutyZhongdian);
        data.dutyMubiao = JSON.stringify(data.dutyMubiao);
        data.dutyZuofeng = JSON.stringify(data.dutyZuofeng);
        data.dbtype= this.$store.state.user.user.dbtype;
        this.submitLoading = true;
        new Promise((response, reject) => {
          scoring(qs.stringify(data))
            .then(response => {
              if (response.data.code == 0) {
                this.$message({
                  message: response.data.msg,
                  type: "success"
                });
                this.$router.push({
                  path: "/web",
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
      }
    }
  }
};
</script>


<style lang="scss" scoped>
.form-msg{
  margin: .5rem 0;
}
.question {
  .mask-show {
    position: fixed;
    width: 100%;
    height: 100%;
    left: 0px;
    top: 0px;
    background: rgba(0, 0, 0, 0.3);
  }
  .title {
    background: #fff;
    text-align: center;
    font-weight: bold;
    font-size: 1rem;
    padding: 0.5rem 0px;
    color: #409EFF;
  }
  .content {
    // padding: 0 0 52px 0;
    .list {
      background: #fff;
      margin-top: 0.5rem;
      font-size: 0.8rem;
      .question-title {
        text-align: left;
        font-weight: bold;
        padding: 0.5rem 0px;
        .index {
          text-align: center;
        }
        .name {
          .el-col {
            text-align: center;
            font-weight: normal;
            color: #a8a8a8;
          }
        }
      }
      .on-line-top::after {
        content: " ";
        position: absolute;
        pointer-events: none;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        left: 0;
        right: 0;
        top: 0;
        width: 100%;
        -webkit-transform: scaleY(0.5);
        transform: scaleY(0.5);
        border-bottom: 1px solid #ebedf0;
      }
      .answer {
        background: #fafafa;
        position: relative;
        padding: 1rem 0 1rem 0;
        .radio {
          text-align: center;
        }
      }
    }
    .message {
      margin: 1rem 0;
      color: #ccc;
      text-align: center;
    }
  }
  .edit-btn {
    width: 100%;
    position: fixed;
    bottom: 0px;
    left: 0px;
    text-align: center;
    background: #fff;
    padding: 10px 0px;
    .el-button {
      width: 50%;
    }
  }
}
.toptic-title {
  background: #409EFF;
  color: #fff;
  padding: 0.1rem 0.6rem;
  border-top: 1px solid #f1f1f1;
  font-weight: 600;
  font-size: 0.9rem;
}
.toptic {
  padding: 0.1rem 0.6rem;
}
.season-text {
  padding: 0.1rem 0.6rem;
  background: #fff;
}
.season-content {
  word-wrap: break-word;
  text-align: justify;
}
</style>
<style>
.question .el-radio-button__orig-radio:checked + .el-radio-button__inner {
  background-color: #409EFF;
  border-color: #409EFF;
  -webkit-box-shadow: -1px 0 0 0 #409EFF;
  box-shadow: -1px 0 0 0 #409EFF;
}
.season-text .el-collapse-item__wrap {
  border: 0;
}
.question .el-select .el-input.is-focus .el-input__inner{
  border-color:#409EFF;
}
.question .el-select .el-input__inner:focus{
  border-color:#409EFF;
}
</style>