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
            <el-col :span="24" class="right">
                <el-col class="title">编辑考核</el-col>
                <el-col class="list" @mouseenter.native="onMouseOver" @mouseleave.native="onMouseOut">
                    <el-col class="content">
                        <el-col :span="24">
                            <p><strong style="margin-right: 10px;">1</strong><strong>有责任感，有主人翁精神，以身作则，在工作中言行一致，能自觉完成任务，主动承担责任</strong></p>
                            <el-col class="text-message">
                                <el-col :span="4" class="name">分值</el-col>
                                <el-col :span="4">1分</el-col>
                                <el-col :span="4">2分</el-col>
                                <el-col :span="4">3分</el-col>
                                <el-col :span="4">4分</el-col>
                                <el-col :span="4">5分</el-col>
                            </el-col>
                            <el-col class="people">
                                <el-col :span="4" class="name">彭义香</el-col>
                                <el-col :span="4"><el-radio v-model="radio" label="1">&nbsp;</el-radio></el-col>
                                <el-col :span="4"><el-radio v-model="radio" label="2">&nbsp;</el-radio></el-col>
                                <el-col :span="4"><el-radio v-model="radio" label="3">&nbsp;</el-radio></el-col>
                                <el-col :span="4"><el-radio v-model="radio" label="4">&nbsp;</el-radio></el-col>
                                <el-col :span="4"><el-radio v-model="radio" label="5">&nbsp;</el-radio></el-col>
                            </el-col>
                            <el-col class="people">
                                <el-col :span="4" class="name">汪宏波</el-col>
                                <el-col :span="4"><el-radio v-model="radio2" label="1">&nbsp;</el-radio></el-col>
                                <el-col :span="4"><el-radio v-model="radio2" label="2">&nbsp;</el-radio></el-col>
                                <el-col :span="4"><el-radio v-model="radio2" label="3">&nbsp;</el-radio></el-col>
                                <el-col :span="4"><el-radio v-model="radio2" label="4">&nbsp;</el-radio></el-col>
                                <el-col :span="4"><el-radio v-model="radio2" label="5">&nbsp;</el-radio></el-col>
                            </el-col>
                        </el-col>
                    </el-col>
                    <el-col class="edit-content" v-if="editContent">
                        <quill-editor v-model="a_content"
                                      ref="myQuillEditor"
                                      class="editer"
                                      :options="editorOption" @ready="onEditorReady($event)"
                        >
                        </quill-editor>
                        <el-col class="must"><el-checkbox v-model="checked">此题是否必答</el-checkbox></el-col>
                        <el-col class="input-title">
                            <el-col :span="5">选项分值</el-col>
                            <el-col :span="7" :offset="1">操作</el-col>
                        </el-col>
                        <el-col class="input">
                            <el-col :span="5"><el-input v-model="input" placeholder="请输入内容" size="small"></el-input></el-col>
                            <el-col :span="7" :offset="1">
                                <el-button type="text"><i class="icon iconfont icon-upper"></i>上移</el-button>
                                <el-button type="text"><i class="icon iconfont icon-lower"></i>下移</el-button>
                                <el-button type="text"><i class="icon iconfont icon-shanchu"></i>删除</el-button>
                            </el-col>
                        </el-col>
                        <el-col class="input">
                            <el-col :span="5"><el-input v-model="input2" placeholder="请输入内容" size="small"></el-input></el-col>
                            <el-col :span="7" :offset="1">
                                <el-button type="text"><i class="icon iconfont icon-upper"></i>上移</el-button>
                                <el-button type="text"><i class="icon iconfont icon-lower"></i>下移</el-button>
                                <el-button type="text"><i class="icon iconfont icon-shanchu"></i>删除</el-button>
                            </el-col>
                        </el-col>
                        <el-col class="input">
                            <el-col :span="5"><el-input v-model="input3" placeholder="请输入内容" size="small"></el-input></el-col>
                            <el-col :span="7" :offset="1">
                                <el-button type="text"><i class="icon iconfont icon-upper"></i>上移</el-button>
                                <el-button type="text"><i class="icon iconfont icon-lower"></i>下移</el-button>
                                <el-button type="text"><i class="icon iconfont icon-shanchu"></i>删除</el-button>
                            </el-col>
                        </el-col>
                        <el-col class="input">
                            <el-col :span="5"><el-input v-model="input4" placeholder="请输入内容" size="small"></el-input></el-col>
                            <el-col :span="7" :offset="1">
                                <el-button type="text"><i class="icon iconfont icon-upper"></i>上移</el-button>
                                <el-button type="text"><i class="icon iconfont icon-lower"></i>下移</el-button>
                                <el-button type="text"><i class="icon iconfont icon-shanchu"></i>删除</el-button>
                            </el-col>
                        </el-col>
                        <el-col class="input">
                            <el-col :span="5"><el-input v-model="input5" placeholder="请输入内容" size="small"></el-input></el-col>
                            <el-col :span="7" :offset="1">
                                <el-button type="text"><i class="icon iconfont icon-upper"></i>上移</el-button>
                                <el-button type="text"><i class="icon iconfont icon-lower"></i>下移</el-button>
                                <el-button type="text"><i class="icon iconfont icon-shanchu"></i>删除</el-button>
                            </el-col>
                        </el-col>
                        <el-col style="text-align: center"> <el-button type="warning" @click="editContentBtn">完成编辑</el-button></el-col>
                    </el-col>
                    <el-col v-if="cs" class="hover">
                        <el-button type="warning" plain size="mini"><i class="icon iconfont icon-weibiaoti2010104"></i>编辑</el-button>
                        <el-button type="warning" plain size="mini"><i class="icon iconfont icon-copy"></i>复制</el-button>
                        <el-button type="warning" plain size="mini"><i class="icon iconfont icon-shanchu"></i>删除</el-button>
                        <el-button type="warning" plain size="mini"><i class="icon iconfont icon-upper"></i>上移</el-button>
                        <el-button type="warning" plain size="mini"><i class="icon iconfont icon-lower"></i>下移</el-button>
                        <el-button type="warning" plain size="mini"><i class="icon iconfont icon-most-top"></i>最上</el-button>
                        <el-button type="warning" plain size="mini"><i class="icon iconfont icon-most-bottom"></i>最下</el-button>
                    </el-col>
                </el-col>
                <el-col class="list" @mouseenter.native="onMouseOver2" @mouseleave.native="onMouseOut2">
                    <el-col class="content">
                        <el-col :span="24">
                            <p><strong  style="margin-right: 10px;">2</strong><strong>有责任感，有主人翁精神，以身作则，在工作中言行一致，能自觉完成任务，主动承担责任</strong></p>
                            <el-col class="text-message">
                                <el-col :span="4" class="name">文字</el-col>
                                <el-col :span="4">很不满意</el-col>
                                <el-col :span="4">不满意</el-col>
                                <el-col :span="4">一般</el-col>
                                <el-col :span="4">满意</el-col>
                                <el-col :span="4">很满意</el-col>
                            </el-col>
                            <el-col class="people">
                                <el-col :span="4" class="name">彭义香</el-col>
                                <el-col :span="4"><el-radio v-model="radio" label="1">&nbsp;</el-radio></el-col>
                                <el-col :span="4"><el-radio v-model="radio" label="2">&nbsp;</el-radio></el-col>
                                <el-col :span="4"><el-radio v-model="radio" label="3">&nbsp;</el-radio></el-col>
                                <el-col :span="4"><el-radio v-model="radio" label="4">&nbsp;</el-radio></el-col>
                                <el-col :span="4"><el-radio v-model="radio" label="5">&nbsp;</el-radio></el-col>
                            </el-col>
                            <el-col class="people">
                                <el-col :span="4" class="name">汪宏波</el-col>
                                <el-col :span="4"><el-radio v-model="radio2" label="1">&nbsp;</el-radio></el-col>
                                <el-col :span="4"><el-radio v-model="radio2" label="2">&nbsp;</el-radio></el-col>
                                <el-col :span="4"><el-radio v-model="radio2" label="3">&nbsp;</el-radio></el-col>
                                <el-col :span="4"><el-radio v-model="radio2" label="4">&nbsp;</el-radio></el-col>
                                <el-col :span="4"><el-radio v-model="radio2" label="5">&nbsp;</el-radio></el-col>
                            </el-col>
                        </el-col>
                    </el-col>
                    <el-col v-if="cs2" class="hover">
                        <el-button type="warning" plain size="mini"><i class="icon iconfont icon-weibiaoti2010104"></i>编辑</el-button>
                        <el-button type="warning" plain size="mini"><i class="icon iconfont icon-copy"></i>复制</el-button>
                        <el-button type="warning" plain size="mini"><i class="icon iconfont icon-shanchu"></i>删除</el-button>
                        <el-button type="warning" plain size="mini"><i class="icon iconfont icon-upper"></i>上移</el-button>
                        <el-button type="warning" plain size="mini"><i class="icon iconfont icon-lower"></i>下移</el-button>
                        <el-button type="warning" plain size="mini"><i class="icon iconfont icon-most-top"></i>最上</el-button>
                        <el-button type="warning" plain size="mini"><i class="icon iconfont icon-most-bottom"></i>最下</el-button>
                    </el-col>
                </el-col>
                <el-col class="submit">
                    <el-button type="primary" icon="el-icon-tickets" @click="apply">完成</el-button>
                </el-col>
            </el-col>
            <el-col class="left">
                <el-button type="primary" icon="el-icon-plus">分值单选</el-button>
                <el-button type="primary" icon="el-icon-plus">文字单选</el-button>
                <el-button type="primary" icon="el-icon-tickets">题库导入</el-button>
                <el-button type="warning" icon="el-icon-tickets">保存草稿</el-button>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import { quillEditor } from 'vue-quill-editor' //调用编辑器
    export default {
        data() {
            return {
                active: 1,
                radio: '',
                radio2: '',
                a_content:'请输入此题标题',
                editorOption:{
                    modules:{
                        toolbar:[
                            ['bold',  'underline', 'strike'],
                            [{ 'color': [] }, { 'background': [] }]
                        ]
                    }
                },
                checked: true,
                input:'1',
                input2:'2',
                input3:'3',
                input4:'4',
                input5:'5',
                cs: false,
                cs2: false,
                editContent:true,
            };
        },
        components: {
            quillEditor//使用编辑器
        },
        mounted() {},
        created() {},
        methods: {
            onEditorReady(editor) {
            },
            onMouseOver(){
                if(this.editContent == false){
                    this.cs = true;
                }
            },
            onMouseOut(){
                this.cs = false;
            },
            onMouseOver2(){
                this.cs2 = true;
            },
            onMouseOut2(){
                this.cs2 = false;
            },
            editContentBtn(){
                this.editContent = false;
            },
            apply(){
                this.$router.push({
                    path: '/detail/questionnaire/apply',
                    query: {
                    }
                })
            }
            /*onSubmit() {
            }*/
        }
    };
