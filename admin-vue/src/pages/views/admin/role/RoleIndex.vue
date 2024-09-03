<template>
    <div>
        <div>
            <el-form :model="formData" inline>
                <el-form-item>
                    <el-input v-model="formData.name" type="text" placeholder="名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="handleQueryBtnClick">查询</el-button>
                    <el-button @click="handleCommand('add', null)">添加</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div>
            <el-table v-loading="tableLoadingRef" :data="tableData" empty-text="无数据" height="640">
                <el-table-column type="index" label="序号" width="80" align="center"/>
                <el-table-column prop="name" label="名称" width="200"  align="left"/>
                <el-table-column prop="state" label="状态" width="120" align="center">
                    <template #default="scope">
                        <span :style="{color: scope.row.state === 0 ? '#FF0000' : '#409EFF'}">{{ scope.row.state === 0 ? '禁用' : '启用' }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="remark" label="备注" align="left" :show-overflow-tooltip="true"/>
                <el-table-column prop="updateBy" label="操作人" align="center"/>
                <el-table-column prop="updateTime" label="操作时间" align="center"/>
                <el-table-column fixed="right" label="操作" align="center" width="120">
                    <template #default="scope">
                        <el-dropdown trigger="click" @command="(command) => {handleCommand(command, scope.row)}">
                            <el-button link type="primary" size="default">编辑</el-button>
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item command="detail">详情</el-dropdown-item>
                                    <el-dropdown-item command="modify" :disabled="scope.row.id === 1">修改</el-dropdown-item>
                                    <el-dropdown-item command="authority">权限</el-dropdown-item>
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
            <role-edit v-model="showEditRef" :edit-data="editDataRef" @edit-success="handleEditSuccess"></role-edit>
        </div>
        <div>
            <role-edit-menus v-model="showEditMenusRef" :edit-data="editMenusDataRef"></role-edit-menus>
        </div>
    </div>
</template>

<script>
export default {
    name: 'RoleIndex'
}
</script>

<script setup>

import {ref, reactive, onMounted} from "vue";

import {ElMessage, ElMessageBox} from "element-plus";

import adminApi from '@/api/admin'

import RoleEdit from "@/pages/views/admin/role/RoleEdit";
import RoleEditMenus from "@/pages/views/admin/role/RoleEditMenus";

const formData = reactive({
    name: '',
})

const tableLoadingRef = ref(false)
const tableData = ref([])

function query() {

    const params = {}
    if (formData.name && formData.name !== '') {
        params.name = formData.name
    }

    tableLoadingRef.value = true
    adminApi.roleQuery(pagination.value.currentPage, pagination.value.pageSize, params).then(res => {
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

function handleQueryBtnClick() {
    pagination.value.currentPage = 1
    query()
}

onMounted(() => query())

const editDataRef = ref(null)
const showEditRef = ref(false)

function handleEditSuccess() {
    query()
}

const showEditMenusRef = ref(false)
const editMenusDataRef = ref(undefined)

function changeState(row, state) {
    adminApi.roleChangeState([row.id,], state)
        .then(() => {
            row.state = state
            ElMessage({type: 'success', showClose: true, message: '操作成功'})
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
        case 'authority':
            adminApi.roleMenuQuery(row.id)
                .then((res) => {
                    editMenusDataRef.value = {roleId: row.id, menus: res}
                    showEditMenusRef.value = true
                })
                .catch((e) => {
                    ElMessage({type: 'error', showClose: true, message: e.message})
                })
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
                    adminApi.roleDelete([row.id,])
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

</style>
