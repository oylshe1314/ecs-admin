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
                    <el-input v-model="formData.key" type="text" placeholder="兑换码"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="handleQuery">查询</el-button>
                    <el-button type="primary" @click="handleCommand('add')">添加</el-button>
                    <el-button type="success" @click="handleCommand('generate')">生成</el-button>
                    <template v-if="selectData.rows.length > 0">
                        <el-button type="danger" @click="handleCommand('delete')">删除</el-button>
                    </template>
                </el-form-item>
            </el-form>
        </div>
        <div>
            <el-table v-loading="tableLoadingRef" :data="tableData" @selection-change="handleSelect" empty-text="无数据" height="640">
                <el-table-column type="selection" width="40"/>
                <el-table-column type="index" label="序号" width="80" align="center"/>
                <el-table-column prop="key" label="兑换码" width="200" align="left"/>
                <el-table-column prop="channel" label="渠道" width="200" align="left"/>
                <el-table-column prop="expiration" label="有效期" width="200" align="left"/>
                <el-table-column prop="itemId" label="物品ID" width="400" align="left" :show-overflow-tooltip="true"/>
                <el-table-column prop="itemNum" label="物品数量" align="left" :show-overflow-tooltip="true"/>
            </el-table>
        </div>
        <div>
            <el-pagination v-bind="pagination" style="justify-content: center"></el-pagination>
        </div>
        <div>
            <cdkey-build v-model="showEditRef" :build-type="buildTypeRef" @edit-success="handleEditSuccess"></cdkey-build>
        </div>
    </div>
</template>

<script>
export default {
    name: 'CdkeyIndex'
}
</script>

<script setup>

import {onMounted, reactive, ref} from "vue";

import {ElMessage, ElMessageBox} from "element-plus";

import gameApi from '@/api/game'

import CdkeyBuild from "@/pages/views/game/cdkey/CdkeyBuild";

const formData = reactive({
    channel: 0,
    key: '',
})

const tableLoadingRef = ref(false)
const tableData = ref([])

function query() {
    const params = {}
    if (formData.channel && formData.channel !== 0) {
        params.channel = formData.channel
    }
    if (formData.key && formData.key !== '') {
        params.key = formData.key
    }

    tableLoadingRef.value = true
    gameApi.cdkeyQuery(pagination.value.currentPage, pagination.value.pageSize, params).then(res => {
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

const selectData = reactive({
    rows: [],
})

function handleSelect(rows) {
    selectData.rows = []
    for (const row of rows) {
        selectData.rows.push(row)
    }
}

const showEditRef = ref(false)
const buildTypeRef = ref('add')

function handleEditSuccess() {
    query()
}

function handleCommand(command) {
    switch (command) {
        case 'add':
            buildTypeRef.value = 'add'
            showEditRef.value = true
            break
        case 'generate':
            buildTypeRef.value = 'generate'
            showEditRef.value = true
            break
        case 'delete':
            if (selectData.rows.length === 0) {
                return
            }
            ElMessageBox.confirm('确认删除', '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
                .then(() => {
                    let keys = []
                    for (const row of selectData.rows) {
                        keys.push(row.key)
                    }
                    gameApi.cdkeyDelete(keys)
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