</script>

<style lang="scss" scoped>
    .step {
        background: #fff;
        padding: 20px;
        border-radius: 5px;
    }
    .edit{
        margin-top:10px;
        .title {
            text-align: center;
            font-size: 16px;
            font-weight: bold;
        }
        .left{
            text-align: center;
            background: #fff;
            padding: 20px 20px 0px 20px;
            border-radius: 5px;
            margin-top: 10px;
            position: fixed;
            width: 160px;
            right: 10px;
            top: 100px;
            .el-button{
                margin-bottom: 20px;
            }
            .el-button+.el-button{
                margin-left: 0px;
            }
        }
        .right{
            background: #fff;
            padding: 20px;
            border-radius: 5px;
            .list{
                position: relative;
                padding-bottom: 45px;
                padding-top: 20px;
                border-bottom: 1px solid #e7e7e7;
                .content{
                    .text-message{
                        margin-top: 10px;
                        margin-bottom: 10px;
                        .el-col{
                            text-align: center;
                            color:#E6A23C;
                        }
                        .name{
                            text-align: center;
                        }
                    }
                    .people{
                        padding: 10px 0px;
                        background: #fafafa;
                        .el-col{
                            text-align: center;
                        }
                        .name{
                            text-align: center;
                        }
                        .el-radio{
                            margin-left: 15px;
                        }
                    }
                }
                .edit-content{
                    background: #f8f8f8;
                    border: 1px solid #e7e7e7;
                    padding:20px;
                    margin-top: 10px;
                    .editer{
                        background:#fff;
                    }
                    .must{
                        margin:10px 0px;
                    }
                    .input-title{
                        background: #f3f3f3;
                        padding: 10px 10px;
                        margin-bottom: 10px;
                    }
                    .input{
                        .el-button{
                            position:relative;
                            top:-5px;
                        }
                    }
                }
                .hover{
                    position:absolute;
                    z-index:4;
                    left:0px;
                    bottom: 6px;
                    text-align: right;
                    .el-button{

                    }
                }
            }
        }
    }
    .submit{
        text-align: center;
        padding-top: 20px;
    }
</style>
