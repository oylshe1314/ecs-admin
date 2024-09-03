<template>
    <div>
        <div>
            <el-form :model="formData" inline>
                <el-form-item>
                    <el-input v-model="formData.serverId" placeholder="服务器ID"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="formData.playerId" placeholder="玩家ID"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-date-picker v-model="formData.beginDate"
                                    type="date"
                                    placeholder="开始时间"
                                    value-format="YYYY-MM-DD"
                                    :disabled-date="beginDisabledDate"
                    />
                </el-form-item>
                <el-form-item>
                    <el-date-picker v-model="formData.endDate"
                                    type="date"
                                    placeholder="结束时间"
                                    value-format="YYYY-MM-DD"
                                    :disabled-date="endDisabledDate"
                    />
                </el-form-item>
                <el-form-item>
                    <el-button @click="handleQuery">查询</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div>
            <el-table v-loading="tableLoadingRef" :data="tableData" empty-text="无数据" height="640">
                <el-table-column type="index" label="序号" width="120" align="center"/>
                <el-table-column prop="platform" label="平台" align="center"/>
                <el-table-column prop="serverId" label="服务器ID" align="center"/>
                <el-table-column prop="playerId" label="玩家ID" align="center"/>
                <el-table-column prop="accountName" label="账号" align="center"/>
                <el-table-column prop="roleName" label="角色名" align="center"/>
                <el-table-column prop="logTime" label="注册时间" align="center"/>
                <el-table-column prop="logoffTime" label="最后在线" align="center"/>
                <el-table-column prop="loginIp" label="登录IP" align="center"/>
            </el-table>
        </div>
        <div>
            <el-pagination v-bind="pagination" style="justify-content: center"></el-pagination>
        </div>
    </div>
</template>

<script>
export default {
    name: "CreateIndex"
}
</script>

<script setup>

import {ref, reactive, onMounted} from "vue";

import logApi from '@/api/log'
import {stringToday} from "@/util/date";

import {ElMessage} from "element-plus";

const today = stringToday()
const formData = reactive({
    serverId: '',
    playerId: '',
    beginDate: today,
    endDate: today,
})

function beginDisabledDate(time) {
    if (!formData.endDate || formData.endDate === '') {
        return time.getTime() > Date.now()
    } else {
        return time.getTime() > Date.parse(formData.endDate + "T00:00:00")
    }
}

function endDisabledDate(time) {
    if (!formData.beginDate || formData.beginDate === '') {
        return time.getTime() > Date.now()
    } else {
        return time.getTime() < new Date(formData.beginDate + "T00:00:00") || time.getTime() > Date.now()
    }
}

const tableLoadingRef = ref(false)
const tableData = ref([])

function query() {
    const params = {}
    if (formData.playerId && formData.playerId !== '') {
        params.playerId = formData.playerId
    }
    if (formData.serverId && formData.serverId !== '') {
        params.serverId = formData.serverId
    }
    if (formData.beginDate && formData.beginDate !== '') {
        params.beginDate = formData.beginDate
    }
    if (formData.endDate && formData.endDate !== '') {
        params.endDate = formData.endDate
    }

    tableLoadingRef.value = true
    logApi.queryCreate(pagination.value.currentPage, pagination.value.pageSize, params).then(res => {
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
})

function handleQuery() {
    pagination.value.currentPage = 1
    query()
}

onMounted(() => {
    query()
})

</script>

<style scoped>

</style>