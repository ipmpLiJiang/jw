<template>
  <div>
    <el-row class="step">
      <el-steps
              :active="active"
              finish-status="success"
              align-center
      >
        <el-step title="创建评价关系"></el-step>
        <el-step title="编辑考核"></el-step>
        <el-step title="发布考核"></el-step>
        <!-- <el-step title="评估报告"></el-step> -->
      </el-steps>
    </el-row>

    <el-row class="apply">
      <p>此考核现在处于<span>草稿状态</span>，如果您的考核<strong>准备就绪</strong>，请点击</p>
      <el-button type="primary" icon="el-icon-success" @click="result">发布考核</el-button>
    </el-row>

    <el-row class="myself">
      <p>题目维度权重占比<span>责任意识:25%</span><span>高效执行:25%</span><span>团结协作:25%</span><span>创新能力:25%</span></p>
      <el-button type="primary" plain @click="editQuan">修改权重</el-button>
    </el-row>
    <el-dialog
            title="滑动设置题目维度权重占比"
            :visible.sync="centerDialogVisible"
            width="700px"
            center>
      <el-col class="slider">
        <el-col :span="5" class="title">责任意识权重:</el-col>
        <el-col :span="19">
          <el-slider
                  v-model="value8"
                  :format-tooltip="formatTooltip"
                  :step="5"
                  show-stops
                  show-input>
          </el-slider>
        </el-col>
      </el-col>
      <el-col class="slider">
        <el-col :span="5" class="title">高效执行权重:</el-col>
        <el-col :span="19">
          <el-slider
                  v-model="value9"
                  :format-tooltip="formatTooltip"
                  :step="5"
                  show-stops
                  show-input>
          </el-slider>
        </el-col>
      </el-col>
      <el-col class="slider">
        <el-col :span="5" class="title">团结协作权重:</el-col>
        <el-col :span="19">
          <el-slider
                  v-model="value10"
                  :format-tooltip="formatTooltip"
                  :step="5"
                  show-stops
                  show-input>
          </el-slider>
        </el-col>
      </el-col>
      <el-col class="slider">
        <el-col :span="5" class="title">创新能力权重:</el-col>
        <el-col :span="19">
          <el-slider
                  v-model="value11"
                  :format-tooltip="formatTooltip"
                  :step="5"
                  show-stops
                  show-input>
          </el-slider>
        </el-col>
      </el-col>
      <span slot="footer" class="dialog-footer">
                  <el-button @click="centerDialogVisible = false">取 消</el-button>
                  <el-button type="primary"  @click="apply">确 定</el-button>
                </span>
    </el-dialog>
  </div>
</template>

<script>
    export default {
        data() {
            return {
                active: 2,
                value8: 25,
                value9: 25,
                value10: 25,
                value11: 25,
                centerDialogVisible: false,
            };
        },
        components: {
        },
        mounted() {
        },
        created() {
        },

        methods: {
            result(){
                this.$router.push({
                    path: '/detail/questionnaire/send',
                    query: {
                    }
                })
            },
            formatTooltip(val) {
                return val + '%';
            },
            editQuan(){
                this.centerDialogVisible = true
            },
            apply(){
                if(this.value8 + this.value9 + this.value10 + this.value11 == '100'){
                    this.centerDialogVisible = false
                }else{
                    this.$message({
                        message: '权重分配总分为100%，请检查您的分配比例',
                        type: 'error'
                    });
                }
            }
        }
    };
</script>


<style lang="scss" scoped>
  .step {
    background: #fff;
    padding: 20px;
    border-radius: 5px;
  }
  .apply{
    background: #fff;
    padding: 20px;
    border-radius: 5px;
    margin:10px 0px;
    display: flex;
    flex-direction: row;
    align-items: center;
    p{
      flex:1;
      span{
        font-weight: 600;
        color:red;
        margin:0px 4px;
      }
      strong{
        color:#409EFF;
        margin:0px 4px;
      }
    }
  }
  .myself {
    background: #fff;
    padding: 20px;
    border-radius: 5px;
    margin-bottom: 10px;
    display: flex;
    align-items: center;
    p{
      flex:1;
      span{
        margin:0px 8px;
        color:#409EFF;
      }
    }
    .el-button{
      float:right;
    }
  }
</style>
