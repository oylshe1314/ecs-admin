<template>
    <div>
        <div>
            <el-form :model="formData" inline>
                <el-form-item>
                    <el-select v-model.number="formData.roleId" placeholder="角色">
                        <el-option :value="0" label="所有角色"/>
                        <el-option v-for="option in roleSelectOptionsRef" :key="option.id" :value="option.id" :label="option.name"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="formData.username" placeholder="用户名"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="handleQuery">查询</el-button>
                    <el-button @click="handleCommand('add', null)">添加</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div>
            <el-table v-loading="tableLoadingRef" :data="tableData" empty-text="无数据" height="640">
                <el-table-column type="index" label="序号" width="80" align="center"/>
                <el-table-column prop="roleName" label="角色" width="120" align="left"/>
                <el-table-column prop="username" label="用户名" width="160" align="left"/>
                <el-table-column prop="nickname" label="昵称" width="160" align="left"/>
                <el-table-column prop="avatar" label="头像" width="80" align="center">
                    <template #default="scope">
                        <el-popover v-if="scope.row.avatar && scope.row.avatar !==  ''" placement="bottom" trigger="click" width="184px">
                            <img alt="" :src="require('@/assets/images/avatars/' + scope.row.avatar)" style="width: 160px; height: 160px;">
                            <template #reference>
                                <el-button type="primary" link>预览</el-button>
                            </template>
                        </el-popover>
                    </template>
                </el-table-column>
                <el-table-column prop="email" label="邮箱" width="200" align="left"/>
                <el-table-column prop="mobile" label="手机" width="200" align="left"/>
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
                                    <el-dropdown-item command="modify" :disabled="scope.row.id === 1">修改</el-dropdown-item>
                                    <el-dropdown-item v-if="scope.row.state === 0" command="enable" :disabled="scope.row.id === 1">启用</el-dropdown-item>
                                    <el-dropdown-item v-else command="disable" :disabled="scope.row.id === 1">禁用</el-dropdown-item>
                                    <el-dropdown-item command="delete" :disabled="scope.row.id === 1">删除</el-dropdown-item>
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
            <admin-edit v-model="showEditRef" :edit-data="editDataRef" @edit-success="handleEditSuccess"></admin-edit>
        </div>
    </div>
</template>

<script>
export default {
    name: 'AdminIndex'
}
</script>

<script setup>

import {ref, reactive, onMounted} from "vue";

import {ElMessage, ElMessageBox} from "element-plus";

import adminApi from '@/api/admin'
import commonApi from "@/api/common";

import AdminEdit from "@/pages/views/admin/AdminEdit";

const formData = reactive({
    roleId: 0,
    username: '',
})

const roleSelectOptionsRef = ref([])

onMounted(() => {
    commonApi.selectRoles()
        .then((res) => {
            roleSelectOptionsRef.value = res
        })
        .catch((e) => {
            ElMessage({type: 'error', showClose: true, message: '查询角色列表失败: ' + e.message})
        })
})

const tableLoadingRef = ref(false)
const tableData = ref([])

function query() {
    const params = {}
    if (formData.roleId && formData.roleId !== 0) {
        params.roleId = formData.roleId
    }
    if (formData.username && formData.username !== '') {
        params.username = formData.username
    }

    tableLoadingRef.value = true
    adminApi.query(pagination.value.currentPage, pagination.value.pageSize, params).then(res => {
        tableData.value = res.results
        pagination.value.total = res.total
        pagination.value.pageCount = res.pages < 1 ? 1 : res.pages
    }).catch(e => {
        ElMessage({type: 'error', showClose: true, message: e.message})
    }).finally(() => {
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

const showEditRef = ref(false)
const editDataRef = ref(null)

function handleEditSuccess() {
    query()
}

function changeState(row, state) {
    adminApi.changeState([row.id,], state)
        .then(() => {
            ElMessage({type: 'success', showClose: true, message: '操作成功'})
            row.state = state
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
            ElMessageBox.confirm('确认删除', '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
                .then(() => {
                    adminApi.delete([row.id,])
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

<style>
/*别删*/
.cell {
    height: 23px;
}
</style>

<style scoped>

</style>
