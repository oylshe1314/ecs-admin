<template>
    <div>
        <div>
            <el-form :model="formData" inline>
                <el-form-item>
                    <el-input v-model="formData.serverId" placeholder="服务器ID"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-date-picker v-model="formData.date"
                                    type="date"
                                    placeholder="开始时间"
                                    value-format="YYYY-MM-DD"
                                    :disabled-date="beginDisabledDate"
                    />
                </el-form-item>
                <el-form-item>
                    <el-button @click="handleQuery">查询</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div>
            <curve-charts :width="900" :height="360" :labels="labelsRef" :data="dataRef"></curve-charts>
        </div>
    </div>
</template>

<script>
export default {
    name: "OnlineIndex",
}
</script>

<script setup>

import {ref, reactive, onMounted} from 'vue'

import {ElMessage} from "element-plus";

import logApi from "@/api/log";
import {stringToday} from "@/util/date";

import CurveCharts from "@/components/CurveCharts"

const formData = reactive({
    serverId: '',
    date: stringToday(),
})

function beginDisabledDate(time) {
    if (!formData.endDate || formData.endDate === '') {
        return time.getTime() > Date.now()
    } else {
        return time.getTime() > Date.parse(formData.endDate + "T00:00:00")
    }
}

const labelsRef = ref([])
const dataRef = ref([])

function parseEachHour(eachHour, labels, data) {
    if (eachHour && Array.isArray(eachHour)) {
        for (const hour of eachHour) {
            labels.push(hour.hour + '时')
            data.push(hour.online)
        }
    }
}

function query() {
    const params = {}
    if (formData.serverId && formData.serverId !== '') {
        params.serverId = formData.serverId
    }

    logApi.queryOnline(formData.date, params).then(res => {
        const labels = []
        const data = []
        parseEachHour(res.eachHour, labels, data)
        labelsRef.value = labels
        dataRef.value = data
    }).catch(e => {
        ElMessage({type: 'error', showClose: true, message: e.message})
    }).finally(() => {
    })
}

function handleQuery() {
    query()
}

onMounted(() => {
    query()
})

</script>

<style scoped>

</style>