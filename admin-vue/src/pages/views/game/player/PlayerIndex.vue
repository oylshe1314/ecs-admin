<template>
    <div class="gateSetting">
        <div>
            <el-form :model="formData" inline>
                <el-form-item>
                    <el-input v-model="formData.appId" type="text" placeholder="服务ID"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="formData.playerId" type="text" placeholder="玩家ID"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="handleQuery">查询</el-button>
                    <el-button type="warning" @click="handleOperate('kick')" :disabled="dataRef.length === 0">下线</el-button>
                    <el-button type="danger" @click="handleOperate('ban')" :disabled="dataRef.length === 0">封禁</el-button>
                    <el-button type="success" @click="handleOperate('unban')" :disabled="dataRef.length === 0">解封</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div id="tree" class="player-tree">
            <input v-model="filterData.value" type="text" class="player-filter" @input="handleFilter"/>
            <el-tree-v2 ref="treeRef"
                        :data="dataRef"
                        :props="treeProps"
                        :default-expanded-keys="['Player']"
                        :height="630"
                        empty-text="无数据"
                        :filter-method="filterMethod"
            />
        </div>
    </div>
</template>

<script>
export default {
    name: 'PlayerIndex'
}
</script>

<script setup>

import {ref, reactive} from "vue";

import {ElMessage, ElMessageBox} from "element-plus";
import {closeLoading, openLoading} from "@/util/loading";

import gameApi from '@/api/game'

const formData = reactive({
    appId: '',
    playerId: '',
})

const treeProps = {
    value: 'value',
    label: 'label',
    children: 'children'
}

const treeRef = ref()
const dataRef = ref([])

function handleQuery() {
    if (!formData.appId || formData.appId === '') {
        ElMessage({type: 'error', showClose: true, message: '请输入服务ID'})
        return
    }
    if (!formData.playerId || formData.playerId === '') {
        ElMessage({type: 'error', showClose: true, message: '请输入玩家ID'})
        return
    }

    openLoading('#tree', "查询中...")
    gameApi.playerQuery(formData.appId, formData.playerId)
        .then((res) => {
            const data = []
            if (res.player) {
                const root = {value: 'Player', label: "Player:", children: []}
                transToTreeData(root.value, res.player, root)
                data.push(root)
            }
            dataRef.value = data
        })
        .catch((e) => {
            console.log(e)
            ElMessage({type: 'error', showClose: true, message: e.message})
        })
        .finally(() => {
            closeLoading()
        })
}

function transToTreeData(key, obj, node) {
    if (Array.isArray(obj)) {
        for (const i in obj) {
            const child = {}
            const si = String(i)
            const val = obj[i]
            if (val instanceof Object) {
                child.value = key + '.' + si
                child.label = si + ':'
                child.children = []
                transToTreeData(child.value, val, child)
            } else {
                child.value = key + '.' + si
                child.label = si + ': ' + (typeof val === 'string' ? '"' + val + '"' : String(val))
            }
            node.children.push(child)
        }
    } else {
        for (const k in obj) {
            const child = {}
            const sk = String(k)
            const val = obj[k]
            if (val instanceof Object) {
                child.value = key + '.' + sk
                child.label = sk + ':'
                child.children = []
                transToTreeData(child.value, val, child)
            } else {
                child.value = key + '.' + sk
                child.label = sk + ': ' + (typeof val === 'string' ? '"' + val + '"' : String(val))
            }
            node.children.push(child)
        }
    }
}

const filterData = reactive({
    value: ''
})

function handleFilter() {
    treeRef.value.filter(filterData.value)
    if (filterData.value === '') {
        treeRef.value.setExpandedKeys(['Player'])
    }
}

function filterMethod(query, node) {
    if (query === '') {
        return true
    }
    return node.value.includes(query)
}

function handleOperate(command) {
    let msg = ''
    let operate = 0;
    switch (command) {
        case 'kick':
            operate = 3
            msg = '确认强制玩家下线'
            break
        case 'ban':
            operate = 1
            msg = '确认封禁此玩家'
            break
        case 'unban':
            operate = 2
            msg = '确认解除玩家封禁'
            break
        default:
            return
    }

    const data = {
        appId: formData.appId,
        playerId: formData.playerId,
        operate: operate,
        args: 0,
    }

    if (operate !== 1) {
        ElMessageBox.confirm(msg, '警告', {type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消'})
            .then(() => {
                gameApi.playerOperate(data)
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
        ElMessageBox.prompt(msg, '警告', {
            type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消',
            inputPlaceholder: '请输入封禁天数，填0或者不填表示永久封禁。',
            inputValidator: (args) => {
                if (isNaN(Number(args))) {
                    return '请输入正确的封禁天数'
                }
                return null
            }
        })
            .then(({value}) => {
                if (value !== '') {
                    data.args = Number(value)
                }
                gameApi.playerOperate(data)
                    .then(() => {
                        ElMessage({type: 'success', showClose: true, message: "操作成功"})
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

.gateSetting {
    width: 100%;
    height: 100%;
    /*display: flex;*/
    /*justify-content: center;*/
}

.player-filter {
    width: 702px;
    height: 30px;
    border-top: none;
    border-left: none;
    border-right: none;
    border-bottom: #DCDFE6 solid 1px;
    outline: none;
    padding-left: 10px;
    padding-right: 10px;
}

.player-tree {
    width: 722px;
    height: 672px;
    /*background: pink;*/
    /*position: relative;*/
    /*top: 200px;*/
    /*left: 50%;*/
    /*margin: 180px 0 0 -180px;*/
    /*padding: 40px 40px 40px 40px;*/
    border: #DCDFE6 solid 1px;
    border-radius: 3px;
}

</style>
