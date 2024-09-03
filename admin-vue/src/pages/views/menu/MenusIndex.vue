<template>
    <div>
        <div>
            <el-form :model="formData" inline>
                <el-form-item>
                    <el-select v-model.number="formData.type" placeholder="类型">
                        <el-option :value="0" label="所有"/>
                        <el-option :value="1" label="目录"/>
                        <el-option :value="2" label="菜单"/>
                        <el-option :value="3" label="接口"/>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="formData.name" type="text" placeholder="名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="formData.path" type="text" placeholder="路径"></el-input>
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
                <el-table-column prop="parentName" label="上级名称" width="160" align="left"/>
                <el-table-column prop="typeName" label="类型" width="100" align="center"/>
                <el-table-column prop="icon" label="图标" width="100" align="center">
                    <template #default="scope">
                        <el-icon v-if="scope.row.icon !== ''">
                            <component :is="scope.row.icon"/>
                        </el-icon>
                    </template>
                </el-table-column>
                <el-table-column prop="name" label="名称" width="160" align="left"/>
                <el-table-column prop="path" label="路径" width="260" align="left" :show-overflow-tooltip="true"/>
                <el-table-column prop="sortBy" label="排序值" width="80" align="center"/>
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
                                    <el-dropdown-item v-if="scope.row.state === 0" command="enable">启用</el-dropdown-item>
                                    <el-dropdown-item v-else command="disable">禁用</el-dropdown-item>
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
            <menu-edit v-model="showEditRef" :edit-data="editDataRef" @edit-success="handleEditSuccess"></menu-edit>
        </div>
    </div>
</template>

<script>
export default {
    name: 'MenuIndex'
}
</script>

<script setup>

import {onMounted, reactive, ref} from "vue";

import {ElMessage, ElMessageBox} from "element-plus";

import menuApi from '@/api/menu'

import MenuEdit from "@/pages/views/menu/MenuEdit";

const formData = reactive({
    type: 0,
    name: '',
    path: '',
})

const tableLoadingRef = ref(false)
const tableData = ref([])

function query() {

    const params = {}
    if (formData.type && formData.type !== 0) {
        params.type = formData.type
    }
    if (formData.name && formData.name !== '') {
        params.name = formData.name
    }
    if (formData.path && formData.path !== '') {
        params.path = formData.path
    }

    tableLoadingRef.value = true
    menuApi.query(pagination.value.currentPage, pagination.value.pageSize, params).then(res => {
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
    menuApi.changeState([row.id,], state)
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
            ElMessageBox.confirm('确认删除', '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
                .then(() => {
                    menuApi.delete([row.id,])
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
