<template>
    <div>
        <div>
            <el-form :model="formData" inline>
                <el-form-item>
                    <el-select v-model.number="formData.channel" placeholder="渠道">
                        <el-option :value="0" label="所有渠道"/>
                        <el-option :value="1" label="微信小游戏"/>
                        <el-option :value="2" label="TapTap"/>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="formData.userId" type="text" placeholder="用户ID"/>
                </el-form-item>
                <el-form-item>
                    <el-button @click="handleQuery">查询</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div>
            <el-table v-loading="tableLoadingRef" :data="tableData" empty-text="无数据" height="640">
                <el-table-column type="index" label="序号" width="80" align="center"/>
                <el-table-column prop="userId" label="用户ID" width="120" align="center"/>
                <el-table-column prop="channelName" label="渠道" width="160" align="left"/>
                <el-table-column prop="username" label="用户名" width="320" align="left"/>
                <el-table-column prop="recentServer" label="最近登录服务器" width="200" align="left"/>
                <el-table-column prop="banLogin" label="封禁" width="80" align="center">
                    <template #default="scope">
                        <span :style="{color: scope.row.banLogin ? '#FF0000' : '#409EFF'}">{{ scope.row.banLogin ? '是' : '否' }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="banLoginTime" label="封禁时间" width="180" align="left"/>
                <el-table-column prop="thirdInfo" label="第三方信息" align="left" :show-overflow-tooltip="true"/>
                <el-table-column fixed="right" label="操作" align="center" width="120">
                    <template #default="scope">
                        <el-dropdown trigger="click" @command="(command) => {handleCommand(command, scope.row)}">
                            <el-button link type="primary" size="default">编辑</el-button>
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item command="kick"><span style="color: #E6A23C">下线</span></el-dropdown-item>
                                    <el-dropdown-item v-if="scope.row.banLogin" command="unban"><span style="color: #529b2e">解封</span></el-dropdown-item>
                                    <el-dropdown-item v-else command="ban"><span style="color: #F56C6C">封禁</span></el-dropdown-item>
                                    <el-dropdown-item v-if="scope.row.channel === 0" command="password"><span style="color: #409EFF">密码</span>
                                    </el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div>
            <el-pagination v-bind="pagination" style="justify-content: center"></el-pagination>
        </div>
    </div>
</template>

<script>
export default {
    name: "UserIndex"
}
</script>

<script setup>

import {ref, reactive, onMounted} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";

import gameApi from '@/api/game'

const formData = reactive({
    channel: 0,
    userId: '',
})

const tableLoadingRef = ref(false)
const tableData = ref([])

function query() {
    const params = {}
    if (formData.channel && formData.channel !== 0) {
        params.channel = formData.channel
    }
    if (formData.userId && formData.userId !== '') {
        params.userId = formData.userId
    }
    tableLoadingRef.value = true
    gameApi.userQuery(pagination.value.currentPage, pagination.value.pageSize, params)
        .then((res) => {
            tableData.value = res.results
            pagination.value.total = res.total
            pagination.value.pageCount = res.pages < 1 ? 1 : res.pages
        })
        .catch((e) => {
            ElMessage({type: 'error', showClose: true, message: e.message})
        })
        .finally(() => {
            tableLoadingRef.value = false
        })
}

const pagination = ref({
        layout: "prev,pager,next",
        background: true,
        currentPage: 1,
        pageSize: 15,
        total: 0,
        pageCount: 1,
        prevIcon: 'CaretLeft',
        nextIcon: 'CaretRight',
        onCurrentChange: (pageNo) => {
            pagination.value.currentPage = pageNo
            query()
        },
        onPrevClick: (pageNo) => {
            if (pageNo > 1) {
                pagination.value.currentPage = pageNo - 1
                query()
            }
        },
        onNextClick: (pageNo) => {
            if (pageNo < pagination.value.pageCount) {
                pagination.value.currentPage = pageNo + 1
                query()
            }
        }
    }
)

function handleQuery() {
    pagination.value.currentPage = 1
    query()
}

onMounted(() => query())

function handleCommand(command, row) {
    let msg = ''
    let operate = 0;
    switch (command) {
        case 'kick':
            operate = 3
            msg = '确认强制玩家下线'
            break
        case 'ban':
            operate = 1
            msg = '确认封禁此账号'
            break
        case 'unban':
            operate = 2
            msg = '确认解除账号封禁'
            break
        case 'password':
            if (row.channel !== 0) {
                ElMessage({type: 'error', showClose: true, message: '第三方渠道账号无法重置密码'})
                return;
            }
            operate = 4
            msg = '确认重置账号密码'
            break
        default:
            return
    }

    const data = {
        userId: row.id,
        operate: operate,
    }

    if (operate === 1) {
        ElMessageBox.prompt(
            msg,
            '警告',
            {
                type: 'warning',
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                inputPlaceholder: '请输入封禁天数，填0或者不填表示永久封禁。',
                inputValidator: (args) => {
                    if (isNaN(Number(args))) {
                        return '请输入正确的封禁天数'
                    }
                    return null
                }
            })
            .then(({value}) => {
                data.args = '0'
                if (value !== '') {
                    data.args = value
                }
                gameApi.userOperate(data)
                    .then(() => {
                        ElMessage({type: 'success', showClose: true, message: "操作成功"})
                        query()
                    })
                    .catch((e) => {
                        ElMessage({type: 'error', showClose: true, message: e.message})
                    })
            })
            .catch(() => {
            })
    } else if (operate === 4) {
        ElMessageBox.prompt(
            msg,
            '警告',
            {
                type: 'warning',
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                inputPlaceholder: '请输入新密码，长度6-16位。',
                inputValidator: (args) => {
                    if (args.length < 6 || args.length > 16) {
                        return '密码长度6-16位'
                    }
                    let pattern = /^[\w!@#$%^&*()-=+\\|{}[\];:'",.<>/?]{6,16}$/
                    if (!pattern.test(args)) {
                        return '密码格式不正确'
                    }
                    return null
                },
            })
            .then(({value}) => {
                if (value !== '') {
                    data.args = value
                }
                gameApi.userOperate(data)
                    .then(() => {
                        ElMessage({type: 'success', showClose: true, message: "操作成功"})
                    })
                    .catch((e) => {
                        ElMessage({type: 'error', showClose: true, message: e.message})
                    })
            })
            .catch(() => {
            })
    } else {
        ElMessageBox.confirm(msg, '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
            .then(() => {
                gameApi.userOperate(data)
                    .then(() => {
                        ElMessage({type: 'success', showClose: true, message: "操作成功"})
                        if (operate === 2) {
                            query()
                        }
                    })
                    .catch((e) => {
                        ElMessage({type: 'error', showClose: true, message: e.message})
                    })
            })
            .catch(() => {
            })
    }
}

</script>

<style scoped>

</style>