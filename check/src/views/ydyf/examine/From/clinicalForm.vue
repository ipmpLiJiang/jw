<template>
  <div>
    <h4 class="title">临床人员考核</h4>
    <el-card class="form-container" shadow="never">
      <el-col
        :span="24"
        class="title"
        style="text-align: center; font-size: 20px"
        >临床人员考核信息</el-col
      >
      <el-col :span="24" class="title">个人信息</el-col>
      <el-form ref="form" :model="form">
        <el-col :span="6">
          <el-form-item label="姓名">
            <el-input
              v-model="form.username"
              style="width: 150px; margin-left: 28px"
              :disabled="Show"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="选择性别">
            <!-- <el-input
              v-model="form.sex"
              style="width: 350px; margin-left: 28px"
              :disabled="Show"
            ></el-input> -->
            <el-select
              v-model="form.sex"
              placeholder="请选择性别"
              style="width: 150px"
              :disabled="Show"
            >
              <el-option
                v-for="item in sex"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="出生年月">
            <el-input
              v-model="form.birth"
              style="width: 150px"
              :disabled="Show"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="政治面貌">
            <!-- <el-input
              v-model="form.politicsStatus"
              style="width: 150px"
              :disabled="Show"
            ></el-input> -->
            <el-select
              v-model="form.politicalStatus"
              placeholder="请选择政治面貌"
              style="width: 150px"
              :disabled="Show"
            >
              <el-option
                v-for="item in appellation"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="文化程度">
            <!-- <el-input
              v-model="form.station"
              style="width: 150px"
              :disabled="Show"
            ></el-input> -->
            <el-select
              v-model="form.educationLevel"
              placeholder="请选择文化程度"
              style="width: 150px"
              :disabled="Show"
            >
              <el-option
                v-for="item in culture"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="现聘岗位">
            <el-input
              v-model="form.currentPosition"
              style="width: 150px"
              :disabled="Show"
               clearable
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="聘用时间">
            <!-- <el-input
              v-model="form.hireDate"
              style="width: 350px"
              :disabled="Show"
            ></el-input> -->

            <el-date-picker
              v-model="form.hireDate"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd"
              format="yyyy-MM-dd"
              style="width: 150px"
              :disabled="Show"
               clearable
            >
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="从事工作">
            <el-input
              v-model="form.jobContent"
              style="width: 150px"
              :disabled="Show"
               clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-form>

      <el-col :span="24" class="title">本年度医德医风自评及工作总结</el-col>
      <el-col>
        <p
          style="border: 1px solid #dcdfe6; min-height: 300px"
          v-html="form.selfSummary"
          :disabled="Show"
        ></p>
      </el-col>

      <el-col :span="24" class="title">考评项目评分</el-col>
      <el-col :span="24">
        <ul class="indicator">
          <li
            class="li-title"
            v-for="(item, index) in questionData"
            :key="index"
          >
            <div class="title">{{ item.title }}</div>
            <div
              class="label"
              v-for="(k, kIndex) in item.content"
              :key="kIndex"
            >
              {{ k.question }}
              <el-form ref="forms" :model="k" :rules="rules">
                <div class="value">
                  <div class="core">
                    <el-form-item label="自我评分" prop="selfScore">
                      <el-input
                        placeholder="请输入分数"
                        clearable
                        class="core-input"
                        v-model="k.selfScore"
                        :disabled="mydirectorShow"
                      />
                    </el-form-item>
                  </div>

                  <div class="core" v-if="directorShow">
                    <el-form-item label="科室主任评分" prop="headScore">
                      <el-input
                        placeholder="请输入分数"
                        clearable
                        class="core-input"
                        v-model="k.headScore"
                        :disabled="directorFrom"
                        @change="handlerPageNo"
                        oninput="value=value.replace(/[^0-9]/g,'')"
                      >
                      </el-input>
                    </el-form-item>
                  </div>

                  <div class="core" v-if="secretaryShow">
                    <el-form-item label="党支部书记评分" prop="branchScore">
                      <el-input
                        placeholder="请输入分数"
                        clearable
                        class="core-input"
                        v-model="k.branchScore"
                        :disabled="secretaryFrom"
                        @change="handlerSecretary"
                        oninput="value=value.replace(/[^0-9]/g,'')"
                      >
                      </el-input>
                    </el-form-item>
                  </div>
                </div>
              </el-form>
            </div>
          </li>
        </ul>

        <el-row :gutter="24">
          <el-col :span="8"
            ><div class="grid-content bg-purple">
              自我评分
              <el-progress
                type="circle"
                :percentage="total.mytotal"
                :format="format"
              ></el-progress>
            </div>

            <!-- <span class="self-evaluation" > <span style="">自评时间:</span> <span>{{this.questionData[0].content[0].selfSubmitTime}}</span></span> -->

            <el-card v-if="mydirectorDate" style="margin-top: 20px">
              <h4>自我评分</h4>
              <p>
                {{ this.questionData[0].content[0].selfSubmitName }} 提交于
                <span style="color: #fe9a2e; font-size: 16px">{{
                  this.questionData[0].content[0].selfSubmitTime
                }}</span>
              </p>
            </el-card>
          </el-col>

          <el-col :span="8"
            ><div class="grid-content bg-purple" v-if="directorShow">
              科室主任评分
              <el-progress
                type="circle"
                :percentage="total.directortotal"
                :format="format"
              ></el-progress>
            </div>
            <el-card v-if="directorDate" style="margin-top: 20px">
              <h4>科室主任评分</h4>
              <p>
                {{ this.questionData[0].content[0].headSubmitName }} 提交于
                <span style="color: #fe9a2e; font-size: 16px">{{
                  this.questionData[0].content[0].headSubmitTime
                }}</span>
              </p>
            </el-card>
          </el-col>

          <!-- <el-col :span="8" v-if="secretaryDate" -->
          <el-col :span="8"
            ><div class="grid-content bg-purple" v-if="secretaryGrade">
              书记及合计评分
              <el-progress
                type="circle"
                :percentage="total.secretarytotal"
                :format="secretaryformat"
              ></el-progress>
            </div>

            <el-card v-if="secretaryDate" style="margin-top: 20px">
              <h4>书记评分</h4>
              <p>
                {{ this.questionData[0].content[0].branchSubmitName }} 提交于
                <span style="color: #fe9a2e; font-size: 16px">{{
                  this.questionData[0].content[0].branchSubmitTime
                }}</span>
              </p>
            </el-card>
          </el-col>
        
          <el-col
            class="count"
            style="margin-top: 30px"
            v-if="form.step == 4 && this.PermissionList.indexOf('400') != -1"
          >
            <h3>
              实时评优百分比:<span style="color: red"
                >优秀比上限为{{ this.total.ratio }}%</span
              >
            </h3>
            <el-progress
              :text-inside="true"
              :stroke-width="26"
              :percentage="percent"
            ></el-progress>
          </el-col>
        </el-row>
      </el-col>

      <el-row class="step" v-if="Submit">
        <el-button
          type="primary"
          style="margin-top: 12px; width: 150px"
          @click="SubmitFrom"
          >提交</el-button
        >
      </el-row>
      <!-- <el-row class="count">
          <h3>实时评优百分比：</h3>
         
         <el-progress type="circle" :percentage="25" ></el-progress>
        </el-row> -->
    </el-card>
  </div>
