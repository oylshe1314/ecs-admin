<template>
    <div>
        <div>
            <el-form :model="formData" inline>
                <el-form-item>
                    <el-select v-model="formData.typeId" placeholder="服务类型" style="width: 240px">
                        <el-option :value="'0'" label="所有类型"/>
                        <el-option v-for="type in typeSelectOptionsRef" :key="type.id" :value='type.id' :label="type.name"/>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="formData.appId" type="text" placeholder="服务ID"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="handleQuery">查询</el-button>
                    <el-button type="primary" @click="handlePublish">发布</el-button>
                    <el-button type="success" @click="handleControl('start')">启动</el-button>
                    <el-button type="danger" @click="handleControl('stop')">停止</el-button>
                    <el-button type="warning" @click="handleControl('restart')">重启</el-button>
                    <!--                    <el-button type="success" @click="handleControl('reload')">重载</el-button>-->
                </el-form-item>
                <el-form-item>
                    <el-checkbox :model-value="typeChecked.checked"
                                 :indeterminate="typeChecked.indeterminate"
                                 v-for="typeChecked in typesCheckedRef"
                                 v-bind:key="typeChecked.type"
                                 @change="(value) => serverTypeSelect(value, typeChecked)">
                        {{ typeChecked.type }}
                    </el-checkbox>
                </el-form-item>
            </el-form>
        </div>
        <div>
            <el-table
                ref="tableRef"
                v-loading="tableLoadingRef"
                :data="tableData"
                @selection-change="handleSelect"
                empty-text="无数据"
                height="672">
                <el-table-column type="selection" width="40"/>
                <el-table-column type="index" label="序号" width="80" align="center"/>
                <el-table-column prop="typeName" label="服务类型" width="100" align="left"/>
                <el-table-column prop="appId" label="服务ID" width="80" align="center"/>
                <el-table-column prop="svrArea" label="区域" width="100" align="left"/>
                <el-table-column prop="svrName" label="名称" width="120" align="left"/>
                <el-table-column prop="newestVersion" label="最新版本" width="200" align="center">
                    <template #default="scope">
                        <div v-if="Array.isArray(scope.row.newestVersion)">
                            <span :style="{color: '#409EFF'}">
                                {{ scope.row.newestVersion[0] }}
                            </span>
                            -
                            <span :style="{color: '#409EFF'}">
                                {{ scope.row.newestVersion[1] }}
                            </span>
                            -
                            <span :style="{color: '#409EFF'}">
                                {{ scope.row.newestVersion[2] }}
                            </span>
                        </div>
                        <div v-else>
                            <span>{{ scope.row.newestVersion }}</span>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="publishedVersion" label="发布版本" width="200" align="center">
                    <template #default="scope">
                        <div v-if="Array.isArray(scope.row.publishedVersion)">
                            <span :style="{color: scope.row.publishedVersion[0] !== scope.row.newestVersion[0] ? '#FF0000' : '#409EFF'}">
                                {{ scope.row.publishedVersion[0] }}
                            </span>
                            -
                            <span :style="{color: scope.row.publishedVersion[1] !== scope.row.newestVersion[1] ? '#FF0000' : '#409EFF'}">
                                {{ scope.row.publishedVersion[1] }}
                            </span>
                            -
                            <span :style="{color: scope.row.publishedVersion[2] !== scope.row.newestVersion[2] ? '#FF0000' : '#409EFF'}">
                                {{ scope.row.publishedVersion[2] }}
                            </span>
                        </div>
                        <div v-else>
                            <span>{{ scope.row.publishedVersion }}</span>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="runningVersion" label="运行版本" width="200" align="center">
                    <template #default="scope">
                        <div v-if="Array.isArray(scope.row.runningVersion)">
                            <span :style="{color: scope.row.runningVersion[0] !== scope.row.newestVersion[0] ? '#FF0000' : '#409EFF'}">
                                {{ scope.row.runningVersion[0] }}
                            </span>
                            -
                            <span :style="{color: scope.row.runningVersion[1] !== scope.row.newestVersion[1] ? '#FF0000' : '#409EFF'}">
                                {{ scope.row.runningVersion[1] }}
                            </span>
                            -
                            <span :style="{color: scope.row.runningVersion[2] !== scope.row.newestVersion[2] ? '#FF0000' : '#409EFF'}">
                                {{ scope.row.runningVersion[2] }}
                            </span>
                        </div>
                        <div v-else>
                            <span>{{ scope.row.runningVersion }}</span>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="status" label="状态" width="80" align="center">
                    <template #default="scope">
                        <span :style="{color: scope.row.status === 0 ? '#FF0000' : '#529b2e'}">{{ scope.row.status === 0 ? '已关闭' : '运行中' }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="pid" label="PID" width="80" align="left"/>
                <el-table-column prop="cpu" label="CPU" width="80" align="left"/>
                <el-table-column prop="memory" label="内存" width="80" align="left"/>
                <el-table-column prop="online" label="在线" width="80" align="left"/>
                <el-table-column prop="coroutine" label="协程" width="80" align="left"/>
                <el-table-column prop="info" label="信息" :show-overflow-tooltip="true"/>
            </el-table>
        </div>
    </div>
</template>

<script>
export default {
    name: "ServiceIndex"
}
</script>

<script setup>

import {onMounted, reactive, ref} from "vue";

import {ElMessage, ElMessageBox} from "element-plus";

import serverApi from '@/api/server'
import commonApi from "@/api/common";

const typeSelectOptionsRef = ref([])

const formData = reactive({
    typeId: '0',
    appId: '',
})

const tableRef = ref()
const tableLoadingRef = ref(false)
const tableData = ref([])
const typesCheckedRef = ref([])

function setServerTypes(res) {
    const map = new Map();
    for (const item of res) {
        let count = map.get(item.typeName)
        if (!count) {
            count = 0
        }
        map.set(item.typeName, count + 1)
    }

    const ary = [];
    map.forEach((value, key) => {
        ary.push({
            type: String(key),
            checked: false,
            indeterminate: false,
            count: Number(value),
            selected: 0,
        })
    })

    ary.sort((a, b) => {
        return a.type.localeCompare(b.type)
    })
    typesCheckedRef.value = ary
}

function serverTypeSelect(value, typeChecked) {
    if (typeChecked.indeterminate) {
        value = true
    } else if (typeChecked.checked) {
        value = false
    }
    for (const row of tableData.value) {
        if (row.typeName === typeChecked.type) {
            tableRef.value.toggleRowSelection(row, value)
        }
    }
}

// async function requestDetect(row) {
//     row.runningVersion = '诊断中'
//     setTimeout(function () {
//         serverApi.serviceDetect(row.configId)
//             .then(res => {
//                 row.runningVersion = res.runningVersion
//                 row.status = res.status
//                 row.pid = res.pid
//                 row.cpu = res.cpu
//                 row.memory = res.memory
//                 row.online = res.online
//                 row.coroutine = res.coroutine
//                 row.info = res.info
//             })
//             .catch((e) => {
//                 row.runningVersion = '诊断失败'
//                 ElMessage({type: 'error', showClose: true, message: e.message})
//             })
//     }, 1000)
// }

function query() {
    const params = {}
    if (formData.typeId && formData.typeId !== '0') {
        params.typeId = formData.typeId
    }
    if (formData.appId && formData.appId !== '') {
        params.appId = Number(formData.appId)
        if (isNaN(params.appId) || params.appId < 1 || params.appId > 999 || (params.appId % 1) !== 0) {
            ElMessage({type: 'error', showClose: true, message: "服务ID必须是1-999的整数"})
            return
        }
    }

    tableLoadingRef.value = true
    serverApi.serviceList(params).then(res => {
        setServerTypes(res)
        tableData.value = res
        // for (const row of tableData.value) {
        //     requestDetect(row)
        // }
    }).catch(e => {
        ElMessage({type: 'error', showClose: true, message: e.message})
    }).finally(() => {
        tableLoadingRef.value = false
    })
}

function queryTypes() {
    commonApi.selectServerTypes()
        .then((types) => {
            typeSelectOptionsRef.value = types
        })
        .catch((e) => {
            ElMessage({type: 'error', showClose: true, message: '查询服务类型失败: ' + e.message})
        })
}

onMounted(() => {
    queryTypes()
    query()
})

function handleQuery() {
    query()
}

const selectData = reactive({
    rows: [],
})

function handleSelect(rows) {
    const selectedRows = []
    const map = new Map()
    for (const row of rows) {
        selectedRows.push(row)

        let selected = map.get(row.typeName)
        if (!selected) {
            selected = 0
        }
        map.set(row.typeName, selected + 1)
    }

    selectData.rows = selectedRows

    for (const typeChecked of typesCheckedRef.value) {
        typeChecked.checked = false
        typeChecked.indeterminate = false
        typeChecked.selected = 0

        const selected = map.get(typeChecked.type)
        if (selected) {
            typeChecked.checked = true
            typeChecked.selected = selected
            typeChecked.indeterminate = typeChecked.selected > 0 && typeChecked.selected < typeChecked.count
        }
    }
}

async function requestPublish(i, rows) {
    const row = rows[i]
    row.publishedVersion = "发布中"
    serverApi.servicePublish(row.configId)
        .then((res) => {
            row.publishedVersion = res
            ElMessage({type: 'success', showClose: true, message: '发布成功'})
            if (i + 1 < rows.length) {
                requestPublish(i + 1, rows)
            }
        })
        .catch((e) => {
            row.publishedVersion = '发布失败'
            ElMessage({type: 'error', showClose: true, message: e.message})
        })
}

function handlePublish() {
    const selectedRow = selectData.rows
    if (!selectedRow || selectedRow.length === 0) {
        ElMessage({type: 'error', showClose: true, message: '请选择服务再操作'})
        return
    }

    ElMessageBox.confirm('确认发布服务', '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
        .then(() => {
            requestPublish(0, selectedRow)
        })
        .catch(() => {
        })
}

async function requestControl(i, rows, operate, command) {
    const row = rows[i]
    let thisOperate = operate
    let thisCommand = command
    switch (operate) {
        case 1:
            if (row.status === 1) {
                if (i + 1 < rows.length) {
                    requestControl(i + 1, rows, operate, command)
                }
                return
            }
            break
        case 2:
            if (row.status === 0) {
                if (i + 1 < rows.length) {
                    requestControl(i + 1, rows, operate, command)
                }
                return;
            }
            break
        case 3:
            if (row.status === 0) {
                thisOperate = 1
                thisCommand = '启动'
            }
            break
    }

    row.runningVersion = command + '中'
    serverApi.serviceControl(row.configId, thisOperate)
        .then((res) => {
            row.runningVersion = res.runningVersion
            row.status = res.status
            row.pid = res.pid
            row.cpu = res.cpu
            row.memory = res.memory
            row.online = res.online
            row.coroutine = res.coroutine
            row.info = res.info
            ElMessage({type: 'success', showClose: true, message: command + '成功'})
            if (i + 1 < rows.length) {
                requestControl(i + 1, rows, operate, command)
            }
        })
        .catch((e) => {
            row.runningVersion = command + '失败'
            ElMessage({type: 'error', showClose: true, message: e.message})
        })
}

function handleControl(command) {
    const selectedRow = selectData.rows
    if (!selectedRow || selectedRow.length === 0) {
        ElMessage({type: 'error', showClose: true, message: '请选择服务再操作'})
        return
    }

    let operate = 0
    switch (command) {
        case 'start':
            operate = 1
            command = '启动'
            break
        case 'stop':
            operate = 2
            command = '停止'
            break
        case 'restart':
            operate = 3
            command = '重启'
            break
        default:
            return
    }

    ElMessageBox.confirm('确认' + command + '服务', '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
        .then(() => {
            requestControl(0, selectedRow, operate, command)
        })
        .catch(() => {
        })
}

</script>

<style scoped>
.version {
    font-size: 13px;
}
</style>