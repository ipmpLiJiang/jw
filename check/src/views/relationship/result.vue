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
        <el-row class="edit">
            <el-col class="title">评价关系确认</el-col>
            <el-table
                    :data="tableData"
                    stripe
                    size="small"
                    style="width: 100%">
                <el-table-column
                        prop="name"
                        label="被评人">
                </el-table-column>
                <el-table-column
                        prop="a"
                        label="A类评分人">
                </el-table-column>
                <el-table-column
                        prop="b"
                        label="B类评分人">
                </el-table-column>
                <el-table-column
                        prop="c"
                        label="C类评分人">
                </el-table-column>
                <el-table-column
                        prop="d"
                        label="D类评分人">
                </el-table-column>
                <el-table-column
                        label="操作">
                    <template slot-scope="scope">
                        <el-button type="text" size="small"><i class="icon iconfont icon-weibiaoti2010104"></i>修改</el-button>
                        <el-button type="text" size="small"><i class="icon iconfont icon-shanchu"></i>删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-row>
        <el-row class="myself">
            <p>评分人权重占比<span>A类:25%</span><span>B类:25%</span><span>C类:25%</span><span>D类:25%</span></p>
            <el-button type="primary" plain @click="editQuan">修改权重</el-button>
        </el-row>
        <el-row class="button">
            <el-button type="warning">上一步</el-button>
            <el-button type="primary" @click="question">编辑考核</el-button>
        </el-row>
        <el-dialog
                title="滑动设置评分人权重占比"
                :visible.sync="centerDialogVisible"
                width="700px"
                center>
            <el-col class="slider">
                <el-col :span="5" class="title">A类评分人权重:</el-col>
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
                <el-col :span="5" class="title">B类评分人权重:</el-col>
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
                <el-col :span="5" class="title">C类评分人权重:</el-col>
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
                <el-col :span="5" class="title">D类评分人权重:</el-col>
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
                value8: 25,
                value9: 25,
                value10: 25,
                value11: 25,
                centerDialogVisible: false,
                active: 0,
                tableData: [{
                    name: '袁莉',
                    a: '杨光辉',
                    b: '刘亚玲',
                    c: '胡豫 张明',
                    d: '彭义香',
                },{
                    name: '汪宏波',
                    a: '邹俊宁',
                    b: '张明',
                    c: '樊玮 刘亚玲',
                    d: '刘亚玲',
                }]
            };
        },
        components: {

        },
        mounted() {},
        created() {},

        methods: {
            formatTooltip(val) {
                return val + '%';
            },
            question(){
                this.$router.push({
                    path: '/detail/questionnaire',
                    query: {
                    }
                })
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
    .edit {
        background: #fff;
        padding: 20px;
        margin: 10px 0px;
        border-radius: 5px;
        .title {
            text-align: center;
            font-size: 16px;
            font-weight: bold;
        }
        .message {
            margin: 20px 0px 10px 0px;
            color: #cf9236;
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
.button {
    background: #fff;
    padding: 20px;
    border-radius: 5px;
    text-align: center;
}
.slider{
    margin-bottom: 10px;
    .title{
        line-height: 38px;
        height: 38px;
        font-weight: bold;
    }
}
</style>