</template>

<script>
import { updateUserPassword } from "@/api/user/user";
import AddQuarter from "../../user/addQuarter";
import infoFrom from "../../common/infoFrom";
import { MessageBox } from "element-ui";
import {
  initInfo,
  updateSelfSummary,
  updateBaseInfo,
  submitScore,
  baseInfo,
  getScore,
  initFormData,
  ProfiCiency,
} from "../../api/Form/index";
import work from "../../common/workSummary";
import score from "../../common/score";
import qs from "qs";
export default {
  data() {
    var checkAge = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("分数不能为空"));
      }
    };
    return {
      form: {
        username: "",
        sex: "1",
        birth: "",
        politicsStatus: "",
        station: "",
        status: "",
        workingTtime: "",
        jobContent: "",
        text: "",
        selfSummary: "",
        currentPosition: "",
        hireDate: "",
      },
      dialogVisible: true,
      forms: {
        selfScore: "",
        headScore: "",
        branchScore: "",
      },
      k: {
        selfScore: "",
        headScore: "",
        branchScore: "",
      },
      // getList: {},
      active: 0,
      Submit: true,
      Show: false, //普通用户输入框是否禁止输入
      mydirectorShow: false,
      directorShow: false, //科室主任打分表显示隐藏
      directorFrom: false, //科室主任打分输入框禁止输入
      secretaryShow: false, //党支部打分表显示隐藏
      secretaryFrom: false, //党支部打分输入框禁止输入
      mydirectorDate: false, //自我提交时间
      directorDate: false, //科室主任提交时间
      secretaryDate: false, //书记提交时间
      secretaryGrade: false,
      PermissionID: "",
      total: {
        mytotal: 28,
        directortotal: 86,
        secretarytotal: 50,
        ratio: 20,
      },
      excellentFrom: {},
      PermissionList: [],
      questionData: [
        {
          title: "救死扶伤，全新全意为人民服务(10分)",
          content: [
            {
              item: 1,
              question: "1、工作认真，负责，细致，责任心强（10分）",
              selfScore: "",
              headScore: "",
              branchScore: "",
            },
          ],
        },
        {
          title: "尊重病人的人格和权利，为病人保守医疗秘密(15分)",
          content: [
            {
              item: 2,
              question: "1、平等对待患者，做到一视同仁，不得歧视患者(5分)",
              selfScore: "",
              headScore: "",
              branchScore: "",
            },
            {
              item: 3,
              question:
                "2、尊重患者知情权，选择权和隐私权，为患者保守医疗秘密(10分)",
              selfScore: "",
              headScore: "",
              branchScore: "",
            },
          ],
        },
        {
          title: "遵纪守法，廉洁行医(20分)",
          content: [
            {
              item: 4,
              question:
                "1、坚持廉洁行医,拒收患者或家属的“红包”等财物,自觉抵制回扣等各种形式商业贿赂,严格执行“九不准”规定（10分)",
              selfScore: "",
              headScore: "",
              branchScore: "",
            },
            {
              item: 5,
              question:
                "2、不开具虚假医学证明,不参与虚假医疗广告宣传和药品医疗器械促销,不隐匿、伪造或违反规定涂改、销毁医学文书及有关资料（10分)",
              selfScore: "",
              headScore: "",
              branchScore: "",
            },
          ],
        },
        {
          title: "因病施治，规范医疗服务行为(20分)",
          content: [
            {
              item: 6,
              question: "1、减持合理检查，合理质量，合理治疗（5分）",
              selfScore: "",
              headScore: "",
              branchScore: "",
            },
            {
              item: 7,
              question: "2、认真落实有关控制医疗费用的制度措施（5分）",
              selfScore: "",
              headScore: "",
              branchScore: "",
            },
            {
              item: 8,
              question:
                "3、严格执行医疗服务和药品价格政策,不多收、乱收和私自收取费用（10分）",
              selfScore: "",
              headScore: "",
              branchScore: "",
            },
          ],
        },
        {
          title: "顾全大局，团结合作，和谐共事(10分)",
          content: [
            {
              item: 9,
              question: "1、顾全大局，团结合作，和谐共事(10分)",
              selfScore: "",
              headScore: "",
              branchScore: "",
            },
          ],
        },
        {
          title: "严禁求实，努力提高专业技术水平(10分)",
          content: [
            {
              item: 10,
              question: "1、严禁求实，努力提高专业技术水平(10分)",
              selfScore: "",
              headScore: "",
              branchScore: "",
            },
          ],
        },
      ],

      rules: {
        selfScore: [{ validator: checkAge, trigger: "blur" }],
        headScore: [{ validator: checkAge, trigger: "blur" }],
        branchScore: [{ validator: checkAge, trigger: "blur" }],
        // myScoreWork: [{ validator: checkAge, trigger: "blur" }],
      },
      sex: [
        {
          value: 0,
          label: "女",
        },
        {
          value: 1,
          label: "男",
        },
      ],
      appellation: [
        {
          value: "1",
          label: "中国共产党员",
        },
        {
          value: "2",
          label: "团员",
        },
        {
          value: "3",
          label: "民盟",
        },
        {
          value: "4",
          label: "民建",
        },
        {
          value: "5",
          label: "民进",
        },
        {
          value: "6",
          label: "农工党",
        },

    
        {
          value: "8",
          label: "致公党",
        },

        {
          value: "9",
          label: "九三学社",
        },
        {
          value: "10",
          label: "无党派人士",
        },
        {
          value: "11",
          label: "群众",
        },
        {
          value: "12",
          label: "其他",
        },
      ],
      culture: [
        {
          value: "1",
          label: "大专及以下",
        },
        {
          value: "2",
          label: "本科",
        },
        {
          value: "3",
          label: "硕士研究生",
        },
        {
          value: "4",
          label: "博士研究生",
        },
      ],
      technical: [
        {
          value: "1",
          label: "教授",
        },
        {
          value: "2",
          label: "副教授",
        },
        {
          value: "3",
          label: "讲师",
        },
        {
          value: "4",
          label: "助教",
        },
        {
          value: "5",
          label: "正高级工程师",
        },
        {
          value: "6",
          label: "高级工程师",
        },
        {
          value: "7",
          label: "工程师",
        },
        {
          value: "8",
          label: "助理工程师",
        },
        {
          value: "9",
          label: "工人",
        },
        {
          value: "10",
          label: "研究员",
        },
        {
          value: "11",
          label: "副研究员",
        },
        {
          value: "12",
          label: "助理研究员",
        },
        {
          value: "13",
          label: "主任医师",
        },
        {
          value: "14",
          label: "副主任医师",
        },
        {
          value: "15",
          label: "主治医师",
        },
        {
          value: "16",
          label: "医师",
        },
        {
          value: "17",
          label: "主任药师",
        },
        {
          value: "18",
          label: "副主任药师",
        },
        {
          value: "19",
          label: "主管药师",
        },
        {
          value: "20",
          label: "药师",
        },
        {
          value: "21",
          label: "药士",
        },
        {
          value: "22",
          label: "主任护师",
        },

        {
          value: "23",
          label: "副主任护师",
        },
        {
          value: "24",
          label: "主管护师",
        },
        {
          value: "25",
          label: "护师",
        },
        {
          value: "26",
          label: "护士",
        },
        {
          value: "27",
          label: "主任技师",
        },

        {
          value: "28",
          label: "副主任技师",
        },
        {
          value: "29",
          label: "主管技师",
        },
        {
          value: "30",
          label: "技师",
        },

        {
          value: "31",
          label: "技士",
        },
        {
          value: "32",
          label: "三级职员",
        },
        {
          value: "33",
          label: "四级职员",
        },
        {
          value: "34",
          label: "五级职员",
        },
        {
          value: "35",
          label: "六级职员",
        },
        {
          value: "36",
          label: "七级职员",
        },
        {
          value: "37",
          label: "八级职员",
        },
        {
          value: "38",
          label: "九级职员",
        },
        {
          value: "39",
          label: "十级职员",
        },
        {
          value: "40",
          label: "其他",
        },
      ],
      percent: 0,
      maxExcellentPercent: 1,
      branchId: 0,
    };
  },
  components: {
    AddQuarter,
    work,
    infoFrom,
    score,
  },
  mounted() {},
  created() {
    this.PermissionList = this.$store.state.user.user.medicalEthicsRoleList;
    this.getScore();
    // this.getinfo();
    this.getList();
    // this.proficiency();
  },
  methods: {
    stepChange() {
      if (this.active-- < 0) this.active = 0;
    },

    checkScore(dataList) {
      let res = false;
      for (let i = 0; i < dataList.length; i++) {
        if (
          dataList[i].selfScore > dataList[i].maxScore ||
          dataList[i].selfScore < 0
        ) {
          return res;
        }
        if (
          dataList[i].branchScore > dataList[i].maxScore ||
          dataList[i].branchScore < 0
        ) {
          return res;
        }
        if (
          dataList[i].headScore > dataList[i].maxScore ||
          dataList[i].headScore < 0
        ) {

          return res;
        }
      }
      return true;
    },

    //提交
    SubmitFrom() {
      // if(this.total.secretarytotal>89){

      //       if(this.percent>this.maxExcellentPercent){

      //      this.$message.warning("优秀率已满,无法再次评优哦~");
      //         return false;
      //    }
      // }
      if (this.form.step == 3) {
        let qData = this.questionData;
      
        let arr = [];
        for (let i = 0; i < qData.length; i++) {
          for (let j = 0; j < qData[i]["content"].length; j++) {
            qData[i]["content"][j].userId = this.$route.query.userId;
       
            if (
              qData[i]["content"][j].headScore == "" ||
              qData[i]["content"][j].headScore == null
            ) {
              this.$message.warning("请将所有评分填写完~");
              return false;
            } else {
            }
            arr.push(qData[i]["content"][j]);
          }
        }
        let RESULT = this.checkScore(arr);
        if (!RESULT) {
          this.$confirm("所填分数不能大于要求最高值", "提交失败", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          })
            .then(() => {
              return false;
            })
            .catch(() => {});
        } else {
          this.$confirm(
            "您所填的评分总计为:" + this.total.directortotal + "分",
            "提交成绩",
            {
              type: "success",
            }
          ).then(() => {
            new Promise((response, reject) => {
              submitScore(arr).then((response) => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success",
                  });
                  this.$router.go(-1);
                } else {
                  this.$message({
                    message: response.data.msg,
                    type: "error",
                  });
                }
              });
            });
          });
        }
      } else {
        let qData = this.questionData;
        let arr = [];

        for (let i = 0; i < qData.length; i++) {
          for (let j = 0; j < qData[i]["content"].length; j++) {
            qData[i]["content"][j].userId = this.$route.query.userId;
            if (
              qData[i]["content"][j].branchScore == null ||
              qData[i]["content"][j].branchScore == ""
            ) {
              this.$message.warning("请将所有评分填写完~");
              return false;
            } else {
            }
            arr.push(qData[i]["content"][j]);
          }
        }

        if (this.total.secretarytotal > 89) {
          if (!this.GetPercent()) {
            this.$message.warning("优秀率已满,无法再次评优哦~");
            return false;
          }
        }
        let RESULT = this.checkScore(arr);

        if (!RESULT) {
          this.$confirm("所填分数不能大于要求最高值", "提交失败", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          })
            .then(() => {
              return false;
            })
            .catch(() => {});
          return false;
        } else {
          this.$confirm(
            "您所填的评分总计为:" +
              this.secretaryformat(this.total.secretarytotal),

            "提交成绩",
            {
              type: "success",
            }
          ).then(() => {
            new Promise((response, reject) => {
              submitScore(arr).then((response) => {
                if (response.data.code == 0) {
                  this.$message({
                    message: response.data.msg,
                    type: "success",
                  });
                  this.$router.go(-1);
                } else {
                  this.$message({
                    message: response.data.msg,
                    type: "error",
                  });
                }
              });
            });
          });
        }
      }
    },
    getForm() {
      let userId = this.$store.state.user.user.moneycard;
      new Promise((response, reject) => {
        initInfo(userId)
          .then((response) => {
            if (response.data.code == 0) {
              this.form = response.data.data;
              this.$message({
                message: response.data.msg,
                type: "success",
              });
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 判断用户权限方法
    getinfo() {
      // this.PermissionID = this.$store.state.YdyfRole;
      // this.PermissionList = localStorage.getItem("YdyfRoleList");
      // //如果用户id等于200 那么无法看到科室主任和党支部打分表
      // if (this.PermissionList.indexOf("200") != -1) {
      //   // if (this.PermissionID == 200) {
      //   (this.mydirectorShow = true),
      //     (this.directorShow = false),
      //     (this.secretaryShow = false);
      //   this.Show = true;
      // }
      // //如果用户id等于300 那么就是科室主任 无法看到党支部打分表,普通用户列表可显示,但禁止输入
      // if (this.PermissionList.indexOf("300") != -1) {
      //   // if (this.PermissionID == 300) {
      //   (this.Show = true),
      //     (this.mydirectorShow = true),
      //     (this.directorShow = true);
      //   this.directorFrom = false;
      //   this.secretaryShow = false;
      //   this.secretaryFrom = false;
      // }
      // //如果用户id等于101 那么就是党支部书记 看到全部打分表,但禁止输入
      // if (this.PermissionList.indexOf("101") != -1) {
      //   // if (this.PermissionID == 101) {
      //   this.Show = true;
      //   this.mydirectorShow = true;
      //   this.directorShow = true;
      //   this.directorFrom = true;
      //   this.secretaryShow = true;
      //   this.secretaryFrom = false;
      // } else if (this.PermissionList.indexOf("100") != -1) {
      //   (this.directorShow = false), (this.secretaryShow = false);
      // }
    },

    getList() {
      let data = this.$route.query;
      new Promise((response, reject) => {
        baseInfo(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.form = response.data.data;
              this.branchId = response.data.data.branchId;
              if (this.branchId !== 0) {
                this.proficiency();
              }
              if (response.data.data.step == 3) {
                this.Show = true;
                this.mydirectorDate = true;
                if (this.PermissionList.indexOf("200") != -1) {
              
                  this.Submit = false;
                  this.mydirectorShow = true;
                  (this.directorShow = false), (this.secretaryShow = false);
                  this.secretaryGrade = false;
                  if (this.PermissionList.indexOf("300") != -1) {
                    this.Submit = true;
                  }
                }
                if (this.PermissionList.indexOf("300") != -1) {
                  (this.Show = true),
                    (this.mydirectorShow = true),
                    (this.directorShow = true);
                }
                 if (this.PermissionList.indexOf("400") != -1){
                       
                       this.Show=false,
                     this.mydirectorShow=true,
                     this.directorShow=false;
                     this.Submit=false
                 }
                else {
                }
              }

              if (response.data.data.step == 4) {
                this.mydirectorDate = true;
                this.directorDate = true;
                if (
                  this.PermissionList.indexOf("300") != -1 ||
                  this.PermissionList.indexOf("200") != -1
                ) {
                  this.Show = true;
                  this.Submit = false;
                  (this.mydirectorShow = true),
                    (this.directorShow = true),
                    (this.directorFrom = true);
                }
                if (this.PermissionList.indexOf("400") != -1) {
                  this.secretaryGrade = true;
                  this.secretaryDate = false;
                  this.Submit = false;
                  (this.mydirectorShow = true),
                    (this.directorShow = true),
                    (this.directorFrom = true),
                    (this.secretaryShow = true),
                    (this.secretaryFrom = false),
                    (this.Submit = true);
                } else {
                }
              }
              if (response.data.data.step == 5) {
                //如果状态等于5，等于评分已完,全部按钮隐藏,只展示
                this.mydirectorDate = true;
                this.directorDate = true;
                this.secretaryDate = true;
                this.secretaryGrade = true;
                this.Show = true;
                (this.mydirectorShow = true),
                  (this.directorShow = true),
                  (this.directorFrom = true),
                  (this.secretaryShow = true),
                  (this.secretaryFrom = true),
                  (this.Submit = false);

                // if (
                //   this.PermissionList.indexOf("300") != -1 ||
                //   this.PermissionList.indexOf("200") != -1 ||
                //   this.PermissionList.indexOf("101") != -1
                // ) {
                //   this.Submit = false;
                // } else {
                // }
                // (this.Show = true),
                //   (this.directorShow = true),
                //   (this.directorFrom = true);
                // this.directorFrom=true,
                //  this.secretaryShow=false,
                //  this.directorFrom=true,
                //  this.secretaryShow=false
              }
            } else {
              this.$message({
                message: response.data.msg,
                type: "error",
              });
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    //获取用户评分列表
    getScore() {
      let data = this.$route.query;

      new Promise((response, reject) => {
        getScore(qs.stringify(data))
          .then((response) => {
            if (response.data.code == 0) {
              this.questionData = response.data.data;
            

              for (let i = 0; i < this.questionData.length; i++) {
                for (
                  let j = 0;
                  j < this.questionData[i]["content"].length;
                  j++
                ) {
                  if (this.questionData[i]["content"][j].step == 1) {
                   
                    this.questionData[i]["content"][
                      j
                    ].branchScore = this.questionData[i]["content"][j].maxScore;
                  }
                  if (this.questionData[i]["content"][j].step == 2) {
                    this.questionData[i]["content"][
                      j
                    ].headScore = this.questionData[i]["content"][j].headScore;
                  } else {
                    this.questionData[i]["content"][
                      j
                    ].headScore = this.questionData[i]["content"][j].maxScore;
                  }
                }
              }
              this.integral();
            } else {
              // this.$message({
              //   message: response.data.msg,
              //   type: "error",
              // });
            }
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    //获取实时评优比
    proficiency() {
      let branchId = this.$route.query.branchId ? this.$route.query.branchId : this.branchId;
      new Promise((response, reject) => {
        ProfiCiency(branchId).then((response) => {
          if (response.data.code == 0) {
            this.excellentFrom = response.data.data;
            this.percent = parseInt(this.excellentFrom.currentExcellentCount / this.excellentFrom.totalCount * 100);
            this.total.ratio = Number(response.data.data.maxExcellentPercent);
            this.$message({
              message: response.data.msg,
              type: "success",
            });
          } else {
            this.$message({
              message: response.data.msg,
              type: "error",
            });
          }
        });
      });
    },

    format(percentage) {
      return percentage > 100 ? "100分" : `${percentage}分`;
    },
    secretaryformat(percentage) {
      return percentage >= 90
        ? "优秀"
        : percentage >= 80
        ? "良好"
        : percentage >= 60
        ? "一般"
        : percentage <= 59
        ? "较差"
        : `${percentage}分`;
    },

    //计算自我评分
    integral() {
      let qData = this.questionData;
      let arr = [];
      for (let i = 0; i < qData.length; i++) {
        for (let j = 0; j < qData[i]["content"].length; j++) {
          qData[i]["content"][j].userId = this.$route.query.userId;
          arr.push(qData[i]["content"][j]);
        }
      }
      let Score = 0;
      let directorScore = 0;
      let secretary = 0;
      for (let k = 0; k < arr.length; k++) {
        Score += Number(arr[k].selfScore);
        directorScore += Number(arr[k].headScore);
        secretary += Number(arr[k].branchScore);
      }

      this.total.mytotal = Score;
      this.total.directortotal = directorScore;
      this.total.secretarytotal =
        Score * 0.3 + directorScore * 0.35 + secretary * 0.35;
    },

    //
    handlerPageNo() {
      this.integral();
    },
    //书记评分
    handlerSecretary() {
      this.integral();
    },

    //判断优秀是否大于上限
    GetPercent() {
      let dCount = (this.excellentFrom.currentExcellentCount + 1) / this.excellentFrom.totalCount * 100;//当前优秀人数
      if(dCount <= this.excellentFrom.maxExcellentPercent){
        return true;
      }else{
        return false;
      }
    },
  },
};
</script>


<style lang="scss" scoped>
.form-container {
  width: 1000px;
  margin: 0 auto;
  .step {
    float: right;
    // margin-top: 20px;
    // margin-bottom: 50px;
    margin: 20px 30px 50px 0;
  }
}
.title {
  height: 50px;
  line-height: 50px;
  border-bottom: 1px solid #eaeaea;
  padding: 0px 10px;
  font-weight: 600;
  color: #424242;
  background: #fff;
  margin-bottom: 20px;
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
.active0 {
  width: 100%;
  height: 100%;
  margin-top: 50px;

  .title {
    text-align: center;
    font-size: 20px;
  }
}
.active1 {
  width: 100%;
  height: 100%;
  margin-top: 50px;
  .title {
    text-align: center;
    font-size: 20px;
  }
}
.active2 {
  width: 100%;
  height: 100%;
  margin-top: 50px;
  .title {
    text-align: center;
    font-size: 20px;
  }
  .count {
    text-align: center;
    .el-progress-circle__path {
      fill: red;
    }
  }
}

.indicator {
  display: flex;
  flex-wrap: wrap;
  background: #fff;
  margin: 10px;
  box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
  .li-title {
    width: 100%;
    margin-bottom: 20px;
    padding: 0 20px;
    .label {
      font-weight: 500;
      margin-bottom: 10px;
    }
    .core {
      width: 33%;
      display: inline-block;
    }
    .core-input {
      width: 180px;
      margin-top: 10px;
    }
    .title {
      line-height: 10px;
      background-color: #409eff;
      color: #fff;
      font-size: 16px;
      font-weight: 500;
      width: 100%;
    }
  }
  .title {
    font-size: 16px;
    font-weight: 500;
    padding-top: 20px;
  }
  li {
    width: 100%;
    margin-bottom: 20px;
    padding: 0 20px;
    .label {
      font-weight: 500;
      margin-bottom: 10px;
      margin-top: 20px;
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
</style>
