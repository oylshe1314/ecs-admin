<template>
    <div>
        <div>
            <el-form :model="formData" inline>
                <el-form-item>
                    <el-input v-model="formData.name" type="text" placeholder="名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="formData.host" type="text" placeholder="地址"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="handleQuery">查询</el-button>
                    <el-button @click="handleCommand('add', null)">添加</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div id="table">
            <el-table v-loading="tableLoadingRef" :data="tableData" empty-text="无数据" height="640">
                <el-table-column type="index" label="序号" width="80" align="center"/>
                <el-table-column prop="name" label="主机名称" width="100" align="left"/>
                <el-table-column prop="host" label="地址" width="140" align="left"/>
                <el-table-column prop="port" label="端口" width="100" align="left"/>
                <el-table-column prop="dir" label="服务目录" width="200" align="left" :show-overflow-tooltip="true"/>
                <el-table-column prop="user" label="主机用户" width="100" align="left"/>
                <el-table-column prop="password" label="主机密码" width="180" align="left"/>
                <el-table-column prop="key" label="主机密钥" width="80" align="center">
                    <template #default="scope">
                        <el-popover v-if="scope.row.key && scope.row.key !==  ''" placement="bottom" trigger="click" width="514px">
                            <div class="hostKey-wrapper">
                                <pre class="hostKey">{{ scope.row.key }}</pre>
                            </div>
                            <template #reference>
                                <el-link type="primary">查看</el-link>
                            </template>
                        </el-popover>
                    </template>
                </el-table-column>
                <el-table-column prop="state" label="状态" width="80" align="center">
                    <template #default="scope">
                        <span :style="{color: scope.row.state === 0 ? '#FF0000' : '#409EFF'}">{{ scope.row.state === 0 ? '禁用' : '启用' }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="remark" label="备注" align="left" :show-overflow-tooltip="true"/>
                <el-table-column prop="updateBy" label="操作人" width="120" align="center"/>
                <el-table-column prop="updateTime" label="操作时间" width="180" align="center"/>
                <el-table-column fixed="right" label="操作" align="center" width="120">
                    <template #default="scope">
                        <el-dropdown trigger="click" @command="(command) => {handleCommand(command, scope.row)}">
                            <el-button link type="primary" size="default">编辑</el-button>
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item command="detail">详情</el-dropdown-item>
                                    <el-dropdown-item command="modify">修改</el-dropdown-item>
                                    <el-dropdown-item v-if="scope.row.state === 0" command="enable" :disabled="scope.row.id === 1">启用</el-dropdown-item>
                                    <el-dropdown-item v-else command="disable" :disabled="scope.row.id === 1">禁用</el-dropdown-item>
                                    <el-dropdown-item command="delete">删除</el-dropdown-item>
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
        <div>
            <host-edit v-model="showEditRef" :edit-data="editDataRef" @edit-success="handleEditSuccess"></host-edit>
        </div>
    </div>
</template>

<script>
export default {
    name: "HostIndex"
}
</script>

<script setup>

import {onMounted, reactive, ref} from "vue";

import {ElMessage, ElMessageBox} from "element-plus";

import serverApi from '@/api/server'

import HostEdit from "@/pages/views/server/host/HostEdit";

const formData = reactive({
    name: '',
    host: '',
})

const tableLoadingRef = ref(false)
const tableData = ref([])

function query() {

    const params = {}
    if (formData.name && formData.name !== '') {
        params.name = formData.name
    }
    if (formData.host && formData.host !== '') {
        params.host = formData.host
    }

    tableLoadingRef.value = true
    serverApi.hostQuery(pagination.value.currentPage, pagination.value.pageSize, params).then(res => {
        tableData.value = res.results
        pagination.value.total = res.total
        pagination.value.pageCount = res.pages < 1 ? 1 : res.pages
    }).catch(e => {
        ElMessage({type: 'error', showClose: true, message: e.message})
    }).finally(() => {
        tableLoadingRef.value = false
    })
}

onMounted(() => query())

const showEditRef = ref(false)
const editDataRef = ref(null)

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
    query()
}

function handleEditSuccess() {
    query()
}

function changeState(row, state) {
    serverApi.hostChangeState([row.id,], state)
        .then(() => {
            row.state = state
            ElMessage({type: 'success', showClose: true, message: '修改成功'})
        })
        .catch((e) => {
            ElMessage({type: 'error', showClose: true, message: e.message})
        })
}

function handleCommand(command, row) {
    switch (command) {
        case 'add':
            editDataRef.value = null
            showEditRef.value = true
            break
        case 'detail':
            break
        case 'modify':
            editDataRef.value = row
            showEditRef.value = true
            break
        case 'enable':
            changeState(row, 1)
            break
        case 'disable':
            ElMessageBox.confirm('确认禁用', '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
                .then(() => {
                    changeState(row, 0)
                })
                .catch(() => {
                })
            break
        case 'delete':
            ElMessageBox.confirm('确认删除菜单', '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
                .then(() => {
                    serverApi.hostDelete([row.id,])
                        .then(() => {
                            query()
                        })
                        .catch((e) => {
                            ElMessage({type: 'error', showClose: true, message: e.message})
                        })
                })
                .catch(() => {
                })
            break
    }
}

</script>

<style scoped>

.hostKey-wrapper {
    width: 490px;
    height: 410px;
    border-radius: 5px;
    overflow: hidden;
    border-style: solid;
    border-width: 1px;
    border-color: #79bbff;
    /*background-color: #f4f4f5;*/
}

.hostKey {
    width: 480px;
    max-width: 480px;
    height: 400px;
    max-height: 400px;
    overflow: auto;
    padding: 5px;
    margin: 0;
}

</style>